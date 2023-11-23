package simulacion;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

public class Simulacion {

    private static double tiempoTotalEspera = 0;
    private static int clientesAtendidosTotal = 0;
    private static int maxClientesEsperando = 0;

    
    public static void main(String[] args) {
        // Parámetros de la simulación
        int duracionSimulacion = 120; // Duración de la simulación en minutos
        int frecuenciaActual = 20; // Frecuencia actual de los autobuses en minutos
        int nuevaFrecuencia = 15; // Nueva frecuencia de los autobuses en minutos

        // Inicializar la simulación
        Queue<Double> llegadasClientes = generarLlegadasClientes();
        Queue<Double> llegadasAutobuses = generarLlegadasAutobuses();

        int clientesEsperando = 0;

        // Simulación
        for (int tiempo = 0; tiempo < duracionSimulacion; tiempo++) {
            // Verificar si llega un cliente
            if (!llegadasClientes.isEmpty() && llegadasClientes.peek() <= tiempo) {
                llegadasClientes.poll();
                clientesEsperando++;
            }

            // Verificar si llega un autobús
            if (tiempo % frecuenciaActual == 0) {
                // Cambiar a la nueva frecuencia cuando corresponda
                if (tiempo >= duracionSimulacion / 2) {
                    frecuenciaActual = nuevaFrecuencia;
                }
                // Atender a los clientes
                int clientesAtendidos = Math.min(clientesEsperando, 8);
                clientesEsperando -= clientesAtendidos;
                // Llamada a la función de atención a los clientes
                atenderClientes(tiempo, clientesAtendidos, clientesEsperando);

                System.out.println("Tiempo: " + tiempo + " - Llegó un autobús. Atendidos: " + clientesAtendidos +
                        ", Esperando: " + clientesEsperando);
            }
        }
    }

    private static void atenderClientes(int tiempo, int clientesAtendidos, int clientesEsperando) {
        // Determinar el tiempo de espera promedio de los clientes atendidos en esta ronda
        if (clientesAtendidos > 0) {
            double tiempoEsperaPromedio = tiempoTotalEspera*120 / clientesAtendidosTotal;
            System.out.println("Tiempo de espera promedios: " + tiempoEsperaPromedio);
        }

        // Actualizar estadísticas globales
        tiempoTotalEspera += clientesEsperando;
        clientesAtendidosTotal += clientesAtendidos;

        // Actualizar el número máximo de clientes que esperan
        if (clientesEsperando > maxClientesEsperando) {
            maxClientesEsperando = clientesEsperando;
        }
    }
    
    // Generador de llegadas de clientes con distribución exponencial
    private static Queue<Double> generarLlegadasClientes() {
        Queue<Double> llegadas = new LinkedList<>();
        Random random = new Random();
        double lambda = 0.5; // Tasa promedio de llegada de clientes por minuto

        double tiempo = 0;
        while (tiempo < 120) { // Simular durante 2 horas
            double llegada = -Math.log(1 - random.nextDouble()) / lambda;
            tiempo += llegada;
            llegadas.add(tiempo);
        }
        return llegadas;
    }

    // Generador de llegadas de autobuses con distribución normal
    private static Queue<Double> generarLlegadasAutobuses() {
        Queue<Double> llegadas = new LinkedList<>();
        Random random = new Random();
        double media = 4; // Media de llegada de autobuses en minutos
        double desviacionEstandar = 1; // Desviación estándar de llegada de autobuses en minutos

        double tiempo = 0;
        while (tiempo < 120) { // Simular durante 2 horas
            double llegada = Math.max(0, random.nextGaussian() * desviacionEstandar + media);
            tiempo += llegada;
            llegadas.add(tiempo);
        }
        return llegadas;
    }
}
