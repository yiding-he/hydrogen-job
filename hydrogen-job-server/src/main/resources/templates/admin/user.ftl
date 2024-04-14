<!DOCTYPE html>
<html>
<head>
  <#import "../common/common.macro.ftl" as netCommon>
  <@netCommon.commonStyle />
  <!-- DataTables -->
  <link rel="stylesheet"
        href="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/css/dataTables.bootstrap.min.css">
  <title>${bundle.admin_name}</title>
</head>
<body class="hold-transition skin-blue sidebar-mini">
<div class="wrapper">
  <!-- header -->
  <@netCommon.commonHeader />
  <!-- left -->
  <@netCommon.commonLeft "user" />

  <!-- Content Wrapper. Contains page content -->
  <div class="content-wrapper">
    <!-- Content Header (Page header) -->
    <section class="content-header">
      <h1>${bundle.user_manage}</h1>
    </section>

    <!-- Main content -->
    <section class="content">
      <div class="row table-query">
        <div class="col-xs-3">
          <div class="input-group">
            <span class="input-group-addon">${bundle.user_role}</span>
            <select class="form-control" id="role">
              <option value="-1">${bundle.system_all}</option>
              <option value="1">${bundle.user_role_admin}</option>
              <option value="0">${bundle.user_role_normal}</option>
            </select>
          </div>
        </div>
        <div class="col-xs-3">
          <div class="input-group">
            <span class="input-group-addon">${bundle.user_username}</span>
            <input type="text" class="form-control" id="username" autocomplete="on">
          </div>
        </div>
        <div class="col-xs-1">
          <button class="btn btn-block btn-info" id="searchBtn">${bundle.system_search}</button>
        </div>
        <div class="col-xs-2">
          <button class="btn btn-block btn-success add" type="button">${bundle.user_add}</button>
        </div>
      </div>

      <div class="row">
        <div class="col-xs-12">
          <div class="box">
            <div class="box-body">
              <table id="user_list" class="table table-bordered table-striped" width="100%">
                <thead>
                <tr>
                  <th name="id">ID</th>
                  <th name="username">${bundle.user_username}</th>
                  <th name="role">${bundle.user_role}</th>
                  <th>${bundle.system_opt}</th>
                </tr>
                </thead>
                <tbody></tbody>
                <tfoot></tfoot>
              </table>
            </div>
          </div>
        </div>
      </div>
    </section>
  </div>

  <!-- footer -->
  <@netCommon.commonFooter />
</div>

<!-- 新增.模态框 -->
<div class="modal fade" id="addModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">${bundle.user_add}</h4>
      </div>
      <form class="form-horizontal form" role="form">
        <div class="modal-body">
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label required">${bundle.user_username}</label>
            <div class="col-sm-9"><input type="text" class="form-control" name="username"
                                         placeholder="${bundle.system_please_input}${bundle.user_username}" maxlength="50">
            </div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label required">${bundle.user_password}</label>
            <div class="col-sm-9"><input type="text" class="form-control" name="password"
                                         placeholder="${bundle.system_please_input}${bundle.user_password}" maxlength="50">
            </div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label required">${bundle.product}</label>
            <div class="col-sm-9"><input type="text" class="form-control" name="password"
                                         placeholder="${bundle.system_please_input}${bundle.product}" maxlength="50"></div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label">${bundle.product_line}</label>
            <div class="col-sm-9"><input type="text" class="form-control" name="password"
                                         placeholder="${bundle.system_please_input}${bundle.product_line}" maxlength="50">
            </div>
          </div>
        </div>
        <div class="modal-footer">
          <button type="submit" class="btn btn-primary">${bundle.system_save}</button>
          <button type="button" class="btn btn-default" data-dismiss="modal">${bundle.system_cancel}</button>
        </div>
      </form>
    </div>
  </div>
</div>

<!-- 更新.模态框 -->
<div class="modal fade" id="updateModal" tabindex="-1" role="dialog" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <div class="modal-header">
        <h4 class="modal-title">${bundle.user_update}</h4>
      </div>
      <div class="modal-body">
        <form class="form-horizontal form" role="form">
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label">${bundle.user_username}<font color="red">*</font></label>
            <div class="col-sm-8"><input type="text" class="form-control" name="username"
                                         placeholder="${bundle.system_please_input}${bundle.user_username}" maxlength="20"
                                         readonly></div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label">${bundle.user_password}<font color="red">*</font></label>
            <div class="col-sm-8"><input type="text" class="form-control" name="password"
                                         placeholder="${bundle.user_password_update_placeholder}" maxlength="20"></div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label">${bundle.user_role}<font color="red">*</font></label>
            <div class="col-sm-10">
              <input type="radio" name="role" value="0"/>${bundle.user_role_normal}
              &nbsp;&nbsp;&nbsp;&nbsp;
              <input type="radio" name="role" value="1"/>${bundle.user_role_admin}
            </div>
          </div>
          <div class="form-group">
            <label for="lastname" class="col-sm-2 control-label">${bundle.user_permission}<font
                color="black">*</font></label>
            <div class="col-sm-10">
              <#if groupList?exists && groupList?size gt 0>
                <#list groupList as item>
                  <input type="checkbox" name="permission" value="${item.id}" />${item.title}(${item.appname})<br>
                </#list>
              </#if>
            </div>
          </div>

          <hr>
          <div class="form-group">
            <div class="col-sm-offset-3 col-sm-6">
              <button type="submit" class="btn btn-primary">${bundle.system_save}</button>
              <button type="button" class="btn btn-default" data-dismiss="modal">${bundle.system_cancel}</button>
              <input type="hidden" name="id">
            </div>
          </div>

        </form>
      </div>
    </div>
  </div>
</div>

<@netCommon.commonScript />
<!-- DataTables -->
<script
  src="${request.contextPath}/static/adminlte/bower_components/datatables.net/js/jquery.dataTables.min.js"></script>
<script
  src="${request.contextPath}/static/adminlte/bower_components/datatables.net-bs/js/dataTables.bootstrap.min.js"></script>
<script src="${request.contextPath}/static/js/user.index.1.js"></script>
</body>
</html>
