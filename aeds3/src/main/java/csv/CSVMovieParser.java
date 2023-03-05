package csv;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CSVMovieParser {

    public static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public static List<MovieObj> parseCSV(String path) throws IOException, CsvValidationException, ParseException {
        CSVReader reader = new CSVReader(new FileReader(path));
        String[] line;
        List<MovieObj> movies = new ArrayList<>();

        while ((line = reader.readNext()) != null) {
            movies.add(parseLine(line));
        }
        reader.close();
        return movies;
    }



    public static MovieObj parseLine(String[] line) throws ParseException {
        MovieObj movie = new MovieObj();
        movie.setTitle(line[1]);
        movie.setReleaseDate(formatter.parse(line[2]));
        movie.setImdbRating(Float.parseFloat(line[4]));
        movie.setRuntime(Integer.parseInt(line[5]));
        movie.setGenres(line[6]);
        movie.setDirectors(line[7]);

        return movie;
    }

}
