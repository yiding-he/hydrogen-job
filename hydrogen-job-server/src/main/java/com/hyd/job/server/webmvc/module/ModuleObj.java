package com.hyd.job.server.webmvc.module;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ModuleObj {

    private Long id;

    private String moduleName;

    private String modulePath;

    private String status;

    private String icon;
}
