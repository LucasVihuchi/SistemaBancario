package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.ValorInvalidoException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;

import java.time.LocalDate;

public class ContaPoupanca extends Conta{
    private int aniversarioConta;

    static {
        ContaPoupanca.tipo = TipoConta.POUPANCA;
    }

    public ContaPoupanca(String cpfTitularExt, Agencia idAgenciaExt) {
        super(cpfTitularExt, idAgenciaExt);
    }

    public void simularRendimento(double valor, int qtdDias) throws ValorNegativoException, ValorInvalidoException {
        if (valor <= 0) {
            throw new ValorNegativoException("Simulação de valores negativos não é permitido");
        }
        if (qtdDias <= 30) {
            throw new ValorInvalidoException("Simulação para menos de 30 dias não é possível.");
        }
        int qtdMeses = qtdDias / 30;
        double valorFinal = valor * Math.pow((1 + TaxasConta.taxaJuros), qtdMeses);
        System.out.printf("O rendimento do depósito de R$ %.2f por %d dias(%d meses) foi R$ %.2f.\n", valor, qtdDias, qtdMeses, valorFinal);
    }

    public void render() {
        boolean isDataAniversario = this.aniversarioConta == LocalDate.now().getDayOfMonth();
        boolean isUltimoDiaMes = (LocalDate.now().getDayOfMonth() == LocalDate.now().lengthOfMonth());
        boolean isAniversarioMaiorQueDiasMes = (this.aniversarioConta > LocalDate.now().lengthOfMonth());

        if(isDataAniversario || (isUltimoDiaMes && isAniversarioMaiorQueDiasMes)) {
            this.saldo *= (1 + TaxasConta.taxaJuros);
        }
    }

    @Override
    public void deposito(double valor) throws ValorNegativoException {
        super.deposito(valor);
        if(this.aniversarioConta == 0) {
            this.aniversarioConta = LocalDate.now().getDayOfMonth();
        }
    }

    public int getAniversarioConta() {
        return this.aniversarioConta;
    }
}
