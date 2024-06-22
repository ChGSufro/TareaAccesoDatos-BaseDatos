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
            //Las claves estan en un archivo aparte dentro del proyecto.
            //Se conecta a rds de aws por lo que si se clona se deberia poder probar.
            Properties prop = new Properties();
            prop.load(getClass().getClassLoader().getResourceAsStream("config.properties"));
            url = prop.getProperty("url");
            user = prop.getProperty("user");
            password = prop.getProperty("password");

            conexion = conectarBaseDatos();
            statement = conexion.createStatement();

        } catch (SQLException | IOException e) {

            throw new RuntimeException("Error al conectar a la base de datos", e);
        }
    }

    private Connection conectarBaseDatos() throws SQLException {
        Connection conexion = DriverManager.getConnection(url, user, password);
        conexion.setAutoCommit(false);//se desactiva el auto commit, para manejar las transacciones manualmente
        System.out.println("Conexión exitosa");
        return conexion;
    }

    public void cerrarConexión() {
        try {
            if (statement != null) statement.close();
            if (conexion != null) conexion.close();
            System.out.println("Desconexión exitosa");
        } catch (SQLException e) {
            System.out.println("Error al cerrar la conexión");
            e.printStackTrace();
        }
    }

    //Metodos para manejar las transacciones fuera de esta clase
    public void commit() throws SQLException{
        if (conexion != null) conexion.commit();
    }

    public void rollback() throws SQLException{
        if (conexion != null) conexion.rollback();
    }

    //CRUD de adoptantes
    public boolean adoptanteExiste(String rut) throws SQLException {
        String query = "SELECT 1 FROM Adoptante WHERE rut_usuario = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public boolean adoptanteTieneAdopciones(String rut) throws SQLException {
        String query = "SELECT 1 FROM Adopcion WHERE rut_usuario = ? AND estado_adopcion = 'Activa'";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void crearAdoptante(String rut, String nombres, String apellidos, String sexo, String direccion, String telefono, String correo) throws SQLException {
        String query = "INSERT INTO Adoptante (rut_usuario, nombres, apellidos, sexo, direccion, telefono, correo) VALUES (?, ?, ?, ?, ?, ?, ?)";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        preparedStatement.setString(2, nombres);
        preparedStatement.setString(3, apellidos);
        preparedStatement.setString(4, sexo);
        preparedStatement.setString(5, direccion);
        preparedStatement.setString(6, telefono);
        preparedStatement.setString(7, correo);
        preparedStatement.executeUpdate();
    }

    public void eliminarAdoptante(String rut) throws SQLException {
        String query = "DELETE FROM Adoptante WHERE rut_usuario = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        preparedStatement.executeUpdate();
    }


    //CRUD de animales
    public ResultSet leerAnimales() throws SQLException {
        return statement.executeQuery("SELECT * FROM Animal");
    }

    public Boolean animalExiste(String rut) throws SQLException {
        String query = "SELECT 1 FROM Animal WHERE rut_mascota = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void bloquearAnimal(String rut) throws SQLException {
        String query = "SELECT * FROM Animal WHERE rut_mascota = ? FOR UPDATE";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        preparedStatement.executeQuery();
    }

    public Boolean animalAdoptado(String rut) throws SQLException {
        String query = "SELECT 1 FROM Animal WHERE rut_mascota = ? AND estado = 'Adoptado'";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rut);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSet.next();
    }

    public void actualizarEstadoAnimalAdoptado(String rutAnimal) throws SQLException {
        String query = "UPDATE Animal SET estado = 'Adoptado' WHERE rut_mascota = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rutAnimal);
        preparedStatement.executeUpdate();
    }

    public void actualizarEstadoAnimalDisponiblePorAdoptante(String rutAdoptante) throws SQLException {
        String query = "UPDATE Animal SET estado = 'Libre' WHERE rut_mascota IN(SELECT rut_mascota FROM Adopcion WHERE rut_usuario = ?)";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1,  rutAdoptante);
        preparedStatement.executeUpdate();
    }


    //CRUD de adopciones
    public void crearAdopcion(String rutAdoptante, String rutAnimal, Date fecha, Time hora) throws SQLException {
        String query = "INSERT INTO Adopcion (rut_usuario, rut_mascota, rut_empleado, fecha_adopcion, hora_adopcion, estado_adopcion) VALUES (?, ?, ?, ?, ?, 'Activa')";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rutAdoptante);
        preparedStatement.setString(2, rutAnimal);
        preparedStatement.setString(3, "22222");
        preparedStatement.setDate(4, fecha);
        preparedStatement.setTime(5, hora);

        preparedStatement.executeUpdate();
    }

    public void eliminarAdopcion(String rutAdoptante) throws SQLException {
        String query = "DELETE FROM Adopcion WHERE rut_usuario = ?";
        PreparedStatement preparedStatement = conexion.prepareStatement(query);
        preparedStatement.setString(1, rutAdoptante);
        preparedStatement.executeUpdate();
    }


}
