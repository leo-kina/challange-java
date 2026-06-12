package br.com.kaivi.analyzer;

import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.Transcricao;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class AnalisadorOportunidades extends AnalisadorBase {

    private static final Map<String[], String> GATILHOS_COMPRA = new LinkedHashMap<>();
    private static final String[] CONCORRENTES = {
        "Salesforce", "HubSpot", "Pipedrive", "Microsoft Dynamics",
        "Zoho", "Monday", "Notion", "SAP", "Oracle", "Zendesk"
    };

    static {
        GATILHOS_COMPRA.put(new String[]{"expandir", "crescer", "escalar", "novo módulo"},
                "📈 Sinal de expansão — propor módulos adicionais");
        GATILHOS_COMPRA.put(new String[]{"automatizar", "processo manual", "ainda é manual", "na mão"},
                "🤖 Processo manual identificado — demonstrar automação do Kaivi");
        GATILHOS_COMPRA.put(new String[]{"demo", "demonstração", "ver o produto", "apresentação"},
                "🎯 Cliente quer ver o produto — agendar demo técnica");
        GATILHOS_COMPRA.put(new String[]{"integrar", "integração", "conectar sistemas"},
                "🔗 Interesse em integração — apresentar arquitetura do Kaivi");
        GATILHOS_COMPRA.put(new String[]{"contrato", "renovar", "renovação"},
                "📋 Janela de renovação — acelerar proposta comercial");
        GATILHOS_COMPRA.put(new String[]{"prazo", "urgente", "quanto antes", "precisamos rápido"},
                "⚡ Urgência identificada — priorizar follow-up em 24h");
        GATILHOS_COMPRA.put(new String[]{"reunião com o board", "apresentar para diretoria", "aprovação interna"},
                "🏢 Processo de aprovação interna — fornecer materiais executivos");
        GATILHOS_COMPRA.put(new String[]{"fiz as contas", "calculei", "projeção de roi"},
                "💡 Cliente já calculou ROI — momento ideal para fechar proposta");
    }

    private static final Map<String[], String> BARREIRAS_ORCAMENTO = new LinkedHashMap<>();

    static {
        BARREIRAS_ORCAMENTO.put(new String[]{"caro", "muito caro", "não cabe no orçamento"},
                "💸 Objeção de preço — preparar comparativo de custo vs benefício");
        BARREIRAS_ORCAMENTO.put(new String[]{"budget congelado", "verba cortada", "sem budget"},
                "❄️  Budget congelado — explorar opções de pagamento parcelado");
        BARREIRAS_ORCAMENTO.put(new String[]{"aprovação financeira", "precisa aprovar", "nosso cfo"},
                "🔐 Aprovação pendente do financeiro — fornecer ROI documentado");
        BARREIRAS_ORCAMENTO.put(new String[]{"comparando preço", "cotação", "outras propostas"},
                "⚖️  Cliente cotando — diferenciar valor além do preço");
    }

    public AnalisadorOportunidades() {
        super("Analisador de Oportunidades Kaivi", "1.0");
    }

    @Override
    public void analisar(Transcricao transcricao, CartaoInteligencia cartao) {
        String texto = transcricao.getConteudo();
        if (texto == null || texto.isBlank()) return;

        for (Map.Entry<String[], String> entrada : GATILHOS_COMPRA.entrySet()) {
            for (String gatilho : entrada.getKey()) {
                if (contemPalavra(texto, gatilho)) {
                    cartao.adicionarOportunidade(entrada.getValue()
                            + " " + extrairTrecho(texto, gatilho));
                    break;
                }
            }
        }

 
        for (Map.Entry<String[], String> entrada : BARREIRAS_ORCAMENTO.entrySet()) {
            for (String gatilho : entrada.getKey()) {
                if (contemPalavra(texto, gatilho)) {
                    cartao.adicionarBarreiraOrcamento(entrada.getValue()
                            + " " + extrairTrecho(texto, gatilho));
                    break;
                }
            }
        }

        double budget = extrairBudget(texto);
        if (budget > 0) {
            cartao.setBudgetIdentificado(budget);
            cartao.adicionarOportunidade(String.format(
                    "💰 Budget mencionado: R$ %,.2f — lead de alto valor", budget));
        }


        detectarConcorrente(texto, cartao);
    }

  
    public double extrairBudget(String texto) {
        Pattern pattern = Pattern.compile(
                "(?:R\\$|USD|BRL)?\\s*(\\d+[.,]?\\d*)\\s*(mil|k|K)?",
                Pattern.CASE_INSENSITIVE);
        Matcher m = pattern.matcher(texto);

        while (m.find()) {
            try {
                String valorStr = m.group(1).replace(",", ".");
                double valor = Double.parseDouble(valorStr);
                String sufixo = m.group(2);
                if (sufixo != null) valor *= 1000;
                if (valor >= 1000) return valor; // ignora nmeros pequenos sem contexto
            } catch (NumberFormatException ignored) {}
        }
        return 0;
    }

    private void detectarConcorrente(String texto, CartaoInteligencia cartao) {
        for (String concorrente : CONCORRENTES) {
            if (contemPalavra(texto, concorrente)) {
                cartao.setConcorrenteDetectado(concorrente);
                cartao.adicionarAlertaChurn("⚠️  Concorrente mencionado: " + concorrente
                        + " " + extrairTrecho(texto, concorrente));
                return;
            }
        }
    }
}
