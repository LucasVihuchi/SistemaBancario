package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.SeguroVidaRepositorio;
import com.grupo4.repositorios.UsuarioRepositorio;
import com.grupo4.segurovida.SeguroVida;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Classe para objetos do tipo ContaCorrente, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Conta como superclasse.
 */
public class ContaCorrente extends Conta {
    private static final TipoConta tipo = TipoConta.CORRENTE;

    /** Construtor para instanciar nova ContaCorrente durante o fluxo da aplicação.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     */
    public ContaCorrente(String cpfTitularExt, Agencia agenciaExt) {
        super(cpfTitularExt, agenciaExt);
    }

    /** Construtor para instanciar nova ContaCorrente apenas no carregamento da aplicação.
     * Note que esse construtor deve ser utilizado apenas pelos Loaders de arquivos.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     * @param saldoExt saldo da conta
     */
    public ContaCorrente(String cpfTitularExt, Agencia agenciaExt, double saldoExt) {
        super(cpfTitularExt, agenciaExt, saldoExt);
    }

    public static TipoConta getTipo() {
        return tipo;
    }

    /** Método para gerar relatório de tributação da conta e salvar o relatório em um arquivo de texto.
     *
     * @throws IOException se ocorrer um erro de escrita no arquivo de relatório
     */
    public void geraRelatorioTributacao() throws IOException {
        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoArquivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

        File pathHistoricoTransacoesDB = new File("C:\\RepositorioBanco\\");
        File historicoTransacoesBD = new File(pathHistoricoTransacoesDB.getAbsolutePath() + "\\historicoTransacoesRepositorio.txt");

        if (!pathHistoricoTransacoesDB.exists()) {
            pathHistoricoTransacoesDB.mkdirs();
        }

        if (!historicoTransacoesBD.exists()) {
            historicoTransacoesBD.createNewFile();
        }

        double totalTaxasSaque = 0;
        double totalTaxasDeposito = 0;
        double totalTaxasTransferencia = 0;

        try (FileReader historicoTransacoesDBReader = new FileReader(historicoTransacoesBD);
             BufferedReader historicoTransacoesDBReaderBuff = new BufferedReader(historicoTransacoesDBReader)) {

            String linhaLida;
            while (((linhaLida = historicoTransacoesDBReaderBuff.readLine()) != null)) {
                String[] itensTemp = linhaLida.split("¨¨");
                if (itensTemp[1].equals(this.cpfTitular) && itensTemp[3].equals("c")) {
                    switch (itensTemp[0]) {
                        case "saque":
                            totalTaxasSaque += TaxasConta.taxaSaque;
                            break;
                        case "deposito":
                            totalTaxasDeposito += TaxasConta.taxaDeposito;
                            break;
                        case "transferencia":
                            totalTaxasTransferencia += TaxasConta.taxaTransferencia;
                            break;
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos");
        }

        File pathRelatorioTributacaoContaCorrente = new File("C:\\RepositorioBanco\\Relatorios\\Clientes\\");
        File relatorioTributacaoContaCorrente = new File(pathRelatorioTributacaoContaCorrente.getAbsolutePath() + "\\" + this.cpfTitular + " " + formatoArquivo.format(momentoAtual) + ".txt");

        if (!pathRelatorioTributacaoContaCorrente.exists()) {
            pathRelatorioTributacaoContaCorrente.mkdirs();
        }

        if (!relatorioTributacaoContaCorrente.exists()) {
            relatorioTributacaoContaCorrente.createNewFile();
        }

        try (FileWriter relatorioTributacaoContaCorrenteWriter = new FileWriter(relatorioTributacaoContaCorrente);
             BufferedWriter relatorioTributacaoContaCorrenteWriterBuff = new BufferedWriter(relatorioTributacaoContaCorrenteWriter)) {

            relatorioTributacaoContaCorrenteWriterBuff.append("Relatório de tributação conta corrente - " + formatoBrasileiro.format(momentoAtual));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Nome: " + UsuarioRepositorio.getUsuario(this.cpfTitular).getNome() + " / CPF: " + this.cpfTitular);
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Total gasto com taxas de saque: R$ " + String.format("%.2f", totalTaxasSaque));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Total gasto com taxas de depósito: R$ " + String.format("%.2f", totalTaxasDeposito));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Total gasto com taxas de transferência: R$ " + String.format("%.2f", totalTaxasTransferencia));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("--------//--------");
            relatorioTributacaoContaCorrenteWriterBuff.newLine();

            try {
                SeguroVida seguroVidaAtual = SeguroVidaRepositorio.getSeguroVida(this.cpfTitular);
                relatorioTributacaoContaCorrenteWriterBuff.append("Valor pago até o momento do seguro de vida: R$ " + String.format("%.2f", seguroVidaAtual.getValorPago()));
                relatorioTributacaoContaCorrenteWriterBuff.newLine();
                relatorioTributacaoContaCorrenteWriterBuff.append("Mensalidade do seguro de vida: R$ " + String.format("%.2f", seguroVidaAtual.calculaMensalidade()));
                relatorioTributacaoContaCorrenteWriterBuff.newLine();
                relatorioTributacaoContaCorrenteWriterBuff.append("Valor contratado do seguro de vida: R$ " + String.format("%.2f", seguroVidaAtual.getValorSegurado()));
                relatorioTributacaoContaCorrenteWriterBuff.newLine();
                relatorioTributacaoContaCorrenteWriterBuff.append("--------//--------");
                relatorioTributacaoContaCorrenteWriterBuff.newLine();
            } catch (CpfInexistenteException e) {

            }

            relatorioTributacaoContaCorrenteWriterBuff.append("Taxas:");
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Taxa para saque: R$ " + String.format("%.2f", TaxasConta.taxaSaque));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Taxa para depósito: R$ " + String.format("%.2f", TaxasConta.taxaDeposito));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();
            relatorioTributacaoContaCorrenteWriterBuff.append("Taxa para transferência: R$ " + String.format("%.2f", TaxasConta.taxaTransferencia));
            relatorioTributacaoContaCorrenteWriterBuff.newLine();

        } catch (IOException | CpfInexistenteException e) {
            System.out.println("Erro de escrita de arquivos");
        }

    }

    /** Método para contratar seguro de vida.
     *
     * @param valorSeguradoExt valor a ser segurado
     * @param qtdMesesExt quantidade de meses em que o seguro será pago
     * @param segurados lista de CPFs dos segurados
     * @throws SaldoInsuficienteException se não houver saldo suficiente na conta para pagar a taxa inicial de contratação
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws CpfInexistenteException se a conta-corrente associada ao CPF do destinatário não for encontrada no sistema
     * @throws SeguroExistenteException se o usuário já possui um seguro de vida contratado
     * @throws IOException se ocorrer um erro de escrita no arquivo de seguros de vida
     */
    public void contrataSeguroVida(double valorSeguradoExt, int qtdMesesExt, List<String> segurados) throws SaldoInsuficienteException, ValorNegativoException, CpfInexistenteException, SeguroExistenteException, IOException {
        SeguroVida seguroVidaTemp = new SeguroVida(this.cpfTitular, valorSeguradoExt, qtdMesesExt, segurados);
        SeguroVidaRepositorio.adicionaSeguroVida(seguroVidaTemp);
        this.saldo -= (valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida);
        registraTransacao((valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida), "contratacaoSeguro");
        atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
    }

    /** Método para pagar a mensalidade do seguro de vida.
     *
     * @throws CpfInexistenteException se a conta-corrente associada ao CPF do destinatário não for encontrada no sistema
     * @throws SaldoInsuficienteException se não houver saldo suficiente na conta para pagar a mensalidade
     * @throws IOException se ocorrer um erro de escrita no arquivo de seguros de vida
     */
    public void pagaMensalidadeSeguroVida() throws CpfInexistenteException, SaldoInsuficienteException, IOException {
        if (this.saldo < SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade()) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade();
        registraTransacao((SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade()), "pagamentoSeguro");
        atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
    }
}
