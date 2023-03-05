package frontend;

import database.crud.Crud;
import database.crud.WorkingStructure;

public class Main {
    public static void main(String[] args) throws Exception {
        Crud crud = new Crud(new WorkingStructure("src/main/java/tmp/database.db"));

        crud.Menu();
    }
}
