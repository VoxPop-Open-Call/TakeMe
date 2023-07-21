package pt.famility.backoffice.web.rest.util;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;

public interface ResponseBooleanUtil {

    static <X> ResponseEntity<Boolean> wrapBooleanOrNotFound(Optional<X> maybeResponse) {
        return wrapBooleanOrNotFound(maybeResponse, (HttpHeaders)null);
    }

    static <X> ResponseEntity<Boolean> wrapBooleanOrNotFound(Optional<X> maybeResponse, HttpHeaders header) {
        return (ResponseEntity)maybeResponse.map((response) -> {
            return ((ResponseEntity.BodyBuilder)ResponseEntity.ok().headers(header)).body(true);
        }).orElse(new ResponseEntity(HttpStatus.NOT_FOUND));
    }

}
