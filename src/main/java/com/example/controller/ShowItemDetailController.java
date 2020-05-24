package com.example.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.service.ShowItemDetailService;

@Controller
@RequestMapping("")
public class ShowItemDetailController {

	@Autowired
	private ShowItemDetailService showItemDetailService;

	/**
	 * アイテムの詳細画面を表示します.
	 * 
	 * @param id    アイテムID
	 * @param model リクエストスコープ
	 * @return アイテム詳細画面
	 */
	@RequestMapping("/showItemDetail")
	public String showItemDetail(String id, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		Item item = showItemDetailService.showItemDetail(Integer.parseInt(id));
		item.setToppingList(showItemDetailService.showToppingList());
		model.addAttribute("item", item);

		return "item_detail";

	}

}
