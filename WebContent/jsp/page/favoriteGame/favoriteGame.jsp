<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
				var size = 0;
				$(".thumbnail").each(function(){
					size = size==0 ? $(this).width() : size;
					$(this).height(size);
				});
			});
		</script>
	</head>
	<body>
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">default</s:param>
			<s:param name="title">default</s:param>
		</s:include> 
		<div class="site-wrapper searchDetailList pt30">
			<span class="ml10">전체 <s:property value="dataList.size" />개</span>
			<div class="mt10">
				<s:iterator value="dataList" status="stat">
					<s:if test="(#stat.index+1)%3==1">
						<div class="row">
					</s:if>
							<div class="viewPuzzleMain pointer thumbnail ta-c" data-idx="<s:property value="puzzle_seq"/>" style="background:url(<s:property value="puzzleUrl"/>)">
							</div>
					<s:if test="(#stat.index+1)%3==0">
						</div>
					</s:if>
				</s:iterator>
			</div>
		</div>	
	</body>
</html>