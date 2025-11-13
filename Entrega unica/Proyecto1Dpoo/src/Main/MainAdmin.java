package Main;

import java.util.*;
import java.io.*;
import java.time.*;
import java.security.SecureRandom;
import java.util.UUID;
import java.nio.file.Files;
import java.nio.file.Path;

import Enums.*;
import Eventos.*;
import Tiquetes.*;
import Transacciones.*;
import Reembolsos.*;
import Usuarios.*;
import log.EntradaLog;

public class MainAdmin {

    private static final Scanner in = new Scanner(System.in);
    private static final String DATA_DIR = "data";

    private static final Map<String, Usuario> usuariosByCorreo = new HashMap<>();
    private static final Map<String, Usuario> usuariosByLogin  = new HashMap<>();
    private static final List<Venue> venues = new ArrayList<>();
    private static final List<Evento> eventos = new ArrayList<>();
    private static final List<Tiquete> tiquetes = new ArrayList<>();
    private static final List<Transaccion> transacciones = new ArrayList<>();
    private static final List<Reembolso> reembolsos = new ArrayList<>();

    private static final List<Oferta> ofertas = new ArrayList<>();
    private static final List<EntradaLog> logRegistros = new ArrayList<>();

    private static final Set<String> allowlist = new HashSet<>(Arrays.asList(
        "admin.seguro@bm.com", "org.seguro@bm.com"
    ));

    private static Administrador ADMIN;



    public static void main(String[] args) {
        boolean cargado = loadSnapshot();
        if (!cargado) {
            seedDemo();
            snapshot();
        }

        sysoLines(
            "",
            
            "==========================================",
            "          BOLETAMASTER - ADMIN           ",
            "=========================================="
        );

        boolean exit = false;
        while (!exit) {
            sysoLines(
                "",
                "1) Registrarse como Administrador",
                "2) Iniciar sesión (Administrador)",
                "0) Salir"
            );
            int op = readInt(">> Opción: ");
            switch (op) {
                case 1 -> registrarAdmin();
                case 2 -> {
                    Administrador a = loginAdmin();
                    if (a != null) menuAdmin(a);
                }
                case 0 -> exit = true;
                default -> System.out.println("Opción inválida");
            }
        }
        System.out.println("Adiós");
    }



    private static void registrarAdmin() {
        sysoLines("", "====== REGISTRO ADMINISTRADOR ======");
        String nombre = readString(">> Nombre completo: ");
        String correo = readString(">> Correo: ");
        if (usuariosByCorreo.containsKey(correo)) {
            System.out.println("Correo ya registrado");
            return;
        }
        if (!allowlist.contains(correo)) {
            System.out.println("Correo no preaprobado para ADMIN");
            return;
        }

        String login = readString(">> Login (usuario): ");
        if (usuariosByLogin.containsKey(login)) {
            System.out.println("Login ya existente");
            return;
        }
        String pwd = readPassword(">> Contraseña: ");

        String id = UUID.randomUUID().toString();
        String hashCompat = Integer.toHexString(pwd.hashCode()); // hash simple demo

        Administrador nuevo = new Administrador(id, nombre, correo, login, hashCompat);

        usuariosByCorreo.put(correo, nuevo);
        usuariosByLogin.put(login, nuevo);
        ADMIN = nuevo;

        System.out.println("Registro exitoso: " + nombre + " [Administrador]");
        snapshot();
    }

    private static Administrador loginAdmin() {
        sysoLines("", "=========== LOGIN ADMIN ===========");
        String id = readString(">> Login (correo o usuario): ");
        String pwd = readPassword(">> Contraseña: ");

        Usuario u = id.contains("@") ? usuariosByCorreo.get(id) : usuariosByLogin.get(id);
        if (u == null) {
            System.out.println("No existe ese usuario");
            return null;
        }
        if (!u.validarPassword(pwd)) {
            System.out.println("Credenciales inválidas");
            return null;
        }
        if (!(u instanceof Administrador a)) {
            System.out.println("Este ejecutable es solo para ADMINISTRADORES.");
            return null;
        }

        String pin = generarPIN();
        System.out.println("[2FA simulado] PIN: " + pin);
        String ing = readString(">> Ingresa PIN: ");
        if (!pin.equals(ing)) {
            System.out.println("PIN incorrecto");
            return null;
        }

        System.out.println("Bienvenid@ " + a.getName() + " (Administrador)");
        ADMIN = a;
        return a;
    }



