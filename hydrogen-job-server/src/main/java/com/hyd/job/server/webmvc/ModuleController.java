package com.hyd.job.server.webmvc;

import com.hyd.job.server.domain.User;
import com.hyd.job.server.mapper.LogMapper;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import com.hyd.job.server.webmvc.module.ModuleOperationRegistry;
import com.hyd.job.server.webmvc.module.PrivilegeService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttribute;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.servlet.ModelAndView;

import java.util.Collection;
import java.util.Map;

@Controller
@RequestMapping("/admin/modules")
@SessionAttributes(names = {"user"}, types = {User.class})
public class ModuleController {

    @Autowired
    private PrivilegeService privilegeService;

    @Autowired
    private ModuleOperationRegistry moduleOperationRegistry;

    @Autowired
    private LogMapper logMapper;

    @RequestMapping("{modulePath}/{operation}")
    public ModelAndView moduleGetAction(
        @PathVariable("modulePath") String modulePath,
        @PathVariable("operation") String operation,
        @SessionAttribute("user") User user,
        HttpServletRequest request,
        HttpServletResponse response
    ) throws Exception {

        ExtendedModelMap model = new ExtendedModelMap();
        RequestContext requestContext = new RequestContext(request, response, model, user);

        if (!privilegeService.checkPrivilege(user.getUserId(), modulePath, operation)) {
            requestContext.addMessage("权限不足", "对不起，您没有该操作的权限。", "index");
            return new ModelAndView("module", model);
        }

        ModuleOperation moduleOperation = moduleOperationRegistry.findModuleOperation(modulePath, operation);

        if (moduleOperation != null) {
            requestContext.setCurrentOperation(moduleOperation);
            executeModuleOperation(user, moduleOperation, requestContext, true);

            if (requestContext.getNextOperation() != null) {
                // 如果指定了下一个 ModuleOperation，则执行指定的 ModuleOperation
                moduleOperation = moduleOperationRegistry.findModuleOperation(requestContext.getNextOperation());
                if (moduleOperation != null) {
                    return new ModelAndView(
                        "redirect:/modules/" + moduleOperation.getModulePath() + "/" + moduleOperation.getOperation()
                    );
                }
            } else if (model.isEmpty() || isModelAttributeEmpty(model, "components")) {
                // 如果没有指定下一个，操作也没有任何返回值，则返回 index
                moduleOperation = moduleOperationRegistry.findModuleOperation(modulePath, "index");
                if (moduleOperation != null) {
                    return new ModelAndView("redirect:index");
                }
            }

            return new ModelAndView("/admin/module", model);

        } else {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
    }

    private void executeModuleOperation(
        User sessionUser, ModuleOperation moduleOperation, RequestContext requestContext, boolean addLog
    ) throws Exception {
        if (!requestContext.isGetMethod() && addLog) {
            logMapper.addOperationLog(sessionUser, moduleOperation, requestContext.getHttpServletRequest());
        }
        moduleOperation.execute(requestContext);
    }


    /**
     * 判断 Model 属性值是否不存在或是一个空集合
     *
     * @return 如果属性值不存在或者是一个空集合，则返回 true
     */
    @SuppressWarnings("RedundantIfStatement")
    private boolean isModelAttributeEmpty(Model model, String attrName) {
        if (!model.containsAttribute(attrName)) {
            return true;
        }

        Object value = model.getAttribute(attrName);
        if (value == null) {
            return true;
        }

        if (value instanceof Collection) {
            if (((Collection<?>) value).isEmpty()) {
                return true;
            }
        }

        if (value instanceof Map) {
            if (((Map<?, ?>) value).isEmpty()) {
                return true;
            }
        }

        return false;
    }
}
