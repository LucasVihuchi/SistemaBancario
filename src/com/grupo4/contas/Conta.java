package com.grupo4.contas;

import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SaldoInsuficienteException;
import com.grupo4.exceptions.ValorNegativoException;
import com.grupo4.interfaces.TaxasConta;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;

import java.io.*;
import java.time.LocalDate;
import java.util.Locale;

/** Classe abstrata para objetos do tipo Conta, onde serão contidos, atributos e métodos para o mesmo.
 */
public abstract class Conta {
    protected String cpfTitular;
    protected double saldo;
    protected Agencia agencia;
    protected static TipoConta tipo;

    /** Construtor para instanciar nova Conta durante o fluxo da aplicação.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     */
    public Conta(String cpfTitularExt, Agencia agenciaExt) {
        this.cpfTitular = cpfTitularExt;
        this.agencia = agenciaExt;
    }

    /** Construtor para instanciar nova Conta apenas no carregamento da aplicação.
     * Note que esse construtor deve ser utilizado apenas pelos Loaders de arquivos.
     *
     * @param cpfTitularExt CPF do titular da conta
     * @param agenciaExt agência à qual a conta está associada
     * @param saldoExt saldo da conta
     */
    public Conta(String cpfTitularExt, Agencia agenciaExt, double saldoExt) {
        this(cpfTitularExt, agenciaExt);
        this.saldo = saldoExt;
    }

    /** Método para sacar saldo da conta.
     *
     * @param valor valor a ser sacado
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws SaldoInsuficienteException se não houver saldo suficiente na conta
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
    public void saque(double valor) throws ValorNegativoException, SaldoInsuficienteException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("\nSaque de valores negativos não é permitido!");
        }
        if (this.saldo < (valor + TaxasConta.taxaSaque)) {
            throw new SaldoInsuficienteException();
        }
        this.saldo -= (valor + TaxasConta.taxaSaque);
        registraTransacao(valor, "saque");
        if (this instanceof ContaCorrente) {
            atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
        } else if (this instanceof ContaPoupanca) {
            atualizaSaldo(TipoConta.POUPANCA, this.cpfTitular);
        }
    }

    /** Método para depositar saldo na conta.
     *
     * @param valor valor a ser depositado
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
    public void deposito(double valor) throws ValorNegativoException, IOException {
        if (valor <= TaxasConta.taxaDeposito) {
            throw new ValorNegativoException("\nDepósito de valores negativos não é permitido!");
        }
        this.saldo += (valor - TaxasConta.taxaDeposito);
        registraTransacao(valor, "deposito");
        if (this instanceof ContaCorrente) {
            atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
        } else if (this instanceof ContaPoupanca) {
            atualizaSaldo(TipoConta.POUPANCA, this.cpfTitular);
        }
    }

    /** Método para transferir saldo de uma conta para outra conta no próprio banco.
     *
     * @param valor valor a ser transferido
     * @param cpfDestinatario CPF do destinatário da conta
     * @param tipoExt tipo de conta do destinatário
     * @throws ValorNegativoException se um valor negativo for fornecido
     * @throws SaldoInsuficienteException se não houver saldo suficiente na conta
     * @throws CpfInexistenteException se a conta do tipo informado associada ao CPF do destinatário não for encontrada no sistema
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
    public void transferencia(double valor, String cpfDestinatario, TipoConta tipoExt) throws ValorNegativoException, SaldoInsuficienteException, CpfInexistenteException, IOException {
        if (valor <= 0) {
            throw new ValorNegativoException("Transferência de valores negativos não é permitido");
        }
        if (this.saldo < (valor + TaxasConta.taxaTransferencia)) {
            throw new SaldoInsuficienteException();
        }

        if (tipoExt.equals(TipoConta.CORRENTE)) {
            ContaCorrenteRepositorio.getContaCorrente(cpfDestinatario).saldo += valor;
        } else if (tipoExt.equals(TipoConta.POUPANCA)) {
            ContaPoupanca contaPoupanca = ContaPoupancaRepositorio.getContaPoupanca(cpfDestinatario);
            contaPoupanca.saldo += valor;
            if (contaPoupanca.getAniversarioConta() == 0) {
                contaPoupanca.setAniversarioConta(LocalDate.now().getDayOfMonth());
                contaPoupanca.atualizaAniversarioConta();
            }
        }
        this.saldo -= (valor + TaxasConta.taxaTransferencia);
        registraTransacao(valor, "transferencia", cpfDestinatario);

        if (this instanceof ContaCorrente) {
            atualizaSaldo(TipoConta.CORRENTE, this.cpfTitular);
        } else if (this instanceof ContaPoupanca) {
            atualizaSaldo(TipoConta.POUPANCA, this.cpfTitular);
        }
        atualizaSaldo(tipoExt, cpfDestinatario);
    }

    /** Método para exibir saldo da conta.
     */
    public void exibirSaldo() {
        System.out.println("\nSaldo atual na conta: R$ " + String.format("%.2f", this.saldo));
    }

