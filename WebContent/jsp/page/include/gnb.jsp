<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="gnbArea">
	<div data-url="/" <s:if test="#request['struts.actionMapping'].name.equals('index')">class="on"</s:if>><i class="fa fa-newspaper-o fa-2x" aria-hidden="true"></i></div>
	<div data-url="/myGame/myGame.do" <s:if test="#request['struts.actionMapping'].name.equals('myGame')">class="on"</s:if>><i class="fa fa-gamepad fa-2x" aria-hidden="true"></i></div>
	<div data-url="/gameChart/gameChart.do" <s:if test="#request['struts.actionMapping'].name.equals('gameChart')">class="on"</s:if>><i class="fa fa-line-chart fa-2x" aria-hidden="true"></i></div>
	<div data-url="/search/search.do" <s:if test="#request['struts.actionMapping'].name.equals('search')">class="on"</s:if>><i class="fa fa-search fa-2x" aria-hidden="true"></i></div>
	<div data-url="/myPage/myPage.do" <s:if test="#request['struts.actionMapping'].name.equals('myPage')">class="on"</s:if>><i class="fa fa-user fa-2x" aria-hidden="true"></i></div>
</div>