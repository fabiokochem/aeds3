package comp;

import java.io.*;
import java.util.Date;

public class MovieObj implements Serializable, Cloneable {

    public int id;
    public String title;
    public Date releaseDate;
    public float imdbRating;
    public int runtime;
    public String genres;
    public String directors;

    public MovieObj(){
        this(-1, "", new Date(), -1f, -1, "", "");
    }

    public MovieObj(int id, String title, Date releaseDate, float imdbRating, int runtime, String genres, String directors){
        this.id = id;
        this.title = title;
        this.releaseDate = releaseDate;
        this.imdbRating = imdbRating;
        this.runtime = runtime;
        this.genres = genres;
        this.directors = directors;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public float getImdbRating() {
        return imdbRating;
    }

    public void setImdbRating(float imdbRating) {
        this.imdbRating = imdbRating;
    }

    public int getRuntime() {
        return runtime;
    }

    public void setRuntime(int runtime) {
        this.runtime = runtime;
    }

    public String getGenres() {
        return genres;
    }

    public void setGenres(String genres) {
        this.genres = genres;
    }

    public String getDirectors() {
        return directors;
    }

    public void setDirectors(String directors) {
        this.directors = directors;
    }

    public static MovieObj fromByteArray(byte[] arr) throws IOException {
        MovieObj obj = new MovieObj();
        ByteArrayInputStream arr_input = new ByteArrayInputStream(arr);
        DataInputStream data_input = new DataInputStream(arr_input);

        //TODO: the array is not being read correctly

        obj.setId(data_input.readInt());
        obj.setTitle(data_input.readUTF());
        obj.setReleaseDate(new Date(data_input.readLong()));
        obj.setImdbRating(data_input.readFloat());
        obj.setRuntime(data_input.readInt());
        obj.setGenres(data_input.readUTF());
        obj.setDirectors(data_input.readUTF());

        return obj;
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream ba_output = new ByteArrayOutputStream();
        DataOutputStream data_output = new DataOutputStream(ba_output);

        data_output.writeInt(this.getId());
        data_output.writeUTF(this.getTitle());
        data_output.writeLong(this.getReleaseDate().getTime());
        data_output.writeFloat(this.getImdbRating());
        data_output.writeInt(this.getRuntime());
        data_output.writeUTF(this.getGenres());
        data_output.writeUTF(this.getDirectors());
        return ba_output.toByteArray();
    }

    @Override
    public String toString() {
        return "MovieObj{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", releaseDate=" + releaseDate +
                ", imdbRating=" + imdbRating +
                ", runtime=" + runtime +
                ", genres='" + genres + '\'' +
                ", directors='" + directors + '\'' +
                '}';
    }

    @Override
    public MovieObj clone() {
        try {
            MovieObj clone = (MovieObj) super.clone();
            clone.releaseDate = (Date) releaseDate.clone();
            return clone;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
