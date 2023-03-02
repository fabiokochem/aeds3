package csv;

import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;

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
        movie.setTitle(line[0]);
        movie.setReleaseDate(formatter.parse(line[1]));
        movie.setYear(Short.parseShort(line[2]));
        movie.setImdbRating(Float.parseFloat(line[3]));
        movie.setRuntime(Integer.parseInt(line[4]));
        movie.setGenres(line[5]);
        movie.setDirectors(line[6]);

        return movie;
    }

}
