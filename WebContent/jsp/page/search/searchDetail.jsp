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
			<s:param name="option">view</s:param>
			<s:param name="title">#${keyword}</s:param>
		</s:include> 
		<div class="site-wrapper searchDetailList pt30">
			<s:iterator value="dataList1" status="stat">
				<s:if test = "#stat.first">
					<div>
						<p class="ml15"><strong>인기 게시물</strong></p>
				</s:if>
					<s:if test="(#stat.index+1)%3==1">
						<div class="row">
					</s:if>
							<div class="viewPuzzleMain thumbnail ta-c" data-idx="<s:property value="seq"/>" style="background:url(<s:property value="puzzleUrl"/>)">
							</div>
					<s:if test="(#stat.index+1)%3==0">
						</div>
					</s:if>
					<s:if test = "#stat.last">
						</div>
					</s:if>
			</s:iterator>
			<s:iterator value="dataList2" status="stat">
				<s:if test = "#stat.first">
					<div class="mt50">
						<p class="ml15"><strong>최근 퍼즐</strong></p>
				</s:if>
					<s:if test="(#stat.index+1)%3==1">
						<div class="row">
					</s:if>
							<div class="thumbnail ta-c" style="background:url(<s:property value="puzzleUrl"/>)">
							</div>
					<s:if test="(#stat.index+1)%3==0">
						</div>
					</s:if>
					<s:if test = "#stat.last">
						</div>
					</s:if>
			</s:iterator>
		</div>	
	</body>
</html>