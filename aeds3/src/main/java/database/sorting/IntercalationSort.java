package database.sorting;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

public class IntercalationSort implements Closeable {
    private final String db_path;

    private final int registersPerBlock;
    private final int ways;

    private File[] tmpInputFiles;
    private File[] tmpOutputFiles;

    public IntercalationSort(int registersPerBlock, int ways, String db_path) throws IOException{
        this.db_path = db_path;
        this.registersPerBlock = registersPerBlock;
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
        try (RandomAccessFile db = new RandomAccessFile(this.getDb_path(), "rw")) {
            try (RandomAccessFile tmpFile = new RandomAccessFile(this.getTmpOutputFiles()[0], "r")) {
                byte[] buffer = new byte[4096];
                int nRead;
                while ((nRead = tmpFile.read(buffer, 0, buffer.length)) != -1) {
                    db.write(buffer, 0, nRead);
                }
            }
        }
    }

    public String getDb_path() {
        return db_path;
    }

    public int getRegistersPerBlock() {
        return registersPerBlock;
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