    private static void menuAdmin(Administrador a) {
        boolean back = false;
        while (!back) {
            sysoLines(
                "",
                "------------------------------------------",
                "                MENÚ ADMIN",
                "------------------------------------------",
                "1) Fijar tarifas",
                "2) Aprobar venues",
                "3) Cancelar evento",
                "4) Resolver reembolsos",
                "5) Ver log del sistema",
                "6) Eliminar oferta Marketplace",
                "0) Cerrar sesión"
            );
            int op = readInt(">> Opción: ");
            try {
                switch (op) {
                    case 1 -> flujoFijarTarifas(a);
                    case 2 -> flujoAprobarVenues();
                    case 3 -> flujoCancelarEvento();
                    case 4 -> flujoResolverReembolsos(a);
                    case 5 -> verLog(a);
                    case 6 -> flujoEliminarOfertaComoAdmin(a);
                    case 0 -> back = true;
                    default -> System.out.println("Opción inválida");
                }
            } catch (Exception e) {
                System.out.println("Error: " + e.getMessage());
            }
        }
    }



    private static void flujoFijarTarifas(Administrador a) {
        double pgen = readDouble(">> % servicio general (0..1): ");
        a.setPorcentajeServicioGeneral(pgen);
        for (TipoDeEvento t : TipoDeEvento.values()) {
            String resp = readString(">> ¿Fijar % específico para " + t + "? (s/n): ");
            if (resp.equalsIgnoreCase("s")) {
                double p = readDouble(">> % (0..1): ");
                a.fijarPorcentajeServicio(t, p);
            }
        }
        double emision = readDouble(">> Costo de emisión fijo: ");
        a.setCostoEmisionFijo(emision);
        System.out.println("Tarifas actualizadas");
        snapshot();
    }

    private static void flujoAprobarVenues() {
        List<Venue> pend = new ArrayList<>();
        for (Venue v : venues) if (!v.isAprobado()) pend.add(v);
        if (pend.isEmpty()) {
            System.out.println("No hay venues pendientes");
            return;
        }
        System.out.println("Venues pendientes:");
        for (int i = 0; i < pend.size(); i++) {
            System.out.println("(" + i + ") " + pend.get(i).getNombre() +
                               " - capMax=" + pend.get(i).getCapacidadMaxima());
        }
        int iap = readInt(">> Índice a aprobar: ");
        if (iap < 0 || iap >= pend.size()) {
            System.out.println("Índice inválido");
            return;
        }
        pend.get(iap).setAprobado(true);
        System.out.println("Venue aprobado");
        snapshot();
    }

    private static void flujoCancelarEvento() {
        if (eventos.isEmpty()) {
            System.out.println("No hay eventos");
            return;
        }
        System.out.println("Eventos existentes:");
        for (int i = 0; i < eventos.size(); i++) {
            Evento e = eventos.get(i);
            System.out.println("(" + i + ") " + e.getNombre() +
                               " (cancelado=" + e.isCancelado() + ")");
        }
        int ic = readInt(">> Índice a cancelar: ");
        if (ic < 0 || ic >= eventos.size()) {
            System.out.println("Índice inválido");
            return;
        }
        eventos.get(ic).setCancelado(true);
        System.out.println("Evento cancelado");
        snapshot();
    }

    private static void flujoResolverReembolsos(Administrador a) {
        if (reembolsos.isEmpty()) {
            System.out.println("No hay reembolsos");
            return;
        }
        System.out.println("Reembolsos registrados:");
        for (int i = 0; i < reembolsos.size(); i++) {
            Reembolso r = reembolsos.get(i);
            System.out.println("(" + i + ") " + r.getId() + " " + r.getMotivo() +
                               " estado=" + r.getEstado());
        }
        int ir = readInt(">> Índice reembolso: ");
        if (ir < 0 || ir >= reembolsos.size()) {
            System.out.println("Índice inválido");
            return;
        }
        Reembolso r = reembolsos.get(ir);
        String act = readString(">> Aprobar (a) / Rechazar (r): ");
        if (act.equalsIgnoreCase("a")) {
            r.setEstado("Aprobado");
            double val = r.getValorBase() + r.getValorServicio();
            r.setValorAcreditado(val);
            Usuario sol = r.getSolicitante();
            if (sol != null) {
                sol.setSaldoVirtual(sol.getSaldoVirtual() + val);
                System.out.println("Aprobado y abonado " + round2(val));
            } else {
                System.out.println("Aprobado, pero solicitante nulo (?)");
            }
        } else {
            r.setEstado("Rechazado");
            System.out.println("Rechazado");
        }
        snapshot();
    }



