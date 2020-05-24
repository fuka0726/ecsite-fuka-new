package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import com.example.domain.Topping;

/**
 * toppingテーブルを表すレポジトリ.
 * @author fuka
 *
 */
@Repository
public class ToppingRepository {

	@Autowired
	private NamedParameterJdbcTemplate template;
	
	
	
	/**
	 * Toppingオブジェクトを生成するローマッパー/
	 */
	private static final RowMapper<Topping> TOPPING_ROW_MAPPER = (rs, i) -> {
		Topping topping = new Topping();
		topping.setId(rs.getInt("id"));
		topping.setName(rs.getString("name"));
		topping.setPriceM(rs.getInt("price_m"));
		topping.setPriceL(rs.getInt("price_l"));

		return topping;
	};


	/**
	 * トッピングを全件検索.
	 * 
	 * @return トッピング一覧
	 */
	public List<Topping> findAll() {
		String sql = "SELECT id,name,price_m,price_l from toppings ORDER BY id";
		List<Topping> toppingList = template.query(sql, TOPPING_ROW_MAPPER);
		return toppingList;
	}
	
	/**
	 * IDから該当するトッピング情報を取得します.
	 * 
	 * @param id ID
	 * @return トッピング情報
	 */
	public Topping load(Integer id) {
		String sql = "SELECT id,name,price_m,price_l FROM toppings WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Topping topping = template.queryForObject(sql, param, TOPPING_ROW_MAPPER);
		return topping;
	}

	/**
	 * 注文トッピングIDから該当するトッピングを取得.
	 * 
	 * @param toppingId
	 * @return 注文トッピングリスト
	 */
	public Topping findByToppingId(Integer toppingId) {
		String sql = "SELECT id,name,price_m,price_l from toppings where id= :topping_id ORDER BY id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("topping_id", toppingId);
		Topping topping = template.queryForObject(sql, param, TOPPING_ROW_MAPPER);
		return topping;
	}

}
