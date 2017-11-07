package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.Socket;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Random;


public class MiddlewareRegistrar {

    private final int portaP;//Porta padrão do middleware onde as solicitações de outros middlwares e do servidor de estador serão recebidas
    private int unicoId = 0;//Este atributo vem do registrador e representa a identificação unica deste middware no registrador
    private boolean condicao = true;//Este atributo representa a condição do middlware(se esta pronto para receber solicitações ou não)
    private final int tipoId = 12345678;//este atributo representa o tipo do dispositivo que esta executando o middeware
    private final String ip = obterIp();//Este atributo representa o ip desta maquina
    private int tipoSolicitacao = 1;//Este atributo serve para que o registrador identifique que tipo de solicitação é essa, neste caso solicitação para registrar este middeware

    public MiddlewareRegistrar(int portaP) {
        this.portaP = portaP;
    }

    
    
    
    public int getUnicoId() {
        return unicoId;
    }

    //este metodo envia as informações do middleware para o registrador
    public void registrar() {
        try {
            Socket cliente = new Socket("192.168.100.7 ", 12345);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(this.tipoSolicitacao + "#" + serializar());

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            unicoId = entrada.readInt();
            saida.close();
            cliente.close();
            
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            exit(0);
        }
    }

    //Este medoto registra o middleware no servidor de estados
    public void registrarNoServidorDeEstados() {

        try {
            Socket cliente = new Socket("192.168.100.7 ", 54321);
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(this.tipoSolicitacao + "#" + this.tipoId + ";" + this.unicoId + ";" + this.portaP + ";" + this.ip);
            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            exit(0);
        }
    }


    //este metodo gera uma porta aleatoriamente para ser usada na aplicação como entrada de informação pelo registrador ou outra aplicação
    private int randomPorta() {
        Random gerador = new Random();

        int porta = gerador.nextInt(48127) + 1024;

        return porta;

    }

    private String obterIp() {

        try {
            //este trecho de codígo a seguir ate o fechamento do proximo if serve para obter o ip da maquina,
            //este trecho de codígo mapeia todas as interfaces de rede e todos os endereços ate encontrar o ip correto,
            //isto porque no linux o metodo "InetAddress.getLocalHost().getHostAddress()" apresenta problemas.

            String ip = null;

            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();

            if (interfaces != null) {
                while (interfaces.hasMoreElements()) {

                    Enumeration<InetAddress> enderecos = interfaces.nextElement().getInetAddresses();
                    while (enderecos.hasMoreElements()) {
                        InetAddress endereco = enderecos.nextElement();

                        if (!endereco.isLoopbackAddress() && endereco.isSiteLocalAddress()) {
                            ip = endereco.getHostAddress();
                            return ip;
                        }
                    }
                }
            }

        } catch (SocketException ex) {
            System.out.println("Problemas ao obter Host Address" + ex.getMessage());
            return null;
        }
        return null;
    }

    //este metodo converte os dados desta classe em uma string
    public String serializar() {

        return  this.unicoId + ";" + this.tipoId + ";" + this.portaP + ";" + this.ip + ";" + this.condicao;

    }

}
