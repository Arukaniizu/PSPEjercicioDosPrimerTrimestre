package juanxxiii;

import juanxxiii.MostrarDatos;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        long inicio, fin, tiempo;
        Scanner scanner = new Scanner(System.in);
        int rangoInicial = 1;
        int numeroDeHilos = 0;
        try {
            System.out.println("Inserta el numero de hilos");
            numeroDeHilos = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.err.println("Dato introducido incorrecto");
             return;
        }
        MostrarDatos verDatos = new MostrarDatos(0);
        int numeroRegistros = verDatos.verDatosDos();
        int rango = numeroRegistros / numeroDeHilos;
        int resto = numeroRegistros % numeroDeHilos;
        int rangoFinal = rango;
        inicio = System.currentTimeMillis();
        for (int i = 0; i < numeroDeHilos; i++) {
            if (resto != 0) {
                verDatos = new MostrarDatos(resto, rangoFinal, rangoInicial, numeroDeHilos, rango);
                verDatos.start();
                rangoInicial += rango + 1;
                rangoFinal += rango + 1;
                resto += -1;
                try {
                    verDatos.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            } else {
                verDatos = new MostrarDatos(resto, rangoFinal, rangoInicial, numeroDeHilos, rango);
                verDatos.start();
                rangoInicial += rango;
                rangoFinal += rango;
                try {
                    verDatos.join();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

            //Este while es para que todos los hilos entren y acaben a la vez si no cuando
            //acaba el hilo final se sale aunque otros no terminasen y sale mal la cuenta del dinero.
        while (MostrarDatos.getContadorDeHilos() < numeroDeHilos) {
        }
        fin = System.currentTimeMillis();
        tiempo = fin - inicio;
        System.out.println("---------------------------------------------------------------------------------------------");
        System.out.println("La empesa gasta " + MostrarDatos.getDinero() + "€ en sueldo de los empleados");
        System.out.println(MostrarDatos.getTiempoPrimeraLectura() + " milisegundos tarda en realizar la lectura secuencial");
        System.out.println(tiempo + " milisegundos tarda en realizar la lectura con hilos");
    }
}
