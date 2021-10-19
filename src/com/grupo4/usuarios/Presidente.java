package com.grupo4.usuarios;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.contas.ContaPoupanca;
import com.grupo4.enums.Cargo;
import com.grupo4.interfaces.GeradorRelatorioDiretoria;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;
import com.grupo4.repositorios.SeguroVidaRepositorio;
import com.grupo4.segurovida.SeguroVida;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** Classe para objetos do tipo Presidente, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Funcionario como superclasse.
 */
public class Presidente extends Funcionario implements GeradorRelatorioDiretoria{
    private static final Cargo cargo = Cargo.PRESIDENTE;

    /** Construtor para instanciar novo Gerente durante o fluxo da aplicação.
     *
     * @param nomeExt nome do presidente
     * @param cpfExt CPF do presidente
     * @param senhaExt senha do presidente
     */
    public Presidente(String nomeExt, String cpfExt, String senhaExt) {
        super(nomeExt, cpfExt, senhaExt);
    }

    /** Método para gerar o relatório com o capital total armazenado no banco.
     *
     * @throws IOException se ocorrer um erro de escrita no arquivo do relatório
     */
    public void geraRelatorioCapitalBanco() throws IOException {
        List<ContaCorrente> listaContasCorrente = ContaCorrenteRepositorio.getContasCorrente();
        List<ContaPoupanca> listaContasPoupanca = ContaPoupancaRepositorio.getContasPoupanca();
        List<SeguroVida> listaSegurosVida = SeguroVidaRepositorio.getSegurosVida();

        double capitalTotal = 0;

        for (ContaCorrente contaCorrente : listaContasCorrente) {
            capitalTotal += contaCorrente.getSaldo();
        }
        for (ContaPoupanca contaPoupanca : listaContasPoupanca) {
            capitalTotal += contaPoupanca.getSaldo();
        }
        for (SeguroVida seguroVida : listaSegurosVida) {
            capitalTotal += seguroVida.getValorPago();
        }

        System.out.println("\nO capital total armazenado no banco é de: R$ " + String.format("%.2f", capitalTotal));

        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoArquivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");


        File pathRelatorioCapitalBanco = new File("C:\\RepositorioBanco\\Relatorios\\Presidencia\\");
        File relatorioCapitalBanco = new File(pathRelatorioCapitalBanco.getAbsolutePath() + "\\CapitalBanco-" + formatoArquivo.format(momentoAtual) + ".txt");

        if(!pathRelatorioCapitalBanco.exists()) {
            pathRelatorioCapitalBanco.mkdirs();
        }

        if (!relatorioCapitalBanco.exists()) {
            relatorioCapitalBanco.createNewFile();
        }

        try (FileWriter relatorioCapitalBancoWriter = new FileWriter(relatorioCapitalBanco);
             BufferedWriter relatorioCapitalBancoWriterBuff = new BufferedWriter(relatorioCapitalBancoWriter)) {

            relatorioCapitalBancoWriterBuff.append("Relatório de capital no banco - " + formatoBrasileiro.format(momentoAtual));
            relatorioCapitalBancoWriterBuff.newLine();
            relatorioCapitalBancoWriterBuff.append("O capital total armazenado no banco é de: R$ " + String.format("%.2f", capitalTotal));

        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos");
        }
    }

    public static Cargo getCargo() {
        return cargo;
    }
}
