package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author dheiso
 */
public class MiddlewareHandoffAtivo {

    private int unicoId;
    private int tipoId;
    private int porta;
    private String ip;
    
        //este medoto envia o estado de uma aplicação par ao servidor de estados 
        public void eHandoff(int tipoIdApp, int unicoIdApp, String estado) {
        
        try {
            Socket cliente = new Socket("192.168.100.7 ", 54321);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(2 + "#" + this.tipoId + ";" + this.unicoId + ";" + this.porta + ";" + this.ip + "#" + tipoIdApp + ";" + unicoIdApp +";" + estado);
            saida.close();
            cliente.close();
            
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            
        }
        
    }
        public void removerAppServidorEstados(int tipoIdApp, int unicoIdApp, String estado) {
        
        try {
            Socket cliente = new Socket("192.168.100.7 ", 54321);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(3 + "#" + this.tipoId + ";" + this.unicoId + ";" + this.porta + ";" + this.ip + "#" + tipoIdApp + ";" + unicoIdApp +";" + estado);
            saida.close();
            cliente.close();
            
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            
        }
        
    }
        
        

  
    // Este medoto separa as informaçẽos importante para registrar o middleware no servidor de estados
    public void deserializar(String linha) {
        String vetor[] = linha.split(";");
        
        
        this.unicoId = Integer.parseInt(vetor[0]);
        this.tipoId = Integer.parseInt(vetor[1]);
        this.porta = Integer.parseInt(vetor[2]);
        this.ip = vetor[3];
    }

}
