package com.hyd.job.server.webmvc.module;

import com.hyd.job.server.webmvc.RequestContext;
import org.apache.commons.collections4.KeyValue;
import org.apache.commons.collections4.keyvalue.DefaultKeyValue;

import java.util.ArrayList;
import java.util.List;

/**
 * 模块操作的入口
 */
public abstract class ModuleOperation {

    public abstract String getModulePath();

    public abstract String getOperation();

    public abstract void execute(RequestContext requestContext) throws Exception;

    /**
     * 给选项列表补充一项
     *
     * @param list     现有列表
     * @param key      选项的参数值
     * @param value    选项的文本
     * @param position 插入位置，-1表示末尾
     *
     * @return 补充选项后的新列表
     */
    protected List<KeyValue<String, String>> addOption(
        List<KeyValue<String, String>> list, String key, String value, int position) {

        if (position < 0) {
            position = list.size() + 1 + position;
        }

        List<KeyValue<String, String>> result = new ArrayList<>(list);
        result.add(position, new DefaultKeyValue<>(key, value));
        return result;
    }
}
