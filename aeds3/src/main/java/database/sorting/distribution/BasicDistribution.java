package database.sorting.distribution;

import comp.MovieObj;
import database.crud.WorkingStructure;
import database.sorting.IntercalationSort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BasicDistribution extends IntercalationSort {
    public BasicDistribution(int registersPerBlock, int ways, WorkingStructure archive) throws IOException {
        super(registersPerBlock, ways, archive);
    }

    public void distribution() throws IOException {
        Iterator<MovieObj> iterator = this.getArchive().readAll();

        FileOutputStream[] streams = new FileOutputStream[this.getWays()];
        for (int t = 0; t <  this.getTmpInputFiles().length; t++) streams[t] = new FileOutputStream(this.getTmpInputFiles()[t]);
        List<MovieObj> arr = new ArrayList<>();

        for (int nBlock = 0; this.getArchive().notEOF(); nBlock++) {
            arr.clear();
            ObjectOutputStream oos = new ObjectOutputStream(streams[nBlock%this.getWays()]);
            for(int i = 0; i < this.getRegistersPerBlock() && iterator.hasNext(); i++) arr.add(iterator.next());
            arr.sort(Comparator.comparingInt(MovieObj::getId));
            for (MovieObj movieObj : arr) oos.writeObject(movieObj);
        }
    }
}
