package com.example.form;

/**
 * ログイン情報をリクエストパラメータで受け取るformクラス.
 * @author fuka
 *
 */
public class UserForm {

	/** メールアドレス */
	private String email;
	/** パスワード */
	private String password;
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	@Override
	public String toString() {
		return "UserForm [email=" + email + ", password=" + password + ", getEmail()=" + getEmail() + ", getPassword()="
				+ getPassword() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode() + ", toString()="
				+ super.toString() + "]";
	}
	
	
}
