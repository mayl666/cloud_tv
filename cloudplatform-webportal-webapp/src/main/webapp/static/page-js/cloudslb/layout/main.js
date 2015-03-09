/**
 * Created by yaokuo on 2014/12/12.
 */
define(function(require){
    var common = require('../../common');
    var cn = new common();

    /*加载数据*/
    var dataHandler = require('./dataHandler');
    var basicInfoHandler = new dataHandler();

    /*初始化侧边栏菜单*/
    var index = [1,0];
    cn.Sidebar(index);//index为菜单中的排序(1-12)
    /*
     * 加载slb基础信息
     */
    cn.GetData("/slb/"+$("#slbId").val(),basicInfoHandler.resCountHandler);
});