    private static class Oferta {
        private final String id;
        private final Cliente vendedor;
        private final Tiquete tiquete;
        private double precio;
        private Double precioContraoferta;
        private Cliente compradorPropuesto;

        public Oferta(String id, Cliente vendedor, Tiquete tiquete, double precio) {
            this.id = id;
            this.vendedor = vendedor;
            this.tiquete = tiquete;
            this.precio = precio;
        }

        public String getId() { return id; }
        public Cliente getVendedor() { return vendedor; }
        public Tiquete getTiquete() { return tiquete; }
        public double getPrecio() { return precio; }

        @Override
        public String toString() {
            String base = "[Oferta " + id + "] vendedor=" +
                          (vendedor != null ? vendedor.getLogin() : "null") +
                          ", tiquete=" + (tiquete != null ? tiquete.getId() : "null") +
                          ", precio=" + precio;
            if (precioContraoferta != null && compradorPropuesto != null) {
                base += " | Contraoferta de " + compradorPropuesto.getLogin() +
                        " por " + precioContraoferta;
            }
            return base;
        }
    }

    private static void registrarLog(String tipo, String descripcion) {
        logRegistros.add(new EntradaLog(LocalDateTime.now(), tipo, descripcion));
    }

    private static void verLog(Administrador admin) {
        System.out.println("\n===== LOG DEL SISTEMA =====");
        if (logRegistros.isEmpty()) {
            System.out.println("No hay registros.");
            return;
        }
        for (EntradaLog e : logRegistros) {
            System.out.println(e);
        }
    }

    private static void flujoEliminarOfertaComoAdmin(Administrador admin) {
        if (ofertas.isEmpty()) {
            System.out.println("No hay ofertas en el Marketplace.");
            return;
        }
        System.out.println("Ofertas en el Marketplace:");
        for (int i = 0; i < ofertas.size(); i++) {
            System.out.println("(" + i + ") " + ofertas.get(i));
        }
        int idx = readInt(">> Índice oferta a eliminar: ");
        if (idx < 0 || idx >= ofertas.size()) {
            System.out.println("Índice inválido.");
            return;
        }
        Oferta o = ofertas.get(idx);
        ofertas.remove(o);
        registrarLog("OFERTA_ELIMINADA_ADMIN",
            "El administrador " + admin.getLogin() + " eliminó la oferta " + o.getId());
        System.out.println("Oferta eliminada por el administrador.");
    }



    private static void seedDemo() {
        ADMIN = new Administrador(
            UUID.randomUUID().toString(),
            "Admin Demo",
            "admin.seguro@bm.com",
            "admin",
            Integer.toHexString("Admin#2025".hashCode())
        );
        ADMIN.setPorcentajeServicioGeneral(0.10);
        ADMIN.setCostoEmisionFijo(2.5);
        usuariosByCorreo.put(ADMIN.getCorreo(), ADMIN);
        usuariosByLogin.put(ADMIN.getLogin(), ADMIN);

        Organizador org = new Organizador(
            UUID.randomUUID().toString(),
            "Org Demo",
            "org.seguro@bm.com",
            "organizador",
            Integer.toHexString("Org#2025".hashCode())
        );
        usuariosByCorreo.put(org.getCorreo(), org);
        usuariosByLogin.put(org.getLogin(), org);

        Cliente cli = new Cliente(
            UUID.randomUUID().toString(),
            "User Demo",
            "user@demo.com",
            "userdemo",
            Integer.toHexString("User#2025".hashCode())
        );
        cli.setSaldoVirtual(9999.0);
        usuariosByCorreo.put(cli.getCorreo(), cli);
        usuariosByLogin.put(cli.getLogin(), cli);

        Venue v = new Venue(
            UUID.randomUUID().toString(),
            "Coliseo Central",
            "Bogotá",
            10000
        );
        v.setAprobado(true);
        venues.add(v);

        Evento e = new Evento(
            UUID.randomUUID().toString(),
            "Rock Fest",
            TipoDeEvento.Musical,
            LocalDate.now().plusDays(10),
            LocalTime.of(20, 0),
            org,
            v
        );
        eventos.add(e);
        e.getLocalidades().add(
            new Localidad(
                UUID.randomUUID().toString(),
                e,
                "General",
                TipoLocalidad.General,
                80.0,
                200
            )
        );
    }


