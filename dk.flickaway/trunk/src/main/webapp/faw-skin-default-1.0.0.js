faw_skin_default = {
	afterStreamLoaded: function() {
		// fix thumbnail list to display as a grid
		var length = $('.faw-image-list-item').size();
		$('.faw-image-list-item').each(function(i) {
			if (i%4 == 0) $(this).addClass('first-item');
			else if (i%4 == 3) { $(this).addClass('last-item').after('<br class="clear"/>'); }
			else $(this).addClass('central-item');
			if (i<4) $(this).addClass('first-line');
			if (i==length-1) $(this).addClass('last-item');
		});
	}
}