package database.sorting.distribution;

import csv.CSVMovieParser;
import database.crud.WorkingStructure;
import junit.framework.TestCase;

import java.io.IOException;

public class BasicDistributionTest extends TestCase {


    WorkingStructure workingStructure;

    @Override
    public void setUp() throws Exception {
        super.setUp();

        workingStructure = new WorkingStructure("src/main/java/tmp/database.db");
        if (workingStructure.getLast_id() == 0) {
            CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv").forEach(movieObj -> {
                try {
                    workingStructure.createCRUD(movieObj);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }

    public void testDistribution() throws IOException {
        BasicDistribution distribution = new BasicDistribution(4, 2, workingStructure);
        distribution.distribution();
    }
}