package services;

import java.util.List;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Country;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class CountryService {

	private static final JPAService jpaService = JPAService.getInstance();

	public List<Country> findAll() {
		return jpaService.runInTransaction(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Country> cq = cb.createQuery(Country.class);
			Root<Country> root = cq.from(Country.class);
			cq.select(root);
			List<Country> countries = em.createQuery(cq).getResultList();
			return countries;
		});
	}

	public Country add(Country country) {
		return jpaService.runInTransaction(em -> {
			Country storedCountry = new Country(country.getName(), country.getCode());
			em.persist(storedCountry);
			return storedCountry;
		});
	}

	public void removeById(Integer countryId) {
		jpaService.runInTransaction(em -> {
			Country country = em.find(Country.class, countryId);
			em.remove(country);
			return null;
		});
	}
}