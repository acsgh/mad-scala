---
title: 'Documentation'
container_class: 'container-fluid'
---
{::options parse_block_html="true" /}
<div class="row">
<div class="col-3 left-menu">
{:.no_toc}
* Will be replaced with the ToC, excluding the "Contents" header
{:toc}
</div>
<div class="col-9">

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
import acsgh.mad.scala.server.router.http.body.writer.default._

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
import acsgh.mad.scala.server.router.http.body.reader.default._

requestBody { bytes =>
}
```

If you want to convert the body you can use:
``` scala
import acsgh.mad.scala.server.router.http.body.reader.default._

requestBody[T] { bytes =>
}
```

Keep in mind that for that to work, it needs an implicit HttpBodyReader[T] in the scope. We provide a few handlers by importing: acsgh.mad.scala.server.router.http.body.reader.default._
The reader also has a parameter called strictContentTypes, so if it true, and the content types received are not the one expected an 405 will be returned

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

##### pathParam
Extract from 1 to 15 path params from the url. The key is case insensible.

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/{id1}") { implicit ctx =>
    pathParam("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id2}/{id2}") { implicit ctx =>
    pathParam("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param is required by default, it could be also optional, or have a default value

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/{id1}") { implicit ctx =>
    pathParam("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    pathParam("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default, all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can be mixed with other methods.

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/{id1}") { implicit ctx =>
    pathParam("id1".as[Long]){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    pathParam("id1".as[Long].opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    pathParam("id1".as[Long].default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned

##### formParam
Extract from 1 to 15 form params from the request. The key is case insensible.

The form param can come from the URL query part and in case of POST request in the body as URL form encoded or multipart. We are getting the param automatically for you. As the param can be repeated we are aggregating all sources in just one.

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/") { implicit ctx => // /?id1=1
    formParam("id1"){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx => /?id1=1&id2=2
    formParam("id2", "id2"){ (id1, id2) =>
        responseBody(s"Param id1 is ${id1}, Param id2 is ${id2}")
    }
}
```

If the param is not present, a 400 Bad Request will be returned

