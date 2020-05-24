package com.example.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ログイン、ログアウト機能を持つコントローラー.
 * @author fuka
 *
 */
@Controller
@RequestMapping("")
public class LoginLogoutController {
	
	@Autowired
	private HttpServletRequest request;
	
	
	/**
	 * @param model　リクエストスコープ
	 * @param error　エラー
	 * @return　ログイン画面
	 */
	@RequestMapping("/tologin")
	public String toLogin(Model model,@RequestParam(required = false) String error) {
		if (error != null) {
			model.addAttribute("errorMessage", "メールアドレスまたはパスワードが不正です");
		}
		return "login";
	}
	
	
	
	
	
}
