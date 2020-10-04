/**
 * 显示Modal
 * @param html modal中显示的html
 */
function modal(html) {
    var modal = $('#modal');
    modal.html(html);
    // 打开modal https://getbootstrap.com/docs/4.5/components/modal/#methods
    modal.modal('show');
    // 隐藏modal https://getbootstrap.com/docs/4.5/components/modal/#events
    modal.on('hidden.bs.modal', function(e) {
        modal.empty();
    });
}
