package pt.famility.backoffice.config.firebase;

import com.google.firebase.auth.FirebaseToken;
import lombok.ToString;

@ToString
public class FirebaseTokenHolder {
	private FirebaseToken token;

	public FirebaseTokenHolder(FirebaseToken token) {
		this.token = token;
	}

	public String getEmail() {
		return token.getEmail();
	}

	public String getName() {
		return token.getName();
	}

	public String getUid() {
		return token.getUid();
	}
}
