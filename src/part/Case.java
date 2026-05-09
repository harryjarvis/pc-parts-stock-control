package part;

public class Case extends Part {

	private String caseType;
	private int maxGpuLength;
	private String colour;
	private double length;
	private double width;
	private double height;
	private double volume;
	
	public Case(int id, String name, String manufacturer, double price, 
			String caseType, int maxGpuLength, String colour, double length, double width, double height, double volume) {
		
		super(id, name, manufacturer, price, "Case");
		
		this.caseType = caseType;
		this.maxGpuLength = maxGpuLength;
		this.colour = colour;
		this.length = length;
		this.width = width;
		this.height = height;
		this.volume = volume;
	}

	public String getCaseType() {
		return caseType;
	}

	public String getColour() {
		return colour;
	}

	public double getLength() {
		return length;
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}

	public int getMaxGpuLength() {
		return maxGpuLength;
	}
	
	public double getVolume() {
		return volume;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	public void setLength(double length) {
		this.length = length;
	}

	public void setWidth(double width) {
		this.width = width;
	}

	public void setHeight(double height) {
		this.height = height;
	}

	public void setMaxGpuLength(int maxGpuLength) {
		this.maxGpuLength = maxGpuLength;
	}
	
	public void setVolume(double volume) {
		this.volume = volume;
	}
	
}
