package model;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinColumn;

// Entität für einen Testlauf in der Datenbank
@Entity
public class Testlauf implements Serializable {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int ID;
	private String testlaufTitel;
	private String beschreibung;

	// Testfälle, die zu diesem Testlauf gehören
	@ManyToMany(cascade = { CascadeType.PERSIST, CascadeType.MERGE })
	@JoinTable(name = "testlauf_testfall", joinColumns = @JoinColumn(name = "testlauf_id"), inverseJoinColumns = @JoinColumn(name = "testfall_id"))
	private Set<Testfall> ausgewaehlteTestfaelle = new HashSet<>();

	public String getTestlaufTitel() {
		return testlaufTitel;
	}

	public void setTestlaufTitel(String testlaufTitel) {
		this.testlaufTitel = testlaufTitel;
	}

	public String getBeschreibung() {
		return beschreibung;
	}

	public void setBeschreibung(String beschreibung) {
		this.beschreibung = beschreibung;
	}

	public Set<Testfall> getAusgewaehlteTestfaelle() {
		return ausgewaehlteTestfaelle;
	}

	public void setAusgewaehlteTestfaelle(Set<Testfall> ausgewaehlteTestfaelle) {
		this.ausgewaehlteTestfaelle = ausgewaehlteTestfaelle;
	}

	public Testlauf() {
	}

	public Testlauf(String testlaufTitel, String beschreibung, Set<Testfall> ausgewaehlteTestfaelle) {
		this.testlaufTitel = testlaufTitel;
		this.beschreibung = beschreibung;
		this.ausgewaehlteTestfaelle = ausgewaehlteTestfaelle;
	}

}
