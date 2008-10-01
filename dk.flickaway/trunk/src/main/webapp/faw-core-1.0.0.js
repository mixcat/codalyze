(function($) {
	
	function buildItemView(item, width, height) {
		var thumbnail_url = item.media.m.replace('_m.', '_s.');
		var normal_url = item.media.m.replace('_m.','.');
		var big_url = item.media.m.replace('_m.','_b.');
		return $('<li class="faw-image-list-item"/>').append(
			$('<a class="faw-image-tmb-link" title="faw-image-normal"/>').attr('href', normal_url)
			.append(
				$('<img width="75" height="75" class="faw-image-tmb"/>')
				.attr("src", thumbnail_url)
				.attr('alt', item.title)
			)
		).append(
				$('<a title="faw-image-big"/>').attr('href', big_url)	
		);
	};
	
	function displayFlickrStream(data) {
		$('.faw-image-list').html(' ');
		
		$.each(data.items, function(i,item) {
			$('.faw-container .faw-image-list').append(buildItemView(item));
		});
			
		$('.faw-image-tmb-link').click(function() {
			$('.faw-image-tmb-link-selected').removeClass('faw-image-tmb-link-selected');
			$(this).addClass('faw-image-tmb-link-selected');
			var source = ($('.faw-ctrl-big').hasClass('faw-ctrl-selected')) ? 'a[title=faw-image-big]' : 'a[title=faw-image-normal]';
			$('.image-view').attr('src',$(this).parent().find(source).attr('href'));
			$('.faw-image-title').html($(this).find('img').attr('alt'));
			
			return false;
		});
		
		$('.faw-image-list-item').each(function(i) {
			if (i%4 == 0) $(this).addClass('first-item');
			else if (i%4 == 3) { $(this).addClass('last-item').after('<br class="clear"/>'); }
			else $(this).addClass('central-item');
			if (i<4) $(this).addClass('first-line');
			if (i==data.items.length-1) $(this).addClass('last-item');
		});
		
		$('.faw-image-list-item:first a').click();
	};
	
	function selectCurrentSetButton(name) {
		$('.faw-set-selection-current').removeClass('faw-set-selection-current');
		$('a[title=' + name +']').addClass('faw-set-selection-current');
	};
	
	$(function() {
		
		$('.faw-ctrl-big, .faw-ctrl-normal').click(function() {
			$('.faw-ctrl-big, .faw-ctrl-normal').removeClass('faw-ctrl-selected');
			$(this).addClass('faw-ctrl-selected');
			$('.faw-image-tmb-link-selected').click();
		});
		
		
		$(".faw-feed-url").each(function(i) {
			var href = $(this).attr('href');
			$(this).attr('href', href + '&format=json&jsoncallback=?')
			.html($(this).attr('title'))
			.appendTo('.faw-set-selection').click(function() {
				selectCurrentSetButton($(this).attr('title'));
				$.getJSON($(this).attr('href'), displayFlickrStream);
				$(this).blur();
				return false;
			});
		});
		
		$('.faw-set-selection-current').click();
		
	});
})(jQuery);