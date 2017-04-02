import java.util.concurrent.Semaphore;

public class ProgressControl {
    private Semaphore readRequest;
    private Semaphore writeRequest;
    private Semaphore processRequest;

    public ProgressControl() throws InterruptedException {
	this.readRequest = new Semaphore(1);
	this.writeRequest = new Semaphore(1);
	this.processRequest = new Semaphore(1);
	this.readRequest.acquire();
	this.processRequest.acquire();
    }

    public void requestIncoming() throws InterruptedException {
	this.readRequest.acquire();
    }

    public void requstOutgoing() throws InterruptedException {
	this.writeRequest.acquire();
    }

    public void requestProcess() throws InterruptedException {
	this.processRequest.acquire();
    }

    public void signalIncoming() {
	this.readRequest.release();
    }

    public void singalOutgoing() {
	this.writeRequest.release();
    }

    public void signalProcess() {
	this.processRequest.release();
    }

}
