package malvadoes;

import personagens.Inimigo;
import rpg.Combatente;

public class TubaraoForaDaAgua extends Inimigo {
    private final int ferocidade;
    private boolean sangramentoUsado;

    public TubaraoForaDaAgua(String nome, int pontosDeVida, int ataque, int defesa,
                             int defesaMagica, int ferocidade) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
        if (ferocidade < 0) {
            throw new IllegalArgumentException("A ferocidade nao pode ser negativa.");
        }
        this.ferocidade = ferocidade;
    }

    @Override
    public int getAgilidade() {
        return 12;
    }

    @Override
    public void inicioDaRodada() {
        super.inicioDaRodada();
        if (estaVivo()) {
            receberDanoDireto(1);
            System.out.println("Tubarao fora da agua perdeu 1 de hp por estar fora da agua");
        }
    }

    @Override
    protected int aplicarHabilidade(Combatente alvo) {
        int dano = causarDanoFisico(alvo, getAtaque() + ferocidade);
        alvo.aplicarVulnerabilidade(2, 2.0);
        return dano;
    }

    @Override
    protected String getNomeHabilidade() {
        return "Sangramento";
    }

    @Override
    public String usarHabilidadeDeEquipe(Combatente[] alvos) {
        if (sangramentoUsado) {
            return null;
        }
        for (Combatente alvo : alvos) {
            if (alvo != null && alvo.estaVivo()) {
                sangramentoUsado = true;
                int dano = usarHabilidade(alvo);
                return getNome() + " usa Sangramento contra " + alvo.getNome()
                        + ", causando " + dano + " de dano. O alvo recebe o dobro de dano por 1 rodada."
                        + " HP restante: " + alvo.getPontosDeVida();
            }
        }
        return null;
    }

    @Override
    public void exibirDados() {
        System.out.println("=== Tubarao fora da agua ===\n" + dadosGerais()
                + "\nFerocidade: " + ferocidade
                + "\nDebuff: perde 1 HP no inicio de cada rodada fora da agua"
                + "\nHabilidade: Sangramento (dobra o dano recebido por 1 rodada)");
    }
}
