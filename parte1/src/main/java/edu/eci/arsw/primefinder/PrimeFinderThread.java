package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

/**
 * PrimeFinderThread is a thread that finds all prime numbers within a given
 * range [a, b].
 * It stores the found primes in a list and optionally prints each prime to the standard
 * output.
 *
 * Usage:
 * - Instantiate with the desired range.
 * - Start the thread to begin prime searching.
 * - Retrieve the list of found primes using {@link #getPrimes()}.
 *
 * Note:
 * - The primality test is optimized and handles all cases correctly.
 * - The thread is not thread-safe for concurrent access to the primes list.
 * 
 * @author Diego Cardenas
 * 
 */
public class PrimeFinderThread extends Thread {

	private int a, b;
	private List<Integer> primes = new LinkedList<Integer>();
	private boolean printPrimes;

	/**
	 * Constructs a PrimeFinderThread with the specified range.
	 * Primes will be printed to console by default.
	 *
	 * @param a the starting integer of the range (inclusive)
	 * @param b the ending integer of the range (inclusive)
	 */
	public PrimeFinderThread(int a, int b) {
		this(a, b, false);
	}

	/**
	 * Constructs a PrimeFinderThread with the specified range and print option.
	 *
	 * @param a the starting integer of the range (inclusive)
	 * @param b the ending integer of the range (inclusive)
	 * @param printPrimes whether to print found primes to console
	 */
	public PrimeFinderThread(int a, int b, boolean printPrimes) {
		super("PrimeFinderThread-" + a + "-" + b);
		this.a = a;
		this.b = b;
		this.printPrimes = printPrimes;
	}

	/**
	 * Executes the prime number search within the specified range [a, b].
	 * Iterates through each integer in the range, checks if it is prime using
	 * {@code isPrime(i)},
	 * and if so, adds it to the {@code primes} collection and optionally prints 
	 * the prime number to the console.
	 * This method is intended to be run in a separate thread.
	 */
	@Override
	public void run() {
		System.out.println("Hilo " + Thread.currentThread().getName() + " iniciado. Buscando primos en rango [" + a + ", " + b + "]");
		
		for (int i = a; i <= b; i++) {
			if (isPrime(i)) {
				primes.add(i);
				if (printPrimes) {
					System.out.println("Primo encontrado: " + i);
				}
			}
		}
		
		System.out.println("Hilo " + Thread.currentThread().getName() + " completado. " + primes.size() + " números primos encontrados.");
	}

	/**
	 * Determines whether a given integer is a prime number.
	 * A prime number is a natural number greater than 1 that has no positive divisors 
	 * other than 1 and itself.
	 *
	 * @param n the integer to check for primality
	 * @return true if {@code n} is a prime number, false otherwise
	 */
	boolean isPrime(int n) {
		// Handle edge cases
		if (n <= 1) return false;
		if (n <= 3) return true;
		if (n % 2 == 0 || n % 3 == 0) return false;
		
		// Check for divisors from 5 onwards
		// All primes > 3 are of the form 6k ± 1
		for (int i = 5; i * i <= n; i += 6) {
			if (n % i == 0 || n % (i + 2) == 0) {
				return false;
			}
		}
		
		return true;
	}

	/**
	 * Returns the list of prime numbers found by this thread.
	 *
	 * @return a list of integers representing the prime numbers found in the range
	 */
	public List<Integer> getPrimes() {
		return primes;
	}

	/**
	 * Returns the range that this thread is searching.
	 *
	 * @return a string representation of the search range
	 */
	public String getRange() {
		return "[" + a + ", " + b + "]";
	}

	/**
	 * Returns the starting value of the range.
	 *
	 * @return the starting integer of the range
	 */
	public int getStartRange() {
		return a;
	}

	/**
	 * Returns the ending value of the range.
	 *
	 * @return the ending integer of the range
	 */
	public int getEndRange() {
		return b;
	}
}
