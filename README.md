# Escuela Colombiana de Ingeniería

Arquitecturas de Software – ARSW

#### Taller – programación concurrente, condiciones de carrera y sincronización de hilos. EJERCICIO INDIVIDUAL O EN PAREJAS.

##### Parte I – Antes de terminar la clase.

Creación, puesta en marcha y coordinación de hilos.

1. Revise el programa “primos concurrentes” (en la carpeta parte1), dispuesto en el paquete edu.eci.arsw.primefinder. Este es un programa que calcula los números primos entre dos intervalos, distribuyendo la búsqueda de los mismos entre hilos independientes. Por ahora, tiene un único hilo de ejecución que busca los primos entre 0 y 30.000.000. Ejecútelo, abra el administrador de procesos del sistema operativo, y verifique cuantos núcleos son usados por el mismo.

2. Modifique el programa para que, en lugar de resolver el problema con un solo hilo, lo haga con tres, donde cada uno de éstos hará la tarcera parte del problema original. Verifique nuevamente el funcionamiento, y nuevamente revise el uso de los núcleos del equipo.

3. Lo que se le ha pedido es: debe modificar la aplicación de manera que cuando hayan transcurrido 5 segundos desde que se inició la ejecución, se detengan todos los hilos y se muestre el número de primos encontrados hasta el momento. Luego, se debe esperar a que el usuario presione ENTER para reanudar la ejecución de los mismo.

## Solucion 

Esta parte del laboratorio implementa un sistema de búsqueda de números primos utilizando hilos (threads) en Java. El objetivo es demostrar el uso de programación concurrente para mejorar el rendimiento de algoritmos computacionalmente intensivos.

## Estructura del Proyecto

```
parte1/
├── src/
│   └── main/
│       └── java/
│           └── edu/
│               └── eci/
│                   └── arsw/
│                       └── primefinder/
│                           ├── Main.java
│                           └── PrimeFinderThread.java
├── pom.xml
└── README.md
```

## Clases Principales

### PrimeFinderThread.java

Esta clase extiende `Thread` y implementa la lógica para encontrar números primos en un rango específico.

**Características principales:**
- Busca números primos en un rango [a, b] dado
- Utiliza un algoritmo optimizado de verificación de primalidad
- Almacena los números primos encontrados en una lista
- Proporciona información de progreso durante la ejecución
- Soporte para impresión opcional de números primos encontrados

**Métodos importantes:**
- `PrimeFinderThread(int a, int b)`: Constructor básico
- `PrimeFinderThread(int a, int b, boolean printPrimes)`: Constructor con opción de impresión
- `run()`: Método principal del hilo que ejecuta la búsqueda
- `isPrime(int n)`: Verifica si un número es primo
- `getPrimes()`: Retorna la lista de números primos encontrados

### Main.java

Clase principal que demuestra diferentes escenarios de uso de hilos para la búsqueda de números primos.

**Demostraciones incluidas:**
1. **Hilo único**: Búsqueda de primos en un rango pequeño con un solo hilo
2. **Dos hilos**: División del trabajo entre dos hilos para mejorar rendimiento
3. **Tres hilos**: Comparación de rendimiento con división en tres rangos

## Algoritmo de Verificación de Primalidad

El algoritmo implementado utiliza una optimización basada en el hecho de que todos los números primos mayores a 3 tienen la forma 6k ± 1:

```java
boolean isPrime(int n) {
    // Casos especiales
    if (n <= 1) return false;
    if (n <= 3) return true;
    if (n % 2 == 0 || n % 3 == 0) return false;
    
    // Verificar divisores de la forma 6k ± 1
    for (int i = 5; i * i <= n; i += 6) {
        if (n % i == 0 || n % (i + 2) == 0) {
            return false;
        }
    }
    
    return true;
}
```

## Compilación y Ejecución

### Prerrequisitos
- Java JDK 8 o superior
- Maven 3.6 o superior

### Compilar el proyecto
```bash
cd parte1
mvn compile
```

### Ejecutar el programa
```bash
mvn exec:java -Dexec.mainClass="edu.eci.arsw.primefinder.Main"
```

O alternativamente, compilar y ejecutar manualmente:
```bash
cd parte1
javac -d target/classes src/main/java/edu/eci/arsw/primefinder/*.java
java -cp target/classes edu.eci.arsw.primefinder.Main
```

### Ejemplo de Salida

![Ejemplo de salida](img/image.png)

### Consumo de Recursos

![Consumo de recursos](img/consumo-recursos-1.png)

![Consumo de recursos 2](img/consumo-recursos-2.png)


## Conceptos de Concurrencia Demostrados

### 1. Paralelización de Tareas
- División de un problema grande en subproblemas más pequeños
- Ejecución simultánea de múltiples hilos
- Sincronización de hilos usando `join()`

