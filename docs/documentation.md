---
title: 'Documentation'
container_class: 'container-fluid'
---
{::options parse_block_html="true" /}
<div class="row">
<div class="col-3 left-menu">
Table of content:
{:.no_toc}
* Will be replaced with the ToC, excluding the "Contents" header
{:toc}
</div>
<div class="col-9 offset-md-3">

The library is slitted in several modules in order to you choose the one that more fits into your needs and reduce the dependencies

# Common
## Core
This module contains the basic model for HTTP and WS. This model is later user by the server and client

<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala/core_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala/core_2.13/latest/acsgh/mad/scala/core/index.html" target="_blank">Javadoc</a>
</li>
</ul>

# Server
Contains the basic routing logic it has two submodules, one to handle only http and other that give WS support

## Core


<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server/core_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server/core_2.13/latest/acsgh/mad/scala/server/index.html" target="_blank">Javadoc</a>
</li>
</ul>

### Server Builder
The main class is Server. This is immutable and build from the ServerBuilder. There is a server builder extension base on the server provider underneath.

<table class="table">
<tr>
<th>Property name</th>
<th>Type</th>
<th>Description</th>
<th>Default Value</th>
</tr>
<tr>
<td>name</td>
<td>String</td>
<td>Indicate the server name that is going to be used in the logs information and also in the Server header returned to all request</td>
<td>Mad Server</td>
</tr>
<tr>
<td>productionMode</td>
<td>Boolean</td>
<td>Indicate if the server is in production mode, it reduces information like exception stacktrace in the response and compact the json / html responses</td>
<td>false</td>
</tr>
<tr>
<td>ipAddress</td>
<td>String</td>
<td>The ip address where the server is going to listen</td>
<td>0.0.0.0</td>
</tr>
<tr>
<td>httpPort</td>
<td>Option[Int]</td>
<td>The http port</td>
<td>Some(6080)</td>
</tr>
<tr>
<td>httpsPort</td>
<td>Option[Int]</td>
<td>The https port, it requires a SSL config available</td>
<td>None</td>
</tr>
<tr>
<td>sslConfig</td>
<td>Option[SSLConfig]</td>
<td>The https SSL/TLS config</td>
<td>None</td>
</tr>
<tr>
<td>readerIdleTimeSeconds</td>
<td>Int</td>
<td>Reader idle timeout in seconds</td>
<td>60</td>
</tr>
<tr>
<td>http.workerThreads</td>
<td>Int</td>
<td>The amount of http workers threads that handle the request</td>
<td>30</td>
</tr>
<tr>
<td>http.workerTimeoutSeconds</td>
<td>Int</td>
<td>The amount of http workers threads that handle the request</td>
<td>60</td>
</tr>
<tr>
<td>ws.workerThreads</td>
<td>Int</td>
<td>The amount of websocket workers threads that handle the request</td>
<td>30</td>
</tr>
<tr>
<td>ws.workerTimeoutSeconds</td>
<td>Int</td>
<td>The amount of websocket workers threads that handle the request</td>
<td>60</td>
</tr>
</table>

### HTTP
#### Route
A Http route is defined as a function that receive an HttpRequestContext and return a HttpResponse.
The HttpRequestContext has:
<ul>
<li>Request: The original request received</li>
<li>Response: A response builder that is used to create the final response. It is the only attribute mutable in the context</li>
<li>Route: The current route, it can be used to have more internal information</li>
</ul>

The routes are added to the builder calling different http method like. It also supports wildcards

``` scala
builder.get("/") { implicit ctx =>
    responseBody("Hello world!")
}

builder.get("/{id}") { implicit ctx => Match /asd, /1234 but no /asd/123
    responseBody("Hello world!")
}

builder.get("/{path+}") { implicit ctx => // Matchs /hello and /hello/world
    responseBody("Hello world!")
}

builder.get("/*/id") { implicit ctx => // Matchs /person/id and /dog/id
    responseBody("Hello world!")
}
```

you can interact with the HttpRequestContext but in order to make it easy we have created directives that allow you to extract information from the request and modify the response

