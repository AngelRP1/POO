package com.mycompany.comm;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import org.bson.Document;
import com.mycompany.comm.Connection;

public class Comm {
    public static void main(String[] args) {
        String uri = "mongodb://admin:1234@localhost:27017/";
        Connection banco = new Connection(uri);

        try {
            // Crear cuentas sin subgerente ni admin, con claves tipo "F4 C3 D4 FF"
            banco.crearEjecutivo("Juan Pérez", "01/01/1980", "5512345678", "F4 C3 D4 FF", "juan@example.com", "Pass123!", "Usuario", false);
            banco.crearEjecutivo("María Gómez", "12/03/1990", "5523456789", "A2 B1 C4 DD", "maria@example.com", "Pass123!", "Usuario", false);
            banco.crearEjecutivo("Luis Torres", "20/05/1985", "5534567890", "X1 Y2 Z3 AA", "luis@example.com", "Pass123!", "Usuario", false);
            banco.crearEjecutivo("Ana Rodríguez", "30/07/1992", "5545678901", "Q3 W4 E5 RR", "ana@example.com", "Pass123!", "Usuario", false);
            banco.crearEjecutivo("Carlos Ruiz", "18/10/1975", "5556789012", "J7 K8 L9 SS", "carlos@example.com", "Pass123!", "Usuario", false);

            banco.agregarTarjeta("F4 C3 D4 FF", "débito", "4111111111111111", "1234", "12/25", 0.0);
            banco.agregarTarjeta("A2 B1 C4 DD", "crédito", "5555555555554444", "4321", "10/26", 50000.0);
            banco.agregarTarjeta("X1 Y2 Z3 AA", "crédito", "5105105105105100", "9876", "09/27", 30000.0);
            banco.agregarTarjeta("Q3 W4 E5 RR", "débito", "4000056655665556", "5678", "11/26", 0.0);
            banco.agregarTarjeta("J7 K8 L9 SS", "crédito", "6011000990139424", "1122", "08/26", 15000.0);
            banco.crearEjecutivo("Luis Fernando Martínez",
                    "14/11/1990", "5555555550", "F1 D2 C3 G4", "luis@example.com", "luispass", "Usuario", false);
            banco.agregarTarjeta("F1 D2 C3 G4", "crédito", "4556737586899855", "2255", "11/27", 20000.0);

            banco.crearEjecutivo("María José González",
                    "30/06/1988", "5554443322", "A7 B8 C9 D0", "maria@example.com", "mariapass", "Usuario", false);
            banco.agregarTarjeta("A7 B8 C9 D0", "débito", "4716234567891234", "9988", "08/26", 0.0);

            banco.crearEjecutivo("Ricardo Ramírez Torres",
                    "03/03/1995", "5588776655", "Z1 Y2 X3 W4", "ricardo@example.com", "rickpass", "Usuario", false);
            banco.agregarTarjeta("Z1 Y2 X3 W4", "crédito", "6011556448578945", "1122", "01/29", 15000.0);

            banco.crearEjecutivo("Ana Laura Domínguez",
                    "19/12/1987", "5566778899", "K4 L5 M6 N7", "ana@example.com", "anapass", "Usuario", false);
            banco.agregarTarjeta("K4 L5 M6 N7", "débito", "4556123412341234", "4455", "10/28", 0.0);

            banco.crearEjecutivo("Carlos Alberto Reyes",
                    "21/08/1992", "5511223344", "X9 Y8 Z7 W6", "carlos@example.com", "carpass", "Usuario", false);
            banco.agregarTarjeta("X9 Y8 Z7 W6", "crédito", "5500000000000004", "5566", "07/30", 30000.0);

            banco.crearEjecutivo("Fernanda Ruiz Herrera",
                    "05/01/1991", "5599887766", "Q1 W2 E3 R4", "fernanda@example.com", "ferpass", "Usuario", false);
            banco.agregarTarjeta("Q1 W2 E3 R4", "débito", "4111111111111111", "1212", "03/26", 0.0);

            banco.crearEjecutivo("José Ángel Torres",
                    "27/04/1989", "5533445566", "T7 Y6 U5 I4", "jose@example.com", "josepass", "Usuario", false);
            banco.agregarTarjeta("T7 Y6 U5 I4", "crédito", "6011000990139424", "3434", "09/28", 10000.0);

            banco.crearEjecutivo("Daniela Castro Paredes",
                    "10/10/1994", "5544332211", "B3 N6 M2 Z8", "daniela@example.com", "danipass", "Usuario", false);
            banco.agregarTarjeta("B3 N6 M2 Z8", "débito", "4532756279624064", "9090", "05/27", 0.0);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            banco.cerrarConexion();
        }
    }
}
