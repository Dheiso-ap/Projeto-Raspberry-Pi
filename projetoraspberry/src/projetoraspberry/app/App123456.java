package projetoraspberry.app;

/**
 *
 * @author Dheiso
 */
import java.util.Scanner;

public class App123456 {

    public static void main(String[] args) {

        Scanner ler = new Scanner(System.in);

        AppDados dados = new AppDados();

        AppControle controle = new AppControle(dados);//classe responsavel pela logica de controle inclusive realizar handoff

        if (args.length != 0) {
            //registra a aplicação no middleware
            controle.registro(false);
            if (controle.receberEstado()) {
                dados.iniciar();
            }
        } else {

           controle.registro(true);

            dados.iniciar();

        }

        controle.start();

        if (controle.evento()) {//se o evento ocorrer

            controle.solicitar();

        }

    }

}
