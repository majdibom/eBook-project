package org.sid.sec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
        //càd quand un utilisateur vient de s'authentifier, on appelle l'implementation de UserDetailsService et on verifie si l'utilisateur existe ou nn avec loadUserByUsername dans UserDetailsServiceImpl
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);//on va plus utiliser les sessions
        http.authorizeRequests().antMatchers("/login/**").permitAll();//on n'as pas besoin de s'authentifier quand il s'agit d'une registration ou login
        http.authorizeRequests().antMatchers(HttpMethod.GET,"/bookApps/**","/download/**").permitAll();
        http.authorizeRequests().antMatchers("/user/**").permitAll();
        http.authorizeRequests().antMatchers("/appUsers/**","/appRoles/**","/books/**","/register/**").hasAuthority("ADMIN");
       // http.authorizeRequests().anyRequest().authenticated();
        http.addFilter(new JWTAuthenticationFilter(authenticationManager()));//ce filtre la va nous permet de générer le token jwt une fois que le user saisie son username et son password

        //mnt un filtre qui va s'exécuter en premier lieu pour vérifier la signature
        http.addFilterBefore(new JWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);//le JWTAuthorizationFilter est de type UsernamePasswordAuthenticationFilter/ ce filtre est appliqué quand une requete arrive
    }
}
