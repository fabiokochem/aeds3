package frontend;

import database.crud.Crud;

public class Main {
    public static void main(String[] args) throws Exception {
        Crud crud = new Crud();

        crud.Menu();
    }
}
