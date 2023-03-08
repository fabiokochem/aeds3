package database.sorting.intercalation;

import comp.MovieObj;
import database.crud.WorkingStructure;
import database.sorting.IntercalationSort;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class IntercalationBasicSort extends IntercalationSort {
    public IntercalationBasicSort(int registersPerBlock, int ways, String db_path) throws IOException {
        super(registersPerBlock, ways, db_path);

        int totalBlocks = distribution();
        intercalate(totalBlocks);
    }

    public int distribution() throws IOException {
        int totalBlocks = 0;
        List<MovieObj> movies = new ArrayList<>(this.getRegistersPerBlock());
        WorkingStructure[] tmpInputFiles = new WorkingStructure[this.getWays()];

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i] = new WorkingStructure(this.getDb_path());
        }

        try (WorkingStructure archive = new WorkingStructure(this.getDb_path())) {
            int fileIndex = 0;

            do {
                movies.clear();
                for (int i = 0; i < this.getRegistersPerBlock(); i++) movies.add(archive.readNext());
                movies.sort(Comparator.comparingInt(MovieObj::getId));

                for (MovieObj movieObj : movies) tmpInputFiles[fileIndex].append(movieObj);

                fileIndex = (fileIndex + 1) % this.getWays();
                totalBlocks++;
            } while (!movies.isEmpty());
        }

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i].close();
        }
        return totalBlocks;
    }

    public void intercalate(int totalBlocks) throws IOException {
       WorkingStructure[] tmpInputFiles = new WorkingStructure[this.getWays()];
       WorkingStructure[] tmpOutputFiles = new WorkingStructure[this.getWays()];


       for (int i = 0; i < this.getWays(); i++) {
           tmpInputFiles[i] = new WorkingStructure(this.getTmpInputFiles()[i].getAbsolutePath());
           tmpOutputFiles[i] = new WorkingStructure(this.getTmpOutputFiles()[i].getAbsolutePath());
            tmpInputFiles[i].reset();
            tmpOutputFiles[i].reset();
       }

       for (int i = 1; i*this.getRegistersPerBlock() < totalBlocks; i++) {
           int n = (int) Math.ceil((double) totalBlocks / (i * this.getRegistersPerBlock()));

           for (int j = 0; j < n; j++) {
              merge(this.getRegistersPerBlock()*i, tmpInputFiles, tmpOutputFiles[j%this.getWays()]);
           }

           File[] swap = this.getTmpInputFiles();
           this.setTmpInputFiles(this.getTmpOutputFiles());
           this.setTmpOutputFiles(swap);

           for (int j = 0; j < getWays(); j++) {
                tmpInputFiles[j].close();
                tmpOutputFiles[j].close();

                tmpInputFiles[j] = new WorkingStructure(this.getTmpInputFiles()[j].getAbsolutePath());
                tmpOutputFiles[j] = new WorkingStructure(this.getTmpOutputFiles()[j].getAbsolutePath());
                tmpOutputFiles[j].reset();
           }
       }


       for (int i = 0; i < this.getWays(); i++) {
           tmpInputFiles[i].close();
           tmpOutputFiles[i].close();
       }

    }


    public void merge(int registersPerBlock, WorkingStructure[] tmpInputFiles, WorkingStructure tmpOutputFile) throws IOException {
        int[] registersRead = new int[this.getWays()];
        MovieObj[] registers = new MovieObj[this.getWays()];
        int minIndex;

        while(true) {
            minIndex = -1;
            for (int i = 0; i < this.getWays(); i++) {
                if (registersRead[i] < registersPerBlock) {
                    registers[i] = tmpInputFiles[i].readNext();
                    registersRead[i]++;
                }
            }


            for (int i = 0; i < this.getWays(); i++) {
                if (registers[i] != null) {
                    if (minIndex == -1) {
                        minIndex = i;
                    } else if (registers[i].getId() < registers[minIndex].getId()) {
                        minIndex = i;
                    }
                }
            }

            if (minIndex == -1) {
                break;
            }

            tmpOutputFile.append(registers[minIndex]);
            registers[minIndex] = null;
        }
    }

}
