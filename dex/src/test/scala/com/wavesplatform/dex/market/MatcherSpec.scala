package com.wavesplatform.dex.market

import akka.actor.ActorSystem
import akka.testkit.TestKitBase
import com.typesafe.config.ConfigFactory
import com.wavesplatform.settings.loadConfig
import com.wavesplatform.utils.ScorexLogging
import org.scalatest._

abstract class MatcherSpec(_actorSystemName: String) extends WordSpecLike with MatcherSpecLike {
  protected def actorSystemName: String = _actorSystemName
}

trait MatcherSpecLike extends TestKitBase with Matchers with BeforeAndAfterAll with BeforeAndAfterEach with ScorexLogging {
  this: Suite =>

  protected def actorSystemName: String

  implicit override lazy val system: ActorSystem = ActorSystem(
    actorSystemName,
    loadConfig(ConfigFactory.empty())
  )

  override protected def afterAll(): Unit = {
    super.afterAll()
    shutdown(system)
  }
}
