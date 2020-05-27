package by.company.auction.configs;

import by.company.auction.services.UserDetailsServiceAuction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceAuction userDetailsServiceAuction;

    @Autowired
    public void configure(AuthenticationManagerBuilder builder) throws Exception {
        builder.userDetailsService(userDetailsServiceAuction).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http
                .csrf().disable()
                .httpBasic()
                .and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests()

                .antMatchers(HttpMethod.GET, "/users/current").authenticated()
                .antMatchers(HttpMethod.GET, "/users/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/users/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PATCH, "/users/**").authenticated()

                .antMatchers(HttpMethod.POST, "/towns/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/towns/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/towns/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/categories/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/categories/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/categories/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/companies/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.PUT, "/companies/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.DELETE, "/companies/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.DELETE, "/messages/**").hasAnyAuthority("ADMIN")
                .antMatchers(HttpMethod.GET, "/messages/**").authenticated()

                .antMatchers(HttpMethod.POST, "/bids/**").hasAnyAuthority("USER")
                .antMatchers(HttpMethod.DELETE, "/bids/**").hasAnyAuthority("ADMIN")

                .antMatchers(HttpMethod.POST, "/lots/**").hasAnyAuthority("VENDOR")
                .antMatchers(HttpMethod.PATCH, "/lots/**").hasAnyAuthority("ADMIN, VENDOR")
                .antMatchers(HttpMethod.DELETE, "/lots/**").hasAnyAuthority("ADMIN, VENDOR")

                .antMatchers("/**").permitAll();
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
