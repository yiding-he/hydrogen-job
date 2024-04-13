package com.hyd.job.server.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageData<T> {

  private int recordsTotal;

  private int recordsFiltered;

  private List<T> data;
}
