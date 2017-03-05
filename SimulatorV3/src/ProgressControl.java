import java.util.concurrent.Semaphore;

public class ProgressControl {
	private Semaphore readRequest;
	private Semaphore writeRequest;
	private Semaphore spRequest;

	public ProgressControl() throws InterruptedException {
		this.readRequest = new Semaphore(1);
		this.writeRequest = new Semaphore(1);
		this.spRequest = new Semaphore(1);
		this.readRequest.acquire();
		this.spRequest.acquire();
	}

	public void requestIncoming() throws InterruptedException {
		this.readRequest.acquire();
	}

	public void requstOutgoing() throws InterruptedException {
		this.writeRequest.acquire();
	}

	public void requestShortestPath() throws InterruptedException {
		this.spRequest.acquire();
	}

	public void signalIncoming() {
		this.readRequest.release();
	}

	public void singalOutgoing() {
		this.writeRequest.release();
	}

	public void signalShortestPath() {
		this.spRequest.release();
	}

}
