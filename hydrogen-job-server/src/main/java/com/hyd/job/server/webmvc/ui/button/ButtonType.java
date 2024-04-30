package com.hyd.job.server.webmvc.ui.button;

/**
 * 按钮类型
 */
public enum ButtonType {

    /**
     * 点击按钮将会提交表单
     */
    SubmitForm,

    /**
     * 点击按钮将会跳转页面
     */
    NavigateUrl,

    /**
     * 点击按钮将会执行浏览器后退（仅限 GET 请求使用）
     */
    HistoryBack
}
