/**
 * 分页查询 同步
 * @param number 当前页
 * @param size 每页数据量
 */
function pagination(number, size) {
    var queryForm = $('#query');
    queryForm.find('input[name="number"]').val(number);
    queryForm.find('input[name="size"]').val(size);
    queryForm.submit();
}