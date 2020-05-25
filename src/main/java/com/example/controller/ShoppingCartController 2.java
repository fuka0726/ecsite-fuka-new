package com.example.controller;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.form.AddItemForm;
import com.example.service.ShoppingCartService;

@Controller
@RequestMapping("")
public class ShoppingCartController {

	@Autowired
	private ShoppingCartService service;

	@Autowired
	private HttpSession session;

	/**
	 * カートにアイテムを追加します.
	 * 
	 * @param form   フォーム
	 * @param userId ユーザーID
	 * @return カート一覧画面
	 */
	@RequestMapping("/add-item")
	// ログインユーザーを受け取る
	public String addItem(AddItemForm form, @AuthenticationPrincipal LoginUser loginUser) {
	int userId = 0;
	// ログインユーザーがいればログインユーザーのIDを渡す
	if(loginUser != null) {
		userId = loginUser.getUser().getId();  
	}else {
		userId = session.getId().hashCode();   
	}
		service.addItem(form, userId);
		return "redirect:/show-cart"; //DBに更新するときはリダイレクト
	}
	
	/**
	 * カートの中身を表示します.
	 * 
	 * @param userId hiddenで送られきたユーザーID
	 * @return カート画面
	 */
	@RequestMapping("/show-cart")
	// ログインユーザーを受け取る
	public String showCartList(Integer userId, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		Order order = null;
		// ログインしている場合
		if (loginUser != null) {
			order = service.showCartList(loginUser.getUser().getId());
			// ダミーのユーザーIDが発番されている場合
		} else if (session.getAttribute("dummyUserId") != null) {
			order = service.showCartList((int) (session.getAttribute("dummyUserId")));
		}
		// ショッピングカートにアイテムがなければメッセージを表示する
		if (order == null) {
			model.addAttribute("message", "カート内に商品はありません");
		}
		model.addAttribute("order", order);
		return "cart_list";
	}



	/**
	 * 注文アイテムIDに紐づく注文アイテムと注文トッピングをカートから除きます.
	 * 
	 * @param orderItemId 注文アイテムID
	 * @return ショッピングカートリスト
	 */
	@RequestMapping("/remove-order-item")
	// ログインユーザーを受け取る
	public String removeOrderItem(Integer orderItemId, Model model, Integer userId,
			@AuthenticationPrincipal LoginUser loginUser) {
		service.removeOrderItemAndOrderTopping(orderItemId);
//		直下1行はあとで消す。(ログインユーザーがいない場合はuserId=2で検索する)
		userId = 2;
		// ログインユーザーがいればログインユーザーのIDで検索する
		if (loginUser != null) {
			userId = loginUser.getUser().getId();
		}
		return "redirect:/show-cart";
	}
}
