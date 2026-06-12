# KAIVI.AI v1.0
### *Seu assistente invisível de reuniões de negócios*

> Foca 100% na negociação. O Kaivi anota tudo.

O **Kaivi.AI** é um sistema inteligente desenvolvido em Java puro que analisa transcrições de reuniões comerciais e transforma texto bruto em inteligência acionável para equipes de vendas. Sem depender de APIs externas ou bibliotecas de terceiros, o Kaivi usa análise lexical baseada em dicionários de palavras-chave e expressões regulares para detectar automaticamente oportunidades, riscos, orçamentos, concorrentes e o clima da conversa — tudo em tempo real.

---

## 🎯 O que o Kaivi faz?

Imagine que você acabou de sair de uma reunião com um cliente. Em vez de gastar 30 minutos revisando suas anotações, você cola a transcrição no Kaivi e em segundos recebe:

- 📈 **Quais gatilhos de compra foram ativados** (*"precisamos automatizar", "demo", "renovar contrato"*)
- 💰 **Qual orçamento o cliente mencionou** (*extração automática de valores como "R$ 120 mil" ou "120k"*)
- 🚨 **Se há risco real de churn** (*detecção de frustrações, reclamações de performance ou menções a concorrentes*)
- 🎭 **Qual é a persona do decisor** (*CFO, CEO, CTO, Gerente, TI, RH...*)
- 📊 **Um score de prioridade de 0 a 10** para que você saiba em qual lead investir tempo primeiro
- 💬 **Um chat contextual** onde você pode perguntar sobre a reunião em linguagem natural

---

## 🚀 Funcionalidades

### 1. Análise de Reunião Manual
Você informa o título, vendedor, cliente, empresa e país — depois cola a transcrição diretamente no terminal. O Kaivi processa tudo instantaneamente e exibe o **Cartão de Inteligência** completo.

### 2. Exemplos Pré-carregados
O sistema vem com 4 cenários reais simulados para demonstração imediata:
| Reunião | Contexto |
|---------|----------|
| Discovery Call - Expansão LATAM | Cliente com urgência e budget aprovado de R$120k |
| Revisão de Contrato Q2 | Cliente frustrado, testando o HubSpot (risco de churn) |
| Primeiro Contato - Mercado UK | Lead frio, demonstrando interesse inicial |
| Upsell - Renovação Anual | Cliente satisfeito querendo expandir com aprovação do diretor |

### 3. Chat de Memória Contextual
Após a análise, você pode conversar com o Kaivi sobre aquela reunião específica usando linguagem natural. O chat entende a intenção da sua pergunta e busca a informação correta no cartão e na transcrição.

**Exemplos de perguntas que o chat entende:**
- *"Qual foi o budget mencionado?"*
- *"Tem algum risco de cancelamento?"*
- *"Como foi o sentimento geral?"*
- *"Algum concorrente foi citado?"*
- *"Quais são as oportunidades de venda?"*

### 4. Ranking de Leads
Exibe todos os leads analisados ordenados por score, para que a equipe comercial priorize o tempo com eficiência.

---

## 🔬 Como o Sistema Funciona por Dentro

### Pipeline de Processamento

Quando uma transcrição é enviada, o Kaivi executa o seguinte fluxo:

```
Entrada (texto bruto)
       │
       ▼
┌─────────────────┐
│   Reuniao.java  │  ← Metadados: título, vendedor, cliente, país
└────────┬────────┘
         │
         ▼
┌──────────────────────┐
│  Transcricao.java    │  ← Tokeniza o texto, conta palavras
│                      │     Calcula proporção de fala:
│  vendedor vs cliente │     (quem falou mais?)
└──────────┬───────────┘
           │
           ▼
┌──────────────────────────────────────────┐
│         ProcessadorKaivi.java            │
│  Orquestra os analisadores em pipeline   │
│                                          │
│  ┌─────────────────────────────────┐     │
│  │  AnalisadorSentimento.java      │     │
│  │  → Detecta: persona, sentimento │     │
│  │  → Emite: alertas de churn      │     │
│  └─────────────────────────────────┘     │
│                                          │
│  ┌──────────────────────────────────┐    │
│  │  AnalisadorOportunidades.java    │    │
│  │  → Detecta: gatilhos de compra   │    │
│  │  → Extrai: budget via Regex      │    │
│  │  → Detecta: concorrentes e       │    │
│  │             barreiras de budget  │    │
│  └──────────────────────────────────┘   │
└──────────────────┬───────────────────────┘
                   │
                   ▼
        ┌───────────────────┐
        │ CartaoInteligencia│  ← Consolida tudo + calcula Score (0-10)
        └────────┬──────────┘
                 │
                 ▼
        ┌────────────────┐
        │  ChatMemoria   │  ← Permite perguntas contextuais
        └────────────────┘
```

