package controller;

import java.io.Serializable;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.component.UIComponent;
import jakarta.faces.component.UIInput;
import jakarta.faces.context.FacesContext;
import jakarta.faces.event.AbortProcessingException;
import jakarta.faces.event.ComponentSystemEvent;
import jakarta.faces.validator.ValidatorException;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;
import model.Benutzer;
import util.PasswordHasher;
import util.UserSessionBean;

@Named
@ViewScoped
public class LoginController implements Serializable {

	private String usernameInput;
	private String passwordInput;
	private Benutzer authenticatedBenutzer;

	@Inject
	private EntityManager em;

	@Inject
	private UserSessionBean userSessionBean;

	public LoginController() {
	}

	// Erstellt Initialbenutzer, falls keine vorhanden sind
	// (wird beim Start der Anwendung ausgeführt)
	@PostConstruct
	public void benutzerErstellen() {
		try {
			if (em.createQuery("SELECT COUNT(b) FROM Benutzer b", Long.class).getSingleResult() == 0) {
				// Demo-Passwort aus der Umgebung (APP_SEED_PASSWORD), nie im Code
				String startPasswort = PasswordHasher
						.hash(System.getenv().getOrDefault("APP_SEED_PASSWORD", "changeme"));
				em.getTransaction().begin();
				em.persist(new Benutzer("Admin", startPasswort, "requirementsengineer"));
				em.persist(new Benutzer("NameRequirementsengineer", startPasswort, "requirementsengineer"));
				em.persist(new Benutzer("NameTestfallersteller", startPasswort, "testfallersteller"));
				em.persist(new Benutzer("NameTestmanager", startPasswort, "testmanager"));
				em.persist(new Benutzer("Nametester", startPasswort, "tester"));
				em.persist(new Benutzer("NametesterEins", startPasswort, "tester"));
				em.persist(new Benutzer("NametesterZwei", startPasswort, "tester"));
				em.getTransaction().commit();
			}
		} catch (Exception e) {
			if (em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
		}
	}

	// Erfasst den Benutzernamen nach der Eingabe
	// (login.xhtml)
	public void postValidateName(ComponentSystemEvent ev) throws AbortProcessingException {
		UIInput temp = (UIInput) ev.getComponent();
		this.usernameInput = (String) temp.getValue();
	}

	// Validiert die Login-Daten
	// (aufgerufen von login.xhtml)
	public void validateLogin(FacesContext context, UIComponent component, Object value) throws ValidatorException {
		String enteredPassword = (String) value;
		this.passwordInput = enteredPassword;

		Benutzer kandidat = null;
		try {
			TypedQuery<Benutzer> query = em.createQuery("SELECT b FROM Benutzer b WHERE b.name = :name",
					Benutzer.class);
			query.setParameter("name", this.usernameInput);
			kandidat = query.getSingleResult();
		} catch (NoResultException e) {
			// absichtlich gleiche Meldung wie bei falschem Passwort
		}

		if (kandidat == null || !passwortStimmt(kandidat, enteredPassword)) {
			throw new ValidatorException(new FacesMessage("Login falsch!"));
		}
		this.authenticatedBenutzer = kandidat;
	}

	// Prüft das Passwort gegen den Hash. Alte Klartext-Einträge werden beim
	// ersten erfolgreichen Login automatisch auf einen Hash umgestellt.
	private boolean passwortStimmt(Benutzer benutzer, String eingabe) {
		String gespeichert = benutzer.getPasswort();
		if (PasswordHasher.isHashed(gespeichert)) {
			return PasswordHasher.matches(eingabe, gespeichert);
		}
		boolean klartextTreffer = gespeichert != null && gespeichert.equals(eingabe);
		if (klartextTreffer) {
			em.getTransaction().begin();
			benutzer.setPasswort(PasswordHasher.hash(eingabe));
			em.merge(benutzer);
			em.getTransaction().commit();
		}
		return klartextTreffer;
	}

	// Führt den Login durch und leitet auf die entsprechende Seite weiter
	// (aufgerufen von login.xhtml)
	public String login() {
		if (this.authenticatedBenutzer != null) {
			// Speichern des authentifizierten Benutzers im UserSessionBean
			userSessionBean.setAuthenticatedUser(this.authenticatedBenutzer);

			String rolle = this.authenticatedBenutzer.getRolle();

			switch (rolle) {
			case "requirementsengineer":
				return "/requirementsengineer.xhtml?faces-redirect=true";
			case "testfallersteller":
				return "/testfallersteller.xhtml?faces-redirect=true";
			case "testmanager":
				return "/testmanager.xhtml?faces-redirect=true";
			case "tester":
				return "/tester.xhtml?faces-redirect=true";
			default:
				return "/login.xhtml?faces-redirect=true";
			}
		} else {
			return "/login.xhtml?faces-redirect=true";
		}
	}

	public String getUsernameInput() {
		return usernameInput;
	}

	public void setUsernameInput(String usernameInput) {
		this.usernameInput = usernameInput;
	}

	public String getPasswordInput() {
		return passwordInput;
	}

	public void setPasswordInput(String passwordInput) {
		this.passwordInput = passwordInput;
	}

	public Benutzer getAuthenticatedBenutzer() {
		return authenticatedBenutzer;
	}

	public void setAuthenticatedBenutzer(Benutzer authenticatedBenutzer) {
		this.authenticatedBenutzer = authenticatedBenutzer;
	}
}
