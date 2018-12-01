/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ServidorEstados;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Calendar;

/**
 *
 * @author Dheiso
 */

/*Esta classe contem os dados para registrar um middleware,
e um dos atributos é uma lsita com os registros das aplicações*/
public class ServidorDadosMD {

    private int minuto;//este atributo é utilizado para guardar o minuto em que este registro foi atualizado
    private int segundos;//este atributo é utilizado para guardar os segundos em que este registro foi atualizado;
    private String ipMD;//ip do middleware
    private int unicoId;//identificação unica do middleware
    private int tipoId;
    private int portaMD;//porta do middleware
    private Boolean validade = true;
    private ArrayList<ServidorDadosApp> dadosApp = new ArrayList<>();

    public void addApp(ServidorDadosApp app) {//adiciona o registro de uma aplicação
        this.dadosApp.add(app);

    }

    public boolean atualizaApp(ServidorDadosApp appDados) {//atualiza o registro de uma aplicação, referente ao estado desta aplicação
        for (ServidorDadosApp registro : dadosApp) {
            if (registro.getUnicoId() == appDados.getUnicoId()) {
                registro.setEstado(appDados.getEstado());
                return true;
            }

        }
        return false;
    }

    public ArrayList<ServidorDadosApp> getDadosApp() {//retorna uma lista de todas as aplicações registradas no middleware
        return dadosApp;
    }

    public boolean isEmpty() {
        return dadosApp.isEmpty();
    }

    public boolean removeApp(ServidorDadosApp app) {//remove registro de uma aplicação
        for (ServidorDadosApp registro : dadosApp) {
            if (registro.getUnicoId() == app.getUnicoId()) {
                dadosApp.remove(registro);
                return true;
            }

        }
        return false;
    }

    public void atualizaTempoDeAcesso() {//atualiza o momento que este registro de middleware foi atualizado
        Calendar Data = Calendar.getInstance();
        this.minuto = Data.get(Calendar.MINUTE);
        this.segundos = Data.get(Calendar.SECOND);
    }

    public void removerApps() {//remove todas os registro de aplicações que estão em um registro do middleware
        dadosApp.clear();
    }

    public int getMinuto() {//retorna o minuto que este registro foi atualizado
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundos() {//retorna o segudno que este regitro foi atualizado
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    
    

    public Boolean getValidade() {//retorna se a validade, que indica se o middleware está ativo ou não
        return validade;
    }

    public void setValidade(Boolean validade) {
        this.validade = validade;
    }

    

    public String getIpMD() {
        return ipMD;
    }

    public void setIpMD(String ipMD) {
        this.ipMD = ipMD;
    }

    public int getUnicoId() {//retorna a identificação unica do registro do middleware
        return unicoId;
    }

    public void setUnicoId(int unicoId) {
        this.unicoId = unicoId;
    }

    public int getTipoId() {//retorna um valro que indica qual é o tipo do middleware
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public int getPortaMD() {//retorna o valor da porta em que o middlware trabalha
        return portaMD;
    }

    public void setPortaMD(int portaMD) {
        this.portaMD = portaMD;
    }

    public void deserializar(String linha) {//deserializa uma string com as informações nescessarias para criar um novo objeto dessa classe
        String vetor[] = linha.split(";");

        this.tipoId = Integer.parseInt(vetor[0]);
        this.unicoId = Integer.parseInt(vetor[1]);
        this.portaMD = Integer.parseInt(vetor[2]);
        this.ipMD = vetor[3];

    }

}
