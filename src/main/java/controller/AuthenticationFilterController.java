package controller;

import java.io.IOException;
import jakarta.inject.Inject;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import util.UserSessionBean;

//Filter zur Authentifizierung von Benutzern
//(wird auf bestimmte Seiten angewendet, um unautorisierten Zugriff zu verhindern)
@WebFilter(filterName = "AuthenticationFilterController", urlPatterns = { "/requirementsengineer.xhtml",
		"/testfallersteller.xhtml", "/testmanager.xhtml", "/tester.xhtml" })
public class AuthenticationFilterController implements Filter {

	@Inject
	private UserSessionBean userSessionBean;

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		String requestURI = httpRequest.getRequestURI();
		String contextPath = httpRequest.getContextPath();

		// Prüft, ob ein Benutzer angemeldet ist
		boolean isLoggedIn = (userSessionBean != null && userSessionBean.getAuthenticatedUser() != null);

		// Prüft, ob die angefragte Seite die Login-Seite ist
		boolean isLoginPage = requestURI.contains("/login.xhtml");

		// Prüft, ob es sich um eine Ressource (z.B. CSS) handelt
		boolean isResource = requestURI.startsWith(contextPath + "/jakarta.faces.resource/")
				|| requestURI.startsWith(contextPath + "/resources/");

		// Wenn nicht angemeldet, nicht auf der Login-Seite und keine Ressource,
		// dann Weiterleitung zur Login-Seite
		// Ansonsten die Anfrage normal fortsetzen
		if (!isLoggedIn && !isLoginPage && !isResource) {
			httpResponse.sendRedirect(contextPath + "/login.xhtml");
		} else {
			chain.doFilter(request, response);
		}
	}
}