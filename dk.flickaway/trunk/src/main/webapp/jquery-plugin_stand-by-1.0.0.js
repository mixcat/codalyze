(function($) {
	$.fn.standby = function(settings, delegates) {
		function left() {
			$(target).animate({"left": "-=17em"}, "slow", right );
		}
		
		function right() {
			$(target).animate({"left": "+=17em"}, "fast", left );
		}
		
		function stop() {
			if (!running || --count > 0) return;
			running = false;
			$(target).stop();
			$(target).css('left', '0em');
			$(target).hide();
		}
		
		function start() {
			count++;
			if (running) return;
			running = true;
			right();
			$(target).show();
			$(target).css('left', '0em');
			if (timeout) clearTimeout(timeout);
			timeout = setTimeout(function() {
				count = 0;
				running = false;
				$(target).stop();
				$(target).css('left', '0em');
				$(target).hide();
			}, 4000);
		}
		
		var running = false;
		var target = $(this).find('.stb-middle');
		var timeout = null;
		var count = 0;
		if (delegates) {
			delegates.start = start;
			delegates.stop = stop;
		}
	}
})(jQuery);