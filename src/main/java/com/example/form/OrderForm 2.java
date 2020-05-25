package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * @author fuka
 *
 */
public class OrderForm {
	
	/** 配達先氏名 */
	@NotBlank(message =  "お名前を入力してください")
	private String destinationName;
	
	/** 配達先メールアドレス */
	@NotBlank(message = "メールアドレスを入力してください")
	@Email(message ="アドレスが不正です")
	private String destinationEmail;
	
	/** 配達先郵便番号 */
	@Pattern(regexp = "^[0-9]{7}$", message="7桁の数字を入力してください")
	private String destinationZipcode;
	
	/** 配達先住所 */
	@NotBlank(message = "住所を入力してください")
	private String destinationAddress;
	
	/** 配達先電話番号 */
	@NotBlank(message = "電話番号を入力してください")
	private String destinationTel;
	
	/** 配達日 */
	private String deliveryDate;
	
	/** 配達時間 */
	private String deliveryHour;
	
	/** 支払方法 */
	private Integer paymentMethod;
	
	/** カード番号 */
	private String card_number;
	
	/** 有効期限(年) */
	private String card_exp_year;
	
	/** 有効期限(月) */
	private String card_exp_month;
	
	/** カード名義人 */
	private String card_name;
	
	/** セキュリティコード */
	private String card_cvv;
	
	/** ユーザーID */
	private Integer userId;
	
	/** クレジット決済apiのエラーメッセージ(入力値チェック用) */
	private String error_code;

	public String getDestinationName() {
		return destinationName;
	}

	public void setDestinationName(String destinationName) {
		this.destinationName = destinationName;
	}

	public String getDestinationEmail() {
		return destinationEmail;
	}

	public void setDestinationEmail(String destinationEmail) {
		this.destinationEmail = destinationEmail;
	}

	public String getDestinationZipcode() {
		return destinationZipcode;
	}

	public void setDestinationZipcode(String destinationZipcode) {
		this.destinationZipcode = destinationZipcode;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public void setDestinationAddress(String destinationAddress) {
		this.destinationAddress = destinationAddress;
	}

	public String getDestinationTel() {
		return destinationTel;
	}

	public void setDestinationTel(String destinationTel) {
		this.destinationTel = destinationTel;
	}

	public String getDeliveryDate() {
		return deliveryDate;
	}

	public void setDeliveryDate(String deliveryDate) {
		this.deliveryDate = deliveryDate;
	}

	public String getDeliveryHour() {
		return deliveryHour;
	}

	public void setDeliveryHour(String deliveryHour) {
		this.deliveryHour = deliveryHour;
	}

	public Integer getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(Integer paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public String getCard_number() {
		return card_number;
	}

	public void setCard_number(String card_number) {
		this.card_number = card_number;
	}

	public String getCard_exp_year() {
		return card_exp_year;
	}

	public void setCard_exp_year(String card_exp_year) {
		this.card_exp_year = card_exp_year;
	}

	public String getCard_exp_month() {
		return card_exp_month;
	}

	public void setCard_exp_month(String card_exp_month) {
		this.card_exp_month = card_exp_month;
	}

	public String getCard_name() {
		return card_name;
	}

	public void setCard_name(String card_name) {
		this.card_name = card_name;
	}

	public String getCard_cvv() {
		return card_cvv;
	}

	public void setCard_cvv(String card_cvv) {
		this.card_cvv = card_cvv;
	}

	public Integer getUserId() {
		return userId;
	}

	public void setUserId(Integer userId) {
		this.userId = userId;
	}

	public String getError_code() {
		return error_code;
	}

	public void setError_code(String error_code) {
		this.error_code = error_code;
	}

	@Override
	public String toString() {
		return "OrderForm [destinationName=" + destinationName + ", destinationEmail=" + destinationEmail
				+ ", destinationZipcode=" + destinationZipcode + ", destinationAddress=" + destinationAddress
				+ ", destinationTel=" + destinationTel + ", deliveryDate=" + deliveryDate + ", deliveryHour="
				+ deliveryHour + ", paymentMethod=" + paymentMethod + ", card_number=" + card_number
				+ ", card_exp_year=" + card_exp_year + ", card_exp_month=" + card_exp_month + ", card_name=" + card_name
				+ ", card_cvv=" + card_cvv + ", userId=" + userId + ", error_code=" + error_code + "]";
	}

	
}
