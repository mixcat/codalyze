	function parseHash(hashString) {
		var hash = {}
		if (hashString != '') {
			$.each(hashString.substring(1).split('&'),function(i, part) {
				var split = part.split('=');
				hash[split[0]] = split[1];
			});
		}
		return hash;
	}
	
	function updateHashString(hash) {
		var hashString = '';
		for(var key in hash) {
			if (hashString.length) hashString += '&';
			hashString += (key + '=' + hash[key]);
		}
		if (hashString.length) {
			window.location.hash = '#' + hashString;
		}
	}
	
	function setHashParameter(hash, name, value) {
		hash[name] = value;
		updateHashString(hash);
	}
	
	function cleanString(string) {
		if (!string) return '';
		return string.replace(/\\/g,'\\\\');
	}
	
	function buildItemView(item, width, height) {
		var thumbnail_url = item.media.m.replace('_m.', '_s.');
		var normal_url = item.media.m.replace('_m.','.');
		var big_url = item.media.m.replace('_m.','_b.');
		return $('<li class="faw-image-list-item"/>').append(
			$('<a class="faw-image-tmb-link" title="faw-image-normal"/>').attr('href', normal_url)
			.append(
				$('<img width="75" height="75" class="faw-image-tmb"/>')
				.attr("src", thumbnail_url)
				.attr('alt', cleanString(item.title))
			)
		).append(
				$('<a title="faw-image-big"/>').attr('href', big_url)	
		).append(
				$('<a title="faw-image-original-href"/>').attr('href', item.link)
		);
	};
	
	function displayFlickrStream(data) {
		$('.faw-image-list').html(' ');
		
		$.each(data.items, function(i,item) {
			$('.faw-container .faw-image-list').append(buildItemView(item));
		});
			
		$('.faw-image-tmb-link').click(function() {
			var source = ($('.faw-ctrl-big').hasClass('faw-ctrl-selected')) ? 'a[title=faw-image-big]' : 'a[title=faw-image-normal]';
			var imageSrc = $(this).parent().find(source).attr('href');
			var imageTitle = $(this).find('img').attr('alt');
			var originalLink = $(this).parent().find('a[title=faw-image-original-href]').attr('href');
			
			$('.faw-image-tmb-link-selected').removeClass('faw-image-tmb-link-selected');
			$(this).addClass('faw-image-tmb-link-selected');
			$('.image-view').attr('src', imageSrc);
			$('.faw-image-title').html(imageTitle);
			$('.faw-original-location-link').attr('href', originalLink);
			
			setHashParameter(hash, 'l', originalLink);
			
			return false;
		});
		
		$('.faw-image-list-item').each(function(i) {
			if (i%4 == 0) $(this).addClass('first-item');
			else if (i%4 == 3) { $(this).addClass('last-item').after('<br class="clear"/>'); }
			else $(this).addClass('central-item');
			if (i<4) $(this).addClass('first-line');
			if (i==data.items.length-1) $(this).addClass('last-item');
		});
		
		if(hash.l) {
			$('a[href='+hash.l+']').parent().find('.faw-image-tmb-link').click();
		}
		else {
			$('.faw-image-list-item:first a').click();
		}
	};
	
	function selectCurrentSetButton(name) {
		$('.faw-set-selection-current').removeClass('faw-set-selection-current');
		$('a[title=' + name +']').addClass('faw-set-selection-current');
	};
	
	var hash = {};
	try {
		hash = parseHash(location.hash);
	} catch(ex) {
	}
	
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
				setHashParameter(hash, 's', $(this).attr('title'));
				$(this).blur();
				return false;
			});
		});
		
		if(hash.s) {
			$('.faw-feed-url[title='+hash.s+']').click();
		}
		else {
			$('.faw-set-selection-current').click();
		}
		
	});