package com.example.form;

import java.util.List;

public class AddItemForm {

	private String itemId;

	private String size;

	private List<Integer> topping;

	private String quantity;

	public String getItemId() {
		return itemId;
	}

	public void setItemId(String itemId) {
		this.itemId = itemId;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public List<Integer> getTopping() {
		return topping;
	}

	public void setTopping(List<Integer> topping) {
		this.topping = topping;
	}

	public String getQuantity() {
		return quantity;
	}

	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "AddItemForm [itemId=" + itemId + ", size=" + size + ", topping=" + topping + ", quantity=" + quantity
				+ "]";
	}

}
