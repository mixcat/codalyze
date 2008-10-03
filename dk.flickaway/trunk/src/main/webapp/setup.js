google.load("feeds", "1");
function glob(content) {
	alert('content');
	return true;
}

$("#msg").ajaxError(function(event, request, settings){
	alert(request.text);
	$(this).append("<li>Error requesting page " + settings.url + "</li>");
});

function lookupPhotostream(result) {
	if (result.error || result.url == null) {
		alert('could not retieve user info');
		return;
	}
	loadPhotostream(result.url);
}

var feeds = {};
authoId = null;
var feed_title = null;
function jsonFlickrFeed(data) {
	if (data.items && data.items.length > 0) {
		feed_title = data.title;
		authorId = data.items[0].author_id;
		findSets(authorId);
	}
}

function jsonFlickrApi(data) {
	$.each(data.photosets.photoset, function(i, photoset) {
		feeds[photoset.title._content] = 'http://api.flickr.com/services/feeds/photoset.gne?set='+photoset.id+'&nsid='+authorId;
	});
	
	var skeleton_feed_html = '\n';
	for (title in feeds) {
		skeleton_feed_html += '<link title="'+title+'" rel="feed-source" href="'+feeds[title]+'"></link>\n';
	}
	$.ajax({url:'skeleton.html', dataType:'html', success: function(html) {
		//$('head').append(skeleton_feed_html);
		//$('head title').html(feed_title);
		$('#prev').contents().find('head').append(skeleton_feed_html);
		$('#prev').contents().find('head title').html(feed_title);
		window.frames[0].faw_core_main();
		$('#prev').css('left','0em;')
		$('#prev').css('top','3em');
		$('#faw-setup .faw-setup-wait').hide();
		$('#faw-setup .faw-setup-preview').show();
		$('#faw-setup .faw-setup-skeleton').val(html.replace('<link title="feeds"></link>',skeleton_feed_html).replace('%%title%%',feed_title));
	}});
}

function findSets(authorId) {
	$.ajax({url:'http://api.flickr.com/services/rest/?method=flickr.photosets.getList&api_key=4145fe917d61d1c4d2716a08db89d6f1&user_id='+authorId+'&format=json&callback=foo', dataType:'jsonp'});
}
function loadPhotostream(url) {
	feeds['photostream'] = url.replace(/&format=.*$/,'').replace(/&lang=.*[$|&]/,'');
	$.ajax({url:url + '&format=json&callback=glob', dataType:'jsonp'});
}

$(function() {
	$('#feed-url-input-submit').click(function() {
		$('#faw-setup .faw-setup-begin').hide();
		$('#faw-setup .faw-setup-wait').show();
		google.feeds.lookupFeed($('#feed-url-input').val(), lookupPhotostream);
	});
	
	$('#faw-setup .faw-setup-preview').click(function() {
		$(this).hide();
		$('#prev').css('left','-500em;')
		$('#prev').css('top','-500em');
		$('#faw-setup .faw-setup-done').show();
		
	});
	
});