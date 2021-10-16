package com.grupo4;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.contas.ContaPoupanca;
import com.grupo4.enums.Agencia;
import com.grupo4.enums.TipoConta;
import com.grupo4.exceptions.*;
import com.grupo4.repositorios.ContaCorrenteRepositorio;
import com.grupo4.repositorios.ContaPoupancaRepositorio;
import com.grupo4.repositorios.SeguroVidaRepositorio;
import com.grupo4.repositorios.UsuarioRepositorio;
import com.grupo4.usuarios.*;

import java.io.IOException;
import java.util.*;

// 1 - Carrega as informações dos arquivos
// 2 - pergunta se quer fazer login ou cadastro de contas

// CADASTRO
// 3.B - Perguntar os dados do cliente se ele tiver cadastro, só loga, senão cria cadastro de cliente
// 4.B - Perguntar os dados da conta
// 5.B - Fecha o cadastro


public class SistemaInterno {

    public static void main(String[] args) {
        repositoriosLoader();

        while (true) {
            Scanner leitor = new Scanner(System.in);
            System.out.println("Bem vindo(a), Usuário(a)");
            System.out.print("Escolha uma das opções abaixo:" +
                    "\n1 - Login" +
                    "\n2 - Cadastro de contas" +
                    "\n0 - Sair" +
                    "\nOpção: ");

            int opcao;
            try {
                opcao = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início...\n");
                continue;
            } finally {
                leitor.nextLine();
            }

            switch (opcao) {
                case 1 -> {
                    Usuario usuario = realizaLogin();
                    if (usuario == null) {
                        continue;
                    }
                    caixaEletronico(usuario);
                }
                case 2 -> {
                    realizaCadastroConta();
                }
                case 0 -> {
                    System.exit(0);
                }
                default -> {
                    System.out.println("\nValor inserido inválido. Retornando ao início...\n");
                    continue;
                }
            }
        }

    }

    // Finalizado
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

    public static Usuario realizaLogin() {
        Scanner leitor = new Scanner(System.in);

        System.out.print("Insira seu CPF: ");
        String cpfLogin = leitor.nextLine();
        System.out.print("Insira sua senha: ");
        String senhaLogin = leitor.nextLine();
        System.out.println();
        Usuario usuarioLogin;
        try {
            usuarioLogin = UsuarioRepositorio.getUsuario(cpfLogin);
        } catch (CpfInexistenteException e) {
            System.out.println(e.getMessage() + "\n");
            return null;
        }
        try {
            usuarioLogin.logar(senhaLogin);
        } catch (SenhaIncorretaException e) {
            System.out.println(e.getMessage());
            return null;
        }
        return usuarioLogin;
    }

