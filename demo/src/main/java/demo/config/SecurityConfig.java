package demo.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;

import javax.sql.DataSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private DataSource dataSource;



    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .jdbcAuthentication()
                .dataSource(dataSource)
                .usersByUsernameQuery("select username, password, true from users where username = ?")
                .authoritiesByUsernameQuery("select username, role from users WHERE username = ?")
                .rolePrefix("ROLE_")
                .passwordEncoder(new ShaPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/login/**").permitAll()
                .antMatchers("/users/**").hasRole("ADMIN")
                .antMatchers("/appointments-employee/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers("/clients/**").hasAnyRole("ADMIN", "EMPLOYEE")
                .antMatchers("/appointments-client/**").hasRole("CLIENT")
                .and()
                .formLogin()
                .loginPage("/login")
                .successHandler(authSuccessHandler())
                .failureUrl("/login?error");

    }

    @Bean
    AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler();
    }
}