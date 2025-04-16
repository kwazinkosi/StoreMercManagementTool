package model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "t_shirts")
public class TShirt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "T-Shirt ID is required")
    @Column(name = "t_shirt_id", nullable = false, unique = true, length = 20)
	private String tShirtId;
	
	
	@NotBlank(message = "Name is required")
    @Column(nullable = false, length = 100)
    private String name;

	@NotBlank(message = "Colour is required")
	@Column(nullable = false, length = 50)
	private String colour;

	@NotNull(message = "Gender is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "gender_recommendation", length = 20)
	private Gender genderRecommendation;

	@NotNull(message = "Size is required")
    @Enumerated(EnumType.STRING)
    @Column(length = 10)
	private Size size;

	@NotNull(message = "Price is required")
    @DecimalMin(value = "0.0", inclusive = false, message = "Price must be positive")
    @Digits(integer = 8, fraction = 2)
    @Column(precision = 10, scale = 2)
	private BigDecimal price;

	@DecimalMin(value = "0.0", message = "Rating must be at least 0")
    @DecimalMax(value = "5.0", message = "Rating can't exceed 5")
	private Double rating;

	@NotNull(message = "Availability is required")
    @Column(nullable = false)
	private Boolean availability;

	@Column(name = "last_accessed")
	private LocalDateTime lastAccessed; // For relevance sorting

	// Enums
	public enum Gender {
		M, F, U // M = Male, F = Female, U = Unisex
	}

	public enum Size {
		XS, S, M, L, XL, XXL
	}

	// Constructors
	public TShirt() {
		// Default no-args constructor required by Hibernate
	}

	public TShirt(String shirtId, String name, String colour, Gender genderRecommendation, Size size, BigDecimal price, Double rating,
			Boolean availability) {
		
		this.tShirtId = shirtId;
		this.name = name;
		this.colour = colour;
		this.genderRecommendation = genderRecommendation;
		this.size = size;
		this.price = price;
		this.rating = rating;
		this.availability = availability;
	}

	public String gettShirtId() {
		return tShirtId;
	}

	public void settShirtId(String tShirtId) {
		this.tShirtId = tShirtId;
	}

	// Getters and Setters
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public Gender getGenderRecommendation() {
		return genderRecommendation;
	}

	public void setGenderRecommendation(Gender genderRecommendation) {
		this.genderRecommendation = genderRecommendation;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Boolean getAvailability() {
		return availability;
	}

	public void setAvailability(Boolean availability) {
		this.availability = availability;
	}

	public LocalDateTime getLastAccessed() {
		return lastAccessed;
	}

	public void setLastAccessed(LocalDateTime lastAccessed) {
		this.lastAccessed = lastAccessed;
	}

	@Override
	public String toString() {
		return "TShirt{" + "id=" + id + ", name='" + name + '\'' + ", colour='" + colour + '\''
				+ ", genderRecommendation=" + genderRecommendation + ", size=" + size + ", price=" + price + ", rating="
				+ rating + ", availability=" + availability + ", lastAccessed=" + lastAccessed + '}';
	}
}