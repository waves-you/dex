package com.wavesplatform.dex.grpc.integration

import com.wavesplatform.dex.it.api.BaseContainersKit
import com.wavesplatform.dex.it.api.node.{HasWavesNode, NodeApiExtensions}
import com.wavesplatform.dex.it.assets.DoubleOps
import com.wavesplatform.dex.it.config.{GenesisConfig, PredefinedAccounts, PredefinedAssets}
import com.wavesplatform.dex.it.test.InformativeTestStart
import com.wavesplatform.dex.it.waves.{MkWavesEntities, WavesFeeConstants}
import com.wavesplatform.dex.test.matchers.DiffMatcherWithImplicits
import com.wavesplatform.utils.ScorexLogging
import org.scalatest._
import org.scalatest.concurrent.Eventually

import scala.concurrent.duration.DurationInt

trait IntegrationSuiteBase
    extends FreeSpec
    with Matchers
    with BeforeAndAfterAll
    with BeforeAndAfterEach
    with Eventually
    with BaseContainersKit
    with HasWavesNode
    with MkWavesEntities
    with WavesFeeConstants
    with NodeApiExtensions
    with PredefinedAssets
    with PredefinedAccounts
    with DoubleOps
    with DiffMatcherWithImplicits
    with InformativeTestStart
    with ScorexLogging {

  GenesisConfig.setupAddressScheme()

  override protected val moduleName: String = "waves-integration-it"

  override implicit def patienceConfig: PatienceConfig = super.patienceConfig.copy(timeout = 30.seconds, interval = 1.second)

  override protected def beforeAll(): Unit = {
    log.debug(s"Perform beforeAll")
    wavesNode1.start()
  }

  override protected def afterAll(): Unit = {
    log.debug(s"Perform afterAll")
    stopBaseContainers()
    super.afterAll()
  }
}
