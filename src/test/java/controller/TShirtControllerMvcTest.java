import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

@ExtendWith(MockitoExtension.class)
class TShirtControllerTest {

    @Mock
    private TShirtService tShirtService;
    
    @InjectMocks
    private TShirtController controller;
    
    private MockMvc mockMvc;
    
    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
    }
    
    @Test
    @DisplayName("Controller returns error view when criteria is null")
    void searchTShirts_withNullCriteria_returnsErrorView() {
        // Arrange
        Model model = new ConcurrentModel();
        
        // Act
        String viewName = controller.searchTShirts(null, model);
        
        // Assert
        assertEquals("search-tshirts", viewName);
        assertTrue(model.containsAttribute("error"));
    }

    @Test
    @DisplayName("Controller returns error view when price format is invalid")
    void searchTShirts_withInvalidPriceFormat_returnsErrorView() {
        // Arrange
        SearchCriteria criteria = new SearchCriteria();
        criteria.setPriceRangeMinStr("abc"); // Invalid number
        Model model = new ConcurrentModel();
        
        // Act
        String viewName = controller.searchTShirts(criteria, model);
        
        // Assert
        assertEquals("search-tshirts", viewName);
        assertTrue(model.containsAttribute("error"));
        assertTrue(model.getAttribute("error").toString().contains("Invalid price"));
    }
    
    @Test
    @DisplayName("Controller adds error to model when service throws exception")
    void searchTShirts_withServiceException_addsErrorToModel() {
        // Arrange
        SearchCriteria criteria = new SearchCriteria();
        Model model = new ConcurrentModel();
        when(tShirtService.searchAndSortTShirts(any())).thenThrow(new RuntimeException("Service error"));
        
        // Act
        String viewName = controller.searchTShirts(criteria, model);
        
        // Assert
        assertEquals("search-results", viewName);
        assertTrue(model.containsAttribute("error"));
        assertTrue(model.getAttribute("error").toString().contains("Service error"));
    }
    
    @Test
    @DisplayName("Controller returns expected view and model attributes when search is successful")
    void searchTShirts_withValidCriteria_returnsResultsAndCorrectView() {
        // Arrange
        SearchCriteria criteria = new SearchCriteria();
        criteria.setOutputPreference(OutputPreference.PRICE_ASC);
        
        List<TShirt> mockResults = Arrays.asList(new TShirt(), new TShirt());
        Model model = new ConcurrentModel();
        
        when(tShirtService.searchAndSortTShirts(any())).thenReturn(mockResults);
        
        // Act
        String viewName = controller.searchTShirts(criteria, model);
        
        // Assert
        assertEquals("search-results", viewName);
        assertTrue(model.containsAttribute("results"));
        assertEquals(mockResults, model.getAttribute("results"));
        assertEquals(OutputPreference.PRICE_ASC, model.getAttribute("sortBy"));
    }
    
    @Test
    @DisplayName("MockMvc test for submitting search form with valid parameters")
    void searchTShirts_mockMvc_withValidParameters() throws Exception {
        // Arrange
        List<TShirt> mockResults = Arrays.asList(new TShirt(), new TShirt());
        when(tShirtService.searchAndSortTShirts(any())).thenReturn(mockResults);
        
        // Act & Assert
        mockMvc.perform(post("/tshirts/search")
                .param("colour", "red")
                .param("size", "MEDIUM")
                .param("priceRangeMinStr", "10.00")
                .param("priceRangeMaxStr", "50.00")
                .param("outputPreference", "PRICE_ASC"))
                .andExpect(status().isOk())
                .andExpect(view().name("search-results"))
                .andExpect(model().attributeExists("results"));
    }
    
    @ParameterizedTest
    @CsvSource({
        "red, SMALL, MALE, 10.00, 20.00, PRICE_ASC",  
        "blue, MEDIUM, FEMALE, 15.00, 30.00, PRICE_DESC",
        "green, LARGE, UNISEX, 5.00, 25.00, COLOR"
    })
    @DisplayName("Service correctly processes various search criteria combinations")
    void searchTShirts_service_withVariousCriteria(
            String color, Size size, Gender gender, 
            String minPrice, String maxPrice, 
            OutputPreference outputPref) throws Exception {
        
        // Arrange
        List<TShirt> mockResults = Arrays.asList(new TShirt(), new TShirt());
        when(tShirtService.searchAndSortTShirts(any())).thenReturn(mockResults);
        
        // Act & Assert
        mockMvc.perform(post("/tshirts/search")
                .param("colour", color)
                .param("size", size.toString())
                .param("gender", gender.toString())
                .param("priceRangeMinStr", minPrice)
                .param("priceRangeMaxStr", maxPrice)
                .param("outputPreference", outputPref.toString()))
                .andExpect(status().isOk())
                .andExpect(view().name("search-results"))
                .andExpect(model().attributeExists("results"))
                .andExpect(model().attribute("sortBy", outputPref));
    }
}