#### HttpDirectives

##### requestBody

Extract the request body bytes. The directive has also a way to parse content into something more manageable
``` scala
requestBody { bytes =>
}
```

If you want to convert the body you can use:
``` scala
requestBody[T] { bytes =>
}
```

But keep in mind that for that to work, it need an implicit HttpBodyReader[T] in the scope. We provide the String handler 
but the rest of readers is on your side. The reader also has a parameter called strictContentTypes, so if it true, and the content types received are not the one expected an 405 will be returned

##### redirect
Redirect one request into another location. The redirection could be in several ways provided by the RedirectStatus enum
``` scala
import acsgh.mad.scala.core.http.model.RedirectStatus

get("/v1") { implicit ctx =>
    redirect("/v2", RedirectStatus.FOUND)
}
```

##### error
The server return an error page. It is defined by the HttpErrorCodeHandlers configured.
``` scala
import acsgh.mad.scala.core.http.model.ResponseStatus._

get("/") { implicit ctx =>
    error(PRECONDITION_FAILS, Some("Invalid id"))
}
```

##### serve
Serve another route without redirect. The redirection is internal and transparent to the client
``` scala
get("/") { implicit ctx =>
    serve("/index.html")
}
```

##### requestParam
Extract from 1 to 15 path params from the url. The key is non case sensitive.

