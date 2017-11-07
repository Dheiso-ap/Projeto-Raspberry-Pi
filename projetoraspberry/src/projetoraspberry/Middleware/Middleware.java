package projetoraspberry.Middleware;

/**
 *
 * @author dheiso
 */
/*Esta classe funciona como multi thread e trata varias conexões de aplicações executando na mesma maquina 
 */
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

public class Middleware {

    public static void main(String[] args) throws IOException {

        Scanner ler = new Scanner(System.in);
        int porta = 1478;
        MiddlewareRegistrar registrar = new MiddlewareRegistrar(porta);
        registrar.registrar();
        registrar.registrarNoServidorDeEstados();

        MiddlewareControle controle = new MiddlewareControle(null, registrar.serializar());
        controle.tabelaApp = new MiddlewareTabelaHash();
        controle.cont = 0;
        ServerSocket middleware = new ServerSocket(porta);
        System.out.println("Middleware ativo...");
        int cont = 0;

        while (true) {


            Socket cliente = middleware.accept();
            // System.out.println("Conexão aceita");

            MiddlewareControle tratamento = new MiddlewareControle(cliente, registrar.serializar());

            Thread thread = new Thread(tratamento);

            thread.start();

        }

    }

}
