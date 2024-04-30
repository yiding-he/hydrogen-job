package com.hyd.job.server.webmvc;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.hyd.job.server.domain.User;
import com.hyd.job.server.webmvc.module.ModuleOperation;
import com.hyd.job.server.webmvc.ui.Component;
import com.hyd.job.server.webmvc.ui.button.ButtonType;
import com.hyd.job.server.webmvc.ui.form.Form;
import com.hyd.job.server.webmvc.ui.table.DataTable;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpMethod;
import org.springframework.ui.Model;

import java.util.*;

import static org.springframework.util.StringUtils.hasText;

/**
 * 用于构造返回页面内容的上下文对象
 */
public class RequestContext {

    private final HttpServletRequest httpServletRequest;

    private final HttpServletResponse httpServletResponse;

    private final Model model;

    private final User sessionUser;

    private final Map<String, List<String>> pageConditions = new HashMap<>();

    private ModuleOperation currentOperation;

    private Class<? extends ModuleOperation> nextOperation;

    public RequestContext(
        HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
        Model model, User sessionUser
    ) {
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.model = model;
        this.sessionUser = sessionUser;

        this.model.addAttribute("components", new ArrayList<>());

        // 解析分页查询中的一般条件
        // 如果存在 conditions 参数，则取该参数内容，否则取 request 全部参数，
        // 然后在创建表格的时候放入表格的 conditions 属性，那么该表格翻页的时候
        // 请求里面就会带上 conditions 参数了。
        if (isGetMethod()) {
            if (httpServletRequest.getParameter("conditions") != null) {
                JSONObject conditionsObj = JSON.parseObject(httpServletRequest.getParameter("conditions"));
                conditionsObj.forEach((key, values) -> {
                    List<String> valuesList = ((JSONArray) values).toJavaList(String.class);
                    pageConditions.put(key, valuesList);
                });
            } else {
                httpServletRequest.getParameterMap().forEach((key, value) -> {
                    if (key.equals("index") || key.equals("limit")) {
                        return;
                    }
                    List<String> valuesList = Arrays.asList(value);
                    pageConditions.put(key, valuesList);
                });
            }
        }
    }

    /**
     * 向返回页面添加一个组件
     *
     * @param component 组件
     */
    @SuppressWarnings("unchecked")
    private <T extends Component> T addComponent(T component) {
        List<Object> list = (List<Object>) this.model.getAttribute("components");
        if (list != null) {
            list.add(component);
        }
        return component;
    }

    /**
     * 设置下一个 ModuleOperation，用于没有查询结果的操作跳转到下一个界面，例如删除操作成功后跳转到列表页。
     * 注意不要造成循环跳转。
     */
    public void navigateTo(Class<? extends ModuleOperation> nextOperation) {
        this.nextOperation = nextOperation;
    }

    public Class<? extends ModuleOperation> getNextOperation() {
        return nextOperation;
    }

    public ModuleOperation getCurrentOperation() {
        return currentOperation;
    }

    public void setCurrentOperation(ModuleOperation currentOperation) {
        this.currentOperation = currentOperation;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    public boolean isGetMethod() {
        return httpServletRequest.getMethod() != null &&
            httpServletRequest.getMethod().equalsIgnoreCase("get");
    }

    //////////////////////////////////////////////////////////////

    public Form addConfirm(String title, String message, String key) {
        return addComponent(
            new Form()
                .setAction(currentOperation.getOperation())
                .setMethod(HttpMethod.POST)
                .setTitle(title)
                .setMessage(message)
                .setSubmitButtonText("确定")
                .addHiddenField("id", key)
        );
    }

    public Form addForm(String action, HttpMethod method, String title) {
        return addComponent(
            new Form()
                .setAction(action)
                .setMethod(method)
                .setTitle(title)
        );
    }

    public DataTable addTable() {
        DataTable dataTable = new DataTable();
        addComponent(dataTable);

        if (this.pageConditions != null) {
            dataTable.setConditions(JSON.toJSONString(this.pageConditions));
        }

        return dataTable;
    }

    public Form addMessage(String title, String message) {
        return addMessage(title, message, null, "").clearButtons();
    }

    public Form addMessage(String title, String message, String nextOperation) {
        return addMessage(title, message, "确定", nextOperation);
    }

    public Form addMessage(String title, String message, String submitButtonText, String nextOperation) {
        return addComponent(
            new Form()
                .setAction(nextOperation)
                .setMethod(HttpMethod.GET)
                .setTitle(title)
                .setMessage(message)
                .setSubmitButtonText(submitButtonText)
                .removeButton(ButtonType.HistoryBack)
        );
    }

    //////////////////////////////////////////////////////////////

    private static final List<String> EMPTY_PARAM_VALUE = Collections.singletonList(null);

    public String getParameter(String paramName) {
        String value = httpServletRequest.getParameter(paramName);
        if (!hasText(value) && this.pageConditions != null) {
            value = this.pageConditions.getOrDefault(paramName, EMPTY_PARAM_VALUE).get(0);
        }
        if (!hasText(value)) {
            throw new IllegalArgumentException("参数 '" + paramName + "' 不能为空");
        }
        return value;
    }

    public String getParameter(String paramName, String defaultValue) {
        String value = httpServletRequest.getParameter(paramName);
        if (!hasText(value) && this.pageConditions != null) {
            value = this.pageConditions.getOrDefault(paramName, EMPTY_PARAM_VALUE).get(0);
        }
        if (!hasText(value)) {
            return defaultValue;
        }
        return value;
    }

    public Long getParameterLong(String paramName) {
        return Long.valueOf(getParameter(paramName));
    }

    public Long getParameterLong(String paramName, long defaultValue) {
        return Long.valueOf(getParameter(paramName, String.valueOf(defaultValue)));
    }

    public Integer getParameterInt(String paramName) {
        return Integer.valueOf(getParameter(paramName));
    }

    public Integer getParameterInt(String paramName, int defaultValue) {
        return Integer.valueOf(getParameter(paramName, String.valueOf(defaultValue)));
    }
}