    public static void caixaEletronico(Usuario usuarioExt) {
        Scanner leitor = new Scanner(System.in);

        while (true) {
            System.out.print("\nEscolha qual tipo de conta você deseja: " +
                    "\n1 - Conta corrente" +
                    "\n2 - Conta poupança" +
                    "\n0 - Sair" +
                    "\nOpção: ");

            int opcaoConta;
            try {
                opcaoConta = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início da seleção de conta...\n");
                continue;
            } finally {
                leitor.nextLine();
            }

            if (opcaoConta < 0 || opcaoConta > 2) {
                System.out.println("\nValor inserido inválido. Retornando ao início da seleção de conta...\n");
                continue;
            } else if (opcaoConta == 0) {
                System.exit(0);
            } else if (opcaoConta == 1) {
                ContaCorrente contaCorrenteLogada;
                try {
                    contaCorrenteLogada = ContaCorrenteRepositorio.getContaCorrente(usuarioExt.getCpf());
                } catch (CpfInexistenteException e) {
                    System.out.println("\nConta corrente associada ao CPF não existe!");
                    continue;
                }
                while (true) {
                    System.out.println("\nEscolha uma das opções abaixo:" +
                            "\n1 - Saque" +
                            "\n2 - Depósito" +
                            "\n3 - Transferência" +
                            "\n4 - Exibir Saldo" +
                            "\n5 - Gerar relatório de tributação" +
                            "\n6 - Contratar seguro de vida");

                    if (usuarioExt instanceof Gerente || usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("7 - Gerar relatório de numero de contas na(s) agência(s)");
                    }
                    if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("8 - Gerar relatório de informações dos clientes no sistema");
                    }
                    if (usuarioExt instanceof Presidente) {
                        System.out.println("9 - Gerar relatório do capital total armazenado no banco");
                    }
                    System.out.print("0 - Sair" +
                            "\nOpção: ");

                    int opcaoOperacao;
                    try {
                        opcaoOperacao = leitor.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                        continue;
                    } finally {
                        leitor.nextLine();
                    }

                    if (opcaoOperacao == 0) {
                        System.exit(0);
                    }

                    Gerente gerenteLogado = null;
                    Diretor diretorLogado = null;
                    Presidente presidenteLogado = null;
                    if (usuarioExt instanceof Cliente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 6) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                    }
                    else if (usuarioExt instanceof Gerente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 7) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        gerenteLogado = (Gerente) usuarioExt;
                    }
                    else if (usuarioExt instanceof Diretor) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 8) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        diretorLogado = (Diretor) usuarioExt;
                    }
                    else if (usuarioExt instanceof Presidente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 9) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        presidenteLogado = (Presidente) usuarioExt;
                    }

                    switch (opcaoOperacao) {
                        case 1 -> {
                            System.out.print("Insira o valor a ser retirado: ");
                            double valorSaque = leitor.nextDouble();
                            try {
                                contaCorrenteLogada.saque(valorSaque);
                            } catch (ValorNegativoException | SaldoInsuficienteException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorSaque) + " foi sacado com sucesso!");
                        }
                        case 2 -> {
                            System.out.print("Insira o valor a ser depositado: ");
                            double valorDeposito = leitor.nextDouble();
                            try {
                                contaCorrenteLogada.deposito(valorDeposito);
                            } catch (ValorNegativoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorDeposito) + " foi depositado com sucesso!");
                        }
                        case 3 -> {
                            System.out.print("Insira o valor a ser transferido: ");
                            double valorTransferencia = leitor.nextDouble();
                            System.out.print("Insira o cpf do destinatário: ");
                            leitor.nextLine();
                            String cpfDestinatario = leitor.nextLine();
                            System.out.print("Escolha o tipo de conta:" +
                                    "\n1 - Conta Corrente" +
                                    "\n2 - Conta Poupança" +
                                    "\nOpção: ");

                            int idTipoConta = leitor.nextInt();
                            TipoConta tipoContaTransferencia = TipoConta.getTipoContaPorIndice(idTipoConta);
                            try {
                                contaCorrenteLogada.transferencia(valorTransferencia, cpfDestinatario, tipoContaTransferencia);
                            } catch (ValorNegativoException | SaldoInsuficienteException | CpfInexistenteException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorTransferencia) + " foi transferido com sucesso!");
                        }
                        case 4 -> contaCorrenteLogada.exibirSaldo();
                        case 5 -> {
                            try {
                                contaCorrenteLogada.geraRelatorioTributacao();
                            } catch (IOException e) {
                                System.out.println("Erro de leitura/escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 6 -> {
                            System.out.print("Insira o valor a ser segurado: ");
                            double valorTotalSegurado = leitor.nextDouble();
                            System.out.print("Insira em quantos meses deseja pagar o seguro: ");
                            int qtdMeses = leitor.nextInt();
                            List<String> segurados = new ArrayList<>();
                            while (true) {
                                System.out.print("Escolha uma das opções abaixo:" +
                                        "\n1 - Adicionar novo segurado" +
                                        "\n0 - Finalizar" +
                                        "\nOpção: ");
                                int opcaoSegurados;
                                try {
                                    opcaoSegurados = leitor.nextInt();
                                } catch (InputMismatchException e) {
                                    System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                    continue;
                                } finally {
                                    leitor.nextLine();
                                }

                                if (opcaoSegurados == 1) {
                                    System.out.print("Insira o CPF do segurado: ");
                                    String segurado = leitor.nextLine();
                                    segurados.add(segurado);
                                } else if (opcaoSegurados == 0) {
                                    break;
                                } else {
                                    System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                    continue;
                                }
                            }
                            try {
                                contaCorrenteLogada.contrataSeguroVida(valorTotalSegurado, qtdMeses, segurados);
                            } catch (SaldoInsuficienteException | ValorNegativoException | CpfInexistenteException | SeguroExistenteException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO seguro no valor de R$ " + String.format("%.2f", valorTotalSegurado) + " foi contratado com sucesso!");
                        }
                        case 7 -> {
                            if (usuarioExt instanceof Gerente) {
                                try {
                                    gerenteLogado.geraRelatorioNumContas();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                                Set<Agencia> agencias = new HashSet<>();
                                while (true) {
                                    System.out.print("Escolha uma das opções abaixo: " +
                                            "\n1 - Selecionar nova agencia" +
                                            "\n0 - Finalizar" +
                                            "\nOpção: ");
                                    int opcaoAgencias;
                                    try {
                                        opcaoAgencias = leitor.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                        continue;
                                    }  finally {
                                        leitor.nextLine();
                                    }

                                    if (opcaoAgencias == 1) {
                                        System.out.print("Insira o número da agência: ");
                                        int idAgencia = leitor.nextInt();
                                        Agencia agencia = Agencia.getAgenciaPorId(idAgencia);
                                        agencias.add(agencia);
                                    } else if (opcaoAgencias == 0) {
                                        break;
                                    } else {
                                        System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                        continue;
                                    }
                                }

                                if (usuarioExt instanceof Diretor) {
                                    try {
                                        diretorLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        System.exit(1);
                                    }
                                } else if (usuarioExt instanceof Presidente) {
                                    try {
                                        presidenteLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        System.exit(1);
                                    }
                                }
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 8 -> {
                            if (usuarioExt instanceof Diretor) {
                                try {
                                    diretorLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Presidente) {
                                try {
                                    presidenteLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 9 -> {
                            try {
                                presidenteLogado.geraRelatorioCapitalBanco();
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                    }

                }


            }
            else if (opcaoConta == 2) {
                ContaPoupanca contaPoupancaLogada;
                try {
                    contaPoupancaLogada = ContaPoupancaRepositorio.getContaPoupanca(usuarioExt.getCpf());
                } catch (CpfInexistenteException e) {
                    System.out.println("\nConta Poupança associada ao CPF não existe!");
                    continue;
                }

                while (true) {
                    System.out.println("\nEscolha uma das opções abaixo:" +
                            "\n1 - Saque" +
                            "\n2 - Depósito" +
                            "\n3 - Transferência" +
                            "\n4 - Exibir Saldo" +
                            "\n5 - Gerar relatório de simulação de rendimento");

                    if (usuarioExt instanceof Gerente || usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("6 - Gerar relatório de numero de contas na(s) agência(s)");
                    }
                    if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("7 - Gerar relatório de informações dos clientes no sistema");
                    }
                    if (usuarioExt instanceof Presidente) {
                        System.out.println("8 - Gerar relatório do capital total armazenado no banco");
                    }
                    System.out.print("0 - Sair" +
                            "\nOpção: ");

                    int opcaoOperacao;
                    try {
                        opcaoOperacao = leitor.nextInt();
                    } catch (InputMismatchException e) {
                        System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                        continue;
                    } finally {
                        leitor.nextLine();
                    }

                    if (opcaoOperacao == 0) {
                        System.exit(0);
                    }
                    System.out.println();

                    Gerente gerenteLogado = null;
                    Diretor diretorLogado = null;
                    Presidente presidenteLogado = null;
                    if (usuarioExt instanceof Cliente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 5) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                    }
                    else if (usuarioExt instanceof Gerente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 6) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        gerenteLogado = (Gerente) usuarioExt;
                    }
                    else if (usuarioExt instanceof Diretor) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 7) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        diretorLogado = (Diretor) usuarioExt;
                    }
                    else if (usuarioExt instanceof Presidente) {
                        if (opcaoOperacao < 1 || opcaoOperacao > 8) {
                            System.out.println("\nValor inserido inválido. Retornando a seleção de operação...\n");
                            continue;
                        }
                        presidenteLogado = (Presidente) usuarioExt;
                    }

                    switch (opcaoOperacao) {
                        case 1 -> {
                            System.out.print("Insira o valor a ser retirado: ");
                            double valorSaque = leitor.nextDouble();
                            try {
                                contaPoupancaLogada.saque(valorSaque);
                            } catch (ValorNegativoException | SaldoInsuficienteException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorSaque) + " foi sacado com sucesso!");
                        }
                        case 2 -> {
                            System.out.print("Insira o valor a ser depositado: ");
                            double valorDeposito = leitor.nextDouble();
                            try {
                                contaPoupancaLogada.deposito(valorDeposito);
                            } catch (ValorNegativoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorDeposito) + " foi depositado com sucesso!");
                        }
                        case 3 -> {
                            System.out.print("Insira o valor a ser transferido: ");
                            double valorTransferencia = leitor.nextDouble();
                            System.out.print("Insira o cpf do destinatário: ");
                            String cpfDestinatario = leitor.nextLine();
                            System.out.print("Escolha o tipo de conta:" +
                                    "\n1 - Conta Corrente" +
                                    "\n2 - Conta Poupança" +
                                    "\nOpção: ");
                            int idTipoConta = leitor.nextInt();
                            TipoConta tipoContaTransferencia = TipoConta.getTipoContaPorIndice(idTipoConta);
                            try {
                                contaPoupancaLogada.transferencia(valorTransferencia, cpfDestinatario, tipoContaTransferencia);
                            } catch (ValorNegativoException | SaldoInsuficienteException | CpfInexistenteException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorTransferencia) + " foi transferido com sucesso!");
                        }
                        case 4 -> contaPoupancaLogada.exibirSaldo();
                        case 5 -> {
                            System.out.print("Insira o valor a ser simulado: ");
                            int valorSimulacao = leitor.nextInt();
                            System.out.print("Insira a quantidade de dias para serem simulados: ");
                            int qtdDiasSimulacao = leitor.nextInt();
                            try {
                                contaPoupancaLogada.geraSimulacaoRendimento(valorSimulacao, qtdDiasSimulacao);
                            } catch (ValorNegativoException | ValorInvalidoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 6 -> {
                            if (usuarioExt instanceof Gerente) {
                                try {
                                    gerenteLogado.geraRelatorioNumContas();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                                Set<Agencia> agencias = new HashSet<>();
                                while (true) {
                                    System.out.print("Escolha uma das opções abaixo: " +
                                            "\n1 - Selecionar nova agencia" +
                                            "\n0 - Finalizar" +
                                            "\nOpção: ");
                                    int opcaoAgencias;
                                    try {
                                        opcaoAgencias = leitor.nextInt();
                                    } catch (InputMismatchException e) {
                                        System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                        continue;
                                    } finally {
                                        leitor.nextLine();
                                    }

                                    if (opcaoAgencias == 1) {
                                        System.out.print("Insira o número da agência: ");
                                        int idAgencia = leitor.nextInt();
                                        Agencia agencia = Agencia.getAgenciaPorId(idAgencia);
                                        agencias.add(agencia);
                                    } else if (opcaoAgencias == 0) {
                                        break;
                                    } else {
                                        System.out.println("\nValor inserido inválido. Retornando ao menu anterior...\n");
                                        continue;
                                    }
                                }

                                if (usuarioExt instanceof Diretor) {
                                    try {
                                        diretorLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        System.exit(1);
                                    }
                                } else if (usuarioExt instanceof Presidente) {
                                    try {
                                        presidenteLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        System.exit(1);
                                    }
                                }
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 7 -> {
                            if (usuarioExt instanceof Diretor) {
                                try {
                                    diretorLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Presidente) {
                                try {
                                    presidenteLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                        case 8 -> {
                            try {
                                presidenteLogado.geraRelatorioCapitalBanco();
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                System.exit(1);
                            }
                            System.out.println("Relatório gerado com sucesso!");
                        }
                    }
                }
            }
        }

    }

    public static void realizaCadastroConta() {
        Scanner leitor = new Scanner(System.in);

        while(true) {
            System.out.print("\nEscolha o tipo de conta que deseja cadastrar:" +
                    "\n1 - Conta corrente" +
                    "\n2 - Conta poupança" +
                    "\n0 - Voltar ao menu anterior" +
                    "\nOpção: ");

            int opcaoCadastro;
            try {
                opcaoCadastro = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            }

            if (opcaoCadastro < 0 || opcaoCadastro > 2) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            } else if (opcaoCadastro == 0){
                return;
            }
            leitor.nextLine();
            System.out.print("Insira seu CPF: ");
            String cpfCadastro = leitor.nextLine();
            System.out.print("Insira o número da agência: ");
            int idAgenciaCadastro = leitor.nextInt();
            Agencia agenciaCadastro = Agencia.getAgenciaPorId(idAgenciaCadastro);
            try {
                UsuarioRepositorio.getUsuario(cpfCadastro);
            } catch (CpfInexistenteException e) {
                if(!realizaCadastroCliente()) {
                    continue;
                }
            }

            if(opcaoCadastro == 1) {
                ContaCorrente contaCorrenteCadastro = new ContaCorrente(cpfCadastro, agenciaCadastro);
                try {
                    ContaCorrenteRepositorio.adicionaContaCorrente(contaCorrenteCadastro);
                } catch (ContaExistenteException e) {
                    System.out.println(e.getMessage());
                    continue;
                } catch (IOException e) {
                    System.out.println("Erro de escrita de arquivos");
                    System.exit(1);
                }
                System.out.println("Conta corrente cadastrada com sucesso!");
            }
            else if (opcaoCadastro == 2) {
                ContaPoupanca contaPoupancaCadastro = new ContaPoupanca(cpfCadastro, agenciaCadastro);
                try {
                    ContaPoupancaRepositorio.adicionaContaPoupanca(contaPoupancaCadastro);
                } catch (ContaExistenteException e) {
                    System.out.println(e.getMessage());
                    continue;
                } catch (IOException e) {
                    System.out.println("Erro de escrita de arquivos");
                    System.exit(1);
                }
                System.out.println("Conta poupança cadastrada com sucesso!");
            }
            System.out.println();
            return;
        }
    }

    // Finalizado
    public static boolean realizaCadastroCliente() {
        Scanner leitor = new Scanner(System.in);

        escolhaTipoUsuario: while (true) {
            System.out.print("\nEscolha o tipo de usuário que deseja cadastrar:" +
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
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            } finally {
                leitor.nextLine();
            }

            if (opcaoCadastro < 0 || opcaoCadastro > 4) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            } else if (opcaoCadastro == 0){
                return false;
            }

            System.out.print("Insira seu nome completo: ");
            String nomeCadastro = leitor.nextLine();
            System.out.print("Insira seu CPF: ");
            String cpfCadastro = leitor.nextLine();
            System.out.print("Insira sua senha: ");
            String senhaCadastro = leitor.nextLine();
            System.out.print("");

            if (opcaoCadastro == 1) {
                Cliente clienteCadastro = new Cliente(nomeCadastro, cpfCadastro, senhaCadastro);
                try {
                    UsuarioRepositorio.adicionaUsuario(clienteCadastro);
                } catch (UsuarioExistenteException e) {
                    System.out.println(e.getMessage());
                    continue;
                } catch (IOException e) {
                    System.out.println("Erro de escrita de arquivos");
                    System.exit(1);
                }
                System.out.println("\nCliente cadastrado com sucesso!");
            }
            else {
                int contadorSenha = 0;
                while (true) {
                    System.out.print("Insira a senha de administrador: ");
                    String senhaAdmin = leitor.nextLine();
                    if (senhaAdmin.equals(Funcionario.getSenhaAdmin())) {
                        switch (opcaoCadastro) {
                            case 2:
                                System.out.print("Insira o número da agência do gerente: ");
                                int idAgenciaCadastro = leitor.nextInt();
                                Agencia agenciaCadastro = Agencia.getAgenciaPorId(idAgenciaCadastro);
                                Gerente gerenteCadastro = new Gerente(nomeCadastro, cpfCadastro, senhaCadastro, agenciaCadastro);
                                try {
                                    UsuarioRepositorio.adicionaUsuario(gerenteCadastro);
                                } catch (UsuarioExistenteException e) {
                                    System.out.println(e.getMessage());
                                    continue escolhaTipoUsuario;
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                                System.out.println("\nGerente cadastrado com sucesso!");
                                break;
                            case 3:
                                Diretor diretorCadastro = new Diretor(nomeCadastro, cpfCadastro, senhaCadastro);
                                try {
                                    UsuarioRepositorio.adicionaUsuario(diretorCadastro);
                                } catch (UsuarioExistenteException e) {
                                    System.out.println(e.getMessage());
                                    continue escolhaTipoUsuario;
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                                System.out.println("\nDiretor cadastrado com sucesso!");
                                break;
                            case 4:
                                if (UsuarioRepositorio.isPresidenteCadastrado()) {
                                    System.out.println("Presidente já cadastrado no sistema!");
                                    continue escolhaTipoUsuario;
                                }
                                Presidente presidenteCadastro = new Presidente(nomeCadastro, cpfCadastro, senhaCadastro);
                                try {
                                    UsuarioRepositorio.adicionaUsuario(presidenteCadastro);
                                } catch (UsuarioExistenteException e) {
                                    System.out.println(e.getMessage());
                                    continue escolhaTipoUsuario;
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    System.exit(1);
                                }
                                System.out.println("\nPresidente cadastro com sucesso!");
                                break;
                        }
                    }
                    else {
                        contadorSenha++;
                        System.out.println("Senha de administrador incorreta!");
                        if (contadorSenha >= 3) {
                            System.out.println("Voltando ao início....");
                            continue escolhaTipoUsuario;
                        } else {
                            continue;
                        }
                    }
                    break;
                }
            }
            return true;
        }
    }
}
