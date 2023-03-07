package database.sorting.intercalation;

import comp.MovieObj;
import database.crud.Crud;
import database.crud.WorkingStructure;
import database.sorting.IntercalationSort;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Stack;

public class IntercalationBasicSort extends IntercalationSort {
    public IntercalationBasicSort(int registersPerBlock, int ways, String db_path) throws IOException {
        super(registersPerBlock, ways, db_path);

        int totalBlocks = distribution();
        intercalate(totalBlocks);
    }

    public int distribution() throws IOException {
        Crud crud = new Crud(this.getDb_path());
        int totalBlocks = 0;

        try (WorkingStructure archive = new WorkingStructure(this.getDb_path())) {
            FileOutputStream[] streams = new FileOutputStream[this.getWays()];
            for (int t = 0; t < this.getTmpInputFiles().length; t++)
                streams[t] = new FileOutputStream(this.getTmpInputFiles()[t]);
            List<MovieObj> arr = new ArrayList<>();

            for (int nBlock = 0; !archive.isEOF(); nBlock++) {
                arr.clear();
                ObjectOutputStream oos = new ObjectOutputStream(streams[nBlock % this.getWays()]);
                for (int i = 0; i < this.getRegistersPerBlock() && !archive.isEOF(); i++) {
                    arr.add(archive.readNext());
                    totalBlocks++;
                }
                arr.sort(Comparator.comparingInt(MovieObj::getId));
                for (MovieObj movieObj : arr) oos.writeObject(movieObj);
            }

            for (int t = 0; t < this.getTmpInputFiles().length; t++) streams[t].close();
        }
        return totalBlocks;
    }

    public void intercalate(int totalBlocks) throws IOException {
        ObjectInputStream[] streams = new ObjectInputStream[this.getWays()];
        ObjectOutputStream[] streams2 = new ObjectOutputStream[this.getWays()];

        // Reads all input temp files intercalates and then swap them
        for (int intercalation = 1; intercalation*this.getRegistersPerBlock() < totalBlocks; intercalation++) {
            for (int t = 0; t <  this.getTmpInputFiles().length; t++) streams[t] = new ObjectInputStream(Files.newInputStream(this.getTmpInputFiles()[t].toPath()));
            for (int t = 0; t <  this.getTmpOutputFiles().length; t++) streams2[t] = new ObjectOutputStream(Files.newOutputStream(this.getTmpOutputFiles()[t].toPath()));

            this.merge(intercalation, streams, streams2[(intercalation-1) % this.getWays()]);

            File[] tempFilesSwap = this.getTmpInputFiles();
            this.setTmpInputFiles(this.getTmpOutputFiles());
            this.setTmpOutputFiles(tempFilesSwap);
        }
    }


    public void merge(int nBlocks, ObjectInputStream[] inputStreams, ObjectOutputStream outputStream) throws IOException {

        int[] blocks_read = new int[this.getWays()];

        List<Stack<MovieObj>> arr = new ArrayList<>();

        for (int t = 0; t < this.getWays(); t++) {
            arr.add(new Stack<>());
        }

        while (true) {
            int min = Integer.MAX_VALUE;
            int minIndex = -1;

            //TODO: Não está lendo todos os blocos

            // se o limite de blocos lidos nao foi atingido leia mais um bloco
            for (int i = 0; i < this.getWays(); i++) {
                if (blocks_read[i] < nBlocks) {
                    blocks_read[i]++;
                    if (arr.get(i).empty()) {
                        arr.get(i).addAll(this.readBlock(inputStreams[i]));
                        if (arr.get(i).empty()) {
                            blocks_read[i] = Integer.MAX_VALUE;
                        }
                    }
                }
            }


            // procura o menor elemento
            for (int i = 0; i < this.getWays(); i++) {
                if (!arr.get(i).empty()) {
                    if (arr.get(i).peek().getId() < min) {
                        min = arr.get(i).peek().getId();
                        minIndex = i;
                    }
                }
            }

            // se nao houver mais elementos
            if (minIndex == -1) {
                break;
            }

            // escreve o menor elemento no arquivo de saida
            outputStream.writeObject(arr.get(minIndex).pop());
        }
        outputStream.close();
    }

    private List<MovieObj> readBlock(ObjectInputStream stream) throws IOException {
        List<MovieObj> arr1 = new ArrayList<>();
        for (int i = 0; i < this.getRegistersPerBlock(); i++) {
            try {
                arr1.add((MovieObj) stream.readObject());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (EOFException e) {
                break;
            }
        }
        return arr1;
    }
}
