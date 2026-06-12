package br.com.kaivi.model;

import java.util.ArrayList;
import java.util.List;


public class CartaoInteligencia {

    private Transcricao transcricao;
    private List<String> oportunidadesVenda;
    private List<String> alertasChurn;
    private List<String> barreiraOrcamento;
    private NivelSentimento sentimento;
    private String persona;
    private double budgetIdentificado;
    private String concorrenteDetectado;
    private double proporcaoFalaVendedor;
    private double proporcaoFalaCliente;
    private int scorePrioridade;

    public CartaoInteligencia(Transcricao transcricao) {
        this.transcricao = transcricao;
        this.oportunidadesVenda = new ArrayList<>();
        this.alertasChurn = new ArrayList<>();
        this.barreiraOrcamento = new ArrayList<>();
        this.sentimento = NivelSentimento.NEUTRO;
        this.persona = "Não identificado";
        this.proporcaoFalaVendedor = transcricao.getProporcaoFalaVendedor();
        this.proporcaoFalaCliente = transcricao.getProporcaoFalaCliente();
    }


    public void gerar() {
        this.scorePrioridade = calcularScore();
    }

    public int calcularScore() {
        int score = 5; // base neutra
        score += Math.min(oportunidadesVenda.size() * 2, 4);
        if (budgetIdentificado > 0) score += 1;
        if (sentimento == NivelSentimento.POSITIVO) score += 1;
        if (sentimento == NivelSentimento.NEGATIVO) score -= 3;
        if (sentimento == NivelSentimento.MISTO)    score -= 1;
        if (!alertasChurn.isEmpty())   score -= alertasChurn.size();
        if (concorrenteDetectado != null) score -= 1;
        return Math.max(0, Math.min(score, 10));
    }

  
    public boolean temRiscoChurn() { return !alertasChurn.isEmpty(); }

 
    public boolean temBarreiraOrcamento() { return !barreiraOrcamento.isEmpty(); }



    public void adicionarOportunidade(String o)    { oportunidadesVenda.add(o); }
    public void adicionarAlertaChurn(String a)     { alertasChurn.add(a); }
    public void adicionarBarreiraOrcamento(String b) { barreiraOrcamento.add(b); }



    public Transcricao getTranscricao()             { return transcricao; }
    public List<String> getOportunidadesVenda()     { return oportunidadesVenda; }
    public List<String> getAlertasChurn()           { return alertasChurn; }
    public List<String> getBarreiraOrcamento()      { return barreiraOrcamento; }
    public NivelSentimento getSentimento()          { return sentimento; }
    public void setSentimento(NivelSentimento s)    { this.sentimento = s; }
    public String getPersona()                      { return persona; }
    public void setPersona(String p)                { this.persona = p; }
    public double getBudgetIdentificado()           { return budgetIdentificado; }
    public void setBudgetIdentificado(double b)     { this.budgetIdentificado = b; }
    public String getConcorrenteDetectado()         { return concorrenteDetectado; }
    public void setConcorrenteDetectado(String c)   { this.concorrenteDetectado = c; }
    public double getProporcaoFalaVendedor()        { return proporcaoFalaVendedor; }
    public double getProporcaoFalaCliente()         { return proporcaoFalaCliente; }
    public int getScorePrioridade()                 { return scorePrioridade; }

    @Override
    public String toString() {
        return String.format("CartaoInteligencia{score=%d, sentimento=%s, oportunidades=%d, alertas=%d}",
                scorePrioridade, sentimento.getRotulo(), oportunidadesVenda.size(), alertasChurn.size());
    }
}
