package com.retail.storeapp.security;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class AuthenticatorConfig extends WebSecurityConfigurerAdapter {

    private Logger logger = LoggerFactory.getLogger(AuthenticatorConfig.class);
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JwtConfig jwtConfig;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.addExposedHeader("authorization");
        source.registerCorsConfiguration("/**", corsConfiguration.applyPermitDefaultValues());

        //http
        //.csrf().disable()
        //.csrf().ignoringAntMatchers("/h2-console/**");
        http.headers().addHeaderWriter(new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN));

        http.cors().configurationSource(source).and()
                .csrf().disable()
                // make sure we use stateless session; session won't be used to store user's state.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()

                // handle an authorized attempts
                .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .and()

                // Add a filter to validate user credentials and add token in the response header

                // What's the authenticationManager()?
                // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
                // The filter needs this auth manager to authenticate the user.
//            .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
//            .addFilterAfter(new JwtTokenAuthenticationFilter(jwtConfig), JwtUsernameAndPasswordAuthenticationFilter.class)
                .authorizeRequests()

                // allow all POST requests
                //.antMatchers(HttpMethod.POST, jwtConfig.getUri()).permitAll()
                // Enter url path excluding context_path like ewaste in this case
                //.antMatchers("/entityQuery/**").authenticated();

                // any other requests must be authenticated
                //
//            .antMatchers("/entityPersist/saveUser").permitAll()
//            .antMatchers("/index.html").permitAll()
//            .antMatchers("/home").permitAll()
//                .antMatchers("/**").permitAll()
                //.antMatchers("/admin/index.html").permitAll()
                .antMatchers("/userService/**").permitAll()
                .antMatchers("/billService/**").permitAll()
                .antMatchers("/productService/**").permitAll()
                .antMatchers("/h2-console/**").permitAll()
                .anyRequest().authenticated();
        logger.info("jwtConfig:" + jwtConfig.toString());
    }

    // Spring has UserDetailsService interface, which can be overriden to provide our implementation for fetching user from database (or any other source).
    // The UserDetailsService object is used by the auth manager to load the user from database.
    // In addition, we need to define the password encoder also. So, auth manager can compare and verify passwords.
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Bean
    public JwtConfig jwtConfig() {
        return new JwtConfig();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
