package service;

import model.TShirt;
import repository.IRepository;
import exception.DuplicateTShirtException;
import exception.TShirtServiceException;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dto.SearchCriteria;

@Service
public class TShirtServiceImpl implements ITShirtService {
    
    private final IRepository tShirtRepository;
    
    @Autowired
    public TShirtServiceImpl(IRepository tShirtRepository) {
        this.tShirtRepository = tShirtRepository;
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TShirt> searchAndSortTShirts(SearchCriteria criteria) {
        if (criteria == null) {
            throw new IllegalArgumentException("Search criteria cannot be null");
        }
        
        try {
            List<TShirt> results = tShirtRepository.search(criteria);
            
            // Update relevance (LRU simulation)
            results.forEach(tShirt -> tShirtRepository.updateLastAccessed(tShirt.getId()));
            
            return results;
        } catch (Exception e) {
            throw new TShirtServiceException("Error searching and sorting T-Shirts", e);
        }
    }
    
    @Override
    @Transactional
    public void addTShirt(TShirt tShirt) {
        try {
            validateTShirt(tShirt);
            tShirtRepository.save(tShirt);
        } catch (DuplicateTShirtException e) {
            // Re-throw the exception
            throw e;
        } catch (Exception e) {
            throw new TShirtServiceException("Error adding T-Shirt", e);
        }
    }
    
    @Override
    @Transactional(readOnly = true)
    public List<TShirt> searchTShirts(String searchTerm) {
        try {
            return tShirtRepository.search(searchTerm);
        } catch (Exception e) {
            throw new TShirtServiceException("Error searching T-Shirts", e);
        }
    }
    
    private void validateTShirt(TShirt tShirt) {
        if (tShirt == null) {
            throw new IllegalArgumentException("T-Shirt cannot be null");
        }
        
        if (tShirtRepository.findDuplicate(tShirt).isPresent()) {
            throw new DuplicateTShirtException("Duplicate T-Shirt found with same properties");
        }
    }
}