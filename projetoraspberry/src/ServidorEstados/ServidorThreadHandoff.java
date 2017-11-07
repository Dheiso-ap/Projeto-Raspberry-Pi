package ServidorEstados;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dheiso
 */
public class ServidorThreadHandoff extends Thread {
    
    private ServidorDadosMD registro;
    private ServidorDadosHandoff dadosHandoff = new ServidorDadosHandoff();
    private String ipRegistrador = "localhost";
    private int portaRegistrador = 12345;
    private String dados;
    private boolean tipoResposta;
    private int teste = 0;//este atributo é usado para que a classe que instanciou essa classe saiba se se o registro foi verificado e sua condição
    
    public ServidorThreadHandoff(ServidorDadosMD registro) {
        this.registro = registro;
    }
    
    @Override
    public void run() {
        
        if (!verifica()) {
            
            removerRegistroRegistrador();
            solicitar();
            
            if (tipoResposta) {
                teste = 1;
                for (ServidorDadosApp registroApp : registro.getDadosApp()) {

                    handoff(registroApp.getTipoId(), registroApp.getEstado());
                }
            }else{
                teste = 2;
                System.out.println("Handoff indisponivel");
            }
            
        }else{
            teste = 2;
        }
        
    }
    
    public void handoff(int tipoId, String estado) {
        
        Socket cliente;
        try {
            cliente = new Socket(dadosHandoff.getIp(), dadosHandoff.getA_porta());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(5 + "#" + tipoId + "#" + estado);
            saida.close();
            cliente.close();
            
        } catch (IOException ex) {
            System.out.println("Problemas ao realizar o handoff");
        }
        
    }
    
    public boolean verifica() {
        
        try {
            Socket cliente = new Socket(registro.getIpMD(), registro.getPortaMD());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject("4");
            
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            boolean resposta = false;
            resposta = entrada.readBoolean();
            
            entrada.close();
            saida.close();
            cliente.close();
            return resposta;
        } catch (IOException ex) {
            System.out.println("Problemas de comunicação");
            return false;
        }
        
    }
    
    public void solicitar() {
        
        try {
            Socket cliente = new Socket(ipRegistrador, portaRegistrador);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(2 + "#" + registro.getUnicoId() + "#" + registro.getTipoId());
            
            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            
            deserializar((String) entrada.readObject());
        
            if (tipoResposta) {
                dadosHandoff.deserializar(dados);
            }
            entrada.close();
            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão");
        } catch (ClassNotFoundException ex) {
            System.out.println("Problema ao receber informação");
        }
        
    }
    
    public void removerRegistroRegistrador() {
        
        try {
            Socket cliente = new Socket(ipRegistrador, portaRegistrador);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(4 + "#" + registro.getUnicoId() + "#" + registro.getTipoId());
            
         
            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão");
        }
        
    }
    
    public int retornaValorDaVerificacao(){
        return teste;
    }
    

    
    public void deserializar(String linha) {
        String vetor[] = linha.split("#");
        
        this.tipoResposta = Boolean.parseBoolean(vetor[0]);
        this.dados = vetor[1];
    }
    
}
