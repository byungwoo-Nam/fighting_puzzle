<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<!DOCTYPE html>
<html lang="ko">
	<head>
		<s:include value="/jsp/page/include/head.jsp" />
		<script type="text/javascript">
		function fileDeleteOnUpdate(fileObj){
			var inputHTML = '<input type="hidden" name="' + fileObj.input + '" value="' + fileObj.name + '" />';
			$("#eventBanner").append(inputHTML);
		}
		$(function(){
			// 배너 이미지
			$('#puzzle_url').filer({
			    limit: 1,
			    maxSize: 10,
			    extensions: ['jpeg', 'jpg', 'gif', 'bmp', 'png'],
			    showThumbs: true,
			    changeInput: FilerInput.puzzleUpload,
			    templates: FilerTemplate.edit,
			    theme: "dragdropbox"
			});
			
			// 업데이트시 이미지 등록
			var banner_url = "<s:property value="eventBannerDTO.banner_url"/>";
			var banner_url_name = "<s:property value="eventBannerDTO.banner_url_name"/>";
			if(banner_url != ""){
				var banner_url_obj = $("#banner_url").prop("jFiler");
				var banner_url_data = {
						name: banner_url_name,
					    type: "image",
					    file: "http://"+ banner_url,
					    input: "banner_urlDeleteFileName"
				};
				banner_url_obj.append(banner_url_data);
				banner_url_obj.options.onRemove = function(dom, obj){
			    	fileDeleteOnUpdate(obj);
			    };
			}
		});
		</script>
	</head>
	<body>
		<s:include value="/jsp/page/include/gnb.jsp" />
		<s:form id="puzzle" name="puzzleEditor" data-mode="EditorForm"  cssClass="dib" method="post" namespace="/puzzle" action="puzzleWriteAction" theme="simple" enctype="multipart/form-data">
			<div class="container">
				<input type="file" id="puzzle_url" name="puzzle_url" accept="image/*" />
				<div class="mt20 clear">
					<button type="submit" id="gameStart" class="btn btn-primary btn-block" >등록하기</button>
				</div> 
			</div>
		</s:form>
	</body>
</html>