# Introduction #

One simple html page with very limited content:

  * remote library: jquery (google javascript libraries apis)
  * remote script: loader.js?iv=

<initial\_version>


  * skin name: class name of body element
  * page title
  * remote anchors to flickr feeds (full feed urls)

Loader.js?iv=

<initial\_version>



  * small and short-cached (~weeks)
  * **iv** is a numeric value that describes on which version this page was generated.
  * loader will always load the best version compatible with iv
  * loader detects the configured skin and loads the best css compatible with iv

Css

  * css skin files are versioned and configured as never-expire



# Details #

  * excellent simple doc on http caching: http://www.mnot.net/cache_docs/

# jot #

- core(js) carries the template(html). template and core must be compatible
- skin(css) hooks to template(html). skin and template must be compatible