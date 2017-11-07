/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.net.Socket;

/**
 *
 * @author dheiso
 */
public class MiddlewareSolicitarModificar {

    private int tipoSolicitacao = 2;
    private String inf;//informações de registro deste middleware
    private MiddlewareDadosMD dados = new MiddlewareDadosMD();
    private boolean test;// este atributo é usado para verificar se o registrador encontrou um candidato para o handoff ou não
    private String dadosMD;//se o registrador encontrou um candidato para o handoff esta string recebe as informações desse candidato
    private String resposta;//este atributo é usado para receber a resposta do registrador
    //atributos usados para realizar solicitações ao registrador
    private int unicoId;
    private int tipoId;

    public MiddlewareSolicitarModificar(String inf) {
        this.inf = inf;
        obterIdentificadores();
    }

    public boolean isTest() {
        return test;
    }

    public MiddlewareDadosMD getDados() {
        return dados;
    }

    public void obterPar() {

        this.tipoSolicitacao = 2;
        solicitar();

    }
    
    //medoto usado par definir se o registro deste middleware no registrodor sera ativo ou inativo
    //OBS: medoto incompleto, falta definir as situações onde sera solicitado a ativação ou inativação do registro
    public void mudarEstado() {
        this.tipoSolicitacao = 3;
        solicitar();
    }

    public void solicitar() {

        try {
            Socket cliente = new Socket("192.168.100.7 ", 12345);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.flush();
            saida.writeObject(this.tipoSolicitacao + "#" + this.unicoId + "#" + this.tipoId);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            resposta = (String) entrada.readObject();
            deserializar(resposta);
            if (test) {
                dados.deserializar(dadosMD);
            }

            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            exit(0);
        } catch (ClassNotFoundException ex) {
            System.out.println("Problemas ao receber aquivo");
        }
    }

    public void deserializar(String linha) {
        String vetor[] = linha.split("#");

        this.test = Boolean.parseBoolean(vetor[0]);
        this.dadosMD = vetor[1];
    }
    
    private void obterIdentificadores() {
        String vetor[] = inf.split(";");
        
        this.unicoId = Integer.parseInt(vetor[0]);
        this.tipoId = Integer.parseInt(vetor[1]);
    }

}
