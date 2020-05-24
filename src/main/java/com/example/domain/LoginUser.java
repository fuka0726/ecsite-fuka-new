package com.example.domain;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

/**
 * ユーザーのログイン情報を格納するエンティティ.
 * @author fuka
 *
 */
public class LoginUser extends org.springframework.security.core.userdetails.User {
	
	private static final long serialVersionUID = 1L;
	private final User user;
	
	
	
	/**
	 * ログイン時に利用者に権限を付与します
	 * ログインしたユーザーの情報を保持
	 * @param user　利用者
	 * @param authorityList　権限リスト
	 */
	public LoginUser(User user, Collection<GrantedAuthority> authorityList) {
		super(user.getEmail(), user.getPassword(), authorityList); //継承したUserクラス（Spring)のコンストラクタ
		this.user = user;
	}


	public User getUser() {
		return user;
	}
	
}
	
	
	

