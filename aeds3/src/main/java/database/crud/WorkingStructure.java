package database.crud;

import comp.MovieObj;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class WorkingStructure implements AutoCloseable {

    // variaveis ====================================================

    private int last_id = -1;
    public String basePath; 
    public RandomAccessFile file;

    // construtores =================================================

    public WorkingStructure(String path) throws IOException { //"src/main/java/tmp/database.db"
        this.basePath = path;
        this.initializing();
    }

    // metodos publicos ======================================================

    public int createCRUD(MovieObj obj) throws IOException {
        this.initializing();

        if (obj.getId() < 0) {
            obj.setId(++this.last_id);
        } else if(last_id >= obj.getId()) {
            throw new IllegalArgumentException("[ERROR] The ID (" + obj.getId() + ") is already in use! Last ID: " + last_id);
        }

        this.getFile().seek(this.getFile().length());
        this.getFile().writeBoolean(true);
        this.write(obj);

        this.updateCab(obj.getId());
        return obj.getId();
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

        return new Iterator<>() {
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

        if(!notEOF()) return null;

        int size = this.file.readInt();
        byte[] arr = new byte[size];

        this.file.read(arr);
        
        return MovieObj.fromByteArray(arr);
    }


    //Create a database file
    private void initializing() throws IOException {
        this.file = new RandomAccessFile(this.basePath, "rw");
        if(this.getFile().length() == 0){
            this.getFile().seek(0);
            this.getFile().writeInt(0);
        } else {
            this.getFile().seek(0);
            last_id = this.getFile().readInt();
        }
    }

    //Update the last ID
    private void updateCab(int key) throws IOException{
        this.getFile().seek(0);
        this.getFile().writeInt(key);
    }

    //Read the last ID
    public int readCab() throws IOException {
        this.getFile().seek(0);
        int lastId = this.getFile().readInt();
        this.getFile().close();
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
        if (file == null) {
            try {
                file = new RandomAccessFile(this.basePath, "rw");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    @Override
    public void close() throws IOException {
        this.file.close();
    }
}