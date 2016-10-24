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
		<s:form id="puzzle" name="puzzleEditor" data-mode="EditorForm"  cssClass="dib" method="post" namespace="/puzzle" action="puzzleWriteAction" theme="simple" enctype="multipart/form-data">
			<div class="container">
				<input type="file" id="puzzle_url" name="puzzle_url" accept="image/*" capture="camera">
				<div class="mt20 clear">
					<input type="submit" class="writeActionBtn simpleBtn mb10 btn2 dib fr" value="등록" />
				</div> 
			</div>
		</s:form>
	</body>
</html>