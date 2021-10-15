package com.grupo4.segurovida;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.ContaCorrenteRepositorio;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class SeguroVida {
    private String cpfSegurado;
    private double valorSegurado;
    private int qtdMeses;
    private double valorPago;
    private List<String> segurados;
    private LocalDate dataContratacao;

    public SeguroVida(String cpfSeguradoExt, double valorSeguradoExt, int qtdMesesExt, List<String> segurados) throws CpfInexistenteException, SaldoInsuficienteException, ValorNegativoException {
        if (qtdMesesExt <= 0 || valorSeguradoExt <= 0) {
            throw new ValorNegativoException();
        }
        if((valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida) > ContaCorrenteRepositorio.getContaCorrente(this.cpfSegurado).getSaldo()) {
            throw new SaldoInsuficienteException();
        }
        this.cpfSegurado = cpfSeguradoExt;
        this.valorSegurado = valorSeguradoExt;
        this.qtdMeses = qtdMesesExt;
        this.dataContratacao = LocalDate.now();
        this.segurados = segurados;
        this.valorPago = valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida;
    }

    public SeguroVida(String cpfSegurado, double valorSegurado, int qtdMeses, double valorPago, List<String> segurados, LocalDate dataContratacao) {
        this.cpfSegurado = cpfSegurado;
        this.valorSegurado = valorSegurado;
        this.qtdMeses = qtdMeses;
        this.valorPago = valorPago;
        this.segurados = segurados;
        this.dataContratacao = dataContratacao;
    }

    public double calculaMensalidade() {
        return (this.valorSegurado * (1 - TaxasConta.taxaContratacaoSeguroDeVida)) / qtdMeses;
    }

    public String getCpfSegurado() {
        return this.cpfSegurado;
    }

    public double getValorSegurado() {
        return this.valorSegurado;
    }

    public int getQtdMeses() {
        return this.qtdMeses;
    }

    public double getValorPago() {
        return this.valorPago;
    }

    public List<String> getSegurados() {
        return this.segurados;
    }

    public LocalDate getDataContratacao() {
        return this.dataContratacao;
    }
}
