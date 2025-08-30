package arsw.threads;

/**
 * RegistroLlegada class used to register the arrival of participants in a
 * competition.
 * Keeps track of the last position reached and the winner.
 *
 * Synchronized methods ensure correct concurrency when accessing and modifying
 * the position.
 */
public class RegistroLlegada {

	private int ultimaPosicionAlcanzada = 1;

	private String ganador = null;

	/**
	 * Returns the name of the winner.
	 *
	 * @return a {@code String} representing the winner's name.
	 */
	public String getGanador() {
		return ganador;
	}

	/**
	 * Sets the name of the winner.
	 *
	 * @param ganador the name of the winner to be set
	 */
	public void setGanador(String ganador) {
		this.ganador = ganador;
	}

	/**
	 * Returns the last position reached.
	 * <p>
	 * This method is synchronized to ensure thread-safe access to the
	 * {@code ultimaPosicionAlcanzada} field.
	 *
	 * @return the last position reached as an integer.
	 */
	public synchronized int getUltimaPosicionAlcanzada() {
		return ultimaPosicionAlcanzada;
	}

	/**
	 * Sets the value of the last position reached.
	 * This method is synchronized to ensure thread safety when updating the
	 * position.
	 *
	 * @param ultimaPosicionAlcanzada the last position reached to be set
	 */
	public synchronized void setUltimaPosicionAlcanzada(int ultimaPosicionAlcanzada) {
		this.ultimaPosicionAlcanzada = ultimaPosicionAlcanzada;
	}

	/**
	 * Returns the current value of {@code ultimaPosicionAlcanzada} and then
	 * increments it.
	 * <p>
	 * This method is synchronized to ensure thread safety when multiple threads
	 * access and modify {@code ultimaPosicionAlcanzada} concurrently.
	 * </p>
	 *
	 * @return the value of {@code ultimaPosicionAlcanzada} before it is incremented
	 */
	public synchronized int getYSetUltimaPosicion() {
		int posicion = ultimaPosicionAlcanzada;
		ultimaPosicionAlcanzada++;
		return posicion;
	}
}
