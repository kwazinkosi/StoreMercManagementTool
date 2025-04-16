package model.enums;

public enum OutputPreference {
    PRICE,       // Sort by price ascending
    RATING,      // Sort by rating descending
    BOTH,        // Sort by price ascending, then rating descending
    RELEVANCE    // Sort by last accessed time (simulate LRU)
}