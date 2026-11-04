package util;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class PasswordHasherTest {

	@Test
	void richtigesPasswortWirdAkzeptiert() {
		String hash = PasswordHasher.hash("geheim-123");
		assertTrue(PasswordHasher.matches("geheim-123", hash));
	}

	@Test
	void falschesPasswortWirdAbgelehnt() {
		String hash = PasswordHasher.hash("geheim-123");
		assertFalse(PasswordHasher.matches("falsch", hash));
	}

	@Test
	void gleicheEingabeErgibtUnterschiedlicheHashes() {
		assertNotEquals(PasswordHasher.hash("abc"), PasswordHasher.hash("abc"));
	}

	@Test
	void klartextUndNullWerdenNichtAlsHashErkannt() {
		assertFalse(PasswordHasher.isHashed("123"));
		assertFalse(PasswordHasher.isHashed(null));
		assertFalse(PasswordHasher.matches("123", "123"));
		assertFalse(PasswordHasher.matches(null, PasswordHasher.hash("x")));
	}

	@Test
	void beschaedigterHashWirdAbgelehnt() {
		assertFalse(PasswordHasher.matches("x", "pbkdf2$abc$def"));
		assertFalse(PasswordHasher.matches("x", "pbkdf2$1$%%%$%%%"));
	}
}
