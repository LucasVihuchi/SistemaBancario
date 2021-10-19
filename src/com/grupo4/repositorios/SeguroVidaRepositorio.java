package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.segurovida.SeguroVida;

import java.io.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/** Classe que possui repositório para objetos do tipo SeguroVida, onde serão contidos, atributos e métodos para manipulação dos seguros de vida.
 */
public class SeguroVidaRepositorio {
    private static HashMap<String, SeguroVida> listaDeSeguroVida = new HashMap<>();

    /** Método para adicionar um novo SeguroVida à lista de seguros de vida no banco tanto em tempo de execução quanto no arquivo de texto de seguros de vida.
     *
     * @param seguroVidaExt objeto do tipo SeguroVida que será adicionado no sistema
     * @throws SeguroExistenteException se um SeguroVida associado ao CPF do usuário já existir
     * @throws IOException se ocorrer um erro de escrita no arquivo de seguros de vida
     */
    public static void adicionaSeguroVida(SeguroVida seguroVidaExt) throws SeguroExistenteException, IOException {
        if(listaDeSeguroVida.containsKey(seguroVidaExt.getCpfSegurado())) {
            throw new SeguroExistenteException();
        }
        listaDeSeguroVida.put(seguroVidaExt.getCpfSegurado(), seguroVidaExt);

        File pathSeguroVidaBD = new File ("C:\\RepositorioBanco\\");
        File seguroVidaBD = new File(pathSeguroVidaBD.getAbsolutePath() + "\\seguroVidaRepositorio.txt");

        if(!pathSeguroVidaBD.exists()) {
            pathSeguroVidaBD.mkdirs();
        }

        if(!seguroVidaBD.exists()) {
            seguroVidaBD.createNewFile();
        }

        try (FileWriter seguraVidaDBWriter = new FileWriter(seguroVidaBD, true);
             BufferedWriter seguroVidaDBWriterBuff = new BufferedWriter(seguraVidaDBWriter)) {

            seguroVidaDBWriterBuff.append(seguroVidaExt.getCpfSegurado() + "¨¨" +
                    seguroVidaExt.getValorSegurado() + "¨¨" +
                    seguroVidaExt.getQtdMeses() + "¨¨" +
                    seguroVidaExt.getValorPago() + "¨¨" +
                    seguroVidaExt.getSegurados() + "¨¨" +
                    seguroVidaExt.getDataContratacao());

            seguroVidaDBWriterBuff.newLine();

        } catch (IOException e) {
            System.out.println("Erro de escrita de arquivos!");
        }
    }

    /** Método para remover um SeguroVida da lista de seguros de vida no banco. Note que essa função não altera o arquivo de seguros de vida e que não está em uso atualmente.
     *
     * @param cpfExt cpf do usuário que possui a ContaPoupanca que deve ser deletada
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     */
    public static void removeSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeSeguroVida.remove(cpfExt);
    }

    /** Método para retornar um SeguroVida específico a partir de um cpf informado.
     *
     * @param cpfExt cpf do usuário que possui o SeguroVida que deve ser retornado
     * @return Um objeto do tipo SeguroVida que corresponde ao cpf fornecido
     * @throws CpfInexistenteException se o cpf informado não existe no sistema
     */
    public static SeguroVida getSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeSeguroVida.get(cpfExt);
    }

    /** Método para retornar um List com todos os objetos SeguroVida registrados no banco.
     *
     * @return List com todos os objetos SeguroVida registrados no banco
     */
    public static List<SeguroVida> getSegurosVida() {
        return listaDeSeguroVida.values().stream().toList();
    }

    /** Método para carregar os objetos SeguroVida guardados no arquivo de seguros de vida no início da aplicação.
     *
     * @throws IOException se ocorrer um erro de leitura no arquivo de seguros de vida
     */
    public static void SeguroVidaLoader () throws IOException {
        File pathSeguroVidaBD = new File ("C:\\RepositorioBanco\\");
        File seguroVidaBD = new File(pathSeguroVidaBD.getAbsolutePath() + "\\seguroVidaRepositorio.txt");

        if(!pathSeguroVidaBD.exists()) {
            pathSeguroVidaBD.mkdirs();
        }

        if(!seguroVidaBD.exists()) {
            seguroVidaBD.createNewFile();
        }


        try (FileReader seguroVidaBDReader = new FileReader(seguroVidaBD);
             BufferedReader seguroVidaBDReaderBuff = new BufferedReader(seguroVidaBDReader)) {

            String linhaLida;
            while ((linhaLida = seguroVidaBDReaderBuff.readLine()) != null) {
                String[] itensTemp = linhaLida.split("¨¨");

                String cpfTemp = itensTemp[0];
                double valorSeguradoTemp = Double.parseDouble(itensTemp[1]);
                int qtdMesesTemp = Integer.parseInt(itensTemp[2]);
                double valorPagoTemp = Double.parseDouble(itensTemp[3]);

                List<String> seguradosTemp = new ArrayList<>();
                String[] vetorSegurados = itensTemp[4].split(", ");
                for (int indice = 0; indice < vetorSegurados.length; indice++) {
                    if (indice == 0) {
                        seguradosTemp.add(vetorSegurados[0].substring(1));
                    }
                    else if (indice == (vetorSegurados.length-1)) {
                        seguradosTemp.add(vetorSegurados[indice].substring(0, vetorSegurados[indice].length() - 1));
                    }
                    else {
                        seguradosTemp.add(vetorSegurados[indice]);
                    }
                }

                LocalDate dataContratacaoTemp = LocalDate.parse(itensTemp[5]);

                SeguroVida seguroVidaTemp = new SeguroVida(cpfTemp, valorSeguradoTemp, qtdMesesTemp, valorPagoTemp, seguradosTemp, dataContratacaoTemp);
                listaDeSeguroVida.put(seguroVidaTemp.getCpfSegurado(), seguroVidaTemp);
            }
        } catch (IOException e) {
            System.out.println("Erro de leitura de arquivos!");
        }
    }
}
