package pt.famility.backoffice.config.firebase;


import com.google.firebase.FirebaseException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pt.famility.backoffice.service.FirebaseService;
import pt.famility.backoffice.service.exception.FirebaseTokenInvalidException;


import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Component
public class FirebaseFilter extends OncePerRequestFilter {

    private static String HEADER_NAME = "X-Authorization-Firebase";

    private FirebaseService firebaseService;

    public FirebaseFilter(FirebaseService firebaseService) {
        this.firebaseService = firebaseService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, FirebaseTokenInvalidException {
        String xAuth = request.getHeader(HEADER_NAME);

        try {
            if (StringUtils.isNotBlank(xAuth)) {
                // we only extract token if header present
                FirebaseTokenHolder holder = firebaseService.parseToken(xAuth);
                String userName = holder.getUid();
                Authentication auth = new FirebaseAuthenticationToken(userName, holder);
                SecurityContextHolder.getContext().setAuthentication(auth);
            }

            filterChain.doFilter(request, response); // request must continue in the filter chain

        } catch (FirebaseTokenInvalidException | InterruptedException | ExecutionException | FirebaseException e) {
            throw new SecurityException("Error processing firebase token", e);
        }

    }
}
