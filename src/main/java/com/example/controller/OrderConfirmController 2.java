package com.example.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.mail.MailSender;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import com.example.domain.CreditCardApi;
import com.example.domain.LoginUser;
import com.example.domain.Order;
import com.example.domain.OrderItem;
import com.example.domain.OrderTopping;
import com.example.domain.User;
import com.example.form.OrderForm;
import com.example.service.CreditCardApiService;
import com.example.service.OrderConfirmService;
import com.example.service.ShoppingCartService;
import com.example.service.UserDetailServiceImpl;

/**
 * 注文前の商品を表示するコントローラー.
 * 
 * @author fuka
 *
 */
@Controller
@RequestMapping("")
public class OrderConfirmController {

	@Autowired
	private OrderConfirmService orderConfirmService;

	@Autowired
	private ShoppingCartService shoppingCartService;

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Autowired
	private CreditCardApiService CreditCardApiService;

	@Autowired
	private MailSender sender;

	// Web API を呼び出すためのメソッドを提供するクラス
	@Bean
	RestTemplate RestTemplate() {
		return new RestTemplate();
	}

	@ModelAttribute
	public OrderForm SetUpForm() {
		return new OrderForm();
	}

	/**
	 * 注文前の商品を取得し、注文確認ページに画面遷移
	 * 
	 * @param       userId ユーザーid
	 * @param model リクエストスコープ
	 * @return 注文確認ページに画面遷移
	 */
	@RequestMapping("/orderConfirm")
	public String orderConfirm(Integer userId, Model model, @AuthenticationPrincipal LoginUser loginUser) {
		List<Order> orderList = orderConfirmService.showOrderList(loginUser.getUser().getId());
		User user = userDetailServiceImpl.findByUserId(loginUser.getUser().getId());
		model.addAttribute("orderList", orderList);
		model.addAttribute("user", user);
		return "order_confirm";
	}

	/**
	 * お届け先情報を取得し、注文を確定する.
	 * 
	 * @param form お届け先情報
	 * @param resultset エラーメッセージを格納するオブジェクト
	 * @param userId ユーザーid
	 * @param model リクエストスコープ
	 * @param loginUser 利用者ユーザー
	 * @return 注文完了画面(リダイレクト)
	 */
	@RequestMapping(value = "/completeOrder", method = RequestMethod.POST)
	public String completeOrder(@Validated OrderForm form, BindingResult resultset, Integer userId, Model model,
			@AuthenticationPrincipal LoginUser loginUser) {
		LocalDateTime localDateTime = null;
		LocalDateTime timeLimit = null;

		// 日付が入力されていない場合
		if ("".equals(form.getDeliveryDate())) {
			FieldError deliveryDateError = new FieldError(resultset.getObjectName(), "deliveryDate", "配達日が未入力です");
			resultset.addError(deliveryDateError);
			// 日付が入力されている場合は、注文時間の12時間後の日時が代入された変数を作成する(配達時間のルール設定のため)
		} else {
			// 配達日をSQL用のTimeStamp型に変更
			Date date = Date.valueOf(form.getDeliveryDate());
			LocalDate localDate = date.toLocalDate(); // date型にしている
			LocalTime localTime = LocalTime.of(Integer.parseInt(form.getDeliveryHour()), 00);
			// localDateTimeは発注日時
			localDateTime = LocalDateTime.of(localDate, localTime);
			timeLimit = LocalDateTime.now();
			timeLimit = timeLimit.plusHours(12);
		}

		// 日付が入っているかつ、配達時間を注文日時の12時間以内に設定したらエラーが発生
		if (!"".equals(form.getDeliveryDate()) && timeLimit.isAfter(localDateTime)) {
			FieldError deliveryHourError = new FieldError(resultset.getObjectName(), "deliveryHour",
					"配達時間注文日時より12時間以上後のお時間帯をお選びください");
			resultset.addError(deliveryHourError);
		}

		// エラーがある場合は注文確認画面に遷移
		if (resultset.hasErrors()) {
			return orderConfirm(userId, model, loginUser);
		}

		//現在時刻を取得
		LocalDateTime nowTime = LocalDateTime.now();
		
		// データベースの総額をアップデートするために、カートリストの商品一覧を呼ぶsql実行
		Order ordered = shoppingCartService.showCartList(loginUser.getUser().getId());
		Order order = new Order();
		BeanUtils.copyProperties(form, order);
		// 注文日をセット(localDateTimeは72行目からのif文前で生成している)
		Timestamp timeStamp = Timestamp.valueOf(localDateTime);
		order.setDeliveryTime(timeStamp);
		//注文番号に日付をセット
		DateTimeFormatter formatterforOrderNumber = DateTimeFormatter.ofPattern("yyyyMMdd");
		String date_for_order_number = nowTime.format(formatterforOrderNumber);
		order.setOrderNumber(date_for_order_number);
		// ユーザーidをセット
		order.setUserId(loginUser.getUser().getId());
		// 総額をセット
		order.setTotalPrice(ordered.getCalcTotalPrice() + ordered.getTax());
		// 支払方法によって入金情報を変更
		if (order.getPaymentMethod() == 1) {
			order.setStatus(1);
		} else {
			order.setStatus(2);
		}
		
		//シーケンス判定
	
		DateTimeFormatter formatterforSequence = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		String date_for_sequence_judge = nowTime.format(formatterforSequence);
		orderConfirmService.resetSequence(date_for_sequence_judge);
		
		

		// クレカ機能実装-------------------------------------
		// カード決済用のオブジェクト生成
//		CreditCardForm creditCardForm = new CreditCardForm();
		// リクエストパラメーターで受け取った値をカード決済用のオブジェクトに入れ替える
//		BeanUtils.copyProperties(form, creditCardForm);

		// 注文確認メールに詳細情報を記載するためsql発行(カード決済のための注文idをここで取得したいため)
		List<Order> orderList = orderConfirmService.showOrderList(loginUser.getUser().getId());
		// カード決済のため注文idをセット
//		for (Order id : orderList) {
//			creditCardForm.setOrder_number(id.getId());
//		}

//		creditCardForm.setAmount(ordered.getCalcTotalPrice() + ordered.getTax());
//		creditCardForm.setUser_id(loginUser.getUser().getId());

		// クレジット決済のwebapiにリクエスト送信する
//		System.out.println(creditCardForm.getCard_cvv());
//		System.out.println(creditCardForm.getCard_exp_month());
//		System.out.println(creditCardForm.getCard_exp_year());
//		System.out.println(creditCardForm.getCard_name());
//		System.out.println(creditCardForm.getCard_number());
//		System.out.println(creditCardForm.getOrder_number());
//		System.out.println(creditCardForm.getUser_id());
		CreditCardApi creditCardApi = null;

		// 注文ステータス「2」を選択したら、
		// 注文確認画面で入力されたクレジットカード情報を受け取りクレジットAPIに接続する
		if (order.getPaymentMethod() == 2) {
			creditCardApi = CreditCardApiService.service(form);
			System.out.println(creditCardApi);
		}

		// E-01:カードの有効期限が実行時年月よりも「前」だった場合
		if (order.getPaymentMethod() == 2 && creditCardApi.getError_code().equals("E-01")) {
			FieldError creditApiError = new FieldError(resultset.getObjectName(), "error_code", "カードの有効期限が切れています");
			resultset.addError(creditApiError);
			return orderConfirm(userId, model, loginUser);
			
			// E-02:セキュリティーコードが「123」でなかった場合
		} else if (order.getPaymentMethod() == 2 && creditCardApi.getError_code().equals("E-02")) {
			FieldError creditApiError = new FieldError(resultset.getObjectName(), "error_code", "カード情報が不正です");
			resultset.addError(creditApiError);
			return orderConfirm(userId, model, loginUser);
			
			// E-03:カード番号、有効期限、セキュリティーコードが数字以外の値渡された場合
		} else if (order.getPaymentMethod() == 2 && creditCardApi.getError_code().equals("E-03")) {
			FieldError creditApiError = new FieldError(resultset.getObjectName(), "error_code",
					"カード番号、有効期限、セキュリティーコードは数値で入力して下さい");
			resultset.addError(creditApiError);
			return orderConfirm(userId, model, loginUser);
		}

		// 注文情報を更新する
		orderConfirmService.updateStatus(order);

		// メール送信するメソッドを呼ぶ.
				sendEmail(order, orderList);
		
		return "redirect:/tocomplete";

	}

