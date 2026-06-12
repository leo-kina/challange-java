package br.com.kaivi.service;

import br.com.kaivi.analyzer.AnalisadorBase;
import br.com.kaivi.analyzer.AnalisadorOportunidades;
import br.com.kaivi.analyzer.AnalisadorSentimento;
import br.com.kaivi.chat.ChatMemoria;
import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.Reuniao;
import br.com.kaivi.model.Transcricao;

import java.util.ArrayList;
import java.util.List;

public class ProcessadorKaivi {

    private final List<AnalisadorBase> analisadores;

    public ProcessadorKaivi() {
        this.analisadores = new ArrayList<>();
        analisadores.add(new AnalisadorSentimento());
        analisadores.add(new AnalisadorOportunidades());
    }


    public SessaoKaivi processar(Reuniao reuniao, String conteudoTranscrito) {
        if (reuniao == null || conteudoTranscrito == null || conteudoTranscrito.isBlank()) {
            throw new IllegalArgumentException("Reunião e conteúdo da transcrição são obrigatórios.");
        }


        Transcricao transcricao = new Transcricao(reuniao, conteudoTranscrito);
        transcricao.processar();

        CartaoInteligencia cartao = new CartaoInteligencia(transcricao);


        for (AnalisadorBase analisador : analisadores) {
            analisador.analisar(transcricao, cartao);
        }


        cartao.gerar();

        ChatMemoria chat = new ChatMemoria(transcricao, cartao);

        return new SessaoKaivi(reuniao, transcricao, cartao, chat);
    }


    public List<SessaoKaivi> processarLote(List<Reuniao> reunioes, List<String> transcricoes) {
        List<SessaoKaivi> sessoes = new ArrayList<>();
        for (int i = 0; i < reunioes.size(); i++) {
            sessoes.add(processar(reunioes.get(i), transcricoes.get(i)));
        }
        return sessoes;
    }

    public int getTotalAnalisadores() { return analisadores.size(); }
}