    /** Método para registrar uma transação da conta atual no arquivo de histórico de transações.
     *
     * @param valor valor a ser transferido
     * @param tipoTransacao tipo de transação a ser registrada
     * @param cpfDestinatario CPF do destinatário da conta
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
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
            if (this instanceof ContaCorrente) {
                historicoTransacoesDBWriterBuff.append("c");
            } else if (this instanceof ContaPoupanca) {
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

    /** Método para atualizar saldo de uma conta no arquivo de registro de contas do TipoConta fornecido.
     *
     * @param tipoConta tipo de conta do destinatário
     * @param cpfUsuario CPF do proprietário da conta
     * @throws IOException se ocorrer um erro de escrita no arquivo de histórico de transações
     */
    protected void atualizaSaldo(TipoConta tipoConta, String cpfUsuario) throws IOException {
        File pathContaBD = new File("C:\\RepositorioBanco\\");
        File contaBD = null;
        if (tipoConta.equals(TipoConta.CORRENTE)) {
            contaBD = new File(pathContaBD.getAbsolutePath() + "\\contaCorrenteRepositorio.txt");
        } else if (tipoConta.equals(TipoConta.POUPANCA)) {
            contaBD = new File(pathContaBD.getAbsolutePath() + "\\contaPoupancaRepositorio.txt");
        }

        if (!pathContaBD.exists()) {
            pathContaBD.mkdirs();
        }

        if (!contaBD.exists()) {
            contaBD.createNewFile();
        }

        StringBuilder conteudoBD = new StringBuilder();

        try (FileReader contaBDReader = new FileReader(contaBD);
             BufferedReader contaBDReaderBuff = new BufferedReader(contaBDReader)) {
            String linha;
            while ((linha = contaBDReaderBuff.readLine()) != null) {
                String[] separada = linha.split("¨¨");
                if (separada[0].equals(cpfUsuario)) {
                    if(tipoConta.equals(TipoConta.CORRENTE)) {
                        linha = linha.replace(separada[1], String.format(Locale.ROOT, "%.2f", ContaCorrenteRepositorio.getContaCorrente(cpfUsuario).getSaldo()));
                    } else if(tipoConta.equals(TipoConta.POUPANCA)) {
                        linha = linha.replace(separada[1], String.format(Locale.ROOT, "%.2f", ContaPoupancaRepositorio.getContaPoupanca(cpfUsuario).getSaldo()));
                    }
                }
                conteudoBD.append(linha + "\n");
            }
        } catch (IOException | CpfInexistenteException e) {
            System.out.println("Erro de leitura de arquivos!");
        }

        try (FileWriter contaDBWriter = new FileWriter(contaBD);
             BufferedWriter contaDBWriterBuff = new BufferedWriter(contaDBWriter)) {
            contaDBWriterBuff.append(conteudoBD);
        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
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
        return tipo;
    }
}
