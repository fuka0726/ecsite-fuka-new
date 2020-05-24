package com.example.service;

import java.sql.Date;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Order;
import com.example.repository.JoinOrderRepository;
import com.example.repository.OrderRepository;



/**
 * 注文確認画面を表示するサービスクラスです.
 * @author fuka
 *
 */
@Service
@Transactional
public class OrderConfirmService {
	
	@Autowired
	private JoinOrderRepository joinOrderRepository;
	
	@Autowired
	private OrderRepository orderRepository;
	
	
	/**
	 * 
	 * ユーザーから注文前の注文情報リストを取得します。
	 * @param userId
	 * @return
	 */
	public List<Order> showOrderList(Integer userId) {
		List<Order> orderList = joinOrderRepository.findByUserIdAndStatus(userId, 0);
		return orderList;
	}
	
	/**
	 * お届け情報から注文ステータスを更新する.
	 * @param order お届け情報
	 */
	public void updateStatus(Order order) {
		//現在時刻を取得
		LocalDateTime nowTime = LocalDateTime.now();
		//現在時刻より1時間進んだ時間を取得
		LocalDateTime oneHourLater = nowTime.plusHours(1);
		//「yyyy-MM-dd HH:mm:ss」にフォーマット
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String format = oneHourLater.format(formatter);
		// LocalDateTimeのうち、ミリ秒を削除
//		String deliver = format.substring(0,19);
//		LocalDateTime allowedDeliveryDate = LocalDateTime.parse(deliver, formatter);
		
		//注文日に関するオブジェクトを生成
		Date date = Date.valueOf(format);
		order.setOrderDate(date);
		orderRepository.updateStatus(order);
	}
	
	/**
	 * その日初めての注文の場合、シーケンスをリセット.
	 * @param orderDate
	 */
	public void resetSequence(String orderDate) {
		Integer count = orderRepository.countOrderOnTheDay(orderDate);
		if (count == 0) {
			orderRepository.resetSequence();
		}
	}
	
	
	
}
