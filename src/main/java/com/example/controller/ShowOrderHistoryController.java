package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.User;
import com.example.service.JoinShowOrderHistoryService;

/**
 * 注文履歴を表示するコントローラー.
 * @author fuka
 *
 */

@Controller
@RequestMapping("")
public class ShowOrderHistoryController {
	
	@Autowired
	private JoinShowOrderHistoryService service;
	
	/**
	 * 注文履歴を表示します.
	 * @param model リクエストスコープ
	 * @param loginUser　ユーザーID
	 * @return　注文一覧画面
	 */
	@RequestMapping("/show-order-history")
	public String showOrderHistory(Model model, @AuthenticationPrincipal LoginUser loginUser) {
		//管理者ユーザーのログインIDを渡す
		User user = loginUser.getUser();
		List<Order> orderList = service.showOrderHistory(user.getId());
		if (orderList.size() == 0) {
			model.addAttribute("message", "注文履歴がありません");
		}
		model.addAttribute("orderList", orderList);
		
		return "order_history";
	
	}
	
//	@RequestMapping("/show-order-history")
//	public String toOrderHistory(@AuthenticationPrincipal LoginUser loginUser,Model model) {
//		
//		User user = loginUser.getUser();
//		List<Order> orderHistoryList = service.getOrderHistory(user.getId());
//		
//		model.addAttribute("orderHistoryList", orderHistoryList);
//		return "order_history";
//	}
	
	
	
	
	
}
