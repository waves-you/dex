package com.wavesplatform

import com.wavesplatform.dex.time.NTP
import org.scalatest.{BeforeAndAfterAll, Suite}

trait NTPTime extends BeforeAndAfterAll { _: Suite =>
  protected val ntpTime = new NTP("pool.ntp.org")

  protected def ntpNow: Long = ntpTime.getTimestamp()

  override protected def afterAll(): Unit = {
    super.afterAll()
    ntpTime.close()
  }
}
