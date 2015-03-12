var currentPage = 1; //第几页 
var recordsPerPage = 15; //每页显示条数
var queryBuildStatusrefresh;//刷新handler
	
$(function(){
	//初始化 
	page_init();
	
	/*动态加载界面下拉列表值*/
	var sltArray = [1,2,3,5,7,8,9,10,11,12,13,14];
	addSltOpt(sltArray,$("#containerStatus"));
	
	$(document).on('click', 'th input:checkbox' , function(){
		var that = this;
		$(this).closest('table').find('tr > td:first-child input:checkbox')
		.each(function(){
			this.checked = that.checked;
			$(this).closest('tr').toggleClass('selected');
		});
	});
	var mclusterId;
	var status;
	//modal显示创建进度
	$(document).on('click', "[name='buildStatusBoxLink']" , function(){
		mclusterId = $(this).closest('tr').find('td:first input').val();
		
		if($(this).html().indexOf("运行中")>=0){
			$('#buildStatusHeader').html("创建成功");
			status = "1";
		}else if($(this).html().indexOf("创建中")>=0){
			$('#buildStatusHeader').html("<i class=\"ace-icon fa fa-spinner fa-spin green bigger-125\"></i>创建中...");
			status = "2";
		}else if($(this).html().indexOf("创建失败")>=0){
			//$('#buildStatusHeader').html("<font color=\"red\">创建失败</font>");
			$('#buildStatusHeader').html("创建失败");
			status = "3";
		}
		queryBuildStatus(mclusterId,"new");
	});
	
	$('#create-mcluster-status-modal').on('shown.bs.modal', function(){
		if(status == "2") {
			queryBuildStatusrefresh = setInterval(function() {  
				queryBuildStatus(mclusterId,"update");
			},5000);
		}
	}).on('hidden.bs.modal', function (e) {
		queryBuildStatusrefresh = window.clearInterval(queryBuildStatusrefresh);
		location.reload();
	});
	
});

