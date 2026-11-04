package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

//Entität für eine Anforderung in der Datenbank
@Entity
public class Anforderung implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ID;
	private String anforderungstitel;
	private String beschreibung;

	// Zugehörige Testfälle (One-to-Many-Beziehung)
	@OneToMany(mappedBy = "zuErfuellendeAnforderung", cascade = CascadeType.ALL, orphanRemoval = true)
	private Set<Testfall> zugehoerigeTestfaelle = new HashSet<>();

	public Anforderung() {
	}

	public Anforderung(String anforderungstitel, String beschreibung) {
		this.anforderungstitel = anforderungstitel;
		this.beschreibung = beschreibung;
	}

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getAnforderungstitel() {
		return anforderungstitel;
	}

	public void setAnforderungstitel(String anforderungstitel) {
		this.anforderungstitel = anforderungstitel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Set<Testfall> getZugehoerigeTestfaelle() {
		return zugehoerigeTestfaelle;
	}

	public void setZugehoerigeTestfaelle(Set<Testfall> zugehoerigeTestfaelle) {
		this.zugehoerigeTestfaelle = zugehoerigeTestfaelle;
	}

	// Fügt einen Testfall zur Anforderung hinzu
	public void addTestfall(Testfall testfall) {
		this.zugehoerigeTestfaelle.add(testfall);
		testfall.setZuErfuellendeAnforderung(this);
	}

}