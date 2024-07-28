package beans;

import java.io.Serializable;

import jakarta.faces.view.ViewScoped;
import jakarta.inject.Named;
import models.Country;

@Named("country")
@ViewScoped
public class CountryBean extends Country implements Serializable {

	private static final long serialVersionUID = 1L;
}