package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.model.DadosSerie;
import br.com.alura.screenmatch.model.DadosTemporada;
import br.com.alura.screenmatch.service.ConsumoApi;
import br.com.alura.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Principal {
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY  = "&apikey=4521711a";
    private ConverteDados conversor = new ConverteDados();

    public void exibeMenu(){
        try {
            // busca a serie pelo nome
            System.out.println("Digite o nome da série para busca: ");
            var nomeSerie = leitura.nextLine();
            var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);

            // Converte o JSON recebido em uma classe e exibe dados da serie
            DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
            System.out.println(dados);

            // Exibe dados de cada temporada
            List<DadosTemporada> temporadas = new ArrayList<>();
            for (int i = 1; i <= dados.totalTemporadas(); i++) {
                json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }
            temporadas.forEach(System.out::println);

            //Exibe o título de cada temporada
            temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));
        }catch (NullPointerException e){
            System.out.println("Título não informado");
        }catch (Exception e){
            System.out.println("Falha ao buscar dados\n" + e);
        }
    }
}
