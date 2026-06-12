package br.com.kaivi.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


public class Reuniao {

    private String id;
    private String titulo;
    private String nomeVendedor;
    private String nomeCliente;
    private String empresaCliente;
    private String pais;
    private List<String> participantes;
    private LocalDateTime dataHoraInicio;
    private LocalDateTime dataHoraFim;
    private StatusTranscricao status;

    
    public Reuniao() {
        this.id = UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        this.participantes = new ArrayList<>();
        this.status = StatusTranscricao.AGUARDANDO;
    }

    
    public Reuniao(String titulo, String nomeVendedor, String nomeCliente,
                   String empresaCliente, String pais) {
        this();
        this.titulo = titulo;
        this.nomeVendedor = nomeVendedor;
        this.nomeCliente = nomeCliente;
        this.empresaCliente = empresaCliente;
        this.pais = pais;
    }



    public void iniciar() {
        this.dataHoraInicio = LocalDateTime.now();
        this.status = StatusTranscricao.EM_ANDAMENTO;
        System.out.println("  [Kaivi] Reunião iniciada. Gravando em silêncio...");
    }

 
    public int encerrar() {
        this.dataHoraFim = LocalDateTime.now();
        this.status = StatusTranscricao.CONCLUIDA;
        int duracao = getDuracaoMinutos();
        System.out.println("  [Kaivi] Reunião encerrada. Duração: " + duracao + " min.");
        return duracao;
    }

    public int getDuracaoMinutos() {
        if (dataHoraInicio == null || dataHoraFim == null) return 0;
        return (int) java.time.Duration.between(dataHoraInicio, dataHoraFim).toMinutes();
    }

    public void adicionarParticipante(String nome) {
        this.participantes.add(nome);
    }

    
    public String getId()               { return id; }
    public String getTitulo()           { return titulo; }
    public void   setTitulo(String t)   { this.titulo = t; }
    public String getNomeVendedor()     { return nomeVendedor; }
    public void   setNomeVendedor(String n) { this.nomeVendedor = n; }
    public String getNomeCliente()      { return nomeCliente; }
    public void   setNomeCliente(String n)  { this.nomeCliente = n; }
    public String getEmpresaCliente()   { return empresaCliente; }
    public void   setEmpresaCliente(String e) { this.empresaCliente = e; }
    public String getPais()             { return pais; }
    public void   setPais(String p)     { this.pais = p; }
    public List<String> getParticipantes() { return participantes; }
    public LocalDateTime getDataHoraInicio() { return dataHoraInicio; }
    public void setDataHoraInicio(LocalDateTime d) { this.dataHoraInicio = d; }
    public StatusTranscricao getStatus() { return status; }
    public void setStatus(StatusTranscricao s) { this.status = s; }

    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String data = dataHoraInicio != null ? dataHoraInicio.format(fmt) : "não iniciada";
        return String.format("[Reunião #%s] %s | Vendedor: %s | Cliente: %s (%s) | %s",
                id, titulo, nomeVendedor, nomeCliente, empresaCliente, data);
    }
}
