package com.hyd.job.server.webmvc.module;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 用于查询指定 ModuleOperation 实例的类
 */
@Component
public class ModuleOperationRegistry {

    private List<ModuleOperation> moduleOperations;

    @Autowired
    public void setModuleOperations(List<ModuleOperation> moduleOperations) {
        this.moduleOperations = moduleOperations;
    }

    public ModuleOperation findModuleOperation(Class<? extends ModuleOperation> type) {
        return moduleOperations.stream()
            .filter(mo -> type.isAssignableFrom(mo.getClass()))
            .findFirst()
            .orElse(null);
    }

    public ModuleOperation findModuleOperation(String module, String operation) {
        return moduleOperations.stream()
            .filter(mo -> mo.getModulePath().equals(module) && mo.getOperation().equals(operation))
            .findFirst()
            .orElse(null);
    }
}
