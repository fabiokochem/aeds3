package database.sorting.intercalation;

import comp.MovieObj;
import database.crud.Crud;
import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;
import java.util.Date;

public class IntercalationBasicSortTest extends TestCase {

        private File database;

        @Override
        public void setUp() throws Exception {
            super.setUp();


            database = File.createTempFile("database", ".db", new File("target"));
            Crud crud = new Crud(database.getAbsolutePath());


            crud.create(new MovieObj(-1, "Filme 1", new Date(), 5.0f, 100, "Ação, Aventura", "Diretor 1, Diretor 2"));
            crud.create(new MovieObj(-1, "Filme 2", new Date(), 2.4f, 112, "Ficção, Drama", "Diretor 3, Diretor 2"));
            crud.delete(crud.create(new MovieObj(-1, "Filme 3", new Date(), 3.0f, 120, "Ação, Ficção", "Diretor 1, Diretor 4")));
            crud.create(new MovieObj(-1, "Filme 4", new Date(), 4.0f, 100, "Ação, Aventura", "Diretor 1, Diretor 2"));
            crud.create(new MovieObj(-1, "Filme 5", new Date(), 2.4f, 112, "Ficção, Drama", "Diretor 3, Diretor 2"));
        }

        public void testDistribution() throws IOException {
            IntercalationBasicSort distribution = new IntercalationBasicSort(2, 2, database.getAbsolutePath());
            distribution.overWriteDB();
        }

        @Override
        public void tearDown() throws Exception {
            super.tearDown();

            //database.delete();
        }
}