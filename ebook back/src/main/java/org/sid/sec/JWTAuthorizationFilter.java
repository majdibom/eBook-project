package org.sid.sec;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class JWTAuthorizationFilter extends OncePerRequestFilter {

    //chaque requete envoyer par le navigateur cette requete va s'exécuter d'abord
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {


        response.addHeader("Access-Control-Allow-Origin", "*");//j'autorise tt les domaine à envoyer des requetes
        response.addHeader("Access-Control-Allow-Headers", //allow j'autorise au client(le navigateur) d'envoyer tt ces entetes
                "Origin, Accept,X-Requested-With,"
                        + "Content-Type,"
                        + "Access-Control-Requested-Method,"
                        + "Access-Control-Requested-Headers,"
                        + "Authorization ");

        //expose j'autorise le client de lire le contenu des requetes qui contient ces entetes
        response.addHeader("Access-Control-Expose-Headers", "Access-Control-Allow-Origin,Access-Control-Allow-Credentials,Authorization");
        response.addHeader("Access-Control-Allow-Methods","GET,POST,PUT,DELETE,PATCH");

        if (request.getMethod().equals("OPTIONS")) {
            //je repond ok pk? psk quand il m'envoie une requete avec option, donc il a déjà envoyé des entetes(car quand le navigateur envoie GET/appRoles il envoie OPTION avant GET/appRoles pour demander l'autorisation), donc automatiquement je l'ai autorisé à faire des opérations donc on ne vérifie pas le token (code ci-dessous)
            response.setStatus(HttpServletResponse.SC_OK);
        }
        else {
            if (request.getRequestURI().equals("/login")) { //quand qqun va s'authentifier, c'est pa la peine aussi de vérifier le token et le code ci-dessous car la ressource c login ne nécessite pas vérification du token, ci on enleve cette partie il va essayer de recuperer le jwt avec le code ci-dessous et retourner null (voir https://www.youtube.com/watch?v=tBwuTyE_rO4 à 57:08)
                filterChain.doFilter(request, response);
                return;
            }

            String jwt = request.getHeader(SecurityParams.JWT_HEADER_NAME);//pour chaque requete on va verifier si le token existe dans la requete avec request.getheader....
            if (jwt == null || !jwt.startsWith(SecurityParams.HEADER_PREFIX)) { //dans ce cas en authentifie pas le user, on passe au filtre suivant qui va rejeter l'accé
                filterChain.doFilter(request, response);
                return; //return on peut l'effacer et on fait else ici, le code ci-dessous comme si dans else
            }

            JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SecurityParams.SECRET)).build();//Avant de décoder il faut signer, pour vérifier le token valide ou non par la suite, car avec le secret on vérifie la signature du token(voir note) (je pense que le verifier c le secret hashé)
            DecodedJWT decodedJWT = verifier.verify(jwt.substring(SecurityParams.HEADER_PREFIX.length()));//ici on utilise verifier(le secret) pour vérifier le token après qu'on enlève Bearer et on decode par las uite

            String username = decodedJWT.getSubject();
            List<String> roles = decodedJWT.getClaims().get("roles").asList(String.class);//GetClaims est un tableau il faut le transformer en liste avec asList (ici on veut transformer claims roles en liste de string )
            //faut transformer ces roles en collection
            Collection<GrantedAuthority> authorities = new ArrayList<>();
            roles.forEach(rn -> {
                authorities.add(new SimpleGrantedAuthority(rn));
            });

            UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(username, null, authorities);//on dit à spring voilà l'utilisateur authentifié
            SecurityContextHolder.getContext().setAuthentication(user);// on authetifie l'utilisateur qui est porté par le jwt/ définir cet utilisateur dans le contexte de sécurité de spring
            filterChain.doFilter(request, response);//cette fois on passe au filtre suivant (il vérifie s'il a le droit d'acceder au ressource demandé, l'url càd)
        }

    }
}
