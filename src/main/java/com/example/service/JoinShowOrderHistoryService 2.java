package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.domain.Order;
import com.example.repository.JoinOrderRepository;
import com.example.repository.OrderRepository;

/**
 * 注文履歴を表示するサービスクラス.
 * @author fuka
 *
 */
@Service
public class JoinShowOrderHistoryService {

	@Autowired
	private OrderRepository orderRepository;
	
	@Autowired
	private JoinOrderRepository repository;
	
	/**
	 * 
	 * ユーザーIDに紐付く注文履歴を表示します.
	 * 
	 * @param userId　ユーザーID
	 * @return 注文情報リスト
	 */
//	public List<Order> showOrderHistory(Integer userId){
//		List<Order> orderList = new ArrayList<>();
//		orderList = repository.findByUserIdAndStatus(userId, 2);
//		return orderList;
//	}
	
//	public List<Order> getOrderHistory(Integer userId){
//		List<Order> orderList = new ArrayList<>();
//		orderList = orderRepository.findByUserId(userId);
//		return orderList;
//	}
	
	/**
	 * 注文履歴画面を表示するメソッド.
	 * 
	 * @param userId ユーザーID
	 * @return 注文情報
	 */
	public List<Order> showOrderHistory(Integer userId) {
		List<Order>orderList=repository.findByUserId(userId);
		return orderList;
	}
	
	
	
	
	
}
