package controller;

import java.io.Serializable;
import java.util.List;
import java.util.ArrayList;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import model.Anforderung;
import service.TestfallService;
import service.AnforderungService;

@Named
@ViewScoped
public class TestfallerstellerController implements Serializable {

	@Inject
	private TestfallService testfallService;

	@Inject
	private AnforderungService anforderungService;

	private String neuerTestfallTitel;
	private String neueTestfallBeschreibung;
	private Integer neueZuErfuellendeAnforderungId; // ID der zu erfüllenden Anforderung für den Testfall

	public String getNeuerTestfallTitel() {
		return neuerTestfallTitel;
	}

	public void setNeuerTestfallTitel(String neuerTestfallTitel) {
		this.neuerTestfallTitel = neuerTestfallTitel;
	}

	public String getNeueTestfallBeschreibung() {
		return neueTestfallBeschreibung;
	}

	public void setNeueTestfallBeschreibung(String neueTestfallBeschreibung) {
		this.neueTestfallBeschreibung = neueTestfallBeschreibung;
	}

	public Integer getNeueZuErfuellendeAnforderungId() {
		return neueZuErfuellendeAnforderungId;
	}

	public void setNeueZuErfuellendeAnforderungId(Integer neueZuErfuellendeAnforderungId) {
		this.neueZuErfuellendeAnforderungId = neueZuErfuellendeAnforderungId;
	}

	// Erstellt einen neuen Testfall
	// (wird von der Seite testfallersteller.xhtml aufgerufen)
	public void erstelleTestfall() {
		try {
			testfallService.createTestfall(neueZuErfuellendeAnforderungId, neuerTestfallTitel,
					neueTestfallBeschreibung);

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!", "Testfall erfolgreich erfasst."));

			// Felder leeren
			this.neuerTestfallTitel = null;
			this.neueTestfallBeschreibung = null;
			this.neueZuErfuellendeAnforderungId = null;

		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler!", e.getMessage()));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Speichern des Testfalles: " + e.getMessage()));
		}
	}

	// Gibt Anforderungen als SelectItems zurück (für testfallersteller.xhtml)
	public List<SelectItem> getAnforderungenAsSelectItems() {
		List<SelectItem> selectItems = new ArrayList<>();
		try {
			List<Anforderung> anforderungen = anforderungService.getAllAnforderungen();
			for (Anforderung anforderung : anforderungen) {
				selectItems.add(new SelectItem(anforderung.getID(), anforderung.getAnforderungstitel()));
			}
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Laden der Anforderungen: " + e.getMessage()));
		}
		return selectItems;
	}
}