package Vistas;

import sql.SQL;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class Menu {
    private final SQL sql;
    private final Scanner scanner;

    public Menu(SQL sql) {
        this.sql = sql;
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Realizar adopción");
            System.out.println("2. Registrar adoptante");
            System.out.println("3. Mostrar animales");
            System.out.println("4. Eliminar adoptante");
            System.out.print("Ingrese su opción (1-4): ");
            String opcion = scanner.nextLine();

            switch (opcion) {
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
            if (sql.adoptanteExiste(rutAdoptante)) {
                System.out.println("El adoptante existe. Procesando adopción...");
                sql.actualizarEstadoAnimal(rutAnimal);
                System.out.println("Estado del animal actualizado a 'Adoptado'.");



                // AGREGAR FUNCIONALIDAD DEL CAMBIO DE ESTADO




            } else {
                System.out.println("El adoptante no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar el adoptante");
            e.printStackTrace();
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
        System.out.print("Ingrese el sexo del adoptante: ");
        String sexo = scanner.nextLine();
        System.out.print("Ingrese la dirección del adoptante: ");
        String direccion = scanner.nextLine();
        System.out.print("Ingrese el teléfono del adoptante: ");
        String telefono = scanner.nextLine();
        System.out.print("Ingrese el correo del adoptante: ");
        String correo = scanner.nextLine();

        try {
            sql.registrarAdoptante(rutAdoptante, nombres, apellidos, sexo, direccion, telefono, correo);
            System.out.println("Adoptante registrado exitosamente.");
        } catch (SQLException e) {
            System.out.println("Error al registrar el adoptante");
            e.printStackTrace();
        }
    }

    private void mostrarAnimales() {
        try {
            ResultSet animales = sql.get_datos_animal();
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
            System.out.println("Error al obtener los datos de los animales");
            e.printStackTrace();
        }
    }

    private void eliminarAdoptante() {
        System.out.print("Ingrese el RUT del adoptante a eliminar: ");
        String rutAdoptante = scanner.nextLine();
        try {
            if (sql.adoptanteExiste(rutAdoptante)) {
                sql.eliminarAdoptante(rutAdoptante);
                System.out.println("Adoptante eliminado exitosamente.");
            } else {
                System.out.println("El adoptante no existe.");
            }
        } catch (SQLException e) {
            System.out.println("Error al eliminar el adoptante");
            e.printStackTrace();
        }
    }
}