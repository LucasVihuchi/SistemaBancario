package com.grupo4.usuarios;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.Cargo;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;
import com.grupo4.repositorios.UsuarioRepositorio;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Classe para objetos do tipo Gerente, onde serão contidos, atributos e métodos para o mesmo. Essa classe possui a classe Funcionario como superclasse.
 */
public class Gerente extends Funcionario{
    private Agencia agencia;
    private static final Cargo cargo = Cargo.GERENTE;

    /** Construtor para instanciar novo Gerente durante o fluxo da aplicação.
     *
     * @param nomeExt nome do gerente
     * @param cpfExt CPF do gerente
     * @param senhaExt senha do gerente
     * @param agenciaExt agência pela qual o gerente é responsável
     */
    public Gerente(String nomeExt, String cpfExt, String senhaExt, Agencia agenciaExt) {
        super(nomeExt, cpfExt, senhaExt);
        this.agencia = agenciaExt;
    }

    /** Método para gerar relatório do número de contas na agência do gerente.
     *
     * @throws IOException se ocorrer um erro de escrita no arquivo do relatório
     */
    public void geraRelatorioNumContas() throws IOException {
        int qtdContasCorrente = ContaCorrenteRepositorio.getContasCorrentePorAgencia(agencia).size();
        int qtdContasPoupanca = ContaPoupancaRepositorio.getContasPoupancaPorAgencia(agencia).size();
        System.out.println("O numero de contas corrente na agência " +
                this.agencia.getIdAgencia() + " é igual a " + qtdContasCorrente);
        System.out.println("O numero de contas poupança na agência " +
                this.agencia.getIdAgencia() + " é igual a " + qtdContasPoupanca);
        System.out.println("O numero de total de contas na agência " +
                this.agencia.getIdAgencia() + " é igual a " + (qtdContasPoupanca+qtdContasCorrente));

        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        DateTimeFormatter formatoArquivo = DateTimeFormatter.ofPattern("dd-MM-yyyy_HH-mm-ss");


        File pathRelatorioNumContas = new File("C:\\RepositorioBanco\\Relatorios\\Gerencia\\");
        File relatorioNumContas = new File(pathRelatorioNumContas.getAbsolutePath() + "\\NumContas-" + formatoArquivo.format(momentoAtual) + ".txt");

        if(!pathRelatorioNumContas.exists()) {
            pathRelatorioNumContas.mkdirs();
        }

        if(!relatorioNumContas.exists()) {
            relatorioNumContas.createNewFile();
        }

        try (FileWriter relatorioNumContasWriter = new FileWriter(relatorioNumContas);
             BufferedWriter relatorioNumContasWriterBuff = new BufferedWriter(relatorioNumContasWriter)) {

            relatorioNumContasWriterBuff.append("Relatório de número de contas na agência - " + formatoBrasileiro.format(momentoAtual));
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("Nome: " + UsuarioRepositorio.getUsuario(this.cpf).getNome() + " / CPF: " + this.cpf);
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de contas corrente na agência " +
                    this.agencia.getIdAgencia() + " é igual a " + qtdContasCorrente);
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de contas poupança na agência " +
                    this.agencia.getIdAgencia() + " é igual a " + qtdContasPoupanca);
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de total de contas na agência " +
                    this.agencia.getIdAgencia() + " é igual a " + (qtdContasPoupanca+qtdContasCorrente));

        } catch (IOException | CpfInexistenteException e) {
            System.out.println("Erro de escrita de arquivos");
        }
    }

    public Agencia getAgencia() {
        return this.agencia;
    }

    public static Cargo getCargo() {
        return cargo;
    }
}
