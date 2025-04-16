package dto;

import model.TShirt.Gender;
import model.TShirt.Size;
import model.enums.OutputPreference;
import model.valueObjects.Range;

import java.math.BigDecimal;

public class SearchCriteria {
	private String colour;
	private Size size;
	private Gender gender;
	private Range<BigDecimal> priceRange = new Range<>();;
	private OutputPreference outputPreference;
	private String priceRangeMinStr; // For form binding
    private String priceRangeMaxStr; // For form binding


	// Getters and Setters
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {

		this.colour = colour;
	}

	public Size getSize() {
		return size;
	}

	public void setSize(Size size) {
		this.size = size;
	}

	public Gender getGender() {
		return gender;
	}

	public void setGender(Gender gender) {

		this.gender = gender;
	}

	public Range<BigDecimal> getPriceRange() {
		return priceRange;
	}

	public void setPriceRange(Range<BigDecimal> priceRange) {
		this.priceRange = priceRange;
	}

	public OutputPreference getOutputPreference() {
		return outputPreference;
	}

	public void setOutputPreference(OutputPreference outputPreference) {
		this.outputPreference = outputPreference;
	}

	public String getPriceRangeMinStr() {
		return priceRangeMinStr;
	}

	public void setPriceRangeMinStr(String priceRangeMinStr) {
		this.priceRangeMinStr = priceRangeMinStr;
	}

	public String getPriceRangeMaxStr() {
		return priceRangeMaxStr;
	}

	public void setPriceRangeMaxStr(String priceRangeMaxStr) {
		this.priceRangeMaxStr = priceRangeMaxStr;
	}
}