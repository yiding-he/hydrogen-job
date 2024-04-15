jQuery.fn.extend({
  data_table: function(options) {
    const table = this.datatable({
      deferRender: true,
      processing: true,
      serverSide: true,
      ajax: {
        type: "post",
        url: options.data_url(),
        data: options.data_param
      },
      searching: false,
      ordering: false,
      //"scrollX": true,	// scroll x，close self-adaption
      columns: options.columns,
      language: {
        "sProcessing": I18n.dataTable_sProcessing,
        "sLengthMenu": I18n.dataTable_sLengthMenu,
        "sZeroRecords": I18n.dataTable_sZeroRecords,
        "sInfo": I18n.dataTable_sInfo,
        "sInfoEmpty": I18n.dataTable_sInfoEmpty,
        "sInfoFiltered": I18n.dataTable_sInfoFiltered,
        "sInfoPostFix": "",
        "sSearch": I18n.dataTable_sSearch,
        "sUrl": "",
        "sEmptyTable": I18n.dataTable_sEmptyTable,
        "sLoadingRecords": I18n.dataTable_sLoadingRecords,
        "sInfoThousands": ",",
        "oPaginate": {
          "sFirst": I18n.dataTable_sFirst,
          "sPrevious": I18n.dataTable_sPrevious,
          "sNext": I18n.dataTable_sNext,
          "sLast": I18n.dataTable_sLast
        },
        "oAria": {
          "sSortAscending": I18n.dataTable_sSortAscending,
          "sSortDescending": I18n.dataTable_sSortDescending
        }
      }
    });
    table.on('click', '.delete', function() {
      const id = $(this).parent('p').attr("id");
      layer.confirm( I18n.system_ok + I18n.system_opt_del + '?', {
        icon: 3,
        title: I18n.system_tips ,
        btn: [ I18n.system_ok, I18n.system_cancel ]
      }, function(index){
        layer.close(index);
        $.ajax({
          type : 'POST',
          url : options.delete_url(),
          data : options.delete_param(id),
          dataType : "json",
          success : options.delete_callback
        });
      });
    })
    return table;
  }
})
