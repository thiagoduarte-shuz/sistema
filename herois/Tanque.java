package herois;

import personagens.Personagem;
import rpg.Combatente;

import java.util.concurrent.ThreadLocalRandom;

public class Tanque extends Personagem {
    private final int agilidade;
    private final int chanceDeFalha;
    private boolean gritoDeGuerraUsado;
    private boolean peleDeFerroUsada;

    public Tanque(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica,
                  int agilidade) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
        if (agilidade < 0) {
            throw new IllegalArgumentException("A agilidade nao pode ser negativa.");
        }
        this.agilidade = agilidade;
        this.chanceDeFalha = 20;
    }

    @Override
    public int getAgilidade() {
        return agilidade;
    }

    @Override
    public boolean devePerderTurno() {
        return ThreadLocalRandom.current().nextInt(100) < chanceDeFalha;
    }

    @Override
    public String usarHabilidadeDeEquipe(Combatente[] alvos) {
        if (!gritoDeGuerraUsado) {
            int afetados = 0;
            for (Combatente alvo : alvos) {
                if (alvo != null && alvo.estaVivo()) {
                    alvo.aplicarVulnerabilidade(2, 1.5);
                    afetados++;
                }
            }
            gritoDeGuerraUsado = true;
            return getNome() + " usa Grito de Guerra: " + afetados
                    + " inimigo(s) recebem 50% mais dano por 2 rodadas.";
        }

        if (!peleDeFerroUsada) {
            ativarDefesaTotal();
            peleDeFerroUsada = true;
            return getNome() + " usa Pele de Ferro e fica imune a todo dano ate o fim desta rodada.";
        }
        return null;
    }

    @Override
    public void exibirDados() {
        System.out.println("=== Tanque ===\n" + dadosGerais()
                + "\nAgilidade: " + agilidade
                + "\nFalha passiva: 20% de chance de perder a rodada"
                + "\nHabilidades: Grito de Guerra e Pele de Ferro");
    }
}
