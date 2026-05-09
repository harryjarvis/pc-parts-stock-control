package part;

public class Motherboard extends Part {

	private String socket;
	private String formFactor;
	private String chipset;
	private int maxMemory;
	private boolean wifi;
	private String colour;
	
	public Motherboard(int id, String name, String manufacturer, double price, 
			String socket, String formFactor, String chipset, int maxMemory, boolean wifi, String colour) {
		
		super(id, name, manufacturer, price, "Motherboard");
		
		this.socket = socket;
		this.formFactor = formFactor;
		this.chipset = chipset;
		this.maxMemory = maxMemory;
		this.wifi = wifi;
		this.colour = colour;
	}

	public String getSocket() {
		return socket;
	}

	public String getFormFactor() {
		return formFactor;
	}

	public String getChipset() {
		return chipset;
	}

	public int getMaxMemory() {
		return maxMemory;
	}

	public boolean isWifi() {
		return wifi;
	}
	
	public String getColour() {
		return colour;
	}

	public void setSocket(String socket) {
		this.socket = socket;
	}

	public void setFormFactor(String formFactor) {
		this.formFactor = formFactor;
	}

	public void setChipset(String chipset) {
		this.chipset = chipset;
	}

	public void setMaxMemory(int maxMemory) {
		this.maxMemory = maxMemory;
	}

	public void setWifi(boolean wifi) {
		this.wifi = wifi;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}
}
