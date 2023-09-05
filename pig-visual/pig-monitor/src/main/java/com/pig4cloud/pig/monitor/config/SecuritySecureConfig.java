/*
 * Copyright (c) 2020 pig4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pig4cloud.pig.monitor.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;
import jakarta.servlet.DispatcherType;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import java.util.UUID;

import static org.springframework.http.HttpMethod.DELETE;
import static org.springframework.http.HttpMethod.POST;

/**
 * WebSecurityConfigurer
 *
 * @author lishangbu
 * @date 2019/2/1
 */
@Configuration(proxyBeanMethods = false)
public class SecuritySecureConfig {

	private final AdminServerProperties adminServer;

	private final SecurityProperties security;

	public SecuritySecureConfig(AdminServerProperties adminServer, SecurityProperties security) {
		this.adminServer = adminServer;
		this.security = security;
	}

	@Bean
	protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
		successHandler.setTargetUrlParameter("redirectTo");
		successHandler.setDefaultTargetUrl(this.adminServer.path("/"));

		http.authorizeHttpRequests((authorizeRequests) -> authorizeRequests //
			.requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/assets/**")))
			.permitAll()
			.requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/actuator/info")))
			.permitAll()
			.requestMatchers(new AntPathRequestMatcher(adminServer.path("/actuator/health")))
			.permitAll()
			.requestMatchers(new AntPathRequestMatcher(this.adminServer.path("/login")))
			.permitAll()
			.dispatcherTypeMatchers(DispatcherType.ASYNC)
			.permitAll() // https://github.com/spring-projects/spring-security/issues/11027
			.anyRequest()
			.authenticated())
			.formLogin(
					(formLogin) -> formLogin.loginPage(this.adminServer.path("/login")).successHandler(successHandler))
			.logout((logout) -> logout.logoutUrl(this.adminServer.path("/logout")))
			.httpBasic(Customizer.withDefaults());

		http.addFilterAfter(new CustomCsrfFilter(), BasicAuthenticationFilter.class) // <5>
			.csrf((csrf) -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
				.csrfTokenRequestHandler(new CsrfTokenRequestAttributeHandler())
				.ignoringRequestMatchers(
						new AntPathRequestMatcher(this.adminServer.path("/instances"), POST.toString()), // <6>
						new AntPathRequestMatcher(this.adminServer.path("/instances/*"), DELETE.toString()), // <6>
						new AntPathRequestMatcher(this.adminServer.path("/actuator/**")) // <7>
				));

		http.rememberMe((rememberMe) -> rememberMe.key(UUID.randomUUID().toString()).tokenValiditySeconds(1209600));

		return http.build();

	}

	// Required to provide UserDetailsService for "remember functionality"
	@Bean
	public InMemoryUserDetailsManager userDetailsService(PasswordEncoder passwordEncoder) {
		UserDetails user = User.withUsername(security.getUser().getName())
			.password(passwordEncoder.encode(security.getUser().getPassword()))
			.roles("USER")
			.build();
		return new InMemoryUserDetailsManager(user);
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

}
