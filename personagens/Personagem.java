package personagens;

import rpg.Combatente;

public abstract class Personagem extends Combatente {
    protected Personagem(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
    }
}
