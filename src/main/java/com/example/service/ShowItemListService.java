package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Item;
import com.example.form.SearchForm;
import com.example.repository.ItemRepository;

/**
 * 商品一覧を検索するサービスクラス.
 * 
 * @author fuka
 *
 */
@Service
@Transactional
public class ShowItemListService {

	@Autowired
	private ItemRepository itemRepository;

	/**
	 * 商品情報を全件取得します.
	 * @param integer 
	 * 
	 * @return 商品情報一覧
	 */
	public List<Item> showItemList() {
		List<Item> itemList = itemRepository.findAll();
		return itemList;
	}

	
	/**
	 * 商品検索を行う
	 * @param form
	 * @return　検索結果
	 */
	public List<Item> search(SearchForm form) {
		List<Item> itemList = itemRepository.search(form);
		return itemList;
	}
	
	
	/**
	 * 検索にヒットした件数を取得する
	 * @param form　商品検索フォーム
	 * @return　検索ヒット数
	 */
	public Integer count(SearchForm form) {
		Integer item = itemRepository.count(form);
		return item;
	}
	
	/**
	 * 商品を追加する(バッジ処理用).
	 * @param item
	 */
	public void insert(Item item) {
		itemRepository.insert(item);
	}
	
	
	
	/**
	 *
	 * 名前から商品を曖昧検索します.
	 * 
	 * @param name 商品名
	 * @param integer 
	 * @return 検索された商品一覧
	 */
//	public List<Item> searchByName(String name) {
//		List<Item> itemList = itemRepository.findByLikeName(name);
//		return itemList;
//	}
	
	/**
	 * 商品一覧情報を並び替えます.
	 * 
	 * @param form　選択された並び替えの順
	 * @return  並び替えた商品の情報一覧
	 */
//	public List<Item> searchBySomeway(SearchForm form) {
//		//検索する値が分類0または1の場合
//		if (form.getSort().equals(0) || form.getSort().equals(1)) {
//			//Mサイズの価格の低い順に並べ替え
//			return itemRepository.orderByLowerMsizePrice();
//		//検索する値が分類2の場合
//		} else if (form.getSort().equals(2)) {
//			//Mサイズの価格の高い順に並べ替え
//			return itemRepository.orderByHigherMsizePrice();
//		//検索する値が分類3の場合	
//		} else if (form.getSort().equals(3)) {
//			//Lサイズの価格が低い順に並べ替え
//			return itemRepository.orderByLowerLsizePrice();
//		} else {
//			//Lサイズの価格の高い順に並べ替え
//			return itemRepository.orderByHigherLsizePrice();
//		}
//	}
	
	
	
	
	


	/**
	 * オートコンプリート用にJavaScriptの配列の中身を文字列で作ります.
	 * 
	 * @param employeeList　商品一覧
	 * @return　オートコンプリート用JavaScriptの配列の文字列
	 * 　　　
	 */
	public StringBuilder getItemListForAutocomplete(List<Item> itemList) {
		StringBuilder itemListForAutocomplete = new StringBuilder();
		for (int i = 0; i < itemList.size(); i++) {
			if (i != 0) {
				itemListForAutocomplete.append(",");
			}
			Item item = itemList.get(i);
			itemListForAutocomplete.append("\"");
			itemListForAutocomplete.append(item.getName());
			itemListForAutocomplete.append("\"");
		}
		return itemListForAutocomplete;
	}
	

	
}
