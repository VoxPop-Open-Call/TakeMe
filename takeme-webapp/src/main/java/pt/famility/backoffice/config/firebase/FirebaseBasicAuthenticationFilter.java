package pt.famility.backoffice.config.firebase;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import pt.famility.backoffice.security.jwt.JWTConfigurer;
import pt.famility.backoffice.security.jwt.TokenProvider;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class FirebaseBasicAuthenticationFilter extends BasicAuthenticationFilter {

    private final Logger log = LoggerFactory.getLogger(FirebaseBasicAuthenticationFilter.class);
    private final TokenProvider tokenProvider;

    @Autowired
    public FirebaseBasicAuthenticationFilter(AuthenticationManager authenticationManager, TokenProvider tokenProvider) {
        super(authenticationManager);
        this.tokenProvider = tokenProvider;
    }

    public FirebaseBasicAuthenticationFilter(AuthenticationManager authenticationManager, AuthenticationEntryPoint authenticationEntryPoint, TokenProvider tokenProvider) {
        super(authenticationManager, authenticationEntryPoint);
        this.tokenProvider = tokenProvider;

    }

    @Override
    protected void onSuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response, Authentication authResult) {
        String jwt = tokenProvider.createToken(SecurityContextHolder.getContext().getAuthentication(), true);
        response.addHeader(JWTConfigurer.AUTHORIZATION_HEADER, "Bearer " + jwt);
    }

    static class JWTToken {

        private String idToken;

        JWTToken(String idToken) {
            this.idToken = idToken;
        }

        @JsonProperty("id_token")
        String getIdToken() {
            return idToken;
        }

        void setIdToken(String idToken) {
            this.idToken = idToken;
        }
    }
}