    private static String readFileIfExists(String name) throws IOException {
        File f = new File(DATA_DIR, name);
        if (!f.exists()) return null;
        return Files.readString(Path.of(f.getAbsolutePath())).trim();
    }

    private static String generarPIN() {
        SecureRandom r = new SecureRandom();
        int n = 100000 + r.nextInt(900000);
        return String.valueOf(n);
    }

    private static String readString(String p) {
        System.out.print(p);
        return in.nextLine().trim();
    }

    private static String readPassword(String p) {
        return readString(p);
    }

    private static int readInt(String p) {
        while (true) {
            try {
                System.out.print(p);
                return Integer.parseInt(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Ingresa un número");
            }
        }
    }

    private static double readDouble(String p) {
        while (true) {
            try {
                System.out.print(p);
                return Double.parseDouble(in.nextLine().trim());
            } catch (Exception e) {
                System.out.println("Ingresa un número (decimal)");
            }
        }
    }

    private static double round2(double x) {
        return Math.round(x * 100.0) / 100.0;
    }

    private static void sysoLines(String... ls) {
        for (String s : ls) System.out.println(s);
    }



    private static void snapshot() {
        try {
            File d = new File(DATA_DIR);
            if (!d.exists()) d.mkdirs();
        } catch (Exception ignored) {}
        write("usuarios.json", dumpUsuarios());
        write("venues.json", dumpVenues());
        write("eventos.json", dumpEventos());
        write("tiquetes.json", dumpTiquetes());
        write("transacciones.json", dumpTransacciones());
        write("reembolsos.json", dumpReembolsos());
    }

    private static void write(String name, String content) {
        try (FileWriter fw = new FileWriter(new File(DATA_DIR, name))) {
            fw.write(content);
        } catch (Exception e) {
            System.out.println("No se pudo escribir " + name + ": " + e.getMessage());
        }
    }

    private static String dumpUsuarios() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Usuario u : usuariosByCorreo.values()) {
            if (i++ > 0) sb.append(",\n");
            sb.append("  {\"id\":\"").append(u.getId())
              .append("\", \"name\":\"").append(u.getName())
              .append("\", \"correo\":\"").append(u.getCorreo())
              .append("\", \"login\":\"").append(u.getLogin())
              .append("\", \"rol\":\"").append(u.getClass().getSimpleName())
              .append("\", \"saldoVirtual\":").append(round2(u.getSaldoVirtual()))
              .append(", \"pwdHash\":\"").append(u.getPasswordH()).append("\"}");
        }
        return sb.append("\n]\n").toString();
    }

    private static String dumpVenues() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Venue v : venues) {
            if (i++ > 0) sb.append(",\n");
            sb.append("  {\"id\":\"").append(v.getId())
              .append("\", \"nombre\":\"").append(v.getNombre())
              .append("\", \"direccion\":\"").append(v.getDireccion())
              .append("\", \"capacidadMaxima\":").append(v.getCapacidadMaxima())
              .append(", \"aprobado\":").append(v.isAprobado()).append("}");
        }
        return sb.append("\n]\n").toString();
    }

    private static String dumpEventos() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Evento e : eventos) {
            if (i++ > 0) sb.append(",\n");
            sb.append("  {")
              .append("\"id\":\"").append(e.getId()).append("\"")
              .append(", \"nombre\":\"").append(e.getNombre()).append("\"")
              .append(", \"tipo\":\"").append(e.getTipo()).append("\"")
              .append(", \"fecha\":\"").append(e.getFecha()).append("\"")
              .append(", \"hora\":\"").append(e.getHora()).append("\"")
              .append(", \"venueId\":\"").append(e.getVenue().getId()).append("\"")
              .append(", \"organizadorCorreo\":\"")
              .append(e.getOrganizador() != null ? e.getOrganizador().getCorreo() : "")
              .append("\"")
              .append(", \"cancelado\":").append(e.isCancelado())
              .append(", \"tiquetesVendidos\":").append(e.getTiquetesVendidos())
              .append(", \"localidades\":[");
            for (int j = 0; j < e.getLocalidades().size(); j++) {
                Localidad l = e.getLocalidades().get(j);
                if (j > 0) sb.append(",");
                sb.append("{")
                  .append("\"id\":\"").append(l.getId()).append("\"")
                  .append(", \"nombre\":\"").append(l.getNombre()).append("\"")
                  .append(", \"tipo\":\"").append(l.getTipo()).append("\"")
                  .append(", \"precio\":").append(round2(l.getPrecioPublico()))
                  .append(", \"capacidad\":").append(l.getCapacidad())
                  .append("}");
            }
            sb.append("]}");
        }
        return sb.append("\n]\n").toString();
    }

    private static String dumpTiquetes() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Tiquete t : tiquetes) {
            if (i++ > 0) sb.append(",\n");
            String clase = (t instanceof TiqueteDeluxe) ? "Deluxe" : "Normal";
            sb.append("  {")
              .append("\"id\":\"").append(t.getId()).append("\"")
              .append(", \"eventoId\":\"").append(t.getEvento().getId()).append("\"")
              .append(", \"localidadId\":\"").append(t.getLocalidad().getId()).append("\"")
              .append(", \"estado\":\"").append(t.getEstado()).append("\"")
              .append(", \"clase\":\"").append(clase).append("\"")
              .append(", \"ownerCorreo\":\"")
              .append(t.getPropietarioActual() != null ? t.getPropietarioActual().getCorreo() : "")
              .append("\"")
              .append(", \"precioBase\":").append(round2(t.getPrecioBase()))
              .append("}");
        }
        return sb.append("\n]\n").toString();
    }

    private static String dumpTransacciones() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Transaccion t : transacciones) {
            if (i++ > 0) sb.append(",\n");
            sb.append("  {")
              .append("\"id\":\"").append(t.getId()).append("\"")
              .append(", \"clienteCorreo\":\"")
              .append(t.getCliente() != null ? t.getCliente().getCorreo() : "")
              .append("\"")
              .append(", \"items\":").append(t.getItems() != null ? t.getItems().size() : 0)
              .append(", \"medioPago\":\"")
              .append(t.getMedioPago() != null ? t.getMedioPago().name() : "")
              .append("\"")
              .append(", \"subtotal\":").append(round2(t.getSubtotal()))
              .append(", \"recargo\":").append(round2(t.getRecargoServicio()))
              .append(", \"emision\":").append(round2(t.getCostoEmision()))
              .append(", \"descuento\":").append(round2(t.getDescuento()))
              .append(", \"contabilizaIngreso\":").append(t.isContabilizaIngreso())
              .append("}");
        }
        return sb.append("\n]\n").toString();
    }

    private static String dumpReembolsos() {
        StringBuilder sb = new StringBuilder("[\n");
        int i = 0;
        for (Reembolso r : reembolsos) {
            if (i++ > 0) sb.append(",\n");
            sb.append("  {")
              .append("\"id\":\"").append(r.getId()).append("\"")
              .append(", \"estado\":\"").append(r.getEstado()).append("\"")
              .append(", \"motivo\":\"").append(r.getMotivo()).append("\"")
              .append(", \"solicitanteCorreo\":\"")
              .append(r.getSolicitante() != null ? r.getSolicitante().getCorreo() : "")
              .append("\"")
              .append(", \"adminCorreo\":\"")
              .append(r.getAdmin() != null ? r.getAdmin().getCorreo() : "")
              .append("\"")
              .append(", \"tiqueteId\":\"")
              .append(r.getTiquete() != null ? ((Usuario) r.getTiquete()).getId() : "")
              .append("\"")
              .append(", \"valorBase\":").append(round2(r.getValorBase()))
              .append(", \"valorEmision\":").append(round2(r.getValorCargoEmision()))
              .append(", \"valorServicio\":").append(round2(r.getValorServicio()))
              .append(", \"valorAcreditado\":").append(round2(r.getValorAcreditado()))
              .append(", \"fecha\":\"")
              .append(r.getFecha() != null ? r.getFecha().toString() : "")
              .append("\"")
              .append("}");
        }
        return sb.append("\n]\n").toString();
    }

    private static boolean loadSnapshot() {
        try {
            File dir = new File(DATA_DIR);
            if (!dir.exists()) return false;

            // USUARIOS
            String jUsers = readFileIfExists("usuarios.json");
            if (jUsers == null) return false;

            usuariosByCorreo.clear();
            usuariosByLogin.clear();
            ADMIN = null;

            String innerU = stripArray(jUsers);
            if (!innerU.isEmpty()) {
                for (String obj : splitTopLevelObjects(innerU)) {
                    Map<String, String> m = parseFlatJsonObject(obj);
                    String id     = m.get("id");
                    String name   = m.get("name");
                    String correo = m.get("correo");
                    String login  = m.get("login");
                    String rol    = m.get("rol");
                    String pwd    = m.get("pwdHash");
                    double saldo  = parseDoubleSafe(m.get("saldoVirtual"), 0.0);

                    Usuario nuevo;
                    switch (rol) {
                        case "Administrador" -> nuevo = new Administrador(id, name, correo, login, pwd);
                        case "Organizador"   -> nuevo = new Organizador(id, name, correo, login, pwd);
                        default              -> nuevo = new Cliente(id, name, correo, login, pwd);
                    }
                    nuevo.setSaldoVirtual(saldo);
                    usuariosByCorreo.put(correo, nuevo);
                    usuariosByLogin.put(login, nuevo);
                    if ("Administrador".equals(rol) && allowlist.contains(correo)) {
                        ADMIN = (Administrador) nuevo;
                    }
                }
            }

            String jVen = readFileIfExists("venues.json");
            venues.clear();
            if (jVen != null) {
                String innerV = stripArray(jVen);
                if (!innerV.isEmpty()) {
                    for (String obj : splitTopLevelObjects(innerV)) {
                        Map<String, String> m = parseFlatJsonObject(obj);
                        Venue v = new Venue(
                            m.get("id"),
                            m.get("nombre"),
                            m.get("direccion"),
                            Integer.parseInt(m.getOrDefault("capacidadMaxima", "0"))
                        );
                        v.setAprobado(Boolean.parseBoolean(m.getOrDefault("aprobado", "false")));
                        venues.add(v);
                    }
                }
            }

            String jEv = readFileIfExists("eventos.json");
            eventos.clear();
            if (jEv != null) {
                String innerE = stripArray(jEv);
                if (!innerE.isEmpty()) {
                    for (String obj : splitTopLevelObjects(innerE)) {
                        Map<String, String> flat = parseFlatJsonObject(obj);
                        String id = flat.get("id");
                        String nombre = flat.get("nombre");
                        String tipoStr = flat.get("tipo");
                        String fechaStr = flat.get("fecha");
                        String horaStr  = flat.get("hora");
                        String venueId  = flat.get("venueId");
                        String orgCorreo= flat.get("organizadorCorreo");
                        boolean cancel  = Boolean.parseBoolean(flat.getOrDefault("cancelado", "false"));
                        int vendidos    = Integer.parseInt(flat.getOrDefault("tiquetesVendidos", "0"));

                        Venue venue = venues.stream()
                                            .filter(v -> v.getId().equals(venueId))
                                            .findFirst().orElse(null);
                        Organizador org = (orgCorreo != null && !orgCorreo.isEmpty())
                            ? (Organizador) usuariosByCorreo.get(orgCorreo) : null;

                        Evento e = new Evento(
                            id,
                            nombre,
                            Enums.TipoDeEvento.valueOf(tipoStr),
                            LocalDate.parse(fechaStr),
                            LocalTime.parse(horaStr),
                            org,
                            venue
                        );
                        e.setCancelado(cancel);
                        e.setTiquetesVendidos(vendidos);

                        List<String> locObjs = extractArrayObjects(obj, "\"localidades\"");
                        for (String lo : locObjs) {
                            Map<String, String> ml = parseFlatJsonObject(lo);
                            String idL   = ml.get("id");
                            String nomL  = ml.get("nombre");
                            Enums.TipoLocalidad tipoL = Enums.TipoLocalidad.valueOf(ml.get("tipo"));
                            double precio = parseDoubleSafe(ml.get("precio"), 0.0);
                            int cap       = Integer.parseInt(ml.getOrDefault("capacidad", "0"));
                            Localidad l = new Localidad(idL, e, nomL, tipoL, precio, cap);
                            e.getLocalidades().add(l);
                        }
                        eventos.add(e);
                    }
                }
            }

            String jTiq = readFileIfExists("tiquetes.json");
            tiquetes.clear();
            if (jTiq != null) {
                String innerT = stripArray(jTiq);
                if (!innerT.isEmpty()) {
                    for (String obj : splitTopLevelObjects(innerT)) {
                        Map<String, String> m = parseFlatJsonObject(obj);
                        String id = m.get("id");
                        String evId = m.get("eventoId");
                        String locId= m.get("localidadId");
                        String clase= m.getOrDefault("clase", "Normal");
                        String estado = m.getOrDefault("estado", "Vendido");
                        double precioBase = parseDoubleSafe(m.get("precioBase"), 0.0);
                        String ownerC = m.getOrDefault("ownerCorreo", "");

                        Evento e = eventos.stream()
                                          .filter(x -> x.getId().equals(evId))
                                          .findFirst().orElse(null);
                        Localidad l = (e == null) ? null
                            : e.getLocalidades().stream()
                               .filter(x -> x.getId().equals(locId))
                               .findFirst().orElse(null);

                        Tiquete t;
                        if ("Deluxe".equalsIgnoreCase(clase)) {
                            t = new TiqueteDeluxe(id, e, l, precioBase);
                        } else {
                            t = new TiqueteNormal(id, e, l, precioBase);
                        }
                        t.setEstado(Enums.TipoTiquetes.valueOf(estado));
                        if (ownerC != null && !ownerC.isEmpty()) {
                            Usuario u = usuariosByCorreo.get(ownerC);
                            if (u instanceof Usuarios.Cliente cu) t.setPropietarioActual(cu);
                        }
                        if (l != null) l.getTiquetes().add(t);
                        tiquetes.add(t);
                    }
                }
            }

            String jTx = readFileIfExists("transacciones.json");
            transacciones.clear();
            if (jTx != null) {
                String inner = stripArray(jTx);
                if (!inner.isEmpty()) {
                    for (String obj : splitTopLevelObjects(inner)) {
                        Map<String, String> m = parseFlatJsonObject(obj);
                        Usuarios.Cliente cli = null;
                        String correo = m.getOrDefault("clienteCorreo", "");
                        if (!correo.isEmpty() &&
                            usuariosByCorreo.get(correo) instanceof Usuarios.Cliente c) {
                            cli = c;
                        }

                        Transacciones.Transaccion tx = new Transacciones.Transaccion(
                            m.get("id"),
                            cli,
                            null
                        );
                        String medio = m.getOrDefault("medioPago", "");
                        if (!medio.isEmpty()) {
                            tx.setMedioPago(Enums.MedioPago.valueOf(medio));
                        }
                        double sub = parseDoubleSafe(m.get("subtotal"), 0.0);
                        double rec = parseDoubleSafe(m.get("recargo"), 0.0);
                        double emi = parseDoubleSafe(m.get("emision"), 0.0);
                        double des = parseDoubleSafe(m.get("descuento"), 0.0);
                        tx.calcularTotales(sub, rec, emi, des);
                        tx.setContabilizaIngreso(
                            Boolean.parseBoolean(m.getOrDefault("contabilizaIngreso", "false"))
                        );
                        transacciones.add(tx);
                    }
                }
            }

            String jRe = readFileIfExists("reembolsos.json");
            reembolsos.clear();
            if (jRe != null) {
                String inner = stripArray(jRe);
                if (!inner.isEmpty()) {
                    for (String obj : splitTopLevelObjects(inner)) {
                        Map<String, String> m = parseFlatJsonObject(obj);
                        String id = m.get("id");
                        String est= m.getOrDefault("estado", "Pendiente");
                        String mot= m.getOrDefault("motivo", "");
                        String solC = m.getOrDefault("solicitanteCorreo", "");
                        String admC = m.getOrDefault("adminCorreo", "");
                        String tiqId= m.getOrDefault("tiqueteId", "");

                        Usuarios.Cliente sol = null;
                        if (!solC.isEmpty() &&
                            usuariosByCorreo.get(solC) instanceof Usuarios.Cliente c) {
                            sol = c;
                        }
                        Administrador adm = null;
                        if (!admC.isEmpty() &&
                            usuariosByCorreo.get(admC) instanceof Administrador a) {
                            adm = a;
                        }
                        Tiquetes.Tiquete t = tiquetes.stream()
                                                     .filter(x -> x.getId().equals(tiqId))
                                                     .findFirst().orElse(null);

                        double vb = parseDoubleSafe(m.get("valorBase"), 0.0);
                        double ve = parseDoubleSafe(m.get("valorEmision"), 0.0);
                        double vs = parseDoubleSafe(m.get("valorServicio"), 0.0);
                        double va = parseDoubleSafe(m.get("valorAcreditado"), 0.0);

                        LocalDateTime fecha = null;
                        try {
                            String f = m.get("fecha");
                            if (f != null && !f.isEmpty()) fecha = LocalDateTime.parse(f);
                        } catch (Exception ignored) {}

                        Reembolsos.Reembolso r = new Reembolsos.Reembolso(
                            id, adm, sol, t, vb, ve, vs, va, mot, est, fecha
                        );
                        reembolsos.add(r);
                    }
                }
            }

            return true;

        } catch (Exception e) {
            System.out.println("No se pudo cargar snapshot: " + e.getMessage());
            return false;
        }
    }

    private static String stripArray(String json) {
        if (json == null) return "";
        json = json.trim();
        if (json.length() < 2) return "";
        if (json.charAt(0) == '[' && json.charAt(json.length() - 1) == ']') {
            return json.substring(1, json.length() - 1).trim();
        }
        return json;
    }

    private static double parseDoubleSafe(String s, double def) {
        try {
            return s == null ? def : Double.parseDouble(s);
        } catch (Exception e) {
            return def;
        }
    }

    private static List<String> extractArrayObjects(String objJson, String fieldName) {
        List<String> res = new ArrayList<>();
        int idx = objJson.indexOf(fieldName);
        if (idx < 0) return res;
        int lb = objJson.indexOf('[', idx);
        if (lb < 0) return res;
        int brace = 0;
        int start = -1;
        for (int i = lb + 1; i < objJson.length(); i++) {
            char c = objJson.charAt(i);
            if (c == '{') {
                if (brace == 0) start = i;
                brace++;
            } else if (c == '}') {
                brace--;
                if (brace == 0 && start != -1) {
                    res.add(objJson.substring(start, i + 1));
                    start = -1;
                }
            } else if (c == ']' && brace == 0) {
                break;
            }
        }
        return res;
    }

    private static List<String> splitTopLevelObjects(String inner) {
        List<String> objetos = new ArrayList<>();
        int brace = 0, start = 0;
        for (int i = 0; i < inner.length(); i++) {
            char c = inner.charAt(i);
            if (c == '{') brace++;
            else if (c == '}') brace--;
            if (brace == 0 &&
               (i == inner.length() - 1 ||
                (i < inner.length() - 1 && inner.charAt(i + 1) == ','))) {
                objetos.add(inner.substring(start, i + 1).trim());
                start = i + 2;
            }
        }
        if (start < inner.length()) {
            String rem = inner.substring(start).trim();
            if (!rem.isEmpty()) objetos.add(rem);
        }
        return objetos;
    }

    private static Map<String, String> parseFlatJsonObject(String obj) {
        Map<String, String> out = new HashMap<>();
        String s = obj.trim();
        if (s.startsWith("{")) s = s.substring(1);
        if (s.endsWith("}")) s = s.substring(0, s.length() - 1);

        List<String> pairs = new ArrayList<>();
        int q = 0, start = 0;
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            if (c == '"') q ^= 1;
            if (c == ',' && q == 0) {
                pairs.add(s.substring(start, i).trim());
                start = i + 1;
            }
        }
        if (start < s.length()) pairs.add(s.substring(start).trim());

        for (String p : pairs) {
            int sep = p.indexOf(':');
            if (sep < 0) continue;
            String k = p.substring(0, sep).trim();
            String v = p.substring(sep + 1).trim();
            if (k.startsWith("\"") && k.endsWith("\"")) k = k.substring(1, k.length() - 1);
            if (v.startsWith("\"") && v.endsWith("\"")) v = v.substring(1, v.length() - 1);
            out.put(k, v);
        }
        return out;
    }
}
