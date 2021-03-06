package com.wavesplatform.dex.db

import com.wavesplatform.account.Address
import com.wavesplatform.dex.model.{OrderInfo, OrderStatus}
import com.wavesplatform.transaction.assets.exchange.{AssetPair, Order}

object EmptyOrderDB extends OrderDB {
  override def containsInfo(id: Order.Id): Boolean                                                  = false
  override def status(id: Order.Id): OrderStatus.Final                                              = OrderStatus.NotFound
  override def get(id: Order.Id): Option[Order]                                                     = None
  override def saveOrderInfo(id: Order.Id, sender: Address, oi: OrderInfo[OrderStatus.Final]): Unit = {}
  override def saveOrder(o: Order): Unit                                                            = {}
  override def loadRemainingOrders(owner: Address,
                                   maybePair: Option[AssetPair],
                                   activeOrders: Seq[(Order.Id, OrderInfo[OrderStatus])]): Seq[(Order.Id, OrderInfo[OrderStatus])] =
    Seq.empty
}
