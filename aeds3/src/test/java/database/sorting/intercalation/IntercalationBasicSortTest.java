package database.sorting.intercalation;

import comp.MovieObj;
import csv.CSVMovieParser;
import database.crud.Crud;
import database.crud.WorkingStructure;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class IntercalationBasicSortTest extends TestCase {

        private File database;

        @Override
        public void setUp() throws Exception {
            super.setUp();


            database = File.createTempFile("database", ".db", new File("target"));
            Crud crud = new Crud(database.getAbsolutePath());


            crud.reset();
            int n = 0;
            for (MovieObj movieObj : CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv")) {
                if (crud.lastId() == 20 ) break;
                movieObj.setTitle("Filme " + (n++));
                crud.create(movieObj);
            }

            for (int i = 0; i < 10; i ++) {
                crud.delete(i);
            }

            assertEquals(20, crud.lastId());
            assertNull(crud.read(1));
            assertEquals("Filme 10", crud.read(11).getTitle());
        }

        public void testIntercalation() throws IOException {
            try (IntercalationBasicSort distribution = new IntercalationBasicSort(2, 2, database.getAbsolutePath())) {
                int totalBlocks = distribution.distribution();
                distribution.intercalate(totalBlocks);
                distribution.overWriteDB();
                try (WorkingStructure workingStructure = new WorkingStructure(database.getAbsolutePath())) {
                    assertEquals("Filme 9", workingStructure.read(10).getTitle());
                }
            }
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();

            //database.delete();
        }

    public void testDistribution() throws IOException {
        try (IntercalationBasicSort intercalationBasicSort = new IntercalationBasicSort(2, 2, database.getAbsolutePath())) {
            intercalationBasicSort.distribution();
            MovieObj movieObj;
            try (WorkingStructure workingStructure = new WorkingStructure(intercalationBasicSort.getTmpInputFiles()[0].getAbsolutePath())) {
                movieObj = workingStructure.read(workingStructure.readCab());
            }
            assertEquals("Filme 18", movieObj.getTitle());
        }


    }


}