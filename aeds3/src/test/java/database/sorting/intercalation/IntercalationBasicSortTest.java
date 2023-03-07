package database.sorting.intercalation;

import comp.MovieObj;
import csv.CSVMovieParser;
import database.crud.Crud;
import junit.framework.TestCase;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;

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
        }

        public void testIntercalation() throws IOException {
            IntercalationBasicSort distribution = new IntercalationBasicSort(2, 2, database.getAbsolutePath());
            try (ObjectInputStream fis = new ObjectInputStream(new FileInputStream(distribution.getTmpInputFiles()[0]))) {
                for (int i = 0; i < 10; i++) {
                    MovieObj movieObj = (MovieObj) fis.readObject();
                    System.out.println(movieObj);
                }
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            distribution.overWriteDB();
            assertTrue(database.length() > 50);
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();

            //database.delete();
        }
}