package database.crud;

import comp.MovieObj;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class WorkingStructureTest extends TestCase {


    private File database;

    @Override
    public void setUp() throws Exception {
        super.setUp();


        database = File.createTempFile("database", ".db", new File("target"));
        Crud crud = new Crud(database.getAbsolutePath());

        crud.reset();
    }

    public void testRead() throws IOException {
        try (WorkingStructure ws = new WorkingStructure(database.getAbsolutePath())) {
            MovieObj read = ws.readNext();
            System.out.println(read);
            assertNotNull(read);

        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        database.delete();
    }
}