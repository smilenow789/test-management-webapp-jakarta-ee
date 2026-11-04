package util;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import model.Benutzer;

//Verwaltet die Session-Daten des angemeldeten Benutzers
//(Lebensdauer: solange die Benutzersession aktiv ist)
@Named
@SessionScoped
public class UserSessionBean implements Serializable {

	private Benutzer authenticatedUser; // Der aktuell authentifizierte Benutzer

	public UserSessionBean() {
	}

	// Gibt den authentifizierten Benutzer zurück
	public Benutzer getAuthenticatedUser() {
		return authenticatedUser;
	}

	// Setzt den authentifizierten Benutzer
	public void setAuthenticatedUser(Benutzer authenticatedUser) {
		this.authenticatedUser = authenticatedUser;
	}

	// Gibt den Namen des authentifizierten Benutzers zurück
	public String getAuthenticatedUsername() {
		if (authenticatedUser != null) {
			return authenticatedUser.getName();
		} else {
			return null;
		}
	}

	// Gibt die Rolle des authentifizierten Benutzers zurück
	public String getAuthenticatedUserRole() {
		if (authenticatedUser != null) {
			return authenticatedUser.getRolle();
		} else {
			return null;
		}

	}

	// Loggt den Benutzer aus und leitet zur Login-Seite um
	// (aufgerufen über einen "Logout"-Button in der UI)
	public String logout() {
		authenticatedUser = null;
		return "/login.xhtml?faces-redirect=true";
	}
}
