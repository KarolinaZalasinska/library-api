package com.example.libraryapi.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@Profile("prod")
public class ProdSecurityConfig {
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
//        httpSecurity
//                .httpBasic(Customizer.withDefaults())
//                .csrf(csrfConfigurer -> csrfConfigurer.disable())
//                .authorizeRequests(authorize ->
//                        authorize
//                                .requestMatchers(HttpMethod.POST, "/authors/**").authenticated().hasRole("ADMIN")
//                                .anyRequest().authenticated()
//                );
//        return httpSecurity.build();
//    }

    /*
        @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests()
                .antMatchers("/students").hasAnyRole("USER")
                .antMatchers("/tasks").hasAnyRole("ADMIN")
                .antMatchers("/").permitAll()
                .and()
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .formLogin() // teraz będę konfigurował formularz autoryzacji
                .loginPage("/login")
                .usernameParameter("username") // nadajemy nazwę jaka będzie jako name w inpucie loginu formularza
                .passwordParameter("password")// nadajemy nazwę jaka będzie jako name w inpucie hasła formularza
                .loginProcessingUrl("/login")
                .failureForwardUrl("/login?error") // co sięstanie w momencie wpisania błędnych danych
                .defaultSuccessUrl("/")// co sięstanie w momencie prawidłowego uwierzytelnienia
                .and()
                .logout() // mówimy springowi, że przechodzimy do obsłużenia logout
                .logoutSuccessUrl("/login")
                .logoutUrl("/logout");
        return http.build();
    }
     */
}
