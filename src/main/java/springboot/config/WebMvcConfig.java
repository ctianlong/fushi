package springboot.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import springboot.security.NavMenuActiveInterceptor;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new NavMenuActiveInterceptor()).excludePathPatterns("/teacher/**", "/", "/login");
	}
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/").setViewName("index");
		registry.addViewController("/login").setViewName("login");
		//映射 /error 到 error已经在springboot的BasicErrorController中完成
		registry.addViewController("/error").setViewName("error");
		
		//以下映射只需添加对应位置实际页面，映射过程springboot已经完成
		registry.addViewController("/403").setViewName("error/403");
		registry.addViewController("/401").setViewName("error/401");
		registry.addViewController("/404").setViewName("error/404");
	}
	
	
	/*
	 * 该Bean配合通过以下方式注入EntityManager使用，但springboot已为我们配置该Bean
	 * @PersistenceContext
	 * private EntityManager entityManager;
	 */
//	@Bean
//	public PersistenceAnnotationBeanPostProcessor persistenceAnnotationBeanPostProcessor(){
//		return new PersistenceAnnotationBeanPostProcessor();
//	}

}
