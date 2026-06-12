package br.com.kaivi.chat;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Mensagem {

    public enum Tipo { USUARIO, KAIVI }

    private Tipo tipo;
    private String conteudo;
    private LocalDateTime enviadaEm;

    public Mensagem(Tipo tipo, String conteudo) {
        this.tipo = tipo;
        this.conteudo = conteudo;
        this.enviadaEm = LocalDateTime.now();
    }

    public Tipo getTipo()       { return tipo; }
    public String getConteudo() { return conteudo; }
    public LocalDateTime getEnviadaEm() { return enviadaEm; }

    @Override
    public String toString() {
        String prefixo = tipo == Tipo.USUARIO ? "  Você" : "  Kaivi";
        String hora = enviadaEm.format(DateTimeFormatter.ofPattern("HH:mm"));
        return String.format("%s [%s]: %s", prefixo, hora, conteudo);
    }
}
