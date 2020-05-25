package com.example.batch;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.example.domain.Order;
import com.example.service.AdminOrderListService;

@Component
public class Batch2 implements CommandLineRunner {

	@Autowired
	private AdminOrderListService adminOrderListService;
	
	@Override
	public void run(String... args) throws Exception {
		// パス名とファイル名を作成
		LocalDate today = LocalDate.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd");
		String dayForFileName = today.format(formatter);
		String pathUrl = "/Users/fuka/Documents/workspace-spring-tool-suite-4-4.1.0.RELEASE/ecsite-fuka-new/csv_batch/";
		String fileName = "order_" + dayForFileName + ".csv";
		
		//書き込み操作はFileWriterクラスとPrintWriterクラスを使用する
		FileWriter fileWriter = new FileWriter(pathUrl + fileName);
		PrintWriter pw = new PrintWriter(new BufferedWriter(fileWriter));
		
		List<Order> orderList = adminOrderListService.getOrderListForAdmin();
		StringBuilder outputStringBuilder = new StringBuilder();
		outputStringBuilder.append("注文番号,日付,利用者名,ステータス,合計(税込)　\r\n");
		
		for (Order order : orderList) {
			String orderNumber = order.getOrderNumber();
			String Date = String.valueOf(order.getOrderDate());
			String name = order.getDestinationName();
			String status = String.valueOf(order.getStatus());
			String totalPrice = String.valueOf(order.getCalcTotalPrice());
			//CSVファイル内部に記載する形式で文字列を設定
			outputStringBuilder.append(orderNumber + "," + Date + "," + name + "," + status + "," + totalPrice + ",\r\n");
		}
		
		// CSVファイルに書き込み
		pw.print(outputStringBuilder.toString());
		pw.close();
		
		
	}
	 
	
	
	
	
	
}
