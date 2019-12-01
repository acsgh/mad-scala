---
current_version: '0.10.1'
title: 'Documentation'
---
{::options parse_block_html="true" /}
Table of content:
{:.no_toc}
* Will be replaced with the ToC, excluding the "Contents" header
{:toc}
<br/>
<hr/>
## 1. Modules
### 1.1. Router
Contains the basic routing logic it has two submodules, one to handle only http and other that give WS support

#### 1.1.1 HTTP
##### 1.1.1.1 Dependencies
<ul class="nav nav-tabs" id="myTab" role="tablist">
  <li class="nav-item">
    <a class="nav-link active" id="maven-tab" data-toggle="tab" href="#http-maven" role="tab" aria-controls="home" aria-selected="true">Maven</a>
  </li>
  <li class="nav-item">
    <a class="nav-link" id="profile-tab" data-toggle="tab" href="#http-gradle" role="tab" aria-controls="profile" aria-selected="false">Gradle</a>
  </li>
</ul>
<div class="tab-content" id="http-tabs">
  <div class="tab-pane fade show active" id="http-maven" role="tabpanel" aria-labelledby="maven-tab">
  ```
  <dependency>
    <groupId>com.github.acsgh.mad.scala.server.router</groupId>
    <artifactId>http_2.12</artifactId>
    <version>{{ page.current_version}}</version>
    <type>pom</type>
  </dependency>
  ``` 
  </div>
  <div class="tab-pane fade" id="http-gradle" role="tabpanel" aria-labelledby="profile-tab">
  ```
  implementation 'com.github.acsgh.mad.scala.server.router:http_2.12:{{ page.current_version}}'
  ``` 
  </div>
</div>

##### 1.1.1.2 Usage
The main class in this module is the class **HttpRouter**. Any server contains one and it is an abstraction of all the routes an application holds.
