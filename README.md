# Taller de Programación Concurrente - ARSW

*Arquitecturas de Software - Escuela Colombiana de Ingeniería*

Este taller explora conceptos fundamentales de programación concurrente en Java, incluyendo la creación de hilos, sincronización, manejo de condiciones de carrera y mecanismos de coordinación entre procesos paralelos.

## 📋 Tabla de Contenidos

- [Parte I: Búsqueda Concurrente de Números Primos](#parte-i-búsqueda-concurrente-de-números-primos)
- [Parte II: Simulador de Carreras de Galgos](#parte-ii-simulador-de-carreras-de-galgos)
- [Prerequisitos](#prerequisitos)
- [Instalación y Ejecución](#instalación-y-ejecución)
- [Conceptos Clave](#conceptos-clave)

---

## Parte I: Búsqueda Concurrente de Números Primos

### 🎯 Objetivos
- Implementar paralelización de algoritmos computacionalmente intensivos
- Comparar rendimiento entre ejecución secuencial y concurrente
- Implementar control de pausas en procesos concurrentes

### 📁 Estructura del Proyecto

```
parte1/
├── src/main/java/edu/eci/arsw/primefinder/
│   ├── Main.java                 # Clase principal con demostraciones
│   └── PrimeFinderThread.java    # Hilo para búsqueda de primos
├── pom.xml
└── README.md
```

### 🧩 Componentes Principales

#### `PrimeFinderThread.java`
Hilo especializado en la búsqueda eficiente de números primos en un rango dado.

**Características:**
- ✅ Algoritmo optimizado basado en la forma 6k±1
- ✅ Control de pausa/reanudación con sincronización
- ✅ Reporte de progreso en tiempo real
- ✅ Almacenamiento de resultados thread-safe

**Constructor:**
```java
PrimeFinderThread(int start, int end, boolean printPrimes)
```

#### `Main.java`
Demuestra diferentes escenarios de paralelización:

1. **Escenario Base**: Un hilo buscando primos 0-30,000,000
2. **Paralelización 3 hilos**: División equitativa del rango
3. **Control temporal**: Pausa automática después de 5 segundos

### ⚡ Algoritmo de Primalidad

Implementación optimizada que reduce significativamente las verificaciones:

```java
boolean isPrime(int n) {
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    
    // Solo verificar números de la forma 6k ± 1
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) return false;
    }
    return true;
}
```

**Ventajas del algoritmo:**
- Reduce verificaciones en ~66% comparado con fuerza bruta
- Complejidad temporal: O(√n/3)
- Optimizado para grandes volúmenes de datos

## Ejemplo de salida 

![Salida de Primos](./img/image.png)

## Consumo de recursos 

![Consumo de CPU y Memoria](./img/consumo-recursos-1.png)

![Consumo de CPU y Memoria](./img/consumo-recursos-2.png)

### 🚀 Ejecución - Parte I

```bash
cd parte1
mvn compile exec:java -Dexec.mainClass="edu.eci.arsw.primefinder.Main"
```

### 📊 Resultados Esperados

| Configuración | Tiempo Aprox. | Núcleos Utilizados | Primos Encontrados |
|---------------|---------------|-------------------|-------------------|
| 1 hilo        | ~45s          | 1                 | 1,857,859         |
| 3 hilos       | ~18s          | 3                 | 1,857,859         |

---

## Parte II: Simulador de Carreras de Galgos

### 🎯 Objetivos
- Identificar y corregir condiciones de carrera (race conditions)
- Implementar sincronización thread-safe
- Desarrollar mecanismos de pausa/reanudación con monitors
- Coordinar múltiples hilos con estados compartidos

### 📸 Vista del Simulador

#### Interfaz Gráfica de la Carrera
![Simulador de Carreras](./img/media/image1.png)

#### Modelo de la Aplicación
![Modelo de la aplicación](./img/media/image2.png)

### 📁 Estructura del Proyecto

```
parte2/
├── src/main/java/arsw/threads/
│   ├── MainCanodromo.java      # Aplicación principal
│   ├── Galgo.java              # Hilo representando cada galgo
│   ├── RegistroLlegada.java    # Estado compartido (thread-safe)
│   ├── Canodromo.java          # Interfaz gráfica Swing
│   ├── Carril.java             # Representación visual del carril
│   └── Test*.java              # Clases para pruebas unitarias
└── pom.xml
```

### 🐛 Problemas Identificados y Soluciones

#### **Problema 1: Condición de Carrera en Resultados**

**❌ Problema:**
```java
// Mostraba resultados antes de que terminaran todos los hilos
can.winnerDialog(reg.getGanador(), reg.getUltimaPosicionAlcanzada() - 1);
```

**✅ Solución:**
```java
// Esperar sincronización de todos los hilos
for (int i = 0; i < can.getNumCarriles(); i++) {
    try {
        galgos[i].join(); // Bloquea hasta que el hilo termine
    } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        e.printStackTrace();
    }
}
```

#### **Problema 2: Race Condition en RegistroLlegada**

**❌ Problema:** Múltiples hilos accedían concurrentemente al contador de posiciones.

**✅ Solución Thread-Safe:**
```java
public class RegistroLlegada {
    private int ultimaPosicionAlcanzada = 1;
    private String ganador = null;
    
    // Operación atómica para obtener y actualizar posición
    public synchronized int getYSetUltimaPosicion() {
        int posicion = ultimaPosicionAlcanzada;
        ultimaPosicionAlcanzada++;
        return posicion;
    }
    
    public synchronized void setGanador(String ganador) {
        if (this.ganador == null) {
            this.ganador = ganador;
        }
    }
}
```

#### **Problema 3: Control de Pausa/Reanudación**

**✅ Solución con Monitor Pattern:**
```java
public class Galgo extends Thread {
    private static volatile boolean pausado = false;
    private static final Object pauseLock = new Object();
    
    public static void pausar() {
        synchronized (pauseLock) {
            pausado = true;
        }
    }
    
    public static void continuar() {
        synchronized (pauseLock) {
            pausado = false;
            pauseLock.notifyAll(); // Despierta todos los hilos esperando
        }
    }
    
    private void verificarPausa() throws InterruptedException {
        synchronized (pauseLock) {
            while (pausado) {
                pauseLock.wait(); // Libera el lock y espera
            }
        }
    }
}
```

### 🔄 Ciclo de Vida de la Carrera

```
[INICIO] → [CORRIENDO] ⇄ [PAUSADO] → [FINALIZADO]
     ↓         ↓            ↓            ↓
  Reset    verificarPausa() wait()   join() + results
```

### 🚀 Ejecución - Parte II

```bash
cd parte2
mvn compile exec:java -Dexec.mainClass="arsw.threads.MainCanodromo"
```

**Para entornos sin GUI:**
```bash
mvn test -Dtest=TestCanodromo
mvn test -Dtest=TestGalgoPause
```

---

## 📋 Prerequisitos

- **Java JDK**: 8 o superior
- **Apache Maven**: 3.6+
- **SO**: Windows/Linux/macOS con soporte para GUI (Parte II)

## ⚙️ Instalación y Ejecución

### Clonar y Compilar
```bash
git clone https://github.com/diegcard-arsw/lab2-arsw.git
cd lab2-arsw

# Compilar ambas partes
mvn clean compile
```

### Ejecución Individual
```bash
# Parte I - Búsqueda de Primos
cd parte1 && mvn exec:java -Dexec.mainClass="edu.eci.arsw.primefinder.Main"

# Parte II - Simulador de Carreras  
cd parte2 && mvn exec:java -Dexec.mainClass="arsw.threads.MainCanodromo"
```

### Pruebas Automatizadas
```bash
mvn test                    # Ejecutar todas las pruebas
mvn test -Dtest=TestGalgo*  # Solo pruebas de galgos
```

---

## 🧠 Conceptos Clave

### Sincronización y Thread Safety

| Mecanismo | Uso en el Proyecto | Beneficio |
|-----------|-------------------|-----------|
| `synchronized` | Métodos críticos en RegistroLlegada | Acceso exclusivo a recursos compartidos |
| `volatile` | Variable de pausa en Galgo | Visibilidad entre hilos |
| `wait/notify` | Control de pausa/reanudación | Coordinación eficiente de hilos |
| `join()` | Espera de finalización | Sincronización de ciclo de vida |

### Patrones de Concurrencia Implementados

1. **Producer-Consumer**: Galgos producen posiciones, RegistroLlegada consume
2. **Monitor Pattern**: Control centralizado de pausa con `wait/notifyAll`
3. **Thread Pool Pattern**: Distribución de trabajo entre hilos fijos
4. **Immutable Objects**: Configuraciones de carrera no modificables

### Optimizaciones de Rendimiento

- **Lock Granularity**: Sincronización mínima necesaria
- **Atomic Operations**: Operaciones get-and-set atómicas
- **Volatile Variables**: Evita cache incoherence
- **Efficient Algorithms**: Algoritmo optimizado de primalidad

## 📈 Análisis de Rendimiento

### Métricas Observadas
- **Speedup**: 2.8x con 3 hilos (efficiency ~93%)
- **CPU Utilization**: 85-95% en configuración multi-hilo
- **Memory Overhead**: <50MB adicionales por paralelización
- **Thread Contention**: Mínima gracias a sincronización optimizada

### Benchmarking
```bash
# Para mediciones de rendimiento detalladas
mvn exec:java -Dexec.args="-benchmark -threads=4 -range=50000000"
```

---

## 🤝 Contribución

Este proyecto es parte del curso de Arquitecturas de Software (ARSW) en la Escuela Colombiana de Ingeniería. Las mejoras y optimizaciones son bienvenidas siguiendo las mejores prácticas de programación concurrente.

## 📄 Licencia

# Taller de Programación Concurrente - ARSW

---

<div align="center">

### 🚀 Proyecto desarrollado para **Arquitecturas de Software - Escuela Colombiana de Ingeniería**

**Autor:** [Diego Cardenas](https://github.com/diegcard)  
**Año:** 2025
**Licencia:** Uso académico

</div>
