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
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">view</s:param>
			<s:param name="title">남병우님의 퍼즐</s:param>
		</s:include> 
		<div class="site-wrapper">			
			<article id="a_puzzle">
				<div>
					<header>
						<div class="userImage small mr10">
							<img src="/jsp/temp/userSample1.jpg" />
						</div>
						<div class="title">
							<div>
								<a href="/" >#용인</a>
								<a href="/" >#명지대</a>
								<a href="/" >#창조관</a>
							</div>
							<div class="regDate">
								2016-10-10 AM 12:14
							</div>
						</div>
					</header>
					<div class="contentArea mb10">
						<div>
							<div>
								<h2 class="bestRecord bold ta-c mb10"><i class="fa fa-clock-o" aria-hidden="true"></i> 20.00초</h2>
							</div>
						</div>
						<img src="/jsp/temp/thumbnail1.jpg" class="img-responsive" alt="thumbnail1" />
					</div>
					<div class="btn-group btn-group-justified" role="group" aria-label="버튼그룹">
						<div class="btn-group" role="group">
							<button type="button" class="btn btn-success openPop" data-pop-id="puzzleGame"><i class="fa fa-gamepad fa-2x" aria-hidden="true"></i> 게임시작</button>
					  	</div>
					  	<div class="btn-group" role="group">
							<button type="button" class="btn btn-default"><i class="fa fa-heart-o fa-2x" aria-hidden="true"></i> 좋아요</button>
					  	</div>
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart" aria-hidden="true"></i> 200명
						</div>
						<div class="replyInfo fr">
							댓글 0개
						</div>
					</footer>
				</div>
			</article>
		</div>	
		<s:include value="/jsp/page/puzzle/pop/puzzleGame.jsp" />
	</body>
</html>