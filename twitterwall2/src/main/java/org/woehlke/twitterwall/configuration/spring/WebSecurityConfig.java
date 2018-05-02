package org.woehlke.twitterwall.configuration.spring;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.woehlke.twitterwall.configuration.properties.FrontendProperties;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests()
            .antMatchers(
                frontendProperties.getWebSecurityConfigPublicPathsAsArray()
            )
            .permitAll()
            .anyRequest().authenticated()
            .and()
            .formLogin()
            .loginPage("/login")
            .failureForwardUrl("/login")
            .defaultSuccessUrl("/adm")
            .permitAll()
            .and()
            .logout()
            .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
            .logoutSuccessUrl("/logout_success")
            .permitAll();
    }

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        String user = frontendProperties.getLoginUsername();
        String pwd = frontendProperties.getLoginPassword();
        String role = "USER";
        auth
                .inMemoryAuthentication()
                .withUser(user).password(pwd).roles(role);
    }

    @Autowired
    public WebSecurityConfig(FrontendProperties frontendProperties) {
        this.frontendProperties = frontendProperties;
    }

    private final FrontendProperties frontendProperties;
}
