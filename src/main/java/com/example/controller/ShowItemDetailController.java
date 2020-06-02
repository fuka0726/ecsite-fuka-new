package com.example.controller;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.service.ShowItemDetailService;

@Controller
@RequestMapping("/showItemDetail")
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
	@RequestMapping("")
	public String showItemDetail(HttpServletRequest request, HttpServletResponse response, String id, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		
		//Itemを取得
		Item item = showItemDetailService.showItemDetail(Integer.parseInt(id));
		
		//トッピングをセット
		item.setToppingList(showItemDetailService.showToppingList());
		model.addAttribute("item", item);
		
		//Cookieをセット(最大５つ)
		Cookie [] cookies = request.getCookies();
		
		if(cookies != null) {
			for (Cookie cookie :cookies) {
				if (!cookie.getName().equals("item")) { // Cookieにitemが1つもない場合(初回)
					Cookie newCookie = new Cookie("item", item.getName());
					newCookie.setMaxAge(60*60*1);
					newCookie.setPath("/");      //なぜか「"/"」のパスでないとうまく配列にならない。「"/showItemList"」だとできなかった。
					response.addCookie(newCookie);
				} else if  (cookie.getName().equals("item")) {
					String itemName = cookie.getValue() + "/" + item.getName();
					cookie.setValue(itemName);
					cookie.setPath("/");        // 有効なパスを指定しないとcookieを共有できない
					response.addCookie(cookie);
				}
			}
		}
		
		

		return "item_detail";

	}

}
