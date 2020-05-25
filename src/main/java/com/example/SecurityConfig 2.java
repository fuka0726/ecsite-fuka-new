package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration //設定用クラスであることを明示する
@EnableWebSecurity //SpringSecurityのWeb用の機能を利用することを明示する
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailsService memberDetailsService;

	/*
	 * アプリケーション全体のセキュリティーフィルターを設定する. 
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.WebSecurity)
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		
		web.ignoring().antMatchers("/css/**", "/img/**" , "/js/**" ,  "/fonts/**", "/favicon.ico");
	}
	
	/* 
	 * アプリケーションの認証設定やログイン・ログアウトの設定をする.
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		http.authorizeRequests() //認証に関する設定
		.antMatchers("/", "/tologin" , "/login", "/show-register", "/register-user","/showItemList" ,"/showItemDetail","/show-cart", "/add-item", "/remove-order-item" , "/orderConfirm")
		.permitAll() //ログインしなくても使用できるパスを指定
		.anyRequest().authenticated(); //上記で指定したパス以外は認証が必要

		http.formLogin() //ログインに関する設定
			.loginPage("/tologin") //ログインする時のパス
			.loginProcessingUrl("/login") //ログインボタンを押した際に遷移させるパス
			.failureUrl("/tologin?error=true") //ログイン失敗時のパス
			.defaultSuccessUrl("/showItemList", true) //デフォルトでログイン成功時に遷移させるパス
			.usernameParameter("email")
			.passwordParameter("password");
		
		http.logout() //ログアウトに関する設定
			.logoutRequestMatcher(new AntPathRequestMatcher("/logout**")) //ログアウトする時のパス
			.logoutSuccessUrl("/") //商品一覧に遷移
			.deleteCookies("JSESSIONID") //CookieのセッションIDを削除
			.invalidateHttpSession(true); //セッションを無効
	}
	
	/* 
	 * 「認証」に関する設定.
	 * 認証ユーザを取得する「UserDetailService」の設定や
	 * パスワード照合時に使う「PasswordEncoder」の設定
	 *
	 * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder)
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberDetailsService)
		.passwordEncoder(new BCryptPasswordEncoder());
	}
	
	/**
	 * パスワードハッシュかのためのクラスをDIコンテナで使用できるようにする.
	 * @return パスワードハッシュ化のためのオブジェクト
	 */
	@Bean //これをつけると下記のクラスがDIコンテナで注入できる
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
