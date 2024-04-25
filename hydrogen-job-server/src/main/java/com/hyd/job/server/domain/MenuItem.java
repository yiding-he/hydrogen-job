package com.hyd.job.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MenuItem {

    private String name;

    private boolean active;

    private List<MenuItem> subItems;
}
