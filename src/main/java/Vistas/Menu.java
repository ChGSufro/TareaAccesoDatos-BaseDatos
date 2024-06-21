package Vistas;

import sql.SQL;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.Scanner;
import java.util.Calendar;

public class Menu {
    private final Calendar calendar = Calendar.getInstance();

    private final SQL sql;
    private final Scanner scanner;

    public Menu(SQL sql) {
        this.sql = sql;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("0. Salir");
            System.out.println("1. Realizar adopción");
            System.out.println("2. Registrar adoptante");
            System.out.println("3. Mostrar animales");
            System.out.println("4. Eliminar adoptante");
            System.out.print("Ingrese su opción (1-4): ");
            String opcion = scanner.nextLine();

            switch (opcion) {
                case "0":
                    System.out.println("Saliendo...");
                    sql.cerrarConexión();
                    return;

                case "1":
                    realizarAdopcion();
                    break;

                case "2":
                    registrarNuevoAdoptante();
                    break;

                case "3":
                    mostrarAnimales();
                    break;

                case "4":
                    eliminarAdoptante();
                    break;

                default:
                    System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private void realizarAdopcion() {
        System.out.print("Ingrese el RUT del adoptante: ");
        String rutAdoptante = scanner.nextLine();
        System.out.print("Ingrese el RUT del animal a adoptar: ");
        String rutAnimal = scanner.nextLine();
        try {
            if (!sql.adoptanteExiste(rutAdoptante)) {
                System.out.println("El adoptante no existe.");
                return;
            }
            if (!sql.animalExiste(rutAnimal)){
                System.out.println("El animal no existe.");
                return;
            }
            if (sql.animalAdoptado(rutAnimal)){
                System.out.println("El animal ya ha sido adoptado.");
                return;
            }
            sql.bloquearAnimal(rutAnimal);

            if (!consultarConfirmacion()){
                System.out.println("Operación cancelada.");
                return;
            }

            sql.actualizarEstadoAnimalAdoptado(rutAnimal);
            System.out.println("Estado del animal actualizado a 'Adoptado'.");

            sql.crearAdopcion(rutAdoptante, rutAnimal, obtenerFecha(), obtenerHora());
            System.out.println("Adopción realizada exitosamente.");

            sql.commit();

        } catch (SQLException e) {
            System.out.println("Error al procesar la adopcion. \nIntente nuevamente o pruebe mas tarde.");

            try{
                sql.rollback();;

            } catch (SQLException ex) {
                System.out.println("Error al revertir los cambios.");
            }
        }
    }

    private void registrarNuevoAdoptante() {
        System.out.println("Registrando un nuevo adoptante...");

        System.out.print("Ingrese el RUT del adoptante: ");
        String rutAdoptante = scanner.nextLine();

        System.out.print("Ingrese los nombres del adoptante: ");
        String nombres = scanner.nextLine();

        System.out.print("Ingrese los apellidos del adoptante: ");
        String apellidos = scanner.nextLine();

        String sexo = consultarSexo();

        System.out.print("Ingrese la dirección del adoptante: ");
        String direccion = scanner.nextLine();

        System.out.print("Ingrese el teléfono del adoptante: ");
        String telefono = scanner.nextLine();

        System.out.print("Ingrese el correo del adoptante: ");
        String correo = scanner.nextLine();

        try {
            if (sql.adoptanteExiste(rutAdoptante)) {
                System.out.println("El adoptante ya existe.");
                return;
            }

            sql.crearAdoptante(rutAdoptante, nombres, apellidos, sexo, direccion, telefono, correo);
            System.out.println("Adoptante registrado exitosamente.");
            sql.commit();

        } catch (SQLException e) {
            System.out.println("Error al registrar el adoptante.");

            try {
                sql.rollback();

            } catch (SQLException ex) {
                System.out.println("Error al revertir los cambios.");

            }
        }
    }

    private void mostrarAnimales() {
        try {
            ResultSet animales = sql.leerAnimales();
            System.out.println("Animales disponibles:");
            while (animales.next()) {
                System.out.println("RUT: " + animales.getString("rut_mascota") +
                        ", Nombre: " + animales.getString("nombre") +
                        ", Edad: " + animales.getInt("edad") +
                        ", Raza: " + animales.getString("raza") +
                        ", Especie: " + animales.getString("especie") +
                        ", Sexo: " + animales.getString("sexo") +
                        ", Estado: " + animales.getString("estado"));

            }
        } catch (SQLException e) {

            System.out.println("Error al conectar con la base de datos.");
        }
    }

    private void eliminarAdoptante() {
        System.out.print("Ingrese el RUT del adoptante a eliminar: ");
        String rutAdoptante = scanner.nextLine();
        try {

            if (sql.adoptanteExiste(rutAdoptante)) {

                if (sql.adoptanteTieneAdopciones(rutAdoptante)) {
                    System.out.println("El adoptante tiene adopciones activas.");
                    System.out.println("Se eliminarán las adopciones asociadas al adoptante.");

                    if (!consultarConfirmacion()){
                        System.out.println("Operación cancelada.");
                        return;
                    }
                }

                sql.actualizarEstadoAnimalDisponiblePorAdoptante(rutAdoptante);
                sql.eliminarAdopcion(rutAdoptante);
                System.out.println("Adopciones eliminadas exitosamente.");
                sql.eliminarAdoptante(rutAdoptante);

                sql.commit();
                System.out.println("Adoptante eliminado exitosamente.");

            } else {
                System.out.println("El adoptante no existe.");
            }

        } catch (SQLException e) {

            try {
                sql.rollback();

            } catch (SQLException ex) {
                System.out.println("Error al revertir los cambios.");
            }

            System.out.println("Error al eliminar el adoptante");
        }
    }



    // Métodos auxiliares
    private String consultarSexo() {
        System.out.print("Ingrese el sexo del adoptante (M/F): ");
        String sexo = scanner.nextLine();

        while (!sexo.equals("M") && !sexo.equals("F")) {
            System.out.println("Sexo inválido. Intente nuevamente.");
            System.out.print("Ingrese el sexo del adoptante (M/F): ");
            sexo = scanner.nextLine();

        }

        return sexo;
    }

    private boolean consultarConfirmacion(){
        while (true){
            System.out.print("¿Está seguro? (S/N): ");
            String confirmacion = scanner.nextLine();
            if (confirmacion.equals("S")){
                return true;
            } else if (confirmacion.equals("N")){
                return false;
            } else {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private Date obtenerFecha() {
        String fecha = calendar.get(Calendar.YEAR) + "-" + (calendar.get(Calendar.MONTH) + 1) + "-" + calendar.get(Calendar.DAY_OF_MONTH);
        return Date.valueOf(fecha);
    }

    private Time obtenerHora() {
        String hora = calendar.get(Calendar.HOUR_OF_DAY) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
        return Time.valueOf(hora);
    }


}