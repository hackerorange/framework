/**
 * Created by a1379 on 2017/8/31.
 */

var change_date = function() {

    days=-30;
    // 参数表示在当前日期下要增加的天数
    var now = new Date();
    // + 1 代表日期加，- 1代表日期减
    now.setDate((now.getDate() + 1) - 1 * days);
    var year = now.getFullYear();
    var month = now.getMonth() - 1;
    var day = now.getDate();
    if (month < 10) {
        month = '0' + month;
    }
    if (day < 10) {
        day = '0' + day;
    }

    return year + '-' + month + '-' + day;
};
