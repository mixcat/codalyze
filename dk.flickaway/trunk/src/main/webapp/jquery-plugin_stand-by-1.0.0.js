(function($) {
	
	$.fn.standby = function(settings, delegates) {
		
		function left() {
			$(target).animate({"left": "-=7em"}, "slow", right );
		}
		
		function right() {
			$(target).animate({"left": "+=7em"}, "slow", left );
		}
		
		function stop() {
			if (!running) return;
			running = false;
			$(target).stop();
			$(target).css('left', '0em');
			$(target).hide();
		}
		
		function start() {
			if (running) return;
			running = true;
			right();
			$(target).show();
			$(target).css('left', '0em');
		}
		
		var running = false;
		var target = $(this).find('.stb-middle');
		if (delegates) {
			delegates.start = start;
			delegates.stop = stop;
		}
	
	}
	
})(jQuery);