### 2. Mejora de Rendimiento
- Comparación de tiempos de ejecución entre diferentes configuraciones
- Demostración de cómo el paralelismo puede reducir el tiempo total de procesamiento
- Análisis del overhead de creación y gestión de hilos

### 3. Gestión de Hilos
- Uso de la clase `Thread` y override del método `run()`
- Nomenclatura descriptiva de hilos para debugging
- Espera sincronizada de finalización de hilos

## Consideraciones de Rendimiento

### Ventajas del Paralelismo:
- **Utilización de múltiples núcleos**: Aprovecha el hardware multi-core
- **Reducción del tiempo total**: División efectiva del trabajo
- **Escalabilidad**: Posibilidad de ajustar el número de hilos según los recursos

### Limitaciones:
- **Overhead de hilos**: Creación y gestión de hilos consume recursos
- **Punto de rendimientos decrecientes**: Demasiados hilos pueden degradar el rendimiento
- **Dependencia del hardware**: El beneficio varía según el número de núcleos disponibles

## Posibles Mejoras

1. **Pool de hilos**: Utilizar `ExecutorService` para gestión más eficiente
2. **Trabajo balanceado**: Implementar distribución dinámica de rangos
3. **Resultados agregados**: Combinar resultados de múltiples hilos de forma más elegante
4. **Configuración flexible**: Permitir configurar número de hilos y rangos desde argumentos
5. **Métricas detalladas**: Incluir más estadísticas de rendimiento y utilización de recursos



#####Parte II 


Para este ejercicio se va a trabajar con un simulador de carreras de galgos (carpeta parte2), cuya representación gráfica corresponde a la siguiente figura:

![](./img/media/image1.png)

En la simulación, todos los galgos tienen la misma velocidad (a nivel de programación), por lo que el galgo ganador será aquel que (por cuestiones del azar) haya sido más beneficiado por el *scheduling* del
procesador (es decir, al que más ciclos de CPU se le haya otorgado durante la carrera). El modelo de la aplicación es el siguiente:

![](./img/media/image2.png)

Como se observa, los galgos son objetos ‘hilo’ (Thread), y el avance de los mismos es visualizado en la clase Canodromo, que es básicamente un formulario Swing. Todos los galgos (por defecto son 17 galgos corriendo en una pista de 100 metros) comparten el acceso a un objeto de tipo
RegistroLLegada. Cuando un galgo llega a la meta, accede al contador ubicado en dicho objeto (cuyo valor inicial es 1), y toma dicho valor como su posición de llegada, y luego lo incrementa en 1. El galgo que
logre tomar el ‘1’ será el ganador.

Al iniciar la aplicación, hay un primer error evidente: los resultados (total recorrido y número del galgo ganador) son mostrados antes de que finalice la carrera como tal. Sin embargo, es posible que una vez corregido esto, haya más inconsistencias causadas por la presencia de condiciones de carrera.

Taller.

1.  Corrija la aplicación para que el aviso de resultados se muestre
    sólo cuando la ejecución de todos los hilos ‘galgo’ haya finalizado.
    Para esto tenga en cuenta:

    a.  La acción de iniciar la carrera y mostrar los resultados se realiza a partir de la línea 38 de MainCanodromo.

    b.  Puede utilizarse el método join() de la clase Thread para sincronizar el hilo que inicia la carrera, con la finalización de los hilos de los galgos.

2.  Una vez corregido el problema inicial, corra la aplicación varias
    veces, e identifique las inconsistencias en los resultados de las
    mismas viendo el ‘ranking’ mostrado en consola (algunas veces
    podrían salir resultados válidos, pero en otros se pueden presentar
    dichas inconsistencias). A partir de esto, identifique las regiones
    críticas () del programa.

3.  Utilice un mecanismo de sincronización para garantizar que a dichas
    regiones críticas sólo acceda un hilo a la vez. Verifique los
    resultados.

4.  Implemente las funcionalidades de pausa y continuar. Con estas,
    cuando se haga clic en ‘Stop’, todos los hilos de los galgos
    deberían dormirse, y cuando se haga clic en ‘Continue’ los mismos
    deberían despertarse y continuar con la carrera. Diseñe una solución que permita hacer esto utilizando los mecanismos de sincronización con las primitivas de los Locks provistos por el lenguaje (wait y notifyAll).


## Criterios de evaluación

1. Funcionalidad.

    1.1. La ejecución de los galgos puede ser detenida y resumida consistentemente.
    
    1.2. No hay inconsistencias en el orden de llegada registrado.
    
2. Diseño.   

    2.1. Se hace una sincronización de sólo la región crítica (sincronizar, por ejemplo, todo un método, bloquearía más de lo necesario).
    
    2.2. Los galgos, cuando están suspendidos, son reactivados son sólo un llamado (usando un monitor común).

