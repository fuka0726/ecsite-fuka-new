package com.example.repository;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import com.example.domain.Order;

/**
 * 注文テーブルを操作するためのリポジトリクラス.
 * 
 * @author fuka
 *
 */
@Repository
public class OrderRepository {

	private final static RowMapper<Order> ORDER_ROW_MAPPER = (rs, i) -> {
		Order order = new Order();
		order.setId(rs.getInt("id"));
		order.setUserId(rs.getInt("user_id"));
		order.setStatus(rs.getInt("status"));
		order.setTotalPrice(rs.getInt("total_price"));
		order.setOrderDate(rs.getDate("order_date"));
		order.setOrderNumber(rs.getString("order_number"));
		order.setDestinationName(rs.getString("destination_name"));
		order.setDestinationEmail(rs.getString("destination_email"));
		order.setDestinationZipcode(rs.getString("destination_zipcode"));
		order.setDestinationAddress(rs.getString("destination_address"));
		order.setDestinationTel(rs.getString("destination_tel"));
		order.setDeliveryTime(rs.getTimestamp("delivery_time"));
		order.setPaymentMethod(rs.getInt("payment_method"));
		return order;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	private SimpleJdbcInsert insert;

	/**
	 * テーブルにデータ挿入時、挿入したオブジェクトのidを取得するために必要なメソッド.
	 * ShoppingCartServiceの72行目に関連
	 */
	@PostConstruct //add
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("orders");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	
	/**
	 * order情報を受け取ってそれをDB上に挿入します. add
	 * 
	 * @param order 注文情報
	 * @return idを入れたオーダー情報
	 */
	public Order insert(Order order) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		Number key = insert.executeAndReturnKey(param);
		order.setId(key.intValue());
		return order;
	}

	/**
	 * IDから注文状況を取得します.
	 * 
	 * @param id ID
	 * @return 注文状況 注文のとき
	 */
	public Order load(Integer id) {
		String sql = "SELECT id, user_id, status, total_price, order_date, order_number, destination_name, destination_email,"
				+ " destination_zipcode, destination_address, destination_tel, delivery_time, payment_method"
				+ " FROM orders WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Order order = template.queryForObject(sql, param, ORDER_ROW_MAPPER);
		return order;
	}

	/**
	 * 注文確認用の注文情報をリストで取得します.
	 * 注文確認画面
	 * @param userId ユーザーID
	 * @param status 注文状態
	 * @return 注文リスト
	 */
	public List<Order> findListByUserIdAndStatus(Integer userId, Integer status) {
		List<Order> orderList = new ArrayList<>();
		String sql = "SELECT id, user_id, status, total_price, order_date, order_number, destination_name, destination_email,"
				+ " destination_zipcode, destination_address, destination_tel, delivery_time, payment_method"
				+ " FROM orders WHERE user_id = :userId AND status = :status ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		return orderList;
	}

	/**
	 * 特定のユーザーの特定の注文状況を検索するメソッド. //orderを検索する　add+@　色々つかう
	 * 
	 * @param userId ユーザーID
	 * @param status 注文状態
	 * @return 注文リスト
	 */
	public Order findByUserIdAndStatus(Integer userId, Integer status) {
		String sql = "SELECT id, user_id, status, total_price, order_date, order_number, destination_name, destination_email,"
				+ " destination_zipcode, destination_address, destination_tel, delivery_time, payment_method"
				+ " FROM orders WHERE user_id = :userId AND status = :status ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId).addValue("status", status);
		List<Order> orderList = template.query(sql, param, ORDER_ROW_MAPPER);
		if (orderList.size() == 0) {
			return null;
		}
		return orderList.get(0);
	}

