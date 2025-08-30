package edu.eci.arsw.primefinder;

import java.util.List;

public class Main {

	public static void main(String[] args) {
		System.out.println("=== ARSW Lab 2 - Parte 1: Prime Number Finder ===\n");
		
		// Test 1: Single thread execution
		System.out.println("1. Ejecutando búsqueda de números primos con un solo hilo:");
		System.out.println("   Rango: 0 - 30");
		long startTime = System.currentTimeMillis();
		
		PrimeFinderThread singleThread = new PrimeFinderThread(0, 30);
		singleThread.start();
		
		try {
			singleThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		long endTime = System.currentTimeMillis();
		System.out.println("   Números primos encontrados: " + singleThread.getPrimes().size());
		System.out.println("   Tiempo de ejecución: " + (endTime - startTime) + " ms\n");
		
		// Test 2: Multiple threads execution
		System.out.println("2. Ejecutando búsqueda de números primos con múltiples hilos:");
		System.out.println("   Rango dividido: 0-15000 (Hilo 1) y 15001-30000 (Hilo 2)");
		
		startTime = System.currentTimeMillis();
		
		PrimeFinderThread thread1 = new PrimeFinderThread(0, 15000);
		PrimeFinderThread thread2 = new PrimeFinderThread(15001, 30000);
		
		thread1.start();
		thread2.start();
		
		try {
			thread1.join();
			thread2.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		endTime = System.currentTimeMillis();
		
		int totalPrimes = thread1.getPrimes().size() + thread2.getPrimes().size();
		System.out.println("   Hilo 1 encontró: " + thread1.getPrimes().size() + " números primos");
		System.out.println("   Hilo 2 encontró: " + thread2.getPrimes().size() + " números primos");
		System.out.println("   Total de números primos: " + totalPrimes);
		System.out.println("   Tiempo de ejecución: " + (endTime - startTime) + " ms\n");
		
		// Test 3: Performance comparison with 3 threads
		System.out.println("3. Ejecutando búsqueda con 3 hilos para comparación de rendimiento:");
		System.out.println("   Rangos: 0-10000, 10001-20000, 20001-30000");
		
		startTime = System.currentTimeMillis();
		
		PrimeFinderThread threadA = new PrimeFinderThread(0, 10000);
		PrimeFinderThread threadB = new PrimeFinderThread(10001, 20000);
		PrimeFinderThread threadC = new PrimeFinderThread(20001, 30000);
		
		threadA.start();
		threadB.start();
		threadC.start();
		
		try {
			threadA.join();
			threadB.join();
			threadC.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		endTime = System.currentTimeMillis();
		
		int totalPrimesThreeThreads = threadA.getPrimes().size() + threadB.getPrimes().size() + threadC.getPrimes().size();
		System.out.println("   Hilo A encontró: " + threadA.getPrimes().size() + " números primos");
		System.out.println("   Hilo B encontró: " + threadB.getPrimes().size() + " números primos");
		System.out.println("   Hilo C encontró: " + threadC.getPrimes().size() + " números primos");
		System.out.println("   Total de números primos: " + totalPrimesThreeThreads);
		System.out.println("   Tiempo de ejecución: " + (endTime - startTime) + " ms\n");
		
		System.out.println("=== Fin de la demostración ===");
	}
}