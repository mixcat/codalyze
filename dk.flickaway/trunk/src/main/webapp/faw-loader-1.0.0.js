$('<link rel="stylesheet" type="text/css" media="screen" href="faw-skin-default-1.0.0.css"/>').appendTo('head');

$(function() {
	//$.ajax({url:'faw-skin-default-1.0.0.js', dataType:'script', cache:true});
	$.ajax({url:'faw-core-1.0.0.js', dataType:'script', cache:true});
	//$.ajax({url:'faw-skin-default-pjs-1.0.0.css', dataType:'text', cache:true, success:function(text){
	//	eval(text.substring(3, text.search(/#end#/)));
	//}});
});