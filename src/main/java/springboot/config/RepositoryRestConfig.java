package springboot.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurerAdapter;

import springboot.domain.SysRole;
import springboot.domain.SysUser;

@Configuration
class RepositoryRestConfig {

	//配置spring-data-rest
	@Bean
	public RepositoryRestConfigurer repositoryRestConfigurer() {
		return new RepositoryRestConfigurerAdapter() {
			@Override
			public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config) {
//				config.setBasePath("/sdrapi");
				//暴露ID
				config.exposeIdsFor(SysUser.class, SysRole.class);
			}
		};
	}
}
