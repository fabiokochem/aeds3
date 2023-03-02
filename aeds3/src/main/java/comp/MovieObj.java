package comp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MovieObj implements Serializable {

    public int id = -1;
    public String title;
    public Date releaseDate;
    public short year;
    public float imdbRating;
    public int runtime;
    public String genres;
    public String directors;

    public MovieObj(){
        this("", new Date(), (short)-1, (float)-1, -1, "", "");
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

    public MovieObj createFrom(String[] arr) throws Exception {
        MovieObj obj = new MovieObj();
        obj.setId(Integer.parseInt(arr[0]));
        obj.setTitle(arr[1]);
        obj.setReleaseDate(new SimpleDateFormat("dd/MM/yyyy").parse(arr[2]));
        obj.setYear(Short.valueOf(arr[3]));
        obj.setImdbRating(Float.parseFloat(arr[4]));
        obj.setRuntime(Integer.parseInt(arr[5]));
        obj.setGenres(arr[6]);
        obj.setDirectors(arr[7]);

        return obj;
    }

    @Override
    public String toString() {
        String s = "";
        s += "ID: " + this.getId() + "; \n";
        s += "Título: " + this.getTitle() + "; \n";
        s += "Data de lançamento: " + this.getReleaseDate().toString() + "; \n";
        s += "Ano de lançamento: " + this.getYear() + "; \n";
        s += "Rating da IMDB: " + this.getImdbRating() + "; \n";
        s += "Minutagem: " + this.getRuntime() + "; \n";
        s += "Gêneros: " + this.getGenres() + "; \n";
        s += "Diretores: " + this.getDirectors();

        return s;
    }


}
