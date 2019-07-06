package com.acs.scala.server.mad.router

import java.io.UnsupportedEncodingException
import java.net.{URI, URISyntaxException, URLDecoder}
import java.util.regex.{Matcher, Pattern}

object Address extends LogSupport {

  def build(rawUri: String): Address = try {
    val uri = new URI(rawUri)
    new Address(uri, new Params, extractQueryParam(uri.getQuery))
  } catch {
    case e: URISyntaxException =>
      throw new RuntimeException(e)
  }

  private def extractQueryParam(query: String): Params = {
    if (query == null) {
      Params()
    } else {
      val params = query.split("&").flatMap(toMapEntry).groupBy(_._1).mapValues(_.map(_._2).toList)
      Params(params)
    }
  }

  private def toMapEntry(rawEntry: String): Option[(String, String)] = {
    try {
      val parts = rawEntry.split("=")

      val key = parts(0)
      val value = if (parts.length > 1) parts(1) else ""
      Some((URLDecoder.decode(key, "UTF-8"), URLDecoder.decode(value, "UTF-8")));
    } catch {
      case e: Exception =>
        log.error("Unable to parse query params", e)
        None
    }
  }

}

case class Address
(
  uri: URI,
  pathParams: Params,
  queryParams: Params
) {

  private[router] def ofRoute(routeUri: String): Address = new Address(uri, extractPathParams(routeUri), queryParams)

  private[router] def matchUrl(routeUri: String): Boolean = {
    val pattern = getPattern(routeUri)
    Pattern.matches(pattern, uri.getPath)
  }

  private def extractPathParams(routeUri: String): Params = {
    var params = Map[String, List[String]]()

    if (matchUrl(routeUri)) {
      val names = getParamNames(routeUri)
      val patternString: String = getPattern(routeUri)
      val pattern: Pattern = Pattern.compile(patternString)
      val matcher: Matcher = pattern.matcher(uri.getPath)

      if (matcher.find) {
        (1 to matcher.groupCount()).foreach { i =>
          val name: String = names(i - 1)
          val previousValues: List[String] = params.getOrElse(name, List())
          val currentValue: String = urlDecode(matcher.group(i))
          val nextValues: List[String] = currentValue :: previousValues
          params = params + (name -> nextValues)
        }
      }
    }
    Params(params)
  }

  private def urlDecode(value: String): String = {
    try {
      URLDecoder.decode(value, "UTF-8")
    } catch {
      case e: UnsupportedEncodingException =>
        throw new RuntimeException(e)
    }
  }

  private def getPattern(routeUri: String): String = {
    var pattern = routeUri.replaceAll("\\*", ".*")
    pattern = pattern.replaceAll("\\{[^/{}+]*\\}", "([^/{}]*)")
    pattern = pattern.replaceAll("\\{[^/{}]*\\+\\}", "([^{}]*)")
    pattern
  }

  private def getParamNames(routeUri: String): List[String] = {
    var names = List[String]()
    val pattern = Pattern.compile("(\\{[^/{}]*\\})")
    val matcher = pattern.matcher(routeUri)

    while (matcher.find) {
      names = matcher.group.replace("{", "").replace("+}", "").replace("}", "") :: names
    }
    names
  }


}
