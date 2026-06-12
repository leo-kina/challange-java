package br.com.kaivi.chat;

import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.Transcricao;

import java.util.ArrayList;
import java.util.List;


public class ChatMemoria {

    private Transcricao transcricao;
    private CartaoInteligencia cartao;
    private List<Mensagem> historico;
    private String sessaoId;
    private int limiteContexto;

    public ChatMemoria(Transcricao transcricao, CartaoInteligencia cartao) {
        this.transcricao = transcricao;
        this.cartao = cartao;
        this.historico = new ArrayList<>();
        this.sessaoId = "SESS-" + transcricao.getId();
        this.limiteContexto = 20; // máximo de mensagens mantidas em memória
    }


    public String perguntar(String pergunta) {
        Mensagem msgUsuario = new Mensagem(Mensagem.Tipo.USUARIO, pergunta);
        historico.add(msgUsuario);

        String resposta = buscarContexto(pergunta);

        Mensagem msgKaivi = new Mensagem(Mensagem.Tipo.KAIVI, resposta);
        historico.add(msgKaivi);

        if (historico.size() > limiteContexto) {
            historico.subList(0, 2).clear();
        }

        return resposta;
    }

   
    public String buscarContexto(String pergunta) {
        String q = pergunta.toLowerCase();
        String texto = transcricao.getConteudo() != null ? transcricao.getConteudo() : "";

        if (q.contains("budget") || q.contains("valor") || q.contains("quanto")
                || q.contains("preço") || q.contains("dinheiro")) {
            if (cartao.getBudgetIdentificado() > 0) {
                return String.format("Na reunião com %s, o valor mencionado foi R$ %,.2f. %s",
                        transcricao.getReuniao().getNomeCliente(),
                        cartao.getBudgetIdentificado(),
                        cartao.temBarreiraOrcamento()
                                ? "Atenção: houve barreiras de orçamento: " + cartao.getBarreiraOrcamento().get(0)
                                : "Não houve objeções de preço registradas.");
            }
            return "Não identifiquei menção de valores financeiros nessa reunião.";
        }


        if (q.contains("risco") || q.contains("churn") || q.contains("cancelamento")
                || q.contains("insatisfação") || q.contains("problema")) {
            if (cartao.temRiscoChurn()) {
                return "Alertas de risco detectados nessa reunião:\n"
                        + String.join("\n", cartao.getAlertasChurn());
            }
            return "Nenhum sinal de risco identificado nessa reunião. Cliente aparentava estar satisfeito.";
        }

        if (q.contains("oportunidade") || q.contains("venda") || q.contains("upsell")
                || q.contains("próximo passo") || q.contains("follow")) {
            if (!cartao.getOportunidadesVenda().isEmpty()) {
                return "Oportunidades identificadas:\n"
                        + String.join("\n", cartao.getOportunidadesVenda());
            }
            return "Não identifiquei gatilhos de compra explícitos nessa reunião.";
        }


        if (q.contains("sentimento") || q.contains("clima") || q.contains("humor")
                || q.contains("satisfeito") || q.contains("como foi")) {
            return String.format("Sentimento geral da reunião: %s\nPersona identificada: %s\nTermômetro de fala — Vendedor: %.0f%% | Cliente: %.0f%%",
                    cartao.getSentimento().getDescricao(),
                    cartao.getPersona(),
                    cartao.getProporcaoFalaVendedor(),
                    cartao.getProporcaoFalaCliente());
        }


        if (q.contains("concorrente") || q.contains("competitor") || q.contains("outro sistema")) {
            if (cartao.getConcorrenteDetectado() != null) {
                return "Concorrente mencionado na conversa: " + cartao.getConcorrenteDetectado()
                        + ". Recomendo preparar um comparativo antes do próximo contato.";
            }
            return "Nenhum concorrente foi citado durante essa reunião.";
        }

   
        String[] termos = q.split("\\s+");
        for (String termo : termos) {
            if (termo.length() > 3 && texto.toLowerCase().contains(termo)) {
                String trecho = extrairTrecho(texto, termo);
                if (!trecho.isEmpty()) {
                    return "Encontrei referência a \"" + termo + "\" na transcrição: " + trecho;
                }
            }
        }

        return "Não encontrei informações específicas sobre isso nessa reunião. "
                + "Tente perguntar sobre: budget, risco de churn, oportunidades, sentimento ou concorrentes.";
    }

    private String extrairTrecho(String texto, String palavra) {
        int idx = texto.toLowerCase().indexOf(palavra.toLowerCase());
        if (idx < 0) return "";
        int inicio = Math.max(0, idx - 30);
        int fim = Math.min(texto.length(), idx + palavra.length() + 50);
        return "\"..." + texto.substring(inicio, fim).trim() + "...\"";
    }

    public List<Mensagem> getHistorico()  { return historico; }
    public String getSessaoId()           { return sessaoId; }
    public int getLimiteContexto()        { return limiteContexto; }
    public void setLimiteContexto(int l)  { this.limiteContexto = l; }

    public void imprimirHistorico() {
        System.out.println("\n  ── Histórico do Chat de Memória (" + sessaoId + ") ──");
        for (Mensagem msg : historico) {
            System.out.println(msg);
        }
    }
}
