package repository;

import model.TShirt;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import dto.SearchCriteria;
import exception.PersistenceException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@Repository
@Transactional
public class TShirtRepositoryImpl implements IRepository {

	@Autowired 
	private final SessionFactory sessionFactory;
	
	
    public TShirtRepositoryImpl(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory; 
    }

	@Override
	@Transactional(readOnly = true)
    public List<TShirt> search(SearchCriteria criteria) {
    	
        try (Session session = sessionFactory.openSession()) {
           
        	String hql = buildHqlQuery(criteria);
            Query<TShirt> query = session.createQuery(hql, TShirt.class);
            // Bind parameters
            if (criteria.getColour() != null && !criteria.getColour().isEmpty()) 
                query.setParameter("colour", criteria.getColour());
            if (criteria.getSize() != null) 
                query.setParameter("size", criteria.getSize());
            if (criteria.getGender() != null) 
                query.setParameter("gender", criteria.getGender().name());
            if (criteria.getPriceRange() != null) {
            	
            	if (criteria.getPriceRange().getMin() != null && criteria.getPriceRange().getMax() != null) {
                    query.setParameter("minPrice", criteria.getPriceRange().getMin());
                    query.setParameter("maxPrice", criteria.getPriceRange().getMax());
                }
            	else if (criteria.getPriceRange().getMin() != null && criteria.getPriceRange().getMax() == null) {
					query.setParameter("minPrice", criteria.getPriceRange().getMin());
				}
				else if (criteria.getPriceRange().getMin() == null && criteria.getPriceRange().getMax() != null) {
					query.setParameter("maxPrice", criteria.getPriceRange().getMax());
                }
            }

            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error searching T-Shirts", e);
        }
    }

    private String buildHqlQuery(SearchCriteria criteria) {
        
    	StringBuilder hql = new StringBuilder("SELECT t FROM TShirt t WHERE 1=1");
        // Add filters
        if (criteria.getColour() != null && !criteria.getColour().isEmpty()) 
        	hql.append(" AND LOWER(t.colour) = LOWER(:colour)");
        if (criteria.getSize() != null) 
            hql.append(" AND t.size = :size");
        if (criteria.getGender() != null) 
            hql.append(" AND LOWER(t.genderRecommendation) = LOWER(:gender)");
        if (criteria.getPriceRange() != null) {
            if (criteria.getPriceRange().getMin() != null && criteria.getPriceRange().getMax() != null 
            		&& criteria.getPriceRange().getMin() != criteria.getPriceRange().getMax()
            		&& !criteria.getPriceRange().getMin().equals("")
            		&& !criteria.getPriceRange().getMax().equals("")) {
                hql.append(" AND t.price BETWEEN :minPrice AND :maxPrice");
            }
			else if (criteria.getPriceRange().getMin() != null && criteria.getPriceRange().getMax() == null) {
				hql.append(" AND t.price >= :minPrice");
			} else if (criteria.getPriceRange().getMin() == null && criteria.getPriceRange().getMax() != null) {
				hql.append(" AND t.price <= :maxPrice");
			}
        }
        // Add sorting
        if (criteria.getOutputPreference() != null) {
            switch (criteria.getOutputPreference()) {
                case PRICE:
                    hql.append(" ORDER BY t.price ASC");
                    break;
                case RATING:
                    hql.append(" ORDER BY t.rating DESC");
                    break;
                case BOTH:
                    hql.append(" ORDER BY t.price ASC, t.rating DESC");
                    break;
                case RELEVANCE:
                    hql.append(" ORDER BY t.lastAccessed DESC");
                    break;
                default:
                    throw new IllegalArgumentException("Invalid output preference: " + criteria.getOutputPreference());
            }
        }
        return hql.toString();
    }


	/**
	 * Update the last accessed time of a T-Shirt in the database, called when a T-Shirt is viewed
	 * 
	 * @param id the id of the T-Shirt
	 */
    @Override
    public void updateLastAccessed(Long id) {
    	
        try (Session session = sessionFactory.openSession()) {
            
        	session.beginTransaction();
            TShirt tShirt = session.get(TShirt.class, id);
            if (tShirt != null) {
                tShirt.setLastAccessed(LocalDateTime.now());
                session.merge(tShirt);
            }
			else {
				throw new IllegalArgumentException("T-Shirt with ID " + id + " not found");
			}
            session.getTransaction().commit(); // Commit the transaction, i.e. persist the changes to the database
        } catch (Exception e) {
            throw new RuntimeException("Error updating last accessed time", e);
        }
    }

    @Override
    public void save(TShirt tShirt) {
        try (Session session = sessionFactory.openSession()) {
        	// Check for duplicates
            if (findDuplicate(tShirt).isPresent()) {
                return; // Do not save if a duplicate exists
            }

            Transaction transaction = session.beginTransaction();
            try {
                if (tShirt.getId() == null) {
                    session.persist(tShirt); //CEATES A NEW T-SHIRT
                } else {
                    session.merge(tShirt); //UPDATES AN EXISTING T-SHIRT
                }
                transaction.commit(); //COMMIT THE TRANSACTION, I.E. PERSIST THE CHANGES TO THE DATABASE
            } catch (Exception e) {
            	if (transaction.isActive() && transaction != null)
            		transaction.rollback();
                throw new PersistenceException("Failed to save T-Shirt", e);
            }
        }
    }

	@Override
	public List<TShirt> search(String searchTerm) {
		    
        try (Session session = sessionFactory.openSession()) {
            
            String hql = "FROM TShirt t WHERE t.colour LIKE :searchTerm OR t.size LIKE :searchTerm OR t.genderRecommendation LIKE :searchTerm";
            Query<TShirt> query = session.createQuery(hql, TShirt.class);
            query.setParameter("searchTerm", "%" + searchTerm + "%");
            return query.getResultList();
        } catch (Exception e) {
            throw new RuntimeException("Error searching T-Shirts", e);
        }
	}
	
	@Override
	public Optional<TShirt> findDuplicate(TShirt tShirt) {
        try (Session session = sessionFactory.openSession()) {
            String hql = "FROM TShirt t WHERE t.name = :name " +
                        "AND t.colour = :colour " +
                        "AND t.size = :size " +
                        "AND t.genderRecommendation = :gender " +
                        "AND t.id != :id";

            return Optional.ofNullable(session.createQuery(hql, TShirt.class)
                .setParameter("name", tShirt.getName())
                .setParameter("colour", tShirt.getColour())
                .setParameter("size", tShirt.getSize())
                .setParameter("gender", tShirt.getGenderRecommendation())
                .setParameter("id", tShirt.getId() != null ? tShirt.getId() : -1L)
                .uniqueResult());
        }
    }
}