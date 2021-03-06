package com.wavesplatform.dex

import com.typesafe.config.Config
import com.wavesplatform.dex.settings.utils.ConfigOps.ConfigOps

package object settings {

  implicit def toConfigOps(config: Config): ConfigOps = new ConfigOps(config)

  private val format = new java.text.DecimalFormat("#.################")

  /** Formats amount or price */
  def formatValue(value: BigDecimal): String = format.format(value.bigDecimal)
}
