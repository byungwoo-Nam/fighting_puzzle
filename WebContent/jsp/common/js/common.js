$(document).on("click",".gnbArea div",function(e){
	var url = $(this).attr("data-url");
	$(location).attr("href",url);
	e.preventDefault();
});

$(document).on("click",".viewPuzzleMain",function(e){
	var url = "/puzzle/puzzleMain.do";
	var seq = $(this).attr("data-idx");
	url += "?seq=" + seq;
	$(location).attr("href",url);
	e.preventDefault();
});

$(document).on("click",".backBtn",function(e){
	history.back();
	e.preventDefault();
});

$(document).on("click",":button[type='submit']",function(e){
	if(validateCheck($(this).parents("form"))){
		if(confirm($(this).parents("form").attr("data-confirm-msg"))){
			return true;
		}
	}
	return false;
});

function getAjaxData(data){
	var rtnData = "";
	$.ajax({
		url: data.url,
		type: "post",
		data: {"data":JSON.stringify(data)},
		dataType:"text",
		async: false,
		success:function(result){
			rtnData = result;
		},
		error: function(xhr,status, error){
			alert("에러발생");
		}
	});
	return rtnData;
}

function validateCheck(obj){
	var rtn = false;
	var data = {"ajaxMode":"formValidate", "formID":obj.attr("id")+obj.attr("data-mode")};
	obj.find("input[name!='']").each(function(){
		if($(this).attr("type")=="file"){
			data[$(this).attr("name")] = $(this)[0].jFiler.files_list.length;
		}else if($(this).attr("type")=="radio"){
			data[$(this).attr("name")] = $(':radio[name="'+$(this).attr("name")+'"]:checked').val();
		}else{
			if($(this).attr("name") in data && $.isArray(data[$(this).attr("name")])){
				data[$(this).attr("name")].push($(this).val());
			}else{
				data[$(this).attr("name")] = data[$(this).attr("name")] = $(this).hasClass("arrayData") ?  [$(this).val()] : $(this).val();
			}
		}
	});
	obj.find("select[name!='']").each(function(){
		data[$(this).attr("name")] = $(this).val();
	});
	obj.find("textarea").each(function(){
		data[$(this).attr("name")] = $(this).val();
	});

	data.url = "/ajaxConnect.do";
	var jsonObj = JSON.parse(getAjaxData(data));
	if(!jsonObj.res){
		$("#test1").showAlert({"title":jsonObj.errorTitle, "msg":jsonObj.errorMsg});
		$("#"+jsonObj.elementID).focus();
	}else{
		rtn = true;
	}
	
	return rtn;
}

function blankCheck(s){
	var pattern = /^\s+|\s+$/g;
	return (s.replace( pattern, '' ) == "" ) ? true : false;
}

function convertMillisecond(ms){
	var d = (ms/1000.0);
	var digits = Math.pow(10, 2);
	var num = Math.floor(d * digits) / digits;
	return num.toFixed(2);
}

$.fn.showAlert = function(q) {
	$("#alertSection #alertTitle").html("[" + q.title + "]");
	$("#alertSection #alertMsg").html(q.msg);
	$("#alertSection").alert();
    $("#alertSection").fadeTo(2000, 500).slideUp(500);
}