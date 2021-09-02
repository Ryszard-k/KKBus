package com.pz.KKBus.Security;

import com.pz.KKBus.Security.Services.UserDetailServiceImpl;
import com.pz.KKBus.Staff.Model.Enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.firewall.HttpFirewall;
import org.springframework.security.web.firewall.StrictHttpFirewall;

import java.util.Arrays;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Configuration
    @Order(1)
    public static class App1ConfigurationAdapter extends WebSecurityConfigurerAdapter {

        private UserDetailServiceImpl userDetailServiceImpl;

        @Autowired
        public App1ConfigurationAdapter(UserDetailServiceImpl userDetailServiceImpl) {
            this.userDetailServiceImpl = userDetailServiceImpl;
        }

        public App1ConfigurationAdapter() {
            super();
        }

        @Override
        protected void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
            authenticationManagerBuilder.userDetailsService(userDetailServiceImpl);
        }

        @Override
        public void configure(WebSecurity web) throws Exception {
            web.ignoring()
                    .antMatchers("/sign-up")
                    .antMatchers("/register")
                    .antMatchers(HttpMethod.GET, "/katowiceToKrakow")
                    .antMatchers(HttpMethod.GET, "/krakowToKatowice")
                    .antMatchers(HttpMethod.GET, "/katowiceToKrakow/departure")
                    .antMatchers(HttpMethod.GET, "/krakowToKatowice/departure")
                    .antMatchers("/error");
        }

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable();
            http.headers().disable();
            http
                    .authorizeRequests()
                    .antMatchers("/katowiceToKrakow").hasAnyAuthority(Role.Admin.name(),
                        Role.OfficeWorker.name())
                    .antMatchers("/katowiceToKrakow/*").hasAnyAuthority(Role.Admin.name(),
                        Role.OfficeWorker.name())
                    .antMatchers("/krakowToKatowice").hasAnyAuthority(Role.Admin.name(),
                        Role.OfficeWorker.name())
                    .antMatchers("/krakowToKatowice/*").hasAnyAuthority(Role.Admin.name(),
                        Role.OfficeWorker.name())
                    .antMatchers("/token").hasAuthority(Role.CustomerDisabled.name())
                    .antMatchers("/password").hasAuthority(Role.CustomerEnabled.name())
                    .antMatchers("/chpassword").hasAuthority(Role.CustomerEnabled.name())
                    .antMatchers("/customer").hasAnyAuthority(Role.Admin.name(),
                    Role.OfficeWorker.name())
                    .antMatchers("/customer/*").hasAnyAuthority(Role.Admin.name(),
                    Role.OfficeWorker.name())
                    .antMatchers("/reservation").hasAnyAuthority(Role.Admin.name(),
                    Role.OfficeWorker.name(), Role.Driver.name(), Role.CustomerEnabled.name())
                    .antMatchers(HttpMethod.GET, "/reservation/*").hasAnyAuthority(Role.Admin.name(),
                    Role.OfficeWorker.name(), Role.Driver.name(), Role.CustomerEnabled.name())
                    .antMatchers(HttpMethod.POST, "/reservation/user/*")
                        .hasAnyAuthority(Role.CustomerEnabled.name())
                    .antMatchers(HttpMethod.POST, "/reservation/for-admin/*")
                        .hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers(HttpMethod.DELETE, "/reservation/user/*")
                        .hasAnyAuthority(Role.CustomerEnabled.name())
                    .antMatchers(HttpMethod.DELETE, "/reservation/for-admin/*")
                        .hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/reward").hasAnyAuthority(Role.CustomerEnabled.name(),
                    Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/reward/*").hasAnyAuthority(Role.CustomerEnabled.name(),
                    Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/rewardAdmin").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/rewardAdmin/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/availability").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/availability/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/unavailability").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/unavailability/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers(HttpMethod.GET, "/schedule").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers(HttpMethod.GET, "/schedule/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/schedule").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers(HttpMethod.GET, "/car").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers(HttpMethod.GET, "/car/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/car").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/car/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/carProperties").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/carProperties/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers(HttpMethod.GET, "/courses").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers(HttpMethod.GET, "/courses/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/courses").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/courses/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())
                    .antMatchers("/admin/*").hasAnyAuthority(Role.Admin.name())
                    .antMatchers("/report").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/report/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name(),
                        Role.Driver.name())
                    .antMatchers("/summations/*").hasAnyAuthority(Role.Admin.name(), Role.OfficeWorker.name())

                    .anyRequest().authenticated().and()
                    .formLogin().defaultSuccessUrl("/katowiceToKrakow");
        }
    }

    @Bean
    public HttpFirewall looseHttpFirewall() {
        StrictHttpFirewall firewall = new StrictHttpFirewall();
        firewall.setAllowedHttpMethods(Arrays.asList("GET", "POST", "DELETE"));
        firewall.setAllowSemicolon(true);
        firewall.setAllowUrlEncodedSlash(true);
        firewall.setAllowBackSlash(true);
        firewall.setAllowUrlEncodedPercent(true);
        firewall.setAllowUrlEncodedPeriod(true);
        return firewall;
    }
}