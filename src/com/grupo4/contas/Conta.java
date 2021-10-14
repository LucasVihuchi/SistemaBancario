package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public abstract class Conta {
    protected String cpfTitular;
    protected double saldo;
    protected Agencia agencia;
    protected static TipoConta tipo;

    public Conta(String cpfTitularExt, Agencia idAgenciaExt) {
        this.cpfTitular = cpfTitularExt;
        this.agencia = idAgenciaExt;
    }

    // Construtor usado apenas para carregamento inicial do sistema
    public Conta(String cpfTitularExt, Agencia idAgenciaExt, double saldoExt) {
        this(cpfTitularExt, idAgenciaExt);
        this.saldo = saldoExt;
    }

    public void saque(double valor) throws ValorNegativoException, SaldoInsuficienteException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("Saque de valores negativos não é permitido");
        }
        if (this.saldo < (valor + TaxasConta.taxaSaque)) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= valor - TaxasConta.taxaSaque;
        registraTransacao(valor, "saque");
    }

    public void deposito(double valor) throws ValorNegativoException, IOException {
        if (valor <= TaxasConta.taxaDeposito) {
            throw new ValorNegativoException("Depósito de valores negativos não é permitido");
        }
        this.saldo += valor - TaxasConta.taxaDeposito;
        registraTransacao(valor, "deposito");
    }

    public void transferencia(double valor, String cpfDestinatario, TipoConta tipo) throws ValorNegativoException, SaldoInsuficienteException, CpfInexistenteException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("Transferência de valores negativos não é permitido");
        }
        if (this.saldo < (valor + TaxasConta.taxaTransferencia)) {
            throw new SaldoInsuficienteException();
        }

        this.saldo -= (valor + TaxasConta.taxaTransferencia);
        if (tipo == TipoConta.CORRENTE) {
            ContaCorrenteRepositorio.getContaCorrente(cpfDestinatario).saldo += valor;
        } else if (tipo == TipoConta.POUPANCA) {
            ContaPoupancaRepositorio.getContaPoupanca(cpfDestinatario).saldo += valor;
        }
        registraTransacao(valor, "transferencia", cpfDestinatario);
    }

    protected void registraTransacao(double valor, String tipoTransacao, String... cpfDestinatario) throws IOException {
        File pathHistoricoTransacoesDB = new File ("C:\\RepositorioBanco\\");
        File historicoTransacoesBD = new File (pathHistoricoTransacoesDB.getAbsolutePath() + "\\historicoTransacoesRepositorio.txt");

        if (!pathHistoricoTransacoesDB.exists()) {
            pathHistoricoTransacoesDB.mkdirs();
        }

        if(!historicoTransacoesBD.exists()) {
            historicoTransacoesBD.createNewFile();
        }

        try(FileWriter historicoTransacoesDBWriter = new FileWriter(historicoTransacoesBD, true);
            BufferedWriter historicoTransacoesDBWriterBuff = new BufferedWriter(historicoTransacoesDBWriter)) {

            historicoTransacoesDBWriterBuff.append(tipoTransacao + "¨¨" + this.cpfTitular + "¨¨" + valor + "¨¨");
            if (this.tipo.equals(TipoConta.CORRENTE)) {
                historicoTransacoesDBWriterBuff.append("c");
            } else if (this.tipo.equals(TipoConta.POUPANCA)) {
                historicoTransacoesDBWriterBuff.append("p");
            }
            if (!(cpfDestinatario.length == 0)) {
                historicoTransacoesDBWriterBuff.append("¨¨" + cpfDestinatario[0]);
            }
            historicoTransacoesDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    public void exibirSaldo() {
        System.out.println("Saldo atual na conta: R$ " + String.format("%.2f", this.saldo));
    }

    public String getCpfTitular() {
        return this.cpfTitular;
    }

    public double getSaldo() {
        return this.saldo;
    }

    public Agencia getAgencia() {
        return this.agencia;
    }

    public static TipoConta getTipo() {
        return Conta.tipo;
    }
}
