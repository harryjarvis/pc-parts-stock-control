package part;

public class CPU extends Part {

	private String socket;
	private int cores;
	private int threads;
	private double baseClock;
	private double boostClock;

	public CPU(int id, String name, String manufacturer, double price, 
			String socket, int cores, int threads, double baseClock, double boostClock) {
		
		super(id, name, manufacturer, price, "CPU");
		
		this.socket = socket;
		this.cores = cores;
		this.threads = threads;
		this.baseClock = baseClock;
		this.boostClock = boostClock;
	}
	
	public int getCores() {
		return cores;
	}
	
	public int getThreads() {
		return threads;
	}
	
	public double getBaseClock() {
		return baseClock;
	}
	
	public double getBoostClock() {
		return boostClock;
	}
	
	public String getSocket() {
		return socket;
	}
	
	public void setCores(int cores) {
		this.cores = cores;
	}
	
	public void setThreads(int threads) {
		this.threads = threads;
	}
	
	public void setBaseClock(double baseClock) {
		this.baseClock = baseClock;
	}
	
	public void setBoostClock(double boostClock) {
		this.boostClock = boostClock;
	}
	
	public void setSocket(String socket) {
		this.socket = socket;
	}
}
