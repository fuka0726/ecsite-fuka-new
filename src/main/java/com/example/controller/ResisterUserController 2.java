package com.example.controller;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.User;
import com.example.form.UserRegisterForm;
import com.example.service.ResisterUserService;

/**
 * ユーザー登録の情報制御をするコントローラーです.
 * @author fuka
 *
 */
@Controller
@RequestMapping("")
public class ResisterUserController {
	
	@ModelAttribute
	public UserRegisterForm setUpForm() {
		return new UserRegisterForm();
	
}
	@Autowired
	private ResisterUserService service;
	
	
	/**
	 * ユーザー登録画面に遷移します. 
	 * @return　ユーザー登録画面
	 */
	@RequestMapping("/show-register")
	public String showRegister() {
		return "register_user";
	}
	
	
	/**
	 * 入力した値をフォームで受け取り、そこからユーザードメインを作成してDBに新規作成する.
	 * @param form ユーザー登録のフォーム
	 * @param result
	 * @return パスワードと確認用パスワードが一致していればログイン画面。
	 * そうでなければ、登録画面。
	 */
	@RequestMapping("/register-user")
	public String resisterUser(@Validated UserRegisterForm form, BindingResult result) {
		
		//メールアドレスの確認
		if(service.findByMail(form.getEmail()) != null){
			result.rejectValue("email", "", "そのメールアドレスは既に登録されています");
		}
		
		//パスワードの確認
		if(!form.getPassword().equals(form.getConfirmPassword())) {
			result.rejectValue("password", "", "パスワードが一致してません");
		}
		
		//エラーがある場合は登録画面に遷移
		if (result.hasErrors()) {
			return showRegister();
		}
		
		//フォームからドメインにプロパティ値をコピー
		User user = new User();
		BeanUtils.copyProperties(form, user);
		service.insert(user);
		
		//管理者権限の付与)
		
		return "redirect:/tologin";
		

	}
	
	
	
}
