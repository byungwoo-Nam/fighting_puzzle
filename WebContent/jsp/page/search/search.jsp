<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
				$("#keyword").keyup(function(){
					if($(this).val().length > 0){
						var data = {"ajaxMode":"getData", "dataMode":"search", "keyword":$(this).val()};
						data.url = "/ajaxConnect.do";
						$(".searchDefault").hide();
						setResult(getAjaxData(data));
						$(".searchResult").show();
					}else{
						$(".searchResult").hide();
						$(".searchDefault").show();
					}
				});
				
				$(document).on("click touchstart",".searchDetailBtn",function(e){
					var url = "/search/searchDetail.do";
					var hashtag = $(this).find(".hashtag").html();
					url += "?keyword=" + hashtag;
					$(location).attr("href",url);
					e.preventDefault();
				});
				
				function setResult(resultObj){
					$(".searchResult").empty();
					var resultHTML;
					
					resultHTML = "";
					resultHTML += '<div class="searchArea mt10">';
					
					if(blankCheck(resultObj)){
						resultHTML += '<div class="row mt15 pb15">';
						resultHTML += '<div class="searchContent resultEmpty col-xs-9 col-sm-9 col-md-9 col-lg-9">';
						resultHTML += '<p>검색 결과 없음</p>';
						resultHTML += '</div>';
						resultHTML += '</div>';
					}else{
						$.each(JSON.parse(resultObj), function(key, value) {
							resultHTML += '<div class="searchDetailBtn row mt15 pb15">';
							resultHTML += '<div class="hashtagMark col-xs-2 col-sm-2 col-md-2 col-lg-2 ta-c">';
							resultHTML += '<label for="" class="control-label"><i class="fa fa-hashtag fa-2x" aria-hidden="true"></i></label>';
							resultHTML += '</div>';
							resultHTML += '<div class="searchContent col-xs-9 col-sm-9 col-md-9 col-lg-9">';
							resultHTML += '<p><strong>#<span class="hashtag">' + value.hashtag + '</span></strong></p>';
							resultHTML += '<p>게시물 ' + value.groupCount + '개</p>';
							resultHTML += '</div>';
							resultHTML += '</div>';
						});
					}
					
					resultHTML += '</div>';
					
					$(".searchResult").append(resultHTML);
					
				}
			});
		</script>
	</head>
	<body>
		<s:include value="/jsp/page/include/gnb.jsp">
			<s:param name="option">default</s:param>
			<s:param name="title">default</s:param>
		</s:include>
		<div class="container">
			<div class="input-group mb20">
				<div class="input-group-addon"><i class="fa fa-search" aria-hidden="true"></i></div>
				<input type="text" id="keyword" class="form-control" placeholder="검색어 입력..." />
		    </div>
		    <div class="searchDefault">
				<div class="searchArea mt10">
					<div>
						<span>인기 검색어</span>
						<s:if test="dataList1.size==0">
							<div class="row mt5">
								<div class="searchContent resultEmpty col-xs-9 col-sm-9 col-md-9 col-lg-9">
									<p>검색 결과 없음</p>
								</div>
							</div>
						</s:if>
						<s:iterator value="dataList1" status="stat">
							<div class="searchDetailBtn row mt15 pb15">
								<div class="hashtagMark col-xs-2 col-sm-2 col-md-2 col-lg-2 ta-c">
									<label for="" class="control-label"><i class="fa fa-hashtag fa-2x" aria-hidden="true"></i></label>
								</div>
								<div class="searchContent col-xs-9 col-sm-9 col-md-9 col-lg-9">
									<p><strong>#<span class="hashtag"><s:property value="keyword"/></span></strong></p>
									<p>퍼즐 <s:property value="puzzleCount"/>개</p>
								</div>
							</div>
						</s:iterator>
					</div>
					<div class="mt50">
						<span>최근 검색</span>
						<s:if test="dataList2.size==0">
							<div class="row mt5">
								<div class="searchContent resultEmpty col-xs-9 col-sm-9 col-md-9 col-lg-9">
									<p>검색 결과 없음</p>
								</div>
							</div>
						</s:if>
						<s:iterator value="dataList2" status="stat">
							<div class="searchDetailBtn row mt15 pb15">
								<div class="hashtagMark col-xs-2 col-sm-2 col-md-2 col-lg-2 ta-c">
									<label for="" class="control-label"><i class="fa fa-hashtag fa-2x" aria-hidden="true"></i></label>
								</div>
								<div class="searchContent col-xs-9 col-sm-9 col-md-9 col-lg-9">
									<p><strong>#<span class="hashtag"><s:property value="keyword"/></span></strong></p>
									<p>게시물 <s:property value="puzzleCount"/>개</p>
								</div>
							</div>
						</s:iterator>
					</div>
				</div>
			</div>
			<div class="searchResult">
			</div>
		</div>
	</body>
</html>