package controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import beans.CountryBean;
import beans.CountryDialogBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import models.Country;
import services.CountryService;

@Named
@ViewScoped
public class CountryController implements Serializable {

	private static final long serialVersionUID = 1L;

	private @Inject CountryBean country;
	private @Inject CountryService countryService;
	private @Inject CountryDialogBean dialogCountry;
	private List<Country> countries = new ArrayList<Country>();

	public CountryController() {
	}

	@PostConstruct
	public void init() {
		countries = countryService.findAll();
		Collections.sort(countries);
		country.setCode(countries.get(0).getCode());
		country.setName(countries.get(0).getName());
		country.setId(countries.get(0).getId());
	}

	public List<Country> getCountries() {
		return countries;
	}

	public void add() {
		boolean hasCountry = countries.stream().anyMatch(c -> c.getName().equals(this.dialogCountry.getName())
				|| c.getCode().equals(this.dialogCountry.getCode()));
		if (hasCountry)
			return;
		Country country = countryService.add(this.dialogCountry);
		countries.add(country);
		Collections.sort(countries);

	}

	public void remove() throws IOException {
		countryService.removeById(this.country.getId());
		countries.remove(this.country);
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		context.redirect("dashboard.xhtml");
	}

}
