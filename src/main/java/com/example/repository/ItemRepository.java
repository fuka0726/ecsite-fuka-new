package com.example.repository;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.domain.Item;
import com.example.form.SearchForm;

/**
 * itemsテーブルを表すリポジトリ.
 * 
 * @author fuka
 *
 */
@Repository
public class ItemRepository {
	
	@Autowired
	private NamedParameterJdbcTemplate template;


	/**
	 * Itemオブジェクトを生成するローマッパー.
	 */
	private static final RowMapper<Item> ITEM_ROW_MAPPER = (rs, i) -> {
		Item item = new Item();
		item.setId(rs.getInt("id"));
		item.setName(rs.getString("name"));
		item.setDescription(rs.getString("description"));
		item.setPriceM(rs.getInt("price_m"));
		item.setPriceL(rs.getInt("price_l"));
		item.setImagePath(rs.getString("image_path"));
		item.setDeleted(rs.getBoolean("deleted"));
		return item;
	};

	
	
	/**
	 * 商品を全件検索します.
	 * 
	 * @return 商品一覧
	 */
	public List<Item> findAll() {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted FROM items ORDER BY id";
		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
		return itemList;
	}

	/**
	 * IDから商品を検索します.
	 * 商品詳細表示
	 * @param id ID
	 * @return アイテム情報
	 */
	public Item load(Integer id) {
		String sql = "SELECT id, name, description, price_m, price_l, image_path, deleted"
				+ " FROM items WHERE id = :id";
		SqlParameterSource param = new MapSqlParameterSource().addValue("id", id);
		Item item = template.queryForObject(sql, param, ITEM_ROW_MAPPER);
		return item;
	}	
	
	//createSql文(メルカリの機能)-----------------------------
	
	public StringBuilder createSql(SearchForm form,MapSqlParameterSource param, String mode) {
		StringBuilder sql = new StringBuilder();
		
		if("count".equals(mode)) {
			sql.append("SELECT count(*) ");
		} else {
			sql.append("SELECT id, name, description, price_m, price_l, image_path, deleted ");
		}
			sql.append("FROM items WHERE 1 = 1 ");
		
		//商品名の曖昧検索
		if (!StringUtils.isEmpty(form.getName())) {
			sql.append("AND name ILIKE :name ");
			param.addValue("name", "%" + form.getName() + "%");
		}
		
		//商品の並べ替え
		if(!"count".equals(mode)) {
			Integer startNumber = calcStartNumber(form);
			if ("0".equals(form.getSort())) {
				sql.append(" ORDER BY id ");
			} else if ("1".equals(form.getSort())) {
				sql.append(" ORDER BY price_m, id ");
			} else if ("2".equals(form.getSort())) {
				sql.append(" ORDER BY price_m DESC, id ");
			} else if ("3".equals(form.getSort())) {
				sql.append(" ORDER BY price_l, id ");
			} else if ("4".equals(form.getSort())) {
				sql.append(" ORDER BY price_l DESC, id ");
			}
			sql.append("LIMIT 6 OFFSET " + startNumber);
		}
		
		return sql;
	}
	
	
	
	
	/**
	 * 商品検索を行う
	 * @param form 商品検索フォーム
	 * @return　検索結果
	 */
	public List<Item> search(SearchForm form){
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, null);
		return template.query(sql.toString(), param, ITEM_ROW_MAPPER);
	}
	
	
	/**
	 * 検索にヒットした件数を取得する
	 * @param form　商品検索フォーム
	 * @return　検索ヒット数
	 */
	public Integer count(SearchForm form) {
		MapSqlParameterSource param = new MapSqlParameterSource();
		StringBuilder sql = createSql(form, param, "count");
		return template.queryForObject(sql.toString(), param, Integer.class);
	}
	

	/**
	 * 現在のページでの開始番号-1を求める.
	 * 
	 * @param form　商品検索フォーム
	 * @return　現在のページでの開始番号-1 (OFFSETで使う数字)
	 */
	private Integer calcStartNumber(SearchForm form) {
		Integer pageNumber = form.getPage();
		Integer startNumber = 6 * (pageNumber -1);
		return startNumber;
	}

	
	/**
	 * 商品を追加する(バッジ処理用).
	 * 
	 * @param item 商品情報
	 */
	public void insert (Item item) {
		SqlParameterSource param = new BeanPropertySqlParameterSource(item);
		StringBuilder sql = new StringBuilder();
		sql.append("INSERT INTO items VALUES (:id,:name,:description,:priceM,:priceL,:imagePath,:deleted) ");
		template.update(sql.toString(), param);
	}
	
	
	/**
	 * 商品名を曖昧検索します.
	 * 
	 * @param name
	 * @return 検索された商品の名前.該当商品が存在しない場合は
	 */
//	public List<Item> findByLikeName(String name) {
//		String sql = "SELECT id, name, description, price_m,price_l,image_path,deleted "
//				+ " FROM items WHERE name ilike :name";
//		SqlParameterSource param = new MapSqlParameterSource().addValue("name", "%" + name + "%");
//		List<Item> itemList = template.query(sql, param, ITEM_ROW_MAPPER);
//		return itemList;
//	}
	
	/**
	 * 安い商品順に並び替え
	 * @param priceM　Mサイズ商品
	 * @return　商品情報を返す
	 */
//	public List<Item> orderByLowerMsizePrice() {
//		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_m";
//		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
//		return itemList;
//	}
	
	/**
	 * 高い商品順に並び替え
	 * @param priceM　Mサイズ商品
	 * @return　商品情報を返す
	 */
//	public List<Item> orderByHigherMsizePrice() {
//		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_m DESC";
//		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
//		return itemList;
//	}	
	
	/**
	 * Lサイズの価格が安い順番で取得します.
	 * 
	 * @param priceL Lサイズの価格
	 * @return 商品一覧
	 */
//	public List<Item> orderByLowerLsizePrice() {
//		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_l";
//		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
//		return itemList;
//	}
	
	/**
	 * Lサイズの価格が高い順番で取得します.
	 * 
	 * @param priceL Lサイズの価格
	 * @return 商品一覧
	 */
//	public List<Item> orderByHigherLsizePrice() {
//		String sql = "SELECT id,name,description,price_m,price_l,image_path,deleted FROM items ORDER BY price_l DESC";
//		List<Item> itemList = template.query(sql, ITEM_ROW_MAPPER);
//		return itemList;
//		
//		
//	}
	
}