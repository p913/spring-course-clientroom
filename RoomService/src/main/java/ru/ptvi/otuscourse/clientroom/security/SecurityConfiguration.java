package ru.ptvi.otuscourse.clientroom.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;

@EnableWebSecurity
public class SecurityConfiguration {
    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        // ensure the passwords are encoded properly
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(users.username("master").password("password").roles("MASTER").build());
        return manager;
    }

    @Configuration
    @Order(Ordered.HIGHEST_PRECEDENCE)
    public static class MasterWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.antMatcher("/actuator/**") //
                    .authorizeRequests().anyRequest().hasAuthority(ClientRoomRole.ROLE_MASTER.name())
                    .and() //
                    .httpBasic();
        }

    }

    @Configuration
    public static class ClientRoomWebSecurityConfigurerAdapter extends WebSecurityConfigurerAdapter {

        private final UserDetailsService userDetailsService;

        public ClientRoomWebSecurityConfigurerAdapter(UserDetailsService userDetailsService) {
            this.userDetailsService = userDetailsService;
        }

        @Override
        public void configure(WebSecurity web) {
            web.ignoring()
                    .antMatchers("/**/*.css", "/**/*.js")
                    .antMatchers("/webjars/**");
        }

        @Override
        public void configure(HttpSecurity http) throws Exception {
            http.csrf().disable()
                    .authorizeRequests()
                        .antMatchers("/register.html", "/reg/**").permitAll()
                        .antMatchers("/", "/room/**").hasAuthority(ClientRoomRole.ROLE_CONTRAGENT.name())
                        .anyRequest().authenticated()
                        .and()
                    .formLogin()
                        .loginPage("/login.html")
                        .defaultSuccessUrl("/", true)
                        .permitAll()
                        .and()
                    .logout()
                        .logoutSuccessUrl("/login.html")
                        .and()
                    .rememberMe()
                        .key("d335e240-8896-44e9-9a4e-c6f0223c8838");
        }

        /*@Bean
        public PasswordEncoder passwordEncoder() {
            return new BCryptPasswordEncoder(10);
        }*/


        @Override
        public UserDetailsService userDetailsService() {
            return userDetailsService;
        }


    }
}