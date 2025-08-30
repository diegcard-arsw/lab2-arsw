package arsw.threads;

import java.util.Random;

/**
 * Utility class for generating random integers.
 * <p>
 * This class provides a static method to generate random integers
 * using a single {@link java.util.Random} instance seeded with the current
 * system time.
 * </p>
 */
public class RandomGenerator {

	private static Random random = new Random(System.currentTimeMillis());

	/**
	 * Generates a random integer between 0 (inclusive) and the specified maximum
	 * value (exclusive).
	 *
	 * @param max the upper bound (exclusive) for the generated random integer.
	 * @return a random integer between 0 (inclusive) and max (exclusive).
	 */
	public static int nextInt(int max) {
		return random.nextInt(max);
	}

}
