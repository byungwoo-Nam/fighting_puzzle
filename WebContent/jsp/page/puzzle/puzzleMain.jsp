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
					data = {"ajaxMode":"getData", "dataMode":"reply", "puzzle_seq":inputObj.attr("data-idx")};
					data.url = "/ajaxConnect.do";
					replyRefresh(JSON.parse(getAjaxData(data)));
				});
				
				$("#likeBtn").click(function(){
					if($(this).hasClass("like-active")){
						// 해제
						var data = {"ajaxMode":"dataDelete", "dataMode":"like", "puzzle_seq":$(this).attr("data-idx")};
						data.url = "/ajaxConnect.do";
						JSON.parse(getAjaxData(data));
						$(this).removeClass("like-active");
						$("#likeCnt").html(parseInt($("#likeCnt").html())-1);
					}else{
						// 등록
						var data = {"ajaxMode":"dataInsert", "dataMode":"like", "puzzle_seq":$(this).attr("data-idx")};
						data.url = "/ajaxConnect.do";
						JSON.parse(getAjaxData(data));
						$(this).addClass("like-active");
						$("#likeCnt").html(parseInt($("#likeCnt").html())+1);
					}
				});
				
				function replyRefresh(replyObj){
					var replyHTML;
					$(".replyItemArea").empty();
					$("#userReplyInput").val("");
					$("#replyCnt").html(replyObj.length);
					$.each(replyObj, function(key, value) { 
						replyHTML = "";
						replyHTML += '<div class="replyItems">';
						replyHTML += '<div class="userImage mr10">';
						replyHTML += '<img src="' + value.userPicture + '" />';
						replyHTML += '</div>';
						replyHTML += '<div class="replyItem">';
						replyHTML += '<div class="replyWriter">';
						replyHTML += '<strong>' + value.userName + '</strong>';
						replyHTML += '<span>' + value.printDate + '</span>';
						replyHTML += '</div>';
						replyHTML += '<div class="replyContent">';
						replyHTML += value.content;
						replyHTML += '</div>';
						replyHTML += '</div>';
						replyHTML += '</div>';
						$(".replyItemArea").append(replyHTML);
					});
				}
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
								<h2 class="bestRecord bold ta-c mb10">
									<s:if test = "#data.bestRecord!=0">
										<i class="fa fa-clock-o" aria-hidden="true"></i>
										<s:property value="@util.system.StringUtil@convertMillisecond(#data.bestRecord)"/>초
									</s:if>
									<s:else>
										<p>기록이 없습니다.</p><p>도전하세요!</p>
									</s:else>
								</h2>
							</div>
						</div>
						<img src="${data.puzzleUrl}" class="img-responsive" alt="thumbnail1" />
					</div>
					<div class="btn-group btn-group-justified mb10" role="group" aria-label="버튼그룹">
					  	<div class="btn-group" role="group">
							<button type="button" id="likeBtn" class="btn btn-default <s:if test = "#data.like">like-active</s:if>" data-idx="${data.seq}"><i class="fa fa-heart" aria-hidden="true"></i><span> 좋아요</span></button>
					  	</div>
					  	<div class="btn-group" role="group">
							<button type="button" id="gameStart" class="btn btn-success" data-idx="${data.seq}"><i class="fa fa-play" aria-hidden="true"></i><span> 게임시작</span></button>
					  	</div>
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart likeCntIcon" aria-hidden="true"></i> <span id="likeCnt">${data.likeCnt}</span>명
						</div>
						<div class="replyInfo fr">
							댓글 <span id="replyCnt">${data.replyCnt}</span>개
						</div>
					</footer>
				</div>
			</article>
			<div class="replyArea">
				<div class="replyWriteArea dis-fl mb10">
					<div class="userImage mr10">
						<img src="${session.user_picture}" />
					</div>
					<input type="text" id="userReplyInput" class="form-control" data-idx="${data.seq}" placeholder="댓글을 입력하세요..." />
					<button type="button" class="btn btn-default ml10">게시</button>
				</div>
				<div class="replyItemArea">
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
		</div>	
	</body>
</html>