	/**
	 * 注文時に注文状態を更新します.　//注文後
	 * 
	 * @param order 注文情報
	 */	 
//	public void updateStatus(Order order) {
//		if (order.getPaymentMethod() == 1) {
//			SqlParameterSource param = new BeanPropertySqlParameterSource(order);
//			String insertSql = "UPDATE orders SET status = 2, total_price = :totalPrice, order_date = :orderDate, destination_name = :destinationName, destination_email = :destinationEmail, destination_zipcode = :destinationZipcode, destination_address = :destinationAddress, destination_tel = :destinationTel, delivery_time = :deliveryTime, payment_method = :paymentMethod WHERE id = :id AND status = 0;";
//			template.update(insertSql, param);
//		} else if (order.getPaymentMethod() == 2) {
//			SqlParameterSource param = new BeanPropertySqlParameterSource(order);
//			String insertSql = "UPDATE orders SET status = 1, total_price = :totalPrice, order_date = :orderDate, destination_name = :destinationName, destination_email = :destinationEmail, destination_zipcode = :destinationZipcode, destination_address = :destinationAddress, destination_tel = :destinationTel, delivery_time = :deliveryTime, payment_method = :paymentMethod WHERE id = :id AND status = 0;";
//			template.update(insertSql, param);
//		}
//	}
	/**
	 * Orderオブジェクトを更新します.
	 * @param order orderオブジェクト
	 */
	public void updateStatus(Order order) {
		String sql = "UPDATE orders SET status = :status,payment_method =:status, "
				+ " order_number = :orderNumber || to_char(nextval('order_number'), 'FM0000'),"
				+ " destination_name =:destinationName,"
				+ "destination_email =:destinationEmail,destination_zipcode =:destinationZipcode,"
				+ "destination_address =:destinationAddress,destination_tel =:destinationTel,"
				+ "delivery_time=:deliveryTime,order_date =:orderDate,total_price=:totalPrice "
				+ "WHERE user_id = :userId and status=0";

		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(sql, param);
	}

	/**
	 * 注文合計金額を更新します.　　注文するを押したら
	 * 
	 * @param order 注文情報
	 */
	public void updateTotalPrice(Order order) {
		String sql = "UPDATE orders SET total_price = :totalPrice WHERE id = :id ";
		SqlParameterSource param = new BeanPropertySqlParameterSource(order);
		template.update(sql, param);
	}

	/**
	 * 注文情報のユーザーIDを更新します.
	 * 
	 * @param loginUserId  ログインユーザーのID
	 * @param updateUserId 仮ユーザーのID
	 */
	public void updateUserId(int loginUserId, int dummyUserId) {
		String sql = "UPDATE orders SET user_id = :loginUserId WHERE user_id = :dummyUserId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("loginUserId", loginUserId)
				.addValue("dummyUserId", dummyUserId);
		template.update(sql, param);
	}

	/**
	 * ユーザーIDから注文情報を削除します.
	 * 
	 * @param userId ユーザーID
	 */
	public void deleteByUserId(Integer userId) {
		String sql = "DELETE FROM orders WHERE user_id = :userId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		template.update(sql, param);
	}
	
	
	/**
	 * 全ての注文済みOrderを取得する(管理者用).
	 * @return 全ての注文済みOrder
	 */
	public List<Order> findAllOrder(){
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, user_id, status, total_price, order_date, order_number, destination_name, destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method ");
		sql.append("FROM orders WHERE status <> 0  ORDER BY order_date");
		List<Order> orderList = template.query(sql.toString(), ORDER_ROW_MAPPER);
		if (orderList.size() == 0) {
			return null;
		}
		return orderList;
	}
	
	
	/**
	 *特定のユーザの注文済のorder情報を検索する.
	 *
	 * @param userId ユーザーid
	 * @return　特定ユーザーの注文済のOrder情報
	 */
	public List<Order> findByUserId(Integer userId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT id, user_id, status, total_price, order_date, order_number, destination_name, ");
		sql.append("destination_email, destination_zipcode, destination_address, destination_tel, delivery_time, payment_method ");
		sql.append("FROM orders WHERE status<> 0 AND user_id = :userId ORDER BY order_date ");
		SqlParameterSource param = new MapSqlParameterSource().addValue("userId", userId);
		List<Order> orderHistoryList = template.query(sql.toString(), param, ORDER_ROW_MAPPER);
		
//		if (orderHistoryList.size() == 0) {
//			return null;
//		}
		return orderHistoryList;
	}
	
	/**
	 * その日の注文数を取得する.
	 * @param orderDate　日付
	 * @return　その日の注文数
	 */
	public Integer countOrderOnTheDay(String orderDate) {
		SqlParameterSource param =  new MapSqlParameterSource();
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT count(*) FROM orders ");
		sql.append("WHERE status <> 0 AND to_char(order_date, 'yyyy-MM-dd') = '" + orderDate +"';");
		return template.queryForObject(sql.toString(), param, Integer.class);
	}
	
	/**
	 * 注文番号用のシーケンスをリセットする
	 * 
	 * 
	 */
	public void resetSequence() {
		SqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql  = new StringBuilder();
		sql.append("INSERT INTO dummy (number) VALUES (setval('order_number',1,false)); ");
		template.update(sql.toString(), param);
	}
	

}
