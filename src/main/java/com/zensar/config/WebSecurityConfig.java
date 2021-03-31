package com.zensar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true, proxyTargetClass = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Qualifier("userServiceImpl")
	@Autowired
	private UserDetailsService userService;

	@Autowired
	private DataSource dataSource;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userService).passwordEncoder(NoOpPasswordEncoder.getInstance());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.headers().frameOptions().sameOrigin().and().authorizeRequests()
				.antMatchers("/", "/flight/search", "/flight/book/verify", "/flight/book/cancel", "/img/**").permitAll()
				.antMatchers("/flight/book**", "/flight/book/new","/payment/new").hasRole("AGENT").antMatchers("/**").hasRole("ADMIN")

				.anyRequest().authenticated().and().formLogin().loginPage("/login").defaultSuccessUrl("/")
				.failureUrl("/login?error").permitAll().and().logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login?logout")
				.permitAll().and().csrf().disable().exceptionHandling();
	}

	/*
	 * @Bean public DaoAuthenticationProvider authenticationProvider() {
	 * DaoAuthenticationProvider auth = new DaoAuthenticationProvider();
	 * auth.setUserDetailsService(userService);
	 * auth.setPasswordEncoder(NoOpPasswordEncoder.getInstance()); //
	 * auth.setPasswordEncoder(passwordEncoder()); return auth; }
	 */

	/*
	 * @Override protected void configure(AuthenticationManagerBuilder auth) throws
	 * Exception { auth.authenticationProvider(authenticationProvider()); //
	 * auth.inMemoryAuthentication().withUser("pratapmore@gmail.com").password("123"
	 * ).roles("ROLE_ADMIN"); }
	 */
}