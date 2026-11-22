package herois;

import personagens.Personagem;
import rpg.Combatente;

public class Mago extends Personagem {
    private final int poderMagico;
    private final String elemento;
    private final int manaMaxima;
    private int mana;
    private boolean luaDeGeloUsada;

    public Mago(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica,
                int poderMagico, int manaMaxima, String elemento) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
        if (poderMagico < 0 || manaMaxima <= 0 || elemento == null || elemento.isBlank()) {
            throw new IllegalArgumentException("Poder magico, mana e elemento devem ser validos.");
        }
        this.poderMagico = poderMagico;
        this.manaMaxima = manaMaxima;
        this.mana = manaMaxima;
        this.elemento = elemento;
    }

    @Override
    public int atacar(Combatente alvo) {
        validarAlvo(alvo);
        mana -= 5;
        return causarDanoMagico(alvo, getAtaque() + poderMagico);
    }

    public int getMana() {
        return mana;
    }

    @Override
    public boolean podeAtacar() {
        return mana >= 5;
    }

    @Override
    public String getAcaoDeAtaque() {
        return " lanca magia contra ";
    }

    @Override
    public String getInformacaoAposAtaque() {
        return " (mana: " + mana + ")";
    }

    @Override
    public String usarHabilidadeDeEquipe(Combatente[] alvos) {
        if (luaDeGeloUsada || mana < 25) {
            return null;
        }

        mana -= 25;
        luaDeGeloUsada = true;
        int congelados = 0;
        for (Combatente alvo : alvos) {
            if (alvo != null && alvo.estaVivo()) {
                alvo.congelar();
                congelados++;
            }
        }
        return getNome() + " usa Lua de Gelo e congela " + congelados
                + " inimigo(s) por uma rodada. Mana restante: " + mana;
    }

    @Override
    public void exibirDados() {
        System.out.println("=== Maga ===\n" + dadosGerais()
                + "\nPoder magico: " + poderMagico
                + "\nElemento: " + elemento
                + "\nMana: " + mana + "/" + manaMaxima
                + "\nAtaque: magico (custa 5 de mana)"
                + "\nHabilidade: Lua de Gelo (custa 25 de mana)");
    }
}
