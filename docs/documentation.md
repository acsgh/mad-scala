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

```
libraryDependencies += "com.github.acsgh.mad.scala" %% "core" % "{{ site.current_version }}"
``` 

### Server
Contains the basic routing logic it has two submodules, one to handle only http and other that give WS support

#### Core
##### HTTP
##### WS

#### Converters

##### JSON
###### Jackson
###### Spray
##### Templates
###### Freemarker
###### Thymeleaf
###### Twirl

#### Provider
##### Netty
##### Jetty

#### Support
##### Swagger

</div>
</div>
<br/>
