package database.crud;

import junit.framework.TestCase;

import java.io.IOException;

public class WorkingStructureTest extends TestCase {

    WorkingStructure ws;
    @Override
    public void setUp() throws Exception {
        super.setUp();

        ws = new WorkingStructure("src/test/java/database/crud/teste.txt");
        Crud crud = new Crud(ws);

        crud.reset();


    }

    public void testRead() throws IOException {
        System.out.println(ws.read());
    }
}