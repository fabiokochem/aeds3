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
                movieObj = archive.readNext();
                if(movieObj != null) {
                    movies.insert(new HeapNode(movieObj));
                }
            }

            while ((movieObj = archive.readNext()) != null) {
                oldMovieObjNode = movies.substitute(new HeapNode(movieObj));
                tmpInputFiles[oldMovieObjNode.getWeight() % this.getWays()].append(oldMovieObjNode.getMovieObj());
                totalRegisters++;
            }


            while(!movies.isEmpty()){
                oldMovieObjNode = movies.remove();
                tmpInputFiles[oldMovieObjNode.getWeight() % this.getWays()].append(oldMovieObjNode.getMovieObj());
                totalRegisters++;
            }
        }

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i].close();
        }
        return totalRegisters;
    }
}
