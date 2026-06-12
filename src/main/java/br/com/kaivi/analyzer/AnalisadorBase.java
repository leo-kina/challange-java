package br.com.kaivi.analyzer;

import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.Transcricao;


public abstract class AnalisadorBase {

    protected String nomeAnalisador;
    protected String versao;

    public AnalisadorBase(String nomeAnalisador, String versao) {
        this.nomeAnalisador = nomeAnalisador;
        this.versao = versao;
    }

    public abstract void analisar(Transcricao transcricao, CartaoInteligencia cartao);

 
    protected boolean contemPalavra(String texto, String... palavras) {
        if (texto == null) return false;
        String lower = texto.toLowerCase();
        for (String p : palavras) {
            if (lower.contains(p.toLowerCase())) return true;
        }
        return false;
    }


    protected String extrairTrecho(String texto, String palavraChave) {
        if (texto == null) return "";
        int idx = texto.toLowerCase().indexOf(palavraChave.toLowerCase());
        if (idx < 0) return "";
        int inicio = Math.max(0, idx - 25);
        int fim = Math.min(texto.length(), idx + palavraChave.length() + 40);
        return "\"..." + texto.substring(inicio, fim).trim() + "...\"";
    }

    public String getNomeAnalisador() { return nomeAnalisador; }
    public String getVersao()         { return versao; }

    @Override
    public String toString() {
        return nomeAnalisador + " v" + versao;
    }
}
