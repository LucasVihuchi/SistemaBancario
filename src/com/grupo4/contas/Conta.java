package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;

public abstract class Conta {
    protected String cpfTitular;
    protected double saldo;
    protected Agencia idAgencia;
    protected static TipoConta tipo;

    public Conta(String cpfTitularExt, Agencia idAgenciaExt) {
        this.cpfTitular = cpfTitularExt;
        this.idAgencia = idAgenciaExt;
    }

    public void saque(double valor) throws ValorNegativoException, SaldoInsuficienteException {
        if (valor <= 0) {
            throw new ValorNegativoException("Saque de valores negativos não é permitido");
        }
        if (this.saldo < (valor + TaxasConta.taxaSaque)) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= valor - TaxasConta.taxaSaque;
    }

    public void deposito(double valor) throws ValorNegativoException {
        if (valor <= TaxasConta.taxaDeposito) {
            throw new ValorNegativoException("Depósito de valores negativos não é permitido");
        }
        this.saldo += valor - TaxasConta.taxaDeposito;
    }

    public void transferencia(double valor, String cpfTitular, TipoConta tipo) throws ValorNegativoException, SaldoInsuficienteException {
        if (valor <= 0) {
            throw new ValorNegativoException("Transferência de valores negativos não é permitido");
        }
        if (this.saldo < (valor + TaxasConta.taxaTransferencia)) {
            throw new SaldoInsuficienteException();
        }
        // TODO Terminar o método depois de fazer os repositorios
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

    public Agencia getIdAgencia() {
        return this.idAgencia;
    }

    public static TipoConta getTipo() {
        return Conta.tipo;
    }
}
