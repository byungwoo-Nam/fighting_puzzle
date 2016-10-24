<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
				$("#gameStart").click(function(){
					var url = "/puzzle/puzzleGame.do";
					var seq = $(this).attr("data-idx");
					url += "?seq=" + seq;
					$(location).attr("href",url);
					e.preventDefault();
				});
			});
		</script>
	</head>
	<body>
		<s:set name="data" value='puzzleDTO' />
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">view</s:param>
			<s:param name="title">${data.userName}님의 퍼즐</s:param>
		</s:include> 
		<div class="site-wrapper">			
			<article id="a_puzzleMain">
				<div>
					<header>
						<div class="userImage mr10">
							<img src="${data.userPicture}" />
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
						<img src="${data.puzzleUrl}" class="img-responsive" alt="thumbnail1" />
					</div>
					<div class="btn-group btn-group-justified mb10" role="group" aria-label="버튼그룹">
						<div class="btn-group" role="group">
							<button type="button" id="gameStart" class="btn btn-success" data-idx="${data.seq}"><i class="fa fa-play" aria-hidden="true"></i> 게임시작</button>
					  	</div>
					  	<div class="btn-group" role="group">
							<button type="button" class="btn btn-default"><i class="fa fa-heart" aria-hidden="true"></i> 좋아요</button>
					  	</div>
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart" aria-hidden="true"></i> 200명
						</div>
						<div class="replyInfo fr">
							댓글 10개
						</div>
					</footer>
				</div>
			</article>
			<div class="replyArea">
				<div class="dis-fl mb10">
					<div class="userImage mr10">
						<img src="/jsp/temp/userSample1.jpg" />
					</div>
					<input type="text" id="userReplyInput" class="form-control" placeholder="댓글을 입력하세요..." />
					<button type="submit" class="btn btn-default ml10">게시</button>
				</div>
				<div class="replyItems">
					<div class="userImage mr10">
						<img src="/jsp/temp/userSample2.jpg" />
					</div>
					<div class="replyItem">
						<div class="replyWriter">
							<strong>신광호</strong>
							<span>30분 전</span>
						</div>
						<div class="replyContent">
							재밌음!!
						</div>
					</div>
				</div>
				<div class="replyItems">
					<div class="userImage mr10">
						<img src="/jsp/temp/userSample3.jpg" />
					</div>
					<div class="replyItem">
						<div class="replyWriter">
							<strong>이성호</strong>
							<span>4시간 전</span>
						</div>
						<div class="replyContent">
							너무 어려운데ㅠㅠ
						</div>
					</div>
				</div>
				<div class="replyItems">
					<div class="userImage mr10">
						<img src="/jsp/temp/userSample1.jpg" />
					</div>
					<div class="replyItem">
						<div class="replyWriter">
							<strong>남병우</strong>
							<span>1일 전</span>
						</div>
						<div class="replyContent">
							깨볼사람은 깨봐~~~~ㅋㅋㅋㅋ
						</div>
					</div>
				</div>
			</div>
		</div>	
	</body>
</html>