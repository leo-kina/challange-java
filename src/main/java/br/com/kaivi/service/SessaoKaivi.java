package br.com.kaivi.service;

import br.com.kaivi.chat.ChatMemoria;
import br.com.kaivi.model.CartaoInteligencia;
import br.com.kaivi.model.Reuniao;
import br.com.kaivi.model.Transcricao;


public class SessaoKaivi {

    private final Reuniao reuniao;
    private final Transcricao transcricao;
    private final CartaoInteligencia cartao;
    private final ChatMemoria chat;

    public SessaoKaivi(Reuniao reuniao, Transcricao transcricao,
                       CartaoInteligencia cartao, ChatMemoria chat) {
        this.reuniao    = reuniao;
        this.transcricao = transcricao;
        this.cartao     = cartao;
        this.chat       = chat;
    }

    public Reuniao           getReuniao()     { return reuniao; }
    public Transcricao       getTranscricao() { return transcricao; }
    public CartaoInteligencia getCartao()     { return cartao; }
    public ChatMemoria       getChat()        { return chat; }

    @Override
    public String toString() {
        return String.format("SessaoKaivi{reuniao=%s, score=%d, sentimento=%s}",
                reuniao.getId(), cartao.getScorePrioridade(),
                cartao.getSentimento().getRotulo());
    }
}
