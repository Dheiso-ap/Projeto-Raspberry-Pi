package ServidorEstados;

/**
 *
 * @author dheiso
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ServidorNewThread extends Thread {

    static ServidorRegistro registro;
    private Socket cliente;
    private int tipoSolicitacao;
    private String dados;
    private String dadosMD;
    private String dadosApp;
    private boolean tipoTrabalho;//este atributo define se uma thread sera usada para ficar ativa e procurando middwares falhos
    // private Date data = new Date();

    public ServidorNewThread(Socket cliente, boolean tipoTrabalho) {
        this.cliente = cliente;
        this.tipoTrabalho = tipoTrabalho;
    }

    @Override
    public void run() {

        if (tipoTrabalho == false) {
            int calculo;
            int minAtual;
            int segundoAtual;
            while (true) {

                ArrayList<ServidorDadosMD> registros = new ArrayList<>(registro.getIstanciasMD());

                Calendar Data = Calendar.getInstance();
                minAtual = Data.get(Calendar.MINUTE);
                segundoAtual = Data.get(Calendar.SECOND);

                for (ServidorDadosMD rgt : registros) {

                    if (minAtual >= rgt.getMinuto()) {

                        calculo = minAtual - rgt.getMinuto();
                        calculo = calculo * 60;
                        if (segundoAtual >= rgt.getSegundos()) {
                            calculo = calculo + (segundoAtual - rgt.getSegundos());
                        } else {
                            calculo = calculo - (rgt.getSegundos() - segundoAtual);
                        }

                    } else {

                        calculo = (minAtual - rgt.getMinuto()) + 60;

                        if (segundoAtual >= rgt.getSegundos()) {
                            calculo = calculo + (segundoAtual - rgt.getSegundos());
                        } else {
                            calculo = calculo - (rgt.getSegundos() - segundoAtual);
                        }
                    }

                    if (calculo >= 60 && !rgt.isEmpty()) {
                        ServidorThreadHandoff handoff = new ServidorThreadHandoff(rgt);
                        Thread thread = new Thread(handoff);
                        thread.start();

                        boolean teste = true;

                        while (teste) {
                            switch (handoff.retornaValorDaVerificacao()) {
                                case 1:
                                    this.registro.removerMD(rgt);

                                    teste = false;
                                    break;
                                case 2:
                                    teste = false;
                                    break;
                            }
                        }
                    }

                }

                try {
                    Thread.sleep(1000);
                } catch (InterruptedException ex) {
                    System.out.println("Problemas de execução " + ex.getLocalizedMessage());
                }
            }

        } else {

            try {

                ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

                String linha = (String) entrada.readObject();
                System.out.println(linha);
                deserializar(linha);
                ServidorDadosMD MD = new ServidorDadosMD();
                ServidorDadosApp app = new ServidorDadosApp();
                MD.deserializar(dadosMD);
                Calendar Data = Calendar.getInstance();
                switch (tipoSolicitacao) {

                    case 1://para este caso o servidor registra um middlware

                        registro.addMD(MD);

                        break;
                    case 2://para este caso o servidor adiciona o estado de um app em um middleware ou atualiza o estado
                        //registro.mostrarRegistros();
                        app.deserializar(dadosApp);

                        registro.addApp(MD, app);
                        break;

                    case 3://para este caso o servidor remove os dados e o estado de uma app

                        app.deserializar(dadosApp);
                        registro.removerApp(MD, app);
                        break;

                }
                entrada.close();
                cliente.close();
            } catch (IOException ex) {
                System.out.println("Erro na conexão " + ex.getMessage());
            } catch (ClassNotFoundException ex) {
                System.out.println("Erro ao receber informações");
            }
        }

    }

    public void deserializar(String linha) {
        String vetor[] = linha.split("#");

        this.tipoSolicitacao = Integer.parseInt(vetor[0]);
        switch (this.tipoSolicitacao) {
            case 1:
                this.dadosMD = vetor[1];
                break;
            case 2:
            case 3:
                this.dadosMD = vetor[1];
                this.dadosApp = vetor[2];
                break;

        }

    }

}
