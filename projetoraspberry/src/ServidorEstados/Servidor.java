package ServidorEstados;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author dheiso
 */
public class Servidor {

    public static void main(String args[]) throws IOException {

        ServerSocket servidor = new ServerSocket(54321);
        ServidorNewThread threadInicial = new ServidorNewThread(null, false);
        threadInicial.registro = new ServidorRegistro();
        Thread thread = new Thread(threadInicial);
        thread.start();
        System.out.println("Servidor de Estados Ativo...");

        while (true) {

            Socket cliente = servidor.accept();

            ServidorNewThread tratamento = new ServidorNewThread(cliente, true);

            Thread thread1 = new Thread(tratamento);

            thread1.start();

        }

    }

}
