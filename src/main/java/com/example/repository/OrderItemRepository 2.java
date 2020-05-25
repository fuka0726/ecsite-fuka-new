package com.example.repository;

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

import com.example.domain.OrderItem;

/**
 * 注文商品テーブルを操作するためのリポジトリクラス.
 * @author fuka
 *
 */
@Repository
public class OrderItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;
	
	private SimpleJdbcInsert insert;

	/**
	 * OrderItemオブジェクトを生成するローマッパー.
	 */
	private final static RowMapper<OrderItem> ORDER_ITEM_ROWMAPPER = (rs, i) -> {
		OrderItem orderItem = new OrderItem();
		orderItem.setId(rs.getInt("id"));
		orderItem.setItemId(rs.getInt("item_id"));
		orderItem.setOrderId(rs.getInt("order_id"));
		orderItem.setQuantity(rs.getInt("quantity"));
		orderItem.setSize(rs.getString("size").toCharArray()[0]);
		//sizeはドメインがchar型のため、String→Char型に変換
		//String size = rs.getString("size);
		//char[] charSize = size.toCharArray();
		//orderItem.setSize(charSize[0]);
		return orderItem;
	};


	/**
	 * テーブルにデータ挿入時、挿入したオブジェクトのidを取得するために必要なメソッド. //add
	 */
	@PostConstruct
	public void init() {
		SimpleJdbcInsert simpleJdbcInsert = new SimpleJdbcInsert((JdbcTemplate) template.getJdbcOperations());
		SimpleJdbcInsert withTableName = simpleJdbcInsert.withTableName("order_items");
		insert = withTableName.usingGeneratedKeyColumns("id");
	}
	/**
	 * 注文商品情報をDB上に挿入します.　//add
	 * カートに追加した時にorder_itemsテーブルに格納するメソッド.
	 * @param orderItem 注文商品
	 * @return id情報を持った注文商品
	 */
	public OrderItem insert(OrderItem orderItem) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(orderItem);
		Number key = insert.executeAndReturnKey(param);
		orderItem.setId(key.intValue());
		return orderItem;
	}

	/**
	 * 注文IDから該当する注文アイテム（商品の概要と注文内容）を取得します. //add
	 * 
	 * @param orderId
	 * @return 注文アイテムリスト
	 */
	public List<OrderItem> findListByOrderId(Integer orderId) {
		String sql = "SELECT id, item_id, order_id, quantity, size FROM order_items WHERE order_id = :orderId ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("orderId", orderId);
		List<OrderItem> orderItemList = template.query(sql, param, ORDER_ITEM_ROWMAPPER);
		return orderItemList;
	}

	/**
	 * IDに紐づく注文アイテムと注文トッピングを削除します. //delete
	 * 主キーを元にorderItemとそれに紐づいているorder_toppingをDBから削除する.
	 * @param id
	 */
	public void deleteByID(Integer id) {
		String sql = "WITH deleted AS (DELETE FROM order_items WHERE id = :id RETURNING id) "
				+ "DELETE FROM order_toppings WHERE order_item_id IN (SELECT id FROM deleted)";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		template.update(sql, param);
	}


	/**
	 * 注文商品のユーザーIDを更新します.
	 * ログイン前に追加した商品をログイン後に反映し、削除するメソッド.
	 * @param loginOrderId
	 * @param dummyOrderId
	 */
	public void updateUserId(int loginOrderId, int dummyOrderId) {
		String sql = "UPDATE order_items SET order_id = :loginOrderId WHERE order_id = :dummyOrderId";
		SqlParameterSource param = new MapSqlParameterSource().addValue("loginOrderId", loginOrderId)
				.addValue("dummyOrderId", dummyOrderId);
		template.update(sql, param);
	}

}
