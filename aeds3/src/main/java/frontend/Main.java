package frontend;

import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;
import csv.CSVMovieParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import database.crud.Crud;

public class Main {

    private final Crud crud;

    public Main(Crud crud) {
        this.crud = crud;
    }

    public static void main(String[] args) throws Exception {
        Main main = new Main(new Crud("src/main/java/tmp/database.db"));

        main.Menu();
    }

    public void Menu() throws Exception {
        for (int res = -1; res != 6; ) {
            System.out.println("Bem-Vindo ao Menu! \n (1) Criar filme \n (2) Ler um filme \n (3) Atualizar um filme \n (4) Deletar um filme \n (5) Histórico de movimentações \n (6) Resetar banco de dados \n (7) Sair");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            res = Integer.parseInt(br.readLine());

            try {
                switch (res) {
                    case 1 -> createDesc(crud);
                    case 2 -> readDesc(crud);
                    case 3 -> updateDescCRUD(crud);
                    case 4 -> deleteDesc(crud);
                    case 5 -> lastIdDesc(crud);
                    case 6 -> this.reset();
                    case 7 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }
    }


    public void reset() throws CsvValidationException, IOException, ParseException {
        crud.initializeWorkingStructure();
        for (MovieObj movie : CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv")) {
            crud.createCRUD(movie);
        }
    }

    public void createDesc(Crud crud) throws Exception {
        MovieObj newMovie = buildMovie();
        int id = crud.createCRUD(newMovie);

        System.out.println("-------------------------");
        System.out.println("FILME CRIADO COM SUCESSO!");
        System.out.printf("ID: %3d%n", id);
        System.out.println("-------------------------");
    }

    private static MovieObj buildMovie() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        MovieObj newMovie = new MovieObj();


        System.out.println("Digite o título do filme: ");
        newMovie.setTitle(br.readLine());
        while (true) {
            try {
                System.out.println("Digite a data de lançamento (dd/MM/yyyy): ");
                newMovie.setReleaseDate(new SimpleDateFormat("dd/MM/yyyy").parse(br.readLine()));
                break;
            } catch (ParseException e) {
                System.out.println("Data inválida!");
            }
        }
        System.out.println("Digite seu rating na IMDB: ");
        newMovie.setImdbRating(Float.parseFloat(br.readLine()));
        System.out.println("Digite seu runtime: ");
        newMovie.setRuntime(Integer.parseInt(br.readLine()));
        System.out.println("Digite os genêros, separados por vírgula: ");
        newMovie.setGenres(br.readLine());
        System.out.println("Digite o nome dos diretores, separados por vírgula: ");
        newMovie.setDirectors(br.readLine());

        return newMovie;
    }

    public void readDesc(Crud crud) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme a ser lido: ");
        int id = Integer.parseInt(br.readLine());

        MovieObj result = crud.readCRUD(id);

        System.out.println("------------------------------------");
        if (result == null) {
            System.out.println("[INCONCLUSIVE!] Nenhum filme possui esse ID!");
        } else {
            System.out.println(crud.readCRUD(id).toString());
        }
        System.out.println("------------------------------------");
    }

    public void updateDescCRUD(Crud archive) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja atualizar: ");
        int id = Integer.parseInt(br.readLine());

        if (!crud.deleteCRUD(id)) {
            System.out.println("[INCONCLUSIVE!] Não foi possível deletar o filme! (ID inexistente)");
        } else {


            MovieObj obj = buildMovie();
            archive.createCRUD(obj);

            System.out.println("-------------------------------");
            System.out.println("FILME ATUALIZADO COM SUCESSO");
            System.out.println("-------------------------------");
        }
    }

    public void deleteDesc(Crud archive) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja deletar: ");
        int id = Integer.parseInt(br.readLine());

        System.out.println("------------------------------------");
        if (!crud.deleteCRUD(id)) {
            System.out.println("[INCONCLUSIVE!] Não foi possível deletar o filme! (ID inexistente)");
        } else {
            System.out.println("FILME DELETADO COM SUCESSO!");
        }
        System.out.println("------------------------------------");
    }

    private void lastIdDesc(Crud crud) throws Exception {
        int result = crud.getLast_id();

        System.out.println("------------------------------------");
        if (crud.readCRUD(result) == null) {
            System.out.println("Último ID utilizado: " + result + "(DELETADO NA ÚLTIMA OPERAÇÃO)");
        } else {
            System.out.println("Último ID utilizado: " + result);
        }
        System.out.println("------------------------------------");
    }

}
