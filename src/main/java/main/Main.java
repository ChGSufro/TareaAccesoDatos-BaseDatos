package main;

import Vistas.Menu;
import sql.SQL;

public class Main {
    public static void main(String[] args) {
        SQL sql = new SQL();
        Menu menu = new Menu(sql);
        menu.iniciar();

    }
}
