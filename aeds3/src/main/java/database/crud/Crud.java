package database.crud;

import comp.MovieObj;

import java.io.IOException;

public class Crud {

    private final String db_path;

    public Crud(String db_path) {
        this.db_path = db_path;
    }

    public int create(MovieObj movieObj) throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            return archive.append(movieObj);
        }
    }

    public MovieObj read(int id) throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            return archive.read(id);
        }
    }

    public boolean update(MovieObj movieObj) throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            return  archive.update(movieObj);
        }
    }

    public boolean delete(int id) throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            return  archive.delete(id);
        }
    }

    public void reset() throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            archive.reset();
        }
    }

    public int lastId() throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            return archive.readCab();
        }
    }
}
