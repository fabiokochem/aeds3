package database.crud;

import com.opencsv.exceptions.CsvValidationException;
import comp.MovieObj;
import csv.CSVMovieParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.ParseException;

public class Crud {

    private static int last_id;
    private WorkingStructure archive;

    public void Menu() throws Exception{
        archive = new WorkingStructure("src/main/java/tmp/database.db");
        
        int res = -1;

        while(res != 6){
            System.out.println("Bem-Vindo ao Menu! \n (1) Criar filme \n (2) Ler um filme \n (3) Atualizar um filme \n (4) Deletar um filme \n (5) Histórico de movimentações \n (6) Resetar banco de dados \n (7) Sair");
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            res = Integer.parseInt(br.readLine());

            try {
                if(res == 1)
                    createDesc(archive);
                else if(res == 2)
                    readDesc(archive);
                else if(res == 3)
                    updateDescCRUD(archive);
                else if(res == 4)
                    deleteDesc(archive);
                else if(res == 5)
                    lastIdDesc(archive);
                else if (res == 6)
                    this.reset();
                else if(res != 7) {
                    System.out.println("Opção inválida");
                }
            } catch (Exception e) {
                System.out.println("Erro no banco de dados: " + e.getMessage());
            }
        }
    }

    private void reset() throws CsvValidationException, IOException, ParseException {
        last_id = 0;
        archive.reset();
        for (MovieObj movie : CSVMovieParser.parseCSV("src/main/java/tmp/Movies.csv")) {
            archive.createCRUD(movie);
        }
    }

    private static void createDesc(WorkingStructure archive) throws Exception{
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        String[] arr = new String[8];

        arr[0] = Integer.toString(++last_id);
        System.out.println("Digite o título do filme: ");
        arr[1] = br.readLine();
        System.out.println("Digite a data de lançamento: ");
        arr[2] = br.readLine();
        System.out.println("Digite o ano de lançamento: ");
        arr[3] = br.readLine();
        System.out.println("Digite seu rating na IMDB: ");
        arr[4] = br.readLine();
        System.out.println("Digite seu runtime: ");
        arr[5] = br.readLine();
        System.out.println("Digite os genêros, separados por vírgula: ");
        arr[6] = br.readLine();
        System.out.println("Digite o nome dos diretores, separados por vírgula: ");
        arr[7] = br.readLine();

        MovieObj obj;
        obj = CSVMovieParser.parseLine(arr);
        archive.createCRUD(obj);

        System.out.println("-------------------------");
        System.out.println("FILME CRIADO COM SUCESSO!");
        System.out.println("-------------------------");
    }

    private static void readDesc(WorkingStructure archive) throws Exception{
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

    private static void updateDescCRUD(WorkingStructure archive) throws Exception{
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

    public static void deleteDesc(WorkingStructure archive) throws Exception {
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

    private static void lastIdDesc(WorkingStructure archive) throws Exception{
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
