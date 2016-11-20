<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
				$("#scrollRefresh").click(function(){
					$(this).attr("data-pageNum", parseInt($(this).attr("data-pageNum"))+1);
					var data = {"ajaxMode":"getData", "dataMode":"puzzleList", "pageNum":$(this).attr("data-pageNum")};
					data.url = "/ajaxConnect.do";
					var ajaxResultData = getAjaxData(data);
					if(!blankCheck(ajaxResultData)){
						puzzleRefresh(JSON.parse(ajaxResultData));
						scrollRefresh = true;
					}
				});
				
				function puzzleRefresh(puzzleObj){
					var puzzleHTML;
					$.each(puzzleObj, function(key, value) {
						puzzleHTML = "";
						puzzleHTML += '<article id="a_index">';
						puzzleHTML += '<div>';
						puzzleHTML += '<header>';
						puzzleHTML += '<div class="userImage mr10">';
						puzzleHTML += '<img src="' + value.userPicture + '" />';
						puzzleHTML += '</div>';
						puzzleHTML += '<div class="title">';
						puzzleHTML += '<div>';
						puzzleHTML += '<strong>' + value.userName + '</strong>님이 <strong>새로운 퍼즐</strong>을 등록했습니다.';
						puzzleHTML += '</div>';
						puzzleHTML += '<div class="regDate">';
						puzzleHTML += '30분전';
						puzzleHTML += '</div>';
						puzzleHTML += '</div>';
						puzzleHTML += '</header>';
						puzzleHTML += '<div class="contentArea mb10">';
						puzzleHTML += '<div>';
						puzzleHTML += '<div>';
						puzzleHTML += '<h2 class="bestRecord bold ta-c mb10">';
						puzzleHTML += (value.bestRecord!=0) ? '<i class="fa fa-clock-o" aria-hidden="true"></i>' + convertMillisecond(value.bestRecord) + '초' : '<p>기록이 없습니다.</p><p>도전하세요!</p>';
						puzzleHTML += '</h2>';
						puzzleHTML += '</div>';
						puzzleHTML += '<div class="mb10">';
						$.each(value.hashtagList, function(key, value2) {
							puzzleHTML += '<a href="/" >#' + value2.hashtag + '</a>';
						});
						puzzleHTML += '</div>';
						puzzleHTML += '</div>';
						puzzleHTML += '<img src="' + value.puzzleUrl + '" class="viewPuzzleMain pointer img-responsive" alt="thumbnail1" data-idx="' + value.seq + '" />';
						puzzleHTML += '</div>';
						puzzleHTML += '<footer class="mb10">';
						puzzleHTML += '<div class="dis-ib">';
						puzzleHTML += '<i class="fa fa-child" aria-hidden="true"></i> ' + value.playCnt + '명';
						puzzleHTML += '<i class="fa fa-heart ml10 likeCntIcon" aria-hidden="true"></i> ' + value.likeCnt + '명';
						puzzleHTML += '</div>';
						puzzleHTML += '<div class="replyInfo fr">';
						puzzleHTML += '댓글 ' + value.replyCnt + '개';
						puzzleHTML += '</div>';
						puzzleHTML += '</footer>';
						puzzleHTML += '</div>';
						if(value.replyList.length != 0){
							$.each(value.replyList, function(key, value2) {
								puzzleHTML += '<div class="replyArea row">';
								puzzleHTML += '<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">';
								puzzleHTML += '<div class="userImage">';
								puzzleHTML += '<img src="' + value2.userPicture + '" />';
								puzzleHTML += '</div>';
								puzzleHTML += '</div>';
								puzzleHTML += '<div class="replyContent col-xs-10 col-sm-10 col-md-10 col-lg-10">';
								puzzleHTML += '<div>' + value2.userName + '</div>';
								puzzleHTML += '<div>' + value2.content + '</div>';
								puzzleHTML += '<div>' + value2.printDate + '</div>';
								puzzleHTML += '</div>';
								puzzleHTML += '</div>';
							});
						}
						puzzleHTML += '</article>';						
						$("#puzzleIndexArea").append(puzzleHTML);
					});
				}
			});
		</script>
	</head>
	<body>
		<input type="hidden" id="scrollRefresh" data-pageNum="1" />
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">default</s:param>
			<s:param name="title">default</s:param>
		</s:include>
		<div id="puzzleIndexArea" class="container">	
			<s:if test = "dataList.size==0">
				등록된 퍼즐이 없습니다.
			</s:if>
			<s:iterator value="dataList" status="stat">		
				<article id="a_index">
					<div>
						<header>
							<div class="userImage mr10">
								<img src="<s:property value="userPicture"/>" />
							</div>
							<div class="title">
								<div>
									<strong><s:property value="userName"/></strong>님이 <strong>새로운 퍼즐</strong>을 등록했습니다.
								</div>
								<div class="regDate">
									30분 전
								</div>
							</div>
						</header>
						<div class="contentArea mb10">
							<div>
								<div>
									<h2 class="bestRecord bold ta-c mb10">
										<s:if test = "bestRecord!=0">
											<i class="fa fa-clock-o" aria-hidden="true"></i>
											<s:property value="@util.system.StringUtil@convertMillisecond(bestRecord)"/>초
										</s:if>
										<s:else>
											<p>기록이 없습니다.</p><p>도전하세요!</p>
										</s:else>
									</h2>
								</div>
								<div class="mb10">
									<s:iterator value="hashtagList" status="stat">
										<span class="puzzleHashtagBtn">#<s:property value="hashtag"/></span>
									</s:iterator>
								</div>
							</div>
							<img src="<s:property value="puzzleUrl"/>" class="viewPuzzleMain pointer img-responsive" alt="thumbnail1" data-idx="<s:property value="seq"/>" />
						</div>
						<footer class="mb10">
							<div class="dis-ib">
								<i class="fa fa-child" aria-hidden="true"></i> <s:property value="playCnt"/>명
								<i class="fa fa-heart ml10 likeCntIcon" aria-hidden="true"></i> <s:property value="likeCnt"/>명
							</div>
							<div class="replyInfo fr">
								댓글 <s:property value="replyCnt"/>개
							</div>
						</footer>
					</div>
					<s:if test = "replyList.size!=0">
						<s:iterator value="replyList" status="stat">
							<div class="replyArea row">
								<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2">
									<div class="userImage">
										<img src="<s:property value="userPicture"/>" />
									</div>
								</div>
								<div class="replyContent col-xs-10 col-sm-10 col-md-10 col-lg-10">
									<div><s:property value="userName"/></div>
									<div><s:property value="content"/></div>
									<div><s:property value="printDate"/></div>
								</div>
							</div>
						</s:iterator>
					</s:if>
				</article>

			</s:iterator>
			
		</div>
	</body>
</html>