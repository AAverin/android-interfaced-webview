// create an Observer instance
var resizeObserver = new ResizeObserver(function (entries) {
  var height = entries[0].target.clientHeight
  updateHeight(height)
})

var updateHeight = function(height) {
  if (window.JSNativeAsync) {
    JSNativeAsync.updateHeight(height)
  }
}

// start observing a DOM node
resizeObserver.observe(document.body)
if (window.JSNativeAsync) {
    updateHeight(document.body.clientHeight)
}