``` scala
get("/{id1}") { implicit ctx =>
    requestParam("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id2}/{id2}") { implicit ctx =>
    requestParam("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param by default is required, it could be optional, have a default value

``` scala
get("/{id1}") { implicit ctx =>
    requestParam("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestParam("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can by mix with other methods

``` scala
get("/{id1}") { implicit ctx =>
    requestParam("id1".as(Long)){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestParam("id1".as(Long).opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestParam("id1".as(Long).default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned

##### requestQuery
Extract from 1 to 15 query params from the url. Keep in mind that a query param can be repeated. The key is non case sensitive.

``` scala
get("/") { implicit ctx =>
    requestQuery("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    requestQuery("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param by default is required, it could be optional, have a default value or be a list

``` scala
get("/") { implicit ctx =>
    requestQuery("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestQuery("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestQuery("id1".list){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can by mix with other methods
                                                                                                                                   
``` scala
get("/{id1}") { implicit ctx =>
    requestQuery("id1".as(Long)){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestQuery("id1".as(Long).opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestQuery("id1".as(Long).default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/{id1}") { implicit ctx =>
    requestQuery("id1".as(Long).list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

##### requestHeader
Extract from 1 to 15 a header params from the request. Keep in mind that a header can be repeated. The key is non case sensitive.

``` scala
get("/") { implicit ctx =>
    requestHeader("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    requestHeader("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param by default is required, it could be optional, have a default value or be a list

``` scala
get("/") { implicit ctx =>
    requestHeader("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".list){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can by mix with other methods
                                                                                                                                   
``` scala
get("/{id1}") { implicit ctx =>
    requestHeader("id1".as(Long)){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".as(Long).opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".as(Long).default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/{id1}") { implicit ctx =>
    requestHeader("id1".as(Long).list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned


##### requestCookie
Extract from 1 to 15 cookie value from he request. Keep in mind that a cookie can be repeated. The key is non case sensitive.

``` scala
get("/") { implicit ctx =>
    requestCookie("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    requestCookie("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param by default is required, it could be optional, have a default value or be a list

``` scala
get("/") { implicit ctx =>
    requestCookie("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".list){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can by mix with other methods
                                                                                                                                   
``` scala
get("/{id1}") { implicit ctx =>
    requestCookie("id1".as(Long)){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".as(Long).opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".as(Long).default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/{id1}") { implicit ctx =>
    requestCookie("id1".as(Long).list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned
  
##### responseHeader

Add a header into the response, by default it is a String if you want to add any object different from it you need to add an implicit HttpParamWriter[T] in your scope.
 
 ``` scala
 get("/") { implicit ctx =>
     responseHeader("Content-Type", "Greetings"){
         responseBody(s"Hello world!}")
     }
 }
```
  
##### responseStatus

Add a response status into the response
 
 ``` scala
import acsgh.mad.scala.core.http.model.ResponseStatus._

 get("/") { implicit ctx =>
     responseStatus(NO_CONTENT){
         responseBody(s"Hello world!}")
     }
 }
```
  
##### responseCookie

Add a response cookie into the response
 
 ``` scala
import acsgh.mad.scala.core.http.model.HttpCookie

 get("/") { implicit ctx =>
     responseCookie(HttpCookie("sessionId", "123456")){
         responseBody(s"Hello world!}")
     }
 }
```

you can customize the returned cookie using:
<table class="table">
<tr>
<th>Property name</th>
<th>Type</th>
<th>Description</th>
<th>Default Value</th>
</tr>
<tr>
<td>name</td>
<td>String</td>
<td>Indicate the cookie key</td>
<td>-</td>
</tr>
<tr>
<td>value</td>
<td>String</td>
<td>Indicate the cookie value</td>
<td>-</td>
</tr>
<tr>
<td>expires</td>
<td>Option[LocalDateTime]</td>
<td>Indicate the cookie expiration time</td>
<td>None</td>
</tr>
<tr>
<td>maxAge</td>
<td>Option[Long]</td>
<td>Indicate the cookie max age ni seconds</td>
<td>None</td>
</tr>
<tr>
<td>domain</td>
<td>Option[String]</td>
<td>Indicate the cookie domain where it is going to be applied</td>
<td>None</td>
</tr>
<tr>
<td>path</td>
<td>Option[String]</td>
<td>Indicate the cookie path where it is going to be applied</td>
<td>None</td>
</tr>
<tr>
<td>secure</td>
<td>Boolean</td>
<td>Indicate the cookie is secure meaning only send by https</td>
<td>false</td>
</tr>
<tr>
<td>httpOnly</td>
<td>Boolean</td>
<td>Indicate the cookie is only sent by http calls and not ajax</td>
<td>false</td>
</tr>
</table>

##### responseVersion

Indicate the protocol version of the response
 
 ``` scala
import acsgh.mad.scala.core.http.model.ProtocolVersion._

 get("/") { implicit ctx =>
     responseVersion(HTTP_1_1){
         responseBody(s"Hello world!}")
     }
 }
```

##### responseBody

Set up the response body bytes. The directive has also a way to write content into something more manageable
``` scala
get("/") { implicit ctx =>
    responseBody(byte)
}
```

If you want to convert the body from an object you can use:
``` scala
get("/") { implicit ctx =>
    responseBody(T)
}
```

But keep in mind that for that to work, it need an implicit HttpBodyWriter[T] in the scope. We provide the String handler 
but the rest of readers is on your side. The reader also has a parameter called contentType, that is going to be added into 
the response except Content-Type header is setup already

##### noBody

Utility method to not return any response body
``` scala
get("/") { implicit ctx =>
    noBody()
}
```

#### RequestListener

We provide a listener that inform any actions around the request

``` scala
import acsgh.mad.scala.server.router.http.listener.RequestListener

object MyRequestListener extends RequestListener {
  def onStart()(implicit ctx: HttpRequestContext): Unit = {
    log.debug(s"Request:  ${ctx.request.method} ${ctx.request.uri}")
  }

  def onStop()(implicit ctx: HttpRequestContext): Unit = {
    val duration = System.currentTimeMillis() - ctx.request.starTime

    log.info(s"Response: ${ctx.request.method} ${ctx.request.uri} with ${ctx.response.status.code} in ${TimerSplitter.getIntervalInfo(duration, TimeUnit.MILLISECONDS)}")
  }

  override def onTimeout()(implicit ctx: HttpRequestContext): Unit = {
    log.error(s"Timeout during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'")
  }

  def onException(exception: Exception)(implicit ctx: HttpRequestContext): Unit = {
    log.error(s"Error during request: ${ctx.request.method}: ${ctx.request.uri} - Body: '${new String(ctx.request.bodyBytes, "UTF-8")}'", exception)
  }
}

builder.http.addRequestListeners(listener)
```

There is a default log implementation called: acsgh.mad.scala.server.router.http.listener.LoggingEventListener

#### ErrorCodeHandler

The error handler render the response when some error happens, so you can customize the response. You can configure by one specific response code or for all of them. This is called when you call the directive error. 
If can also called by the library in other situations, in case of exceptions another handler will be called.

``` scala
import acsgh.mad.scala.core.http.model.ResponseStatus._
import acsgh.mad.scala.server.router.http.handler.ErrorCodeHandler

object MyErrorCodeHandler extends ErrorCodeHandler {
  def handle(responseStatus: ResponseStatus, message: Option[String])(implicit requestContext: HttpRequestContext): HttpResponse = {
    responseBody("Page not found :(")
  }
}

builder.http.errorCodeHandlers(NOT_FOUND, MyErrorCodeHandler)
builder.http.defaultErrorCodeHandler(MyErrorCodeHandler)
```

The default error code handler will just show the error code and the message. You do not need to set up one.

#### ExceptionHandler

The exception handler render the response when some an unexpected exception happens, so you can customize the response.

``` scala
import acsgh.mad.scala.core.http.model.ResponseStatus._
import acsgh.mad.scala.server.router.http.handler.ExceptionHandler

object MyExceptionHandler extends ExceptionHandler {
  def handle(throwable: Throwable)(implicit requestContext: HttpRequestContext): HttpResponse = {
    responseBody(s"Something bad happens: ${throwable.getMessage()}")
  }
}

builder.http.exceptionHandler(MyExceptionHandler)
```

The default error code handler will just show the exception message and the stacktrace when the server is not in production mode. You do not need to set up one.

### WS

## Converters

### JSON

#### Spray
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.converter.json/spray_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.converter.json/spray_2.13/latest/acsgh/mad/scala/server/converter/json/spray/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://github.com/spray/spray-json" target="_blank">Spray Json Documentation</a>
</li>
</ul>

#### Jackson

<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.converter.json/jackson_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.converter.json/jackson_2.13/latest/acsgh/mad/scala/server/converter/json/jackson/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://github.com/FasterXML/jackson" target="_blank">Jackson Documentation</a>
</li>
</ul>

### Templates
#### Freemarker
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.converter.template/freemarker_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.converter.template/freemarker_2.13/latest/acsgh/mad/scala/server/converter/template/freemarker/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://freemarker.apache.org/" target="_blank">Freemarker Documentation</a>
</li>
</ul>

#### Thymeleaf
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.converter.template/thymeleaf_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.converter.template/thymeleaf_2.13/latest/acsgh/mad/scala/server/converter/template/thymeleaf/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://www.thymeleaf.org/index.html" target="_blank">Thymeleaf Documentation</a>
</li>
</ul>

#### Twirl
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.converter.template/twirl_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.converter.template/twirl_2.13/latest/acsgh/mad/scala/server/converter/template/twirl/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://github.com/playframework/twirl" target="_blank">Twirl Documentation</a>
</li>
</ul>

## Provider
### Netty
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.provider/netty_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.provider/netty_2.13/latest/acsgh/mad/scala/server/provider/netty/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://netty.io/" target="_blank">Netty Documentation</a>
</li>
</ul>

### Servlet
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.provider/servlet_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.provider/servlet_2.13/latest/acsgh/mad/scala/server/provider/servlet/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://www.oracle.com/technetwork/java/index-jsp-135475.html" target="_blank">Servlet Documentation</a>
</li>
</ul>

### Jetty
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.provider/jetty_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.provider/jetty_2.13/latest/acsgh/mad/scala/server/provider/jetty/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://www.eclipse.org/jetty/" target="_blank">Jetty Documentation</a>
</li>
</ul>

## Support
### Swagger
<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server.support/swagger_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server.support/swagger_2.13/latest/acsgh/mad/scala/server/support/swagger/index.html" target="_blank">Javadoc</a>
</li>
<li>
<a href="https://swagger.io/" target="_blank">Swagger Documentation</a>
</li>
</ul>

</div>
</div>
