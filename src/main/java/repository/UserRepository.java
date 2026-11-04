package repository;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import java.util.List;
import model.Benutzer;

//Repository für Benutzer-Entitäten 
@ApplicationScoped
public class UserRepository {

	@Inject
	private EntityManager em;

	// Findet einen Benutzer anhand seines Benutzernamens
	// Gibt null zurück, wenn kein Benutzer gefunden wird
	public Benutzer findByName(String username) {
		try {
			TypedQuery<Benutzer> query = em.createQuery("SELECT b FROM Benutzer b WHERE b.name = :username",
					Benutzer.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	// Findet einen Benutzer anhand seiner ID
	public Benutzer findById(Integer id) {
		return em.find(Benutzer.class, id);
	}

	// Findet alle Benutzer in der Datenbank
	public List<Benutzer> findAll() {
		return em.createQuery("SELECT b FROM Benutzer b", Benutzer.class).getResultList();
	}

	// Findet Benutzer anhand ihrer Rolle
	public List<Benutzer> findByRole(String role) {
		TypedQuery<Benutzer> query = em.createQuery("SELECT b FROM Benutzer b WHERE b.rolle = :role", Benutzer.class);
		query.setParameter("role", role);
		return query.getResultList();
	}
}