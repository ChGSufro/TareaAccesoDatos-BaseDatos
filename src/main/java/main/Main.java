package main;
import sql.SQL;

import javax.xml.transform.Result;
import java.sql.ResultSet;

public class Main {
    public static void main(String[] args) {
        SQL sql = new SQL();
        try {
            ResultSet datos = sql.get_datos_animal();
            while (datos.next()){
                System.out.println(datos.getString("nombre"));
            }
        } catch (Exception e) {
            System.out.println("Error al obtener los datos");
            e.printStackTrace();
        }

    }
}