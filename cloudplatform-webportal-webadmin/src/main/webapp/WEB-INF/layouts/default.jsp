<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib prefix="sitemesh" uri="http://www.opensymphony.com/sitemesh/decorator" %>  
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<c:set var="ctx" value="${pageContext.request.contextPath}" />

<!DOCTYPE html>
<html> 
<head>
<title>Letv CloudPlatform WebPortal<sitemesh:title/></title>
<meta http-equiv="Content-Type" content="text/html;charset=utf-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
<meta http-equiv="Cache-Control" content="no-store" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Expires" content="0" />
<meta name="description" content="overview &amp; stats" />
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

<!-- bootstrap & fontawesome -->
<link rel="stylesheet" href="${ctx}/static/ace/css/bootstrap.min.css" />
<link rel="stylesheet" href="${ctx}/static/ace/css/font-awesome.min.css" />
<link rel="stylesheet" href="${ctx}/static/styles/bootstrap/bootstrap-datetimepicker.min.css" />

<link rel="stylesheet" href="${ctx}/static/ace/css/chosen.css" />

<!-- page specific plugin styles -->
<link rel="stylesheet" href="${ctx}/static/ace/css/jquery-ui.min.css" />
<!-- text fonts -->
<link rel="stylesheet" href="${ctx}/static/ace/css/ace-fonts.css" />

<!-- ace styles -->
<link rel="stylesheet" href="${ctx}/static/ace/css/ace.min.css" id="main-ace-style" />
<link rel="stylesheet" href="${ctx}/static/ace/css/ace-rtl.min.css" />
<!--skin-custom  -->
<link rel="stylesheet" href="${ctx}/static/styles/ui-css/skin-custom.css" id="skinCSS" />

<!--[if lte IE 9]>
	<link rel="stylesheet" href="${ctx}/static/ace/css/ace-part2.min.css" />
<![endif]--> 


<!--[if lte IE 9]>
	<link rel="stylesheet" href="${ctx}/static/ace/css/ace-ie.min.css" />
<![endif]-->

<!-- inline styles related to this page -->

<!-- ace settings handler -->
<script src="${ctx}/static/ace/js/jquery.min.js"></script>
<script src="${ctx}/static/ace/js/ace-extra.min.js"></script>
<script src="${ctx}/static/ace/js/bootstrap.min.js"></script>
<script src="${ctx}/static/ace/js/jquery-ui.min.js"></script>
<script src="${ctx}/static/ace/js/chosen.jquery.min.js"></script>

<!-- warning box -->
<link rel="stylesheet" href="${ctx}/static/ace/css/jquery.gritter.css" />
<script src="${ctx}/static/ace/js/jquery.gritter.min.js"></script>
<!--cookie插件 -->
<script src="${ctx}/static/scripts/jquery.cookie.js"></script>

<!-- 常用函数 -->
<script src="${ctx}/static/scripts/general-function.js"></script>

<!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

<!--[if lte IE 8]>
	<script src="${ctx}/static/ace/js/html5shiv.min.js"></script>
	<script src="${ctx}/static/ace/js/respond.min.js"></script>
<![endif]-->

<sitemesh:head/>
</head>

