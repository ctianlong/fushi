package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

//@Configuration
//@EnableSwagger2
public class Swagger2Config {
	
//	@Bean
//	public Docket createRestApi(){
//		return new Docket(DocumentationType.SWAGGER_2)
//				.apiInfo(apiInfo())
//				.select()
//				.apis(RequestHandlerSelectors.basePackage("springboot.rest"))
//				.paths(PathSelectors.any())
//				.build();
//	}
//
//	private ApiInfo apiInfo(){
//		return new ApiInfoBuilder()
//				.title("RESTful API")
//				.description("一份API手册")
//				.termsOfServiceUrl("http://localhost:8080")
//				.contact(new Contact("ctianlong", "http://blog.tongjilab.cn", "chengtl0131@gmail.com"))
//				.version("1.0")
//				.build();
//	}
	
}
