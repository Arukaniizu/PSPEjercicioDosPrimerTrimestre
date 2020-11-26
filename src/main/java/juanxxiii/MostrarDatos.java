package juanxxiii;

import java.sql.*;

public class MostrarDatos extends Thread {

    private static long tiempoPrimeraLectura;
    private static int dinero = 0;
    private static int contadorDeHilos;
    int resto;
    int rangoFinal;
    int rangoInicial;
    int numeroDeHilos;
    int rango;


    public MostrarDatos(int resto){
        this.resto = resto;
    }

    public MostrarDatos(int resto, int rangoFinal, int rangoInicial, int numbrethreads, int rango) {
        this.resto = resto;
        this.rangoInicial = rangoInicial;
        this.rangoFinal = rangoFinal;
        this.numeroDeHilos = numbrethreads;
        this.rango = rango;
    }

    public void run() {
        verDatos();
    }

    //llamada con el hilo  principal
    public int verDatosDos() {
        Connection conexion;
        int id_usuarios, ingresos;
        int ingresosTotales = 0;
        String email;
        int cont = 0;
        try {
            long inicio = System.currentTimeMillis();
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
            Statement consulta = conexion.createStatement();
            ResultSet resultado = consulta.executeQuery("Select * FROM `empleados`");
            while (resultado.next()) {
                id_usuarios = resultado.getInt("ID");
                ingresos = resultado.getInt("INGRESOS");
                email = resultado.getString("EMAIL");
                System.out.println(Thread.currentThread().getName() + " El usuario con ID: " + id_usuarios + " y con Email: " + email + " tiene unos ingresos de:" + ingresos);
                ingresosTotales += ingresos;
                cont++;
            }
            resultado.close();
            consulta.close();
            long fin = System.currentTimeMillis();
            tiempoPrimeraLectura = fin - inicio;
        } catch (SQLException ex) {
            ex.printStackTrace();
            System.out.println(" SQLException : " + ex.getMessage());
            System.out.println(" SQLState : " + ex.getSQLState());
            System.out.println(" VendorError : " + ex.getErrorCode());
        }
        System.out.println("----------------------------fin de la primera lectura-----------------------------");
        return cont;
    }

    //llamada con hilos
    public synchronized void verDatos () {
        Connection conexion;
        int id_usuarios, ingresos;
        String email;
        try {
            conexion = DriverManager.getConnection("jdbc:mysql://localhost/bbdd_psp_1", "DAM2020_PSP", "DAM2020_PSP");
            Statement consulta = conexion.createStatement();
            ResultSet resultado;
            if (resto != 0){
                rangoFinal +=1;
                resultado= consulta.executeQuery("select * from empleados where id between " + rangoInicial + " and " + rangoFinal);
            }else{
                resultado= consulta.executeQuery("select * from empleados where id between " + rangoInicial + " and " + rangoFinal);
            }

            while (resultado.next()) {
                id_usuarios = resultado.getInt("ID");
                ingresos = resultado.getInt("INGRESOS");
                email = resultado.getString("EMAIL");
                System.out.println(Thread.currentThread().getName() + " El usuario con ID: " + id_usuarios + " y con Email: " + email + " tiene unos ingresos de:" + ingresos);
                dinero += ingresos;
            }
            contadorDeHilos ++;
            resultado.close();
            consulta.close();
        } catch (SQLException ex){
            ex.printStackTrace();
            System.out.println(" SQLException : " + ex.getMessage());
            System.out.println(" SQLState : " + ex.getSQLState());
            System.out.println(" VendorError : " + ex.getErrorCode());
        }
    }

    public static int getContadorDeHilos() {
        return contadorDeHilos;
    }

    public static int getDinero() {
        return dinero;
    }

    public static long getTiempoPrimeraLectura() {
        return tiempoPrimeraLectura;
    }
}
