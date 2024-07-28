package services;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import models.Credential;

@ApplicationScoped
public class CredentialService {

	private static final JPAService jpaService = JPAService.getInstance();

	public Credential findByUsername(String username) {
		return jpaService.runInTransaction(em -> {
			CriteriaBuilder cb = em.getCriteriaBuilder();
			CriteriaQuery<Credential> cr = cb.createQuery(Credential.class);
			Root<Credential> root = cr.from(Credential.class);
			cr.select(root);
			cr.where(cb.equal(root.get("username"), username));
			return em.createQuery(cr).getSingleResult();
		});
	}

}
