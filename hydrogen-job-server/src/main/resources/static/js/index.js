$(function() {
  $('.tree li .menu-item-text').dblclick(function() {
    $(this).parent().toggleClass('expanded');
  });
})
