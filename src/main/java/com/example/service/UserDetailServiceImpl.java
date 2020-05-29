package com.example.service;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.domain.LoginUser;
import com.example.domain.User;
import com.example.repository.UserRepository;

/**
 * ログインの利用者情報に権限を付与するためのクラス.
 * @author fuka
 *
 */
@Service
public class UserDetailServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserRepository repository;
	
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		User user = repository.findByMail(email);
		if (user == null) {
			throw new UsernameNotFoundException("そのEmailは登録されていません");
		}
		//権限付与
		Collection <GrantedAuthority> authorityList =  new ArrayList<>();
		if (user.getIsAdmin()) {
			authorityList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
		} else if (!user.getIsAdmin()) {
			authorityList.add(new SimpleGrantedAuthority("ROLE_USER"));
		}
		return new LoginUser(user, authorityList); //新しいエンティティを返す.
	}
	
	/**
	 * userIdで検索してUser情報を返す
	 * @param userId
	 * @return
	 */
	public User findByUserId(Integer userId) {
		User user = repository.findByUserId(userId);
		return user;
	}

	
}
