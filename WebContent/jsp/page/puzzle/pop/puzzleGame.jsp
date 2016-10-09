<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>
<script type="text/javascript">
$(document).ready(function() {
});
</script>
<div class="layer layer-puzzleGame" data-close-answer="false" >
	<div class="bg"></div>
	<div id="popLayer" class="pop-layer">
		<div class="pop-container">
			<div class="pop-conts">
				<!--content //-->
				<p id="pop-title" class="pop-title mb20 pb10">업체 검색</p>
				<div class="pl20 pr20">
					<div class="mb25">
						<p class="dib mr15 lh38 fl">업체아이디</p>
						<p class="dib">
							<input type="text" id="shopSearchID" name="" autocomplete="off" value="" />
							<i id="shopSearchBtn" class="ml10 fa fa-search" aria-hidden="true" title="검색" data-layer-id="generalProductView" data-kind="generalProduct" data-seq="<s:property value="seq"/>" > </i>
						</p>
					</div>
				</div>
				<div id="result" class="mt10">
					<span class="dib mt10">검색결과 : 0</span>
					<div id="listEmpty">
						<p class="tc">검색결과가 없습니다.<p>
					</div>
					<div id="listExist" class="hide">
					</div>
				</div>
				<div class="btn-r">
					<i class="layerClose fa fa-times fa-2x" aria-hidden="true" title="닫기"></i>
				</div>
			</div>
		</div>
	</div>
</div>