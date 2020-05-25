package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.domain.CreditCardApi;
import com.example.form.OrderForm;

/**
 * 
 * クレジット決済のwebapiを呼び出すサービスクラス.
 * 
 * @author fuka
 *
 */

@Service
public class CreditCardApiService {
	
	@Autowired
	RestTemplate restTemplate;
	
	private static final String URL = "http://192.168.0.19:8080/sample-credit-card-web-api/credit-card/payment";
	//private static final String URL = "http://172.16.0.19:8080/web-api-sample/credit-card/payment";
	
	
	/**
     * クレジットAPIに接続する.
     * 
     * @param form 注文確認画面で入力されたクレジットカード情報が入っている
     * @return APIから返ってきた情報がCreditクラスのフィールドの形に格納される
     */
	public CreditCardApi service(OrderForm form) {
		return restTemplate.postForObject(URL, form, CreditCardApi.class);
	}
	
	
}
