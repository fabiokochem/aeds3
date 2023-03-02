package external_ordering;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import comp.MovieObj;
import database.crud.WorkingStructure;

public class BalancedInter {
    private WorkingStructure archive;
    private int registersPerBlock;
    private int ways;

    private File[] tmpFiles1;
    private File[] tmpFiles2;

    public BalancedInter(int registersPerBlock, int ways, String db_path, String tmpDir) throws IOException{
        this.archive = new WorkingStructure(db_path);
        this.registersPerBlock = registersPerBlock;
        this.ways = ways;

        tmpFiles1 = new File[ways];
        tmpFiles2 = new File[ways];

        for(int i = 0; i < ways; i++){
            tmpFiles1[i] = new File(Paths.get(tmpDir, "t" + i + ".tmp").toString());
            tmpFiles2[i] = new File(Paths.get(tmpDir, "t" + (i+ways) + ".tmp").toString());
        }
    }

    public void distribution() throws IOException{
        this.archive.file.seek(0);
        this.archive.file.skipBytes(Integer.BYTES);
    
        FileOutputStream[] streams = new FileOutputStream[this.ways];
         for (int t = 0; t <  tmpFiles1.length; t++) streams[t] = new FileOutputStream(tmpFiles1[t]);
        List<MovieObj> arr = new ArrayList<>();

        for (int nBlock = 0; archive.notEOF(); nBlock++) {
            ObjectOutputStream oos = new ObjectOutputStream(streams[nBlock%ways]);
            for(int i = 0; i < registersPerBlock; i++) arr.add(archive.read());
            arr.sort(Comparator.comparingInt(MovieObj::getId));
            for(int j = 0; j < arr.size(); j++) {
                oos.writeObject(arr.get(j));
            } 
        }
    }

    

}