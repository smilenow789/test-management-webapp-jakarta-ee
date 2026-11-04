package service;

import jakarta.inject.Inject;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;
import java.util.List;
import model.Anforderung;
import repository.AnforderungRepository;

@ApplicationScoped
public class AnforderungService {

	@Inject
	private AnforderungRepository anforderungRepository;

	// Initialisiert den Service beim Start
	@PostConstruct
	public void init() {
		initializeAnforderungenIfEmpty();
	}

	// Erstellt Initialanforderungen, falls die Datenbank leer ist
	// (für Demozwecke)
	@Transactional
	public void initializeAnforderungenIfEmpty() {
		if (anforderungRepository.count() == 0) {
			anforderungRepository.save(new Anforderung("EinsAnforderungsTitel", "<p>BeschreibungAnforderungEins<p>"));
			anforderungRepository.save(new Anforderung("ZweiAnforderungsTitel", "<p>BeschreibungAnforderungZwei<p>"));
			anforderungRepository.save(new Anforderung("DreiAnforderungsTitel", "<p>BeschreibungAnforderungDrei<p>"));
			anforderungRepository.save(new Anforderung("VierAnforderungsTitel", "<p>BeschreibungAnforderungVier<p>"));
			anforderungRepository.save(new Anforderung("FünfAnforderungsTitel", "<p>BeschreibungAnforderungFünf<p>"));
			anforderungRepository.save(new Anforderung("SechsAnforderungsTitel", "<p>BeschreibungAnforderungSechs<p>"));

		}
	}

	// Erstellt eine neue Anforderung
	// (wird vom Controller aufgerufen)
	@Transactional
	public Anforderung createAnforderung(String title, String description) {
		if (title == null || title.trim().isEmpty() || description == null || description.trim().isEmpty()) {
			throw new IllegalArgumentException("Titel und Beschreibung der Anforderung dürfen nicht leer sein.");
		}
		Anforderung neueAnforderung = new Anforderung(title, description);
		anforderungRepository.save(neueAnforderung);
		return neueAnforderung;
	}

	// Ruft alle Anforderungen ab
	// (wird vom Controller aufgerufen)
	public List<Anforderung> getAllAnforderungen() {
		return anforderungRepository.findAll();
	}
}