package database.sorting.distribution;

import comp.MovieObj;
import database.sorting.IntercalationSort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class BasicDistribution extends IntercalationSort {
    public BasicDistribution(int registersPerBlock, int ways, String db_path, String tmpDir) throws IOException {
        super(registersPerBlock, ways, db_path, tmpDir);
    }

    public void distribution() throws IOException {
        this.getArchive().file.seek(0);
        this.getArchive().file.skipBytes(Integer.BYTES);

        FileOutputStream[] streams = new FileOutputStream[this.getWays()];
        for (int t = 0; t <  this.getTmpFiles1().length; t++) streams[t] = new FileOutputStream(this.getTmpFiles1()[t].toFile());
        List<MovieObj> arr = new ArrayList<>();

        for (int nBlock = 0; this.getArchive().notEOF(); nBlock++) {
            ObjectOutputStream oos = new ObjectOutputStream(streams[nBlock%this.getWays()]);
            for(int i = 0; i < this.getRegistersPerBlock(); i++) arr.add(this.getArchive().read());
            arr.sort(Comparator.comparingInt(MovieObj::getId));
            for (MovieObj movieObj : arr) oos.writeObject(movieObj);
        }
    }
}
