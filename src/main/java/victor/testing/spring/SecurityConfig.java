package victor.testing.spring;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
//@Profile("!test")
public class SecurityConfig  extends WebSecurityConfigurerAdapter {

    // TODO [SEC] Start with ROLE-based authorization on URL-patterns

    @Override
    protected void configure(HttpSecurity http) throws Exception {
       http
//           .csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).and()
           .csrf().disable()
            .authorizeRequests().anyRequest().authenticated()
       .and()
       .formLogin().permitAll()
            ;
    }

    // *** Dummy users 100% in-mem
    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails userDetails = User.withDefaultPasswordEncoder().username("user").password("user").roles("USER").build();
        UserDetails adminDetails = User.withDefaultPasswordEncoder().username("admin").password("admin").roles("ADMIN").build();
        return new InMemoryUserDetailsManager(userDetails, adminDetails);
    }

    // ... then, switch to loading user data from DB:
    // *** Also loading data from DB
//    @Bean
//    public UserDetailsService userDetailsService() {
//        return new DatabaseUserDetailsService();
//    }

}
