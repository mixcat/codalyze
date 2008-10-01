$('<link rel="stylesheet" type="text/css" media="screen" href="faw-skin-default.css"/>').appendTo('head');
$(function() {
	$.ajax({url:'faw-core-1.0.0.js', dataType:'script', cache:true});
});