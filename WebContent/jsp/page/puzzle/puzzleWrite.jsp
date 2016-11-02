<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
			$(function(){
				// 배너 이미지
				$('#puzzleUrl').filer({
				    limit: 1,
				    maxSize: 10,
				    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
				    showThumbs: true,
				    changeInput: FilerInput.puzzleUpload,
				    templates: FilerTemplate.edit,
				    theme: "dragdropbox"
				});
			});
		</script>
	</head>
	<body>
		<s:include value="/jsp/page/include/gnb.jsp" />
		<s:form id="puzzle" name="puzzleEditor" data-mode="EditorForm"  cssClass="dib" method="post" namespace="/puzzle" action="puzzleWriteAction" theme="simple" enctype="multipart/form-data" data-confirm-msg="퍼즐을 등록 하시겠습니까?">
			<div class="container">
				<div tabIndex="1" id="puzzleUrl_check">
					<input type="file" id="puzzleUrl" name="puzzleUrl" accept="image/*" />
				</div>
				<div class="mt20 clear">
					<button type="submit" class="btn btn-primary btn-block btn-lg" >등록하기</button>
				</div> 
			</div>
		</s:form>
	</body>
</html>