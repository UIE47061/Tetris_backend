package tetris.demo;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TetrisApplication {

	public static void main(String[] args) {
		// 載入 .env 檔案中的環境變數
		Dotenv dotenv = Dotenv.configure()
				.ignoreIfMissing()  // 如果找不到 .env 檔案則忽略（例如在生產環境）
				.load();
		
		// 將 .env 中的變數設定為系統屬性，讓 Spring Boot 可以讀取
		dotenv.entries().forEach(entry -> 
			System.setProperty(entry.getKey(), entry.getValue())
		);

		SpringApplication.run(TetrisApplication.class, args);
	}

}
