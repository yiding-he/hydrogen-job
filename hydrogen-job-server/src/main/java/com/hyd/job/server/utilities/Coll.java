package com.hyd.job.server.utilities;

import java.util.List;

public class Coll {

  /**
   * 返回一个子列表，并处理各种参数情况：
   * 1. 如果 list 为 null，则返回 null
   * 2. 如果 list 为空，则返回 list
   * 3. fromIndex 的最小值限制为 0，toIndex 的最大值限制为列表长度
   * 4. 如果 fromIndex 大于 toIndex，则交换两个值
   * 5. 如果 toIndex 小于 0，则视其为倒数位置，如 -1 表示最后一个元素，-2 表示倒数第二个元素，以此类推
   */
  public static <T> List<T> subList(List<T> list, int fromIndex, int toIndex) {
    if (list == null) return null;
    if (list.isEmpty()) return list;
    if (fromIndex < 0) fromIndex = 0;
    if (toIndex > list.size()) toIndex = list.size();
    if (toIndex < 0) toIndex = list.size() + 1 + toIndex;
    if (fromIndex > toIndex)
      return list.subList(toIndex, fromIndex);
    else
      return list.subList(fromIndex, toIndex);
  }
}
