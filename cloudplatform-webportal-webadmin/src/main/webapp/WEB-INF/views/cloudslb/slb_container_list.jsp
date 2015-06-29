<%@ page language="java" pageEncoding="UTF-8"%>
<!-- /section:settings.box -->
<div class="page-content-area">
<div class="row">
<div class="widget-box widget-color-blue ui-sortable-handle queryOption collapsed">
	<script>
		$(window).load(function() {
			var iw=document.body.clientWidth;
			if(iw>767){//md&&lg
				$('.queryOption').removeClass('collapsed');
			}
		});
	</script>
		<div class="widget-header hidden-md hidden-lg">
			<h5 class="widget-title">Container查询条件</h5>
			<div class="widget-toolbar">
				<a href="#" data-action="collapse">
					<i class="ace-icon fa fa-chevron-down"></i>
				</a>
			</div>
		</div>
		<div class="widget-body">
			<div class="page-header">
			</div>
		</div>
	</div>
	<!-- /.page-header -->
	
		<div class="widget-box widget-color-blue ui-sortable-handle col-xs-12">
			<div class="widget-header">
				<h5 class="widget-title">Container列表</h5>
			</div>
			<div class="widget-body">
				<div class="widget-main no-padding">
					<table id="mcluster_list" class="table table-striped table-bordered table-hover">
						<thead>
							<tr>
								<th class="center">
									<label class="position-relative">
										<input type="checkbox" class="ace" />
										<span class="lbl"></span>
									</label>
								</th>
								<th>Container名称</th>
								<th class='hidden-480'>所属container集群</th>
								<th class="hidden-480">所属物理机集群</th>
								<th>ip</th>
								<th>宿主机ip</th>
								<th class="hidden-480">创建时间 </th>
								<th>当前状态</th>
							</tr>
						</thead>
						<tbody id="tby">
						</tbody>
					</table>
				</div>
			</div>
		</div>
		<div id="pageControlBar" class="col-xs-12">
			<input type="hidden" id="totalPage_input" />
			<ul class="pager">
				<li><a href="javascript:void(0);" id="firstPage">&laquo&nbsp;首页</a></li>
				<li><a href="javascript:void(0);" id="prevPage" >上一页</a></li>
				<li><a href="javascript:void(0);" id="nextPage">下一页</a></li>
				<li><a href="javascript:void(0);" id="lastPage">末页&nbsp;&raquo</a></li>
				<li class='hidden-480'><a>共<lable id="totalPage"></lable>页</a></li>
				<li class='hidden-480'><a>第<lable id="currentPage"></lable>页</a></li>
				<li class='hidden-480'><a>共<lable id="totalRows"></lable>条记录</a></li>
			</ul>
		</div>
	</div>
</div>
<!-- /.page-content-area -->

<link rel="stylesheet" href="${ctx}/static/styles/bootstrap/bootstrapValidator.min.css" />
<script src="${ctx}/static/scripts/bootstrap/bootstrapValidator.min.js"></script>

<script src="${ctx}/static/ace/js/jquery.dataTables.min.js"></script>
<script src="${ctx}/static/ace/js/jquery.dataTables.bootstrap.js"></script>

<script src="${ctx}/static/scripts/pagejs/slb/slb_container_list.js"></script>