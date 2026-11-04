package service;

import jakarta.annotation.PostConstruct;
import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.ArrayList;

import model.Testfall;
import repository.TestfallRepository;
import repository.UserRepository;
import model.Anforderung;
import model.Benutzer;
import repository.AnforderungRepository;

@ApplicationScoped
public class TestfallService {

	@Inject
	private TestfallRepository testfallRepository;

	@Inject
	private UserRepository userRepository;

	@Inject
	private AnforderungRepository anforderungRepository;

	// Erstellt Testfälle beim Start, falls keine vorhanden sind
	// (für Demozwecke)
	@PostConstruct
	@Transactional
	public void initializeTestfaelleIfEmpty() {
		if (testfallRepository.count() == 0) {

			Anforderung anforderung1 = testfallRepository.findOrCreateAnforderung("EinsAnforderungsTitel",
					"BeschreibungAnforderungEins");
			Anforderung anforderung2 = testfallRepository.findOrCreateAnforderung("ZweisAnforderungsTitel",
					"BeschreibungAnforderungZwei");
			Anforderung anforderung3 = testfallRepository.findOrCreateAnforderung("DreiAnforderungsTitel",
					"BeschreibungAnforderungDrei");

			testfallRepository.save(new Testfall(anforderung1, "Testfall Titel 1", "Beschreibung für Testfall 1"));
			testfallRepository.save(new Testfall(anforderung2, "Testfall Titel 2", "Beschreibung für Testfall 2"));
			testfallRepository.save(new Testfall(anforderung2, "Testfall Titel 3", "Beschreibung für Testfall 3"));
			testfallRepository.save(new Testfall(anforderung3, "Testfall Titel 4", "Beschreibung für Testfall 4"));
			testfallRepository.save(new Testfall(anforderung3, "Testfall Titel 5", "Beschreibung für Testfall 5"));
		}
	}

	// Erstellt einen neuen Testfall
	// (wird vom Controller aufgerufen)
	@Transactional
	public Testfall createTestfall(Integer anforderungId, String testfallTitel, String beschreibung) {
		if (testfallTitel == null || testfallTitel.trim().isEmpty() || beschreibung == null
				|| beschreibung.trim().isEmpty()) {
			throw new IllegalArgumentException("Testfalltitel und Beschreibung dürfen nicht leer sein.");
		}
		if (anforderungId == null) {
			throw new IllegalArgumentException("Eine Anforderung muss für den Testfall ausgewählt werden.");
		}

		Anforderung zugehoerigeAnforderung = anforderungRepository.findById(anforderungId);

		if (zugehoerigeAnforderung == null) {
			throw new IllegalArgumentException("Ausgewählte Anforderung konnte nicht gefunden werden.");
		}

		Testfall neuerTestfall = new Testfall(zugehoerigeAnforderung, testfallTitel, beschreibung);
		testfallRepository.save(neuerTestfall);
		return neuerTestfall;
	}

	// Speichert die Ergebnisse von Testfällen
	// (wird vom TesterController aufgerufen)
	@Transactional
	public void saveTestfallResults(List<Testfall> testfaelleToUpdate) {
		if (testfaelleToUpdate != null) {
			for (Testfall t : testfaelleToUpdate) {
				testfallRepository.update(t);
			}
		}
	}

	// Gibt alle Testfälle zurück
	public List<Testfall> getAllTestfaelle() {
		return testfallRepository.findAll();
	}

	// Gibt Testfälle zurück, die einem spezifischen Tester zugewiesen sind
	// (wird vom TesterController aufgerufen)
	public List<Testfall> getAssignedTestfaelleForTester(String username, String role) {
		if (username != null && role.equals("tester")) {
			Benutzer currentTester = userRepository.findByName(username);
			if (currentTester != null) {
				return testfallRepository.findByAssignedTester(currentTester);
			}
		}
		return new ArrayList<>();
	}
}