package springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import springboot.service.StorageService;

import org.springframework.boot.Banner;
import org.springframework.boot.CommandLineRunner;


@SpringBootApplication
public class SpringbootApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootApplication.class, args);
		
		//自定义SpringApplication
//		SpringApplication app = new SpringApplication(SpringbootApplication.class);
//		app.setBannerMode(Banner.Mode.OFF);
//		app.setAddCommandLineProperties(false);
//		app.run(args);
	}
	
	@Bean
	CommandLineRunner init(StorageService storageService) {
		return (args) -> {
//            storageService.deleteAll();
//            storageService.init();
		};
	}
}
