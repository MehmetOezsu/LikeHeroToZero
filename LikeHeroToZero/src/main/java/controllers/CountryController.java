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

    @Inject
    private CountryBean country;
    @Inject
    private CountryService countryService;
    @Inject
    private CountryDialogBean dialogCountry;
    private List<Country> countries = new ArrayList<>();

    @PostConstruct
    public void init() {
        countries = countryService.findAll();
        Collections.sort(countries);
        Country firstCountry = countries.get(0);
        country.setCode(firstCountry.getCode());
        country.setName(firstCountry.getName());
        country.setId(firstCountry.getId());
    }

    public List<Country> getCountries() {
        return countries;
    }

    public void add() {
        boolean hasCountry = countries.stream()
                .anyMatch(c -> c.getName().equals(dialogCountry.getName()) || c.getCode().equals(dialogCountry.getCode()));
        if (hasCountry) return;
        Country newCountry = countryService.add(dialogCountry);
        countries.add(newCountry);
        Collections.sort(countries);
    }

    public void remove() throws IOException {
        countryService.removeById(country.getId());
        countries.remove(country);
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext context = facesContext.getExternalContext();
        context.redirect("dashboard.xhtml");
    }
}
