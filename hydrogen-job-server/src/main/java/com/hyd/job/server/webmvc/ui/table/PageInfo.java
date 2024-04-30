package com.hyd.job.server.webmvc.ui.table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageInfo {

    private int index;

    private int limit;

    private int total;

    private int totalPages;
}
