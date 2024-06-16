package Vistas;

import sql.SQL;

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
            System.out.println("1. Adoptante existente");
            System.out.println("2. Nuevo adoptante");
            System.out.print("Ingrese su opción (1 o 2): ");
            String opcion = scanner.nextLine();

            if (opcion.equals("1")) {
                procesarAdoptanteExistente();
            } else if (opcion.equals("2")) {
                registrarNuevoAdoptante();
            } else {
                System.out.println("Opción inválida. Intente nuevamente.");
            }
        }
    }

    private void procesarAdoptanteExistente() {
        System.out.print("Ingrese el RUT del adoptante: ");
        String rutAdoptante = scanner.nextLine();
        System.out.print("Ingrese el RUT del animal a adoptar: ");
        String rutAnimal = scanner.nextLine();
        try {
            if (sql.adoptanteExiste(rutAdoptante)) {
                System.out.println("El adoptante existe. Procesando adopción...");
                // Aquí puedes agregar la lógica para procesar la adopción
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
}
