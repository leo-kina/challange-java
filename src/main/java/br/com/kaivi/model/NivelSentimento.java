package br.com.kaivi.model;


public enum NivelSentimento {
    POSITIVO("✅ Positivo", "Cliente engajado e satisfeito"),
    NEUTRO("🔵 Neutro", "Sem sinais fortes — reunião informativa"),
    MISTO("⚠️  Misto", "Sentimentos contraditórios — requer atenção"),
    NEGATIVO("🔴 Negativo", "Insatisfação detectada — risco de churn");

    private final String rotulo;
    private final String descricao;

    NivelSentimento(String rotulo, String descricao) {
        this.rotulo = rotulo;
        this.descricao = descricao;
    }

    public String getRotulo()   { return rotulo; }
    public String getDescricao(){ return descricao; }

    @Override
    public String toString() { return rotulo; }
}
