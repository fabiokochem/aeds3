package frontend;

import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;
import csv.CSVMovieParser;
import database.crud.Crud;
import database.crud.WorkingStructure;
import database.sorting.intercalation.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Main {

    public static String db_path = "aeds3/src/main/java/tmp/database.db";
    public static Crud crud = new Crud(db_path);

    public static void main(String[] args) throws Exception {
        System.out.println();
        Menu();
    }

    // Interface de usuario

    public static void Menu() throws Exception{
        for(int res = -1; res != 11;) {
            System.out.println("Bem-Vindo ao Menu! \n (1) Criar filme \n (2) Ler um filme \n (3) Atualizar um filme \n (4) Deletar um filme \n (5) Ultimo ID \n (6) Resetar banco de dados \n (7) Limpar Banco de dados \n (8) Intercalação de Ordenação básica \n (9) Intercalação de ordenação Variável \n (10) Intercalação de ordenação com Heap \n (11) Sair");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            res = Integer.parseInt(br.readLine());

            try {
                switch (res) {
                    case 1 -> create();
                    case 2 -> readDesc();
                    case 3 -> updateDesc();
                    case 4 -> deleteDesc();
                    case 5 -> lastIdDesc();
                    case 6 -> reset();
                    case 7 -> drop();
                    case 8 -> IntercalationBasicSortDesc();
                    case 9 -> IntercalationVaryingSortDesc();
                    case 10 -> IntercalationSelectionSortDes();
                    case 11 -> System.out.println("Saindo...");
                    default -> System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }
    }

    public static void create() throws Exception {
        MovieObj movieObj = promptMovieObj();
        int id = crud.create(movieObj);

        System.out.println("-------------------------");
        System.out.println("FILME CRIADO COM SUCESSO!");
        System.out.printf("ID: %3d%n", id);
        System.out.println("-------------------------");
    }

    public static void drop() throws IOException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            archive.reset();
        }
    }

    public static void reset() throws CsvValidationException, IOException, ParseException {
        try (WorkingStructure archive = new WorkingStructure(db_path)) {
            archive.reset();
            for (MovieObj movie : CSVMovieParser.parseCSV("aeds3/src/main/java/tmp/Movies.csv")) {
                archive.append(movie);
            }
        }
    }

    private static MovieObj promptMovieObj() throws IOException {
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

    public static void readDesc() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme a ser lido: ");
        int id = Integer.parseInt(br.readLine());


        MovieObj result = crud.read(id);

        System.out.println("------------------------------------");
        if (result == null) {
            System.out.println("[INCONCLUSIVE!] Nenhum filme possui esse ID!");
        } else {
            System.out.println(result);
        }

        System.out.println("------------------------------------");
    }

    public static void updateDesc() throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja atualizar: ");
        int changed = Integer.parseInt(br.readLine());

        MovieObj movieObj = promptMovieObj();
        movieObj.setId(changed);

        if (!crud.update(movieObj)) {
            System.out.println("[ERROR] Não foi possível deletar o filme! (ID inexistente)");
        } else {
            System.out.println("-------------------------------");
            System.out.println("FILME (ID: "+ movieObj.getId() + ") ATUALIZADO COM SUCESSO");
            System.out.println("-------------------------------");
        }
    }

    public static void deleteDesc() throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja deletar: ");
        int id = Integer.parseInt(br.readLine());

        System.out.println("------------------------------------");
        if (!crud.delete(id)) {
            System.out.println("[ERROR] Não foi possível deletar o filme! (ID inexistente)");
        } else {
            System.out.println("FILME DELETADO COM SUCESSO!");
        }
        System.out.println("------------------------------------");
    }

    private static void lastIdDesc() throws Exception{

        System.out.println("------------------------------------");
        System.out.println("ÚLTIMO ID: " + crud.lastId());
        System.out.println("------------------------------------");
    }

    public static void IntercalationBasicSortDesc() throws IOException{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int rpb, ways;

        System.out.print("Digite a quantidade de registros por bloco: ");
        rpb = Integer.parseInt(br.readLine());
        System.out.println(rpb);
        System.out.print("Digite a quantidade de caminhos: ");
        ways = Integer.parseInt(br.readLine());
        System.out.println(ways);

        try (IntercalationBasicSort is = new IntercalationSelectionSort(rpb, ways, db_path)){
            is.intercalate(is.distribution());
            is.overWriteDB();
        }
    }

    public static void IntercalationVaryingSortDesc() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int rpb, ways;

        System.out.print("Digite a quantidade de registros por bloco: ");
        rpb = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.print("Digite a quantidade de caminhos: ");
        ways = Integer.parseInt(br.readLine());
        System.out.println();

        try(IntercalationVaryingSort is = new IntercalationVaryingSort(rpb, ways, db_path)){
            is.intercalate(is.distribution());
            is.overWriteDB();
        }
    }

    public static void IntercalationSelectionSortDes() throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int rpb, ways;

        System.out.print("Digite a quantidade de registros por bloco: ");
        rpb = Integer.parseInt(br.readLine());
        System.out.println();
        System.out.print("Digite a quantidade de caminhos: ");
        ways = Integer.parseInt(br.readLine());
        System.out.println();

        try(IntercalationSelectionSort is = new IntercalationSelectionSort(rpb, ways, db_path)) {
            is.intercalate(is.distribution());
            is.overWriteDB();
        }
    }
}