<body class="skin-custom">
	<%@ include file="/WEB-INF/layouts/header.jsp"%>
	<div class="main-container" id="main-container">
		<script type="text/javascript">
		var cookie_skin;
		var mySkin="mySkin";
			try {
				ace.settings.check('main-container', 'fixed')
			} catch (e) {
			}
			function changeSkin(skinType){
				$('#skinCSS').attr('href','/static/styles/ui-css/'+skinType+'.css');
				$.cookie(mySkin,skinType,{path:'/',expires:10}); 
			}
			$(function(){
				cookie_skin=$.cookie(mySkin);				
				if(cookie_skin){					
					$('#skinCSS').attr('href','/static/styles/ui-css/'+cookie_skin+'.css');
					$.cookie(mySkin,cookie_skin,{path:'/',expires:10});
				} 
			});
			
		</script>
	<%@ include file="/WEB-INF/layouts/sidebar.jsp"%>
	
	<!--#内容显示 -->
	<div class="main-content">
		<!-- #section:basics/content.breadcrumbs -->
		<div class="breadcrumbs" id="breadcrumbs">
			<script type="text/javascript">
				try {
					ace.settings.check('breadcrumbs', 'fixed')
				} catch (e) {
				}
				
			</script>

			<ul id="main-content-header" class="breadcrumb">
				<li>
					<i class="ace-icon fa fa-home home-icon"></i> 
					<a href="#"></a>
				</li>
				<li class="active"></li>
			</ul>
			<!-- /.breadcrumb -->

			<!-- #section:basics/content.searchbox -->
			<div class="nav-search" id="nav-search">
				<span class="input-icon"> 
					<input type="text" placeholder="Search ..." value="" class="nav-search-input" id="nav-search-input" autocomplete="off" /> 
					<i class="ace-icon fa fa-search nav-search-icon"></i>
				</span>
			</div>
			<!-- /.nav-search -->

			<!-- /section:basics/content.searchbox -->
		</div>
		
		<!-- /section:basics/content.breadcrumbs -->
		<div class="page-content">
		
			<!-- #section:settings.box -->
			<!-- <div class="ace-settings-container" id="ace-settings-container">
				<div class="btn btn-app btn-xs btn-warning ace-settings-btn" id="ace-settings-btn">
					<i class="ace-icon fa fa-cog bigger-150"></i>
				</div>

				<div class="ace-settings-box clearfix" id="ace-settings-box">
					<div class="pull-left width-50">
						<div class="ace-settings-item">
							<div class="pull-left">
								<select id="skin-colorpicker" class="hide">
									<option data-skin="no-skin" value="#438EB9">#438EB9</option>
									<option data-skin="skin-1" value="#222A2D">#222A2D</option>
									<option data-skin="skin-2" value="#C6487E">#C6487E</option>
									<option data-skin="skin-3" value="#D0D0D0">#D0D0D0</option>
								</select>
							</div>
							<span>&nbsp; Choose Skin</span>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-navbar" /> <label class="lbl"
								for="ace-settings-navbar"> Fixed Navbar</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-sidebar" /> <label class="lbl"
								for="ace-settings-sidebar"> Fixed Sidebar</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-breadcrumbs" /> <label class="lbl"
								for="ace-settings-breadcrumbs"> Fixed Breadcrumbs</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-rtl" /> <label class="lbl"
								for="ace-settings-rtl"> Right To Left (rtl)</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-add-container" /> <label class="lbl"
								for="ace-settings-add-container"> Inside <b>.container</b>
							</label>
						</div>

					</div>

					<div class="pull-left width-50">
						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-hover" /> <label class="lbl"
								for="ace-settings-hover"> Submenu on Hover</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-compact" /> <label class="lbl"
								for="ace-settings-compact"> Compact Sidebar</label>
						</div>

						<div class="ace-settings-item">
							<input type="checkbox" class="ace ace-checkbox-2"
								id="ace-settings-highlight" /> <label class="lbl"
								for="ace-settings-highlight"> Alt. Active Item</label>
						</div>

					</div>
				</div>
			</div> -->

			<!-- /.ace-settings-container -->
			<sitemesh:body/>			
		</div>
	</div>
	<!-- /.内容显示  -->
	<%-- <%@ include file="/WEB-INF/layouts/footer.jsp"%> --%>
	</div>
	

	
<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
	window.jQuery || document.write("<script src='${ctx}/static/ace/js/jquery.min.js'>" + "<"+"/script>");
</script>
<!-- <![endif]-->

<!--[if IE]>
	<script type="text/javascript">  window.jQuery || document.write("<script src='${ctx}/static/ace/js/jquery1x.min.js'>"+"<"+"/script>");
	</script>
