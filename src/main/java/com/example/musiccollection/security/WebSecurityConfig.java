package com.example.musiccollection.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfig {
    
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
		   .authorizeHttpRequests((requests) -> requests
				   .requestMatchers("/css/**", "/images/**", "/js/**", "/storage/**", "/music/**", "/", "/signup/**", "/error/**", "/stripe/webhook", "/user/billingPortal").permitAll() // すべてのユーザーにアクセスを許可するURL
				   .requestMatchers("/postReviewForm", "/editReviewForm", "/deleteReview", "/confirm").hasAnyRole("PAID", "ADMIN") // 有料会員のみレビューの投稿・編集・削除,予約を許可
				   .requestMatchers("/admin/**").hasRole("ADMIN") // 管理者にのみアクセスを許可するURL
				   .anyRequest().authenticated()                  // 上記以外のURLはログインが必要（会員または管理者のどちらでもOK）
			)
		   .formLogin((form) -> form
			       .loginPage("/login")                     // ログインページのURL
			       .loginProcessingUrl("/login")            // ログインフォームの送信先URL
			       .defaultSuccessUrl("/?loggedIn")         // ログイン成功時のリダイレクト先URL
			       .failureUrl("/login?error")              // ログイン失敗時のリダイレクト先URL
			       .permitAll()
			)
		   .logout((logout) -> logout
			       .logoutSuccessUrl("/?loggedOut")         // ログアウト時のリダイレクト先URL
			       .permitAll()
			)
		
		   .csrf(csrf -> csrf 
        		.ignoringRequestMatchers("/stripe/webhook", "/user/checkout", "/api/update-payment-method", "/api/delete-payment-method", "/user/billingPortal")
        		);
		
		return http.build();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
