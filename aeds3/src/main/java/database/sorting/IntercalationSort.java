package database.sorting;

import comp.MovieObj;
import database.crud.WorkingStructure;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;

public class IntercalationSort implements Closeable {
    private final String db_path;

    private final int initialBlockSize;
    private final int ways;

    private File[] tmpInputFiles;
    private File[] tmpOutputFiles;

    public IntercalationSort(int initialBlockSize, int ways, String db_path) throws IOException{
        this.db_path = db_path;
        this.initialBlockSize = initialBlockSize;
        this.ways = ways;

        tmpInputFiles = new File[ways];
        tmpOutputFiles = new File[ways];

        for(int i = 0; i < ways; i++){
            tmpInputFiles[i] = File.createTempFile("sorting" + i + "F", ".temp");
            tmpInputFiles[i].deleteOnExit();
            System.out.println(tmpInputFiles[i]);
            tmpOutputFiles[i] = File.createTempFile("sorting" + (i+ways) + "F", ".temp");
            tmpOutputFiles[i].deleteOnExit();
            System.out.println(tmpOutputFiles[i]);
        }

    }

    public void overWriteDB() throws IOException {
        try (WorkingStructure archive = new WorkingStructure(this.getDb_path())) {
            archive.reset();
            try (WorkingStructure tmp = new WorkingStructure(this.getTmpInputFiles()[0].getAbsolutePath())) {
                MovieObj movieObj;
                while ((movieObj = tmp.readNext()) != null) {
                    archive.append(movieObj);
                }
            }
        }
    }

    public String getDb_path() {
        return db_path;
    }

    public int getInitialBlockSize() {
        return initialBlockSize;
    }

    public int getWays() {
        return ways;
    }

    public File[] getTmpInputFiles() {
        return tmpInputFiles;
    }

    public void setTmpInputFiles(File[] tmpInputFiles) {
        this.tmpInputFiles = tmpInputFiles;
    }

    public File[] getTmpOutputFiles() {
        return tmpOutputFiles;
    }

    public void setTmpOutputFiles(File[] tmpOutputFiles) {
        this.tmpOutputFiles = tmpOutputFiles;
    }

    @Override
    public void close() {
        for (File file : tmpInputFiles) {
            file.delete();
        }
        for (File file : tmpOutputFiles) {
            file.delete();
        }
    }
}