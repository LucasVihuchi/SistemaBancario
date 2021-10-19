package com.grupo4.repositorios;

import com.grupo4.contas.ContaPoupanca;
import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Classe que possui repositório para objetos do tipo ContaPoupanca, onde serão contidos, atributos e métodos para manipulação das contas-poupança.
 */
public class ContaPoupancaRepositorio {
    private static HashMap<String, ContaPoupanca> listaDeContasPoupanca = new HashMap<>();

    /** Método para adicionar uma nova ContaPoupanca à lista de contas-poupança no banco tanto em tempo de execução quanto no arquivo de texto de contas-poupança.
     *
     * @param contaPoupancaExt objeto do tipo ContaPoupanca que será adicionado no sistema
     * @throws ContaExistenteException se uma ContaPoupanca associada ao CPF do usuário já existir
     * @throws IOException se ocorrer um erro de escrita no arquivo de contas-poupanca
     */
    public static void adicionaContaPoupanca(ContaPoupanca contaPoupancaExt) throws ContaExistenteException, IOException {
        if(listaDeContasPoupanca.containsKey(contaPoupancaExt.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasPoupanca.put(contaPoupancaExt.getCpfTitular(), contaPoupancaExt);

        File pathContaPoupancaBD = new File ("C:\\RepositorioBanco\\");
        File contaPoupancaBD = new File(pathContaPoupancaBD.getAbsolutePath() + "\\contaPoupancaRepositorio.txt");

        if (!pathContaPoupancaBD.exists()) {
            pathContaPoupancaBD.mkdirs();
        }

        if(!contaPoupancaBD.exists()) {
            contaPoupancaBD.createNewFile();
        }

        try (FileWriter contaPoupancaDBWriter = new FileWriter(contaPoupancaBD, true);
             BufferedWriter contaPoupancaDBWriterBuff = new BufferedWriter(contaPoupancaDBWriter)) {

            contaPoupancaDBWriterBuff.append(contaPoupancaExt.getCpfTitular() + "¨¨" +
                    contaPoupancaExt.getSaldo() + "¨¨" +
                    contaPoupancaExt.getAgencia().getIdAgencia() + "¨¨" +
                    contaPoupancaExt.getAniversarioConta());

            contaPoupancaDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    /** Método para remover uma ContaPoupanca da lista de contas-poupança no banco. Note que essa função não altera o arquivo de contas-poupança e que não está em uso atualmente.
     *
     * @param cpfExt cpf do usuário que possui a ContaPoupanca que deve ser deletada
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     * @deprecated
     */
    public static void removeContaPoupanca(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasPoupanca.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeContasPoupanca.remove(cpfExt);
    }

    /** Método para retornar uma ContaPoupanca específica a partir de um cpf informado.
     *
     * @param cpfExt cpf do usuário que possui a ContaPoupanca que deve ser retornada
     * @return Um objeto do tipo ContaPoupanca que corresponde ao cpf fornecido
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     */
    public static ContaPoupanca getContaPoupanca(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasPoupanca.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeContasPoupanca.get(cpfExt);
    }

    /** Método para retornar um List com todos os objetos ContaPoupanca registrados nas agências selecionadas.
     *
     * @param agencias vetor de agências que devem ser incluídas no relatório
     * @return List com todos os objetos ContaPoupanca registrados nas agências selecionadas
     */
    public static List<ContaPoupanca> getContasPoupancaPorAgencia(Agencia... agencias) {

        // Criando a lista que vou retornar pro meu gerente
        List<ContaPoupanca> listaFiltrada = new ArrayList<>();

        // Percorrendo cada uma das contas do banco
        for (ContaPoupanca contaPoupanca : listaDeContasPoupanca.values()) {
            // Pra cada conta, vou percorrer todas as agências que eu pedi na chamada da função
            for (Agencia ag : agencias) {
                // Verificar se qualquer uma dela é igual a agência da conta poupança atual em que estou
                if (ag.equals(contaPoupanca.getAgencia())) {
                    listaFiltrada.add(contaPoupanca);
                }
            }
        }
        return listaFiltrada;
    }

    /** Método para retornar um List com todos os objetos ContaPoupanca registrados no banco.
     *
     * @return List com todos os objetos ContaPoupanca registrados no banco
     */
    public static List<ContaPoupanca> getContasPoupanca() {
        return listaDeContasPoupanca.values().stream().toList();
    }

    /** Método para carregar os objetos ContaPoupanca guardados no arquivo de contas-poupança no início da aplicação.
     *
     * @throws IOException se ocorrer um erro de leitura no arquivo de contas-poupança
     */
    public static void ContaPoupancaLoader () throws IOException {
        File pathContaPoupancaBD = new File ("C:\\RepositorioBanco\\");
        File contaPoupancaBD = new File(pathContaPoupancaBD.getAbsolutePath() + "\\contaPoupancaRepositorio.txt");

        if (!pathContaPoupancaBD.exists()) {
            pathContaPoupancaBD.mkdirs();
        }

        if(!contaPoupancaBD.exists()) {
            contaPoupancaBD.createNewFile();
        }


        try (FileReader contaPoupancaBDReader = new FileReader(contaPoupancaBD);
             BufferedReader contaPoupancaBDReaderBuff = new BufferedReader(contaPoupancaBDReader)) {

            String linhaLida;
            while ((linhaLida = contaPoupancaBDReaderBuff.readLine()) != null) {
                String[] itensTemp = linhaLida.split("¨¨");

                String cpfTemp = itensTemp[0];
                double saldoTemp = Double.parseDouble(itensTemp[1]);
                int idAgencia = Integer.parseInt(itensTemp[2]);
                Agencia agenciaTemp = Agencia.getAgenciaPorId(idAgencia);
                int aniversarioContaTemp = Integer.parseInt(itensTemp[3]);

                ContaPoupanca contaPoupancaTemp = new ContaPoupanca(cpfTemp, agenciaTemp, saldoTemp, aniversarioContaTemp);
                listaDeContasPoupanca.put(contaPoupancaTemp.getCpfTitular(), contaPoupancaTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
