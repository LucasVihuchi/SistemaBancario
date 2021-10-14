package com.grupo4;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;
import com.grupo4.repositorios.SeguroVidaRepositorio;
import com.grupo4.repositorios.UsuarioRepositorio;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class SistemaInterno {
    public static void main(String[] args) {
        //repositoriosLoader();

        while (true) {
            Scanner leitor = new Scanner(System.in);
            System.out.println("Bem vindo(a), Usuario(a)");
            System.out.print("Escolha uma das opções abaixo:" +
                    "\n1 - Login" +
                    "\n2 - Cadastro de Contas" +
                    "\n0 - Sair" +
                    "\nOpção: ");

            int opcao;
            try {
                opcao = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início...\n\n\n");
                continue;
            }
            switch (opcao) {
                case 1:
                    // Altas coisas
                    break;
                case 2:
                    // Inicia o cadastro de conta
                    // realizaCadastroConta();
                    break;
                case 0:
                    System.exit(0);
                    break;
            }
        }

    }

    public static void repositoriosLoader() {
        try {
            UsuarioRepositorio.usuarioLoader();
            ContaCorrenteRepositorio.ContaCorrenteLoader();
            ContaPoupancaRepositorio.ContaPoupancaLoader();
            SeguroVidaRepositorio.SeguroVidaLoader();
        } catch (IOException e) {
            System.out.println("Erro de leitura nos arquivos");
            System.exit(1);
        }
    }

    public static void realizaCadastroCliente() {
        Scanner leitor = new Scanner(System.in);

        while (true) {
            System.out.print("\nEscolha o tipo de usuário que deseja cadastrar:\n" +
                    "\n1 - Cliente" +
                    "\n2 - Gerente" +
                    "\n3 - Diretor" +
                    "\n4 - Presidente" +
                    "\n0 - Voltar ao menu anterior" +
                    "\nOpção: ");

            int opcaoCadastro;
            try {
                opcaoCadastro = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n\n\n");
                continue;
            }

            System.out.print("Insira seu nome completo: ");
            String nomeCadastro = leitor.nextLine();
            System.out.print("Insira seu CPF: ");
            String cpfCadastro = leitor.nextLine();
            System.out.print("Insira sua senha: ");
            String senha = leitor.nextLine();

            if (UsuarioRepositorio.isUsuarioCadastrado(cpfCadastro)) {
                continue;
            }

            // Criar usuario de acordo com as opções que nós lemos
            // switch (opcaoCadastro){
            //
            // }



        }


    }
}
