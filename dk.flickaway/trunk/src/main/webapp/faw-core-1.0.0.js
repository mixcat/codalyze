/*html-in-js:faw-template-default-1.0.0.html*/
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

function applyHash(hashString) {
	var hash = {};
	try {
		hash = parseHash(location.hash);
	} catch(ex) {}
	if(hash.s) {
		$.getJSON($('.faw-feed-url[title='+hash.s+']').attr('href'), function(data) {
			$displayFlickrStream(data);
			if(hash.l) {
				displayImage($('a[href='+hash.l+']').parent().find('.faw-image-tmb-link'));
			}
		});
	}
}

var currentHashString = window.location.hash;
var hash = {};
function startHashChangePolling() {
	if(window.location.hash != currentHashString) {
		currentHashString = window.location.hash;
		applyHash(currentHashString);
	}
	setTimeout(function() { startHashChangePolling(); }, 500);
}
startHashChangePolling();

function updateHashString() {
	var hashString = '';
	for(var key in hash) {
		if (hashString.length) hashString += '&';
		hashString += (key + '=' + hash[key]);
	}
	if (hashString.length) {
		currentHashString = '#' + hashString;
		window.location.hash = '#' + hashString;
	}
}

function updateHashParameter(name, value) {
	if (hash[name] != value) {
		hash[name] = value;
	}
}

function cleanString(string) {
	if (!string) return '';
	return string.replace(/\\/g,'\\\\');
}

function buildItemView(item, width, height) {
	faw_skin_default.startWait()
	var thumbnail_url = item.media.m.replace('_m.', '_s.');
	var normal_url = item.media.m.replace('_m.','.');
	var big_url = item.media.m.replace('_m.','_b.');
	return $('<li class="faw-image-list-item"/>').append(
		$('<a class="faw-image-tmb-link" title="faw-image-normal"/>').attr('href', normal_url)
		.append(
			$('<img width="75" height="75" class="faw-image-tmb"/>')
			.attr("src", thumbnail_url)
			.attr('alt', cleanString(item.title))
			.load(function() {faw_skin_default.stopWait();})
		)
	).append(
			$('<a title="faw-image-big"/>').attr('href', big_url)	
	).append(
			$('<a title="faw-image-original-href"/>').attr('href', item.link)
	);
};

function selectCurrentSetButton(name) {
	$('.faw-set-selection-current').removeClass('faw-set-selection-current');
	$('a[title=' + name +']').addClass('faw-set-selection-current');
};

function displayImage(thumbnail) {
	var source = ($('.faw-ctrl-big').hasClass('faw-ctrl-selected')) ? 'a[title=faw-image-big]' : 'a[title=faw-image-normal]';
	var imageSrc = $(thumbnail).parent().find(source).attr('href');
	var imageTitle = $(thumbnail).find('img').attr('alt');
	var originalLink = $(thumbnail).parent().find('a[title=faw-image-original-href]').attr('href');
	
	$('.faw-image-tmb-link-selected').removeClass('faw-image-tmb-link-selected');
	$(thumbnail).addClass('faw-image-tmb-link-selected');
	$('.image-view').attr('src', imageSrc);
	$('.faw-image-title').html(imageTitle);
	$('.faw-original-location-link').attr('href', originalLink);
}

function $clickThumbnail() {
	faw_skin_default.startWait();
	displayImage($(this));
	var originalLink = $(this).parent().find('a[title=faw-image-original-href]').attr('href');
	updateHashParameter('l', originalLink);
	updateHashParameter('s', $('.faw-set-selection-current').attr('title'));
	updateHashString();
	return false;
}

function $clickSetSelection() {
	$(this).blur();
	selectCurrentSetButton($(this).attr('title'));
	$.getJSON($(this).attr('href'), function(data) {
		$displayFlickrStream(data);
		$('.faw-image-list-item:first a').click();
	});
	
	return false;
}

function $clickSizeCtrl() {
	$('.faw-ctrl-big, .faw-ctrl-normal').removeClass('faw-ctrl-selected');
	$(this).addClass('faw-ctrl-selected');
	$('.faw-image-tmb-link-selected').click();
}

function $displayFlickrStream(data) {
	// empty thumbnail list
	$('.faw-image-list').html(' ');
	
	// lay out thumbnails
	$.each(data.items, function(i,item) {
		$('.faw-container .faw-image-list').append(buildItemView(item));
	});
	
	$('.faw-image-tmb-link').click($clickThumbnail);
	
	
	faw_skin_default.afterStreamLoaded();
};

function MAIN() {
	
	try { hash = parseHash(location.hash); } catch(ex) {}
	
	$.ajax({
		url: 'faw-template-default-1.0.0.html',
		dataType: 'text',
		success: function(html) {
			$('.faw-skin-default').append('<div id="faw-root-element">'+html+'</div>');
			faw_skin_default.setup();
			$('.image-view').load(function() {faw_skin_default.stopWait();});
			$('body').ajaxStart(function() {
				faw_skin_default.startWait();
			});
			
			$('body').ajaxStop(function() {
				faw_skin_default.stopWait();
			});
			$('.faw-page-title').html($('head title').html());
			$('.faw-ctrl-big, .faw-ctrl-normal').click($clickSizeCtrl);
			$('link[rel=feed-source]').each(function() {
				var title = $(this).attr('title');
				var href = $(this).attr('href');
				$('.faw-set-selection').append(
					$('<a/>"')
					.attr({'title':title, 'href':href + '&format=json&jsoncallback=?', 'class':'faw-feed-url faw-ctrl'})
					.html(title)
					.click($clickSetSelection)
				);
			});
			
			
			
			if(hash.s) {
				$('.faw-feed-url[title='+hash.s+']').click();
			}
			else {
				$('.faw-set-selection-current').click();
			}
			
		}
	});
}

$(MAIN);