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

    public void exibirSaldo() {
        System.out.println("\nSaldo atual na conta: R$ " + String.format("%.2f", this.saldo));
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
}
