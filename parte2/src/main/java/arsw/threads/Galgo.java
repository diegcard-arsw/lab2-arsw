package arsw.threads;

/**
 * Un galgo que puede correr en un carril
 * 
 * @author Diego Cardenas
 * 
 */
public class Galgo extends Thread {
	private int paso;
	private Carril carril;
	RegistroLlegada regl;
	private static volatile boolean pausado = false;
	private static final Object pauseLock = new Object();

	/**
	 * Constructs a new Galgo (greyhound) thread.
	 *
	 * @param carril the track (Carril) assigned to this Galgo.
	 * @param name   the name of the Galgo thread.
	 * @param reg    the RegistroLlegada instance used to record arrival order.
	 */
	public Galgo(Carril carril, String name, RegistroLlegada reg) {
		super(name);
		this.carril = carril;
		paso = 0;
		this.regl = reg;
	}

	/**
	 * Pauses the execution of the threads by setting the 'pausado' flag to true.
	 * This method is thread-safe and synchronizes on the 'pauseLock' object to
	 * ensure
	 * that the pause state is updated atomically.
	 */
	public static void pausar() {
		synchronized (pauseLock) {
			pausado = true;
		}
	}

	/**
	 * Resumes the execution of all threads that are waiting on the pauseLock
	 * object.
	 * Sets the 'pausado' flag to false and notifies all waiting threads to
	 * continue.
	 * This method should be called when the paused state needs to be lifted,
	 * allowing threads to proceed with their execution.
	 */
	public static void continuar() {
		synchronized (pauseLock) {
			pausado = false;
			pauseLock.notifyAll();
		}
	}

	/**
	 * Verifies if the current thread should be paused.
	 * <p>
	 * This method synchronizes on the {@code pauseLock} object and checks the
	 * {@code pausado} flag.
	 * If {@code pausado} is {@code true}, the thread waits until it is notified to
	 * resume.
	 * </p>
	 *
	 * @throws InterruptedException if the thread is interrupted while waiting.
	 */
	private void verificarPausa() throws InterruptedException {
		synchronized (pauseLock) {
			while (pausado) {
				pauseLock.wait();
			}
		}
	}

	/**
	 * Simulates the running behavior of a Galgo (greyhound) in a race.
	 * The method iterates through each step of the race track, updating the track
	 * and displaying progress.
	 * It checks for pause conditions before each step and sleeps for 100
	 * milliseconds to simulate movement.
	 * When the Galgo reaches the end of the track, it marks the finish, prints its
	 * position,
	 * and sets the winner if it is the first to finish.
	 *
	 * @throws InterruptedException if the thread is interrupted during sleep.
	 */
	public void corra() throws InterruptedException {
		while (paso < carril.size()) {
			verificarPausa();

			Thread.sleep(100);
			carril.setPasoOn(paso++);
			carril.displayPasos(paso);

			if (paso == carril.size()) {
				carril.finish();
				int ubicacion = regl.getYSetUltimaPosicion();
				System.out.println("El galgo " + this.getName() + " llego en la posicion " + ubicacion);
				if (ubicacion == 1) {
					regl.setGanador(this.getName());
				}
			}
		}
	}

	/**
	 * Executes the thread's logic by invoking the {@code corra()} method.
	 * Handles {@link InterruptedException} if the thread is interrupted during
	 * execution.
	 * This method is called when the thread is started.
	 */
	@Override
	public void run() {

		try {
			corra();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}