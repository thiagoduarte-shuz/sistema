package rpg;

import herois.Assassino;
import herois.Mago;
import herois.Tanque;
import malvadoes.Esqueleto;
import malvadoes.TubaraoForaDaAgua;
import personagens.Inimigo;

public class Main {
    public static void main(String[] args) {
        Combatente ranni = new Mago("Ranni", 95, 14, 5, 10, 32, 120, "gelo");
        Combatente eileen = new Assassino("Eileen", 115, 24, 9, 8, 30);
        Combatente havel = new Tanque("Havel", 210, 30, 25, 20, 3);
        Inimigo esqueleto = new Esqueleto("Esqueleto Buxa", 243, 24, 10, 8, 15);
        Inimigo tubarao = new TubaraoForaDaAgua("Tubarao fora da agua", 263, 37, 13, 11, 19);

        Combatente[] combatentes = {ranni, eileen, havel, esqueleto, tubarao};
        for (Combatente combatente : combatentes) {
            combatente.exibirDados();
            System.out.println();
        }

        new Batalha().iniciar(
                new Combatente[]{ranni, havel, eileen},
                "Equipe dos gente fina",
                new Combatente[]{esqueleto, tubarao},
                "Equipe dos gente paia");
    }
}
