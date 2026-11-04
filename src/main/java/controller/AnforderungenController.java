package controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import jakarta.annotation.PostConstruct;
import jakarta.faces.application.FacesMessage;
import jakarta.faces.context.FacesContext;
import jakarta.faces.model.SelectItem;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;

import model.Anforderung;
import service.AnforderungService;

@Named
@ViewScoped
public class AnforderungenController implements Serializable {

	@Inject
	private AnforderungService anforderungService;

	private List<Anforderung> liste;

	private String neueAnforderungTitel;
	private String neueAnforderungBeschreibung;

	@PostConstruct
	public void init() {
		loadAnforderungen();
	}

	// Alle Anforderungen aus der DB laden
	public void loadAnforderungen() {
		this.liste = anforderungService.getAllAnforderungen();
	}

	public List<Anforderung> getListe() {
		return liste;
	}

	public void setListe(List<Anforderung> liste) {
		this.liste = liste;
	}

	public String getNeueAnforderungTitel() {
		return neueAnforderungTitel;
	}

	public void setNeueAnforderungTitel(String neueAnforderungTitel) {
		this.neueAnforderungTitel = neueAnforderungTitel;
	}

	public String getNeueAnforderungBeschreibung() {
		return neueAnforderungBeschreibung;
	}

	public void setNeueAnforderungBeschreibung(String neueAnforderungBeschreibung) {
		this.neueAnforderungBeschreibung = neueAnforderungBeschreibung;
	}

	// Erstellt eine neue Anforderung
	// (wird von der Seite requirementsengineer.xhtml aufgerufen)
	public void erstelleAnforderung() {
		try {
			anforderungService.createAnforderung(neueAnforderungTitel, neueAnforderungBeschreibung);
			loadAnforderungen();

			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_INFO, "Erfolg!", "Anforderung erfolgreich erfasst."));
			// Felder leeren
			this.neueAnforderungTitel = null;
			this.neueAnforderungBeschreibung = null;

		} catch (IllegalArgumentException e) {
			FacesContext.getCurrentInstance().addMessage(null,
					new FacesMessage(FacesMessage.SEVERITY_WARN, "Fehler!", e.getMessage()));
		} catch (Exception e) {
			FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR, "Fehler!",
					"Unerwarteter Fehler beim Speichern der Anforderung: " + e.getMessage()));
		}
	}

	// Gibt Anforderungen als SelectItems zurück (für testfallmanager.xhtml)
	public List<SelectItem> getAnforderungenAsSelectItems() {
		List<SelectItem> selectItems = new ArrayList<>();
		if (liste != null) {
			for (Anforderung anforderung : liste) {
				selectItems.add(new SelectItem(anforderung.getID(), anforderung.getAnforderungstitel()));
			}
		}
		return selectItems;
	}
}