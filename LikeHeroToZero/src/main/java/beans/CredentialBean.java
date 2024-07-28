package beans;

import java.io.Serializable;

import jakarta.enterprise.context.SessionScoped;
import jakarta.inject.Named;
import models.Credential;

@Named("credential")
@SessionScoped
public class CredentialBean extends Credential implements Serializable {

	private static final long serialVersionUID = 1L;
}