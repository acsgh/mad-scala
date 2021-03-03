package acsgh.mad.scala.core

import com.acsgh.common.scala.log.LogSupport

import java.io.UnsupportedEncodingException
import java.net.{URI, URLDecoder}
import java.util.regex.{Matcher, Pattern}

trait URLSupport extends LogSupport {

  protected def extractPathParams(routeUri: String, requestPath: String): Map[String, String] = {
    var params = Map[String, String]()

    if (matchUrl(routeUri, requestPath)) {
      val names = getParamNames(routeUri)
      val patternString: String = getPattern(routeUri)
      val pattern: Pattern = Pattern.compile(patternString)
      val matcher: Matcher = pattern.matcher(requestPath)

      if (matcher.find) {
        (1 to matcher.groupCount()).foreach { i =>
          params = params + (names(i - 1) -> urlDecode(matcher.group(i)))
        }
      }
    }
    params
  }

  protected def extractCookie(input: String): Option[(String, String)] = {
    val parts = input.trim.split("=")

    if (parts.length == 2) {
      Some((parts(0), parts(1)))
    } else if (parts.length == 1) {
      Some((parts(0), ""))
    } else {
      log.warn("Illegal cookie input: {}", input)
      None
    }
  }

  protected def extractQueryParam(requestUri: URI): Map[String, List[String]] = {
    val query = requestUri.getQuery
    if (query == null) {
      Map()
    } else {
      query.split("&").flatMap(toMapEntry).groupBy(_._1).view.mapValues(_.map(_._2).toList).toMap
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

  protected def matchUrl(routeUri: String, path: String): Boolean = {
    val pattern = getPattern(routeUri)
    Pattern.matches(pattern, path)
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
    names.reverse
  }
}
