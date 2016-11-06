<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<s:set name="data" value='puzzleDTO' />
		<script type="text/javascript">
			$(window).load(function() {
				$("#startPop").trigger("click");
				$(".site-wrapper").height($(window).height()-48+"px");
				/*
				var imgRatio = 0;
				var areaRatio = 0;
				
				// 미리보기 이미지
				imgRatio = $("#origImg").width() / $("#origImg").height();
			    areaRatio = $("#prevImgArea").width() / $("#prevImgArea").height();
			    if(imgRatio < areaRatio){
			        $("#origImg").css({"width":"auto","height":"100%"});
			    }else{
			        $("#origImg").css({"width":"100%","height":"auto"});
			    }
			    */
			   	// 퍼즐이미지
			   	/*
			    imgRatio = $("#gameImg").width() / $("#gameImg").height();
			    areaRatio = $("#imgArea").width() / $("#imgArea").height();
			    if(imgRatio < areaRatio){
			        $("#gameImg").css({"width":"auto","height":"100%"});
			    }else{
			        $("#gameImg").css({"width":"100%","height":"auto"});
			    }
			    
			    if($("#imgArea").width() < $("#gameImg").width()){
			        $("#gameImg").css("marginLeft","-"+($("#gameImg").width()-$("#imgArea").width())/2+"px");
			    }
			    
			    */
			    
			    var col = "${data.col}";
			    var row = "${data.row}";
			    
			    $("#imgArea").width($("#gameImg").width());
			    $("#gameImg").width($("#gameImg").width()-4*col);

			    $("#imgArea").fpp({x:col, y:row, margin: 2});

			    //$("#imgArea").width($("#gameImg").width()+10);	    
			});
		</script>
	</head>
	<body>	
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">view</s:param>
			<s:param name="title">${data.userName}님의 퍼즐</s:param>
		</s:include> 
		<div class="site-wrapper">			
			<article id="a_puzzleGame">
				<input type="button" id="startPop" class="hide openPop" data-pop-id="puzzleStartPop" />
				<input type="button" id="endPop" class="hide openPop" data-pop-id="puzzleEndPop" />
				<input type="hidden" id="seq" value="${data.seq}" />
				<div>
					<header class="container-fluid">
						<div class="row">
							<div id="prevImgArea" class="col-xs-8 col-sm-8 col-md-8 col-lg-8">
								<img id="origImg" src="${data.puzzleUrl}" class="parentFix" alt="gameImg" />
							</div>
							<div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 pl0 pr0 ta-c">
								<h3 class="bestRecord bold mb10"><i class="fa fa-clock-o" aria-hidden="true"></i> 20.00초</h3>
								<div class="recording mt20"><span class='timePrint'>00:00</span></div>
							</div>
						</div>
					</header>
					<div class="contentArea mb10">
						<div id="imgArea">
							<img id="gameImg" class="parentFix" src="${data.puzzleUrl}" alt="gameImg" />
						</div>
					</div>
				</div>
			</article>
		</div>
		<jsp:include page="/jsp/page/puzzle/pop/puzzleStartPop.jsp" flush="false" />
		<jsp:include page="/jsp/page/puzzle/pop/puzzleEndPop.jsp" flush="false" />	
	</body>
</html>