package com.hyd.job.server.webmvc.ui.form;

import com.hyd.job.server.webmvc.ui.Component;
import com.hyd.job.server.webmvc.ui.button.Button;
import com.hyd.job.server.webmvc.ui.button.ButtonType;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.KeyValue;
import org.springframework.http.HttpMethod;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 表单
 */
@Data
@Accessors(chain = true)
public class Form extends Component {

    /**
     * 表单提交地址
     */
    private String action;

    /**
     * 表单提交方式
     */
    private HttpMethod method;

    /**
     * 表单最上面显示的消息内容
     */
    private String message;

    /**
     * 表单标题
     */
    private String title;

    /**
     * 表单按钮，默认是“提交”、“返回”两个按钮
     */
    private List<Button> buttons = new ArrayList<>(Arrays.asList(
        Button.submit(), Button.back()
    ));

    /**
     * 表单字段
     */
    private List<FormField> fields = new ArrayList<>();

    public Form setSubmitButtonText(String submitButtonText) {
        buttons.stream()
            .filter(b -> b.getButtonType() == ButtonType.SubmitForm)
            .findFirst()
            .ifPresent(b -> b.setButtonText(submitButtonText));
        return this;
    }

    public Form removeButton(ButtonType buttonType) {
        buttons.removeIf(b -> b.getButtonType() == buttonType);
        return this;
    }

    public Form clearButtons() {
        buttons.clear();
        return this;
    }

    public Form addField(FormField formField) {
        this.fields.add(formField);
        return this;
    }

    public Form addField(String label, String name, FieldType type) {
        return addField(label, name, null, type);
    }

    public Form addHiddenField(String name, Object value) {
        return addField("", name, String.valueOf(value), FieldType.Hidden);
    }

    public Form addField(String label, String name, String value, FieldType type) {
        FormField field = new FormField();
        field.setLabel(label);
        field.setName(name);
        field.setValue(value);
        field.setFieldType(type);
        this.fields.add(field);
        return this;
    }

    public Form addField(String label, String name, List<KeyValue<String, String>> options) {
        return addField(label, name, null, options);
    }

    public Form addField(String label, String name, String value, List<KeyValue<String, String>> options) {
        FormField field = new FormField();
        field.setLabel(label);
        field.setName(name);
        field.setOptions(options);
        field.setFieldType(FieldType.Select);
        field.setValue(value);
        this.fields.add(field);
        return this;
    }
}
