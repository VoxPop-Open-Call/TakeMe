package pt.famility.backoffice.service.shared;


import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import pt.famility.backoffice.config.firebase.FirebaseTokenHolder;
import pt.famility.backoffice.service.exception.FirebaseTokenException;
import pt.famility.backoffice.service.exception.FirebaseTokenExceptionMessages;
import pt.famility.backoffice.service.exception.FirebaseTokenInvalidException;

import java.util.concurrent.ExecutionException;


public class FirebaseParser {

    private static final Logger log = LoggerFactory.getLogger(FirebaseParser.class);

    @Async
	public FirebaseTokenHolder parseToken(String idToken) throws FirebaseTokenInvalidException, InterruptedException, ExecutionException, FirebaseException {
		if (StringUtils.isBlank(idToken)) {
			throw new FirebaseTokenInvalidException();
		} else {
            FirebaseToken decodedToken = null;
            if(idToken != null) {
                // todo catch token expired error
                // TODO: 05.12.2017 catch token is not valid
                decodedToken = FirebaseAuth.getInstance().verifyIdTokenAsync(idToken).get();
            } else {
                throw new FirebaseException("Token is null");
            }
            return new FirebaseTokenHolder(decodedToken);
        }

	}
}
