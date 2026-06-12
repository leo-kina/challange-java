package br.com.kaivi.util;

import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.service.SessaoKaivi;

import java.util.List;


public class FormatadorKaivi {

    private static final String LINHA  = "═".repeat(62);
    private static final String LINHA2 = "─".repeat(62);

    public static void imprimirCartao(SessaoKaivi sessao) {
        CartaoInteligencia c = sessao.getCartao();
        System.out.println("\n" + LINHA);
        System.out.println("  KAIVI.AL — CARTAO DE INTELIGENCIA");
        System.out.println(LINHA);
        System.out.println("  Reuniao  : " + sessao.getReuniao().getTitulo());
        System.out.println("  Vendedor : " + sessao.getReuniao().getNomeVendedor());
        System.out.println("  Cliente  : " + sessao.getReuniao().getNomeCliente()
                + " (" + sessao.getReuniao().getEmpresaCliente() + ")");
        System.out.println("  Pais     : " + sessao.getReuniao().getPais());
        System.out.println("  Palavras : " + sessao.getTranscricao().getTotalPalavras());
        System.out.println(LINHA2);

        System.out.println("  OPORTUNIDADES DE VENDA:");
        if (c.getOportunidadesVenda().isEmpty()) {
            System.out.println("    Nenhum gatilho de compra detectado.");
        } else {
            c.getOportunidadesVenda().forEach(o -> System.out.println("    * " + o));
        }
        System.out.println(LINHA2);

        System.out.println("  ALERTAS DE CHURN / RISCO:");
        if (c.getAlertasChurn().isEmpty()) {
            System.out.println("    Nenhum sinal de risco. Cliente aparenta estar satisfeito.");
        } else {
            c.getAlertasChurn().forEach(a -> System.out.println("    * " + a));
        }
        System.out.println(LINHA2);

        System.out.println("  BARREIRAS DE ORCAMENTO:");
        if (c.getBarreiraOrcamento().isEmpty()) {
            System.out.println("    Sem objecoes financeiras registradas.");
        } else {
            c.getBarreiraOrcamento().forEach(b -> System.out.println("    * " + b));
        }
        System.out.println(LINHA2);

        System.out.println("  PERSONA IDENTIFICADA : " + c.getPersona());
        System.out.println("  SENTIMENTO           : " + c.getSentimento());
        if (c.getBudgetIdentificado() > 0)
            System.out.printf("  BUDGET IDENTIFICADO  : R$ %,.2f%n", c.getBudgetIdentificado());
        if (c.getConcorrenteDetectado() != null)
            System.out.println("  CONCORRENTE          : " + c.getConcorrenteDetectado());
        System.out.printf("  TERMOMETRO DE FALA   : Vendedor %.0f%% | Cliente %.0f%%%n",
                c.getProporcaoFalaVendedor(), c.getProporcaoFalaCliente());
        System.out.println("  SCORE DE PRIORIDADE  : " + c.getScorePrioridade() + " / 10");
        System.out.println(LINHA);
    }

    public static void imprimirRanking(List<SessaoKaivi> sessoes) {
        System.out.println("\n" + LINHA);
        System.out.println("  KAIVI.AL — RANKING DE LEADS");
        System.out.println(LINHA);
        System.out.printf("  %-8s %-18s %-18s %-8s%n", "ID", "Vendedor", "Cliente", "Score");
        System.out.println(LINHA2);
        sessoes.stream()
                .sorted((a, b) -> Integer.compare(
                        b.getCartao().getScorePrioridade(),
                        a.getCartao().getScorePrioridade()))
                .forEach(s -> System.out.printf("  %-8s %-18s %-18s  %d/10%n",
                        s.getReuniao().getId(),
                        truncar(s.getReuniao().getNomeVendedor(), 16),
                        truncar(s.getReuniao().getNomeCliente(), 16),
                        s.getCartao().getScorePrioridade()));
        System.out.println(LINHA);
    }

    private static String truncar(String t, int max) {
        if (t == null) return "-";
        return t.length() > max ? t.substring(0, max - 1) + "." : t;
    }
}
