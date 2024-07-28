package converters;

import jakarta.faces.component.UIComponent;
import jakarta.faces.context.FacesContext;
import jakarta.faces.convert.Converter;
import jakarta.faces.convert.FacesConverter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Country;
import services.JPAService;

@FacesConverter(value = "countryConverter")
public class CountryConverter implements Converter<Country> {

	private static final JPAService jpaService = JPAService.getInstance();

	@Override
	public Country getAsObject(FacesContext ctx, UIComponent component, String countryCode) {
		Country country = jpaService.runInTransaction(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Country> cr = cb.createQuery(Country.class);
			Root<Country> root = cr.from(Country.class);
			cr.select(root).where(cb.equal(root.get("code"), countryCode));
			return em.createQuery(cr).getSingleResult();
		});
		return country;
	}

	@Override
	public String getAsString(FacesContext context, UIComponent component, Country country) {
		return country != null ? country.getCode() : "";
	}

}
