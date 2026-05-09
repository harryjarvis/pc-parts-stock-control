package part;

public class GPU extends Part {

	private String chipset;
	private String vram;
	private double baseClock;
	private double boostClock;
	private int length;
	private String colour;
	
	public GPU(int id, String name, String manufacturer, double price, 
			String chipset, String vram, double baseClock, double boostClock, int length, String colour) {
		
		super(id, name, manufacturer, price, "GPU");
		
		this.chipset = chipset;
		this.vram = vram;
		this.baseClock = baseClock;
		this.boostClock = boostClock;
		this.length = length;
		this.colour = colour;
	}
	
	public String getChipset() {
		return chipset;
	}
	
	public String getVram() {
		return vram;
	}
	
	public double getBaseClock() {
		return baseClock;
	}
	
	public double getBoostClock() {
		return boostClock;
	}
	
	public int getLength() {
		return length;
	}
	
	public String getColour() {
		return colour;
	}

	public void setChipset(String chipset) {
		this.chipset = chipset;
	}
	
	public void setVram(String vram) {
		this.vram = vram;
	}

	public void setBaseClock(double baseClock) {
		this.baseClock = baseClock;
	}

	public void setBoostClock(double boostClock) {
		this.boostClock = boostClock;
	}

	public void setLength(int length) {
		this.length = length;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}
	
}
