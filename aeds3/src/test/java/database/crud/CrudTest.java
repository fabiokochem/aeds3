package database.crud;

import comp.MovieObj;
import junit.framework.TestCase;

import java.util.Date;

public class CrudTest extends TestCase {

    public void testCreateCRUD() {
    }

    public void testTestCreateCRUD() {
        MovieObj obj = new MovieObj(-1,"Ponte pra narnia", new Date(), 5.9f, 120, "Action, Comedy", "Fábio Freire");
    }
}