package projetoraspberry.app;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import static java.lang.System.exit;
import java.net.Socket;
import java.util.Random;
import com.google.gson.Gson;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Dheiso
 */
public class AppControle extends Thread {

    private AppDados dados;
    private Gson gson = new Gson();
    private Socket cliente;

    public AppControle(AppDados dados) {

        this.dados = dados;

    }

    public void iniciar() {
        dados.iniciar();
    }

    public void parar() {
        dados.parar();
    }

    @Override
    public void run() {

        while (true) {

            try {
                enviarEstado();
                Thread.sleep(300);
            } catch (Exception e) {
                System.out.println("socket fechado...");

            }

        }
    }


    
    //este medoto recebe o estado do middleware
    public boolean receberEstado() {

        try {
            cliente = new Socket("localhost", dados.getMDWporta());

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.writeObject(7 + "#" + dados.getUnicoId() + "#" + dados.getTipoId());

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            String json = (String) entrada.readObject();
            dados.setEstado(gson.fromJson(json, AppEstado.class));
            dados.getEstado().DevolverValoresContador(dados.getCont());

            entrada.close();
            cliente.close();
            return true;
        } catch (Exception ex) {
            System.out.println("Erro ao receber handoff " + ex.getMessage());
            return false;
        }

    }

    //este medoto envia o estado atual desta aplicação para o middleware
    public void enviarEstado() {

        try {
            Socket cliente = new Socket("localhost", dados.getMDWporta());

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            dados.getEstado().ObterValoresContador(dados.getCont());

            String json = gson.toJson(dados.getEstado());
            
            saida.flush();
            saida.writeObject(8 + "#" + dados.getTipoId() + "#" + dados.getUnicoId() + "#" + json);
            saida.close();
            cliente.close();
        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            System.exit(0);
        }
    }

    //este medoto fornece para o middleware informações da aplicação para que seja realizado o cadastro desta aplicação
    public void registro(boolean condicao) {
        try {

            dados.setCondicao(condicao);

            Socket cliente = new Socket("localhost", dados.getMDWporta());

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.writeObject(1 + "#" + serializar());
            saida.flush();

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            int unicoId = entrada.readInt();

            dados.setUnicoId(unicoId);

            saida.close();
            entrada.close();
            cliente.close();

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            exit(0);
        }
    }

    //solicita para o middleware o handoff imediato, entrega o estado atual assim que solicitado
    public void solicitar() {
        try {

            Socket cliente = new Socket("localhost", dados.getMDWporta());

            dados.getEstado().ObterValoresContador(dados.getCont());
            String json = gson.toJson(dados.getEstado());

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());

            saida.writeObject(2 + "#" + serializar() + "#" + json);

            System.exit(0);

            saida.close();

            cliente.close();

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            System.exit(0);
        }
    }

    public void atualizar() {
        try {
            Socket cliente = new Socket("localhost", dados.getMDWporta());
            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.writeObject(6 + "#" + dados.getUnicoId() + "#" + dados.getTipoId());
            saida.flush();
            saida.close();
            cliente.close();

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            exit(0);
        }
    }

    //reconhece uma tecla especifica quando pressionada
    public boolean evento() {

        boolean test = false;
        char enter = 0;

        while (!test) {
            try {
                enter = (char) System.in.read();
            } catch (IOException ex) {
                Logger.getLogger(AppControle.class.getName()).log(Level.SEVERE, null, ex);
            }
            if (enter == '1') {
                test = true;
            } else {
                if(enter == '2'){
                    System.out.println("valor: " + dados.getCont().getValor());
                }
            }
        }

        return test;
    }

    public void visualizarEstado() {

        System.out.println(dados.getCont().getValor());

    }

    //este metodo gera uma porta aleatoriamente para ser usada na aplicação como entrada de informação pelo registrador ou outra aplicação
    private int randomPorta() {
        Random gerador = new Random();

        int porta = gerador.nextInt(48127) + 1024;

        return porta;

    }

    public String serializar() {
        return (dados.getTipoId() + ";" + dados.getUnicoId() + ";" + dados.isCondicao());
    }
}
