//package tesController;
//
//
//import model.enums.OutputPreference;
//
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.CsvSource;
//
//import base.BaseIntegrationTest;
//
//import static org.hamcrest.Matchers.*;
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//public class TShirtControllerMvcTest extends BaseIntegrationTest {
//
//	
//	@ParameterizedTest(name = "Invalid price range: minPrice={0}, maxPrice={1}")
//	@CsvSource({
//	    "abc, 100",   // Non-numeric minPrice
//	    "100, xyz",   // Non-numeric maxPrice
//	    "-10, 100",   // Negative minPrice
//	    "100, -20",   // Negative maxPrice
//	    "200, 100"    // minPrice > maxPrice
//	})
//	void searchTShirts_withInvalidPriceRange_shouldReturnError(String minPrice, String maxPrice) throws Exception {
//	    mockMvc.perform(post("/tshirts/search")
//	            .param("priceRangeMinStr", minPrice)
//	            .param("priceRangeMaxStr", maxPrice))
//	        .andExpect(status().isOk())
//	        .andExpect(view().name("search-tshirts"))
//	        .andExpect(model().attributeExists("error"))
//	        .andExpect(model().attribute("error", containsString("Invalid price range values")));
//	}
//	
//	@ParameterizedTest(name = "Sorting by {0}")
//	@CsvSource({
//	    "PRICE",
//	    "RATING"
//	})
//	void searchTShirts_withSorting_shouldReturnSortedResults(String sortBy) throws Exception {
//	    mockMvc.perform(post("/tshirts/search")
//	            .param("outputPreference", sortBy))
//	        .andExpect(status().isOk())
//	        .andExpect(view().name("search-results"))
//	        .andExpect(model().attribute("sortBy", OutputPreference.valueOf(sortBy)));
//	}
//	
//    @ParameterizedTest(name = "Search by color={0}, size={1}, gender={2}, minPrice{3}, maxPrice{4}, sort={5}")
//    @CsvSource({
//        "black, M, M, , , PRICE",  
//        "black, M, F, 15.00, 3000.00, PRICE",
//        " , , U, , , RATING",
//        "yellow, , M, 10.00, 2000.00, PRICE",
//        "white, S, M, , , PRICE"
//    })
//    void searchTShirts_withVariousFilters_shouldReturnResults(String color, String size, String gender, String minRange, String maxRange, String sort) throws Exception {
//        mockMvc.perform(post("/tshirts/search")
//                .param("colour", color)
//                .param("size", size)
//                .param("gender", gender)
//                .param("minPrice", minRange)
//                .param("maxPrice", maxRange)
//                .param("outputPreference", sort))
//            .andExpect(status().isOk())
//            .andExpect(view().name("search-results"))
//            .andExpect(model().attributeExists("results"))
//            .andExpect(model().attribute("sortBy", OutputPreference.valueOf(sort)));
//    }
//    
//    @Test
//    void searchTShirts_withNoMatchingCriteria_shouldReturnNoResults() throws Exception {
//        mockMvc.perform(post("/tshirts/search")
//                .param("colour", "nonexistentColor"))
//            .andExpect(status().isOk())
//            .andExpect(view().name("search-results"))
//            .andExpect(model().attributeExists("results"))
//            .andExpect(model().attribute("results", hasSize(0)))
//            .andExpect(content().string(containsString("No T-Shirts found matching your criteria.")));
//    }
//}
//
//
