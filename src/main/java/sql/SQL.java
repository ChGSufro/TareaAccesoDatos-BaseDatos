package sql;

import java.sql.*;
import java.util.AbstractList;

public class SQL {

        private final String url = "jdbc:postgresql://base-de-datos.cwl2z8i6c6vy.us-east-1.rds.amazonaws.com/CanesFelinos";
        private String user = "postgres";
        private String password = "C#20gc15";
        private Connection conexion;
        private Statement statement;

    public SQL(){
        try {
            conexion = conectar_base_datos();
            statement = conexion.createStatement();
        } catch (SQLException e) {
            System.out.println("Error al conectar a la base de datos");
            e.printStackTrace();
        }
    }

        private Connection conectar_base_datos() throws SQLException{
            Connection conexion = DriverManager.getConnection(url, user, password);
            System.out.println("Conexión exitosa");
            return conexion;
        }

        private void desconectar_base_datos() throws SQLException{
            conexion.close();
            System.out.println("Desconexión exitosa");
        }

        public ResultSet get_datos_animal() throws SQLException{
                ResultSet datos = statement.executeQuery("SELECT * FROM animal");
                return datos;
        }


}
