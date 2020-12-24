package net.autoitemswitch.events;

public class CancellableEvent {
	private boolean cancelled = false;
	
	public void cancel() {
		this.cancelled = true;
	}
	
	public void pass() {
		this.cancelled = false;
	}
	
	public boolean isCancelled() {
		return this.cancelled;
	}
}
