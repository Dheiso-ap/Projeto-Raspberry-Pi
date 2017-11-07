/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoraspberry.app;

import com.google.gson.Gson;
import java.util.Random;

/**
 *
 * @author Dheiso
 */
public class AppDados {
    
    private final int tipoId = 123456;//identificador unico para esta aplicação
    private int unicoId = 0;
    private int MDWporta = 1478;//porta padrão para se comunicar com o middleware
    private int MCporta;//Esta porta sera enviada pelo middlaware para que se comunique com esta app
   
    private AppContador cont = new AppContador(0);
    private AppEstado estado = new AppEstado();
    private boolean condicao = false;//define se a aplicação esta ativa ou não, ou seja se irá recerber ou enviar o estado

    public int getTipoId() {
        return tipoId;
    }

    public int getUnicoId() {
        return unicoId;
    }

    public int getMDWporta() {
        return MDWporta;
    }



    public AppContador getCont() {
        return cont;
    }

    public AppEstado getEstado() {
        return estado;
    }

    public boolean isCondicao() {
        return condicao;
    }

    public void setCondicao(boolean condicao) {
        this.condicao = condicao;
    }
    
    public void setUnicoId(int unicoId) {
        this.unicoId = unicoId;
    }

    public void setMCporta(int MCporta) {
        this.MCporta = MCporta;
    }

    public void setEstado(AppEstado estado) {
        this.estado = estado;
    }


    public boolean isEstado() {
        return condicao;
    }

    public int getMCporta() {
        return MCporta;
    }

    public void iniciar() {
        cont.start();
    }

    public void parar() {
        cont.interrupt();
    }
    
    
    
}
