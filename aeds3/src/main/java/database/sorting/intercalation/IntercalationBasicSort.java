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
    }

    public int distribution() throws IOException {
        int totalRegisters = 0;
        List<MovieObj> movies = new ArrayList<>(this.getInitialBlockSize());
        WorkingStructure[] tmpInputFiles = new WorkingStructure[this.getWays()];

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i] = new WorkingStructure(this.getTmpInputFiles()[i].getAbsolutePath());
        }

        try (WorkingStructure archive = new WorkingStructure(this.getDb_path())) {
            int fileIndex = 0;
            MovieObj movieObj;

            do {
                movies.clear();
                for (int i = 0; i < this.getInitialBlockSize() && (movieObj = archive.readNext()) != null; i++) movies.add(movieObj);
                movies.sort(Comparator.comparingInt(MovieObj::getId));

                for (MovieObj m : movies) {
                    tmpInputFiles[fileIndex].append(m);
                    totalRegisters++;
                }

                fileIndex = (fileIndex + 1) % this.getWays();
            } while (!movies.isEmpty());
        }

        for (int i = 0; i < this.getWays(); i++) {
            tmpInputFiles[i].close();
        }
        return totalRegisters;
    }

    public void intercalate(int totalRegisters) throws IOException {
       WorkingStructure[] tmpInputFiles = new WorkingStructure[this.getWays()];
       WorkingStructure[] tmpOutputFiles = new WorkingStructure[this.getWays()];

       for (int i = 0; i < this.getWays(); i++) {
           tmpInputFiles[i] = new WorkingStructure(this.getTmpInputFiles()[i].getAbsolutePath());
           tmpOutputFiles[i] = new WorkingStructure(this.getTmpOutputFiles()[i].getAbsolutePath());
           tmpOutputFiles[i].reset();
       }

       for (int i = this.getInitialBlockSize(); i/2 < totalRegisters*this.getWays(); i*=2) {
           int nBlocks = (int) Math.ceil((double) totalRegisters / i / this.getWays());

           System.out.println("Tam. bloco: " + i + " | N. blocos: " + nBlocks + " | Total: " + totalRegisters + " | Ways: " + this.getWays());

           for (int j = 0; j < nBlocks; j++) {
               System.out.println("Intercalando bloco " + j + " de " + nBlocks);
               merge(i, tmpInputFiles, tmpOutputFiles[j%this.getWays()]);
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
                if (registersRead[i] < registersPerBlock && registers[i] == null) {
                    registers[i] = tmpInputFiles[i].readNext();
                    registersRead[i]++;
                }
            }

            for (int i = 0; i < this.getWays(); i++) {
                if (registers[i] != null) {
                    if (minIndex == -1 || registers[i].getId() < registers[minIndex].getId()) {
                        minIndex = i;
                    }
                }
            }

            if (minIndex == -1) {
                break;
            }

            tmpOutputFile.append(registers[minIndex]);
            System.out.println("-Escrevendo " + registers[minIndex].getId() + " do caminho " + minIndex);
            registers[minIndex] = null;
        }
    }

}
