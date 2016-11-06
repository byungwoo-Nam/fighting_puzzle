/**
 * jquery plugin to create image fpp
 */

(function( $ ) {
	/**
	 * Private class, for dealing with fpp thingy
	 * @param: elem (jQuery DOM object) - the target element
	 * @param: options (Object) - with properties
	 */
	
	var fpp = function(elem, options) {
		this.elem = elem;
		this.options = options;

		// check if this exist or not
		this.options.image = this.elem.children('img')[0].src;
		if (typeof this.options.image == 'undefined') return false;

		var _this = this;

		var img = new Image();
		img.onload = function() {
			_this.imageLoaded();
		}
		
		img.src = this.options.image;
		
		$(document).on("click","#puzzleStart",function(e){
			_this.puzzleTimer();
		});
		
		return this;
	}
	
	fpp.prototype.puzzleTimer = function(){
		this.T = {};
	   	this.T.timerObj = $(".timePrint")
	   	
		var _this = this;
	   	window.addEventListener('blur', function(e) {
	   		e.preventDefault();
	   		//	멈추
//	   		stopTimer();
	   	}, false);
	   	
	   	this.timerStart();

		function clearTimer() {
		  clearInterval(T.timerInterval);
		  this.T.timerObj.html("00:00"); // reset timer to all zeros
		  this.T.difference = 0;

//		  document.getElementById('stop').style.display="none";
//		  document.getElementById('go').style.display="inline";
//		  document.getElementById('clear').style.display="none";
		}
	}
		
	fpp.prototype.timerStart = function(){
		var _this = this;
		// save start time
		this.T.timerStarted = new Date().getTime()

		if (this.T.difference > 0) {
			this.T.timerStarted = this.T.timerStarted - this.T.difference;
		}
		// update timer periodically
		this.T.timerInterval = setInterval(function() {
			_this.displayTimer()
		}, 10);

	  // show / hide the relevant buttons:
//		document.getElementById('go').style.display="none";
//		document.getElementById('stop').style.display="inline";
//		document.getElementById('clear').style.display="none";
	}
	
	fpp.prototype.displayTimer = function(){
		  // initilized all local variables:
		  var minutes='00', miliseconds=0, seconds='00',
		  time = '',
		  timeNow = new Date().getTime(); // timestamp (miliseconds)

		  this.T.difference = timeNow - this.T.timerStarted;

		  // milliseconds
		  if(this.T.difference > 10) {
		    miliseconds = Math.floor((this.T.difference % 1000) / 10);
		    if(miliseconds < 10) {
		      miliseconds = '0'+String(miliseconds);
		    }
		  }
		  // seconds
		  if(this.T.difference > 1000) {
		    seconds = Math.floor(this.T.difference / 1000);
//		    if (seconds > 60) {
//		      seconds = seconds % 60;
//		    }
		    if(seconds < 10) {
		      seconds = '0'+String(seconds);
		    }
		  }

		  // minutes
		  if(this.T.difference > 60000) {
		    minutes = Math.floor(this.T.difference/60000);
//		    if (minutes > 60) {
//		      minutes = minutes % 60;
//		    }
		    if(minutes < 10) {
		      minutes = '0'+String(minutes);
		    }
		  }

		  // hours
		  if(this.T.difference > 3600000) {
		    hours = Math.floor(this.T.difference/3600000);
		    // if (hours > 24) {
		    // 	hours = hours % 24;
		    // }
		    if(hours < 10) {
		      hours = '0'+String(hours);
		    }
		  }

		  time = '';
//		  time  =  hours   + ':'
//		  time += minutes + ':'
		  time += seconds + ':'
		  time += miliseconds;

		  this.T.timerObj.html(time);
		  if(seconds>999){
			  // 시간초과
			  _this.timerStop();
		  }
		}
	
	fpp.prototype.timerStop = function(){
		clearInterval(this.T.timerInterval); // stop updating the timer

//		document.getElementById('stop').style.display="none";
//		document.getElementById('go').style.display="inline";
//		document.getElementById('clear').style.display="inline";

	}

	/**
	 * Function (Event listener called) when the source image has loaded
	 * Does all init tasks
	 */
	
	fpp.prototype.saveRecord = function(){
		var data = {"ajaxMode":"dataInsert", "dataMode":"record", "puzzle_seq":$("#seq").val(), "time":this.T.difference};
		
		data.url = "/ajaxConnect.do";
		JSON.parse(getAjaxData(data));
	}
	
	fpp.prototype.imageLoaded = function() {
		this.elem.append('<div class="fpp_panel_"></div>');
		this.options.width = this.elem.children("img")[0].width;
		this.options.height = this.elem.children("img")[0].height;
		this.obj = this.elem.children(".fpp_panel_");

		//this.obj.css("width", parseInt(this.options.width) + parseInt(this.options.x * this.options.margin * 2) +parseInt(this.options.error) +"px").css("height", parseInt(this.options.height) + parseInt(this.options.y * this.options.margin * 2) +"px");
		this.obj.css("width", $("#imgArea").width());
//		console.log(this.obj.width());
		this.obj.css("line-height", "10px");

		w = Math.floor(this.options.width / this.options.x);
		h = Math.floor(this.options.height /this.options.y);

		var _this = this;
		
		for(i=0;i<this.options.x;i++) {
			for(j=0;j<this.options.y;j++) {
				pos = "block" +i +j;
				this.obj.append("<div class='draggable' pos='" +pos +"'></div>");
				this.obj.children("div[pos='" +pos +"']")
							.css("background-position", "-" +(j*w) +"px -" +(i*h) +"px")
							.css("background-size", this.options.width+"px " + this.options.height+"px")
							.css("width", w +"px")
							.css("height", h+"px")
							.css("background-image", "url("+this.options.image+")")
							.css("display", "inline-block")
							.css("margin", this.options.margin)
							.css("background-repeat", "no-repeat")
							.css("transition", "background-position .5s ease-out")
							.droppable({over: this.newImgOver, out: this.newImgOut})
							.draggable({containment: "parent", scroll: false, revert: this.imgChange, stop: function(){
								var result = true;
								$(".draggable").each(function(idx){
									if(idx != $(this).attr("origIDX")){
										result = false;
										return false;
									}
								});
								
								if(result){
									_this.timerStop();
									_this.saveRecord();
									$("#endPop").trigger("click");
								}
								
							}});
			}
		}
		this.elem.children("img").hide();
		this.obj.fadeIn();
		this.animate(this.elem);
	};
	
	fpp.prototype.imgChange = function(event, ui) {
		var oldObj ={}; 
		var newObj = {};

		oldObj.pos = $(".ui-draggable-dragging").attr("pos");
		oldObj.bp = $(".ui-draggable-dragging").css("background-position");
		oldObj.origIDX = $(".ui-draggable-dragging").attr("origIDX");
		
		newObj.pos = $(".puzzleTarget").attr("pos");
		newObj.bp = $(".puzzleTarget").css("background-position");
		newObj.origIDX = $(".puzzleTarget").attr("origIDX");
		
		$(".puzzleTarget").css("background-position", oldObj.bp);
		$(".puzzleTarget").attr("pos", oldObj.pos);
		$(".puzzleTarget").attr("origIDX", oldObj.origIDX);
		
		$(".ui-draggable-dragging").css("background-position", newObj.bp);
		$(".ui-draggable-dragging").attr("pos", newObj.pos);
		$(".ui-draggable-dragging").attr("origIDX", newObj.origIDX);
		
		$(".puzzleTarget").removeClass("puzzleTarget");
		
		return true;
	};
	
	fpp.prototype.newImgOver = function(event, ui){
		$(event.target).addClass("puzzleTarget");
	};
	
	fpp.prototype.newImgOut = function(event, ui){
		$(event.target).removeClass("puzzleTarget");
	};

	/**
	 * Function to animate blocks in random fashion
	 * @param: obj is the target element
	 */
	fpp.prototype.animate = function(obj) {
		w = Math.floor(this.options.width / this.options.x);
		h = Math.floor(this.options.height / this.options.y);
		var len = obj.children(".fpp_panel_").children("div").length;

		var selected = [];
		if (this.options.distinct) {
			for (i = 0; i < this.options.x; i++) {
				selected[i] = [];
				for (j = 0; j < this.options.y; j++) {
					selected[i][j] = false;
				}
			}
		}
		
		for(i = 0; i < len; i++) {
			var randI = Math.floor((Math.random() * 1000)) % this.options.x;
			var randJ = Math.floor((Math.random() * 1000)) % this.options.y;

			if (this.options.distinct && selected[randI][randJ]) {
				for (x = 0; x < this.options.x; x++) {
					for (y = 0; y < this.options.y; y++) {
						if (!selected[x][y]) {
							randI = x;
							randJ = y;
							break;
						}
					}
				}
			}
			selected[randI][randJ] = true;
			var bp = "-" +(randI * w) +"px -" +(randJ * h) +"px";
			obj.children(".fpp_panel_").children("div:eq(" +i +")").css("background-position",bp).attr("origIDX", randI+randJ*this.options.x)
		}
		var _this = this;

//		if(Math.floor((Math.random()*5)+0) == 2) {
//			t = setTimeout(function() {
//				_this.rearrange(obj)
//			},this.options.freq);
//		} else {
//			t = setTimeout(function(){
//				_this.animate(obj);
//			},this.options.freq);
//		}
	}

	/**
	 * Function to reorder blocks to right position
	 * @param: obj is the target element
	 */
	fpp.prototype.rearrange = function(obj) {
		w = Math.floor(this.options.width / this.options.x);
		h = Math.floor(this.options.height / this.options.y);
		for( i=0; i<this.options.x; i++) {
			for( j=0; j<this.options.y; j++) {
				pos = "block" +i +j;
				obj.children(".fpp_panel_").children("div[pos='" +pos +"']")
						.css("background-position", "-" +(j*w) +"px -" +(i*h) +"px")
						.css("width", w +"px")
						.css("height", h+"px")
						.css("background-image", "url(" +this.options.image+")");
			}
		}
		var _this = this;
		t = setTimeout(function(){
			_this.animate(obj)
		}, this.options.freq);
	}

	/**
	 * Jquery method contructor
	 * @param: options (Object) - options
	 */
	$.fn.fpp = function(options)
	{
		var settings = $.extend( {}, $.fn.fpp.defaults, options );
		$(this).each(function() {
			return new fpp($(this), settings);
		});
	}
	
	/**
	 * default public properties
	 */
	$.fn.fpp.defaults = {
		width: 0,
		height: 0,
		x : 4,
		y : 4,
		margin : 1,
		error : 16,
		freq :2000,
		distinct: true,
		image: ""
	}
	
}( jQuery ));