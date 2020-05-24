package com.example.form;

/**
 * 
 * 商品の検索、並び替え、ページング用
 * @author fuka
 *
 */
public class SearchForm {
	
	private Integer page;
	
	private String name;
	
	private String sort;

	public Integer getPage() {
		return page;
	}

	public void setPage(Integer page) {
		this.page = page;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}

	@Override
	public String toString() {
		return "SearchForm [page=" + page + ", name=" + name + ", sort=" + sort + "]";
	}

	
}
