package util;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.enterprise.inject.Disposes;
import jakarta.enterprise.inject.Produces;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

// Stellt EntityManager und EntityManagerFactory für die Anwendung bereit
@ApplicationScoped
public class EntityManagerProducer {

	// Erzeugt und verwaltet die EntityManagerFactory
	@Produces
	@ApplicationScoped
	public EntityManagerFactory createEntityManagerFactory() {
		// Verbindungsdaten kommen aus der Umgebung (siehe README / .env.example)
		Map<String, String> props = new HashMap<>();
		props.put("jakarta.persistence.jdbc.url",
				System.getenv().getOrDefault("DB_URL", "jdbc:mysql://localhost:3306/fallstudiedb"));
		props.put("jakarta.persistence.jdbc.user", System.getenv().getOrDefault("DB_USER", "fallstudie"));
		props.put("jakarta.persistence.jdbc.password", System.getenv().getOrDefault("DB_PASSWORD", ""));
		return Persistence.createEntityManagerFactory("fallstudie", props);
	}

	// Erzeugt und verwaltet einen EntityManager pro Anfrage (Request-Scope)
	@Produces
	@RequestScoped
	public EntityManager createEntityManager(EntityManagerFactory emf) {
		return emf.createEntityManager();
	}

	// Schließt den EntityManager, wenn er nicht mehr benötigt wird
	public void closeEntityManager(@Disposes EntityManager em) {
		if (em.isOpen()) {
			em.close();
		}
	}

	// Schließt die EntityManagerFactory, wenn die Anwendung beendet wird
	public void closeEntityManagerFactory(@Disposes EntityManagerFactory emf) {
		if (emf.isOpen()) {
			emf.close();
		}
	}
}