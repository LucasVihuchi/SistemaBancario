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
import com.grupo4.validadores.ValidadorEntrada;

import java.io.IOException;
import java.util.*;

/** Classe que contém o menu principal da aplicação e classe main do sistema.
 */
public class SistemaInterno {

    /** Método main que contém o início do menu principal, bem como as chamadas para processos de login e cadastro de usuários.
     *
     * @param args argumentos recebidos no início da aplicação
     */
    public static void main(String[] args) {
        repositoriosLoader();

        while (true) {
            Scanner leitor = new Scanner(System.in);
            System.out.println("Bem vindo(a), Usuário(a)");
            System.out.print("""
                    Escolha uma das opções abaixo:
                    1 - Login
                    2 - Cadastro de contas
                    0 - Sair
                    Opção:\s""");

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
                    leitor.close();
                    System.exit(0);
                }
                default -> {
                    System.out.println("\nValor inserido inválido. Retornando ao início...\n");
                    continue;
                }
            }
        }

    }

    /** Método para chamar os métodos Loader dos repositórios que fazem o carregamento dos dados dos arquivos de texto.
     */
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

    /** Método para realizar o processo de autenticação de um usuário.
     *
     * @return O Usuário logado ou nulo se ocorrer um erro na autenticação
     */
    public static Usuario realizaLogin() {
        Scanner leitor = new Scanner(System.in);

        String cpfLogin;
        int numTentativasCpf = 0;
        while (true) {
            System.out.print("Insira seu CPF: ");
            cpfLogin = leitor.nextLine();
            try {
                ValidadorEntrada.validaCpf(cpfLogin);
            } catch (CpfInvalidoException e) {
                numTentativasCpf++;
                System.out.println(e.getMessage());
                if(numTentativasCpf >= 3) {
                    System.out.println("Retornando ao menu anterior...\n");
                    return null;
                }
                continue;
            }
            break;
        }

        String senhaLogin;
        int numTentativasSenha = 0;
        while (true) {
            System.out.print("Insira sua senha: ");
            senhaLogin = leitor.nextLine();
            try {
                ValidadorEntrada.validaSenha(senhaLogin);
            } catch (SenhaInvalidaException e) {
                numTentativasSenha++;
                System.out.println(e.getMessage());
                if(numTentativasSenha >= 3) {
                    System.out.println("Retornando ao menu anterior...\n");
                    return null;
                }
                continue;
            }
            break;
        }

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

    /** Método para realizar operações em uma conta-corrente ou conta-poupança
     *
     * @param usuarioExt usuário autenticado
     */
    public static void caixaEletronico(Usuario usuarioExt) {
        Scanner leitor = new Scanner(System.in);

        while (true) {
            System.out.print("""

                    Escolha qual tipo de conta você deseja:\s
                    1 - Conta corrente
                    2 - Conta poupança
                    0 - Sair
                    Opção:\s""");

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
                leitor.close();
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
                    System.out.println("""

                            Escolha uma das opções abaixo:
                            1 - Saque
                            2 - Depósito
                            3 - Transferência
                            4 - Exibir Saldo
                            5 - Gerar relatório de tributação
                            6 - Contratar seguro de vida""");

                    if (usuarioExt instanceof Gerente || usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("7 - Gerar relatório de numero de contas na(s) agência(s)");
                    }
                    if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                        System.out.println("8 - Gerar relatório de informações dos clientes no sistema");
                    }
                    if (usuarioExt instanceof Presidente) {
                        System.out.println("9 - Gerar relatório do capital total armazenado no banco");
                    }
                    System.out.print("0 - Sair" +  "\nOpção: ");

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
                        leitor.close();
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
                            double valorSaque = 0;
                            try {
                                valorSaque = leitor.nextDouble();
                                contaCorrenteLogada.saque(valorSaque);
                            } catch (ValorNegativoException | SaldoInsuficienteException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            } catch (InputMismatchException e) {
                                System.out.println("Erro! Insira apenas números!\n");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorSaque) + " foi sacado com sucesso!");
                        }
                        case 2 -> {
                            System.out.print("Insira o valor a ser depositado: ");
                            double valorDeposito = 0;
                            try {
                                valorDeposito = leitor.nextDouble();
                                contaCorrenteLogada.deposito(valorDeposito);
                            } catch (ValorNegativoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            } catch (InputMismatchException e) {
                                System.out.println("Erro! Insira apenas números!\n");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorDeposito) + " foi depositado com sucesso!");
                        }
                        case 3 -> {
                            System.out.print("Insira o valor a ser transferido: ");
                            double valorTransferencia = 0;
                            try {
                                valorTransferencia = leitor.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.print("Insira o cpf do destinatário: ");
                            String cpfDestinatario = leitor.nextLine();
                            try {
                                ValidadorEntrada.validaCpf(cpfDestinatario);
                            } catch (CpfInvalidoException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            }
                            System.out.print("""
                                    Escolha o tipo de conta:
                                    1 - Conta Corrente
                                    2 - Conta Poupança
                                    Opção:\s""");

                            int idTipoConta = 0;
                            try {
                                idTipoConta = leitor.nextInt();
                                ValidadorEntrada.validaTipoConta(idTipoConta);;
                            } catch (TipoContaInvalidoException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            TipoConta tipoContaTransferencia = TipoConta.getTipoContaPorIndice(idTipoConta);
                            try {
                                contaCorrenteLogada.transferencia(valorTransferencia, cpfDestinatario, tipoContaTransferencia);
                            } catch (ValorNegativoException | SaldoInsuficienteException | CpfInexistenteException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
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
                                leitor.close();
                                System.exit(1);
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 6 -> {
                            System.out.print("Insira o valor a ser segurado: ");
                            double valorTotalSegurado = 0;
                            try {
                                valorTotalSegurado = leitor.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.print("Insira em quantos meses deseja pagar o seguro: ");
                            int qtdMeses;
                            try {
                                qtdMeses = leitor.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            List<String> segurados = new ArrayList<>();
                            while (true) {
                                System.out.print("""
                                        Escolha uma das opções abaixo:
                                        1 - Adicionar novo segurado
                                        0 - Finalizar
                                        Opção:\s""");
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
                                    try {
                                        ValidadorEntrada.validaCpf(segurado);
                                    } catch (CpfInvalidoException e) {
                                        System.out.println("\n" + e.getMessage());
                                        continue;
                                    }
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
                                leitor.close();
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
                                    leitor.close();
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                                Set<Agencia> agencias = new HashSet<>();
                                while (true) {
                                    System.out.print("""
                                            Escolha uma das opções abaixo:\s
                                            1 - Selecionar nova agencia
                                            0 - Finalizar
                                            Opção:\s""");
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
                                        int idAgencia;
                                        idAgencia = leitor.nextInt();
                                        try {
                                            ValidadorEntrada.validaIdAgencia(idAgencia);
                                        } catch (IdAgenciaInvalidoException e) {
                                            System.out.println("\n" + e.getMessage());
                                            continue;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nErro! Insira apenas números!\n");
                                            continue;
                                        } finally {
                                            leitor.nextLine();
                                        }
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
                                        leitor.close();
                                        System.exit(1);
                                    }
                                } else if (usuarioExt instanceof Presidente) {
                                    try {
                                        presidenteLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        leitor.close();
                                        System.exit(1);
                                    }
                                }
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 8 -> {
                            if (usuarioExt instanceof Diretor) {
                                try {
                                    diretorLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Presidente) {
                                try {
                                    presidenteLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 9 -> {
                            try {
                                presidenteLogado.geraRelatorioCapitalBanco();
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
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
                    System.out.println("""

                            Escolha uma das opções abaixo:
                            1 - Saque
                            2 - Depósito
                            3 - Transferência
                            4 - Exibir Saldo
                            5 - Gerar relatório de simulação de rendimento""");

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
                        leitor.close();
                        System.exit(0);
                    }

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
                            double valorSaque = 0;
                            try {
                                valorSaque = leitor.nextDouble();
                                contaPoupancaLogada.saque(valorSaque);
                            } catch (ValorNegativoException | SaldoInsuficienteException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            } catch (InputMismatchException e) {
                                System.out.println("Erro! Insira apenas números!\n");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorSaque) + " foi sacado com sucesso!");
                        }
                        case 2 -> {
                            System.out.print("Insira o valor a ser depositado: ");
                            double valorDeposito = 0;
                            try {
                                valorDeposito = leitor.nextDouble();
                                contaPoupancaLogada.deposito(valorDeposito);
                            } catch (ValorNegativoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            } catch (InputMismatchException e) {
                                System.out.println("Erro! Insira apenas números!\n");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorDeposito) + " foi depositado com sucesso!");
                        }
                        case 3 -> {
                            System.out.print("Insira o valor a ser transferido: ");
                            double valorTransferencia = 0;
                            try {
                                valorTransferencia = leitor.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.print("Insira o cpf do destinatário: ");
                            String cpfDestinatario = leitor.nextLine();
                            try {
                                ValidadorEntrada.validaCpf(cpfDestinatario);
                            } catch (CpfInvalidoException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            }
                            System.out.print("""
                                    Escolha o tipo de conta:
                                    1 - Conta Corrente
                                    2 - Conta Poupança
                                    Opção:\s""");

                            int idTipoConta = 0;
                            try {
                                idTipoConta = leitor.nextInt();
                                ValidadorEntrada.validaTipoConta(idTipoConta);;
                            } catch (TipoContaInvalidoException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            TipoConta tipoContaTransferencia = TipoConta.getTipoContaPorIndice(idTipoConta);
                            try {
                                contaPoupancaLogada.transferencia(valorTransferencia, cpfDestinatario, tipoContaTransferencia);
                            } catch (ValorNegativoException | SaldoInsuficienteException | CpfInexistenteException e) {
                                System.out.println("\n" + e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            }
                            System.out.println("\nO valor de R$ " + String.format("%.2f", valorTransferencia) + " foi transferido com sucesso!");
                        }
                        case 4 -> contaPoupancaLogada.exibirSaldo();
                        case 5 -> {
                            System.out.print("Insira o valor a ser simulado: ");
                            double valorSimulacao = 0;
                            try {
                                valorSimulacao = leitor.nextDouble();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            System.out.print("Insira a quantidade de dias para serem simulados: ");
                            int qtdDiasSimulacao = 0;
                            try {
                                qtdDiasSimulacao = leitor.nextInt();
                            } catch (InputMismatchException e) {
                                System.out.println("\nErro! Insira apenas números!");
                                continue;
                            } finally {
                                leitor.nextLine();
                            }
                            try {
                                contaPoupancaLogada.geraSimulacaoRendimento(valorSimulacao, qtdDiasSimulacao);
                            } catch (ValorNegativoException | ValorInvalidoException e) {
                                System.out.println(e.getMessage());
                                continue;
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 6 -> {
                            if (usuarioExt instanceof Gerente) {
                                try {
                                    gerenteLogado.geraRelatorioNumContas();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Diretor || usuarioExt instanceof Presidente) {
                                Set<Agencia> agencias = new HashSet<>();
                                while (true) {
                                    System.out.print("""
                                            Escolha uma das opções abaixo:\s
                                            1 - Selecionar nova agencia
                                            0 - Finalizar
                                            Opção:\s""");
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
                                        int idAgencia;
                                        idAgencia = leitor.nextInt();
                                        try {
                                            ValidadorEntrada.validaIdAgencia(idAgencia);
                                        } catch (IdAgenciaInvalidoException e) {
                                            System.out.println("\n" + e.getMessage());
                                            continue;
                                        } catch (InputMismatchException e) {
                                            System.out.println("\nErro! Insira apenas números!\n");
                                            continue;
                                        } finally {
                                            leitor.nextLine();
                                        }
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
                                        leitor.close();
                                        System.exit(1);
                                    }
                                } else if (usuarioExt instanceof Presidente) {
                                    try {
                                        presidenteLogado.geraRelatorioNumContas(agencias.toArray(new Agencia[agencias.size()]));
                                    } catch (IOException e) {
                                        System.out.println("Erro de escrita de arquivos");
                                        leitor.close();
                                        System.exit(1);
                                    }
                                }
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 7 -> {
                            if (usuarioExt instanceof Diretor) {
                                try {
                                    diretorLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                            } else if (usuarioExt instanceof Presidente) {
                                try {
                                    presidenteLogado.geraRelatorioClientesBanco();
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                        case 8 -> {
                            try {
                                presidenteLogado.geraRelatorioCapitalBanco();
                            } catch (IOException e) {
                                System.out.println("Erro de escrita de arquivos");
                                leitor.close();
                                System.exit(1);
                            }
                            System.out.println("\nRelatório gerado com sucesso!");
                        }
                    }
                }
            }
        }

    }

    /** Método para realizar o cadastro de uma nova conta-corrente ou conta-poupança no sistema
     */
    public static void realizaCadastroConta() {
        Scanner leitor = new Scanner(System.in);

        escolhaTipoConta: while(true) {
            System.out.print("""

                    Escolha o tipo de conta que deseja cadastrar:
                    1 - Conta corrente
                    2 - Conta poupança
                    0 - Voltar ao menu anterior
                    Opção:\s""");

            int opcaoCadastro;
            try {
                opcaoCadastro = leitor.nextInt();
            } catch (InputMismatchException e) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            } finally {
                leitor.nextLine();
            }

            if (opcaoCadastro < 0 || opcaoCadastro > 2) {
                System.out.println("\nValor inserido inválido. Retornando ao início do cadastro...\n");
                continue;
            } else if (opcaoCadastro == 0){
                System.out.println();
                return;
            }

            String cpfCadastro;
            int numTentativasCpf = 0;
            while (true) {
                System.out.print("Insira seu CPF: ");
                cpfCadastro = leitor.nextLine();
                try {
                    ValidadorEntrada.validaCpf(cpfCadastro);
                } catch (CpfInvalidoException e) {
                    numTentativasCpf++;
                    System.out.println(e.getMessage());
                    if(numTentativasCpf >= 3) {
                        System.out.println("Retornando ao menu anterior...\n");
                        continue escolhaTipoConta;
                    }
                    continue;
                }
                break;
            }
            int idAgenciaCadastro;
            int numTentativasIdAgencia = 0;
            while (true) {
                System.out.print("Insira o número da agência: ");
                try {
                    idAgenciaCadastro = leitor.nextInt();
                    ValidadorEntrada.validaIdAgencia(idAgenciaCadastro);
                } catch (IdAgenciaInvalidoException e) {
                    numTentativasIdAgencia++;
                    System.out.println(e.getMessage());
                    if(numTentativasIdAgencia >= 3) {
                        System.out.println("Retornando ao menu anterior...\n");
                        continue escolhaTipoConta;
                    }
                    continue;
                } catch (InputMismatchException e) {
                    numTentativasIdAgencia++;
                    System.out.println("Erro. Insira apenas números!\n");
                    if(numTentativasIdAgencia >= 3) {
                        System.out.println("Retornando ao menu anterior...\n");
                        continue escolhaTipoConta;
                    }
                    continue;
                } finally {
                    leitor.nextLine();
                }
                break;
            }
            Agencia agenciaCadastro = Agencia.getAgenciaPorId(idAgenciaCadastro);

            String senhaCadastro;
            try {
                UsuarioRepositorio.getUsuario(cpfCadastro);
                System.out.println("Usuário encontrado no sistema!");

                int numTentativasSenha = 0;
                while (true) {
                    System.out.print("Insira sua senha: ");
                    senhaCadastro = leitor.nextLine();
                    try {
                        ValidadorEntrada.validaSenha(senhaCadastro);
                    } catch (SenhaInvalidaException e) {
                        numTentativasSenha++;
                        System.out.println(e.getMessage());
                        if(numTentativasSenha >= 3) {
                            System.out.println("Retornando ao menu anterior...\n");
                            continue escolhaTipoConta;
                        }
                        continue;
                    }
                    break;
                }
                UsuarioRepositorio.getUsuario(cpfCadastro).logar(senhaCadastro);
            } catch (CpfInexistenteException e) {
                if(!realizaCadastroCliente(cpfCadastro)) {
                    continue;
                }
            } catch (SenhaIncorretaException e) {
                System.out.println("\n" + e.getMessage());
                continue;
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
                    leitor.close();
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
                    leitor.close();
                    System.exit(1);
                }
                System.out.println("Conta poupança cadastrada com sucesso!");
            }
            System.out.println();
            return;
        }
    }

    /** Método para realizar o cadastro de um novo cliente no sistema.
     *
     * @param cpfCadastro CPF do usuário a ser cadastrado
     * @return true se o processo for completado com sucesso ou false caso ocorra um erro no processo de cadastro
     */
    public static boolean realizaCadastroCliente(String cpfCadastro) {
        Scanner leitor = new Scanner(System.in);

        escolhaTipoUsuario: while (true) {
            System.out.print("""

                    Escolha o tipo de usuário que deseja cadastrar:
                    1 - Cliente
                    2 - Gerente
                    3 - Diretor
                    4 - Presidente
                    0 - Voltar ao menu anterior
                    Opção:\s""");

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

            String nomeCadastro;
            int numTentativasNome = 0;
            while (true) {
                System.out.print("Insira seu nome completo: ");
                nomeCadastro = leitor.nextLine();
                try {
                    ValidadorEntrada.validaNome(nomeCadastro);
                } catch (NomeInvalidoException e) {
                    numTentativasNome++;
                    System.out.println(e.getMessage());
                    if(numTentativasNome >= 3) {
                        System.out.println("Retornando ao menu anterior...\n");
                        continue escolhaTipoUsuario;
                    }
                    continue;
                }
                break;
            }


            String senhaCadastro;
            int numTentativasSenha = 0;
            while (true) {
                System.out.print("Insira sua senha: ");
                senhaCadastro = leitor.nextLine();
                try {
                    ValidadorEntrada.validaSenha(senhaCadastro);
                } catch (SenhaInvalidaException e) {
                    numTentativasSenha++;
                    System.out.println(e.getMessage());
                    if(numTentativasSenha >= 3) {
                        System.out.println("Retornando ao menu anterior...\n");
                        continue escolhaTipoUsuario;
                    }
                    continue;
                }
                break;
            }

            if (opcaoCadastro == 1) {
                Cliente clienteCadastro = new Cliente(nomeCadastro, cpfCadastro, senhaCadastro);
                try {
                    UsuarioRepositorio.adicionaUsuario(clienteCadastro);
                } catch (UsuarioExistenteException e) {
                    System.out.println(e.getMessage());
                    continue;
                } catch (IOException e) {
                    System.out.println("Erro de escrita de arquivos");
                    leitor.close();
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
                            case 2 -> {
                                int idAgenciaCadastro;
                                int numTentativasIdAgencia = 0;
                                while (true) {
                                    System.out.print("Insira o número da agência do gerente: ");
                                    try {
                                        idAgenciaCadastro = leitor.nextInt();
                                        ValidadorEntrada.validaIdAgencia(idAgenciaCadastro);
                                    } catch (IdAgenciaInvalidoException e) {
                                        numTentativasIdAgencia++;
                                        System.out.println(e.getMessage());
                                        if(numTentativasIdAgencia >= 3) {
                                            System.out.println("Retornando ao menu anterior...\n");
                                            continue escolhaTipoUsuario;
                                        }
                                        continue;
                                    } catch (InputMismatchException e) {
                                        numTentativasIdAgencia++;
                                        System.out.println("Erro. Insira apenas números!\n");
                                        if(numTentativasIdAgencia >= 3) {
                                            System.out.println("Retornando ao menu anterior...\n");
                                            continue escolhaTipoUsuario;
                                        }
                                        continue;
                                    } finally {
                                        leitor.nextLine();
                                    }
                                    break;
                                }
                                Agencia agenciaCadastro = Agencia.getAgenciaPorId(idAgenciaCadastro);
                                Gerente gerenteCadastro = new Gerente(nomeCadastro, cpfCadastro, senhaCadastro, agenciaCadastro);
                                try {
                                    UsuarioRepositorio.adicionaUsuario(gerenteCadastro);
                                } catch (UsuarioExistenteException e) {
                                    System.out.println(e.getMessage());
                                    continue escolhaTipoUsuario;
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                                System.out.println("\nGerente cadastrado com sucesso!");
                            }
                            case 3 -> {
                                Diretor diretorCadastro = new Diretor(nomeCadastro, cpfCadastro, senhaCadastro);
                                try {
                                    UsuarioRepositorio.adicionaUsuario(diretorCadastro);
                                } catch (UsuarioExistenteException e) {
                                    System.out.println(e.getMessage());
                                    continue escolhaTipoUsuario;
                                } catch (IOException e) {
                                    System.out.println("Erro de escrita de arquivos");
                                    leitor.close();
                                    System.exit(1);
                                }
                                System.out.println("\nDiretor cadastrado com sucesso!");
                            }
                            case 4 -> {
                                if (UsuarioRepositorio.isPresidenteCadastrado()) {
                                    System.out.println("\nPresidente já cadastrado no sistema!");
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
                                    leitor.close();
                                    System.exit(1);
                                }
                                System.out.println("\nPresidente cadastro com sucesso!");
                            }
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
