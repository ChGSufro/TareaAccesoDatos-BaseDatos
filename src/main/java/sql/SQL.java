package sql;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;

public class SQL {


    private String url;
    private String user;
    private String password;
    private Connection conexion;
    private Statement statement;

    public SQL() {
        try {
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

            conexion = conectar_base_datos();
            statement = conexion.createStatement();
        } catch (SQLException | IOException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }

    private Connection conectar_base_datos() throws SQLException {
        Connection conexion = DriverManager.getConnection(url, user, password);
        System.out.println("Conexión exitosa");
        return conexion;
    }

    public void close() {
        try {
            if (statement != null) statement.close();
            if (conexion != null) conexion.close();
            System.out.println("Desconexión exitosa");
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }

    public ResultSet get_datos_animal() throws SQLException {
        return statement.executeQuery("SELECT * FROM Animal");
    }

    public boolean adoptanteExiste(String rut) throws SQLException {
        String query = "SELECT 1 FROM Adoptante WHERE rut_usuario = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, rut);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSet.next();
        }
    }

    public void registrarAdoptante(String rut, String nombres, String apellidos, String sexo, String direccion, String telefono, String correo) throws SQLException {
        String query = "INSERT INTO Adoptante (rut_usuario, nombres, apellidos, sexo, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, rut);
            preparedStatement.setString(2, nombres);
            preparedStatement.setString(3, apellidos);
            preparedStatement.setString(4, sexo);
            preparedStatement.setString(5, direccion);
            preparedStatement.setString(6, telefono);
            preparedStatement.setString(7, correo);
            preparedStatement.executeUpdate();
        }
    }

    public void actualizarEstadoAnimal(String rutAnimal) throws SQLException {
        String query = "UPDATE Animal SET estado = 'Adoptado' WHERE rut_mascota = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, rutAnimal);
            preparedStatement.executeUpdate();
        }
    }

    public void eliminarAdoptante(String rut) throws SQLException {
        String query = "DELETE FROM Adoptante WHERE rut_usuario = ?";
        try (PreparedStatement preparedStatement = conexion.prepareStatement(query)) {
            preparedStatement.setString(1, rut);
            preparedStatement.executeUpdate();
        }
    }
}
