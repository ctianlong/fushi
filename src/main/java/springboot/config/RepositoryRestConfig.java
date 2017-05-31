package springboot.config;

import org.springframework.context.annotation.Configuration;

@Configuration
class RepositoryRestConfig {

	//配置spring-data-rest
//	@Bean
//	public RepositoryRestConfigurer repositoryRestConfigurer() {
//		return new RepositoryRestConfigurerAdapter() {
//			@Override
//			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
////				config.setBasePath("/sdrapi");
//				//暴露ID
//				config.exposeIdsFor(User.class, Role.class);
//			}
//		};
//	}
}
