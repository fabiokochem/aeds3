package database.sorting;

import database.crud.WorkingStructure;

import java.io.File;
import java.io.IOException;

public class IntercalationSort {
    private final WorkingStructure archive;
    private final int registersPerBlock;
    private final int ways;

    private File[] tmpInputFiles;
    private File[] tmpOutputFiles;

    public IntercalationSort(int registersPerBlock, int ways, WorkingStructure archive) throws IOException{
        this.archive = archive;
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

    public WorkingStructure getArchive() {
        return archive;
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
}