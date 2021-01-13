"use strict"

var runNativeMethod = function runNativeMethod(method, parametersJson) {
  var _isAndroid = function _isAndroid() {
    return window.JSNativeAsync
  }

  if (!_isAndroid()) {
    console.error("Android Native bridge is not available")
    return
  }

  return new Promise(function (resolve, reject) {
    var handler = "nativeAsyncHandler" + Math.floor(Math.random() * 100000)
    window[handler] = {
      resolveNative: function resolveNative(result) {
        resolve(result)
        delete window[handler]
      },
      rejectNative: function rejectNative(error) {
        reject(error)
        delete window[handler]
      },
    }
    JSNativeAsync.run(handler, method, parametersJson)
  })
}
