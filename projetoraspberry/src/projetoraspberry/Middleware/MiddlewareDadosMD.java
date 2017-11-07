/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoraspberry.Middleware;

/**
 *
 * @author Dheiso
 */
public class MiddlewareDadosMD {

    private String ip = null;//este atributo representa o ip recebido pelo registrador referente a outro middleware que esta pronto para receber o handoff
    private int A_porta = 0;// este atributo representa a porta recebida pelo registrador referente a outro middleware que esta pronto para receber o handoff

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getA_porta() {
        return A_porta;
    }

    public void setA_porta(int A_porta) {
        this.A_porta = A_porta;
    }

    
    
    //este metodo recebe uma string e separa em informações sobre outra aplicação que sera usada como clone
    public void deserializar(String linha) {

        String vetor[] = linha.split(";");
        
        this.ip = vetor[0];
        this.A_porta = Integer.parseInt(vetor[1]);
        
    }

}
