package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@EnableWebSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {
    private Boolean httpsDisabled;

    public SecurityConfiguration(@Value("${HTTPS_DISABLED}")Boolean httpsDisabled) {
        this.httpsDisabled = httpsDisabled;
    }

    @Override
    public void configure(HttpSecurity security){
        try {
            if (!httpsDisabled){
                    security.requiresChannel().anyRequest().requiresSecure();
            }
            security.authorizeRequests().antMatchers("/**")
                    .hasRole("USER")
                    .and()
                    .httpBasic()
                    .and()
                    .csrf()
                    .disable();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
    authenticationManagerBuilder.inMemoryAuthentication()
            .withUser("user").password("password").roles("USER");
    }
}
