package database.crud;

import comp.MovieObj;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Iterator;

public class WorkingStructure implements AutoCloseable {

    // variaveis ====================================================
    public String basePath; 
    public RandomAccessFile file;

    // construtores =================================================

    public WorkingStructure(String path) throws IOException { //"src/main/java/tmp/database.db"
        this.basePath = path;
        this.file = new RandomAccessFile(this.basePath, "rw");
        this.initializing();
    }

    // metodos publicos ======================================================

    //Drop all registers
    public void reset() throws IOException{
        this.file.setLength(0);
        this.initializing();
        this.file.close();
    }

    public boolean isEmpty() throws IOException{
        return this.file.length() == Integer.BYTES;
    }

    //Write a register
    public void write(MovieObj obj) throws IOException{
        byte[] arr = obj.toByteArray();

        this.file.writeBoolean(true);
        this.file.writeInt(arr.length);
        this.file.write(arr);
    }

    public void append(MovieObj obj) throws IOException {
        byte[] arr = obj.toByteArray();

        this.file.seek(this.getFile().length());
        this.file.writeBoolean(true);
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

    public void delete(long pos) throws IOException {
        this.getFile().seek(pos);
        this.file.writeBoolean(false);
    }

    public boolean notEOF() throws IOException{
        return this.file.getFilePointer() < this.file.length();

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
    public void initializing() throws IOException {
        if(this.getFile().length() == 0){
            this.getFile().seek(0);
            this.getFile().writeInt(0);
        } else {
            this.getFile().seek(0);
            this.file.skipBytes(Integer.BYTES);
        }
    }

    //Update the last ID
    public void updateCab(int key) throws IOException{
        this.getFile().seek(0);
        this.getFile().writeInt(key);
    }

    //Read the last ID
    public int readCab() throws IOException {
        this.file.seek(0);
        return this.file.readInt();
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

    public long getPointer() throws IOException {
        return this.getFile().getFilePointer();
    }


    @Override
    public void close() throws IOException {
        this.file.close();
    }
}