package org.sid.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.sid.entities.AppUser;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    //attemtAuthentication pour récupérer les information sur l'utilisateur authentifié, càd username et password
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        //on a les données dans l'objet request

        try {
            AppUser   appUser= new ObjectMapper().readValue(request.getInputStream(),AppUser.class);  //il va prendre le contenu de la requete et après on va le disserializer dans un object appUser, c la disserialisation d'un object Json à un objet java
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(appUser.getUserName(),appUser.getPassword()));
        } catch (IOException e) {
            e.printStackTrace();
            throw  new RuntimeException(e);
        }

    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {

    //ici il faut pas oublier d'importer com.auth0.jwt maven dependency

        User user= (User) authResult.getPrincipal(); //authResult nous permet de récuperer l'utilisateur authentifié
        List<String> roles=new ArrayList<>();
        authResult.getAuthorities().forEach(a->{
            roles.add(a.getAuthority());//récupérer les roles
        });
        String jwt= JWT.create()
                .withIssuer(request.getRequestURI())
                .withSubject(user.getUsername())
                .withArrayClaim("roles",roles.toArray(new String[roles.size()])) //on va convertir roles en tableau de string car claims est un tableau et on donne la dimension du tableau
                .withExpiresAt(new Date(System.currentTimeMillis()+SecurityParams.EXPIRATION))
                .sign(Algorithm.HMAC256(SecurityParams.SECRET))  ;   //med@youssfi.net c'est le secret

        //mnt ajouter le token au Header
        response.addHeader(SecurityParams.JWT_HEADER_NAME,jwt);
    }
}
