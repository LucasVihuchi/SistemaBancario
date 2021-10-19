package com.grupo4.validadores;

import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.*;

/** Classe que contém métodos de validação de dados manipulados pelo sistema.
 */
public class ValidadorEntrada {

    /** Método para validar um CPF fornecido.
     *
     * @param cpf cpf a ser validado
     * @throws CpfInvalidoException se o cpf fornecido for inválido
     */
    public static void validaCpf(String cpf) throws CpfInvalidoException {
        if (cpf.length() != 11) {
            throw new CpfInvalidoException("CPF deve conter 11 dígitos!\n");
        }

        int[] digitos = new int[11];
        boolean isCaracteresIguais = true;
        for (int i = 0; i < 11; i++) {
            try {
                digitos[i] = Integer.parseInt(String.valueOf(cpf.charAt(i)));
                if (i > 0) {
                    if (digitos[i] != digitos[i-1]) {
                        isCaracteresIguais = false;
                    }
                }
            } catch (NumberFormatException e) {
                throw new CpfInvalidoException("CPF deve conter apenas números!\n");
            }
        }
        if(isCaracteresIguais) {
            throw new CpfInvalidoException();
        }

        double somaVerificacaoPrimeiroDigito = 0;
        for (int i = 0; i < 9; i++) {
            somaVerificacaoPrimeiroDigito += (digitos[i] * (10 - i));
        }
        somaVerificacaoPrimeiroDigito *= 10.0;
        if(digitos[9] != (somaVerificacaoPrimeiroDigito % 11) && !((digitos[9] == 0) && ((somaVerificacaoPrimeiroDigito % 11) == 10))) {
            throw new CpfInvalidoException();
        }

        double somaVerificacaoSegundoDigito = 0;
        for (int i = 0; i < 10; i++) {
            somaVerificacaoSegundoDigito += (digitos[i] * (11 - i));
        }
        somaVerificacaoSegundoDigito *= 10.0;
        if(digitos[10] != (somaVerificacaoSegundoDigito % 11) && !((digitos[10] == 0) && ((somaVerificacaoSegundoDigito % 11) == 10))) {
            throw new CpfInvalidoException();
        }
    }

    /** Método para validar um nome fornecido.
     *
     * @param nome nome a ser validado
     * @throws NomeInvalidoException se o nome fornecido for inválido
     */
    public static void validaNome(String nome) throws NomeInvalidoException {
        if (nome.length() < 3){
            throw new NomeInvalidoException("Nome deve conter pelo menos 3 letras!\n");
        }
        else if (!(nome.contains(" "))){
            throw new NomeInvalidoException("Insira seu nome e sobrenome!\n");
        } else if (nome.contains("¨¨")){
            throw new NomeInvalidoException();
        }
    }

    /** Método para validar uma senha fornecida.
     *
     * @param senha senha a ser validada
     * @throws SenhaInvalidaException se a senha fornecida for inválida
     */
    public static void validaSenha(String senha) throws SenhaInvalidaException {
        if(senha.length() < 6) {
            throw new SenhaInvalidaException("Senha deve conter no mínimo 6 caracteres!\n");
        } else if (senha.contains(" ")) {
            throw new SenhaInvalidaException("Senha não pode conter espaços!\n");
        }
    }

    /** Método para validar um número de agência fornecido.
     *
     * @param idAgencia número de agência a ser validado
     * @throws IdAgenciaInvalidoException se o número da agência fornecido for inválido
     */
    public static void validaIdAgencia(int idAgencia) throws IdAgenciaInvalidoException {
        if (idAgencia < 1 || idAgencia > Agencia.values().length) {
            throw new IdAgenciaInvalidoException();
        }
    }

    /** Método para valida um tipo de conta fornecido.
     *
     * @param tipoConta tipo de conta a ser validado.
     * @throws TipoContaInvalidoException se o tipo de conta fornecido for inválido
     */
    public static void validaTipoConta(int tipoConta) throws TipoContaInvalidoException {
        if(tipoConta < 1 || tipoConta > 2) {
            throw new TipoContaInvalidoException();
        }
    }
}
