package com.nikhu.ecommerce.shoppingcart

/**
  * Created by vsubbarravuri on 9/25/16.
  */

// Types used in API routes


case class ShoppingCart(id: String, version: Long, user: User, items: List[ShoppingCartItem] = List.empty[ShoppingCartItem]) {

  def addItem(shoppingCartItem: ShoppingCartItem) = copy(items = items :+ shoppingCartItem)
}


object CartStatus extends Enumeration {
  type CartStatus = Value
  val Created, ItemAdded, Checkout, Abandoned, Expired, Deleted = Value
}

case class ShoppingCartItem(product: Product, quantity: Int) {

  def increaseQuantity(addedQuantity: Int) = copy(quantity = this.quantity + addedQuantity)

  def productId = product.id
}

case class Product(id: String, version: Long, name: String, productType: String, price: Double)

case class User(id: String, version: Long, name: String)