package database.crud;

import com.opencsv.CSVReader;
import comp.MovieObj;

import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WorkingStructure {

    // variaveis ====================================================

    private static int id = 0;
    public String basePath; 
    public RandomAccessFile file;

    // construtores =================================================

    public WorkingStructure(String path) throws IOException { //"src/main/java/tmp/database.db"
        this.basePath = path;
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.initializing();
        this.file.close();
    }

    // metodos publicos ======================================================

    public void createCRUD(MovieObj obj) throws IOException {
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.initializing();

        if(id >= obj.getId()) {
            throw new IllegalArgumentException("[ERROR] The ID is not valid!");
        }

        this.file.seek(this.file.length());
        this.file.writeBoolean(true);
        this.write(obj);

        this.updateCab(obj.getId());
        this.file.close();
    }

    
    public MovieObj readCRUD(int key) throws IOException {
        if(this.isEmpty()) {
            throw new RuntimeException("[ERROR] The file is EMPTY!");
        }

        this.file = new RandomAccessFile(this.basePath, "rw");

        MovieObj obj = null;

        this.file.seek(Integer.BYTES);
        while(this.notEOF() && (obj == null || obj.getId() != key)) {
            boolean tombStone = this.file.readBoolean();
            if(tombStone) {
                obj = this.read();
            } else {
                int size = this.file.readInt();
                this.file.skipBytes(size);
            }
        }

        if(obj == null || obj.getId() != key) obj = null;
        else updateCab(obj.getId());

        this.file.close();
        return obj;
    }

    public boolean deleteCRUD(int key) throws IOException {
        if(this.isEmpty()) {
            throw new RuntimeException("[ERROR] The file is EMPTY!");
        }

        this.file = new RandomAccessFile(this.basePath, "rw");

        boolean res;
        MovieObj obj = null;
        long pos = 0;
        
        this.file.seek(Integer.BYTES);
        while(this.notEOF() && (obj == null || obj.getId() != key)) {
            pos = this.file.getFilePointer();
            boolean tombStone = this.file.readBoolean();

            if(tombStone) {
                obj = this.read();
            } else {
                int size = this.file.readInt();
                this.file.skipBytes(size);
            }
        }

        res = obj != null && obj.getId() == key;
        if(res) {
            this.file.seek(pos);
            this.file.writeBoolean(false);
            updateCab(obj.getId());
        }
 
        this.file.close();
        return res;
    }

    //Drop all registers
    public void reset() throws IOException{
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.file.setLength(0);
        this.initializing();
        this.file.close();
    }

    public boolean isEmpty() throws IOException{
        this.file = new RandomAccessFile(this.basePath, "rw");
        boolean res = this.file.length() == 0;
        this.file.close();
        return res;
    }

    // metodos privados ======================================================

    public boolean notEOF() throws IOException{
        return this.file.getFilePointer() < this.file.length();
    }

    private void write(MovieObj obj) throws IOException{
        byte[] arr = obj.toByteArray();

        this.file.writeInt(arr.length);
        this.file.write(arr);
    }

    public MovieObj read() throws IOException {
        MovieObj obj = null;

        if(notEOF()){
            int size = this.file.readInt();
            byte[] arr = new byte[size];

            this.file.read(arr);

            obj = new MovieObj();
            obj.fromByteArray(arr);
        }
        
        return obj;
    }

    private void initializing() throws IOException {
        if(this.file.length() == 0){
            this.file.seek(0);
            this.file.writeInt(0);
        } else {
            this.file.seek(0);
            id = this.file.readInt();
        }
    }

    private void updateCab(int key) throws IOException{
        this.file.seek(0);
        this.file.writeInt(key);
    }

    public int readCab() throws IOException {
        this.file = new RandomAccessFile(this.basePath, "rw");	
        this.file.seek(0);
        int lastId = this.file.readInt();
        this.file.close();
        return lastId;
    }

}