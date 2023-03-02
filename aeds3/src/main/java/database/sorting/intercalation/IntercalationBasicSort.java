package database.sorting.intercalation;

import comp.MovieObj;
import database.sorting.IntercalationSort;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class IntercalationBasicSort extends IntercalationSort {
    public IntercalationBasicSort(int registersPerBlock, int ways, String db_path, String tmpDir) throws IOException {
        super(registersPerBlock, ways, db_path, tmpDir);
    }

    public void intercalate() throws IOException {
        ObjectInputStream[] streams = new ObjectInputStream[this.getWays()];
        ObjectOutputStream[] streams2 = new ObjectOutputStream[this.getWays()];
        for (int t = 0; t <  this.getTmpFiles1().length; t++) streams[t] = new ObjectInputStream(Files.newInputStream(this.getTmpFiles1()[t]));
        for (int t = 0; t <  this.getTmpFiles2().length; t++) streams2[t] = new ObjectOutputStream(Files.newOutputStream(this.getTmpFiles2()[t]));

        List<List<MovieObj>> arr = new ArrayList<>();

        for (int t = 0; t < this.getWays(); t++) {
            arr.add(new ArrayList<>());
        }

        for (int t = 0; t < this.getWays(); t++) {
            arr.set(t, this.readBlock(streams[t]));
        }



    }


    //TODO: Implementar o merge

    private List<MovieObj> readBlock(ObjectInputStream stream) throws IOException {
        List<MovieObj> arr1 = new ArrayList<>();
        for (int i = 0; i < this.getRegistersPerBlock(); i++) {
            try {
                arr1.add((MovieObj) stream.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        return arr1;
    }
}
