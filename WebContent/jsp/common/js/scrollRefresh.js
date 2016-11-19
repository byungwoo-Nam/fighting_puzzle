var scrollRefresh = true;
$(window).scroll(function(){
	if(scrollRefresh){
	    var scrolltop = $(window).scrollTop(); 
	    if( scrolltop > $(document).height() - $(window).height() - 1000 ){
	    	scrollRefresh = false;
	    	$("#scrollRefresh").trigger("click");
	    }
	}
});