	// 注文完了画面を表示する
	@RequestMapping("/tocomplete")
	public String tocomplete() {
		return "order_finished";
	}

	// 以下メール送付のコード--------------------------------

	/**
	 * 
	 * 注文内容をメールで送る
	 * 
	 * @param  order お届け情報
	 * @param orderList 注文リスト
	 */
	public void sendEmail(Order order, List<Order> orderList) {
		// 改行のためのメソッド
		String br = System.getProperty("line.separator");

		String paymentMethod = null;
		if (order.getPaymentMethod() == 1) {
			paymentMethod = "代引決済";
		} else {
			paymentMethod = "クレジットカード";
		}

		// message1 メール本文
		String msg1 = order.getDestinationName() + "様" + br + br + "この度は発注いただきありがとうございました。" + br + "ご注文を以下の通りお知らせ致します。"
				+ br + "内容にお間違いがないかご確認のほど宜しくお願い致します。" + br + br + "【注文日】：" + order.getOrderDate() + br + "【商品詳細】：" + br;

		// 商品詳細の変数をそれぞれ初期化
		String itemName = null;
		Character itemSize = null;
		Integer itemCount = null;
		Integer itemSubTotal = null;
		List<OrderTopping> toppingList = null;

		// message2 商品の詳細を変数に代入
		String msg2 = "";
		for (Order orderItem : orderList) {
			for (OrderItem item : orderItem.getOrderItemList()) {
				int i = 0;
				String toppingName = "";
				itemName = item.getItem().getName();
				itemSize = item.getSize();
				itemCount = item.getQuantity();
				itemSubTotal = item.getSubTotal();
				toppingList = item.getOrderToppingList();
				for (OrderTopping topping : toppingList) {
					toppingName = toppingName + topping.getTopping().getName() + ",";
					i = i + 1;
					if (toppingList.size() == i) {
						msg2 = msg2 + br + "・商品名：" + itemName + br + "・サイズ：" + itemSize + br + "・個数：" + itemCount + br
								+ "・トッピング：" + toppingName + br + "・金額：" + itemSubTotal + "円(税抜き)" + br
								+ "-----------------------------" + br;
					}
				}
			}
		}

		//本文　文末
		String msg3 = "【商品代金】：" + order.getTotalPrice() + "円(税込)" + br + "【お届け先】："  + order.getDestinationAddress() + br
				+ "【配達日時】：" + order.getDeliveryTime() + br + "【決済方法】：" + paymentMethod + br + br + "株式会社らくらくCoffee"; 
		
		//メールを送る準備
		SimpleMailMessage msg = new SimpleMailMessage();
		
		//送り主
		msg.setFrom("usatei@com");
		//宛先
		msg.setTo("fuuka.pyop@gmail.com"); // order.getDestinationEmail()を本来入れる
		//件名
		msg.setSubject("ご注文完了のお知らせ");
		//本文
		msg.setText(msg1 + msg2 + msg3); 
		
		this.sender.send(msg);
	}

}
