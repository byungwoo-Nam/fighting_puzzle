<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<s:set name="option">
   ${param.option}
</s:set>
<s:set name="title">
   ${param.title}
</s:set>
<s:if test="#option == 'view'">
	<div class="gnbArea-view">
		<button type="button" class="btn btn-basic backBtn">돌아가기</button>
		<div><s:property value="#title" /></div>
	</div>
</s:if>
<s:elseif test="#option == 'login'">
	<div class="gnbArea-view">
		<div><s:property value="#title" /></div>
	</div>
</s:elseif>
<s:else>
	<div class="gnbArea">
		<div data-url="/index.do" <s:if test="#request['struts.actionMapping'].name.equals('index')">class="on"</s:if>><i class="fa fa-newspaper-o fa-2x" aria-hidden="true"></i></div>
		<div data-url="/favoriteGame/favoriteGame.do" <s:if test="#request['struts.actionMapping'].name.equals('favoriteGame')">class="on"</s:if>><i class="fa fa-heart fa-2x" aria-hidden="true"></i></div>
		<div data-url="/gameChart/gameChart.do" <s:if test="#request['struts.actionMapping'].name.equals('gameChart')">class="on"</s:if>><i class="fa fa-line-chart fa-2x" aria-hidden="true"></i></div>
		<div data-url="/search/search.do" <s:if test="#request['struts.actionMapping'].name.equals('search')">class="on"</s:if>><i class="fa fa-search fa-2x" aria-hidden="true"></i></div>
		<div data-url="/puzzle/puzzleWrite.do" <s:if test="#request['struts.actionMapping'].name.equals('puzzleWrite')">class="on"</s:if>><i class="fa fa-upload fa-2x" aria-hidden="true"></i></div>
		<div data-url="/myPage/myPage.do" <s:if test="#request['struts.actionMapping'].name.equals('myPage')">class="on"</s:if>><i class="fa fa-user fa-2x" aria-hidden="true"></i></div>
	</div>
</s:else>
<div class="alert alert-danger" id="alertSection">
	<strong id="alertTitle"></strong>
	<span class="ml5" id="alertMsg"></span>
</div>