### Como o Score de Prioridade é Calculado

O score começa em **5 pontos** (base neutra) e é ajustado dinamicamente:

| Condição | Impacto |
|----------|---------|
| Cada oportunidade de venda detectada | +2 pts (máx. +4) |
| Budget identificado na conversa | +1 pt |
| Sentimento positivo | +1 pt |
| Sentimento misto | -1 pt |
| Sentimento negativo | -3 pts |
| Cada alerta de churn/risco | -1 pt por alerta |
| Concorrente mencionado | -1 pt |

O score final é limitado entre **0 e 10**.

### Como a Detecção de Sentimento Funciona

O `AnalisadorSentimento` opera com dois conjuntos de dados:

**Sinais positivos** (aumentam a contagem positiva):
```
"gostei", "adorei", "excelente", "perfeito", "ótimo", "satisfeito",
"quero avançar", "faz sentido", "me interessa", "melhorou"...
```

**Gatilhos de churn** (cada um gera um alerta específico):
```
"cancelar"       → 🚨 Acionar Customer Success imediatamente
"frustrado"      → 🔴 Escalar para gerência
"sofrendo"       → 🔴 Abrir chamado técnico
"testando outro" → ⚠️  Cliente em avaliação competitiva
"não mencione"   → ⚠️  Tensão política interna
"lento / trava"  → ⚠️  Reclamação de performance
```

A classificação final:
- Se há **mais positivos que alertas** → `POSITIVO`
- Se há **mais alertas que positivos** → `NEGATIVO`
- Se há **ambos** → `MISTO`
- Se não há nenhum → `NEUTRO`

### Como a Extração de Budget Funciona

O `AnalisadorOportunidades` usa uma expressão regular para capturar valores monetários em múltiplos formatos:

```java
Pattern.compile("(?:R\\$|USD|BRL)?\\s*(\\d+[.,]?\\d*)\\s*(mil|k|K)?")
```

Isso permite capturar:
- `"R$ 120 mil"` → 120.000,00
- `"120k"` → 120.000,00
- `"USD 5000"` → 5.000,00

Apenas valores acima de **R$ 1.000** são considerados para evitar falsos positivos.

### Como o Chat Contextual Funciona

O `ChatMemoria` não é um chatbot de IA — ele é um **motor de busca de intenção contextual**. Quando você faz uma pergunta, ele:

1. Converte a pergunta para minúsculas
2. Verifica se contém palavras-chave de alguma categoria:
   - `budget / valor / quanto / preço` → consulta o valor extraído
   - `risco / churn / cancelamento / problema` → lista alertas de churn
   - `oportunidade / venda / follow` → lista gatilhos detectados
   - `sentimento / clima / como foi` → retorna análise de sentimento + termômetro de fala
   - `concorrente / competitor` → informa o concorrente detectado
3. Se nenhuma categoria bater, faz uma **busca textual direta** na transcrição original pelos termos da pergunta
4. Mantém um **histórico das últimas 20 mensagens** para contexto da conversa

---

## 🏗️ Estrutura de Pacotes

```
src/main/java/br/com/kaivi/
│
├── main/
│   └── Main.java                    → Ponto de entrada; menu interativo no console
│
├── model/                           → Entidades de domínio
│   ├── Reuniao.java                 → Metadados da reunião (título, vendedor, cliente...)
│   ├── Transcricao.java             → Conteúdo textual + cálculo de proporção de fala
│   ├── CartaoInteligencia.java      → Consolidação dos insights + cálculo do score
│   ├── NivelSentimento.java         → Enum: POSITIVO, NEUTRO, MISTO, NEGATIVO
│   └── StatusTranscricao.java       → Enum: AGUARDANDO, EM_ANDAMENTO, CONCLUIDA, ERRO
│
├── analyzer/                        → Motores de análise (Strategy Pattern)
│   ├── AnalisadorBase.java          → Classe abstrata com utilitários compartilhados
│   ├── AnalisadorSentimento.java    → Detecta persona, sentimento e riscos de churn
│   └── AnalisadorOportunidades.java → Extrai orçamentos, oportunidades e concorrentes
│
├── service/                         → Camada de orquestração
│   ├── ProcessadorKaivi.java        → Executa o pipeline de análise (single e batch)
│   └── SessaoKaivi.java             → Agrupa Reunião + Transcrição + Cartão + Chat
│
├── chat/                            → Chatbot contextual
│   ├── ChatMemoria.java             → Motor de busca de intenção + histórico de mensagens
│   └── Mensagem.java                → Representa uma mensagem (USUARIO ou KAIVI)
│
└── util/
    └── FormatadorKaivi.java         → Formatação visual dos cartões e ranking no console
```

