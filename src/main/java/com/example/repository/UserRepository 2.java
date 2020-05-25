package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Order;
import com.example.domain.User;

/**
 * Userテーブルを操作するリポジトリ.
 * @author fuka
 *
 */
@Repository
public class UserRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;

	
	/**
	 * Userオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<User> USER_ROW_MAPPER = (rs, i) -> {
		User user = new User();
		user.setId(rs.getInt("Id"));
		user.setName(rs.getString("name"));
		user.setEmail(rs.getString("email"));
		user.setPassword(rs.getString("password"));
		user.setZipcode(rs.getString("zipcode"));
		user.setAddress(rs.getString("address"));
		user.setTelephone(rs.getString("telephone"));
		return user;
	};
	
	
	/**
	 * メールアドレスとパスワードからユーザー情報を取得.
	 * @param email
	 * @param password
	 * @return ユーザー情報(検索がヒットしなければnull)
	 */
	public User findByMailAndPassword(String email, String password) {
		SqlParameterSource param = new MapSqlParameterSource().addValue("email", email).addValue("password", password);
		String sql = "select id, name , email , password , zipcode, address, telephone from users where email= :email and password= :password";
	
		try{
			User user = template.queryForObject(sql, param, USER_ROW_MAPPER);
			return user;
		} catch (Exception e) {
			return null;
		}
	}
		
		
		/**
		 * emailを受け取ってそれを用いてuser情報を返します.
		 * @param email Eメール
		 * @return　検索結果がなければnullを、あればそのユーザー情報を返します
		 */
		public User findByMail(String email) {
			String sql = "SELECT id,name,email,password,zipcode,address,telephone FROM users where email = :email;";
			SqlParameterSource param = new MapSqlParameterSource().addValue("email", email);
			List<User> userList = template.query(sql, param, USER_ROW_MAPPER);
			if (userList.size() == 0) {
				return null;
			}
			return userList.get(0);
		}
		
		/**
		 * 引数でもらったユーザー情報をusersテーブルに挿入します.
		 * @param user ユーザ情報
		 */
		public void insert(User user) {
			String sql = "INSERT INTO users (name,email,password,zipcode,address,telephone) VALUES(:name, :email, :password, :zipcode, :address, :telephone) ;";
			SqlParameterSource param = new BeanPropertySqlParameterSource(user);
			template.update(sql, param);
			
		}
		/**
		 * 注文確認画面で入力されたユーザ情報を更新.
		 * 
		 * @param user ユーザー情報
		 */
		public void update(Order order) {
			String sql = "UPDATE users SET name = :destinationName, email = :destinationEmail, zipcode =:destinationZipcode, address =:destinationAddress, telephone =:destinationTel WHERE id = 1";
			SqlParameterSource param = new BeanPropertySqlParameterSource(order);
			template.update(sql, param);
		}

		public User findByUserId(Integer userId) {
			SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
			String sql = "select id, name , email , password , zipcode, address, telephone from users where id= :userId";

			User user = template.queryForObject(sql, param, USER_ROW_MAPPER);
			return user;
		}

			
	}
	
	

