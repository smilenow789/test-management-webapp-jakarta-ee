package controller;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import service.TestlaufService;
import service.TestfallService;
import service.BenutzerService;
import model.Testfall;
import model.Benutzer;

@Named
@ViewScoped
public class TestmanagerController implements Serializable {

	@Inject
	private TestlaufService testlaufService;

	@Inject
	private TestfallService testfallService;

	@Inject
	private BenutzerService benutzerService;

	private String neuerTestlaufTitel;
	private String neueTestlaufBeschreibung;
	private List<Integer> neueAusgewaehlteTestfaelleIds;
	private Integer neuerZugehoerigerTesterId;

	public String getNeuerTestlaufTitel() {
		return neuerTestlaufTitel;
	}

	public void setNeuerTestlaufTitel(String neuerTestlaufTitel) {
		this.neuerTestlaufTitel = neuerTestlaufTitel;
	}

	public String getNeueTestlaufBeschreibung() {
		return neueTestlaufBeschreibung;
	}

	public void setNeueTestlaufBeschreibung(String neueTestlaufBeschreibung) {
		this.neueTestlaufBeschreibung = neueTestlaufBeschreibung;
	}

	public List<Integer> getNeueAusgewaehlteTestfaelleIds() {
		return neueAusgewaehlteTestfaelleIds;
	}

	public void setNeueAusgewaehlteTestfaelleIds(List<Integer> neueAusgewaehlteTestfaelleIds) {
		this.neueAusgewaehlteTestfaelleIds = neueAusgewaehlteTestfaelleIds;
	}

	public Integer getNeuerZugehoerigerTesterId() {
		return neuerZugehoerigerTesterId;
	}

	public void setNeuerZugehoerigerTesterId(Integer neuerZugehoerigerTesterId) {
		this.neuerZugehoerigerTesterId = neuerZugehoerigerTesterId;
	}

	// Erstellt einen neuen Testlauf (aufgerufen von testmanager.xhtml)
	public void testlaufErstellen() {
		try {
			testlaufService.createTestlauf(neuerTestlaufTitel, neueTestlaufBeschreibung, neueAusgewaehlteTestfaelleIds,
					neuerZugehoerigerTesterId);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!", "Testlauf erfolgreich erfasst."));

			// Felder leeren
			this.neuerTestlaufTitel = null;
			this.neueTestlaufBeschreibung = null;
			this.neueAusgewaehlteTestfaelleIds = null;
			this.neuerZugehoerigerTesterId = null;

		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Warnung!", e.getMessage()));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Speichern des Testlaufs: " + e.getMessage()));
		}
	}

	// Gibt alle Testfälle als SelectItems zurück
	// (für Auswahlliste auf testmanager.xhtml)
	public List<SelectItem> getTestfaelleAsSelectItems() {
		List<SelectItem> selectItems = new ArrayList<>();
		try {
			List<Testfall> testfaelle = testfallService.getAllTestfaelle();
			for (Testfall testfall : testfaelle) {
				selectItems.add(new SelectItem(testfall.getID(), testfall.getTestfallTitel()));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Laden der Testfallliste: " + e.getMessage()));
		}
		return selectItems;
	}

	// Gibt Tester als SelectItems zurück
	// (für Auswahlliste auf testmanager.xhtml)
	public List<SelectItem> getTesterListe() {
		List<SelectItem> selectItems = new ArrayList<>();
		try {
			List<Benutzer> tester = benutzerService.getTester();
			for (Benutzer benutzer : tester) {
				selectItems.add(new SelectItem(benutzer.getID(), benutzer.getName()));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Laden der Testerliste: " + e.getMessage()));
		}
		return selectItems;
	}
}