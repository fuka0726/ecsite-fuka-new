package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.OrderTopping;

/**
 * 注文したトッピングを管理するリポジトリ.
 * 
 * @author fuka
 *
 */
@Repository
public class OrderToppingRepository {

	/**
	 * orderToppingオブジェクトを生成するローマッパー.
	 */
	private final static RowMapper<OrderTopping> ORDER_TOPPING_ROWMAPPER = (rs, i) -> {
		OrderTopping orderTopping = new OrderTopping();
		orderTopping.setId(rs.getInt("id"));
		orderTopping.setToppingId(rs.getInt("topping_id"));
		orderTopping.setOrderItemId(rs.getInt("order_item_id"));
		return orderTopping;
	};

	@Autowired
	private NamedParameterJdbcTemplate template;

	/**
	 * IDからトッピング情報を取得します.
	 * 
	 * @param id 注文商品ID　　add
	 * @return 注文したトッピングリスト
	 */
	public List<OrderTopping> findByOrderItemId(Integer orderItemId) {
		String sql = "SELECT id, topping_id, order_item_id FROM order_toppings WHERE id = :id ORDER BY orderItemId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
		List<OrderTopping> orderToppingList = template.query(sql, param, ORDER_TOPPING_ROWMAPPER);
		return orderToppingList;
	}
	
	/**
	 * オーダートッピング情報を挿入します. //add
	 * 
	 * @param orderTopping オーダートッピング情報  
	 */
	public void insert(OrderTopping orderTopping) {
		String sql = "INSERT INTO order_toppings (topping_id,order_item_id) VALUES (:toppingId,:orderItemId);";
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderTopping);
		template.update(sql, param);
	}
	
	/**
	 * orderItemIdが一致するトッピングを削除するメソッド. //delete
	 * @param orderItemId　カート内の商品ID
	 */
//	public void deleteByOrderItemId(Integer orderItemId) {
//		String sql = "DELETE FROM order_toppings WHERE order_item_id = :orderItemId;";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("orderItemId", orderItemId);
//		template.update(sql, param);
//	}
	

}
