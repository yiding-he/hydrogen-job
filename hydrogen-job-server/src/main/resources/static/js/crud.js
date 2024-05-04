$(() => {
  $(".data-table").on('click', 'a[data-confirm]', (e) => {
    let confirmMessage = e.target.dataset.confirm;
    if (confirmMessage && !confirm(confirmMessage)) {
      e.preventDefault();
    }
  })
})
