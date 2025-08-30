package arsw.threads;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

public class MainCanodromo {

    private static Galgo[] galgos;

    private static Canodromo can;

    private static RegistroLlegada reg = new RegistroLlegada();

    public static void main(String[] args) {
        can = new Canodromo(17, 100);
        galgos = new Galgo[can.getNumCarriles()];
        can.setVisible(true);

        // Acción del botón start
        can.setStartAction(
                new ActionListener() {

                    @Override
                    public void actionPerformed(final ActionEvent e) {
                        // como acción, se crea un nuevo hilo que cree los hilos
                        // 'galgos', los pone a correr, y luego muestra los resultados.
                        // La acción del botón se realiza en un hilo aparte para evitar
                        // bloquear la interfaz gráfica.
                        ((JButton) e.getSource()).setEnabled(false);
                        new Thread() {
                            public void run() {
                                // Resetear el estado de pausa y el registro
                                Galgo.continuar();
                                reg = new RegistroLlegada();
                                can.restart();

                                for (int i = 0; i < can.getNumCarriles(); i++) {
                                    // crea los hilos 'galgos'
                                    galgos[i] = new Galgo(can.getCarril(i), "" + i, reg);
                                    // inicia los hilos
                                    galgos[i].start();
                                }

                                // Esperar a que todos los hilos terminen
                                for (int i = 0; i < can.getNumCarriles(); i++) {
                                    try {
                                        galgos[i].join();
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }

                                can.winnerDialog(reg.getGanador(), reg.getUltimaPosicionAlcanzada() - 1);
                                System.out.println("El ganador fue:" + reg.getGanador());

                                // Reiniciar el botón para permitir nueva carrera
                                ((JButton) e.getSource()).setEnabled(true);
                            }
                        }.start();

                    }
                });

        can.setStopAction(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Galgo.pausar();
                        System.out.println("Carrera pausada!");
                    }
                });

        can.setContinueAction(
                new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        Galgo.continuar();
                        System.out.println("Carrera reanudada!");
                    }
                });

    }

}
