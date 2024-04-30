package com.hyd.job.server.webmvc.ui.button;

import lombok.Data;

@Data
public class Button {

    public static Button back() {
        Button button = new Button();
        button.setButtonText("返回");
        button.setButtonType(ButtonType.HistoryBack);
        return button;
    }

    public static Button submit() {
        return submit("提交");
    }

    public static Button submit(String buttonText) {
        Button button = new Button();
        button.setButtonText(buttonText);
        button.setButtonType(ButtonType.SubmitForm);
        return button;
    }

    public static Button navigate(String buttonText, String url) {
        Button button = new Button();
        button.setButtonText(buttonText);
        button.setButtonType(ButtonType.NavigateUrl);
        button.setUrl(url);
        return button;
    }

    /**
     * 按钮文本
     */
    private String buttonText;

    /**
     * 按钮类型
     */
    private ButtonType buttonType;

    /**
     * 跳转地址，仅当 buttonType 为 NavigateUrl 时有效
     */
    private String url;
}
