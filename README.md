# android-interfaced-webview

## Broad picture

It would be great if everything in our apps could be native. But it's not the case.

Very often we have to integrate some web content into our apps:
- embed elements from social networks (Facebook, YouTube, Instagram or Twitter posts)
- ads
- login/registration/purchase/other templates
- iframes of content built by other teams in the same company - graphics, interactive elements
- other cases

Often we use pure WebView implementation, that results in many small, but annoying issues.

The core issue with "external" content not developed by your own company (social embeds, ads, etc) is that when webview is embedded inside RecyclerView, it's height is not often correct and recalculated properly.

The internal content usually works better, but very often integration of such content can be improved. Here are some examples:
- if you display payment templates via webview, it's often wanted to have some kind of A/B testing run on them, showing different offers to different users
- in login/registration/payment templates it's often required that clicking on buttons works natively. For example, there could be "Register" button on a payment template that should lead to a native Registration page (if you have one)
- it's often needed to somehow get parameters from the native app. For example, often js code needs to do some tracking and tracking identifier should match   the one in the native app, so it needs to be obtained from the app or passed by the app into webview, but you don't want this identifier to present in every webview you load
- when webview starts a long-running operation, it would be great to show native loading indicator instead of the something in the web

All these points usually require a more tight cooperation with the team that builds web elements and templates.

And, at the same time, a better communication bridge is needed between Native and Javascript world.

**android-interfaced-webview** is such a communication bridge.

## Purpose

Purpose of **android-interfaced-webview** is to have a better communication between Native app and Javascript codebase that is usually built within your company or you have control over.

## What can it do

### Js-Native-Js communication

**android-interaced-webview** serves a low-level implementation that adds support for Promise-based js calls of native methods and allows to listen for webview document height changes.

It provides 2 small js scripts that are injected after every page loads inside webview:
- *native.js* that allows to asynchronously call any defined native method
- *height.js* that adds a listener of the document height, calling a native "updateHeight(height)" method

As a result, every webpage loaded in **android-interfaced-webview** will have a new **asynchronous** method available in javacript world

```runNativeMethod(<supported_method_name>, <valid_json_for_parameters>)```

`runNativeMethod` returns a Promise:
- in case of success, value will be a json passed from native
- in case of error, error will be an error message from native

Web-development team is free to call any of the supported methods via `runNativeMethod`.

### Automatic height updates

When setting up the webview, you can pass in a callback, that will be called every time webpage body height changes.

In the callback you can either call some manual implementation or use one of the height update methods available on the IntefacedWebView

## How to use it

Call a setup method on the webview and provide NativeInterface implementation. These would be the methods that you will support, these methods can be called from javascript.

Every method on NativeInterface can have only one optional parameter of `JsonElement`. This would allow javascript to pass parameters into native.

Every method on NativeInterface has to return a String that can be a valid Json String - it will be passed back to javascript promise resolve method.

```
webview.setup(
            FeaturesConfig(
                fixFaviconLoadErrors = true,
                supportHeightUpdates = true
            ),
            object : NativeInterface {
                fun showLoading(paramsJson: JsonElement?): String {

                }

                fun hideLoading(paramsJson: JsonElement?): String {

                }
            }
        )
```

## WebViewClients

By default, library attaches `DelegatedWebViewClient` and calls `injectScripts` method in `onPageFinished`.

If you want to attach a custom WebViewClient, be sure to also call `InterfacedWebView.injectScripts` when page finished loading.

### DelegatedWebViewClient

Wraps the regular WebView client and allows to attach a delegate for:

```
interface WebViewClientInterface {
    fun onUrlClicked(url: String): Boolean {
        return false
    }
    fun onPageFinished(url: String) {}
    fun onPageStarted(url: String) {}
    fun onError(error: Error, failingUrl: String?) {}
}
```

Use it to react on some things happening inside webview.

## Recommendations

With **android-interfaced-webview** your Web team can expect to have `runNativeMethod` present in every template they build. Method will become available after the page was loaded.

To ensure smooth implementation, it is recommended that your Web team implements a wrapper library using their favorite technology stack, that would be calling native methods via `runNativeMethod`. Native development team and Web development team **must** agree on which methods are supported and what is the expected functionality.

## Recipes

### AB-testing web templates

Have some variables in Firebase RemoteConfig and have a method on NativeInterface that can allow Web development team to read a value of a Remote Config parameter. Remote Config parameter values could be changed using A/B test and thus web-built template will provide different implementation to different user groups.

### Handling url clicks natively

Have a method on NativeInterface that will be providing a supported native app schema in json format. For example
```
object: NativeInterface {
  fun getNativeSchema(param: JsonElement?): String = """
{
    "login": "http://login",
    "register": "http://register",
    "close": "http://close"
}
  """.trimIndent()
}
```

And have Web team use the agreed schema on appropriate buttons on the web template.

Attach a custom DelegatedWebViewClient to the IntefacedWebView and listen for `onUrlClicked` to be called. Check for `url` and if it's one of your Schema-supported urls - return `true` from the method to override url handling and do something natively.

As another option, you could define native methods directly and have web-team invoke javascript on click on buttons calling those native methods via `runNativeMethod`.


