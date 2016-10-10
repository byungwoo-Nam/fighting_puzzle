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
			<article id="a_index">
				<div>
					<header>
						<div class="userImage small mr10">
							<img src="/jsp/temp/userSample1.jpg" />
						</div>
						<div class="title">
							<div>
								<strong>남병우</strong>님이 <strong>새로운 퍼즐</strong>을 등록했습니다.
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
						<img src="/jsp/temp/thumbnail1.jpg" class="viewPuzzleMain img-responsive" alt="thumbnail1" />
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart" aria-hidden="true"></i> 200명
						</div>
						<div class="replyInfo fr">
							댓글 3개
						</div>
					</footer>
				</div>
			</article>
			
			<article id="a_index">
				<div>
					<header>
						<div class="userImage small mr10">
							<img src="/jsp/temp/userSample2.jpg" />
						</div>
						<div class="title">
							<div>
								<strong>신광호</strong>님이 <strong>새로운 퍼즐</strong>을 등록했습니다.
							</div>
							<div class="regDate">
								50분 전
							</div>
						</div>
					</header>
					<div class="contentArea mb10">
						<div>
							<div>
								<h2 class="bestRecord bold ta-c mb10"><i class="fa fa-clock-o" aria-hidden="true"></i>  90.00초</h2>
							</div>
							<div class="mb10">
								<a href="/" >#박보검</a>
								<a href="/" >#구르미</a>
								<a href="/" >#그린</a>
								<a href="/" >#달빛</a>
							</div>
						</div>
						<img src="/jsp/temp/thumbnail2.jpg" class="img-responsive" alt="thumbnail2" />
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart" aria-hidden="true"></i> 100명
						</div>
						<div class="replyInfo fr">
							댓글 10개
						</div>
					</footer>
				</div>
			</article>
			
			<article id="a_index">
				<div>
					<header>
						<div class="userImage small mr10">
							<img src="/jsp/temp/userSample3.jpg" />
						</div>
						<div class="title">
							<div>
								<strong>이성호</strong>님이 <strong>새로운 퍼즐</strong>을 등록했습니다.
							</div>
							<div class="regDate">
								1시간 전
							</div>
						</div>
					</header>
					<div class="contentArea mb10">
						<div>
							<div>
								<h2 class="bestRecord bold ta-c mb10"><i class="fa fa-clock-o" aria-hidden="true"></i> 50.00초</h2>
							</div>
							<div class="mb10">
								<a href="/" >#컴공</a>
								<a href="/" >#코딩</a>
								<a href="/" >#어렵다</a>
							</div>
						</div>
						
						<img src="/jsp/temp/thumbnail3.jpg" class="img-responsive" alt="thumbnail3" />
					</div>
					<footer>
						<div class="dis-ib">
							<i class="fa fa-heart" aria-hidden="true"></i> 202명
						</div>
						<div class="replyInfo fr">
							댓글 20개
						</div>
					</footer>
				</div>
			</article>
		</div>
	</body>
</html>