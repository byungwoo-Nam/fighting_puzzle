<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
$(document).ready(function() {
	$("#puzzleStart").click(function(){
		$(".layerClose").trigger("click");
	});
});
</script>
<div class="layer layer-puzzleStartPop" data-close-answer="false" data-parent-id="">
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer">
		<div class="pop-container ta-c">
			<div class="pop-conts">
				<div class="content">
		            <img src="/jsp/img/ready.png" class="mt20" />
		            <i id="puzzleStart" class="fa fa-gamepad" aria-hidden="true"></i>
		            <div><span class="mb15">조이스틱을 터치하면</span><span>게임이 시작됩니다!</span></div>
		        </div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x hide" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>