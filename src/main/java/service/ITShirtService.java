package service;

import model.TShirt;

import java.util.List;

import dto.SearchCriteria;

public interface ITShirtService {
    List<TShirt> searchAndSortTShirts(SearchCriteria criteria);

	void addTShirt(TShirt tShirt);

	List<TShirt> searchTShirts(String searchTerm);
}