/** 将form表单转换成js Object **/
$.fn.serializeObject = function() {
	var o = {};
	var a = this.serializeArray();
	$.each(a, function() {
		if (o[this.name] !== undefined) {
			if (!o[this.name].push) {
				o[this.name] = [o[this.name]];
			}
			o[this.name].push(this.value || '');
		} else {
			o[this.name] = this.value || '';
		}
	});
	return o;
};


function getFullScreen(id){
	var fullScreenHeight;
	
	var windowHeight = $(window).height();
	var bodyMarginTop = parseInt($("body").css("margin-top"));
	var bodyMarginBottom = parseInt($("body").css("margin-bottom"));
	
	fullScreenHeight = windowHeight - bodyMarginTop - bodyMarginBottom;
	
	var $children = $("body").children();
	
	for(var i=0,len=$children.length; i<len; i++){
		var $child = $children.eq(i);
		
		if(!$child.is(":hidden") && $child.attr("id")!=id && $child.find("#"+id).length==0){
			fullScreenHeight = fullScreenHeight - $child.outerHeight();
			fullScreenHeight = fullScreenHeight - parseInt($child.css("margin-top"));
			fullScreenHeight = fullScreenHeight - parseInt($child.css("margin-bottom"));
		}
	}
	
	return fullScreenHeight;
}