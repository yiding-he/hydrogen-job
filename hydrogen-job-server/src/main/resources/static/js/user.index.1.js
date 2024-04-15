$(function () {
  // table data
  const tableData = {};
  // init date tables
  const userListTable = $("#user_list").data_table({
    data_url: () => base_url + "/user/pageList",
    data_param: (d) => ({
      username: $('#username').val(),
      role: $('#role').val(),
      start: d.start,
      length: d.length
    }),
    delete_url: () => base_url + "/user/remove",
    delete_param: (id) => ({id: id}),
    delete_callback: (data) => {
      if (data.code == 0) {
        layer.msg(I18n.system_success);
        userListTable.fnDraw(false);
      } else {
        layer.msg(data.msg || I18n.system_opt_del + I18n.system_fail);
      }
    },
    columns: [
      {
        "data": 'userId',
        "visible": true,
        "width": '7%'
      },
      {
        "data": 'userName',
        "visible": true,
        "width": ''
      },
      {
        "data": 'role',
        "visible": true,
        "width": '10%',
        "render": function (data, type, row) {
          if (data == 1) {
            return I18n.user_role_admin
          } else {
            return I18n.user_role_normal
          }
        }
      },
      {
        "data": I18n.system_opt,
        "width": '200',
        "render": function (data, type, row) {
          return function () {
            // html
            tableData['key' + row.id] = row;
            return `<p id="${row.id}" >
                      <button class="btn btn-warning btn-xs update" type="button">${I18n.system_opt_edit}</button>
                      <button class="btn btn-danger btn-xs delete" type="button">${I18n.system_opt_del}</button>
                    </p>`;
          };
        }
      }
    ]
  });

  // search btn
  $('#searchBtn').on('click', function () {
    userListTable.fnDraw();
  });

  // add role
  $("#addModal .form input[name=role]").change(function () {
    const role = $(this).val();
    if (role == 1) {
      $("#addModal .form input[name=permission]").parents('.form-group').hide();
    } else {
      $("#addModal .form input[name=permission]").parents('.form-group').show();
    }
    $("#addModal .form input[name='permission']").prop("checked", false);
  });

  jQuery.validator.addMethod("myValid01", function (value, element) {
    const length = value.length;
    const valid = /^[a-z][a-z0-9]*$/;
    return this.optional(element) || valid.test(value);
  }, I18n.user_username_valid);

  // add
  $(".add").click(function () {
    $('#addModal').modal({backdrop: false, keyboard: false}).modal('show');
  });
  const addModalValidate = $("#addModal .form").validate({
    errorElement: 'span',
    errorClass: 'help-block',
    focusInvalid: true,
    rules: {
      username: {
        required: true,
        rangelength: [4, 20],
        myValid01: true
      },
      password: {
        required: true,
        rangelength: [4, 20]
      }
    },
    messages: {
      username: {
        required: I18n.system_please_input + I18n.user_username,
        rangelength: I18n.system_lengh_limit + "[4-20]"
      },
      password: {
        required: I18n.system_please_input + I18n.user_password,
        rangelength: I18n.system_lengh_limit + "[4-20]"
      }
    },
    highlight: function (element) {
      $(element).closest('.form-group').addClass('has-error');
    },
    success: function (label) {
      label.closest('.form-group').removeClass('has-error');
      label.remove();
    },
    errorPlacement: function (error, element) {
      element.parent('div').append(error);
    },
    submitHandler: function (form) {

      const permissionArr = [];
      $("#addModal .form input[name=permission]:checked").each(function () {
        permissionArr.push($(this).val());
      });

      const paramData = {
        "username": $("#addModal .form input[name=username]").val(),
        "password": $("#addModal .form input[name=password]").val(),
        "role": $("#addModal .form input[name=role]:checked").val(),
        "permission": permissionArr.join(',')
      };

      $.post(base_url + "/user/add", paramData, function (data, status) {
        if (data.code == 0) {
          $('#addModal').modal('hide');

          layer.msg(I18n.system_add_suc);
          userListTable.fnDraw();
        } else {
          layer.open({
            title: I18n.system_tips,
            btn: [I18n.system_ok],
            content: (data.msg || I18n.system_add_fail),
            icon: '2'
          });
        }
      });
    }
  });
  $("#addModal").on('hide.bs.modal', function () {
    $("#addModal .form")[0].reset();
    addModalValidate.resetForm();
    $("#addModal .form .form-group").removeClass("has-error");
    $(".remote_panel").show();	// remote

    $("#addModal .form input[name=permission]").parents('.form-group').show();
  });

  // update role
  $("#updateModal .form input[name=role]").change(function () {
    const role = $(this).val();
    if (role == 1) {
      $("#updateModal .form input[name=permission]").parents('.form-group').hide();
    } else {
      $("#updateModal .form input[name=permission]").parents('.form-group').show();
    }
    $("#updateModal .form input[name='permission']").prop("checked", false);
  });

  // update
  $("#user_list").on('click', '.update', function () {

    const id = $(this).parent('p').attr("id");
    const row = tableData['key' + id];

    // base data
    $("#updateModal .form input[name='id']").val(row.id);
    $("#updateModal .form input[name='username']").val(row.username);
    $("#updateModal .form input[name='password']").val('');
    $("#updateModal .form input[name='role'][value='" + row.role + "']").click();
    let permissionArr = [];
    if (row.permission) {
      permissionArr = row.permission.split(",");
    }
    $("#updateModal .form input[name='permission']").each(function () {
      if ($.inArray($(this).val(), permissionArr) > -1) {
        $(this).prop("checked", true);
      } else {
        $(this).prop("checked", false);
      }
    });

    // show
    $('#updateModal').modal({backdrop: false, keyboard: false}).modal('show');
  });
  const updateModalValidate = $("#updateModal .form").validate({
    errorElement: 'span',
    errorClass: 'help-block',
    focusInvalid: true,
    highlight: function (element) {
      $(element).closest('.form-group').addClass('has-error');
    },
    success: function (label) {
      label.closest('.form-group').removeClass('has-error');
      label.remove();
    },
    errorPlacement: function (error, element) {
      element.parent('div').append(error);
    },
    submitHandler: function (form) {

      const permissionArr = [];
      $("#updateModal .form input[name=permission]:checked").each(function () {
        permissionArr.push($(this).val());
      });

      const paramData = {
        "id": $("#updateModal .form input[name=id]").val(),
        "username": $("#updateModal .form input[name=username]").val(),
        "password": $("#updateModal .form input[name=password]").val(),
        "role": $("#updateModal .form input[name=role]:checked").val(),
        "permission": permissionArr.join(',')
      };

      $.post(base_url + "/user/update", paramData, function (data, status) {
        if (data.code == 0) {
          $('#updateModal').modal('hide');

          layer.msg(I18n.system_update_suc);
          userListTable.fnDraw();
        } else {
          layer.open({
            title: I18n.system_tips,
            btn: [I18n.system_ok],
            content: (data.msg || I18n.system_update_fail),
            icon: '2'
          });
        }
      });
    }
  });
  $("#updateModal").on('hide.bs.modal', function () {
    $("#updateModal .form")[0].reset();
    updateModalValidate.resetForm();
    $("#updateModal .form .form-group").removeClass("has-error");
    $(".remote_panel").show();	// remote

    $("#updateModal .form input[name=permission]").parents('.form-group').show();
  });

});
