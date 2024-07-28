package controllers;

import java.io.IOException;
import java.io.Serializable;

import beans.CredentialBean;
import jakarta.enterprise.context.SessionScoped;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpSession;
import models.Credential;
import services.CredentialService;
import types.Role;

@Named
@SessionScoped
public class CredentialController implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject CredentialBean credential;
	private @Inject CredentialService credentialService;

	public CredentialController() {
	}

	public boolean isLoggedIn() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		String username = (session != null) ? (String) session.getAttribute("username") : null;
		return username != null;
	}

	public void logout() throws IOException {
		credential = null;
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpSession session = (HttpSession) context.getSession(false);
		session.invalidate();
		context.redirect("index.xhtml");
	}

	public void login() throws IOException {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		Credential user = credentialService.findByUsername(credential.getUsername());
		if (user.getUsername() != null && user.getPassword() != null
				&& user.getUsername().equals(credential.getUsername())
				&& user.getPassword().equals(credential.getPassword())) {
			HttpSession session = (HttpSession) context.getSession(true);
			session.setMaxInactiveInterval(300);
			session.setAttribute("username", user.getUsername());
			credential.setId(user.getId());
			credential.setRole(user.getRole());
			context.redirect("dashboard.xhtml");
		} else {
			context.redirect("login.xhtml");
		}

	}
	
	public boolean isPublisher() {
		return this.credential.getRole() == Role.Publisher;
	}
}
