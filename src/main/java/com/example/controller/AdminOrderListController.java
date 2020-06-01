package com.example.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.domain.Order;
import com.example.service.AdminOrderListService;



/**
 * 注文一覧画面を表示するコントローラー(管理者用).
 * 
 * @author fuka
 *
 */
@Controller
@RequestMapping("")
public class AdminOrderListController {
	
	@Autowired
	private AdminOrderListService adminOrderListService;
	
	/**
	 * 注文一覧画面を表示します.
	 * @param model
	 * @return　注文画面一覧
	 */
	@RequestMapping("/admin_order_list")
	public String toOrderList(Model model) {
		List<Order> orderList = adminOrderListService.getOrderListForAdmin();
		model.addAttribute("orderList", orderList);
		return "admin_order_list";
	}
	
	
	/**
	 * 注文情報をcsv形式でダウンロードする
	 * @param response
	 */
	@RequestMapping("/download_csv")
	public void downloadCsv(HttpServletResponse response) {
		String encodedFileName = null;
		try {
			encodedFileName = URLEncoder.encode("ラクラクCoffee.csv", "UTF-8");
		}  catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		//文字コードと出力するcsvファイル名を設定
		response.setContentType(MimeTypeUtils.APPLICATION_OCTET_STREAM_VALUE  + ";charset=utf-8");
		response.setHeader("Content-Disposition", "attachment; filename=\"" + encodedFileName + "\"" );
		
		try (PrintWriter pw = response.getWriter() ) {
			List<Order> orderList = adminOrderListService.getOrderListForAdmin();
			StringBuilder outputStringBuilder = new StringBuilder();
			outputStringBuilder.append("注文番号,日付,利用者名,ステータス,総計(税込) \r\n");
			
			for (Order order : orderList) {
				String orderNumber = order.getOrderNumber();
				String Date = String.valueOf(order.getOrderDate());
				String name = order.getDestinationName();
				String status = String.valueOf(order.getStatus());
				String totalPrice = String.valueOf(order.getTotalPrice());
				//csvファイル内部に記載する形式で文字列を設定
				outputStringBuilder.append(orderNumber + "," + Date + "," + name + "," + status + "," + totalPrice + ",\r\n");
			}
			//csvファイルに書き込み
			pw.print(outputStringBuilder.toString());
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		
	}
	
	
}
