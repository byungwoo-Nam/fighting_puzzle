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
					$(".replyItemArea").empty();
					$("#userReplyInput").val("");
					getReplyData(1);
					getReplyCount();
				});
				
				$(document).on("click",".replyDelete",function(e){
					if(confirm("댓글을 삭제 하시겠습니까?")){
						var data = {"ajaxMode":"dataDelete", "dataMode":"reply", "seq":$(this).attr("data-idx")};
						data.url = "/ajaxConnect.do";
						JSON.parse(getAjaxData(data));
						$(".replyItemArea").empty();
						$("#userReplyInput").val("");
						getReplyData(1);
						getReplyCount();
					}
				});
				
				$(document).on("click",".replyUpdate",function(e){
					$(".replyUpdateArea .cancel").trigger("click");
					$(this).parents(".replyItems").children(".userImage").hide();
					$(this).parents(".replyItems").children(".replyItem").hide();
					$(this).parents(".replyItems").children(".replyUpdateArea").removeClass("hide");
				});
				
				$(document).on("click",".replyUpdateArea .action",function(e){
					var data = {"ajaxMode":"dataUpdate", "dataMode":"reply", "seq":$(this).attr("data-idx"), "puzzle_seq":$("#userReplyInput").attr("data-idx"), "content":$(this).parents(".replyUpdateArea").children("textarea").val()};
					data.url = "/ajaxConnect.do";
					getAjaxData(data);
					$(this).parents(".replyItems").find(".replyContent").html(data.content);
					$(".replyUpdateArea .cancel").trigger("click");
				});
				
				$(document).on("click",".replyUpdateArea .cancel",function(e){
					$(this).parents(".replyItemArea").find(".userImage").show();
					$(this).parents(".replyItemArea").find(".replyItem").show();
					$(this).parents(".replyItemArea").find(".replyUpdateArea").addClass("hide");
				});
				
				$(document).on("click","#replyMore",function(e){
					$(this).attr("data-pageNum", parseInt($(this).attr("data-pageNum"))+1);
					$(this).remove();
					getReplyData($(this).attr("data-pageNum"));
					if( $("#replyCnt").html() > $(".replyItems").length){
						$(".replyItemArea").append($(this));						
					}
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
				
				function getReplyCount(){
					var data = {"ajaxMode":"getTotalCount", "dataMode":"reply", "puzzle_seq":$("#userReplyInput").attr("data-idx")};
					data.url = "/ajaxConnect.do";
					$("#replyCnt").html(JSON.parse(getAjaxData(data)));
				}
				
				function getReplyData(page){
					var user_seq = "<s:property value="#session.user_seq" />";
					var data = {"ajaxMode":"getData", "dataMode":"reply", "puzzle_seq":$("#userReplyInput").attr("data-idx"), "pageNum":page};
					data.url = "/ajaxConnect.do";
					var replyObj = JSON.parse(getAjaxData(data));
					var replyHTML;
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
						if(user_seq == value.user_seq){
							replyHTML += '<div class="replyOption">';
							replyHTML += '<span class="replyUpdate pointer">수정</span>';
							replyHTML += '<span class="replyDelete pointer ml10" data-idx="' + value.seq + '">삭제</span>';
							replyHTML += '</div>';
						}
						replyHTML += '</div>';
						replyHTML += '<div class="replyUpdateArea hide">';
						replyHTML += '<textarea class="form-control" rows="3">' + value.content + '</textarea>';
						replyHTML += '<div class="mt5 ta-r">';
						replyHTML += '<button type="button" class="action pointer btn btn-warning btn-sm" data-idx="' + value.seq + '">업데이트</button>';
						replyHTML += '<button type="button" class="cancel pointer btn btn-default btn-sm">취소</button>';
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
									<span class="puzzleHashtagBtn">#${hashtag}</span>
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
									<s:if test = "#session.user_seq == user_seq">
										<div class="replyOption">
											<span class="replyUpdate pointer">수정</span>
											<span class="replyDelete pointer ml10" data-idx="<s:property value="seq"/>">삭제</span>
										</div>
									</s:if>
								</div>
								<div class="replyUpdateArea hide">
									<textarea class="form-control" rows="3"><s:property value="content"/></textarea>
									<div class="mt5 ta-r">
										<button type="button" class="action pointer btn btn-warning btn-sm" data-idx="<s:property value="seq"/>">업데이트</button>
										<button type="button" class="cancel pointer btn btn-default btn-sm">취소</button>
									</div>
								</div>
							</div>
						</s:iterator>
					</s:if>
					<s:if test = "#data.replyList.size > 5">
						<div id="replyMore" class="pointer" data-pageNum="1">
							<i class="fa fa-comment" aria-hidden="true"></i><span class="ml5 va-m">다음 댓글 보기...</span>
						</div>
					</s:if>
				</div>
			</div>
		</div>	
	</body>
</html>