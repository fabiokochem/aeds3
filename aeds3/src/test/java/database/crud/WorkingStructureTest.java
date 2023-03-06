package database.crud;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

public class WorkingStructureTest extends TestCase {

    public void testRead() throws IOException {
        try (WorkingStructure ws = new WorkingStructure(File.createTempFile("testdb",".db").getPath())) {
            System.out.println(ws.read());
        }
    }
}