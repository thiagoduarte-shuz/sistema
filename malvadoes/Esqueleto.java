package malvadoes;

import personagens.Inimigo;
import rpg.Combatente;

import java.util.concurrent.ThreadLocalRandom;

public class Esqueleto extends Inimigo {
    private final int alcance;
    private boolean gritoDaMorteUsado;
    private boolean aguardandoProximaRodada;
    private boolean ataqueCriticoPreparado;

    public Esqueleto(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica,
                     int alcance) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
        if (alcance <= 0) {
            throw new IllegalArgumentException("O alcance deve ser positivo.");
        }
        this.alcance = alcance;
    }

    @Override
    public int getAgilidade() {
        return 8;
    }

    @Override
    public void inicioDaRodada() {
        super.inicioDaRodada();
        if (aguardandoProximaRodada) {
            ataqueCriticoPreparado = true;
            aguardandoProximaRodada = false;
        }
    }

    @Override
    public int atacar(Combatente alvo) {
        validarAlvo(alvo);
        boolean critico = ataqueCriticoPreparado
                && ThreadLocalRandom.current().nextInt(100) < 50;
        ataqueCriticoPreparado = false;
        int danoBase = critico ? getAtaque() * 2 : getAtaque();
        int dano = causarDanoFisico(alvo, danoBase);
        if (critico) {
            System.out.println(getNome() + " acertou um ataque critico!");
        }
        return dano;
    }

    @Override
    public String usarHabilidadeDeEquipe(Combatente[] alvos) {
        if (gritoDaMorteUsado) {
            return null;
        }
        gritoDaMorteUsado = true;
        aguardandoProximaRodada = true;
        return getNome() + " usa Grito da Morte. Seu proximo ataque, na proxima rodada, tem 50% de chance de ser critico.";
    }

    @Override
    public String getAcaoDeAtaque() {
        return " dispara de longe contra ";
    }

    @Override
    public void exibirDados() {
        System.out.println("=== Esqueleto Buxa ===\n" + dadosGerais()
                + "\nTipo de ataque: a distancia"
                + "\nAlcance: " + alcance
                + "\nHabilidade: Grito da Morte (50% de chance de ataque critico na proxima rodada)");  
    }
}
