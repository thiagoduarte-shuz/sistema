package personagens;

import rpg.Combatente;

public abstract class Inimigo extends Combatente {
    protected Inimigo(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
    }
}
