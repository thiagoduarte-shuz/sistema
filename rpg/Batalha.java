package rpg;

import java.util.Arrays;
import java.util.Comparator;

public class Batalha {
    public void iniciar(Combatente personagem, Combatente inimigo) {
        iniciar(new Combatente[]{personagem}, new Combatente[]{inimigo});
    }

    public void iniciar(Combatente[] equipePersonagens, Combatente[] equipeInimigos) {
        iniciar(equipePersonagens, "Equipe dos personagens", equipeInimigos, "Equipe dos inimigos");
    }

    public void iniciar(Combatente[] equipePersonagens, String nomeEquipePersonagens,
                        Combatente[] equipeInimigos, String nomeEquipeInimigos) {
        validarEquipe(equipePersonagens, "personagens");
        validarEquipe(equipeInimigos, "inimigos");
        validarNomeEquipe(nomeEquipePersonagens);
        validarNomeEquipe(nomeEquipeInimigos);

        System.out.println("\n=== " + nomeEquipePersonagens + " vs. " + nomeEquipeInimigos + " ===");
        int rodada = 1;

        while (rodada <= 5 && temCombatenteVivo(equipePersonagens) && temCombatenteVivo(equipeInimigos)) {
            System.out.println("\n-- Rodada " + rodada + " --");
            Combatente[] vivosNoInicio = combatentesVivos(equipePersonagens, equipeInimigos);
            prepararRodada(equipePersonagens);
            prepararRodada(equipeInimigos);
            if (temCombatenteVivo(equipePersonagens) && temCombatenteVivo(equipeInimigos)) {
                executarTurnos(equipePersonagens, equipeInimigos);
                if (temCombatenteVivo(equipeInimigos)) {
                    executarTurnos(equipeInimigos, equipePersonagens);
                }
            }

            for (Combatente combatente : equipePersonagens) {
                combatente.fimDaRodada();
            }
            for (Combatente combatente : equipeInimigos) {
                combatente.fimDaRodada();
            }
            exibirDerrotasDaRodada(vivosNoInicio);

            rodada++;
        }

        boolean personagensVivos = temCombatenteVivo(equipePersonagens);
        boolean inimigosVivos = temCombatenteVivo(equipeInimigos);
        if (personagensVivos && inimigosVivos) {
            int hpPersonagens = somarPontosDeVida(equipePersonagens);
            int hpInimigos = somarPontosDeVida(equipeInimigos);
            if (hpPersonagens == hpInimigos) {
                System.out.println("\nEmpate apos 5 rodadas: as equipes terminaram com "
                        + hpPersonagens + " HP.");
                return;
            }
            boolean personagensVenceram = hpPersonagens > hpInimigos;
            Combatente[] vencedores = personagensVenceram ? equipePersonagens : equipeInimigos;
            String nomeVencedores = personagensVenceram ? nomeEquipePersonagens : nomeEquipeInimigos;
            System.out.println("\nFim das 5 rodadas. Vitoria por HP restante da "
                    + nomeVencedores + ": " + nomesDaEquipe(vencedores));
        } else {
            Combatente[] vencedores = personagensVivos ? equipePersonagens : equipeInimigos;
            System.out.println("\nVitoria da "
                    + (personagensVivos ? nomeEquipePersonagens : nomeEquipeInimigos)
                    + ": " + nomesDaEquipe(vencedores));
        }
    }

    private void prepararRodada(Combatente[] equipe) {
        for (Combatente combatente : equipe) {
            if (combatente.estaVivo()) {
                combatente.inicioDaRodada();
            }
        }
    }

    private Combatente[] combatentesVivos(Combatente[]... equipes) {
        return Arrays.stream(equipes)
                .flatMap(Arrays::stream)
                .filter(Combatente::estaVivo)
                .toArray(Combatente[]::new);
    }

    private void exibirDerrotasDaRodada(Combatente[] vivosNoInicio) {
        String derrotados = Arrays.stream(vivosNoInicio)
                .filter(combatente -> !combatente.estaVivo())
                .map(Combatente::getNome)
                .reduce((nome, proximo) -> nome + ", " + proximo)
                .orElse(null);

        if (derrotados == null) {
            System.out.println("Ninguem foi churrascado nessa rodada.");
        } else {
            System.out.println("Foi de F nessa rodada: " + derrotados + ".");
        }
    }

    private void executarTurnos(Combatente[] atacantes, Combatente[] defensores) {
        Combatente[] ordemDeTurno = Arrays.stream(atacantes)
                .sorted(Comparator.comparingInt(Combatente::getAgilidade).reversed())
                .toArray(Combatente[]::new);
        for (Combatente atacante : ordemDeTurno) {
            if (!atacante.estaVivo() || !temCombatenteVivo(defensores)) {
                continue;
            }

            if (atacante.consumirCongelamento()) {
                System.out.println(atacante.getNome() + " esta congelado e perde a vez.");
                continue;
            }
            if (atacante.devePerderTurno()) {
                System.out.println(atacante.getNome() + " falha na acao e perde a vez.");
                continue;
            }

            String habilidade = atacante.usarHabilidadeDeEquipe(defensores);
            if (habilidade != null) {
                System.out.println(habilidade);
                continue;
            }

            Combatente alvo = primeiroVivo(defensores);
            if (alvo == null) {
                continue;
            }
            if (!atacante.podeAtacar()) {
                System.out.println(atacante.getNome()
                        + " nao tem recursos para atacar e perde a vez.");
                continue;
            }
            int dano = atacante.atacar(alvo);
            System.out.println(atacante.getNome() + atacante.getAcaoDeAtaque() + alvo.getNome()
                    + ", causando " + dano + " de dano. HP restante: " + alvo.getPontosDeVida()
                    + atacante.getInformacaoAposAtaque());
        }
    }

    private Combatente primeiroVivo(Combatente[] combatentes) {
        for (Combatente combatente : combatentes) {
            if (combatente.estaVivo()) {
                return combatente;
            }
        }
        return null;
    }

    private boolean temCombatenteVivo(Combatente[] combatentes) {
        return primeiroVivo(combatentes) != null;
    }

    private int somarPontosDeVida(Combatente[] combatentes) {
        return Arrays.stream(combatentes).mapToInt(Combatente::getPontosDeVida).sum();
    }

    private String nomesDaEquipe(Combatente[] combatentes) {
        return Arrays.stream(combatentes)
                .map(Combatente::getNome)
                .reduce((nome, proximo) -> nome + ", " + proximo)
                .orElse("nenhum participante");
    }

    private void validarEquipe(Combatente[] equipe, String descricao) {
        if (equipe == null || equipe.length == 0) {
            throw new IllegalArgumentException("A equipe de " + descricao + " nao pode estar vazia.");
        }
        for (Combatente combatente : equipe) {
            if (combatente == null) {
                throw new IllegalArgumentException("As equipes nao podem conter participantes nulos.");
            }
        }
    }

    private void validarNomeEquipe(String nome) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome da equipe nao pode ser vazio.");
        }
    }
}
