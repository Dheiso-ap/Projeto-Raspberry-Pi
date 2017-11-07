package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.BufferedReader;
import java.io.FileReader;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dheiso
 */
public class MiddlewareControleApp {

    private MiddlewareDadosApp dados;
    private String estado;

    public MiddlewareControleApp(MiddlewareDadosApp dados) {
        this.dados = dados;
    }

    public MiddlewareDadosApp getDados() {
        return dados;
    }

    public void setDados(MiddlewareDadosApp dados) {
        this.dados = dados;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    //este metodo solicita para a aplicação o estado quando for encontrado um candidato para receber o estado
    public boolean solicitarEstado(Socket cliente, boolean confirma) {

        try {

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();

            saida.writeBoolean(confirma);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            if (confirma) {

                this.estado = (String) entrada.readObject();
            }

            saida.close();
            entrada.close();
            return true;

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            return false;
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MiddlewareControleApp.class.getName()).log(Level.SEVERE, null, ex);
            return false;
        }
    }

    public boolean recebeEstado(InputStream fluxo, boolean confirma) {

        try {

            ObjectInputStream entrada = new ObjectInputStream(fluxo);

            this.estado = (String) entrada.readObject();

            //entrada.close();
            return true;

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            return false;
        } catch (ClassNotFoundException ex) {
            System.out.println("Problemas ao receber arquivo " + ex.getLocalizedMessage());
            return false;
        }
    }

    //este medoto envia o estado recebido para uma aplicação que esteja registrada para receber estados(estado referente ao processamento atual da aplicação)
    public void enviarEstado(String estado) throws IOException {

            ObjectOutputStream saida = new ObjectOutputStream(dados.getCliente().getOutputStream());
            saida.flush();
            saida.writeObject(estado);
            saida.close();
            dados.getCliente().close();
        
    }

    public void informaUnicoId(Socket cliente) {

        try {

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeInt(dados.getUnicoId());
            saida.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());

        }
    }

    public void iniciaApp() {
            
        try {
            String nome = "";
            int tipo;
            BufferedReader leitor = new BufferedReader(new FileReader("/home/iron-nigga/repositorio/dadosApp.txt"));

            while (true) {
                String linha = leitor.readLine();

                if (linha != null) {
                    String[] partes = linha.split(";");

                    tipo = Integer.parseInt(partes[0]);
                    nome = partes[1];

                    if (tipo == dados.getTipoId()) {
                        String comando = "java -jar /home/iron-nigga/repositorio/" + nome + ".jar 1 &";

                        Process processo = Runtime.getRuntime().exec(comando);
                        break;
                    }

                } else {
                    break;
                }

            }
            leitor.close();
        } catch (IOException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
    }

}
