package springboot.config;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.support.PersistenceAnnotationBeanPostProcessor;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

@Configuration
public class WebMvcConfig extends WebMvcConfigurerAdapter {
	
	@Override
	public void addViewControllers(ViewControllerRegistry registry) {
		registry.addViewController("/login").setViewName("login");
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
