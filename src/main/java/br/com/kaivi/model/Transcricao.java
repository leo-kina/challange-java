package br.com.kaivi.model;

import java.time.LocalDateTime;
import java.util.UUID;

public class Transcricao {

    private String id;
    private Reuniao reuniao;
    private String conteudo;
    private String idioma;
    private int totalPalavras;
    private double proporcaoFalaVendedor;   
    private double proporcaoFalaCliente;
    private StatusTranscricao status;
    private LocalDateTime geradaEm;

    public Transcricao() {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.status = StatusTranscricao.AGUARDANDO;
        this.idioma = "pt-BR";
        this.geradaEm = LocalDateTime.now();
    }

    public Transcricao(Reuniao reuniao, String conteudo) {
        this();
        this.reuniao = reuniao;
        this.conteudo = conteudo;
        this.totalPalavras = conteudo != null ? conteudo.split("\\s+").length : 0;
        this.status = StatusTranscricao.CONCLUIDA;
    }

    


    public void processar() {
        if (conteudo == null || conteudo.isBlank()) {
            this.status = StatusTranscricao.ERRO;
            return;
        }
        this.totalPalavras = conteudo.split("\\s+").length;
    
        long linhasVendedor = conteudo.lines()
                .filter(l -> reuniao != null &&
                        l.toLowerCase().startsWith(reuniao.getNomeVendedor().toLowerCase().split(" ")[0]))
                .count();
        long totalLinhas = conteudo.lines().count();
        if (totalLinhas > 0) {
            this.proporcaoFalaVendedor = (double) linhasVendedor / totalLinhas * 100;
            this.proporcaoFalaCliente = 100 - this.proporcaoFalaVendedor;
        }
        this.status = StatusTranscricao.CONCLUIDA;
    }

    public String getResumo() {
        if (conteudo == null || conteudo.isBlank()) return "(sem conteúdo)";
        return conteudo.length() > 100 ? conteudo.substring(0, 100) + "..." : conteudo;
    }

 
    public String exportar() {
        return String.format("=== TRANSCRIÇÃO #%s ===\nReunião: %s\nIdioma: %s\nPalavras: %d\n\n%s",
                id,
                reuniao != null ? reuniao.getTitulo() : "N/A",
                idioma,
                totalPalavras,
                conteudo);
    }


    public String getId()               { return id; }
    public Reuniao getReuniao()         { return reuniao; }
    public void setReuniao(Reuniao r)   { this.reuniao = r; }
    public String getConteudo()         { return conteudo; }
    public void setConteudo(String c)   { this.conteudo = c; this.totalPalavras = c.split("\\s+").length; }
    public String getIdioma()           { return idioma; }
    public void setIdioma(String i)     { this.idioma = i; }
    public int getTotalPalavras()       { return totalPalavras; }
    public double getProporcaoFalaVendedor() { return proporcaoFalaVendedor; }
    public void setProporcaoFalaVendedor(double p) { this.proporcaoFalaVendedor = p; proporcaoFalaCliente = 100 - p; }
    public double getProporcaoFalaCliente()  { return proporcaoFalaCliente; }
    public StatusTranscricao getStatus() { return status; }
    public void setStatus(StatusTranscricao s) { this.status = s; }
    public LocalDateTime getGeradaEm()  { return geradaEm; }

    @Override
    public String toString() {
        return String.format("[Transcrição #%s] %d palavras | Fala vendedor: %.0f%% | Status: %s",
                id, totalPalavras, proporcaoFalaVendedor, status.name());
    }
}
