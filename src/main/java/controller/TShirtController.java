package controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import dto.SearchCriteria;
import model.TShirt;
import model.TShirt.Gender;
import model.TShirt.Size;
import model.enums.OutputPreference;
import model.valueObjects.Range;
import service.ITShirtService;

import java.math.BigDecimal;
import java.util.List;

@Controller
@RequestMapping("/tshirts")
public class TShirtController {

	private final ITShirtService tShirtService;

	@Autowired
	public TShirtController(ITShirtService tShirtService) {
		this.tShirtService = tShirtService;
	}

	
	@GetMapping("/add")
	public String showAddForm(Model model) {
		
		model.addAttribute("tShirt", new TShirt());
	    return "add-tshirt";
	}
	
	
	@PostMapping("/add")
	public String addTShirt(@ModelAttribute TShirt tShirt, Model model) {

		try {

			tShirtService.addTShirt(tShirt);
			model.addAttribute("success", "T-Shirt added successfully!");
			return "home";
		} catch (Exception e) {
			model.addAttribute("error", e.getMessage());
			return "add-tshirt"; //
		}
	}

	@GetMapping("/search")
	public String showSearchForm(Model model) {

		SearchCriteria criteria = new SearchCriteria();
		criteria.setPriceRange(new Range<BigDecimal>()); // Initialize to avoid NullValueInNestedPathException
		model.addAttribute("criteria", criteria);
		return "search-tshirts";
	}

	@PostMapping("/search")
	public String searchTShirts(@ModelAttribute SearchCriteria criteria, Model model) {

		
		if (criteria == null) {
			
			model.addAttribute("error", "Search criteria cannot be null");
			return "search-tshirts";
		}
		
		try {
	        // For min value
	        String minValue = criteria.getPriceRangeMinStr(); // You need to add this field
	        if (minValue != null && !minValue.trim().isEmpty()) {
	            criteria.getPriceRange().setMin(new BigDecimal(minValue.trim()));
	        }
	        
	        // For max value
	        String maxValue = criteria.getPriceRangeMaxStr(); // You need to add this field 
	        if (maxValue != null && !maxValue.trim().isEmpty()) {
	            criteria.getPriceRange().setMax(new BigDecimal(maxValue.trim()));
	        }
	    } catch (NumberFormatException e) {
	        model.addAttribute("error", "Invalid price range values. Please enter valid numbers.");
	        return "search-tshirts";
	    }
		
		try {
			
			List<TShirt> results = tShirtService.searchAndSortTShirts(criteria);
			model.addAttribute("results", results);
			model.addAttribute("sortBy", criteria.getOutputPreference());
		} catch (Exception e) {
			
			model.addAttribute("error", "Search failed: " + e.getMessage());
		}
		return "search-results";
	}

	@ModelAttribute("sizes")
	public Size[] populateSizes() {
		return Size.values();
	}

	@ModelAttribute("genders")
	public Gender[] populateGenders() {
		return Gender.values();
	}

	@ModelAttribute("sortOptions")
	public OutputPreference[] populateSortOptions() {
		return OutputPreference.values();
	}
}