package database.sorting.intercalation;

import comp.HeapNode;
import comp.MinHeap;
import comp.MovieObj;
import database.crud.WorkingStructure;

import java.io.IOException;

public class IntercalationSelectionSort extends IntercalationVaryingSort {
    public IntercalationSelectionSort(int registersPerBlock, int ways, String db_path) throws IOException {
        super(registersPerBlock, ways, db_path);
    }

    @Override
    public int distribution() throws IOException {
        int totalRegisters = 0;
        MinHeap movies = new MinHeap(this.getInitialBlockSize());
        WorkingStructure[] tmpInputFiles = new WorkingStructure[this.getWays()];

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i] = new WorkingStructure(this.getTmpInputFiles()[i].getAbsolutePath());
        }

        try (WorkingStructure archive = new WorkingStructure(this.getDb_path())) {
            MovieObj movieObj;
            HeapNode oldMovieObjNode;

            for (int i = 0 ; i < this.getInitialBlockSize(); i++) {
                movies.insert(new HeapNode(archive.readNext()));
            }

            while ((movieObj = archive.readNext()) != null) {
                oldMovieObjNode = movies.substitute(new HeapNode(movieObj));
                tmpInputFiles[oldMovieObjNode.getWeight() % this.getWays()].append(oldMovieObjNode.getMovieObj());
            }

            for (int i = 0; i < this.getInitialBlockSize(); i++) {
                oldMovieObjNode = movies.remove();
                tmpInputFiles[oldMovieObjNode.getWeight() % this.getWays()].append(oldMovieObjNode.getMovieObj());
            }
        }

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i].close();
        }
        return totalRegisters;
    }
}
