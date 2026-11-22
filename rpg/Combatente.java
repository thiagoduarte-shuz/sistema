package rpg;

public abstract class Combatente {
    private final String nome;
    private int pontosDeVida;
    private final int ataque;
    private final int defesa;
    private final int defesaMagica;
    private boolean habilidadeUsada;
    private boolean congelado;
    private boolean defesaTotal;
    private int rodadasVulneravel;
    private double multiplicadorDanoRecebido = 1.5;

    protected Combatente(String nome, int pontosDeVida, int ataque, int defesa, int defesaMagica) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome nao pode ser vazio.");
        }
        if (pontosDeVida <= 0 || ataque < 0 || defesa < 0 || defesaMagica < 0) {
            throw new IllegalArgumentException("Os atributos de combate devem ser validos.");
        }

        this.nome = nome;
        this.pontosDeVida = pontosDeVida;
        this.ataque = ataque;
        this.defesa = defesa;
        this.defesaMagica = defesaMagica;
    }

    public final String getNome() {
        return nome;
    }

    public final int getPontosDeVida() {
        return pontosDeVida;
    }

    public final int getAtaque() {
        return ataque;
    }

    public final int getDefesa() {
        return defesa;
    }

    public final int getDefesaMagica() {
        return defesaMagica;
    }

    public int getAgilidade() {
        return 0;
    }

    public final boolean estaVivo() {
        return pontosDeVida > 0;
    }

    public final boolean habilidadeDisponivel() {
        return !habilidadeUsada;
    }

    public boolean devePerderTurno() {
        return false;
    }

    public void inicioDaRodada() {
    }

    public boolean podeAtacar() {
        return true;
    }

    public String getAcaoDeAtaque() {
        return " ataca ";
    }

    public String getInformacaoAposAtaque() {
        return "";
    }

    public int atacar(Combatente alvo) {
        validarAlvo(alvo);
        return alvo.receberDanoFisico(ataque);
    }

    public final int usarHabilidade(Combatente alvo) {
        validarAlvo(alvo);
        if (!habilidadeDisponivel()) {
            throw new IllegalStateException("A habilidade especial ja foi utilizada.");
        }

        habilidadeUsada = true;
        return aplicarHabilidade(alvo);
    }

    public String usarHabilidadeDeEquipe(Combatente[] alvos) {
        return null;
    }

    protected int aplicarHabilidade(Combatente alvo) {
        return atacar(alvo);
    }

    protected String getNomeHabilidade() {
        return "Ataque especial";
    }

    protected final String usarHabilidadeNoPrimeiroAlvo(Combatente[] alvos) {
        if (!habilidadeDisponivel()) {
            return null;
        }

        for (Combatente alvo : alvos) {
            if (alvo != null && alvo.estaVivo()) {
                int dano = usarHabilidade(alvo);
                return getNome() + " usa " + getNomeHabilidade() + " contra " + alvo.getNome()
                        + ", causando " + dano + " de dano. HP restante: " + alvo.getPontosDeVida();
            }
        }
        return null;
    }

    protected final int causarDanoFisico(Combatente alvo, int danoBruto) {
        return alvo.receberDanoFisico(danoBruto);
    }

    protected final int causarDanoMagico(Combatente alvo, int danoBruto) {
        return alvo.receberDanoMagico(danoBruto);
    }

    public final void congelar() {
        congelado = true;
    }

    public final boolean consumirCongelamento() {
        if (!congelado) {
            return false;
        }
        congelado = false;
        return true;
    }

    public final void aplicarVulnerabilidade(int rodadas) {
        aplicarVulnerabilidade(rodadas, 1.5);
    }

    public final void aplicarVulnerabilidade(int rodadas, double multiplicador) {
        if (rodadas <= 0) {
            throw new IllegalArgumentException("A duracao da vulnerabilidade deve ser positiva.");
        }
        if (multiplicador < 1.0) {
            throw new IllegalArgumentException("O multiplicador de dano deve ser pelo menos 1.");
        }
        if (rodadas >= rodadasVulneravel) {
            rodadasVulneravel = rodadas;
            multiplicadorDanoRecebido = multiplicador;
        }
    }

    public final void ativarDefesaTotal() {
        defesaTotal = true;
    }

    public void fimDaRodada() {
        if (rodadasVulneravel > 0) {
            rodadasVulneravel--;
        }
        defesaTotal = false;
    }

    protected final int receberDanoFisico(int danoBruto) {
        return receberDano(Math.max(0, danoBruto - defesa));
    }

    protected final int receberDanoMagico(int danoBruto) {
        return receberDano(Math.max(0, danoBruto - defesaMagica));
    }

    protected int modificarDanoRecebido(int dano) {
        return dano;
    }

    private int receberDano(int dano) {
        if (defesaTotal || dano == 0) {
            return 0;
        }
        if (rodadasVulneravel > 0) {
            dano = (int) Math.ceil(dano * multiplicadorDanoRecebido);
        }

        int danoRecebido = Math.min(Math.max(0, modificarDanoRecebido(dano)), pontosDeVida);
        pontosDeVida -= danoRecebido;
        return danoRecebido;
    }

    protected final void receberDanoDireto(int dano) {
        int danoRecebido = Math.min(Math.max(0, dano), pontosDeVida);
        pontosDeVida -= danoRecebido;
    }

    protected final void validarAlvo(Combatente alvo) {
        if (alvo == null || alvo == this) {
            throw new IllegalArgumentException("O alvo da acao deve ser outro combatente.");
        }
        if (!estaVivo()) {
            throw new IllegalStateException("Um combatente derrotado nao pode agir.");
        }
        if (!alvo.estaVivo()) {
            throw new IllegalStateException("Nao e possivel atacar um combatente derrotado.");
        }
    }

    protected final String dadosGerais() {
        return "Nome: " + nome
                + "\nHP: " + pontosDeVida
                + "\nAtaque: " + ataque
                + "\nDefesa: " + defesa
                + "\nDefesa magica: " + defesaMagica;
    }

    public abstract void exibirDados();
}
