package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.primefaces.model.charts.ChartData;
import org.primefaces.model.charts.line.LineChartDataSet;
import org.primefaces.model.charts.line.LineChartModel;
import org.primefaces.model.charts.line.LineChartOptions;
import org.primefaces.model.charts.optionconfig.title.Title;

import beans.CountryBean;
import beans.CredentialBean;
import beans.EmissionBean;
import jakarta.annotation.PostConstruct;
import jakarta.faces.context.ExternalContext;
import jakarta.faces.context.FacesContext;
import jakarta.faces.view.ViewScoped;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import jakarta.servlet.http.HttpServletRequest;
import models.Emission;
import services.EmissionService;
import types.Role;

@Named
@ViewScoped
public class EmissionController implements Serializable {

	private static final long serialVersionUID = 1L;
	private @Inject CountryBean country;
	private @Inject CredentialBean user;
	private @Inject EmissionBean emission;
	private @Inject EmissionService emissionService;
	private List<Emission> emissions = new ArrayList<Emission>();
	private LineChartModel model = new LineChartModel();

	public EmissionController() {
	}

	@PostConstruct
	public void init() {
		this.setEmissions();
		this.setEmissionModel();
	}

	public LineChartModel setEmissionModel() {
		model = new LineChartModel();
		
		ChartData data = new ChartData();

		Title title = new Title();
		title.setDisplay(true);
		title.setText("Emission");
		
		LineChartOptions options = new LineChartOptions();
		options.setTitle(title);
		model.setOptions(options);

		if (country.getCode() == null || country.getId() == null) {
			return model;
		}
		List<Emission> emissions = emissionService.findAllByCountry(country, false);
		List<String> years = emissions.stream().map(e -> String.valueOf(e.getYear())).collect(Collectors.toList());
		List<Object> amounts = emissions.stream().map(e -> e.getAmount()).collect(Collectors.toList());
		
		LineChartDataSet dataSet = new LineChartDataSet();
		dataSet.setData(amounts);
		dataSet.setFill(false);
		dataSet.setLabel("CO₂ in kt");
		dataSet.setBorderColor("rgb(75, 192, 192)");
		dataSet.setTension(0.1);
		data.addChartDataSet(dataSet);
		data.setLabels(years);
		model.setData(data);
		return model;
	}

	public LineChartModel getEmissionModel() {
		return model;
	}

	public List<Emission> getEmissions() {
		return emissions;
	}

	public void setEmissions() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ExternalContext context = facesContext.getExternalContext();
		HttpServletRequest request = (HttpServletRequest) context.getRequest();
		String path = request.getRequestURI();
		boolean draft = false;
		if (path.contains("dashboard") && this.user != null && this.user.getRole() == Role.Publisher) {
			draft = true;
		}
		if (country.getCode() != null && country.getId() != null) {
			emissions.clear();
			emissions.addAll(emissionService.findAllByCountry(country, draft));
		}
	}

	public void add() {
		if (this.emission.getAmount() <= 0.0 || this.emission.getYear() <= 0)
			return;
		this.emission.setCountry(this.country);
		Emission newEmission = emissionService.add(this.emission);
		this.emissions.add(newEmission);
		Collections.sort(this.emissions);
	}

	public void approve(Emission emission) {
		emission.setDraft(false);
		boolean updated = emissionService.update(emission);
		if (updated) {
			emissionService.removeById(emission.getId());
			this.emissions.remove(emission);
		}
		this.setEmissions();
	}

	public void remove(Emission emission) {
		emissionService.removeById(emission.getId());
		emissions.remove(emission);
	}

}
