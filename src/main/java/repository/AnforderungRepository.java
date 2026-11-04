package repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import java.util.List;
import model.Anforderung;

// Repository für Anforderung-Entitäten 
@ApplicationScoped
public class AnforderungRepository {

	@Inject
	private EntityManager em;

	// Zählt die Anzahl aller Anforderungen
	public long count() {
		return em.createQuery("SELECT COUNT(a) FROM Anforderung a", Long.class).getSingleResult();
	}

	// Speichert eine Anforderung in der Datenbank
	@Transactional
	public void save(Anforderung anforderung) {
		em.getTransaction().begin();
		em.persist(anforderung);
		em.getTransaction().commit();
	}
	
	// Gibt ein Anforderung zurück, ermittelt anhand der ID
	// (aufgerufen von TestfallService)
	public Anforderung findById(Integer id) {
		return em.find(Anforderung.class, id);
	}

	// Gibt alle Anforderungen zurück als List
	public List<Anforderung> findAll() {
		return em.createQuery("SELECT a FROM Anforderung a", Anforderung.class).getResultList();
	}

}