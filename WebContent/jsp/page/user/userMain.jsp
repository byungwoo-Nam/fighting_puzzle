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
		<s:set name="data" value='userDTO' />
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">default</s:param>
			<s:param name="title">default</s:param>
		</s:include>
		<div class="container">
			<article id="userPage">
				<div class="userImage">
					<img src="${data.pictureUrl}" />
				</div>
				<p class="ta-c mt20"><span class="mr5">${data.name} 님</span><span>|</span><span class="ml5 logoutBtn pointer">로그아웃</span></p>
				<div class="mt40">
					<div class="mt15 pb15 row">
						<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 ta-l"></div>
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5 ta-l">등록한 퍼즐</div>
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5 ta-r"><p>${data.puzzleWriteCnt}</p><i class="fa fa-chevron-right" aria-hidden="true"></i></div>
						<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 ta-l"></div>
					</div>
					<div class="mt15 pb15 row">
						<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 ta-l"></div>
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5 ta-l">플레이한 퍼즐</div>
						<div class="col-xs-5 col-sm-5 col-md-5 col-lg-5 ta-r"><p>${data.playCnt}</p><i class="fa fa-chevron-right" aria-hidden="true"></i></div>
						<div class="col-xs-1 col-sm-1 col-md-1 col-lg-1 ta-l"></div>
					</div>			
				</div>
			</article>
		</div>
	</body>
</html>