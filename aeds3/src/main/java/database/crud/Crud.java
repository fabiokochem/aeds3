package database.crud;

import java.io.IOException;
import java.io.RandomAccessFile;

import comp.MovieObj;

public class Crud {

    private final String basePath;

    public int getLast_id() {
        return last_id;
    }

    public void setLast_id(int last_id) {
        this.last_id = last_id;
    }

    private int last_id = -1;

    public Crud(String basePath) {
        this.basePath = basePath;
    }

    public int createCRUD(MovieObj obj) throws IOException {

        try(WorkingStructure archive = new WorkingStructure(basePath)){

            if (obj.getId() < 0) {
                obj.setId(++this.last_id);
            } else if (last_id >= obj.getId()) {
                throw new IllegalArgumentException("[ERROR] The ID (" + obj.getId() + ") is already in use! Last ID: " + last_id);
            }

            archive.append(obj);

            archive.updateCab(obj.getId());
            return obj.getId();
        }

    }

    public MovieObj readCRUD(int key) throws IOException {

        try(WorkingStructure archive = new WorkingStructure(basePath)){
            if(archive.isEmpty()) {
                throw new RuntimeException("[ERROR] The file is EMPTY!");
            }

            MovieObj obj = null;

            archive.file.seek(Integer.BYTES);
            while(archive.notEOF() && (obj == null || obj.getId() != key)) {
                obj = archive.read();
            }

            if(obj == null || obj.getId() != key) obj = null;
            else archive.updateCab(obj.getId());

            return obj;
        }

    }

    public boolean deleteCRUD(int key) throws IOException {

        try(WorkingStructure archive = new WorkingStructure(basePath)) {
            if(archive.isEmpty()) {
                throw new RuntimeException("[ERROR] The file is EMPTY!");
            }

            boolean res;
            MovieObj obj = null;
            long pos = 0;

            while(archive.notEOF() && (obj == null || obj.getId() != key)) {
                pos = archive.getPointer();
                obj = archive.read();
            }

            res = obj != null && obj.getId() == key;
            if(res) {
                archive.delete(pos);
                archive.updateCab(obj.getId());
            }

            return res;
        }

    }

    public void initializeWorkingStructure(){
        try(WorkingStructure archive = new WorkingStructure(basePath)){
            archive.reset();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