---

## ⚡ Como Compilar e Executar

**Pré-requisito:** Java JDK 17 ou superior instalado.

### Compilar
```bash
mkdir bin
javac -d bin -sourcepath src/main/java src/main/java/br/com/kaivi/main/Main.java
```

### Executar
```bash
java -cp bin br.com.kaivi.main.Main
```

---

## 📊 Exemplo de Saída

Após processar a reunião *"Discovery Call - Expansão LATAM"*, o Kaivi gera:

```
══════════════════════════════════════════════════════════════
  KAIVI.AL — CARTAO DE INTELIGENCIA
══════════════════════════════════════════════════════════════
  Reuniao  : Discovery Call - Expansao LATAM
  Vendedor : Ana Lima
  Cliente  : Carlos Mendez (Grupo Meridian SA)
  Pais     : Argentina
  Palavras : 53
──────────────────────────────────────────────────────────────
  OPORTUNIDADES DE VENDA:
    * 🤖 Processo manual identificado — demonstrar automação do Kaivi
    * 🎯 Cliente quer ver o produto — agendar demo técnica
    * ⚡ Urgência identificada — priorizar follow-up em 24h
    * 💰 Budget mencionado: R$ 120.000,00 — lead de alto valor
──────────────────────────────────────────────────────────────
  ALERTAS DE CHURN / RISCO:
    Nenhum sinal de risco. Cliente aparenta estar satisfeito.
──────────────────────────────────────────────────────────────
  BARREIRAS DE ORCAMENTO:
    Sem objecoes financeiras registradas.
──────────────────────────────────────────────────────────────
  PERSONA IDENTIFICADA : CTO — Decisor técnico
  SENTIMENTO           : ✅ Positivo
  BUDGET IDENTIFICADO  : R$ 120.000,00
  TERMOMETRO DE FALA   : Vendedor 0% | Cliente 100%
  SCORE DE PRIORIDADE  : 10 / 10
══════════════════════════════════════════════════════════════
```

E o ranking consolidado com todos os leads:

```
══════════════════════════════════════════════════════════════
  KAIVI.AL — RANKING DE LEADS
══════════════════════════════════════════════════════════════
  ID       Vendedor           Cliente            Score
──────────────────────────────────────────────────────────────
  A1B2C3D4 Ana Lima           Carlos Mendez       10/10
  E5F6G7H8 Marcos Silva       Beatriz Costa        8/10
  I9J0K1L2 Julia Torres       James Wright         6/10
  M3N4O5P6 Pedro Rocha        Sofia Alves          2/10
══════════════════════════════════════════════════════════════
```

---

## 🧩 Conceitos de OOP Aplicados

| Conceito | Onde é usado |
|----------|-------------|
| **Herança** | `AnalisadorSentimento` e `AnalisadorOportunidades` herdam de `AnalisadorBase` |
| **Polimorfismo** | `ProcessadorKaivi` itera sobre `List<AnalisadorBase>` e chama `analisar()` em cada um |
| **Encapsulamento** | Todos os atributos são `private` com acesso via getters/setters |
| **Abstração** | `AnalisadorBase` define o contrato com método abstrato `analisar()` |
| **Enum** | `NivelSentimento` e `StatusTranscricao` garantem estados tipados e seguros |
| **Coleções** | Uso intensivo de `List`, `Map` (`LinkedHashMap`) para manter ordem de processamento |
| **Regex** | Extração de valores monetários com `Pattern` e `Matcher` da `java.util.regex` |

---

## 🛡️ Tecnologias

- **Java SE 17+** — sem dependências externas
- `java.util.regex` — extração de padrões monetários
- `java.time` — controle de timestamps das sessões
- `java.util.UUID` — IDs únicos para reuniões e transcrições
