package com.example.ipucp.Config;


import com.example.ipucp.Dto.CustomOAuth2User;
import com.example.ipucp.Dto.CustomOAuth2UserService;
import com.example.ipucp.Dto.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;


@Configuration
@EnableWebSecurity
public class LogConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomOAuth2UserService oauthUserService;
    @Autowired
    DataSource dataSource;
    @Autowired
    UserService userService;


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/procesarLog")
                .usernameParameter("correo")
                        .defaultSuccessUrl("/redirecRol",true);


        http.authorizeRequests()
                .antMatchers("/login","/oauth/**").permitAll()
                .antMatchers("/admin","/admin/**").hasAuthority("admin")
                .antMatchers("/seguridad/establecer","/seguridad/confirmado").hasAuthority("seguridadSinInicio")
                .antMatchers("/seguridad", "/seguridad/**").hasAuthority("seguridad")
                .antMatchers("/usuario","/usuario/**").access("hasAuthority('usuario')||hasAuthority('ROLE_USER')")
                .anyRequest().permitAll();


        http.oauth2Login()
                .loginPage("/login")
                .userInfoEndpoint()
                .userService(oauthUserService)
                .and()
                .successHandler(new AuthenticationSuccessHandler() {
                    @Override
                    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {

                        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();
                        userService.processOAuthPostLogin(oAuth2User.getEmail());
                        response.sendRedirect("/loginGoogle");

                    }
                });

        http.logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select correo, contra, estado from usuario where correo = ?")
                .authoritiesByUsernameQuery("select u.correo, r.nombre_rol FROM usuario u inner join rol r on (u.idrol=r.idrol) where u.correo= ? and u.estado=\"1\"  ");
    }



}
