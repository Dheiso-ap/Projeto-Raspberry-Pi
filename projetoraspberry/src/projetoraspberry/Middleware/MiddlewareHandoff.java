package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Random;

public class MiddlewareHandoff {

    private MiddlewareDadosMD dados;
    private String estado;// estado recebido de outros middlewares refentes a uma app 
    Socket cliente;

    public MiddlewareHandoff() {
        this.dados = null;
        this.cliente = null;
    }

    public MiddlewareHandoff(MiddlewareDadosMD dados) throws IOException {
        this.dados = dados;
        cliente = new Socket(dados.getIp(), dados.getA_porta());
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public boolean solicitar(String dadosApp, String estadoApp) {

        try {

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            String linha = 3 + "#" + dadosApp + "#" + estadoApp;
            System.out.println(estadoApp.getBytes().length);
            System.out.println(linha.getBytes().length);
            saida.flush();
            long inicio = System.currentTimeMillis();
            saida.writeObject(3 + "#" + dadosApp + "#" + estadoApp);
            long fim = System.currentTimeMillis();
            long tempo = (fim - inicio);
            
            System.out.println(inicio);
            System.out.println(fim);
            System.out.println(tempo);

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            boolean resposta = entrada.readBoolean();

            return resposta;

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());
            return false;
        }
    }

    //este metodo realiza o handoff do estado desta aplicação para outra aplicação usando as informações recebidas pelo registrador
    public void eHandoff(String estado) {

        try {

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();
            saida.writeObject(estado);
            saida.close();
            cliente.close();

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());

        }

    }

    //este metodo recebe o handoff de uma aplicação
    public void rHandoff(Socket cliente) {

        try {

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            this.estado = (String) entrada.readObject();

        } catch (IOException ex) {
            System.out.println("Erro de Conexão " + ex.getMessage());

        } catch (ClassNotFoundException ex) {
            System.out.println("Erro ao receber informações " + ex.getMessage());
        }

    }

    //este metodo responde para um middleware se esta tudo bem para receber o estado da app ou não(Se estiver tudo bem ele envia uma porta especifica para que o estado seja enviado)
    public void responde(Socket cliente, boolean resposta) {
        try {

            ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
            saida.flush();

            saida.writeBoolean(resposta);
            saida.close();

        } catch (IOException ex) {
            System.out.println("Problemas de conexão " + ex.getMessage());

        }
    }

    //este metodo gera uma porta aleatoriamente para ser usada na aplicação como entrada de informação pelo registrador ou outra aplicação
    private int randomPorta() {
        Random gerador = new Random();

        int porta = gerador.nextInt(48127) + 1024;

        return porta;

    }

}
