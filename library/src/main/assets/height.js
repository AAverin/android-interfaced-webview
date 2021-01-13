// create an Observer instance
var resizeObserver = new ResizeObserver(function (entries) {
  var height = entries[0].target.clientHeight
  if (window.JSNativeAsync) {
    JSNativeAsync.updateHeight(height)
  }
})

// start observing a DOM node
resizeObserver.observe(document.body)
