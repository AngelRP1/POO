package com.mycompany.comm;

import com.mongodb.client.*;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Pattern;

public class Connection {
    private MongoClient client;
    private MongoDatabase database;

    public Connection(String uri) {
        this.client = MongoClients.create(uri);
        this.database = client.getDatabase("banco_digital");
    }

    public MongoDatabase getDatabase() {
        return database;}

    public String crearEjecutivo(String nombre, String fecha, String telefono, String usuario,
                                 String email, String contrasena, String rango, boolean esSubgerente) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // El número de cliente será el usuario en este caso
        String numeroCliente = usuario;

        Document nuevaCuenta = new Document()
                .append("nombre", nombre)
                .append("fecha_nacimiento", fecha)
                .append("numero_cliente", numeroCliente)
                .append("telefono", telefono)
                .append("email", email)
                .append("contrasena", contrasena)
                .append("saldo", 0.0)
                .append("tarjetas", Arrays.asList())  // Lista vacía de tarjetas
                .append("prestamos", Arrays.asList()) // Lista vacía de préstamos
                .append("acciones", Arrays.asList())  // Lista vacía de acciones
                .append("esSubgerente", esSubgerente);

        cuentas.insertOne(nuevaCuenta);
        return numeroCliente;
    }

    public String crearCuentaYObtenerNumero(String nombre, String fecha, String telefono,
                                            String email, String contrasena) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // Generamos el número de cliente de forma automática
        String numeroCliente = generarNumeroCliente();

        Document nuevaCuenta = new Document()
                .append("nombre", nombre)
                .append("fecha_nacimiento", fecha)
                .append("numero_cliente", numeroCliente)
                .append("telefono", telefono)
                .append("email", email)
                .append("contrasena", contrasena)
                .append("saldo", 0.0)
                .append("tarjetas", Arrays.asList())  // Lista vacía de tarjetas
                .append("prestamos", Arrays.asList()) // Lista vacía de préstamos
                .append("acciones", Arrays.asList())  // Lista vacía de acciones
                .append("esSubgerente", false); // Asignamos valor por defecto a 'esSubgerente'

        cuentas.insertOne(nuevaCuenta);
        return numeroCliente;
    }

    public boolean crearCuenta(String nombre, String rfcCurp, String telefono,
                               String email, String contrasena) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // Generamos el número de cliente de forma automática
        String numeroCliente = generarNumeroCliente();

        Document nuevaCuenta = new Document()
                .append("nombre", nombre)
                .append("fecha_nacimiento", rfcCurp) // Usamos el RFC/Curp como fecha de nacimiento
                .append("numero_cliente", numeroCliente)
                .append("telefono", telefono)
                .append("email", email)
                .append("contrasena", contrasena)
                .append("saldo", 0.0)
                .append("tarjetas", Arrays.asList())  // Lista vacía de tarjetas
                .append("prestamos", Arrays.asList()) // Lista vacía de préstamos
                .append("acciones", Arrays.asList())  // Lista vacía de acciones
                .append("esSubgerente", false); // Asignamos valor por defecto a 'esSubgerente'

        cuentas.insertOne(nuevaCuenta);
        return true;
    }

    public boolean agregarTarjeta(String numeroCliente, String tipo,
                                  String numeroTarjeta, String nip,
                                  String fechaVencimiento, double limite) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // Obtener la cuenta para verificar saldo (si es débito)
        Document cuenta = cuentas.find(Filters.eq("numero_cliente", numeroCliente)).first();
        if (cuenta == null) return false;

        // Configurar propiedades según el tipo de tarjeta
        boolean esCredito = tipo.equalsIgnoreCase("crédito");
        double saldoDisponible = esCredito ? limite : cuenta.getDouble("saldo");

        Document tarjeta = new Document()
                .append("tipo", tipo.toLowerCase())
                .append("numero", numeroTarjeta)
                .append("nip", nip)
                .append("fecha_vencimiento", fechaVencimiento)
                .append("limite", esCredito ? limite : 0)
                .append("saldo_disponible", saldoDisponible)
                .append("fecha_creacion", LocalDateTime.now().toString());

        Bson filtro = Filters.eq("numero_cliente", numeroCliente);
        Bson actualizacion = Updates.push("tarjetas", tarjeta);

        UpdateResult result = cuentas.updateOne(filtro, actualizacion);
        return result.getModifiedCount() > 0;
    }

    // Métodos para transacciones - CORREGIDO nombre de variable
    public boolean pagarServicio(String numeroCliente, String numeroTarjeta,
                                 String servicio, String referencia, double monto) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        if (!verificarSaldoTarjeta(numeroCliente, numeroTarjeta, monto)) {
            return false;
        }

        Document accion = new Document()
                .append("tipo", "pago_servicio")
                .append("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .append("servicio", servicio)
                .append("referencia", referencia)
                .append("monto", monto)
                .append("tarjeta_usada", numeroTarjeta);

        Bson filtro = Filters.and(
                Filters.eq("numero_cliente", numeroCliente), // Corregido de "numero_cliente"
                Filters.eq("tarjetas.numero", numeroTarjeta)
        );

        Bson actualizacionSaldo;
        if (obtenerTipoTarjeta(numeroCliente, numeroTarjeta).equalsIgnoreCase("crédito")) {
            actualizacionSaldo = Updates.inc("tarjetas.$.saldo_disponible", -monto);
        } else {
            actualizacionSaldo = Updates.combine(
                    Updates.inc("saldo", -monto),
                    Updates.inc("tarjetas.$.saldo_disponible", -monto)
            );
        }

        Bson actualizacionAccion = Updates.push("acciones", accion);

        UpdateResult result = cuentas.updateOne(filtro,
                Updates.combine(actualizacionSaldo, actualizacionAccion));

        return result.getModifiedCount() > 0;
    }

    // Métodos para transferencias - CORREGIDO nombres de variables y lógica
    public boolean transferir(String origenCliente, String origenTarjeta,
                              String destinoCliente, double monto, String concepto) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // Verificar saldo en cuenta origen
        if (!verificarSaldoTarjeta(origenCliente, origenTarjeta, monto)) {
            return false;
        }

        // Obtener nombre del titular de la cuenta origen
        String nombreOrigen = obtenerNombreCliente(origenCliente);
        if (nombreOrigen.isEmpty()) {
            return false;
        }

        // Crear acciones para ambas cuentas
        LocalDateTime ahora = LocalDateTime.now();
        String fecha = ahora.format(DateTimeFormatter.ISO_DATE_TIME);

        Document accionOrigen = new Document()
                .append("tipo", "transferencia_enviada")
                .append("fecha", fecha)
                .append("monto", monto)
                .append("cuenta_destino", destinoCliente)
                .append("concepto", concepto)
                .append("tarjeta_usada", origenTarjeta);

        Document accionDestino = new Document()
                .append("tipo", "transferencia_recibida")
                .append("fecha", fecha)
                .append("monto", monto)
                .append("cuenta_origen", origenCliente)
                .append("nombre_origen", nombreOrigen)
                .append("concepto", concepto);

        // Actualizar saldos y agregar acciones
        try {
            // 1. Restar de cuenta origen
            Bson filtroOrigen = Filters.and(
                    Filters.eq("numero_cliente", origenCliente),
                    Filters.eq("tarjetas.numero", origenTarjeta)
            );

            Bson actualizacionOrigen;
            if (obtenerTipoTarjeta(origenCliente, origenTarjeta).equalsIgnoreCase("crédito")) {
                actualizacionOrigen = Updates.combine(
                        Updates.inc("tarjetas.$.saldo_disponible", -monto),
                        Updates.push("acciones", accionOrigen)
                );
            } else {
                actualizacionOrigen = Updates.combine(
                        Updates.inc("saldo", -monto),
                        Updates.inc("tarjetas.$.saldo_disponible", -monto),
                        Updates.push("acciones", accionOrigen)
                );
            }

            // 2. Sumar a cuenta destino
            Bson filtroDestino = Filters.eq("numero_cliente", destinoCliente);
            Bson actualizacionDestino = Updates.combine(
                    Updates.inc("saldo", monto),
                    Updates.push("acciones", accionDestino)
            );

            // Ejecutar actualizaciones
            cuentas.updateOne(filtroOrigen, actualizacionOrigen);
            cuentas.updateOne(filtroDestino, actualizacionDestino);

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // Métodos para préstamos - CORREGIDO estructura del documento
    /**
     * Genera un ID de préstamo único con formato PR + número aleatorio
     * Verifica en la base de datos que no exista antes de devolverlo
     */
    public String generarIdPrestamoUnico() {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        String idPrestamo;
        Random rand = new Random();

        do {
            idPrestamo = "PR" + (100000 + rand.nextInt(900000)); // PR + 6 dígitos
        } while (cuentas.countDocuments(Filters.eq("prestamos.id_prestamo", idPrestamo)) > 0);

        return idPrestamo;
    }

    /**
     * Solicita un nuevo préstamo con ID único garantizado
     */
    public boolean solicitarPrestamo(String numeroCliente, double monto,
                                     double tasaInteres, int plazoMeses) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        String idPrestamo = generarIdPrestamoUnico();
        LocalDateTime fechaInicio = LocalDateTime.now();
        LocalDateTime fechaVencimiento = fechaInicio.plusMonths(plazoMeses);

        Document prestamo = new Document()
                .append("id_prestamo", idPrestamo)
                .append("monto", monto)
                .append("saldo_pendiente", monto)
                .append("tasa_interes", tasaInteres)
                .append("fecha_inicio", fechaInicio.toString())
                .append("fecha_vencimiento", fechaVencimiento.toString())
                .append("estado", "activo");

        Bson filtro = Filters.eq("numero_cliente", numeroCliente);
        Bson actualizacion = Updates.push("prestamos", prestamo);

        UpdateResult result = cuentas.updateOne(filtro, actualizacion);
        return result.getModifiedCount() > 0;
    }

    /**
     * Paga un préstamo existente verificando su ID único
     */
    public boolean pagarPrestamo(String numeroCliente, String idPrestamo,
                                 double monto, String metodoPago) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // Verificar que el préstamo existe
        if (cuentas.countDocuments(
                Filters.and(
                        Filters.eq("numero_cliente", numeroCliente),
                        Filters.eq("prestamos.id_prestamo", idPrestamo)
                )) == 0) {
            return false;
        }

        // Lógica de pago...
        Bson filtro = Filters.and(
                Filters.eq("numero_cliente", numeroCliente),
                Filters.eq("prestamos.id_prestamo", idPrestamo)
        );

        Bson actualizacion = Updates.combine(
                Updates.inc("prestamos.$.saldo_pendiente", -monto),
                Updates.set("prestamos.$.estado",
                        new Document("$cond", Arrays.asList(
                                new Document("$lte", Arrays.asList("$prestamos.$.saldo_pendiente", 0)),
                                "pagado",
                                "activo"
                        ))
                )
        );

        UpdateResult result = cuentas.updateOne(filtro, actualizacion);
        return result.getModifiedCount() > 0;
    }


    // Métodos auxiliares 
    private boolean cuentaExiste(String rfcCurp, String email) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Bson filtro = Filters.or(
                Filters.eq("rfc_curp", rfcCurp),
                Filters.eq("email", email)
        );
        return cuentas.countDocuments(filtro) > 0;
    }
    
        // Métodos auxiliares 
    private boolean tarjetaExiste(String rfcCurp, String email) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Bson filtro = Filters.or(
                Filters.eq("fecha_nacimiento", rfcCurp),
                Filters.eq("email", email)
        );
        return cuentas.countDocuments(filtro) > 0;
    }


    private String generarNumeroCliente() {
        return String.format("%010d", new Random().nextInt(1_000_000_000));
    }

    private boolean verificarSaldoTarjeta(String numeroCliente, String numeroTarjeta, double monto) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.and(
                        Filters.eq("numero_cliente", numeroCliente),
                        Filters.eq("tarjetas.numero", numeroTarjeta)
                )).first();

        if (cuenta == null) return false;

        for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
            if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                if ("crédito".equalsIgnoreCase(tarjeta.getString("tipo"))) {
                    return tarjeta.getDouble("saldo_disponible") >= monto;
                } else {
                    return cuenta.getDouble("saldo") >= monto;
                }
            }
        }
        return false;
    }

    private String obtenerTipoTarjeta(String numeroCliente, String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.and(
                        Filters.eq("numero_cliente", numeroCliente),
                        Filters.eq("tarjetas.numero", numeroTarjeta)
                )).first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    return tarjeta.getString("tipo");
                }
            }
        }
        return "";
    }

    private String obtenerNombreCliente(String numeroCliente) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("numero_cliente", numeroCliente))
                .first();
        return cuenta != null ? cuenta.getString("nombre") : "";
    }

    private boolean verificarTarjetaPerteneceACliente(String numeroCliente, String numeroTarjeta) {
        return database.getCollection("cuentas")
                .countDocuments(Filters.and(
                        Filters.eq("numero_cliente", numeroCliente),
                        Filters.eq("tarjetas.numero", numeroTarjeta)
                )) > 0;
    }
    
        // Método para obtener los préstamos de un cliente
    public List<Document> obtenerPrestamos(String numeroCliente) {
        Document cuenta = database.getCollection("cuentas")
            .find(Filters.eq("numero_cliente", numeroCliente))
            .first();

        if (cuenta != null) {
            return cuenta.getList("prestamos", Document.class);
        }
        return null;
    }

    // Método para actualizar saldos de forma sincronizada
    private boolean actualizarSaldosSincronizados(String numeroCliente, double nuevoSaldo) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Actualizar saldo de la cuenta
            Bson filtroCuenta = Filters.eq("numero_cliente", numeroCliente);
            Bson actualizacionCuenta = Updates.set("saldo", nuevoSaldo);
            cuentas.updateOne(filtroCuenta, actualizacionCuenta);

            // 2. Actualizar saldo disponible en todas las tarjetas de débito
            Bson filtroTarjetas = Filters.and(
                    Filters.eq("numero_cliente", numeroCliente),
                    Filters.eq("tarjetas.tipo", "débito")
            );

            Bson actualizacionTarjetas = Updates.set("tarjetas.$[elem].saldo_disponible", nuevoSaldo);

            UpdateOptions options = new UpdateOptions().arrayFilters(
                    Arrays.asList(Filters.eq("elem.tipo", "débito"))
            );

            cuentas.updateMany(
                    filtroTarjetas,
                    actualizacionTarjetas,
                    options
            );

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean depositarACuenta(String numeroCliente, double monto, String concepto) {
        // Validaciones básicas
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "deposito_cuenta")
                    .append("fecha", LocalDateTime.now().toString())
                    .append("monto", monto)
                    .append("concepto", concepto)
                    .append("metodo", "directo");

            // 2. Actualizar saldos sincronizados (cuenta + tarjetas débito)
            Bson filtro = Filters.eq("numero_cliente", numeroCliente);
            Document cuenta = cuentas.find(filtro).first();

            if (cuenta == null) return false;

            double nuevoSaldo = cuenta.getDouble("saldo") + monto;

            // Actualización atómica
            Bson actualizacion = Updates.combine(
                    Updates.set("saldo", nuevoSaldo),
                    Updates.set("tarjetas.$[elem].saldo_disponible", nuevoSaldo),
                    Updates.push("acciones", transaccion)
            );

            UpdateOptions options = new UpdateOptions().arrayFilters(
                    Arrays.asList(Filters.eq("elem.tipo", "débito"))
            );

            UpdateResult result = cuentas.updateOne(filtro, actualizacion, options);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en depósito a cuenta: " + e.getMessage());
            return false;
        }
    }
    public boolean depositarATarjetaDebito(String numeroTarjeta, double monto, String concepto) {
        // Validaciones básicas
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Verificar que la tarjeta existe y es de débito
            Document tarjetaInfo = obtenerInformacionCompletaTarjeta(numeroTarjeta);
            if (tarjetaInfo == null || !"débito".equalsIgnoreCase(tarjetaInfo.getString("tipo_tarjeta"))) {
                return false;
            }

            String numeroCliente = tarjetaInfo.getString("numero_cliente");

            // 2. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "deposito_debito")
                    .append("fecha", LocalDateTime.now().toString())
                    .append("monto", monto)
                    .append("concepto", concepto)
                    .append("tarjeta", numeroTarjeta);

            // 3. Actualizar saldos (igual que depositarACuenta)
            Bson filtro = Filters.eq("numero_cliente", numeroCliente);
            Document cuenta = cuentas.find(filtro).first();

            if (cuenta == null) return false;

            double nuevoSaldo = cuenta.getDouble("saldo") + monto;

            Bson actualizacion = Updates.combine(
                    Updates.set("saldo", nuevoSaldo),
                    Updates.set("tarjetas.$[elem].saldo_disponible", nuevoSaldo),
                    Updates.push("acciones", transaccion)
            );

            UpdateOptions options = new UpdateOptions().arrayFilters(
                    Arrays.asList(Filters.eq("elem.tipo", "débito"))
            );

            UpdateResult result = cuentas.updateOne(filtro, actualizacion, options);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en depósito a débito: " + e.getMessage());
            return false;
        }
    }
    public boolean abonarATarjetaCredito(String numeroTarjeta, double monto, String concepto) {
        // Validaciones básicas
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Verificar que la tarjeta existe y es de crédito
            Document tarjetaInfo = obtenerInformacionCompletaTarjeta(numeroTarjeta);
            if (tarjetaInfo == null || !"crédito".equalsIgnoreCase(tarjetaInfo.getString("tipo_tarjeta"))) {
                return false;
            }

            String numeroCliente = tarjetaInfo.getString("numero_cliente");

            // 2. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "abono_credito")
                    .append("fecha", LocalDateTime.now().toString())
                    .append("monto", monto)
                    .append("concepto", concepto)
                    .append("tarjeta", numeroTarjeta)
                    .append("saldo_anterior", tarjetaInfo.getDouble("saldo_disponible"));

            // 3. Actualizar solo el saldo disponible de esta tarjeta
            Bson filtro = Filters.and(
                    Filters.eq("numero_cliente", numeroCliente),
                    Filters.eq("tarjetas.numero", numeroTarjeta)
            );

            Bson actualizacion = Updates.combine(
                    Updates.inc("tarjetas.$.saldo_disponible", monto),
                    Updates.push("acciones", transaccion)
            );

            UpdateResult result = cuentas.updateOne(filtro, actualizacion);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en abono a crédito: " + e.getMessage());
            return false;
        }
    }
    public boolean transferirDesdeTarjeta(String numeroTarjeta, String numeroCuentaDestino, double monto, String concepto) {
        // Validar que la tarjeta es de débito y tiene fondos
        Document infoTarjeta = obtenerInformacionCompletaTarjeta(numeroTarjeta);
        if (infoTarjeta == null || !"débito".equalsIgnoreCase(infoTarjeta.getString("tipo"))) {
            return false;
        }

        String numeroClienteOrigen = infoTarjeta.getString("numero_cliente");
        double saldoDisponible = infoTarjeta.getDouble("saldo_disponible");

        if (saldoDisponible < monto) {
            return false;
        }

        // Realizar transferencia
        boolean exitoRetiro = retirarDeTarjeta(numeroTarjeta, monto, "Transferencia a cuenta " + numeroCuentaDestino);
        boolean exitoDeposito = depositarACuenta(numeroCuentaDestino, monto, "Transferencia desde tarjeta " + numeroTarjeta);

        if (exitoRetiro && exitoDeposito) {
            registrarTransferencia(numeroClienteOrigen, numeroTarjeta, numeroCuentaDestino, monto, concepto, "tarjeta_a_cuenta");
            return true;
        }
        return false;
    }
    public boolean retirarDeCuenta(String numeroCliente, double monto, String concepto) {
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Verificar saldo suficiente
            Document cuenta = cuentas.find(Filters.eq("numero_cliente", numeroCliente)).first();
            if (cuenta == null || cuenta.getDouble("saldo") < monto) {
                return false;
            }

            // 2. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "retiro_cuenta")
                    .append("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .append("monto", monto)
                    .append("concepto", concepto)
                    .append("metodo", "directo");

            // 3. Actualizar saldos (cuenta + tarjetas débito)
            Bson filtro = Filters.eq("numero_cliente", numeroCliente);
            double nuevoSaldo = cuenta.getDouble("saldo") - monto;

            Bson actualizacion = Updates.combine(
                    Updates.set("saldo", nuevoSaldo),
                    Updates.set("tarjetas.$[elem].saldo_disponible", nuevoSaldo),
                    Updates.push("acciones", transaccion)
            );

            UpdateOptions options = new UpdateOptions().arrayFilters(
                    Arrays.asList(Filters.eq("elem.tipo", "débito"))
            );

            UpdateResult result = cuentas.updateOne(filtro, actualizacion, options);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en retiro de cuenta: " + e.getMessage());
            return false;
        }
    }

    public boolean retirarDeTarjeta(String numeroTarjeta, double monto, String concepto) {
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Obtener información de la tarjeta
            Document tarjetaInfo = obtenerInformacionCompletaTarjeta(numeroTarjeta);
            if (tarjetaInfo == null) {
                return false;
            }

            String numeroCliente = tarjetaInfo.getString("numero_cliente");
            String tipoTarjeta = tarjetaInfo.getString("tipo");
            double saldoDisponible = tarjetaInfo.getDouble("saldo_disponible");

            // 2. Verificar fondos según tipo de tarjeta
            if ("débito".equalsIgnoreCase(tipoTarjeta) && saldoDisponible < monto) {
                return false;
            } else if ("crédito".equalsIgnoreCase(tipoTarjeta) &&
                    (tarjetaInfo.getDouble("limite") - saldoDisponible) < monto) {
                return false;
            }

            // 3. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "retiro_tarjeta")
                    .append("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                    .append("monto", monto)
                    .append("concepto", concepto)
                    .append("tarjeta", numeroTarjeta)
                    .append("saldo_anterior", saldoDisponible);

            // 4. Actualizar saldos
            Bson filtro = Filters.and(
                    Filters.eq("numero_cliente", numeroCliente),
                    Filters.eq("tarjetas.numero", numeroTarjeta)
            );

            Bson actualizacion;
            if ("débito".equalsIgnoreCase(tipoTarjeta)) {
                // Para débito, actualizar cuenta y tarjeta
                actualizacion = Updates.combine(
                        Updates.inc("saldo", -monto),
                        Updates.inc("tarjetas.$.saldo_disponible", -monto),
                        Updates.push("acciones", transaccion)
                );
            } else {
                // Para crédito, solo actualizar tarjeta
                actualizacion = Updates.combine(
                        Updates.inc("tarjetas.$.saldo_disponible", -monto),
                        Updates.push("acciones", transaccion)
                );
            }

            UpdateResult result = cuentas.updateOne(filtro, actualizacion);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en retiro de tarjeta: " + e.getMessage());
            return false;
        }
    }

    // Método auxiliar para obtener información completa de una
    public boolean validarNumeroCuenta(String numeroCuenta) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Document cuenta = cuentas.find(Filters.eq("numero_cliente", numeroCuenta)).first();
        return cuenta != null;
    }

    /**
     * Valida si un número de tarjeta existe en la base de datos
     * @param numeroTarjeta Número de tarjeta a validar
     * @return true si la tarjeta existe, false en caso contrario
     */
    public boolean validarNumeroTarjeta(String numeroTarjeta) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Document cuenta = cuentas.find(Filters.eq("tarjetas.numero", numeroTarjeta)).first();
        return cuenta != null;
    }

    /**
     * Obtiene información completa de una tarjeta
     * @param numeroTarjeta Número de tarjeta a buscar
     * @return Document con la información de la tarjeta o null si no existe
     */
    public Document obtenerInformacionTarjeta(String numeroTarjeta) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        Document cuenta = cuentas.find(Filters.eq("tarjetas.numero", numeroTarjeta)).first();
        if (cuenta == null) return null;

        List<Document> tarjetas = cuenta.getList("tarjetas", Document.class);
        for (Document tarjeta : tarjetas) {
            if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                Document info = new Document(tarjeta);
                info.append("numero_cliente", cuenta.getString("numero_cliente"));
                return info;
            }
        }
        return null;
    }

    /**
     * Transfiere fondos entre cuentas
     * @param cuentaOrigen Número de cuenta origen
     * @param cuentaDestino Número de cuenta destino
     * @param monto Monto a transferir
     * @param concepto Concepto de la transferencia
     * @return true si la transferencia fue exitosa
     */
    public boolean transferirEntreCuentas(String cuentaOrigen, String cuentaDestino, double monto, String concepto) {
        if (!validarNumeroCuenta(cuentaOrigen) || !validarNumeroCuenta(cuentaDestino)) {
            return false;
        }

        // Verificar fondos en cuenta origen
        Document origen = database.getCollection("cuentas")
                .find(Filters.eq("numero_cliente", cuentaOrigen))
                .first();

        if (origen == null || origen.getDouble("saldo") < monto) {
            return false;
        }

        // Realizar transferencia (retiro + depósito)
        boolean retiroExitoso = retirarDeCuenta(cuentaOrigen, monto, "Transferencia a " + cuentaDestino);
        boolean depositoExitoso = depositarACuenta(cuentaDestino, monto, "Transferencia de " + cuentaOrigen);

        return retiroExitoso && depositoExitoso;
    }
    public boolean transferirATarjeta(String numeroCuentaOrigen, String numeroTarjetaDestino, double monto, String concepto) {
        // Validar que la tarjeta destino existe
        Document infoTarjeta = obtenerInformacionCompletaTarjeta(numeroTarjetaDestino);
        if (infoTarjeta == null) {
            return false;
        }

        // Validar que la cuenta origen tiene fondos
        Document cuentaOrigen = database.getCollection("cuentas")
                .find(Filters.eq("numero_cliente", numeroCuentaOrigen))
                .first();

        if (cuentaOrigen == null || cuentaOrigen.getDouble("saldo") < monto) {
            return false;
        }

        // Realizar transferencia
        boolean exitoRetiro = retirarDeCuenta(numeroCuentaOrigen, monto, "Transferencia a tarjeta " + numeroTarjetaDestino);
        boolean exitoDeposito;

        if ("débito".equalsIgnoreCase(infoTarjeta.getString("tipo"))) {
            exitoDeposito = depositarATarjetaDebito(numeroTarjetaDestino, monto, "Transferencia desde cuenta " + numeroCuentaOrigen);
        } else {
            exitoDeposito = abonarATarjetaCredito(numeroTarjetaDestino, monto, "Transferencia desde cuenta " + numeroCuentaOrigen);
        }

        if (exitoRetiro && exitoDeposito) {
            registrarTransferencia(numeroCuentaOrigen, numeroCuentaOrigen, numeroTarjetaDestino, monto, concepto, "cuenta_a_tarjeta");
            return true;
        }
        return false;
    }

    public boolean transferirEntreTarjetas(String numeroTarjetaOrigen, String numeroTarjetaDestino, double monto, String concepto) {
        // Validar tarjetas
        Document infoTarjetaOrigen = obtenerInformacionCompletaTarjeta(numeroTarjetaOrigen);
        Document infoTarjetaDestino = obtenerInformacionCompletaTarjeta(numeroTarjetaDestino);

        if (infoTarjetaOrigen == null || infoTarjetaDestino == null) {
            return false;
        }

        // Validar que la tarjeta origen es débito y tiene fondos
        if (!"débito".equalsIgnoreCase(infoTarjetaOrigen.getString("tipo")) ||
                infoTarjetaOrigen.getDouble("saldo_disponible") < monto) {
            return false;
        }

        String numeroClienteOrigen = infoTarjetaOrigen.getString("numero_cliente");

        // Realizar transferencia
        boolean exitoRetiro = retirarDeTarjeta(numeroTarjetaOrigen, monto, "Transferencia a tarjeta " + numeroTarjetaDestino);
        boolean exitoDeposito;

        if ("débito".equalsIgnoreCase(infoTarjetaDestino.getString("tipo"))) {
            exitoDeposito = depositarATarjetaDebito(numeroTarjetaDestino, monto, "Transferencia desde tarjeta " + numeroTarjetaOrigen);
        } else {
            exitoDeposito = abonarATarjetaCredito(numeroTarjetaDestino, monto, "Transferencia desde tarjeta " + numeroTarjetaOrigen);
        }

        if (exitoRetiro && exitoDeposito) {
            registrarTransferencia(numeroClienteOrigen, numeroTarjetaOrigen, numeroTarjetaDestino, monto, concepto, "tarjeta_a_tarjeta");
            return true;
        }
        return false;
    }

    private void registrarTransferencia(String clienteOrigen, String origen, String destino, double monto, String concepto, String tipo) {
        MongoCollection<Document> acciones = database.getCollection("acciones");

        Document accion = new Document()
                .append("tipo", "transferencia")
                .append("subtipo", tipo)
                .append("fecha", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME))
                .append("origen", origen)
                .append("destino", destino)
                .append("monto", monto)
                .append("concepto", concepto)
                .append("cliente_origen", clienteOrigen)
                .append("estado", "completada");

        acciones.insertOne(accion);
    }
    // Método para obtener el historial de acciones
    public List<Document> obtenerHistorial(String numeroCliente) {
        Document cuenta = database.getCollection("cuentas")
            .find(Filters.eq("numero_cliente", numeroCliente))
            .first();

        if (cuenta != null) {
            return cuenta.getList("acciones", Document.class);
        }
        return null;
    }

    public void cerrarConexion() {
        if (client != null) {
            client.close();
        }
    }

    public boolean tarjetaExisteEnBanco(String numeroTarjeta) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Bson filtro = Filters.eq("tarjetas.numero", numeroTarjeta);
        return cuentas.countDocuments(filtro) > 0;
    }

    public String obtenerNombrePorTarjeta(String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        return cuenta != null ? cuenta.getString("nombre") : "";
    }

    public String obtenerFechaVencimientoTarjeta(String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    return tarjeta.getString("fecha_vencimiento");
                }
            }
        }
        return "";
    }

    public String obtenerTipoTarjeta(String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    return tarjeta.getString("tipo");
                }
            }
        }
        return "";
    }

    public boolean verificarNIP(String numeroTarjeta, String nip) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    return tarjeta.getString("nip").equals(nip);
                }
            }
        }
        return false;
    }

    public double obtenerSaldoTarjeta(String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    return tarjeta.getDouble("saldo_disponible");
                }
            }
        }
        return -1;
    }

    public Document obtenerInformacionCompletaTarjeta(String numeroTarjeta) {
        Document cuenta = database.getCollection("cuentas")
                .find(Filters.eq("tarjetas.numero", numeroTarjeta))
                .first();

        if (cuenta != null) {
            for (Document tarjeta : cuenta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                    // Crear un documento con toda la información relevante
                    return new Document()
                            .append("nombre_cliente", cuenta.getString("nombre"))
                            .append("numero_cliente", cuenta.getString("numero_cliente"))
                            .append("tipo_tarjeta", tarjeta.getString("tipo"))
                            .append("fecha_vencimiento", tarjeta.getString("fecha_vencimiento"))
                            .append("limite", tarjeta.getDouble("limite"))
                            .append("saldo_disponible", tarjeta.getDouble("saldo_disponible"));
                }
            }
        }
        return null;
    }

    public Object[] validarNumero(String numero) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        // 1. Buscar como número de cliente
        Document cuentaCliente = cuentas.find(Filters.eq("numero_cliente", numero)).first();
        if (cuentaCliente != null) {
            return new Object[]{
                    database,
                    cuentaCliente.getString("nombre"),
                    numero,
                    "cliente"
            };
        }

        // 2. Buscar como número de tarjeta
        Document cuentaTarjeta = cuentas.find(Filters.eq("tarjetas.numero", numero)).first();
        if (cuentaTarjeta != null) {
            for (Document tarjeta : cuentaTarjeta.getList("tarjetas", Document.class)) {
                if (tarjeta.getString("numero").equals(numero)) {
                    String tipo = tarjeta.getString("tipo").equals("crédito") ? "credito" : "debito";
                    return new Object[]{
                            database,
                            cuentaTarjeta.getString("nombre"),
                            numero,
                            tipo
                    };
                }
            }
        }

        // 3. Si no es válido
        return new Object[]{null, "", "", "invalido"};
    }

    public boolean retirarDeTarjetaDebito(String numeroTarjeta, double monto, String concepto) {
        if (monto <= 0) return false;

        MongoCollection<Document> cuentas = database.getCollection("cuentas");

        try {
            // 1. Verificar que la tarjeta existe y es de débito
            Document tarjetaInfo = obtenerInformacionCompletaTarjeta(numeroTarjeta);
            if (tarjetaInfo == null || !"débito".equalsIgnoreCase(tarjetaInfo.getString("tipo_tarjeta"))) {
                return false;
            }

            String numeroCliente = tarjetaInfo.getString("numero_cliente");

            // 2. Registrar la transacción
            Document transaccion = new Document()
                    .append("tipo", "retiro_debito")
                    .append("fecha", LocalDateTime.now().toString())
                    .append("monto", -monto)
                    .append("concepto", concepto)
                    .append("tarjeta", numeroTarjeta);

            // 3. Actualizar saldos (igual que depositarACuenta)
            Bson filtro = Filters.eq("numero_cliente", numeroCliente);
            Document cuenta = cuentas.find(filtro).first();

            if (cuenta == null) return false;

            double nuevoSaldo = cuenta.getDouble("saldo") - monto;

            Bson actualizacion = Updates.combine(
                    Updates.set("saldo", nuevoSaldo),
                    Updates.set("tarjetas.$[elem].saldo_disponible", nuevoSaldo),
                    Updates.push("acciones", transaccion)
            );

            UpdateOptions options = new UpdateOptions().arrayFilters(
                    Arrays.asList(Filters.eq("elem.tipo", "débito"))
            );

            UpdateResult result = cuentas.updateOne(filtro, actualizacion, options);
            return result.getModifiedCount() > 0;

        } catch (Exception e) {
            System.err.println("Error en depósito a débito: " + e.getMessage());
            return false;
        }
    }
    
    public double obtenerSaldoTarjetaDebito(String numeroTarjeta) {
        try {
            MongoCollection<Document> cuentas = database.getCollection("cuentas");

            // Buscar la cuenta que contiene la tarjeta
            Document cuenta = cuentas.find(Filters.eq("tarjetas.numero", numeroTarjeta)).first();

            if (cuenta != null) {
                // Buscar la tarjeta específica dentro del array de tarjetas
                List<Document> tarjetas = cuenta.getList("tarjetas", Document.class);
                for (Document tarjeta : tarjetas) {
                    if (tarjeta.getString("numero").equals(numeroTarjeta)) {
                        // Verificar que sea tarjeta de débito
                        if ("débito".equalsIgnoreCase(tarjeta.getString("tipo"))) {
                            return tarjeta.getDouble("saldo_disponible");
                        } else {
                            // Si no es débito, devolver -1 como código de error
                            return -1;
                        }
                    }
                }
            }
            // Si no se encuentra la tarjeta, devolver -2 como código de error
            return -2;
        } catch (Exception e) {
            e.printStackTrace();
            // En caso de error, devolver -3 como código de error
            return -3;
        }
    }

    // READ
    public List<Map<String, Object>> obtenerTodosClientes() {
        MongoCollection<Document> clientes = database.getCollection("clientes");
        List<Map<String, Object>> resultado = new ArrayList<>();

        for(Document doc : clientes.find()) {
            resultado.add(docToMap(doc));
        }

        return resultado;
    }

    public List<Map<String, Object>> buscarClientes(String query) {
        MongoCollection<Document> clientes = database.getCollection("clientes");
        List<Map<String, Object>> resultado = new ArrayList<>();

        Document filtro = new Document("$or", Arrays.asList(
                new Document("nombre", Pattern.compile(query, Pattern.CASE_INSENSITIVE)),
                new Document("numero_cliente", Pattern.compile(query, Pattern.CASE_INSENSITIVE))
        ));

        for(Document doc : clientes.find(filtro)) {
            resultado.add(docToMap(doc));
        }

        return resultado;
    }

    // UPDATE
    public boolean actualizarCliente(String numeroCliente, Map<String, Object> cambios) {
        MongoCollection<Document> clientes = database.getCollection("clientes");

        Document filtro = new Document("numero_cliente", numeroCliente);
        Document actualizacion = new Document("$set", new Document(cambios));

        UpdateResult result = clientes.updateOne(filtro, actualizacion);
        return result.getModifiedCount() > 0;
    }

    // DELETE
    public boolean eliminarCliente(String numeroCliente) {
        MongoCollection<Document> clientes = database.getCollection("clientes");
        DeleteResult result = clientes.deleteOne(new Document("numero_cliente", numeroCliente));
        return result.getDeletedCount() > 0;
    }


    private Map<String, Object> docToMap(Document doc) {
        Map<String, Object> map = new HashMap<>();
        for(String key : doc.keySet()) {
            map.put(key, doc.get(key));
        }
        return map;
    }

    public List<Map<String, Object>> obtenerClientesConTarjetas() {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        List<Map<String, Object>> resultado = new java.util.ArrayList<>();

        for (Document doc : cuentas.find()) {
            Map<String, Object> cliente = new java.util.HashMap<>();
            cliente.put("nombre", doc.getString("nombre"));
            cliente.put("numero_cliente", doc.getString("numero_cliente"));
            cliente.put("tarjetas", (List<Map<String, Object>>) doc.get("tarjetas", List.class));
            resultado.add(cliente);
        }
        return resultado;
    }

    public boolean eliminarTarjeta(String numeroCliente, String numeroTarjeta) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        Bson filtro = Filters.eq("numero_cliente", numeroCliente);
        Bson eliminar = Updates.pull("tarjetas", new Document("numero", numeroTarjeta));
        return cuentas.updateOne(filtro, eliminar).getModifiedCount() > 0;
    }

    public boolean eliminarCuenta(String numeroCliente) {
        MongoCollection<Document> cuentas = database.getCollection("cuentas");
        return cuentas.deleteOne(Filters.eq("numero_cliente", numeroCliente)).getDeletedCount() > 0;
    }


}