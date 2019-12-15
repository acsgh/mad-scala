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

## Modules

The library is slitted in several modules in order to you choose the one that more fits into your needs and reduce the dependencies

### Common
#### Core
This module contains the basic model for HTTP and WS. This model is later user by the server and client

<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala/core_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala/core_2.13/latest/acsgh/mad/scala/core/index.html" target="_blank">Javadoc</a>
</li>
</ul>

### Server
Contains the basic routing logic it has two submodules, one to handle only http and other that give WS support

#### Core


<ul>
<li>
<a href="https://mvnrepository.com/artifact/com.github.acsgh.mad.scala.server/core_2.13/{{ site.current_version }}" target="_blank">Maven Central</a>
</li>
<li>
<a href="https://javadoc.io/doc/com.github.acsgh.mad.scala.server/core_2.13/latest/acsgh/mad/scala/server/index.html" target="_blank">Javadoc</a>
</li>
</ul>


##### HTTP
##### WS

#### Converters

##### JSON

###### Spray
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

###### Jackson

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

##### Templates
###### Freemarker
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

###### Thymeleaf
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

###### Twirl
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

#### Provider
##### Netty
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

##### Servlet
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

##### Jetty
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

#### Support
##### Swagger
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
