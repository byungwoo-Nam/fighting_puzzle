<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
			});
		</script>
	</head>
	<body>
		<s:include value="/jsp/page/include/gnb.jsp" >
			<s:param name="option">login</s:param>
			<s:param name="title">Fighting Photo Puzzle</s:param>
		</s:include>
		<div class="container loginArea">
			<div class="ta-c mb10">
				<strong class="dis-b">보유하신 SNS계정을 사용하여 로그인 가능합니다.</strong>
				<strong class="dis-b">원하시는 로그인 SNS 버튼을 클릭해주세요.</strong>
			</div>
			<div class="snsLogos">
				<div class="snsLogo">
					<img src="/jsp/img/facebook_logo.png" />
				</div>	
				<div class="snsLogo">
					<img src="/jsp/img/kakaotalk_logo.jpg" />
				</div>	
			</div>	
		</div>
	</body>
</html>