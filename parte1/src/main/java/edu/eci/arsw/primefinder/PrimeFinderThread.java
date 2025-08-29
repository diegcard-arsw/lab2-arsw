package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

/**
 * PrimeFinderThread is a thread that finds all prime numbers within a given
 * range [a, b].
 * It stores the found primes in a list and prints each prime to the standard
 * output.
 *
 * Usage:
 * - Instantiate with the desired range.
 * - Start the thread to begin prime searching.
 * - Retrieve the list of found primes using {@link #getPrimes()}.
 *
 * Note:
 * - The primality test used is optimized for odd numbers greater than 2.
 * - The thread is not thread-safe for concurrent access to the primes list.
 * 
 * @author Diego Cardenas
 * 
 */
public class PrimeFinderThread extends Thread {

	int a, b;

	private List<Integer> primes = new LinkedList<Integer>();

	/**
	 * Constructs a PrimeFinderThread with the specified range.
	 *
	 * @param a the starting integer of the range (inclusive)
	 * @param b the ending integer of the range (inclusive)
	 */
	public PrimeFinderThread(int a, int b) {
		super();
		this.a = a;
		this.b = b;
	}

	/**
	 * Executes the prime number search within the specified range [a, b].
	 * Iterates through each integer in the range, checks if it is prime using
	 * {@code isPrime(i)},
	 * and if so, adds it to the {@code primes} collection and prints the prime
	 * number to the console.
	 * This method is intended to be run in a separate thread.
	 */
	@Override
	public void run() {
		for (int i = a; i <= b; i++) {
			if (isPrime(i)) {
				primes.add(i);
				System.out.println(i);
			}
		}
	}

	/**
	 * Determines whether a given integer is a prime number.
	 *
	 * @param n the integer to check for primality
	 * @return true if {@code n} is a prime number, false otherwise
	 */
	boolean isPrime(int n) {
		if (n % 2 == 0)
			return false;
		for (int i = 3; i * i <= n; i += 2) {
			if (n % i == 0)
				return false;
		}
		return true;
	}

	/**
	 * Returns the list of prime numbers found by this thread.
	 *
	 * @return a list of integers representing the prime numbers.
	 */
	public List<Integer> getPrimes() {
		return primes;
	}
}
