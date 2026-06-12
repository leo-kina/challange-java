package br.com.kaivi.analyzer;

import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.NivelSentimento;
import br.com.kaivi.model.Transcricao;

import java.util.LinkedHashMap;
import java.util.Map;


public class AnalisadorSentimento extends AnalisadorBase {

    private static final String[] SINAIS_POSITIVOS = {
        "gostei", "adorei", "excelente", "perfeito", "ótimo", "satisfeito",
        "confiança", "recomendo", "melhorou", "funciona bem", "aprovado",
        "quero avançar", "vamos em frente", "faz sentido", "me interessa"
    };

    private static final Map<String[], String> SINAIS_CHURN = new LinkedHashMap<>();

    static {
        SINAIS_CHURN.put(new String[]{"cancelar", "cancelamento", "rescindir"},
                "🚨 Menção direta a cancelamento — acionar Customer Success imediatamente");
        SINAIS_CHURN.put(new String[]{"insatisfeito", "frustrado", "frustração", "decepcionado"},
                "🔴 Insatisfação explícita — escalar para gerência");
        SINAIS_CHURN.put(new String[]{"sofrendo", "problema sério", "não está funcionando"},
                "🔴 Sofrimento operacional relatado — abrir chamado técnico");
        SINAIS_CHURN.put(new String[]{"avaliando outro", "testando outro", "poc com"},
                "⚠️  Cliente em avaliação competitiva — risco real de perda");
        SINAIS_CHURN.put(new String[]{"não mencione", "não fale pro", "sigilo", "confidencial"},
                "⚠️  Pedido de sigilo — pode indicar tensão política interna");
        SINAIS_CHURN.put(new String[]{"roi", "retorno sobre", "não vejo resultado"},
                "⚠️  Questionamento de ROI — preparar case de valor urgente");
        SINAIS_CHURN.put(new String[]{"lento", "demora", "atraso", "trava"},
                "⚠️  Reclamação de performance — verificar SLA e abrir incidente");
    }

    public AnalisadorSentimento() {
        super("Analisador de Sentimento Kaivi", "1.0");
    }

    @Override
    public void analisar(Transcricao transcricao, CartaoInteligencia cartao) {
        String texto = transcricao.getConteudo();
        if (texto == null || texto.isBlank()) return;

        // Detecta sinais de churn
        for (Map.Entry<String[], String> entrada : SINAIS_CHURN.entrySet()) {
            for (String gatilho : entrada.getKey()) {
                if (contemPalavra(texto, gatilho)) {
                    cartao.adicionarAlertaChurn(entrada.getValue()
                            + " " + extrairTrecho(texto, gatilho));
                    break;
                }
            }
        }


        cartao.setPersona(detectarPersona(texto));

        cartao.setSentimento(classificarSentimento(texto, cartao));
    }

    public String detectarPersona(String texto) {
        Map<String, String> cargos = new LinkedHashMap<>();
        cargos.put("cfo",       "CFO — Decisor financeiro (foco em ROI e budget)");
        cargos.put("ceo",       "CEO — Decisor máximo (visão estratégica)");
        cargos.put("cto",       "CTO — Decisor técnico (foco em integração/arquitetura)");
        cargos.put("diretor",   "Diretoria — Decisor estratégico");
        cargos.put("gerente",   "Gerente — Influenciador / usuário-chave");
        cargos.put("coordenador","Coordenador — Usuário operacional");
        cargos.put("compras",   "Área de Compras — Controla aprovação do contrato");
        cargos.put("ti ",       "TI — Influenciador técnico");
        cargos.put("rh ",       "RH — Usuário-chave do módulo de pessoas");

        for (Map.Entry<String, String> cargo : cargos.entrySet()) {
            if (contemPalavra(texto, cargo.getKey())) return cargo.getValue();
        }
        return "Não identificado — verificar hierarquia do cliente";
    }


    private NivelSentimento classificarSentimento(String texto, CartaoInteligencia cartao) {
        int pontos = 0;
        for (String sinal : SINAIS_POSITIVOS) {
            if (contemPalavra(texto, sinal)) pontos++;
        }
        int alertas = cartao.getAlertasChurn().size();

        if (pontos > 0 && alertas > 0) return NivelSentimento.MISTO;
        if (pontos > alertas)          return NivelSentimento.POSITIVO;
        if (alertas > pontos)          return NivelSentimento.NEGATIVO;
        return NivelSentimento.NEUTRO;
    }
}
