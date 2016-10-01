$(document).on("click",".gnbArea div",function(e){
	e.preventDefault();
	var url = $(this).attr("data-url");
	$(location).attr("href",url);
//	var pageNum = $(this).attr("id");
//	pageNum = pageNum.replace("page_","");
//	$(this).parents("form").find("#pageNum").val(pageNum);
//	$(this).parents("form").submit();
});