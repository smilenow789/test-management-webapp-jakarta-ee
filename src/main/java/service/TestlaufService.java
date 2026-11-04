package service;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import model.Benutzer;
import model.Testfall;
import model.Testlauf;
import repository.TestlaufRepository;
import repository.TestfallRepository;
import repository.UserRepository;

@ApplicationScoped
public class TestlaufService {

	@Inject
	private TestlaufRepository testlaufRepository;

	@Inject
	private TestfallRepository testfallRepository;

	@Inject
	private UserRepository userRepository;

	// Erstellt einen neuen Testlauf
	// (wird vom TestmanagerController aufgerufen)
	@Transactional
	public Testlauf createTestlauf(String titel, String beschreibung, List<Integer> selectedTestfaelleIds,
			Integer zugewiesenerTesterId) {

		// Validierung der Eingabedaten
		if (titel == null || titel.trim().isEmpty() || beschreibung == null || beschreibung.trim().isEmpty()
				|| zugewiesenerTesterId == null || selectedTestfaelleIds == null || selectedTestfaelleIds.isEmpty()) {
			throw new IllegalArgumentException(
					"Alle erforderlichen Felder müssen ausgefüllt und mindestens ein Testfall ausgewählt sein.");
		}

		// Sucht den zugewiesenen Tester
		Benutzer tester = userRepository.findById(zugewiesenerTesterId);
		if (tester == null) {
			throw new IllegalArgumentException("Der zugewiesene Tester konnte nicht gefunden werden.");
		}

		// Sammelt die ausgewählten Testfälle und weist sie dem Tester zu
		Set<Testfall> selectedTestfaelle = new HashSet<>();
		for (Integer testfallId : selectedTestfaelleIds) {
			Testfall testfall = testfallRepository.findById(testfallId);
			if (testfall != null) {
				testfall.addZugewiesenerBenutzer(tester); // Weist den Tester dem Testfall zu
				testfallRepository.update(testfall); // Aktualisiert den Testfall in der Datenbank
				selectedTestfaelle.add(testfall);
			}
		}

		// Validierung, ob gültige Testfälle gefunden wurden
		if (selectedTestfaelle.isEmpty()) {
			throw new IllegalArgumentException(
					"Es wurden keine gültigen Testfälle zum Anlegen des Testlaufs gefunden.");
		}

		// Erstellt und speichert den neuen Testlauf
		Testlauf neuerTestlauf = new Testlauf(titel, beschreibung, selectedTestfaelle);
		testlaufRepository.save(neuerTestlauf);

		// Stellt die bidirektionale Beziehung von Testfall zu Testlauf sicher
		for (Testfall tf : selectedTestfaelle) {
			tf.getZugehoerigeTestlaeufe().add(neuerTestlauf);
			testfallRepository.update(tf);
		}
		return neuerTestlauf;
	}

	// Gibt alle Testläufe zurück
	// (wird vom Controller aufgerufen)
	public List<Testlauf> getAllTestlaeufe() {
		return testlaufRepository.findAll();
	}

}