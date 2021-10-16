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

public class ContaCorrente extends Conta {
    static {
        ContaCorrente.tipo = TipoConta.CORRENTE;
    }

    public ContaCorrente(String cpfTitularExt, Agencia idAgenciaExt) {
        super(cpfTitularExt, idAgenciaExt);
    }

    // Construtor usado apenas para carregamento inicial do sistema
    public ContaCorrente(String cpfTitularExt, Agencia idAgenciaExt, double saldoExt) {
        super(cpfTitularExt, idAgenciaExt, saldoExt);
    }

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

    public void contrataSeguroVida(double valorSeguradoExt, int qtdMesesExt, List<String> segurados) throws SaldoInsuficienteException, ValorNegativoException, CpfInexistenteException, SeguroExistenteException, IOException {
        SeguroVida seguroVidaTemp = new SeguroVida(this.cpfTitular, valorSeguradoExt, qtdMesesExt, segurados);
        SeguroVidaRepositorio.adicionaSeguroVida(seguroVidaTemp);
        this.saldo -= (valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida);
        registraTransacao((valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida), "contratacaoSeguro");
        atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
    }

    public void pagaMensalidadeSeguroVida() throws CpfInexistenteException, SaldoInsuficienteException, IOException {
        if (this.saldo < SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade()) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade();
        registraTransacao((SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade()), "pagamentoSeguro");
        atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
    }
}
