/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoraspberry.Middleware;

import java.net.Socket;

/**
 *
 * @author dheiso
 */
public class MiddlewareDadosApp {


    private int tipoId;
    private int unicoId;
    private boolean condicao;
    private Socket cliente;

    public Socket getCliente() {
        return cliente;
    }

    public void setCliente(Socket cliente) {
        this.cliente = cliente;
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
    
    

    public boolean isCondicao() {
        return condicao;
    }

    public void setCondicao(boolean condicao) {
        this.condicao = condicao;
    }

        
      //este metodo separa as informações recebidas de uma aplicação
        public void deserializar(String linha) {
        String vetor[] = linha.split(";");

        this.tipoId = Integer.parseInt(vetor[0]);
        this.unicoId = Integer.parseInt(vetor[1]);
        this.condicao = Boolean.parseBoolean(vetor[2]);
    }

}
