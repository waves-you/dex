package com.wavesplatform

import com.wavesplatform.account.{KeyPair, PublicKey}
import com.wavesplatform.dex.it.waves.WavesFeeConstants._
import com.wavesplatform.it.api.MatcherCommand
import com.wavesplatform.transaction.assets.exchange.{AssetPair, Order, OrderType}
import org.scalacheck.Gen

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.concurrent.{Await, Future}
import scala.util.Random
import scala.util.control.NonFatal

package object it {

  /**
    * @return The number of successful commands
    */
  def executeCommands(xs: Seq[MatcherCommand], ignoreErrors: Boolean = true, timeout: FiniteDuration = 3.minutes): Int = {
    Await.result(Future.sequence(xs.map(executeCommand(_))), timeout).sum
  }

  private def executeCommand(x: MatcherCommand, ignoreErrors: Boolean = true): Future[Int] = x match {
    case MatcherCommand.Place(api, order) => api.tryPlace(order).map(_.fold(_ => 0, _ => 1))
    case MatcherCommand.Cancel(api, owner, order) =>
      try api.tryCancel(owner, order).map(_.fold(_ => 0, _ => 1))
      catch {
        case NonFatal(e) =>
          if (ignoreErrors) Future.successful(0)
          else Future.failed(e)
      }
  }

  def orderGen(matcher: PublicKey,
               trader: KeyPair,
               assetPairs: Seq[AssetPair],
               types: Seq[OrderType] = Seq(OrderType.BUY, OrderType.SELL)): Gen[Order] =
    for {
      assetPair      <- Gen.oneOf(assetPairs)
      tpe            <- Gen.oneOf(types)
      amount         <- Gen.choose(10, 100)
      price          <- Gen.choose(10, 100)
      orderVersion   <- Gen.oneOf(1: Byte, 2: Byte)
      expirationDiff <- Gen.choose(600000, 6000000)
    } yield {
      val ts = System.currentTimeMillis()
      if (tpe == OrderType.BUY)
        Order.buy(
          trader,
          matcher,
          assetPair,
          amount,
          price * Order.PriceConstant,
          System.currentTimeMillis(),
          ts + expirationDiff,
          matcherFee,
          orderVersion
        )
      else
        Order.sell(
          trader,
          matcher,
          assetPair,
          amount,
          price * Order.PriceConstant,
          System.currentTimeMillis(),
          ts + expirationDiff,
          matcherFee,
          orderVersion
        )
    }

  def choose[T](xs: IndexedSeq[T]): T = xs(Random.nextInt(xs.size))
}
