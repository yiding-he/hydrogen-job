<!DOCTYPE html>
<html lang="en">
<head>
    <#import "../common/common.macro.ftl" as netCommon>
    <@netCommon.commonStyle />
    <link rel="stylesheet" href="${request.contextPath}/static/adminlte/plugins/iCheck/square/blue.css">
    <title>${I18n.admin_name}</title>
</head>
<body>
<h1>You have logged in.</h1>

<@netCommon.commonScript />
<script src="${request.contextPath}/static/adminlte/plugins/iCheck/icheck.min.js"></script>

</body>
</html>
