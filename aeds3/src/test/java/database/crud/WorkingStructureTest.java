package database.crud;

import comp.MovieObj;
import csv.CSVMovieParser;
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
        int n = 0;
        for (MovieObj movieObj : CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv")) {
            if (crud.lastId() == 6 ) break;
            movieObj.setTitle("Filme " + (n++));
            crud.create(movieObj);
        }

        for (int i = 0; i < 3; i ++) {
            crud.delete(i);
        }

        assertEquals(6, crud.lastId());
        assertNull(crud.read(1));
        assertEquals("Filme 5", crud.read(6).getTitle());
    }

    public void testReadNext() throws IOException {
        try (WorkingStructure ws = new WorkingStructure(database.getAbsolutePath())) {
            MovieObj read = ws.readNext();
            assertNotNull(read);
            assertEquals(6, ws.readCab());

            for (int i = 3; i < 6; i++) {
                read = ws.readNext();
                assertNotNull(read);
                assertEquals("Filme " + (i-1), read.getTitle());
            }
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();

        database.delete();
    }

}