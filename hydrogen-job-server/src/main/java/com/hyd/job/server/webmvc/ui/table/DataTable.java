package com.hyd.job.server.webmvc.ui.table;

import com.hyd.job.server.webmvc.ui.Component;
import com.hyd.job.server.webmvc.ui.op.Operation;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Data
@Accessors(chain = true)
public class DataTable extends Component {

    private List<Column> columns = new ArrayList<>();

    private List<Operation> operations = new ArrayList<>();

    private List<Map<String, Object>> rows = new ArrayList<>();

    private String conditions;

    private PageInfo pageInfo;

    private String addButtonText = "添加";  // 如果不想显示添加按钮，将这个属性设为 null

    public DataTable addColumn(String propertyName, String label) {
        return addColumn(propertyName, label, "");
    }

    public DataTable addColumn(String propertyName, String label, String width) {
        Column column = new Column();
        column.setLabel(label);
        column.setPropertyName(propertyName);
        column.setWidth(width);
        this.columns.add(column);
        return this;
    }

    public DataTable addOperation(String name, String code) {
        this.operations.add(new Operation(name, code));
        return this;
    }

    public DataTable addOperation(String name, String code, String confirm) {
        this.operations.add(new Operation(name, code, confirm));
        return this;
    }

    public Row<String, Object> addRow(Serializable id) {
        Row<String, Object> map = new Row<>();
        map.put("id", id);
        this.rows.add(map);
        return map;
    }

    public DataTable setPageInfo(int index, int limit, int total) {
        this.pageInfo = new PageInfo(index, limit, total, (total + limit - 1) / limit);
        return this;
    }
}
