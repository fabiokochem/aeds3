package database.sorting;

import database.crud.WorkingStructure;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IntercalationSort {
    private final WorkingStructure archive;
    private final int registersPerBlock;
    private final int ways;

    private Path[] tmpFiles1;
    private Path[] tmpFiles2;

    public IntercalationSort(int registersPerBlock, int ways, String db_path, String tmpDir) throws IOException{
        this.archive = new WorkingStructure(db_path);
        this.registersPerBlock = registersPerBlock;
        this.ways = ways;

        tmpFiles1 = new Path[ways];
        tmpFiles2 = new Path[ways];

        for(int i = 0; i < ways; i++){
            tmpFiles1[i] = Paths.get(tmpDir, "t" + i + ".tmp");
            tmpFiles2[i] = Paths.get(tmpDir, "t" + (i+ways) + ".tmp");
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

    public Path[] getTmpFiles1() {
        return tmpFiles1;
    }

    public void setTmpFiles1(Path[] tmpFiles1) {
        this.tmpFiles1 = tmpFiles1;
    }

    public Path[] getTmpFiles2() {
        return tmpFiles2;
    }

    public void setTmpFiles2(Path[] tmpFiles2) {
        this.tmpFiles2 = tmpFiles2;
    }
}