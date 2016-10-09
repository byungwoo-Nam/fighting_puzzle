$(document).on("click",".gnbArea div",function(e){
	var url = $(this).attr("data-url");
	$(location).attr("href",url);
	e.preventDefault();
});

$(document).on("click",".viewPuzzleMain",function(e){
	var url = "/puzzle/puzzleMain.do";
	$(location).attr("href",url);
	e.preventDefault();
});

$(document).on("click",".backBtn",function(e){
	history.back();
	e.preventDefault();
});