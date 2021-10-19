package com.grupo4.repositorios;

import com.grupo4.contas.ContaCorrente;
import com.grupo4.enums.Agencia;
import com.grupo4.exceptions.ContaExistenteException;
import com.grupo4.exceptions.CpfInexistenteException;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Classe que possui repositório para objetos do tipo ContaCorrente, onde serão contidos, atributos e métodos para manipulação das contas-corrente.
 */
public class ContaCorrenteRepositorio {
    private static HashMap<String, ContaCorrente> listaDeContasCorrente = new HashMap<>();

    /** Método para adicionar uma nova ContaCorrente à lista de contas-corrente no banco tanto em tempo de execução quanto no arquivo de texto de contas-corrente.
     *
     * @param contaCorrenteExt objeto do tipo ContaCorrente que será adicionado no sistema
     * @throws ContaExistenteException se uma ContaCorrente associada ao CPF do usuário já existir
     * @throws IOException se ocorrer um erro de escrita no arquivo de contas-corrente
     */
    public static void adicionaContaCorrente(ContaCorrente contaCorrenteExt) throws ContaExistenteException, IOException {
        if(listaDeContasCorrente.containsKey(contaCorrenteExt.getCpfTitular())){
            throw new ContaExistenteException();
        }
        listaDeContasCorrente.put(contaCorrenteExt.getCpfTitular(), contaCorrenteExt);

        File pathContaCorrenteBD = new File ("C:\\RepositorioBanco\\");
        File contaCorrenteBD = new File(pathContaCorrenteBD.getAbsolutePath() +  "\\contaCorrenteRepositorio.txt");

        if(!pathContaCorrenteBD.exists()) {
            pathContaCorrenteBD.mkdirs();
        }

        if(!contaCorrenteBD.exists()) {
            contaCorrenteBD.createNewFile();
        }

        try (FileWriter contaCorrenteDBWriter = new FileWriter(contaCorrenteBD, true);
             BufferedWriter contaCorrenteDBWriterBuff = new BufferedWriter(contaCorrenteDBWriter)) {

            contaCorrenteDBWriterBuff.append(contaCorrenteExt.getCpfTitular() + "¨¨" +
                    contaCorrenteExt.getSaldo() + "¨¨" +
                    contaCorrenteExt.getAgencia().getIdAgencia());

            contaCorrenteDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    /** Método para remover uma ContaCorrente da lista de contas-corrente no banco. Note que essa função não altera o arquivo de contas-corrente e que não está em uso atualmente.
     *
     * @param cpfExt cpf do usuário que possui a ContaCorrente que deve ser deletada
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     * @deprecated
     */
    public static void removeContaCorrente(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasCorrente.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeContasCorrente.remove(cpfExt);
    }

    /** Método para retornar uma ContaCorrente específica a partir de um cpf informado.
     *
     * @param cpfExt cpf do usuário que possui a ContaCorrente que deve ser retornada
     * @return Um objeto do tipo ContaCorrente que corresponde ao cpf fornecido
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     */
    public static ContaCorrente getContaCorrente(String cpfExt) throws CpfInexistenteException {
        if (!listaDeContasCorrente.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeContasCorrente.get(cpfExt);
    }

    /** Método para retornar um List com todos os objetos ContaCorrente registrados nas agências selecionadas.
     *
     * @param agencias vetor de agências que devem ser incluídas no relatório
     * @return List com todos os objetos ContaCorrente registrados nas agências selecionadas
     */
    public static List<ContaCorrente> getContasCorrentePorAgencia(Agencia... agencias) {

        // Criando a lista que vou retornar pro meu gerente
        List<ContaCorrente> listaFiltrada = new ArrayList<>();

        // Percorrendo cada uma das contas do banco
        for (ContaCorrente contaCorrente : listaDeContasCorrente.values()) {
            // Pra cada conta, vou percorrer todas as agências que eu pedi na chamada da função
            for (Agencia ag : agencias) {
                // Verificar se qualquer uma dela é igual a agência da conta corrente atual em que estou
                if (ag.equals(contaCorrente.getAgencia())) {
                    listaFiltrada.add(contaCorrente);
                }
            }
        }
        return listaFiltrada;
    }

    /** Método para retornar um List com todos os objetos ContaCorrente registrados no banco.
     *
     * @return List com todos os objetos ContaCorrente registrados no banco
     */
    public static List<ContaCorrente> getContasCorrente() {
        return listaDeContasCorrente.values().stream().toList();
    }

    /** Método para carregar os objetos ContaCorrente guardados no arquivo de contas-corrente no início da aplicação.
     *
     * @throws IOException se ocorrer um erro de leitura no arquivo de contas-corrente
     */
    public static void ContaCorrenteLoader () throws IOException {
        File pathContaCorrenteBD = new File ("C:\\RepositorioBanco\\");
        File contaCorrenteBD = new File(pathContaCorrenteBD.getAbsolutePath() +  "\\contaCorrenteRepositorio.txt");

        if(!pathContaCorrenteBD.exists()) {
            pathContaCorrenteBD.mkdirs();
        }

        if(!contaCorrenteBD.exists()) {
            contaCorrenteBD.createNewFile();
        }

        try (FileReader contaCorrenteBDReader = new FileReader(contaCorrenteBD);
             BufferedReader contaCorrenteBDReaderBuff = new BufferedReader(contaCorrenteBDReader)) {

            String linhaLida;
            while ((linhaLida = contaCorrenteBDReaderBuff.readLine()) != null) {
                String[] itensTemp = linhaLida.split("¨¨");

                String cpfTemp = itensTemp[0];
                double saldoTemp = Double.parseDouble(itensTemp[1]);
                int idAgencia = Integer.parseInt(itensTemp[2]);
                Agencia agenciaTemp = Agencia.getAgenciaPorId(idAgencia);

                ContaCorrente contaCorrenteTemp = new ContaCorrente(cpfTemp, agenciaTemp, saldoTemp);
                listaDeContasCorrente.put(contaCorrenteTemp.getCpfTitular(), contaCorrenteTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
