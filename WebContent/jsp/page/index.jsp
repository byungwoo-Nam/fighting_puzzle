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
		<s:include value="/jsp/page/include/gnb.jsp" />
		<div class="container">	
			<s:if test = "dataList.size==0">
				등록된 퍼즐이 없습니다.
			</s:if>
			<s:iterator value="dataList" status="stat">		
				<article id="a_index">
					<div>
						<header>
							<div class="userImage small mr10">
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
									<h2 class="bestRecord bold ta-c mb10"><i class="fa fa-clock-o" aria-hidden="true"></i> 20.00초</h2>
								</div>
								<div class="mb10">
									<a href="/" >#용인</a>
									<a href="/" >#명지대</a>
									<a href="/" >#창조관</a>
								</div>
							</div>
							<img src="<s:property value="puzzleUrl"/>" class="viewPuzzleMain img-responsive" alt="thumbnail1" data-idx="<s:property value="seq"/>" />
						</div>
						<footer>
							<div class="dis-ib">
								<i class="fa fa-child" aria-hidden="true"></i> 10명
								<i class="fa fa-heart ml10" aria-hidden="true"></i> 200명
							</div>
							<div class="replyInfo fr">
								댓글 3개
							</div>
						</footer>
					</div>
				</article>
			</s:iterator>
			
		</div>
	</body>
</html>