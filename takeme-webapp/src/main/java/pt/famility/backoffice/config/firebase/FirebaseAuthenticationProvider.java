package pt.famility.backoffice.config.firebase;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import pt.famility.backoffice.domain.User;
import pt.famility.backoffice.repository.UserRepository;


import java.util.stream.Collectors;


@Component
public class FirebaseAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private UserRepository userRepository;

    public boolean supports(Class<?> authentication) {
        return (FirebaseAuthenticationToken.class.isAssignableFrom(authentication));
    }

    public Authentication authenticate(Authentication authentication) throws AuthenticationException {

        if (!supports(authentication.getClass())) {
            return null;
        }

        FirebaseAuthenticationToken authenticationToken = (FirebaseAuthenticationToken) authentication;
        String uid = ((FirebaseTokenHolder) authenticationToken.getCredentials()).getUid();

        User details = userRepository.findOneWithAuthoritiesByUid(uid)
            .orElseThrow(() -> new BadCredentialsException(uid));

        authenticationToken = new FirebaseAuthenticationToken( details , authentication.getCredentials(),
            details.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList()));

        return authenticationToken;
    }

}
