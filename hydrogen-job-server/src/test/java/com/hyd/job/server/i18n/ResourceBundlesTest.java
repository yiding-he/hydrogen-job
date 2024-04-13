package com.hyd.job.server.i18n;

class ResourceBundlesTest {

  public static void main(String[] args) {
    System.out.println(
      ResourceBundles.getBundle("zh_CN").getString("user_password_update_placeholder")
    );
  }
}
