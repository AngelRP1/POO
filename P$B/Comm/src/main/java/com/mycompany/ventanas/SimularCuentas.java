package com.mycompany.ventanas;

import com.mycompany.comm.Connection;

import java.util.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class SimularCuentas {
    private static final String[] NOMBRES = {
            "Ana Martínez", "Carlos Pérez", "Lucía Gómez", "Pedro Torres", "María Rodríguez",
            "Juan Herrera", "Laura Mendoza", "Diego Ramírez", "Sofía Vargas", "Andrés Castillo"
    };

    private static final String[] SERVICIOS = {
            "agua", "luz", "internet", "teléfono", "streaming"
    };

    private static final Random rand = new Random();

    public static void main(String[] args) {
        Connection conexion = new Connection("mongodb://admin:1234@localhost:27017/");

        for (int i = 0; i < 10; i++) {
            String nombre = NOMBRES[i];
            String fechaNacimiento = generarFechaNacimiento();
            String telefono = "555" + (100000 + rand.nextInt(900000));
            String email = nombre.toLowerCase().replace(" ", ".") + "@mail.com";
            String contrasena = "pass" + (100 + rand.nextInt(900));
            

            String numeroCliente = conexion.crearCuentaYObtenerNumero(
                    nombre, fechaNacimiento, telefono, email, contrasena);

            // Agregar 3 tarjetas (1 débito y 2 crédito)
            for (int j = 0; j < 3; j++) {
                String tipo = j == 0 ? "débito" : "crédito";
                String numeroTarjeta = "4000" + (10000000 + rand.nextInt(90000000));
                String nip = String.format("%03d", rand.nextInt(1000));
                String fechaVencimiento = generarFechaVencimiento();
                double limite = tipo.equals("crédito") ? (5000 + rand.nextInt(15000)) : 0;

                conexion.agregarTarjeta(numeroCliente, tipo, numeroTarjeta, nip, fechaVencimiento, limite);

                if (j == 0) {
                    // Usamos la tarjeta débito para registrar acciones
                    for (int k = 0; k < 5; k++) {
                        String servicio = SERVICIOS[k];
                        String referencia = "REF" + rand.nextInt(999999);
                        double monto = 100 + rand.nextInt(400); // monto entre 100 y 500

                        conexion.pagarServicio(numeroCliente, numeroTarjeta, servicio, referencia, monto);
                    }
                }
            }

            System.out.println("✓ Usuario '" + nombre + "' registrado con tarjetas y acciones.");
        }
    }

    private static String generarFechaNacimiento() {
        int year = 1980 + rand.nextInt(20); // entre 1980 y 1999
        int month = 1 + rand.nextInt(12);
        int day = 1 + rand.nextInt(28); // para evitar invalid dates
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    private static String generarFechaVencimiento() {
        int year = 2026 + rand.nextInt(5);
        int month = 1 + rand.nextInt(12);
        return String.format("%04d-%02d", year, month);
    }
}
