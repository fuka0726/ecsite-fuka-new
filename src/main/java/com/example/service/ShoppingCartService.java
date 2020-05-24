package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.form.AddItemForm;
import com.example.repository.JoinOrderRepository;
import com.example.repository.OrderItemRepository;
import com.example.repository.OrderRepository;
import com.example.repository.OrderToppingRepository;

@Service
@Transactional
public class ShoppingCartService {

	@Autowired
	private OrderRepository orderRepository;

	@Autowired
	private OrderItemRepository orderItemRepository;

	@Autowired
	private OrderToppingRepository orderToppingRepository;

	@Autowired
	private JoinOrderRepository joinOrderRepository;


	/**
	 * 注文情報を追加します.
	 * 
	 * @param form   詳細画面のリクエストパラメータを受け取るフォーム
	 * @param userId ユーザーID
	 */
	public void addItem(AddItemForm form, Integer userId) {

		// ショッピングカートが空(orderのstatusがない)とき、order、orderItem、orderToppingの３つをinsert.
		// そうでないときはorderItem,orderToppingの2つをinsert。
		Order userOrder = orderRepository.findByUserIdAndStatus(userId, 0);
		if (userOrder == null) {    //カートが空の時の記述
			// orderオブジェクトにuserIdとstatusを格納
			Order order = new Order();
			order.setUserId(userId);
			order.setStatus(0);
			order.setTotalPrice(0);
			order = orderRepository.insert(order);  //自動採番されたIdを含むOrderオブジェクトを取得

			// orderItemオブジェクトにformとorderIdを格納
			OrderItem orderItem = new OrderItem();
			Integer intItemId = Integer.parseInt(form.getItemId());
			orderItem.setItemId(intItemId);
			orderItem.setOrderId(order.getId());   //54でとってきたorderIdをセット
			orderItem.setSize(form.getSize().toCharArray()[0]);
			orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
			orderItem = orderItemRepository.insert(orderItem);   //トッピングもorderitemのidが必要だから

			// OrderToppingオブジェクトにtoppingIdとorderItemIdを格納
			List<Integer> toppingIntegerList = form.getTopping();
			if (form.getTopping() != null) {
				for (Integer toppingId : toppingIntegerList) {
					OrderTopping orderTopping = new OrderTopping();
					orderTopping.setOrderItemId(orderItem.getId());  //63行目のをとってくる
					orderTopping.setToppingId(toppingId);
					orderToppingRepository.insert(orderTopping);   //インサートするだけ
				}
			}
		} else if (userOrder.getStatus() == 0) {   //カートに１以上商品があるとき
			// OrderItemオブジェクトにformとorderId格納
			OrderItem orderItem = new OrderItem();
			Integer intItemId = Integer.parseInt(form.getItemId());
			orderItem.setItemId(intItemId);
			orderItem.setOrderId(userOrder.getId());
			orderItem.setSize(form.getSize().toCharArray()[0]);
			orderItem.setQuantity(Integer.parseInt(form.getQuantity()));
			orderItem = orderItemRepository.insert(orderItem);

			// OrderToppingオブジェクトにtoppingIdとorderItemIdを格納
			if (form.getTopping() != null) {
				for (Integer topping : form.getTopping()) {
					OrderTopping orderTopping = new OrderTopping();
					orderTopping.setToppingId(topping);
					orderTopping.setOrderItemId(orderItem.getId());
					orderToppingRepository.insert(orderTopping);
				}
			}
		}
	}

	/**
	 * ユーザーIDに紐づく注文を表示します.
	 * 
	 * @param userId ユーザーID
	 * @return 注文情報リスト
	 */
	public Order showCartList(Integer userId) {
		if (userId == null) {
			return null;
		}
		List<Order> orderList = new ArrayList<>();
		orderList = joinOrderRepository.findByUserIdAndStatus(userId, 0);
		// 検索結果がなければnullを返す
		if (orderList.size() == 0) {
			return null;
		}
		Order order = orderList.get(0);
		
		return order;
	}
	
	/**
	 * IDに紐づく注文アイテムと注文トッピングを注文から外します.
	 * 
	 * @param id ID
	 */
	public void removeOrderItemAndOrderTopping(Integer id) {
		orderItemRepository.deleteByID(id);
	}
	
}
