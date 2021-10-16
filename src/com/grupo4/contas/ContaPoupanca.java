package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.ValorInvalidoException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.UsuarioRepositorio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ContaPoupanca extends Conta{
    private int aniversarioConta;

    static {
        ContaPoupanca.tipo = TipoConta.POUPANCA;
    }

    public ContaPoupanca(String cpfTitularExt, Agencia idAgenciaExt) {
        super(cpfTitularExt, idAgenciaExt);
    }

    public ContaPoupanca(String cpfTitularExt, Agencia idAgenciaExt, double saldoExt, int aniversarioContaExt) {
        super(cpfTitularExt, idAgenciaExt, saldoExt);
        this.aniversarioConta = aniversarioContaExt;
    }

    public void geraSimulacaoRendimento(double valor, int qtdDias) throws ValorNegativoException, ValorInvalidoException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("Simulação de valores negativos não é permitido");
        }
        if (qtdDias <= 30) {
            throw new ValorInvalidoException("Simulação para menos de 30 dias não é possível.");
        }
        int qtdMeses = qtdDias / 30;
        double valorFinal = valor * Math.pow((1 + TaxasConta.taxaJuros), qtdMeses);
        System.out.printf("O rendimento do depósito de R$ %.2f por %d dias(%d meses) foi R$ %.2f.\n", valor, qtdDias, qtdMeses, valorFinal);


        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoArquivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");

        File pathRelatorioSimulacaoRendimento = new File ("C:\\RepositorioBanco\\Relatorios\\Clientes\\Simulacoes\\");
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

    public void render() throws IOException {
        boolean isDataAniversario = this.aniversarioConta == LocalDate.now().getDayOfMonth();
        boolean isUltimoDiaMes = (LocalDate.now().getDayOfMonth() == LocalDate.now().lengthOfMonth());
        boolean isAniversarioMaiorQueDiasMes = (this.aniversarioConta > LocalDate.now().lengthOfMonth());

        if(isDataAniversario || (isUltimoDiaMes && isAniversarioMaiorQueDiasMes)) {
            this.saldo *= (1 + TaxasConta.taxaJuros);
        }
        atualizaSaldo(TipoConta.POUPANCA, this.cpfTitular);
    }

    @Override
    public void deposito(double valor) throws ValorNegativoException, IOException {
        super.deposito(valor);
        if(this.aniversarioConta == 0) {
            this.aniversarioConta = LocalDate.now().getDayOfMonth();
        }
    }

    public int getAniversarioConta() {
        return this.aniversarioConta;
    }
}
