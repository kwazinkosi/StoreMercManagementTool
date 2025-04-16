package model.valueObjects;

public class Range<T extends Comparable<T>> {
    
	private T min;
    private T max;

    public Range() {
		this.min = null;
		this.max = null;
    }
    public Range(T min, T max) {
        this.min = min;
        this.max = max;
    }

	public void setMin(T min) {
		this.min = min;
	}
	
	public void setMax(T max) {
		this.max = max;
	}
	
	public void setRange(T min, T max) {
		this.min = min;
		this.max = max;
	}
	
    public T getMin() {
        return min;
    }

    public T getMax() {
        return max;
    }

    public boolean contains(T value) {
        return value.compareTo(min) >= 0 && value.compareTo(max) <= 0;
    }
}
