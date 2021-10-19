package com.grupo4.segurovida;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.ContaCorrenteRepositorio;

import java.time.LocalDate;
import java.util.List;

/** Classe para objetos do tipo SeguroVida, onde serão contidos, atributos e métodos para o mesmo.
 */
public class SeguroVida {
    private String cpfSegurado;
    private double valorSegurado;
    private int qtdMeses;
    private double valorPago;
    private List<String> segurados;
    private LocalDate dataContratacao;

    /** Construtor para instanciar novo SeguroVida durante o fluxo da aplicação.
     *
     * @param cpfSeguradoExt CPF do segurado
     * @param valorSeguradoExt valor a ser segurado
     * @param qtdMesesExt quantidade em meses em que será pago o seguro
     * @param segurados lista de CPFs dos segurados
     * @throws CpfInexistenteException se a conta-corrente associada ao CPF do destinatário não for encontrada no sistema
     * @throws SaldoInsuficienteException se não houver saldo suficiente na conta para pagar a taxa inicial de contratação
     * @throws ValorNegativoException se um valor negativo for fornecido
     */
    public SeguroVida(String cpfSeguradoExt, double valorSeguradoExt, int qtdMesesExt, List<String> segurados) throws CpfInexistenteException, SaldoInsuficienteException, ValorNegativoException {
        if (qtdMesesExt <= 0 || valorSeguradoExt <= 0) {
            throw new ValorNegativoException();
        }
        if((valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida) > ContaCorrenteRepositorio.getContaCorrente(cpfSeguradoExt).getSaldo()) {
            throw new SaldoInsuficienteException();
        }
        this.cpfSegurado = cpfSeguradoExt;
        this.valorSegurado = valorSeguradoExt;
        this.qtdMeses = qtdMesesExt;
        this.dataContratacao = LocalDate.now();
        this.segurados = segurados;
        this.valorPago = valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida;
    }

    /**  Construtor para instanciar novo SeguroVida apenas no carregamento da aplicação.
     * Note que esse construtor deve ser utilizado apenas pelos Loaders de arquivos.
     *
     * @param cpfSegurado CPF do segurado
     * @param valorSegurado valor a ser segurado
     * @param qtdMeses quantidade em meses em que será pago o seguro
     * @param valorPago valor já pago do seguro
     * @param segurados lista de CPFs dos segurados
     * @param dataContratacao data de contratação do seguro
     */
    public SeguroVida(String cpfSegurado, double valorSegurado, int qtdMeses, double valorPago, List<String> segurados, LocalDate dataContratacao) {
        this.cpfSegurado = cpfSegurado;
        this.valorSegurado = valorSegurado;
        this.qtdMeses = qtdMeses;
        this.valorPago = valorPago;
        this.segurados = segurados;
        this.dataContratacao = dataContratacao;
    }

    /** Método para calcular mensalidade do seguro de vida
     *
     * @return A mensalidade do seguro de vida em reais
     */
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
