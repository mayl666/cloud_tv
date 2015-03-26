/**
 * Created by yaokuo on 2014/12/14.
 */
define(function(require,exports,module){
    var $ = require('jquery');

    var DataHandler = function(){
    };

    module.exports = DataHandler;

    DataHandler.prototype = {
    	/*
    	 * 相关资源量赋值
    	 */
        resCountHandler : function(data){
            if(data.data.db > 0){
                $("#rds-opened").removeClass("hide");
                $("#rds-not-opened").addClass("hide");
                $("#dbCount").html(data.data.db);
            }else{
                $("#rds-opened").addClass("hide");
                $("#rds-not-opened").removeClass("hide");
            }
            if(data.data.slb > 0){
                $("#slb-opened").removeClass("hide");
                $("#slb-not-opened").addClass("hide");
                $("#slbCount").html(data.data.slb);
            }else{
                $("#slb-opened").addClass("hide");
                $("#slb-not-opened").removeClass("hide");
            }
            if(data.data.gce > 0){
            	$("#gce-opened").removeClass("hide");
            	$("#gce-not-opened").addClass("hide");
            	$("#gceCount").html(data.data.gce);
            }else{
            	$("#gce-opened").addClass("hide");
            	$("#gce-not-opened").removeClass("hide");
            }
        }
    }
});