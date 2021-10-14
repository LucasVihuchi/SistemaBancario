package com.grupo4.repositorios;

import com.grupo4.contas.ContaPoupanca;
import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.UsuarioExistenteException;
import com.grupo4.usuarios.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;

public class UsuarioRepositorio {
    private static HashMap<String, Usuario> listaUsuarios = new HashMap<>();

    public static void adicionaUsuario(Usuario usuarioExt) throws UsuarioExistenteException, IOException {
        if (listaUsuarios.containsKey(usuarioExt.getCpf())) {
            throw new UsuarioExistenteException();
        }
        listaUsuarios.put(usuarioExt.getCpf(), usuarioExt);

        File pathUsuarioBD = new File ("C:\\RepositorioBanco\\");
        File usuarioBD = new File(pathUsuarioBD.getAbsolutePath() + "\\usuarioRepositorio.txt");

        if(!pathUsuarioBD.exists()){
            pathUsuarioBD.mkdirs();
        }

        if(!usuarioBD.exists()) {
            usuarioBD.createNewFile();
        }

        try (FileWriter usuarioDBWriter = new FileWriter(usuarioBD, true);
             BufferedWriter usuarioDBWriterBuff = new BufferedWriter(usuarioDBWriter)){

            usuarioDBWriterBuff.append(usuarioExt.getNome() + "¨¨" + usuarioExt.getCpf() + "¨¨" + usuarioExt.getSenha() + "¨¨");
            if(usuarioExt instanceof Cliente) {
                usuarioDBWriterBuff.append("c");
            }
            else if (usuarioExt instanceof Gerente) {
                usuarioDBWriterBuff.append("g¨¨" + ((Gerente) usuarioExt).getIdAgencia().getIdAgencia());
            }
            else if (usuarioExt instanceof Diretor) {
                usuarioDBWriterBuff.append("d");
            }
            else if (usuarioExt instanceof Presidente) {
                usuarioDBWriterBuff.append("p");
            }
            usuarioDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    public static void removeUsuario(String cpfExt) throws CpfInexistenteException {
        // ! - not
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaUsuarios.remove(cpfExt);
        // TODO Implementar remoção de cadastro do arquivo de texto
    }

    public static boolean isUsuarioCadastrado(String cpfExt) {
        return listaUsuarios.containsKey(cpfExt);
    }

    public static Usuario getUsuario(String cpfExt) throws CpfInexistenteException {
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaUsuarios.get(cpfExt);
    }

    public static List<Usuario> getUsuarios() {
        return listaUsuarios.values().stream().toList();
    }

    public static void usuarioLoader () throws IOException {
        File pathUsuarioBD = new File ("C:\\RepositorioBanco\\");
        File usuarioBD = new File(pathUsuarioBD.getAbsolutePath() + "\\usuarioRepositorio.txt");

        if(!pathUsuarioBD.exists()){
            pathUsuarioBD.mkdirs();
        }

        if(!usuarioBD.exists()) {
            usuarioBD.createNewFile();
        }

        try (FileReader usuarioBDReader = new FileReader(usuarioBD);
             BufferedReader usuarioBDReaderBuff = new BufferedReader(usuarioBDReader)) {

            String linhaLida;
            while ((linhaLida = usuarioBDReaderBuff.readLine()) != null) {
                String[] itensTemp = linhaLida.split("¨¨");

                String nomeTemp = itensTemp[0];
                String cpfTemp = itensTemp[1];
                String senhaTemp = itensTemp[2];
                String tipoUsuario = itensTemp[3];
                Usuario usuarioTemp = null;
                if (tipoUsuario.equals("c")) {
                    usuarioTemp = new Cliente(nomeTemp, cpfTemp, senhaTemp);
                }
                else if (tipoUsuario.equals("g")) {
                    int idAgenciaTemp = Integer.parseInt(itensTemp[4]);
                    Agencia agenciaTemp = Agencia.getAgenciaPorId(idAgenciaTemp);
                    usuarioTemp = new Gerente(nomeTemp, cpfTemp, senhaTemp, agenciaTemp);
                }
                else if (tipoUsuario.equals("d")) {
                    usuarioTemp = new Diretor(nomeTemp, cpfTemp, senhaTemp);
                }
                else if (tipoUsuario.equals("p")) {
                    usuarioTemp = new Presidente(nomeTemp, cpfTemp, senhaTemp);
                }
                listaUsuarios.put(usuarioTemp.getCpf(), usuarioTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
