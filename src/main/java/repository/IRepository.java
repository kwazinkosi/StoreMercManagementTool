package repository;

import model.TShirt;

import java.util.List;
import java.util.Optional;

import dto.SearchCriteria;

public interface IRepository {
    
	List<TShirt> search(SearchCriteria criteria);
    void updateLastAccessed(Long id);
    void save(TShirt tShirt);
	List<TShirt> search(String searchTerm);
	Optional<TShirt> findDuplicate(TShirt tShirt);
}