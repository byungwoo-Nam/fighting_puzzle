<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<jsp:include page="/jsp/include/head.jsp" flush="false">
			<jsp:param value=""  name="title"/>
		</jsp:include>
		<script type="text/javascript">
			$(function(){
				var filter = "win16|win32|win64|mac";
				 
				if(navigator.platform){
					if(0 > filter.indexOf(navigator.platform.toLowerCase())){
						alert("Mobile");
					}else{
						alert("PC");
					}
				}
			});
		</script>
	</head>
	<body>
		<jsp:include page="/jsp/include/gnb.jsp" flush="false" />
		<div class="admin_container">
			<div class="adminContentArea">
				<img src="/jsp/common/img/sample.png" style="width:100%;" />
			</div>
		</div>
	</body>
</html>