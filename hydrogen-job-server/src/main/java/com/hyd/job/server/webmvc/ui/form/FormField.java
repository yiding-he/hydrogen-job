package com.hyd.job.server.webmvc.ui.form;

import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.commons.collections4.KeyValue;

import java.util.List;

@Data
@Accessors(chain = true)
public class FormField {

    private String name;

    private String label;

    private String value;

    private String placeHolder;

    private List<KeyValue<String, String>> options;

    private FieldType fieldType;
}
