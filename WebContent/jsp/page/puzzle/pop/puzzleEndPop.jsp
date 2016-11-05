<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
$(document).ready(function() {
	$("#puzzleStart").click(function(){
		$(".layerClose").trigger("click");
	});
	$("#gameResult button").click(function(){
		var url = $(this).attr("data-url");
		$(location).attr("href",url);
		e.preventDefault();
	});
});
</script>
<div class="layer layer-puzzleEndPop" data-close-answer="false" data-parent-id="">
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer">
		<div class="pop-container ta-c">
			<div class="pop-conts">
				<div class="content" id="gameResult">
					<span>나의 기록</span>
		            <span class="timePrint"></span>
		            <div class="mt20"><button type="button" class="btn btn-primary btn-block btn-lg" data-url="/index.do">확인</button></div>
		        </div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x hide" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>