package services;

import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.NoResultException;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import models.Country;
import models.Emission;

@ApplicationScoped
public class EmissionService {
	private static final JPAService jpaService = JPAService.getInstance();

	public List<Emission> findAllByCountry(Country _country, boolean withDraft) {
		return jpaService.runInTransaction(em -> {
			Country country = em.find(Country.class, _country.getId());
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Emission> cr = cb.createQuery(Emission.class);
			Root<Emission> root = cr.from(Emission.class);
			Predicate pred;
			if (withDraft) {
				pred = cb.equal(root.get("country"), country);
			} else {
				pred = cb.and(cb.equal(root.get("country"), country), cb.equal(root.get("draft"), false));
			}
			cr.select(root).where(pred).orderBy(cb.asc(root.get("year")));
			List<Emission> result = em.createQuery(cr).getResultList();
			return result;
		});
	}

	public Emission findByYear(Emission emission) {
		return jpaService.runInTransaction(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Emission> cr = cb.createQuery(Emission.class);
			Root<Emission> root = cr.from(Emission.class);
			cr.select(root).where(cb.and(cb.equal(root.get("year"), emission.getYear()),
					cb.equal(root.get("draft"), false), cb.equal(root.get("country"), emission.getCountry())));
			try {

				Emission result = em.createQuery(cr).getSingleResult();
				return result;
			} catch (NoResultException e) {
				return null;
			}
		});
	}

	public Emission add(Emission emission) {
		return jpaService.runInTransaction(em -> {
			Country storedCountry = em.find(Country.class, emission.getCountry().getId());
			Emission newEmission = new Emission(emission.getYear(), emission.getAmount(), true, storedCountry);
			em.persist(newEmission);
			return newEmission;
		});
	}

	public boolean update(Emission emission) {
		return jpaService.runInTransaction(em -> {
			Emission updateEmission = this.findByYear(emission);
			if (updateEmission != null) {
				updateEmission.setAmount(emission.getAmount());
				updateEmission.setDraft(emission.isDraft());
				em.merge(updateEmission);
				return true;
			} else {
				em.merge(emission);
				return false;
			}
		});
	}

	public void removeById(Integer emissionId) {
		jpaService.runInTransaction(em -> {
			Emission emission = em.find(Emission.class, emissionId);
			em.remove(emission);
			return null;
		});
	}

}