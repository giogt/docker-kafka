package org.giogt.kafka.mirrormaker

import kafka.utils.Whitelist
import org.scalatest.{ Matchers, WordSpec }

class WhitelistSpec extends WordSpec with Matchers {

  "whitelist" when {
    "'.*'" should {
      "match any topic" in {
        val whitelist = Whitelist(".*")

        isTopicAllowed(whitelist, "test.topic.123") should be(true)
        isTopicAllowed(whitelist, "test.topic.456") should be(true)
        isTopicAllowed(whitelist, "foo.bar.topic.123") should be(true)
      }
    }

    "valued with one specific topic" should {
      "match that topic only" in {
        val whitelist = Whitelist("foo")

        isTopicAllowed(whitelist, "foo") should be(true)
        isTopicAllowed(whitelist, "afoo") should be(false)
        isTopicAllowed(whitelist, "a.foo") should be(false)
        isTopicAllowed(whitelist, "fooa") should be(false)
        isTopicAllowed(whitelist, "foo.a") should be(false)
        isTopicAllowed(whitelist, "afooa") should be(false)
        isTopicAllowed(whitelist, "a.foo.a") should be(false)
      }
    }

    "valued with a list of specific topics" should {
      "match only those topics" in {
        val whitelist = Whitelist("foo, bar")

        isTopicAllowed(whitelist, "foo") should be(true)
        isTopicAllowed(whitelist, "afoo") should be(false)
        isTopicAllowed(whitelist, "a.foo") should be(false)
        isTopicAllowed(whitelist, "fooa") should be(false)
        isTopicAllowed(whitelist, "foo.a") should be(false)
        isTopicAllowed(whitelist, "afooa") should be(false)
        isTopicAllowed(whitelist, "a.foo.a") should be(false)

        isTopicAllowed(whitelist, "bar") should be(true)
        isTopicAllowed(whitelist, "abar") should be(false)
        isTopicAllowed(whitelist, "a.bar") should be(false)
        isTopicAllowed(whitelist, "bara") should be(false)
        isTopicAllowed(whitelist, "bar.a") should be(false)
        isTopicAllowed(whitelist, "abara") should be(false)
        isTopicAllowed(whitelist, "a.bar.a") should be(false)
      }
    }

    "valued with a string including a '.'" should {
      "match any character for the '.'" in {
        val whitelist = Whitelist("foo.bar")

        isTopicAllowed(whitelist, "foo.bar") should be(true)
        isTopicAllowed(whitelist, "fooabar") should be(true)
        isTopicAllowed(whitelist, "foobbar") should be(true)
        isTopicAllowed(whitelist, "foo1bar") should be(true)
        isTopicAllowed(whitelist, "foo2bar") should be(true)
      }
    }

    "valued with a string including a '\\.'" should {
      "match the literal '.' character for the '\\.'" in {
        val whitelist = Whitelist("foo\\.bar")

        isTopicAllowed(whitelist, "foo.bar") should be(true)
        isTopicAllowed(whitelist, "fooabar") should be(false)
        isTopicAllowed(whitelist, "foobbar") should be(false)
        isTopicAllowed(whitelist, "foo1bar") should be(false)
        isTopicAllowed(whitelist, "foo2bar") should be(false)
      }
    }

  }

  def isTopicAllowed(whitelist: Whitelist, topic: String): Boolean =
    whitelist.isTopicAllowed(topic, excludeInternalTopics = false)

}
