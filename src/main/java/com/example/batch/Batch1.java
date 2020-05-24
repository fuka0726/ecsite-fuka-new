package com.example.batch;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;

import com.example.domain.Item;
import com.example.service.ShowItemListService;

/**
 * ECサイト追加課題　CSV→DBのバッチ処理を行うためのクラス.
 * @author fuka
 *
 */
//@Component
public class Batch1 implements CommandLineRunner {
	
	@Autowired
	private ShowItemListService showItemListService;
	
	@Override
	public void run(String... args) throws Exception {
		//実行させたい処理を書く
		//resourcesにあるファイルを読み込む
		System.out.println("開始");
		
		// BufferedReaderクラスのreadLineメソッドを使って1行ずつ読み込み表示する
		try (InputStream is = new ClassPathResource("ecsite.csv").getInputStream();
			BufferedReader br = new BufferedReader(new InputStreamReader(is))) {
			
			String line;
			while ((line = br.readLine()) != null) {
				// ,で区切って各要素を取得
				String [] element = line.split(",");
				Integer id = Integer.parseInt(element[0]);
				String name = element[1];
				String description = element[2];
				Integer priceM = Integer.parseInt(element[3]);
				Integer priceL = Integer.parseInt(element[4]);
				String imagePath = element[5];
				// itemオブジェクトを生成し、各要素をセット
				Item item = new Item();
				item.setId(id);
				item.setName(name);
				item.setDescription(description);
				item.setPriceM(priceM);
				item.setPriceL(priceL);
				item.setImagePath(imagePath);
				// deletedは手動でセット
				item.setDeleted(false);
				showItemListService.insert(item);
			}
			System.out.println("終了");
		} catch (Exception e) {
			System.out.println("バッチ処理失敗");
		}
		
		
	}
	
	
}