function queryByPage(currentPage,recordsPerPage) {
	$("#tby tr").remove();
	var mclusterName = $("#nav-search-input").val()?$("#nav-search-input").val():'null';
	getLoading();
	$.ajax({
		cache:false,
		type : "get",
		url : "/mcluster/" + currentPage + "/" + recordsPerPage + "/" + mclusterName,
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data.data;
			var tby = $("#tby");
			var totalPages = data.data.totalPages;
			
			for (var i = 0, len = array.length; i < len; i++) {
				var td1 = $("<td class=\"center\">"
								+"<label class=\"position-relative\">"
								+"<input name=\"mcluster_id\" value= \""+array[i].id+"\" type=\"checkbox\" class=\"ace\"/>"
								+"<span class=\"lbl\"></span>"
								+"</label>"
							+"</td>");
				var td2 = $("<td>"
						+  "<a href=\"/detail/mcluster/" + array[i].id+"\">"+array[i].mclusterName+"</a>"
						+ "</td>");
				if(array[i].hcluster){
					var td3 = $("<td>"
 							+ array[i].hcluster.hclusterNameAlias
							+ "</td>");
				} else {
					var td3 = $("<td> </td>");
				} 
				var type = "";
				if(array[i].type) {
					type="后台创建";
				} else {
					type = "系统创建";
				}
				var td4 = $("<td>"
						+ type
						+ "</td>");
				
				var userName='system';
				if(array[i].createUserModel) {
					userName = array[i].createUserModel.userName;
				}
				var td5 = $("<td>"
						+ userName
						+ "</td>");
				var td6 = $("<td>"
						+ date('Y-m-d H:i:s',array[i].createTime)
						+ "</td>");
				if(array[i].status == 2){
					var td7 = $("<td>"
							+"<a name=\"buildStatusBoxLink\" data-toggle=\"modal\" data-target=\"#create-mcluster-status-modal\" style=\"cursor:pointer; text-decoration:none;\">"
							+"<i class=\"ace-icon fa fa-spinner fa-spin green bigger-125\"/>"
							+"创建中...</a>"
							+ "</td>");
				}else if(array[i].status == 3){
					var td7 = $("<td>"
							+"<a name=\"buildStatusBoxLink\" data-toggle=\"modal\" data-target=\"#create-mcluster-status-modal\" style=\"cursor:pointer; text-decoration:none;\">"
							+translateStatus(array[i].status)
							+"</a>"
							+ "</td>");
				}else{
					var td7 = $("<td>"
							+translateStatus(array[i].status)
							+ "</td>");
					
				}
					
				var td8 = $("<td>"
						+"<div class=\"hidden-sm hidden-xs  action-buttons\">"
						+"<a class=\"green\" href=\"#\" onclick=\"startMcluster(this)\">"
						+"<i class=\"ace-icon fa fa-play-circle-o bigger-130\"></i>"
						+"</a>"
						+"<a class=\"blue\" href=\"#\" onclick=\"stopMcluster(this)\" data-toggle=\"modal\" data-target=\"#\">"
							+"<i class=\"ace-icon fa fa-power-off bigger-120\"></i>"
						+"</a>"
						+"<a class=\"red\" href=\"#\" onclick=\"deleteMcluster(this)\" data-toggle=\"modal\" data-target=\"#\">"
							+"<i class=\"ace-icon fa fa-trash-o bigger-120\"></i>"
						+"</a>"
						+"</div>"
						+ "</td>"
				);
					
				if(array[i].status == 3||array[i].status == 4||array[i].status == 14){
					var tr = $("<tr class=\"default-danger\"></tr>");
				}else if(array[i].status == 5||array[i].status == 13){
					var tr = $("<tr class=\"warning\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6).append(td7).append(td8);
				tr.appendTo(tby);
			}//循环json中的数据 
			
			if (totalPages <= 1) {
				$("#pageControlBar").hide();
			} else {
				$("#pageControlBar").show();
				$("#totalPage_input").val(totalPages);
				$("#currentPage").html(currentPage);
				$("#totalRows").html(data.data.totalRecords);
				$("#totalPage").html(totalPages);
			}
		},
		error : function(XMLHttpRequest,textStatus, errorThrown) {
			error(XMLHttpRequest);
			return false;
		}
	});
   }
   

function pageControl() {
	// 首页
	$("#firstPage").bind("click", function() {
		currentPage = 1;
		queryByPage(currentPage,recordsPerPage);
	});

	// 上一页
	$("#prevPage").click(function() {
		if (currentPage == 1) {
			$.gritter.add({
				title: '警告',
				text: '已到达首页',
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
			
		} else {
			currentPage--;
			queryByPage(currentPage,recordsPerPage);
		}
	});

	// 下一页
	$("#nextPage").click(function() {
		if (currentPage == $("#totalPage_input").val()) {
			$.gritter.add({
				title: '警告',
				text: '已到达末页',
				sticky: false,
				time: '5',
				class_name: 'gritter-warning'
			});
	
			return false;
			
		} else {
			currentPage++;
			queryByPage(currentPage,recordsPerPage);
		}
	});

	// 末页
	$("#lastPage").bind("click", function() {
		currentPage = $("#totalPage_input").val();
		queryByPage(currentPage,recordsPerPage);
	});
}

	function searchAction(){
		$('#nav-search-input').bind('keypress',function(event){
	        if(event.keyCode == "13")    
	        {
	        	queryByPage(currentPage, recordsPerPage);
	        }
	    });
	}
	
//创建Container集群表单验证
function formValidate() {
	$("#create-mcluster-form").bootstrapValidator({
	  message: '无效的输入',
         feedbackIcons: {
             valid: 'glyphicon glyphicon-ok',
             invalid: 'glyphicon glyphicon-remove',
             validating: 'glyphicon glyphicon-refresh'
         },
         fields: {
       	  mclusterName: {
                 validMessage: '请按提示输入',
                 validators: {
                     notEmpty: {
                         message: 'Container集群名称不能为空!'
                     },
			          stringLength: {
			              max: 40,
			              message: 'Container集群名过长'
			          },regexp: {
		                  regexp: /^([a-zA-Z_0-9]*)$/,
  		                  message: "请输入字母数字或'_'"
                 	  },
                 	 remote: {
	                        message: 'Container集群名已存在!',
	                        url: "/mcluster/validate"
	                    }
	             }
         	}	
         }
     }).on('error.field.bv', function(e, data) {
    	 $('#create-mcluster-botton').addClass("disabled");
     }).on('success.field.bv', function(e, data) {
    	 $('#create-mcluster-botton').removeClass("disabled");
     });
}
//查询Container集群创建过程
function queryBuildStatus(mclusterId,type) {	//type(update或new)
	if(type == "new"){
		$("#build_status_tby tr").remove();
	}
	getLoading();
	$.ajax({
		cache:false,
		type : "get",
		url : "/build/mcluster/"+mclusterId,
		dataType : "json", /*这句可用可不用，没有影响*/
		success : function(data) {
			removeLoading();
			if(error(data)) return;
			var array = data.data;
			var build_status_tby = $("#build_status_tby");
			
			for (var i = 0, len = array.length; i < len; i++) {
				var td1 = $("<td>"
						+ array[i].step
						+"</td>");
				var td2 = $("<td>"
						+ array[i].stepMsg
						+"</td>");
				if(array[i].startTime != null )
				{
					var td3 = $("<td>"
							+ date('Y-m-d H:i:s',array[i].startTime)
							+ "</td>");
				}else{
					var td3 = $("<td>\-</td>");
				}
				if(array[i].endTime != null)
				{
					var td4 = $("<td>"
							+ date('Y-m-d H:i:s',array[i].endTime)
							+ "</td>");
				}else{
					var td4 = $("<td>\-</td>");
				}
				if(array[i].msg != null)
				{
					var td5 = $("<td>"
							+ array[i].msg
							+ "</td>");
				}else{
					var td5 = $("<td>\-</td>");
				}
				
				if(array[i].status == 1){
					var td6 = $("<td>"
							+"<a class=\"green\"><i class=\"ace-icon fa fa-check bigger-120\">成功</a>"
							+ "</td>");
				}else if(array[i].status == 0){
					var td6 = $("<td>"
							+"<a class=\"red\"><i class=\"ace-icon fa fa-times red bigger-120\">失败</a>"
							+ "</td>");
				}else if(array[i].status == 2){
					var td6 = $("<td>"
							+"<a style=\"text-decoration:none;\" class=\"green\"><h5><i class=\"ace-icon fa fa-spinner fa-spin green bigger-120\"/>运行中</h5></a>"
							+ "</td>");
				}else{
					var td6 = $("<td>"
							+"<a class=\"orange\"><i class=\"ace-icon fa fa-coffee orange bigger-120\">等待</a>"
							+ "</td>");
				}
					
				if(array[i].status == 0){
					var tr = $("<tr class=\"danger\"></tr>");
				}else{
					var tr = $("<tr></tr>");
				}
				
				tr.append(td1).append(td2).append(td3).append(td4).append(td5).append(td6);
				if(type == "new"){
					tr.appendTo(build_status_tby);
				}else{
					build_status_tby.find("tr:eq("+i+")").html(tr.html());
				}
			}
		},
		error : function(XMLHttpRequest,textStatus, errorThrown) {
			error(XMLHttpRequest);
			return false;
		}
	});
 }

function createMcluster(){
	getLoading();
	$.ajax({
		cache:false,
		type : "post",
		url : "/mcluster",
		data :$('#create-mcluster-form').serialize(),
		success:function (data){
			removeLoading();
			if(error(data)) return;
			$('#create-mcluster-form').find(":input").not(":button,:submit,:reset,:hidden").val("").removeAttr("checked").removeAttr("selected");
			$('#create-mcluster-form').data('bootstrapValidator').resetForm();
			$('#create-mcluster-botton').addClass('disabled');
			$('#create-mcluster-modal').modal('hide');
			//延时一秒刷新列表
			setTimeout("queryByPage(currentPage, recordsPerPage)",1000);
		}
	});
}
function startMcluster(obj){
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("已停止") < 0){
		warn("当前状态无法执行启动操作!",3000);
		return 0;
	}
	function startCmd(){
		var mclusterId =$(obj).parents("tr").find('[name="mcluster_id"]').val();
		getLoading();
		$.ajax({
			cache:false,
			url:'/mcluster/start',
			type:'post',
			data:{mclusterId : mclusterId},
			success:function(data){
				removeLoading();
				if(error(data)) return;
				queryByPage(currentPage, recordsPerPage);
			}
		});
	}
	confirmframe("启动container集群","启动集群大概需要几分钟时间!","请耐心等待...",startCmd);
}
function stopMcluster(obj){
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("运行中") < 0 &&tr.indexOf("异常") < 0&&tr.indexOf("危险") < 0&&tr.indexOf("严重危险") < 0 ){
		warn("当前状态无法执行关闭操作!",3000);
		return 0;
	}
	function stopCmd(){
		var mclusterId =$(obj).parents("tr").find('[name="mcluster_id"]').val();
		getLoading();
		$.ajax({
			cache:false,
			url:'/mcluster/stop',
			type:'post',
			data:{mclusterId : mclusterId},
			success:function(data){
				removeLoading();
				if(error(data)) return;
				queryByPage(currentPage, recordsPerPage);
			}
		});
	}
	confirmframe("关闭container集群","关闭container集群将不能提供服务,再次启动需要十几分钟!","您确定要关闭?",stopCmd);
}
function deleteMcluster(obj){
	
	warn("危险操作，本版本不启用...",3000);
	return;
	
	var tr = $(obj).parents("tr").html();
	if (tr.indexOf("删除中") >= 0){
		warn("正在删除集群,请耐心等待...",3000);
		return 0;
	}
	function deleteCmd(){
		var mclusterId =$(obj).parents("tr").find('[name="mcluster_id"]').val();
	
		$.ajax({
			cache:false,
			url:'/mcluster/'+mclusterId,
			type:'delete',
			success:function(data){
				
				if(error(data)) return;
				queryByPage(currentPage, recordsPerPage);
			}
		});
	}
	confirmframe("删除container集群","删除container集群后将不能恢复!","您确定要删除?",deleteCmd);
}

function queryHcluster(){
	var options = $('#hcluster_select');
	getLoading();
	$.ajax({
		cache:false,
		url:'/hcluster',
		type:'get',
		dataType:'json',
		success:function(data){
			removeLoading();
			var array = data.data;
			for(var i = 0, len = array.length; i < len; i++){
				
				var option = $("<option value=\""+array[i].id+"\">"
								+array[i].hclusterNameAlias
								+"</option>");
				options.append(option);
			}
		}
	});
}

function page_init(){
	queryByPage(currentPage, recordsPerPage);
	searchAction();
	formValidate();
	pageControl();
	$('[name = "popoverHelp"]').popover();
}
