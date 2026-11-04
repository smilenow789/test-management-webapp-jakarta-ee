package util;

import java.security.GeneralSecurityException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/**
 * Hasht Passwörter mit PBKDF2-HMAC-SHA256 (nur JDK, keine zusätzliche
 * Abhängigkeit). Format: pbkdf2$iterationen$salt$hash (Base64).
 */
public final class PasswordHasher {

	private static final String PREFIX = "pbkdf2";
	private static final String ALGORITHM = "PBKDF2WithHmacSHA256";
	private static final int ITERATIONS = 210_000;
	private static final int SALT_BYTES = 16;
	private static final int HASH_BITS = 256;
	private static final SecureRandom RANDOM = new SecureRandom();

	private PasswordHasher() {
	}

	/** Erzeugt einen gesalzenen Hash für das Klartext-Passwort. */
	public static String hash(String password) {
		byte[] salt = new byte[SALT_BYTES];
		RANDOM.nextBytes(salt);
		byte[] hash = derive(password, salt, ITERATIONS);
		return PREFIX + "$" + ITERATIONS + "$" + Base64.getEncoder().encodeToString(salt) + "$"
				+ Base64.getEncoder().encodeToString(hash);
	}

	/** Prüft ein Klartext-Passwort gegen einen gespeicherten Hash. */
	public static boolean matches(String password, String stored) {
		if (password == null || !isHashed(stored)) {
			return false;
		}
		String[] parts = stored.split("\\$");
		if (parts.length != 4) {
			return false;
		}
		try {
			int iterations = Integer.parseInt(parts[1]);
			byte[] salt = Base64.getDecoder().decode(parts[2]);
			byte[] expected = Base64.getDecoder().decode(parts[3]);
			byte[] actual = derive(password, salt, iterations);
			return MessageDigest.isEqual(expected, actual);
		} catch (IllegalArgumentException e) {
			return false;
		}
	}

	/** Gibt an, ob der gespeicherte Wert bereits ein Hash ist (kein Klartext). */
	public static boolean isHashed(String stored) {
		return stored != null && stored.startsWith(PREFIX + "$");
	}

	private static byte[] derive(String password, byte[] salt, int iterations) {
		try {
			PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, iterations, HASH_BITS);
			return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(spec).getEncoded();
		} catch (GeneralSecurityException e) {
			throw new IllegalStateException("Passwort-Hashing nicht verfügbar", e);
		}
	}

}
