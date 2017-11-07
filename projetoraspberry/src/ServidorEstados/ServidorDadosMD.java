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
public class ServidorDadosMD {

    private int minuto;//este atributo é utilizado para guardar o minuto em que este registro foi atualizado
    private int segundos;//este atributo é utilizado para guardar os segundos em que este registro foi atualizado;
    private String ipMD;//ip do middleware
    private int unicoId;//identificação unica do middleware
    private int tipoId;
    private int portaMD;//porta do middleware
    private Boolean validade = true;
    private ArrayList<ServidorDadosApp> dadosApp = new ArrayList<>();

    public void addApp(ServidorDadosApp app) {
        this.dadosApp.add(app);

    }

    public boolean atualizaApp(ServidorDadosApp appDados) {
        for (ServidorDadosApp registro : dadosApp) {
            if (registro.getUnicoId() == appDados.getUnicoId()) {
                registro.setEstado(appDados.getEstado());
                return true;
            }

        }
        return false;
    }

    public ArrayList<ServidorDadosApp> getDadosApp() {
        return dadosApp;
    }

    public boolean isEmpty() {
        return dadosApp.isEmpty();
    }

    public boolean removeApp(ServidorDadosApp app) {
        for (ServidorDadosApp registro : dadosApp) {
            if (registro.getUnicoId() == app.getUnicoId()) {
                dadosApp.remove(registro);
                return true;
            }

        }
        return false;
    }

    public void atualizaTempoDeAcesso() {
        Calendar Data = Calendar.getInstance();
        this.minuto = Data.get(Calendar.MINUTE);
        this.segundos = Data.get(Calendar.SECOND);
    }

    public void removerApps() {
        dadosApp.clear();
    }

    public int getMinuto() {
        return minuto;
    }

    public void setMinuto(int minuto) {
        this.minuto = minuto;
    }

    public int getSegundos() {
        return segundos;
    }

    public void setSegundos(int segundos) {
        this.segundos = segundos;
    }
    
    

    public Boolean getValidade() {
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

    public int getUnicoId() {
        return unicoId;
    }

    public void setUnicoId(int unicoId) {
        this.unicoId = unicoId;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public int getPortaMD() {
        return portaMD;
    }

    public void setPortaMD(int portaMD) {
        this.portaMD = portaMD;
    }

    public void deserializar(String linha) {
        String vetor[] = linha.split(";");

        this.tipoId = Integer.parseInt(vetor[0]);
        this.unicoId = Integer.parseInt(vetor[1]);
        this.portaMD = Integer.parseInt(vetor[2]);
        this.ipMD = vetor[3];

    }

}
