

$(function() {
	$("#submit-query").click(function() {
		$.ajax({
			  type: "GET",
			  url: "/codalyze/api/v1/query/" + encodeURI($('#query').val()),
			  dataType: "script",
			  success: function(msg){
				$('#query-result').html(msg);
			}
		});
	});
});