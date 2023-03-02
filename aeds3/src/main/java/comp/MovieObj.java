package comp;

import java.io.*;
import java.util.Date;

public class MovieObj implements Serializable, Cloneable {

    public int id = -1;
    public String title;
    public Date releaseDate;
    public short year;
    public float imdbRating;
    public int runtime;
    public String genres;
    public String directors;

    public MovieObj(){
        this("", new Date(), (short)-1, -1f, -1, "", "");
    }

    public MovieObj(String title, Date releaseDate, short year, float imdbRating, int runtime, String genres, String directors){
        this.title = title;
        this.releaseDate = releaseDate;
        this.year = year;
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

    public short getYear() {
        return year;
    }

    public void setYear(short year) {
        this.year = year;
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

    public void fromByteArray(byte[] arr) throws IOException {
        ByteArrayInputStream arr_input = new ByteArrayInputStream(arr);
        DataInputStream data_input = new DataInputStream(arr_input);
  
        this.setId(data_input.readInt());
        this.setTitle(data_input.readUTF());
        this.setReleaseDate(new Date(data_input.readLong()));
        this.setYear(data_input.readShort());
        this.setImdbRating(data_input.readFloat());
        this.setRuntime(data_input.readInt());
        this.setGenres(data_input.readUTF());
        this.setDirectors(data_input.readUTF());
    }

    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream ba_output = new ByteArrayOutputStream();
        DataOutputStream data_output = new DataOutputStream(ba_output);

        data_output.writeInt(this.getId());
        data_output.writeUTF(this.getTitle());
        data_output.writeLong(this.getReleaseDate().getTime());
        data_output.writeShort(this.getYear());
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
                ", year=" + year +
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