The path param is required by default, it could be optional, have a default value or be a list

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/") { implicit ctx =>
    formParam("id1".opt){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    formParam("id1".default("1234")){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    formParam("id1".list){ id1 =>
        responseBody(s"Param id1 is ${id1}")
    }
}
```

By default, all params are String, but we can convert them for you. Convert to T needs an implicit HttpParamReader[T] in its context. The conversion can by mix with other methods
                                                                                                                                   
``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/") { implicit ctx =>
    formParam("id1".as[Long]){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    formParam("id1".as[Long].opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/") { implicit ctx =>
    formParam("id1".as[Long].default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/") { implicit ctx =>
    formParam("id1".as[Long].list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

In case you need to read a multipart file param you can convert the param like this:

                                                                                                                            
``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/") { implicit ctx =>
    formParam("userPhoto".multipartFile){ userPhoto => // MultipartFile
        //userPhoto.content // Array[Byte]
        //userPhoto.filename // Option[String]
        //userPhoto.contentType // String
        responseBody(s"userPhoto name is ${userPhoto.filename}")
    }

    formParam("userPhoto".multipartFile.opt){ userPhoto => // Option[MultipartFile]
        responseBody(s"userPhoto length is ${userPhoto.content.length}")
    }

    formParam("userPhoto".multipartFile.list){ userPhoto => // List[MultipartFile]
        responseBody(s"userPhoto length is ${userPhoto.content.length}")
    }
}
```


##### requestHeader
Extract from 1 to 15 a header params from the request. Keep in mind that a header can be repeated. The key is case insensible.

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

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

The header param is required by default, it could be optional, have a default value or be a list

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

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
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/{id1}") { implicit ctx =>
    requestHeader("id1".as[Long]){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".as[Long].opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestHeader("id1".as[Long].default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/{id1}") { implicit ctx =>
    requestHeader("id1".as[Long].list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned


##### requestCookie
Extract from 1 to 15 cookie value from the request. Keep in mind that a cookie can be repeated. The key is non insensible.

``` scala
import acsgh.mad.scala.server.router.http.params.reader.default._

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
import acsgh.mad.scala.server.router.http.params.reader.default._

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
import acsgh.mad.scala.server.router.http.params.reader.default._

get("/{id1}") { implicit ctx =>
    requestCookie("id1".as[Long]){ id1 => // Long
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".as[Long].opt){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}

get("/{id1}") { implicit ctx =>
    requestCookie("id1".as[Long].default(1234)){ id1 => // Optional[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}


get("/{id1}") { implicit ctx =>
    requestCookie("id1".as[Long].list){ id1 => // List[Long]
        responseBody(s"Param id1 is ${id1}")
    }
}
```

If the param cannot be converted, a 400 Bad Request will be returned
  
##### responseHeader

Add a header into the response, by default it is a String if you want to add any object different from it you need to add an implicit HttpParamWriter[T] in your scope.
 
 ``` scala
import acsgh.mad.scala.server.router.http.params.writer.default._

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
import acsgh.mad.scala.server.router.http.params.writer.default._

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
import acsgh.mad.scala.server.router.http.body.writer.default._

get("/") { implicit ctx =>
    responseBody(byte)
}
```

If you want to convert the body from an object you can use:
``` scala
import acsgh.mad.scala.server.router.http.body.writer.default._

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
import acsgh.mad.scala.server.router.http.body.writer.default._

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
import acsgh.mad.scala.server.router.http.body.writer.default._

object MyExceptionHandler extends ExceptionHandler {
  def handle(throwable: Throwable)(implicit requestContext: HttpRequestContext): HttpResponse = {
    responseBody(s"Something bad happens: ${throwable.getMessage()}")
  }
}

builder.http.exceptionHandler(MyExceptionHandler)
```

The default error code handler will just show the exception message and the stacktrace when the server is not in production mode. You do not need to set up one.

### WS

#### Route

A Websocket route is defined as a function that receive an WSRequestContext and return an Optional[WSResponse].
The WSRequestContext has The original request received

``` scala
ws("/echo") { implicit context =>
    wsRequest[String] { input =>
        wsResponse(s"You said: $input")
    }
}
```

You can define the dafault route in tu builder by builder.ws.defaultHandler(WSRequestContext => Optional[WSResponse])

#### WSDirectives

##### wsRequest

Read the websocket request into the desired format. If you want to convert the body to anything but String, you need to have an implicit WSBodyReader[T] in the context
``` scala
wsRequest[T] { bytes =>
}
```

##### wsResponse

Write the websocket response into the desired format. If you want to convert the body to anything but String, you need to have an implicit WSBodyWriter[T] in the context. 
You can indicate if you want to close the connection after the response is send
``` scala
wsResponse("Hello world!")

wsResponse("Hello world!", close = true)
```

##### close

Close the connection.

``` scala
close()
```


#### RequestListener

We provide a listener that inform any actions around the request

``` scala
import acsgh.mad.scala.server.router.ws.listener.WSRequestListener

object MyRequestListener extends WSRequestListener {
   def onStart()(implicit ctx: WSRequestContext): Unit = {
     log.debug(s"WS Request:  ${ctx.request.uri}")
   }
 
   def onStop(response: Option[WSResponse])(implicit ctx: WSRequestContext): Unit = {
     val duration = System.currentTimeMillis() - ctx.request.starTime
 
     log.info(s"WS Response: ${ctx.request.uri} in ${TimerSplitter.getIntervalInfo(duration, TimeUnit.MILLISECONDS)}")
   }
 
   override def onTimeout()(implicit ctx: WSRequestContext): Unit = {
     log.error(s"WS Timeout during request: ${ctx.request.uri} - Body: '${body(ctx.request)}'")
   }
 
   def onException(exception: Exception)(implicit ctx: WSRequestContext): Unit = {
     log.error(s"WS Error during request: ${ctx.request.uri} - Body: '${body(ctx.request)}'", exception)
   }
}

builder.ws.addRequestListeners(listener)
```

There is a default log implementation called: acsgh.mad.scala.server.router.ws.listener.WSLoggingEventListener


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

Add Json support using spray json as a marshaller. You controller needs to implement SprayDirectives.

After that you have two directives to use:

##### requestJson 
Reads the request body and parse into an object of type T. It requires an implicit JsonReader[T] in the context. If the format is incorrect a 400 Bad Request will be returned.

``` scala
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.spray.SprayDirective
import spray.json._

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with SprayDirective with DefaultJsonProtocol {

    implicit val personFormat: RootJsonFormat[Person] = jsonFormat1(Person)

    get("/") { implicit ctx =>
        requestJson(classOf[Person]) { person =>
        }
    }
}
```

##### responseJson 
Write the object of type T into a Json response body. It requires an implicit JsonWriter[T] in the context. If the server is in production mode the json will be compact.

``` scala
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.spray.SprayDirective
import spray.json._

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with SprayDirective with DefaultJsonProtocol {

    implicit val personFormat: RootJsonFormat[Person] = jsonFormat1(Person)

    get("/") { implicit ctx =>
        val person = Person("Jonh Doe")
        responseJson(person)
    }
}
```

##### Json Error Code handler and Exception handler
It provides you a json response if the accept Content-Type is json. So your response is coherent. They need to be added to the builder

``` scala
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.spray.SprayDirective
import spray.json._
import acsgh.mad.scala.server.converter.json.spray.JsonErrorCodeHandler
import acsgh.mad.scala.server.converter.json.spray.JsonExceptionHandler

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with SprayDirective with DefaultJsonProtocol {

    builder.http.defaultErrorCodeHandler(new JsonErrorCodeHandler())
    builder.http.exceptionHandler(new JsonExceptionHandler())

    implicit val personFormat: RootJsonFormat[Person] = jsonFormat1(Person)

    get("/") { implicit ctx =>
        val person = Person("Jonh Doe")
        responseJson(person)
    }
}
```

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

Add Json support using jackson as a marshaller. You controller needs to implement JacksonDirectives.

After that you have two directives to use:

##### requestJson 
Reads the request body and parse into an object of type T. It requires an implicit ObjectMapper in the context. If the format is incorrect a 400 Bad Request will be returned.

``` scala
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.jackson.JacksonDirectives
import acsgh.mad.scala.server.converter.json.jackson.JacksonObjectMapperProvider
import com.fasterxml.jackson.databind.ObjectMapper

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with JacksonDirectives with DefaultJsonProtocol {

    implicit val objectMapper: ObjectMapper = JacksonObjectMapperProvider.build(builder.productionMode)

    get("/") { implicit ctx =>
        requestJson(classOf[Person]) { person =>
        }
    }
}
```

##### responseJson 
Write the object of type T into a Json response body. It requires an implicit ObjectMapper in the context. If the server is in production mode the json will be compact.

``` scala
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.jackson.JacksonDirectives
import acsgh.mad.scala.server.converter.json.jackson.JacksonObjectMapperProvider
import com.fasterxml.jackson.databind.ObjectMapper

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with JacksonDirectives with DefaultJsonProtocol {

    implicit val objectMapper: ObjectMapper = JacksonObjectMapperProvider.build(builder.productionMode)

    get("/") { implicit ctx =>
        val person = Person("Jonh Doe")
        responseJson(person)
    }
}
```

##### Json Error Code handler and Exception handler
It provides you a json response if the accept Content-Type is json. So your response is coherent. They need to be added to the builder

``` scala
import acsgh.mad.scala.server.converter.json.jackson.JsonErrorCodeHandler
import acsgh.mad.scala.server.converter.json.jackson.JsonExceptionHandler
import acsgh.mad.scala.server.Controller
import acsgh.mad.scala.server.ServerBuilder
import acsgh.mad.scala.server.converter.json.jackson.JacksonDirectives
import acsgh.mad.scala.server.converter.json.jackson.JacksonObjectMapperProvider
import com.fasterxml.jackson.databind.ObjectMapper

case class Person(name:String)

class MyController(builder:ServerBuilder) extends Controller with JacksonDirectives with DefaultJsonProtocol {

    implicit val objectMapper: ObjectMapper = JacksonObjectMapperProvider.build(builder.productionMode)

    builder.http.defaultErrorCodeHandler(new JsonErrorCodeHandler())
    builder.http.exceptionHandler(new JsonExceptionHandler())

    get("/") { implicit ctx =>
        val person = Person("Jonh Doe")
        responseJson(person)
    }
}
```

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
