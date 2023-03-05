package database.crud;

import comp.MovieObj;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class WorkingStructure implements Closeable {

    // variaveis ====================================================

    private int last_id = 0;
    public String basePath; 
    public RandomAccessFile file;

    // construtores =================================================

    public WorkingStructure(String path) throws IOException { //"src/main/java/tmp/database.db"
        this.basePath = path;
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.initializing();
    }

    // metodos publicos ======================================================

    public void createCRUD(MovieObj obj) throws IOException {
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.initializing();

        if(last_id >= obj.getId()) {
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

    //Initialize the file
    public boolean notEOF() throws IOException{
        return this.file.getFilePointer() < this.file.length();
    }

    //Write a register
    private void write(MovieObj obj) throws IOException{
        byte[] arr = obj.toByteArray();

        this.file.writeInt(arr.length);
        this.file.write(arr);
    }


    //Iterator to read all registers
    public Iterator<MovieObj> readAll() throws IOException {
        this.file.seek(0);
        this.file.skipBytes(Integer.BYTES);

        return new Iterator<MovieObj>() {
            @Override
            public boolean hasNext() {
                try {
                    return notEOF();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public MovieObj next() {
                try {
                    return read();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;
            }
        };
    }


    //Read a register

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


    //Create a database file
    private void initializing() throws IOException {
        if(this.file.length() == 0){
            this.file.seek(0);
            this.file.writeInt(0);
        } else {
            this.file.seek(0);
            last_id = this.file.readInt();
        }
    }

    //Update the last ID
    private void updateCab(int key) throws IOException{
        this.file.seek(0);
        this.file.writeInt(key);
    }

    //Read the last ID
    public int readCab() throws IOException {
        this.file = new RandomAccessFile(this.basePath, "rw");	
        this.file.seek(0);
        int lastId = this.file.readInt();
        this.file.close();
        return lastId;
    }


    public int getLast_id() {
        return last_id;
    }

    public void setLast_id(int last_id) {
        this.last_id = last_id;
    }

    public String getBasePath() {
        return basePath;
    }

    public void setBasePath(String basePath) {
        this.basePath = basePath;
    }

    public RandomAccessFile getFile() {
        return file;
    }

    public void setFile(RandomAccessFile file) {
        this.file = file;
    }

    @Override
    public void close() throws IOException {
        this.file.close();
    }
}