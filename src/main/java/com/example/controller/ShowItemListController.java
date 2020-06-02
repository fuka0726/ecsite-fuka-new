package com.example.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Item;
import com.example.domain.LoginUser;
import com.example.form.SearchForm;
import com.example.service.ShowItemListService;

/**
 * 
 * 商品一覧を表示するコントローラー.
 * @author fuka
 *
 */
@Controller
@RequestMapping("/")
public class ShowItemListController {
	
	@Autowired
	private ShowItemListService showItemListService;
	
	/**
	 * 使用するフォームオブジェクトをリクエストスコープに格納する.
	 * @return　フォーム
	 */
	@ModelAttribute
	public SearchForm setUpSearchForm() {
		return new SearchForm();
	}
	
	//１ページに表示する商品は6つ
	private static final int VIEW_SIZE = 6;
	
//	@Autowired
//	private HttpSession session;
	
	
	/**
	 * 商品一覧画面を表示します.
	 * @param form　並び順を変更するフォーム
	 * @param model　モデル
	 * @param searchName　検索文字列
	 * @param loginUser　
	 * @return　商品一覧画面
	 */
	@RequestMapping("")
	public String showItemList(SearchForm form, Model model, String searchName, HttpServletRequest request, @AuthenticationPrincipal LoginUser loginUser) {
		
		
		//Cookieを取得
		Cookie[] cookies = request.getCookies();
		String itemName = "";
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("item")) {
					itemName = cookie.getValue();
				}
			}
		}
		
		//もし商品履歴があったら
		String [] itemHistory = itemName.split("/");
		if (!StringUtils.isEmpty(itemHistory[0])) {
			List <String> itemHistoryArrayList = new ArrayList<> (Arrays.asList(itemHistory)); 
			Collections.reverse(itemHistoryArrayList);
			List <String> itemHistoryList = null;
			
			//商品が5個以上なら
			if (itemHistoryArrayList.size() >= 6) {
				itemHistoryList = new ArrayList<>();
				for (int i = 0; i <= 4; i++) {
					String item = itemHistoryArrayList.get(i);
					itemHistoryList.add(item);
				}
			} else {
				itemHistoryList = itemHistoryArrayList;
			}
			model.addAttribute("itemHistoryList" , itemHistoryList);
		}
		
		
		
		List<Item> allItemList = showItemListService.showItemList();

		//トップページは１ページ目を表示
		if(form.getPage() == null) {
			form.setPage(1);
		}
		
		//検索したページの数
		Integer count = showItemListService.count(form);
		Integer maxPage = 0;
		
		//商品数が6で割り切れた場合(全体の商品数÷6が最大ページ数)
		if (count % VIEW_SIZE == 0) {
			maxPage = count / VIEW_SIZE;
		//商品数が6で割り切れなかった場合ページ数を１増やす
		} else {
			maxPage = count / VIEW_SIZE + 1; 
		}
		
		List <Integer> pageNumberList = new ArrayList<>();
		for (int i = 1; i <=maxPage; i++) {
			pageNumberList.add(i);
		}
		
		List<Item> itemList = showItemListService.search(form);
		model.addAttribute("nowPageNumber", form.getPage());
		model.addAttribute("pageNumberList", pageNumberList);
		model.addAttribute("itemList", itemList);
//		model.addAttribute("maxPage", maxPage);
		
		
		//商品名検索機能
		if (searchName != null) {
			itemList = showItemListService.search(form);
		//ページングの数字からも検索できるように検索文字列をスコープに格納する
			model.addAttribute("searchName" , searchName);
		}
		if(itemList.size() == 0) {
			itemList = showItemListService.showItemList();
			model.addAttribute("errormessage", "該当する商品がありません");
		}				
		
		
		//オートコンプリート
		StringBuilder itemListForAutocomplete = showItemListService.getItemListForAutocomplete(allItemList);
		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
		
		
		return "item_list_coffee";
	}
		
//		List<Item> itemList = showItemListService.searchBySomeway(form);
//		Integer pageNumber = 0;
		
		//商品数が９でわりきれなかったら、ページ数を１増やす
//		if (pageNumber % 9 != 0) {
//			pageNumber = itemList.size() / 9 + 1;
//		} else {
//			pageNumber = itemList.size() / 9;
//		}
//		
//		List<Integer> pageNumberList = new ArrayList<>();
//		for (int i = 1; i <= pageNumber; i++) {
//			pageNumberList.add(i);
//		}
//		model.addAttribute("pageNumberList", pageNumberList);

	

		
//		model.addAttribute("searchNumber", searchNumber);
//		model.addAttribute("itemList", itemList);
		
	
	/**
	 * ページング機能を実装します.
	 * @param form 並び順を選択するフォーム
	 * @param page　ページ数
	 * @param searchName　検索文字列
	 * @param model　モデル
	 * @return　商品一覧画面の○ページ目
	 */
//	@RequestMapping("/to_other_page")
//	public String toOtherPage(SearchForm form, Integer page, String searchName, Model model) {
//		List<Item> itemList = showItemListService.showItemList();
//		//商品数割る9がページ数
//		Integer pageNumber = itemList.size() / 9; //
//		List<Integer> pageNumberList = new ArrayList<>();
//		for (int i = 1; i <= pageNumber; i++) {
//			if (i == page) {
//				continue;
//			}
//		pageNumberList.add(i);
//		}
//		model.addAttribute("pageNumberList", pageNumberList);
//		model.addAttribute("searchName", searchName);
//		
//		model.addAttribute("itemList", itemList.get(page - 1));
//		
//		//オートコンプリート
//		StringBuilder itemListForAutocomplete = showItemListService.getItemListForAutocomplete(itemList);
//		model.addAttribute("itemListForAutocomplete", itemListForAutocomplete);
//		return "item_list_coffee";
//	}
		
	
	
	
	
	
	
	
	
	
//	@RequestMapping("/showItemList")
//	public String showItemList(String searchName,Model model,@AuthenticationPrincipal LoginUser loginUser) {
//		List<Item> itemList = showItemListService.showItemList();
//	
//		if (searchName != null) {
//			itemList = showItemListService.searchByName(searchName);	
//			model.addAttribute("itemList", itemList);
//		}
//		if(itemList.size() == 0) {
//				itemList = showItemListService.showItemList();
//				model.addAttribute("errormessage", "該当する商品がありません");
//			}				
//		model.addAttribute("itemList", itemList);
//		return "item_list_coffee";
//	}
		
}
