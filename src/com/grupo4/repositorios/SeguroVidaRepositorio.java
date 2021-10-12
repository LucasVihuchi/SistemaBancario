package com.grupo4.repositorios;

import com.grupo4.exceptions.CpfInexistenteException;
import com.grupo4.exceptions.SeguroExistenteException;
import com.grupo4.segurovida.SeguroVida;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

public class SeguroVidaRepositorio {
    private static HashMap<String, SeguroVida> listaDeSeguroVida = new HashMap<>();

    public static void adicionaSeguroVida(SeguroVida seguroVidaExt) throws SeguroExistenteException, IOException {
        if(listaDeSeguroVida.containsKey(seguroVidaExt.getCpfSegurado())) {
            throw new SeguroExistenteException();
        }
        listaDeSeguroVida.put(seguroVidaExt.getCpfSegurado(), seguroVidaExt);

        File seguroVidaBD = new File("C:\\RepositorioBanco\\seguroVidaRepositorio.txt");

        if(!seguroVidaBD.exists()) {
            seguroVidaBD.mkdirs();
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

    public static void removeSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        listaDeSeguroVida.remove(cpfExt);
    }

    public static SeguroVida getSeguroVida(String cpfExt) throws CpfInexistenteException {
        if(!listaDeSeguroVida.containsKey(cpfExt)) {
            throw new CpfInexistenteException();
        }
        return listaDeSeguroVida.get(cpfExt);
    }
}
