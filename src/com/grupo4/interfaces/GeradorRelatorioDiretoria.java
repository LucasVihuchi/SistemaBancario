package com.grupo4.interfaces;

import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;
import com.grupo4.repositorios.UsuarioRepositorio;
import com.grupo4.usuarios.Diretor;
import com.grupo4.usuarios.Usuario;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface GeradorRelatorioDiretoria {

    default void geraRelatorioNumContas(Agencia... agenciaExt) throws IOException {
        int qtdContasCorrente;
        int qtdContasPoupanca;
        if (agenciaExt.length == 0) {
            qtdContasCorrente = ContaCorrenteRepositorio.getContasCorrente().size();
            qtdContasPoupanca = ContaPoupancaRepositorio.getContasPoupanca().size();
        } else {
            qtdContasCorrente = ContaCorrenteRepositorio.getContasCorrentePorAgencia(agenciaExt).size();
            qtdContasPoupanca = ContaPoupancaRepositorio.getContasPoupancaPorAgencia(agenciaExt).size();
        }

        System.out.println("O numero de contas corrente " +
                (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                " é igual a " + qtdContasCorrente);
        System.out.println("O numero de contas poupança na agência " +
                (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                " é igual a " + qtdContasPoupanca);
        System.out.println("O numero de total de contas na agência " +
                (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                " é igual a " + qtdContasPoupanca+qtdContasCorrente);

        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");


        File pathRelatorioNumContas;
        File relatorioNumContas;
        if (this instanceof Diretor) {
            pathRelatorioNumContas = new File("C:\\RepositorioBanco\\Relatorios\\Diretoria\\");
            relatorioNumContas = new File(pathRelatorioNumContas.getAbsolutePath() + "\\NumContas-" + momentoAtual + ".txt");
        } else {
            pathRelatorioNumContas = new File("C:\\RepositorioBanco\\Relatorios\\Presidencia\\");
            relatorioNumContas = new File(pathRelatorioNumContas.getAbsolutePath() + "\\NumContas-" + momentoAtual + ".txt");
        }

        if(!pathRelatorioNumContas.exists()) {
            pathRelatorioNumContas.mkdirs();
        }

        if(!relatorioNumContas.exists()) {
            relatorioNumContas.createNewFile();
        }

        try (FileWriter relatorioNumContasWriter = new FileWriter(relatorioNumContas);
             BufferedWriter relatorioNumContasWriterBuff = new BufferedWriter(relatorioNumContasWriter)) {

            relatorioNumContasWriterBuff.append("Relatório de número de contas nas agências - " + formatoBrasileiro.format(momentoAtual));
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de contas corrente na agência " +
                    (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                    " é igual a " + qtdContasCorrente);
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de contas poupança na agência " +
                    (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                    " é igual a " + qtdContasPoupanca);
            relatorioNumContasWriterBuff.newLine();
            relatorioNumContasWriterBuff.append("O numero de total de contas na agência " +
                    (agenciaExt.length == 0 ? "no banco" : "nas agências selecionadas " + Arrays.toString(agenciaExt)) +
                    " é igual a " + qtdContasPoupanca+qtdContasCorrente);

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos");
        }
    }

    default void geraRelatorioClientesBanco() throws IOException {
        List<Usuario> listaUsuarios = UsuarioRepositorio.getUsuarios();
        Collections.sort(listaUsuarios);

        LocalDateTime momentoAtual = LocalDateTime.now();
        DateTimeFormatter formatoBrasileiro = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");

        File pathrelatorioClientesBanco;
        File relatorioClientesBanco;
        if (this instanceof Diretor) {
            pathrelatorioClientesBanco = new File("C:\\RepositorioBanco\\Relatorios\\Diretoria\\");
            relatorioClientesBanco = new File(pathrelatorioClientesBanco.getAbsolutePath() + "\\ClientesBanco-" + momentoAtual + ".txt");
        } else {
            pathrelatorioClientesBanco = new File("C:\\RepositorioBanco\\Relatorios\\Presidencia\\");
            relatorioClientesBanco = new File(pathrelatorioClientesBanco.getAbsolutePath() + "\\ClientesBanco-" + momentoAtual + ".txt");
        }

        if (!pathrelatorioClientesBanco.exists()) {
            pathrelatorioClientesBanco.mkdirs();
        }

        if (!relatorioClientesBanco.exists()) {
            relatorioClientesBanco.createNewFile();
        }

        try (FileWriter relatorioClientesBancoWriter = new FileWriter(relatorioClientesBanco);
             BufferedWriter relatorioClientesBancoWriterBuff = new BufferedWriter(relatorioClientesBancoWriter)) {

            relatorioClientesBancoWriterBuff.append("Relatório de clientes no banco - " + formatoBrasileiro.format(momentoAtual));
            relatorioClientesBancoWriterBuff.newLine();
            relatorioClientesBancoWriterBuff.append("Nome, CPF, Agência Conta Corrente, Agência Conta Poupança");
            relatorioClientesBancoWriterBuff.newLine();
            for (Usuario u : listaUsuarios) {
                int idAgenciaContaCorrente;
                int idAgenciaContaPoupanca;
                try {
                    idAgenciaContaCorrente = ContaCorrenteRepositorio.getContaCorrente(u.getCpf()).getAgencia().getIdAgencia();
                } catch (CpfInexistenteException e) {
                    idAgenciaContaCorrente = -1;
                }
                try {
                    idAgenciaContaPoupanca = ContaPoupancaRepositorio.getContaPoupanca(u.getCpf()).getAgencia().getIdAgencia();
                } catch (CpfInexistenteException e) {
                    idAgenciaContaPoupanca = -1;
                }

                relatorioClientesBancoWriterBuff.append(u.getNome() + ", " +
                        u.getCpf() + ", " +
                        ((idAgenciaContaCorrente == -1) ? "" : idAgenciaContaCorrente) + ", " +
                        ((idAgenciaContaPoupanca == -1) ? "" : idAgenciaContaPoupanca));

                relatorioClientesBancoWriterBuff.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos");
        }
    }
}
