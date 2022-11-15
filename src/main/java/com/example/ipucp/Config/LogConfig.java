package com.example.ipucp.Config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import javax.sql.DataSource;


@Configuration
@EnableWebSecurity

public class LogConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.formLogin()
                .loginPage("/login")
                .loginProcessingUrl("/procesarLog")
                .usernameParameter("correo")
                        .defaultSuccessUrl("/redirecRol",true);

        http.authorizeRequests()
                .antMatchers("/admin","/admin/**").hasAuthority("admin")
                .antMatchers("/seguridad","/seguridad/**").hasAuthority("seguridad")
                .antMatchers("/usuario","/usuario/**").access("hasAuthority('usuario')||hasAuthority('ROLE_USER') ");

        http.logout().logoutUrl("/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true);

    }
    @Autowired
    DataSource dataSource;
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication()
                .dataSource(dataSource)
                .passwordEncoder(new BCryptPasswordEncoder())
                .usersByUsernameQuery("select correo, contra, estado from usuario where correo = ?")
                .authoritiesByUsernameQuery("select u.correo, r.nombre_rol FROM usuario u inner join rol r on (u.idrol=r.idrol) where u.correo= ? and u.estado=\"1\"  ");
    }



}
