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
				
				$(".replyWriteArea button").click(function(){
					var inputObj = $("#userReplyInput");
					var data = {"ajaxMode":"dataInsert", "dataMode":"reply", "puzzle_seq":inputObj.attr("data-idx"), "content":inputObj.val()};
					
					data.url = "/ajaxConnect.do";
					JSON.parse(getAjaxData(data));
					
					var data = {"ajaxMode":"getData", "dataMode":"reply", "puzzle_seq":inputObj.attr("data-idx")};
					var test = JSON.parse(getAjaxData(data));
					console.log(test);
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
								<s:iterator value="#data.hashtagList" status="stat">
									<a href="/" >#${hashtag}</a>
								</s:iterator>
							</div>
							<div class="regDate">
								${data.printDate}
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
							<i class="fa fa-heart" aria-hidden="true"></i> ${data.likeCnt}명
						</div>
						<div class="replyInfo fr">
							댓글 ${data.replyCnt}개
						</div>
					</footer>
				</div>
			</article>
			<div class="replyArea">
				<div class="replyWriteArea dis-fl mb10">
					<div class="userImage mr10">
						<img src="/jsp/temp/userSample1.jpg" />
					</div>
					<input type="text" id="userReplyInput" class="form-control" data-idx="${data.seq}" placeholder="댓글을 입력하세요..." />
					<button type="button" class="btn btn-default ml10">게시</button>
				</div>
				<s:if test = "#data.replyList.size!=0">
					<s:iterator value="#data.replyList" status="stat">
						<div class="replyItems">
							<div class="userImage mr10">
								<img src="<s:property value="userPicture"/>" />
							</div>
							<div class="replyItem">
								<div class="replyWriter">
									<strong><s:property value="userName"/></strong>
									<span><s:property value="printDate"/></span>
								</div>
								<div class="replyContent">
									<s:property value="content"/>
								</div>
							</div>
						</div>
					</s:iterator>
				</s:if>
			</div>
		</div>	
	</body>
</html>