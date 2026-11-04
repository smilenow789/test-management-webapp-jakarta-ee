package repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import model.Testlauf;
import java.util.List;

//Repository für Testlauf-Entitäten
@ApplicationScoped
public class TestlaufRepository {

	@Inject
	private EntityManager em;

	// Speichert einen Testlauf in der Datenbank
	@Transactional
	public void save(Testlauf testlauf) {
		em.getTransaction().begin();
		em.persist(testlauf);
		em.getTransaction().commit();
	}

	// Findet einen Testlauf anhand seiner ID
	public Testlauf findById(Integer id) {
		return em.find(Testlauf.class, id);
	}

	// Findet alle Testläufe in der Datenbank
	public List<Testlauf> findAll() {
		return em.createQuery("SELECT tl FROM Testlauf tl", Testlauf.class).getResultList();
	}

}