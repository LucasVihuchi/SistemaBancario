package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.SeguroVidaRepositorio;
import com.grupo4.segurovida.SeguroVida;

public class ContaCorrente extends Conta{
    static {
        ContaCorrente.tipo = TipoConta.CORRENTE;
    }

    public ContaCorrente(String cpfTitularExt, Agencia idAgenciaExt) {
        super(cpfTitularExt, idAgenciaExt);
    }

    public void geraRelatorioTributacao() {
        // TODO Implementar rotina de geração de relatório de tributação
    }

    public void contrataSeguroVida(double valorSeguradoExt, int qtdMesesExt, String... segurados) throws SaldoInsuficienteException, ValorNegativoException, CpfInexistenteException, SeguroExistenteException {
        SeguroVida seguroVidaTemp = new SeguroVida(this.cpfTitular, valorSeguradoExt, qtdMesesExt, segurados);
        SeguroVidaRepositorio.adicionaSeguroVida(seguroVidaTemp);
        this.saldo -= (valorSeguradoExt * TaxasConta.taxaContratacaoSeguroDeVida);
    }

    public void pagaMensalidadeSeguroVida() throws CpfInexistenteException, SaldoInsuficienteException {
        if (this.saldo < SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade()) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= SeguroVidaRepositorio.getSeguroVida(this.cpfTitular).calculaMensalidade();
    }
}
