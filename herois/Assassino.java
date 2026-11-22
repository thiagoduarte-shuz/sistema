package herois;

import personagens.Personagem;

import java.util.concurrent.ThreadLocalRandom;

public class Assassino extends Personagem {
    private final int agilidade;
    private final int chanceDeEsquiva;

    public Assassino(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica,
                     int agilidade) {
        super(nome, pontosDeVida, ataque, defesa, defesaMagica);
        if (agilidade <= 0) {
            throw new IllegalArgumentException("A agilidade deve ser positiva.");
        }
        this.agilidade = agilidade;
        this.chanceDeEsquiva = 20;
    }

    @Override
    public int getAgilidade() {
        return agilidade;
    }

    @Override
    protected int modificarDanoRecebido(int dano) {
        if (ThreadLocalRandom.current().nextInt(100) < chanceDeEsquiva) {
            int danoReduzido = (int) Math.ceil(dano / 2.0);
            System.out.println(getNome() + " esquiva parcialmente e reduz o dano pela metade!");
            return danoReduzido;
        }
        return dano;
    }

    @Override
    public void exibirDados() {
        System.out.println("=== Assassina ===\n" + dadosGerais()
                + "\nAgilidade: " + agilidade
                + "\nEsquiva passiva: 20% de chance de receber metade do dano");
    }
}
