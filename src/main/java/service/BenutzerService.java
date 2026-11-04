package service;

import jakarta.inject.Inject;
import jakarta.enterprise.context.ApplicationScoped;
import java.util.List;
import model.Benutzer;
import repository.UserRepository;

@ApplicationScoped
public class BenutzerService {

	@Inject
	private UserRepository userRepository;

	// Gibt alle Benutzer zurück
	// (aufgerufen vom Controller)
	public List<Benutzer> getAllBenutzer() {
		return userRepository.findAll();
	}

	// Gibt alle Benutzer mit der Rolle "tester" zurück
	// (aufgerufen vom TestmanagerController für Select-Auswahl)
	public List<Benutzer> getTester() {
		return userRepository.findByRole("tester");
	}
}