package database.crud;

import comp.MovieObj;

import java.io.EOFException;
import java.io.IOException;
import java.io.RandomAccessFile;

public class WorkingStructure implements AutoCloseable {

    // variaveis ====================================================
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
            obj.setId(this.readCab()+1);
        }

        this.file.seek(this.file.length());
        this.write(obj);

        this.updateCab(obj.getId());
        return obj.getId();
    }

    public MovieObj gotoRegister(int id) throws IOException {
        MovieObj obj = null;
        long pos = 0;
        this.file.seek(0);
        this.file.skipBytes(Integer.BYTES);

        while(!this.isEOF() && (obj == null || obj.getId() != id)) {
            pos = this.file.getFilePointer();
            if (!this.file.readBoolean()) {
                this.file.skipBytes(this.file.readInt());
            } else {
                this.file.seek(pos);
                obj = this.readNext();
            }
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
        //TODO: Não está escrevento o booleano na posição correta

        MovieObj movieObj = this.gotoRegister(key);
        if(movieObj == null) return false;
        else {
            System.out.printf("Pos: %h\n", this.file.getFilePointer());
            this.file.writeBoolean(false);
            this.file.skipBytes(this.file.readInt());
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
        this.file.writeBoolean(true);
        this.file.writeInt(arr.length);
        this.file.write(arr);
    }


    //Read a register

    public MovieObj readNext() throws IOException {

        int size;
        byte[] arr;

        try {
            while (!this.file.readBoolean()) {
                size = this.file.readInt();
                this.file.skipBytes(size);
            }

            size = this.file.readInt();
            arr = new byte[size];

            this.file.read(arr);

        } catch (EOFException e) {
            return null;
        }


        MovieObj movieObj = MovieObj.fromByteArray(arr);
        System.out.println(movieObj.getId());
        return movieObj;
    }


    //Create a database file
    private void initializing() throws IOException {
        if(this.getFile().length() == 0){
            this.getFile().seek(0);
            this.getFile().writeInt(0);
        } else {
            this.getFile().seek(0);
            this.getFile().skipBytes(Integer.BYTES);
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
        return lastId;
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