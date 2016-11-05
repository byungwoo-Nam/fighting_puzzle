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
				
				// 해시태그입력
				$("#hashtag").blur(function(){
					var p = new RegExp("\\#([0-9a-zA-Z가-힣]*)", "g");
					var m = [];
					m = $("#hashtag").val().match(p);
					var hashStr = "";
					var hashCnt = 0;
					$("#hashtag").val("");
					for(var i=0; i<m.length; i++) {
						if(m[i] != "#"){
							hashStr += m[i] + " ";
							hashCnt++;
						}
						if(hashCnt >= 5){
							break;
						}
					}
					$("#hashtag").val(hashStr);
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
				<div class="row mb15">
					<div class="col-xs-4 col-sm-4 col-md-4 col-lg-4 ta-c">
						<label for="" class="control-label"><i class="fa fa-puzzle-piece fa-3x" aria-hidden="true"></i></label>
					</div>
					<div tabIndex="2" id="col_check" class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
						<s:select id="col" name="col" cssClass="form-control input-lg" list="CodeConfig.puzzleSizeJson" headerKey="" headerValue="가로" />
					</div>
					<div class="col-xs-2 col-sm-2 col-md-2 col-lg-2"><i class="fa fa-times fa-3x" aria-hidden="true"></i></div>
					<div tabIndex="3" id="row_check" class="col-xs-3 col-sm-3 col-md-3 col-lg-3">
						<s:select id="row" name="row" cssClass="form-control input-lg" list="CodeConfig.puzzleSizeJson" headerKey="" headerValue="세로" />
					</div>
				</div>
				<div tabIndex="4" id="hashtag_check">
					<input type="text" id="hashtag" name="hashtag" class="form-control input-lg" placeholder="해시태그 입력...(최대 5개)" />
				</div>
				<div class="mt20 clear">
					<button type="submit" class="btn btn-primary btn-block btn-lg" >등록하기</button>
				</div> 
			</div>
		</s:form>
	</body>
</html>