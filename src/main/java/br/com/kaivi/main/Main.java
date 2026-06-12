package br.com.kaivi.main;

import br.com.kaivi.model.Reuniao;
import br.com.kaivi.service.ProcessadorKaivi;
import br.com.kaivi.service.SessaoKaivi;
import br.com.kaivi.util.FormatadorKaivi;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final ProcessadorKaivi processador = new ProcessadorKaivi();
    private static final List<SessaoKaivi> sessoes    = new ArrayList<>();
    private static final Scanner scanner              = new Scanner(System.in);

    public static void main(String[] args) {
        exibirBanner();
        boolean rodando = true;

        while (rodando) {
            exibirMenu();
            String op = scanner.nextLine().trim();
            switch (op) {
                case "1" -> analisarReuniao();
                case "2" -> executarExemplos();
                case "3" -> chatMemoria();
                case "4" -> verRanking();
                case "5" -> { rodando = false; System.out.println("\n  Ate logo! O Kaivi continua ouvindo.\n"); }
                default  -> System.out.println("\n  Opcao invalida. Digite 1 a 5.\n");
            }
        }
        scanner.close();
    }



    private static void exibirBanner() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║                     KAIVI.AL  v1.0                      ║");
        System.out.println("║   Seu assistente invisivel de reunioes de negocios       ║");
        System.out.println("║   Foca 100% na negociacao. O Kaivi anota tudo.           ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.printf("  Analisadores ativos: %d%n%n", processador.getTotalAnalisadores());
    }



    private static void exibirMenu() {
        System.out.println("  ┌──────────────────────────────────┐");
        System.out.println("  │          MENU KAIVI.AL           │");
        System.out.println("  ├──────────────────────────────────┤");
        System.out.println("  │  1. Analisar reuniao manual      │");
        System.out.println("  │  2. Executar exemplos            │");
        System.out.println("  │  3. Chat de Memoria              │");
        System.out.println("  │  4. Ranking de leads             │");
        System.out.println("  │  5. Sair                         │");
        System.out.println("  └──────────────────────────────────┘");
        System.out.print("  Escolha: ");
    }


    private static void analisarReuniao() {
        System.out.println("\n─── Nova Reuniao ───");
        System.out.print("  Titulo da reuniao  : "); String titulo   = scanner.nextLine().trim();
        System.out.print("  Nome do vendedor   : "); String vendedor = scanner.nextLine().trim();
        System.out.print("  Nome do cliente    : "); String cliente  = scanner.nextLine().trim();
        System.out.print("  Empresa do cliente : "); String empresa  = scanner.nextLine().trim();
        System.out.print("  Pais               : "); String pais     = scanner.nextLine().trim();

        System.out.println("  Cole a transcricao (linha em branco para finalizar):");
        StringBuilder sb = new StringBuilder();
        String linha;
        while (!(linha = scanner.nextLine()).isBlank()) sb.append(linha).append(" ");

        if (sb.toString().isBlank()) {
            System.out.println("  Conteudo vazio. Analise cancelada.");
            return;
        }

        Reuniao r = new Reuniao(titulo, vendedor, cliente, empresa, pais);
        SessaoKaivi sessao = processador.processar(r, sb.toString().trim());
        sessoes.add(sessao);
        FormatadorKaivi.imprimirCartao(sessao);
    }

    

    private static void executarExemplos() {
        System.out.println("\n  Carregando reunioes de exemplo...\n");

        List<Reuniao> reunioes = new ArrayList<>();
        List<String>  textos   = new ArrayList<>();


        reunioes.add(new Reuniao(
                "Discovery Call - Expansao LATAM",
                "Ana Lima", "Carlos Mendez", "Grupo Meridian SA", "Argentina"));
        textos.add(
            "Carlos, obrigada por nos receber. Nos temos crescido muito e os processos " +
            "ainda sao manuais, isso esta travando a equipe. Vimos a demo de voces e " +
            "adoramos. Precisamos automatizar antes do segundo semestre, quanto antes melhor. " +
            "Temos verba aprovada de R$ 120 mil para tecnologia esse ano. " +
            "Vamos agendar uma reuniao com o nosso CTO na proxima semana?"
        );

        reunioes.add(new Reuniao(
                "Revisao de Contrato Q2",
                "Pedro Rocha", "Sofia Alves", "Fintech Nova Ltda", "Brasil"));
        textos.add(
            "Pedro, preciso ser honesta. Estamos frustrados com a lentidao do sistema, " +
            "o nosso time de RH esta sofrendo todo mes no fechamento. Ja estamos testando " +
            "o HubSpot para ver se resolve. Nao mencione isso pro nosso CFO ainda, " +
            "ele esta focado no ROI do trimestre. Se voces nao resolverem, vamos cancelar."
        );


        reunioes.add(new Reuniao(
                "Primeiro Contato - Mercado UK",
                "Julia Torres", "James Wright", "Wright Solutions Ltd", "Reino Unido"));
        textos.add(
            "Julia, interessante o que voces fazem. Ainda nao usamos nada parecido. " +
            "Temos reunioes internacionais toda semana e eu perco muito tempo anotando. " +
            "Qual e o processo para comecar? Podemos ver uma demonstracao?"
        );


        reunioes.add(new Reuniao(
                "Upsell - Renovacao Anual",
                "Marcos Silva", "Beatriz Costa", "Costa & Irmãos ME", "Brasil"));
        textos.add(
            "Marcos, a ferramenta de voces melhorou muito nossa operacao, estamos satisfeitos. " +
            "Quero renovar o contrato e tambem expandir para o time de compras. " +
            "Fiz as contas aqui e o retorno sobre investimento esta claro. " +
            "Quanto fica o novo modulo? Tenho aprovacao do diretor para fechar esse mes."
        );

        List<SessaoKaivi> novas = processador.processarLote(reunioes, textos);
        sessoes.addAll(novas);
        novas.forEach(FormatadorKaivi::imprimirCartao);
    }

    

    private static void chatMemoria() {
        if (sessoes.isEmpty()) {
            System.out.println("\n  Nenhuma sessao disponivel. Execute os exemplos primeiro.\n");
            return;
        }

        System.out.println("\n  Sessoes disponiveis:");
        for (int i = 0; i < sessoes.size(); i++) {
            System.out.printf("  [%d] %s — %s%n", i + 1,
                    sessoes.get(i).getReuniao().getTitulo(),
                    sessoes.get(i).getReuniao().getNomeCliente());
        }
        System.out.print("  Escolha a sessao: ");

        int idx;
        try { idx = Integer.parseInt(scanner.nextLine().trim()) - 1; }
        catch (NumberFormatException e) { System.out.println("  Opcao invalida."); return; }
        if (idx < 0 || idx >= sessoes.size()) { System.out.println("  Sessao nao encontrada."); return; }

        SessaoKaivi sessao = sessoes.get(idx);
        System.out.println("\n  Chat de Memoria — " + sessao.getReuniao().getTitulo());
        System.out.println("  (Digite 'sair' para voltar ao menu)\n");

        while (true) {
            System.out.print("  Voce: ");
            String pergunta = scanner.nextLine().trim();
            if (pergunta.equalsIgnoreCase("sair")) break;
            if (pergunta.isBlank()) continue;
            String resposta = sessao.getChat().perguntar(pergunta);
            System.out.println("  Kaivi: " + resposta + "\n");
        }
    }



    private static void verRanking() {
        if (sessoes.isEmpty()) {
            System.out.println("\n  Nenhuma sessao disponivel. Execute os exemplos primeiro.\n");
            return;
        }
        FormatadorKaivi.imprimirRanking(sessoes);
    }
}
