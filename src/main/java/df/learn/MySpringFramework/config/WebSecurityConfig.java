package df.learn.MySpringFramework.config;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * 自定义Spring安全配置。
 *
 * @author Rob Winch
 */
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	/**
	 * 配置URL
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
//		http.csrf()
//				.disable()
//				// TODO Refactor login form
//				.authorizeRequests().antMatchers("/assets/**").permitAll()
//				.anyRequest().authenticated().and().logout()
//				.logoutSuccessUrl("/login.html?logout")
//				.logoutUrl("/logout.html").permitAll().and().formLogin()
//				.defaultSuccessUrl("/index.html").loginPage("/login.html")
//				.failureUrl("/login.html?error").permitAll();
	}

	/**
	 * 配置账号和密码
	 */
	@Override
	protected void configure(AuthenticationManagerBuilder auth)
			throws Exception {
//		auth.inMemoryAuthentication().withUser("sybn").password("111")
//				.roles("USER").and().withUser("admin").password("111")
//				.roles("ADMIN", "USER");
//		// auth.jdbcAuthentication().
	}
}