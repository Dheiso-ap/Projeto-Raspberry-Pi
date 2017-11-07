package projetoraspberry.Registrador;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Registrador {

    public static void main(String args[]) throws IOException {

        RegistradorNewThread threadInicial = new RegistradorNewThread(null, 0);//este objeto é instanciado com a unica finalidade de iniciar um atributo estatico presente nesta classe
        threadInicial.tabela = new RegistradorTabelaHash();//inicia atributo estatico 
        int contador = 1;

        ServerSocket servidor = new ServerSocket(12345);
        System.out.println("Aguardando conexões...");

        while (true) {

            contador++;

            Socket cliente = servidor.accept();
            System.out.println("Conexão " + contador + " aceita...");

            RegistradorNewThread tratamento = new RegistradorNewThread(cliente, contador);
            Thread thread = new Thread(tratamento);

            thread.start();

        }

    }

}
