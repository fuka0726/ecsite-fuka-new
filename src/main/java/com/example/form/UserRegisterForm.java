package com.example.form;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

/**
 * ユーザー登録をする上でのリクエストパラメータを受け取るフォーム.
 * @author fuka
 *
 */
public class UserRegisterForm {
	
	/** 名前　*/
	@NotBlank(message="お名前を入力して下さい")
	private String name;
	/** Eメール*/
	@NotBlank(message="メールアドレスを入力して下さい")
	@Email(message="アドレスが不正です")
	private String email;
	/**　郵便番号 */
	@NotBlank(message="郵便番号を入力してください")
	private String zipcode;
	/** 住所 */
	@NotBlank(message="住所を入力してください")
	private String address;
	/** 電話番号 */
	@NotBlank(message="電話番号を入力してください")
	private String telephone;
	/** パスワード */
	@NotBlank(message="パスワードを入力してください")
	private String password;
	/** 確認用パスワード */
	@NotBlank(message="確認用パスワードを入力して下さい")
	private String confirmPassword;
	
	/**  管理者権限 */
	private boolean isAdmin;
	/**  管理者権限（登録フォーム用） */
	private String isAdminNumber;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getZipcode() {
		return zipcode;
	}
	public void setZipcode(String zipcode) {
		this.zipcode = zipcode;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getTelephone() {
		return telephone;
	}
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getConfirmPassword() {
		return confirmPassword;
	}
	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}
	public boolean isAdmin() {
		return isAdmin;
	}
	public void setAdmin(boolean isAdmin) {
		this.isAdmin = isAdmin;
	}
	public String getIsAdminNumber() {
		return isAdminNumber;
	}
	public void setIsAdminNumber(String isAdminNumber) {
		this.isAdminNumber = isAdminNumber;
	}
	@Override
	public String toString() {
		return "UserRegisterForm [name=" + name + ", email=" + email + ", zipcode=" + zipcode + ", address=" + address
				+ ", telephone=" + telephone + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ ", isAdmin=" + isAdmin + ", isAdminNumber=" + isAdminNumber + "]";
	}
	
	
	
	
	
}
