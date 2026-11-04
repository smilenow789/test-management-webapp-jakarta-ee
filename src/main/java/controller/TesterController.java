package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import model.Testfall;
import service.TestfallService;
import util.UserSessionBean;

@Named
@ViewScoped
public class TesterController implements Serializable {

	@Inject
	private TestfallService testfallService;

	@Inject
	private UserSessionBean userSessionBean;

	private List<Testfall> zugewieseneTestfaelleListe;

	// Initialisiert die Liste zugewieseneTestfaelleListe beim Start
	@PostConstruct
	public void init() {
		loadAssignedTestfaelle();
	}

	public void loadAssignedTestfaelle() {
		String currentTesterName = userSessionBean.getAuthenticatedUsername();
		String currentTesterRole = userSessionBean.getAuthenticatedUserRole();
		this.zugewieseneTestfaelleListe = testfallService.getAssignedTestfaelleForTester(currentTesterName,
				currentTesterRole);
		if (this.zugewieseneTestfaelleListe == null) {
			this.zugewieseneTestfaelleListe = new ArrayList<>();
		}
	}

	// Lädt zugewiesene Testfälle für den aktuellen Tester
	// (aufgerufen beim Laden der Seite tester.xhtml)
	public List<Testfall> getZugewieseneTestfaelleListe() {
		return zugewieseneTestfaelleListe;
	}

	public void setZugewieseneTestfaelleListe(List<Testfall> zugewieseneTestfaelleListe) {
		this.zugewieseneTestfaelleListe = zugewieseneTestfaelleListe;
	}

	// Speichert die Ergebnisse der Testfälle
	// (aufgerufen von der Seite tester.xhtml)
	public void speicherTestfallErgebnisse() {
		try {
			testfallService.saveTestfallResults(zugewieseneTestfaelleListe);
			loadAssignedTestfaelle(); // Liste aktualisieren

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!", "Testfallergebnisse erfolgreich erfasst."));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Speichern der Testfallergebnisse: " + e.getMessage()));
		}
	}
}