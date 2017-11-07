
package projetoraspberry.Registrador;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 *
 * @author Dheiso
 */
public class RegistradorResponde {
    
    private Socket cliente;

    public RegistradorResponde(Socket cliente) {
        this.cliente = cliente;
    }
    
    
       //metodo para ligar duas aplicações do mesmo tipo para assim obter a condição de instancia primaria e secundaria
    public void informaPar(RegistradorRegistro inf) {

        try {
            
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.flush();
            if (inf != null) {
                saida.writeObject("true" + "#" + inf.getIp() + ";" + inf.getPorta());
            } else {
                saida.writeObject("false" + "#" + null);

            }
            saida.close();
            cliente.close();
        } catch (IOException ex) {

            System.out.println(ex.getMessage());
        }

    }

    public void informaUnicoId(RegistradorRegistro registro) {

        try {
         
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.flush();
            saida.writeInt(registro.getUnicoId());

            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Erro de conexão " + ex.getMessage());
        }

    }
    
    
    
}
