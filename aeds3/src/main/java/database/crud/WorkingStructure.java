package database.crud;

import comp.MovieObj;

import java.io.IOException;
import java.io.RandomAccessFile;

public class WorkingStructure implements AutoCloseable {

    // variaveis ====================================================

    private int last_id = -1;
    public String basePath; 
    public RandomAccessFile file;

    // construtores =================================================

    public WorkingStructure(String path) throws IOException { //"src/main/java/tmp/database.db"
        this.basePath = path;
        this.file = new RandomAccessFile(path, "rw");
        this.initializing();
    }

    // metodos publicos ======================================================

    public int append(MovieObj obj) throws IOException {

        if (obj.getId() < 0) {
            obj.setId(++this.last_id);
        } else if(last_id > obj.getId()) {
            throw new IllegalArgumentException("[ERROR] The ID (" + obj.getId() + ") is already in use! Last available ID: " + last_id);
        }

        this.getFile().seek(this.getFile().length());
        this.getFile().writeBoolean(true);
        this.write(obj);

        this.updateCab(obj.getId());
        return obj.getId();
    }

    public MovieObj gotoRegister(int id) throws IOException {
        MovieObj obj = null;
        long pos = 0;

        while(!this.isEOF() && (obj == null || obj.getId() != id)) {
            pos = this.file.getFilePointer();
            obj = this.readNext();
        }

        if(obj == null || obj.getId() != id) return null;
        else {
            this.file.seek(pos);
            return obj;
        }
    }
    
    public MovieObj read(int key) throws IOException {
        MovieObj obj = null;

        while(!this.isEOF() && (obj == null || obj.getId() != key)) {
            obj = this.readNext();
        }

        return obj != null && obj.getId() == key ? obj : null;
    }

    public boolean delete(int key) throws IOException {
        MovieObj movieObj = this.gotoRegister(key);
        if(movieObj == null) return false;
        else {
            this.file.writeBoolean(false);
            return true;
        }
    }

    public boolean update(MovieObj obj) throws IOException {
        MovieObj movieObj = this.gotoRegister(obj.getId());
        if(movieObj == null) return false;
        else {
            this.write(obj);
            return true;
        }
    }

    //Drop all registers
    public void reset() throws IOException{
        this.file.setLength(0);
        this.initializing();
    }

    public boolean isEmpty() throws IOException{
        return this.file.length() == 0;
    }

    //Initialize the file
    public boolean isEOF() throws IOException{
        return this.file.getFilePointer() == this.file.length();
    }

    //Write a register
    private void write(MovieObj obj) throws IOException{
        byte[] arr = obj.toByteArray();

        this.file.writeInt(arr.length);
        this.file.write(arr);
    }


    //Read a register

    public MovieObj readNext() throws IOException {

        if(isEOF()) return null;

        int size;

        while (!this.file.readBoolean() && !isEOF()) {
            size = this.file.readInt();
            this.file.skipBytes(size);
        }

        if(isEOF()) return null;

        size = this.file.readInt();
        byte[] arr = new byte[size];

        this.file.read(arr);
        
        return MovieObj.fromByteArray(arr);
    }


    //Create a database file
    private void initializing() throws IOException {
        if(this.getFile().length() == 0){
            this.getFile().seek(0);
            this.getFile().writeInt(0);
            last_id = 0;
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

    public RandomAccessFile getFile() {return file;}

    @Override
    public void close() throws IOException {
        this.file.close();
    }

}