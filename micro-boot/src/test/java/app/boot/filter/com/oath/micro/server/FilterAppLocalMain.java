package app.boot.filter.com.oath.micro.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.oath.micro.server.MicroserverApp;
@Configuration
@ComponentScan(basePackages = { "app.filter.com.oath.micro.server" })
public class FilterAppLocalMain {

	

			
			public static void main(String[] args) throws InterruptedException {
				
				new MicroserverApp( FilterAppLocalMain.class, () -> "filter-app")
						.run();
			}

	}