package com.example.domain;

import java.util.List;

/**
 * 注文商品テーブルを表すドメインです.
 * 
 * @author fuka
 *
 */
public class OrderItem {
	/** ID */
	private Integer id;
	/** アイテムID */
	private Integer itemId;
	/** 注文ID */
	private Integer orderId;
	/** 数量 */
	private Integer quantity;
	/** サイズ */
	private Character size;
	/** 商品 */
	private Item item;
	/** トッピングリスト */
	private List<OrderTopping> orderToppingList;

	/**
	 * 注文アイテムの小計を計算します.
	 * 
	 * @return 注文アイテムの小計
	 */
	public int getSubTotal() {
		int subTotal = 0;
		int orderToppingTotal = 0;
		if (this.size.equals('M')) {
			for (OrderTopping orderTopping : this.orderToppingList) {
				orderToppingTotal += orderTopping.getTopping().getPriceM();
			}
			subTotal = orderToppingTotal + this.item.getPriceM();
		} else if (this.size.equals('L')) {
			for (OrderTopping orderTopping : this.orderToppingList) {
				orderToppingTotal += orderTopping.getTopping().getPriceL();
			}
			subTotal = orderToppingTotal + this.item.getPriceL();
		}
		return subTotal * this.quantity;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getItemId() {
		return itemId;
	}

	public void setItemId(Integer itemId) {
		this.itemId = itemId;
	}

	public Integer getOrderId() {
		return orderId;
	}

	public void setOrderId(Integer orderId) {
		this.orderId = orderId;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Character getSize() {
		return size;
	}

	public void setSize(Character size) {
		this.size = size;
	}

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}

	public List<OrderTopping> getOrderToppingList() {
		return orderToppingList;
	}

	public void setOrderToppingList(List<OrderTopping> orderToppingList) {
		this.orderToppingList = orderToppingList;
	}

	@Override
	public String toString() {
		return "OrderItem [id=" + id + ", itemId=" + itemId + ", orderId=" + orderId + ", quantity=" + quantity
				+ ", size=" + size + ", item=" + item + ", orderToppingList=" + orderToppingList + "]";
	}

}
