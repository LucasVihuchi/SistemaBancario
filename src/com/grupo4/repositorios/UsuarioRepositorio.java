package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.UsuarioExistenteException;
import com.grupo4.usuarios.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class UsuarioRepositorio {
    // CPF (key ou chave) / Usuario (value ou valor)
    private static HashMap<String, Usuario> listaUsuarios = new HashMap<>();

    public static void adicionaUsuario(Usuario usuarioExt) throws UsuarioExistenteException, IOException {
        if (listaUsuarios.containsKey(usuarioExt.getCpf())) {
            throw new UsuarioExistenteException();
        }
        listaUsuarios.put(usuarioExt.getCpf(), usuarioExt);

        // Vou manipular esse caminho ali
        File usuariosBD = new File("C:\\RepositorioBanco\\usuarioRepositorio.txt");

        if (!usuariosBD.exists()) { // Se não existir ainda
            usuariosBD.mkdirs(); // Crio as pastas pra armazenar o arquivo
            usuariosBD.createNewFile(); // Crio o arquivo que vai armazenar os meus dados
        }

        try (FileWriter usuarioDBWriter = new FileWriter(usuariosBD, true);
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

    public static Usuario getUsuario(String cpfExt) throws CpfInexistenteException {
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaUsuarios.get(cpfExt);
    }
}
