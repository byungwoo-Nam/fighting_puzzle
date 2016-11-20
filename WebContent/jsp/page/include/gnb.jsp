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
		<button type="button" class="btn btn-basic backBtn pointer">돌아가기</button>
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
		<div data-url="/index.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('index')">on</s:if>"><i class="fa fa-newspaper-o fa-2x" aria-hidden="true"></i></div>
		<div data-url="/favoriteGame/favoriteGame.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('favoriteGame')">on</s:if>"><i class="fa fa-heart fa-2x" aria-hidden="true"></i></div>
		<div data-url="/gameChart/gameChart.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('gameChart')">on</s:if>"><i class="fa fa-line-chart fa-2x" aria-hidden="true"></i></div>
		<div data-url="/search/search.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('search')">on</s:if>"><i class="fa fa-search fa-2x" aria-hidden="true"></i></div>
		<div data-url="/puzzle/puzzleWrite.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('puzzleWrite')">on</s:if>"><i class="fa fa-upload fa-2x" aria-hidden="true"></i></div>
		<div data-url="/user/userMain.do" class="pointer <s:if test="#request['struts.actionMapping'].name.equals('userMain')">on</s:if>"><i class="fa fa-user fa-2x" aria-hidden="true"></i></div>
	</div>
</s:else>
<div class="alert alert-danger" id="alertSection">
	<strong id="alertTitle"></strong>
	<span class="ml5" id="alertMsg"></span>
</div>