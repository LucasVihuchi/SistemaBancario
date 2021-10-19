package com.grupo4.repositorios;

import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.UsuarioExistenteException;
import com.grupo4.usuarios.*;

import java.io.*;
import java.util.HashMap;
import java.util.List;

/** Classe que possui repositório para objetos do tipo Usuario, onde serão contidos, atributos e métodos para manipulação dos usuários.
 */
public class UsuarioRepositorio {
    private static HashMap<String, Usuario> listaUsuarios = new HashMap<>();

    /** Método para adicionar um novo Usuario à lista de usuários no banco tanto em tempo de execução quanto no arquivo de texto de usuários.
     *
     * @param usuarioExt objeto do tipo Usuario que será adicionado no sistema
     * @throws UsuarioExistenteException se um Usuario já existir
     * @throws IOException se ocorrer um erro de escrita no arquivo de usuários
     */
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
                usuarioDBWriterBuff.append("g¨¨" + ((Gerente) usuarioExt).getAgencia().getIdAgencia());
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

    /** Método para remover um Usuario da lista de usuário no banco. Note que essa função não altera o arquivo de usuários e que não está em uso atualmente.
     *
     * @param cpfExt cpf do Usuario que deve ser deletado
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     * @deprecated
     */
    public static void removeUsuario(String cpfExt) throws CpfInexistenteException {
        // ! - not
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaUsuarios.remove(cpfExt);
        // TODO Implementar remoção de cadastro do arquivo de texto
    }

    /** Método para verificar se o presidente já está cadastrado no sistema.
     *
     * @return true se encontrar um presidente cadastrado e false caso não encontre
     */
    public static boolean isPresidenteCadastrado() {
        for (Usuario usuario : listaUsuarios.values()) {
            if (usuario instanceof Presidente) {
                return true;
            }
        }
        return false;
    }

    /** Método para retornar um Usuario específico a partir de um cpf informado.
     *
     * @param cpfExt cpf do Usuario que deve ser retornado
     * @return Um objeto do tipo Usuario que corresponde ao cpf fornecido
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     */
    public static Usuario getUsuario(String cpfExt) throws CpfInexistenteException {
        if (!listaUsuarios.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaUsuarios.get(cpfExt);
    }

    /** Método para retornar um List com todos os objetos Usuario registrados no banco.
     *
     * @return List com todos os objetos Usuario registrados no banco
     */
    public static List<Usuario> getUsuarios() {
        return listaUsuarios.values().stream().toList();
    }

    /** Método para carregar os objetos Usuario guardados no arquivo de usuários no início da aplicação.
     *
     * @throws IOException se ocorrer um erro de leitura no arquivo de usuários
     */
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
