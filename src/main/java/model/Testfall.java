package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;

// Entität für einen Testfall in der Datenbank
@Entity
public class Testfall implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ID;
	private String testfallTitel;
	private String beschreibung;
	private String ergebnis = "noch nicht ausgeführt"; // Ergebnis des Testfalls (Standard: "noch nicht ausgeführt")

	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "testfall_benutzer", joinColumns = @JoinColumn(name = "testfall_id"), inverseJoinColumns = @JoinColumn(name = "benutzer_id"))
	private Set<Benutzer> zugewieseneBenutzer = new HashSet<>();// Benutzer, denen dieser Testfall zugewiesen ist

	// Die Anforderung, die dieser Testfall erfüllt (Many-to-One-Beziehung)
	@ManyToOne
	@JoinColumn(name = "anforderung_id", nullable = true)
	private Anforderung zuErfuellendeAnforderung;

	// Testläufe, in denen dieser Testfall enthalten ist
	@ManyToMany(mappedBy = "ausgewaehlteTestfaelle")
	private Set<Testlauf> zugehoerigeTestlaeufe = new HashSet<>();

	public int getID() {
		return ID;
	}

	public void setID(int iD) {
		ID = iD;
	}

	public String getTestfallTitel() {
		return testfallTitel;
	}

	public void setTestfallTitel(String testfallTitel) {
		this.testfallTitel = testfallTitel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public String getErgebnis() {
		return ergebnis;
	}

	public void setErgebnis(String ergebnis) {
		this.ergebnis = ergebnis;
	}

	public Set<Benutzer> getZugewieseneBenutzer() {
		return zugewieseneBenutzer;
	}

	public void setZugewieseneBenutzer(Set<Benutzer> zugewieseneBenutzer) {
		this.zugewieseneBenutzer = zugewieseneBenutzer;
	}

	public Anforderung getZuErfuellendeAnforderung() {
		return zuErfuellendeAnforderung;
	}

	public void setZuErfuellendeAnforderung(Anforderung zuErfuellendeAnforderung) {
		this.zuErfuellendeAnforderung = zuErfuellendeAnforderung;
	}

	public Set<Testlauf> getZugehoerigeTestlaeufe() {
		return zugehoerigeTestlaeufe;
	}

	public void setZugehoerigeTestlaeufe(Set<Testlauf> zugehoerigeTestlaeufe) {
		this.zugehoerigeTestlaeufe = zugehoerigeTestlaeufe;
	}

	public Testfall() {
	}

	public Testfall(Anforderung zuErfuellendeAnforderung, String testfallTitel, String beschreibung) {
		this.zuErfuellendeAnforderung = zuErfuellendeAnforderung;
		this.testfallTitel = testfallTitel;
		this.beschreibung = beschreibung;
	}

	// Fügt einen zugewiesenen Benutzer zum Testfall hinzu
	public void addZugewiesenerBenutzer(Benutzer benutzer) {
		this.zugewieseneBenutzer.add(benutzer);
		benutzer.getZugewieseneTestfaelle().add(this);
	}

}
