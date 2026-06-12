package br.com.kaivi.model;


public enum StatusTranscricao {
    AGUARDANDO("Aguardando início da reunião"),
    EM_ANDAMENTO("Reunião em andamento — gravando"),
    CONCLUIDA("Transcrição finalizada e disponível"),
    ERRO("Falha durante a transcrição");

    private final String descricao;

    StatusTranscricao(String descricao) { this.descricao = descricao; }

    public String getDescricao() { return descricao; }

    @Override
    public String toString() { return descricao; }
}