<![endif]-->
<script type="text/javascript">
	if ('ontouchstart' in document.documentElement)
		document.write("<script src='${ctx}/static/ace/js/jquery.mobile.custom.min.js'>" + "<"+"/script>");
</script>
	<!-- 设置sidebar的高亮显示 -->
<script type="text/javascript">
	var path = window.location.pathname;
	var cookie_skin;
	var mySkin="mySkin";
	if(path.indexOf("/list/mcluster/monitor/1") >= 0){
		$('#sidebar-forewarning-monitor-mgr').addClass("active open hsub");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/mcluster/monitor").html("预警管理");
		$('#sidebar-forewarning-monitor-mgr ul li:first').addClass("active");
		$('#main-content-header li:eq(1)').html("cluster监控列表");
	}else if(path.indexOf("/list/mcluster/monitor/2") >= 0){
		$('#sidebar-forewarning-monitor-mgr').addClass("active open hsub");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/mcluster/monitor").html("预警管理");
		$('#sidebar-forewarning-monitor-mgr ul li:eq(1)').addClass("active");
		$('#main-content-header li:eq(1)').html("node监控列表");
	}else if(path.indexOf("/list/mcluster/monitor/3") >= 0){
		$('#sidebar-forewarning-monitor-mgr').addClass("active open hsub");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/mcluster/monitor").html("预警管理");
		$('#sidebar-forewarning-monitor-mgr ul li:eq(2)').addClass("active");
		$('#main-content-header li:eq(1)').html("db监控列表");
	}else if(path.indexOf("/dashboard") >= 0){
		$('#sidebar-dashboard').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/dashboard").html("首页");
		$('#main-content-header li:eq(1)').html("Dashboard");
	}else if(path.indexOf("/view/mcluster/monitor") >= 0){
		$('#sidebar-monitor-mgr').addClass("active open hsub");
		$('#sidebar-monitor-mgr ul li:eq(0)').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/mcluster/monitor").html("监控管理");
		$('#main-content-header li:eq(1)').html("container集群监控视图");
	}else if(path.indexOf("/list/mcluster") >= 0||path.indexOf("/detail/mcluster") >= 0){
		$('#sidebar-cluster-mgr').addClass("active open hsub");
		$('#sidebar-cluster-mgr ul li:first').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/mcluster").html("container集群管理");
		$('#main-content-header li:eq(1)').html("Container集群列表");
	}else if(path.indexOf("/list/hcluster") >= 0||path.indexOf("/detail/hcluster") >= 0){
		$('#sidebar-cluster-mgr').addClass("active open hsub");
		$('#sidebar-cluster-mgr ul li:eq(1)').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/hcluster").html("物理机集群管理");
		$('#main-content-header li:eq(1)').html("物理机集群列表");
	}else if(path.indexOf("/list/dbUser") >= 0){
		$('#sidebar-db-mgr').addClass("active open hsub");
		$('#sidebar-db-mgr ul li:eq(1)').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/dbUser").html("数据库用户管理");
		$('#main-content-header li:eq(1)').html("数据库用户列表");
	}else if(path.indexOf("/list/db") >= 0 ||path.indexOf("/audit/db") >= 0||path.indexOf("/detail/db") >= 0){
		$('#sidebar-db-mgr').addClass("active open hsub");
		$('#sidebar-db-mgr ul li:first').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/db").html("数据库管理");
		$('#main-content-header li:eq(1)').html("数据库列表");
	}else if(path.indexOf("/list/backup") >= 0){
		$('#backupRecover').addClass("active");
		$('#main-content-header li:first a').attr("href", "${ctx}/list/backup").html("备份与修复");
		$('#main-content-header li:eq(1)').remove();
	}
	
			

</script>
<!-- ace scripts -->
<script src="${ctx}/static/ace/js/ace-elements.min.js"></script>
<script src="${ctx}/static/ace/js/ace.min.js"></script>
</body>
</html>
