package database.crud;

import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;
import csv.CSVMovieParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Crud {
    private final WorkingStructure archive;

    public Crud(WorkingStructure archive) {
        this.archive = archive;
    }


    public void Menu() throws Exception{
        for(int res = -1; res != 6;) {
            System.out.println("Bem-Vindo ao Menu! \n (1) Criar filme \n (2) Ler um filme \n (3) Atualizar um filme \n (4) Deletar um filme \n (5) Histórico de movimentações \n (6) Resetar banco de dados \n (7) Sair");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            res = Integer.parseInt(br.readLine());

            try {
                switch (res) {
                    case 1 -> createDesc(archive);
                    case 2 -> readDesc(archive);
                    case 3 -> updateDescCRUD(archive);
                    case 4 -> deleteDesc(archive);
                    case 5 -> lastIdDesc(archive);
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
        archive.reset();
        for (MovieObj movie : CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv")) {
            archive.createCRUD(movie);
        }
    }

    public void createDesc(WorkingStructure archive) throws Exception {
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
        int id = archive.createCRUD(newMovie);

        System.out.println("-------------------------");
        System.out.println("FILME CRIADO COM SUCESSO!");
        System.out.printf("ID: %3d%n", id);
        System.out.println("-------------------------");
    }

    public void readDesc(WorkingStructure archive) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme a ser lido: ");
        int id = Integer.parseInt(br.readLine());

        MovieObj result = archive.readCRUD(id);

        System.out.println("------------------------------------");
        if(result == null) {
            System.out.println("[INCONCLUSIVE!] Nenhum filme possui esse ID!");
        } else {
            System.out.println(archive.readCRUD(id).toString());
        }
        System.out.println("------------------------------------");
    }

    public void updateDescCRUD(WorkingStructure archive) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja atualizar: ");
        int changed = Integer.parseInt(br.readLine());

        if(!archive.deleteCRUD(changed)) {
            System.out.println("[INCONCLUSIVE!] Não foi possível deletar o filme! (ID inexistente)");
        } else {
            String[] arr = new String[8];

            arr[0] = Integer.toString(changed);
            System.out.println("Digite o novo título do filme: ");
            arr[1] = br.readLine();
            System.out.println("Digite a nova data de lançamento: ");
            arr[2] = br.readLine();
            System.out.println("Digite o novo ano de lançamento: ");
            arr[3] = br.readLine();
            System.out.println("Digite seu novo rating na IMDB: ");
            arr[4] = br.readLine();
            System.out.println("Digite seu novo runtime: ");
            arr[5] = br.readLine();
            System.out.println("Digite os seus novos genêros, separados por vírgula: ");
            arr[6] = br.readLine();
            System.out.println("Digite os nomes dos novos diretores, separados por vírgula: ");
            arr[7] = br.readLine();

            MovieObj obj;
            obj = CSVMovieParser.parseLine(arr);
            archive.createCRUD(obj);

            System.out.println("-------------------------------");                    
            System.out.println("FILME ATUALIZADO COM SUCESSO");
            System.out.println("-------------------------------");
        }
    }

    public void deleteDesc(WorkingStructure archive) throws Exception {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Digite o ID do filme que deseja deletar: ");
        int id = Integer.parseInt(br.readLine());

        System.out.println("------------------------------------");
        if(!archive.deleteCRUD(id)) {
            System.out.println("[INCONCLUSIVE!] Não foi possível deletar o filme! (ID inexistente)");
        } else {
            System.out.println("FILME DELETADO COM SUCESSO!");
        }
        System.out.println("------------------------------------");
    }

    private void lastIdDesc(WorkingStructure archive) throws Exception{
        int result = archive.readCab();
        
        System.out.println("------------------------------------");
        if(archive.readCRUD(result) == null) {
            System.out.println("Último ID utilizado: " + result + "(DELETADO NA ÚLTIMA OPERAÇÃO)");
        } else {
            System.out.println("Último ID utilizado: " + result);
        }       
        System.out.println("------------------------------------");
    }
}
