package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.ValorInvalidoException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.UsuarioRepositorio;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Classe para objetos do tipo ContaPoupanca, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Conta como superclasse.
 */
public class ContaPoupanca extends Conta{
    private int aniversarioConta;
    private static final TipoConta tipo = TipoConta.POUPANCA;

    /** Construtor para instanciar nova ContaPoupanca durante o fluxo da aplicação.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     */
    public ContaPoupanca(String cpfTitularExt, Agencia agenciaExt) {
        super(cpfTitularExt, agenciaExt);
    }

    /** Construtor para instanciar nova ContaPoupanca apenas no carregamento da aplicação.
     * Note que esse construtor deve ser utilizado apenas pelos Loaders de arquivos.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     * @param saldoExt saldo da conta
     * @param aniversarioContaExt dia do mês de aniversário da conta
     */
    public ContaPoupanca(String cpfTitularExt, Agencia agenciaExt, double saldoExt, int aniversarioContaExt) {
        super(cpfTitularExt, agenciaExt, saldoExt);
        this.aniversarioConta = aniversarioContaExt;
    }

    /** Método para gerar relatório de simulação de rendimento e salvar o relatório em um arquivo de texto.
     *
     * @param valor valor a ser simulado
     * @param qtdDias quantidade em dias que o dinheiro ficará investido
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws ValorInvalidoException se quantidade de dias for menor que 30 dias
     * @throws IOException se ocorrer um erro de escrita no arquivo de relatório
     */
    public void geraSimulacaoRendimento(double valor, int qtdDias) throws ValorNegativoException, ValorInvalidoException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("Simulação de valores negativos não é permitido");
        }
        if (qtdDias < 30) {
            throw new ValorInvalidoException("Simulação para menos de 30 dias não é possível.");
        }
        int qtdMeses = qtdDias / 30;
        double valorFinal = valor * Math.pow((1 + TaxasConta.taxaJuros), qtdMeses);
        System.out.printf("O rendimento do depósito de R$ %.2f por %d dias(%d meses) foi R$ %.2f.\n", valor, qtdDias, qtdMeses, valorFinal);


        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoArquivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

        File pathRelatorioSimulacaoRendimento = new File ("C:\\RepositorioBanco\\Relatorios\\Simulacoes\\");
        File relatorioSimulacaoRendimento = new File(pathRelatorioSimulacaoRendimento.getAbsolutePath() + "\\" + this.cpfTitular + " " + formatoArquivo.format(momentoAtual) + ".txt");

        if (!pathRelatorioSimulacaoRendimento.exists()) {
            pathRelatorioSimulacaoRendimento.mkdirs();
        }

        if (!relatorioSimulacaoRendimento.exists()) {
            relatorioSimulacaoRendimento.createNewFile();
        }

        try (FileWriter relatorioSimulacaoRendimentoWriter = new FileWriter(relatorioSimulacaoRendimento);
             BufferedWriter relatorioSimulacaoRendimentoWriterBuff = new BufferedWriter(relatorioSimulacaoRendimentoWriter)) {

            relatorioSimulacaoRendimentoWriterBuff.append("Relatório de simulação de rendimento conta poupança - " + formatoBrasileiro.format(momentoAtual));
            relatorioSimulacaoRendimentoWriterBuff.newLine();
            relatorioSimulacaoRendimentoWriterBuff.append("Nome: " + UsuarioRepositorio.getUsuario(this.cpfTitular).getNome() + " / CPF: " + this.cpfTitular);
            relatorioSimulacaoRendimentoWriterBuff.newLine();
            relatorioSimulacaoRendimentoWriterBuff.newLine();
            relatorioSimulacaoRendimentoWriterBuff.append("O rendimento do depósito de R$ " + String.format("%.2f", valor) + " por " + qtdDias + " dias(" + qtdMeses + " meses) foi R$ " + String.format("%.2f", valorFinal) + ".\n");
        } catch (IOException | CpfInexistenteException e) {
            System.out.println("Erro de escrita de arquivos");
        }
    }

    /** Método para realizar o rendimento mensal da conta. Note que esse método deve ser chamado automaticamente pelo sistema uma vez por dia para cada conta.
     *
     * @throws IOException se ocorrer um erro de escrita no arquivo de contas-poupança
     */
    public void render() throws IOException {
        boolean isDataAniversario = this.aniversarioConta == LocalDate.now().getDayOfMonth();
        boolean isUltimoDiaMes = (LocalDate.now().getDayOfMonth() == LocalDate.now().lengthOfMonth());
        boolean isAniversarioMaiorQueDiasMes = (this.aniversarioConta > LocalDate.now().lengthOfMonth());

        if(isDataAniversario || (isUltimoDiaMes && isAniversarioMaiorQueDiasMes)) {
            this.saldo *= (1 + TaxasConta.taxaJuros);
        }
        atualizaSaldo(TipoConta.POUPANCA, this.cpfTitular);
    }

    /** Método para depositar saldo na conta e definir aniversário da conta caso necessário.
     *
     * @param valor valor a ser depositado
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
    @Override
    public void deposito(double valor) throws ValorNegativoException, IOException {
        super.deposito(valor);
        if(this.aniversarioConta == 0) {
            this.aniversarioConta = LocalDate.now().getDayOfMonth();
            this.atualizaAniversarioConta();
        }
    }

    /** Método para atualizar o aniversário da conta-poupança no arquivo de contas-poupança
     *
     * @throws IOException se ocorrer um erro de escrita no arquivo de contas-poupança
     */
    protected void atualizaAniversarioConta() throws IOException {
        File pathContaBD = new File("C:\\RepositorioBanco\\");
        File contaBD = new File(pathContaBD.getAbsolutePath() + "\\contaPoupancaRepositorio.txt");

        if (!pathContaBD.exists()) {
            pathContaBD.mkdirs();
        }

        if (!contaBD.exists()) {
            contaBD.createNewFile();
        }

        StringBuilder conteudoBD = new StringBuilder();

        try (FileReader contaBDReader = new FileReader(contaBD);
             BufferedReader contaBDReaderBuff = new BufferedReader(contaBDReader)) {
            String linha;
            while ((linha = contaBDReaderBuff.readLine()) != null) {
                String[] separada = linha.split("¨¨");
                if (separada[0].equals(this.cpfTitular)) {
                    linha = separada[0] + "¨¨" + separada[1] + "¨¨" + separada[2] + "¨¨" + this.aniversarioConta;
                }
                conteudoBD.append(linha + "\n");
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }

        try (FileWriter contaDBWriter = new FileWriter(contaBD);
             BufferedWriter contaDBWriterBuff = new BufferedWriter(contaDBWriter)) {
            contaDBWriterBuff.append(conteudoBD);
        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    public void setAniversarioConta(int aniversarioConta) {
        this.aniversarioConta = aniversarioConta;
    }

    public int getAniversarioConta() {
        return this.aniversarioConta;
    }

    public static TipoConta getTipo() {
        return tipo;
    }
}
