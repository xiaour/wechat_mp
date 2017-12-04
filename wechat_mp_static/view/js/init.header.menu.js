/**
 * Created by xiaour on 2017/11/28.
 */
//初始化菜单
$(function(){
    $("#menuTree").append("  <div class=\"navbar-inner\">" +
        "<div class=\"container\"><a class=\"btn btn-navbar\" data-toggle=\"collapse\" data-target=\".nav-collapse\">" +
        "<span  class=\"icon-bar\"></span><span class=\"icon-bar\"></span><span class=\"icon-bar\"></span>" +
        "</a><a class=\"brand\" href=\"index.html\">Wechat MP</a><div class=\"nav-collapse\">" +
        "<ul class=\"nav pull-right\">" +
        "<form class=\"navbar-search pull-right\">" +
        "<input type=\"text\" class=\"search-query\" placeholder=\"查找...\">  </form>" +
        "</div>    </div>  </div>");

    $(".subnavbar").append("<div class=\"subnavbar-inner\">" +
        "<div class=\"container\">" +
        "<ul class=\"mainnav\">" +
        "<li id='pm1'><a href=\"index.html#pm1\"><i class=\"icon-dashboard\"></i><span>首页</span> </a> </li>" +
        "<li id='pm2'><a href=\"account.html#pm2\"><i class=\"icon-comments\"></i><span>公众号管理</span> </a> </li>" +
        "<li id='pm3'><a href=\"guidely.html#pm3\"><i class=\"icon-qrcode\"></i><span>二维码管理</span> </a></li>" +
        "<li id='pm4'><a href=\"shortcodes.html#pm4\"><i class=\"icon-code\"></i><span>接口管理</span> </a> </li>" +
        "</ul></li></ul>" +
        "</div></div>")

    var tab = window.location.hash;
    if(tab!=null){
        document.body.scrollTop = document.documentElement.scrollTop = 0;
        $(tab).addClass("active");
    }
});

//元素的补零计算
function addZero(val) {
    if (val < 10) {
        return "0" + val;
    } else {
        return val;
    }
}

function getQueryParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
    var r = window.location.search.substr(1).match(reg);  //获取url中"?"符后的字符串并正则匹配
    var context = "";
    if (r != null)
        context = r[2];
    reg = null;
    r = null;
    return decodeURI(context == null || context == "" || context == "undefined" ? "" : context);
}

Vue.filter("formatTime",function(value,type){
    var dataTime="";
    var data = new Date(value);
    var year   =  data.getFullYear();
    var month  =  addZero(data.getMonth() + 1);
    var day    =  addZero(data.getDate());
    var hour   =  addZero(data.getHours());
    var minute =  addZero(data.getMinutes());
    var second =  addZero(data.getSeconds());
    if(isNaN(year)||isNaN(month)||isNaN(day)||isNaN(hour)||isNaN(minute)||isNaN(second)){
        return "";
    }
    if(type == "YYYY-MM-DD"){
        dataTime =  year + "-"+ month + "-" + day;
    }else if(type == "YYYY-MM-DD HH:MI:SS"){
        dataTime = year + "-"+month + "-" + day + " " +hour+ ":"+minute+":" +second;
    }else if(type == "HH:MI:SS"){
        dataTime = hour+":" + minute+":" + second;
    }else if(type == "YYYY-MM"){
        dataTime = year + "-" + month;

    }
    return dataTime;//将格式化后的字符串输出到前端显示
});
