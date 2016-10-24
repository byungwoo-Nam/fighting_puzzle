<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(document).ready(function() {
				$(".site-wrapper").height($(window).height()-58+"px");
				
				var imgRatio = $("#gameImg").width() / $("#gameImg").height();
			    var areaRatio = $("#imgArea").width() / $("#imgArea").height();
			    if(imgRatio < areaRatio){
			        $("#gameImg").css({"width":"auto","height":"100%"});
			    }else{
			        $("#gameImg").css({"width":"100%","height":"auto"});
			    }
			    
			    if($("#imgArea").width() < $("#gameImg").width()){
			        $("#gameImg").css("marginLeft","-"+($("#gameImg").width()-$("#imgArea").width())/2+"px");
			    }
			    
			    $("#imgArea").width($("#gameImg").width());
			    
			    $("#imgArea").jigsaw({x: 4, y:4, margin: 2});
			    
			    $(window).on("resize orientationchange",function(){
			        var imgRatio=$("#gameImg").width()/$("#gameImg").height();
			        var areaRatio=$("#imgArea").width()/$("#imgArea").height();
			        if(imgRatio < areaRatio){
			            $("#gameImg").css({"width":"auto","height":"100%"});
			        }else{
			            $("#gameImg").css({"width":"100%","height":"auto"});
			        }
			        if($("#imgArea").width() < $("#gameImg").width()){
			            $("#gameImg").css("marginLeft","-"+($("#gameImg").width()-$("#imgArea").width())/2+"px");
			        }
			        console.log(imgRatio);
			        console.log(areaRatio);
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
			<article id="a_puzzleGame">
				<div>
					<!-- 
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
					 -->
					<div class="contentArea mb10">
					<!-- 
						<div>
							<div>
								<h2 class="bestRecord bold ta-c mb10"><i class="fa fa-clock-o" aria-hidden="true"></i> 20.00초</h2>
							</div>
						</div>
						 -->
						<div id="imgArea">
							<img id="gameImg" src="${data.puzzleUrl}" alt="thumbnail1" />
						</div>
					</div>
				</div>
			</article>
		</div>	
	</body>
</html>