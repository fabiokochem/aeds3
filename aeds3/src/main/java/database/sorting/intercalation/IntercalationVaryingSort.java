package database.sorting.intercalation;

import comp.MovieObj;
import database.crud.WorkingStructure;

import java.io.IOException;

public class IntercalationVaryingSort extends IntercalationBasicSort {


    public IntercalationVaryingSort(int registersPerBlock, int ways, String db_path) throws IOException {
        super(registersPerBlock, ways, db_path);
    }


    @Override
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

            if (registersRead[minIndex] == registersPerBlock) {
                MovieObj lookAhead = tmpInputFiles[minIndex].peekNext();
                if (lookAhead != null && lookAhead.getId() > registers[minIndex].getId()) {
                    registersRead[minIndex] = 0;
                }
            }

            registers[minIndex] = null;

        }
    }
}
