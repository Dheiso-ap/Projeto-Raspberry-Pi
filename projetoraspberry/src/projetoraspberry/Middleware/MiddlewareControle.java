package projetoraspberry.Middleware;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class MiddlewareControle extends Thread {

    static int cont;
    static MiddlewareTabelaHash tabelaApp; // esta tabela guarda as informações das aplicações registradas no middeware
    private MiddlewareDadosApp registro = new MiddlewareDadosApp();//objeto usado para armazenar as informações de uma aplicação
    private MiddlewareHandoff handoff;//objeto responsavel por realizar o handoff entre os middlware
    private MiddlewareControleApp controleApp;//objeto responsavel pela comunicação entre o middleware e a aplicação
    private Socket app;//canal de comunicação 
    private String inf;//guarda as informações de registro deste middleware
    private int unicoIDApp;//identificador unico para as aplicações
    private String dados;//Os dados recebidos das aplicações de outro middeware ou um servidor de estados
    private int solicitacao;//este atributo recebe o primeiro item do atributo dados quando é deserializado pela função local e serve para verificar que tipo de solicitação esta sendo requisistada
    private String dadosApp;//este atributo recebe o segundo item do atributo dados quando e deserializado pela função local e representa os dados de uma aplicação
    private int tipoIdApp;//Este metodo é usado caso o servidor de estados faça handoff para este dispositivo
    private String estadoApp;//este atributo recebe o terceiro item do atributo dado quando e deserializado pela função local e representa o estado de uma aplicação
    private int portaMD;//esta porta serve para que outro middleware responda para este

    public MiddlewareControle(Socket app, String inf) {
        this.app = app;
        this.inf = inf;

    }

    @Override
    public void run() {

        try {

            ObjectInputStream entrada = new ObjectInputStream(app.getInputStream());

            dados = (String) entrada.readObject();

            deserializar(dados);

            switch (solicitacao) {
                case 1://registra uma aplicação no middleware
                    this.cont++;
                    this.unicoIDApp = this.cont;
                    registro.deserializar(dadosApp);
                    registro.setUnicoId(unicoIDApp);
                    controleApp = new MiddlewareControleApp(registro);

                    controleApp.informaUnicoId(app);

                    tabelaApp.add(registro);

                    break;
                case 2://este caso acontece quando uma app necessita realizar handoff

                    MiddlewareSolicitarModificar sm = new MiddlewareSolicitarModificar(inf);//objeto usado para solicitar ao registrador um destido para o handoff de uma aplicação

                    registro.deserializar(dadosApp);

                    if (tabelaApp.verificaRegistro(registro)) {
                        controleApp = new MiddlewareControleApp(registro);
                        sm.solicitar();

                        try {

                            if (sm.isTest()) {//se foi encontrado um dispossitivo identico, tenta realizar o handoff, caso contrario o handoff não sera realizado
                                handoff = new MiddlewareHandoff(sm.getDados());

                                //solicita o handoff para o outro middleware
                                if (handoff.solicitar(dadosApp, estadoApp)) {//resposta do outro midleware, se pode realizar handoff ou não                                    
                                    System.out.println("Handoff realizado com sucesso");
                                    MiddlewareHandoffAtivo handoffAtivo = new MiddlewareHandoffAtivo();
                                    handoffAtivo.deserializar(inf);
                                    handoffAtivo.removerAppServidorEstados(registro.getTipoId(), registro.getUnicoId(),null);
                                    //handoff.eHandoff(this.estadoApp);//tenta realizar o handoff com as informações de um dispositivo recebidas do registrador

                                    break;
                                } else {
                                    System.out.println("falha no handoff");
                                    // controleApp.solicitarEstado(app, false);
                                }

                            } else {
                                System.out.println("Não foi possivel realizar o handoff");

                                // controleApp.solicitarEstado(app, false);
                                break;
                            }

                        } catch (Exception e) {
                            System.out.println("Erro ao realizar um handoff..");
                        }

                    } else {
                        System.out.println("Aplicação não cadastrada");
                    }

                    break;
                case 3:// este caso acontece quando um middleware solicita para este um handoff
                    System.out.println(dadosApp);
                    registro.deserializar(dadosApp);

                    MiddlewareDadosApp rDados;
                    handoff = new MiddlewareHandoff();

                    controleApp = new MiddlewareControleApp(registro);//se prepara para iniciar uma nova app, caso nem uma estaja pronta e registrada
                    controleApp.iniciaApp();//inicia uma nova app do mesmo tipo da que foi solicitada
                    Thread.sleep(3000);//espera alguns segundos para que a nova app se registre
                    rDados = tabelaApp.buscaRegistro(registro);//busca a nova app nos registros

                    if (rDados != null) {//se a app for encontrada, confirma para o outro middleware, para que possa enviar o estado, depois envia o estado para a nova app
                        controleApp = new MiddlewareControleApp(rDados);
                        // handoff.rHandoff(app);
                        controleApp.enviarEstado(this.estadoApp);
                        tabelaApp.mudaEstado(rDados, true);
                        handoff.responde(app, true);

                    } else {
                        handoff.responde(app, false);//se a iniciação de uma nova app deu errado responde para o outro middleware que não funcionou
                        System.out.println("Não foi possivel iniciar uma aplicação");
                    }

                    break;
                case 4:// este caso acontece quando o servidor de estados pergunta se este middeware esta ok 
                    handoff = new MiddlewareHandoff();
                    handoff.responde(app, true);
                    break;
                case 5://este caso acontece quando o servidor de estados enviar informações e o estado de aplicações
                    registro.setTipoId(tipoIdApp);

                    MiddlewareDadosApp DadosR;

                    controleApp = new MiddlewareControleApp(registro);//se prepara para iniciar uma nova app, caso nem uma estaja pronta e registrada
                    controleApp.iniciaApp();//inicia uma nova app do mesmo tipo da que foi solicitada
                    Thread.sleep(3000);//espera alguns segundos para que a nova app se registre

                    boolean teste = true;

                    while (teste) {

                        DadosR = tabelaApp.buscaRegistro(registro);//busca a nova app nos registros

                        if (DadosR != null) {
                            controleApp = new MiddlewareControleApp(DadosR);
                            try {
                                controleApp.enviarEstado(estadoApp);
                                tabelaApp.mudaEstado(DadosR, true);
                                teste = false;
                            } catch (IOException ex) {
                                System.out.println("Problemas ao enviar estado procurando nova aplicação..");
                            }

                        } else {
                            System.out.println("Não foi possivel iniciar uma aplicação");
                            teste = false;
                        }
                    }
                    break;
                case 6:
                    break;
                case 7://quando uma app é iniciada a app abre um conexão é espera para receber um estado
                    registro.setTipoId(this.tipoIdApp);
                    registro.setUnicoId(this.unicoIDApp);
                    tabelaApp.addSocketRegistro(registro, app);
                    break;
                case 8://uma app envia seu estado gradualmente para o middleware então o middleware repassa para o servidor de estado
               
                    MiddlewareHandoffAtivo handoffAtivo = new MiddlewareHandoffAtivo();
                    handoffAtivo.deserializar(inf);
                    registro.setTipoId(this.tipoIdApp);
                    registro.setUnicoId(this.unicoIDApp);

                    if (!tabelaApp.verificaRegistro(registro)) {
                        System.out.println("app não cadastrado");
                    } else {
                        handoffAtivo.eHandoff(this.tipoIdApp, this.unicoIDApp, this.estadoApp);

                    }
                    break;
            }

        } catch (IOException ex) {
            System.out.println("Erro de Conexão " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro ao receber informações" + ex.getMessage());
        } catch (InterruptedException ex) {
            System.out.println("Problemas com a thread" + ex.getMessage());
        }

    }


    /*este metodo deserializa a informação recebida em 3 partes a primeira parte da informação ,
    identifica o tipo da informação(se veio de uma aplicação um middleware ou do servidor de estados),
    a segudna parte representa os dados de uma aplicação e a terceira parte representa o estado de uma aplicação(este estado existira apenas no caso da informação vier de uma aplicação ou do servidor de estados).
     */
    private void deserializar(String linha) {
        String vetor[] = linha.split("#");
        this.solicitacao = Integer.parseInt(vetor[0]);
        switch (this.solicitacao) {
            case 1:
                this.dadosApp = vetor[1];
                break;
            case 2:
            case 3:
                this.dadosApp = vetor[1];
                this.estadoApp = vetor[2];
                break;
            case 4:

                break;
            case 5:
                this.tipoIdApp = Integer.parseInt(vetor[1]);
                this.estadoApp = vetor[2];
                break;
            case 6:
            case 7:
                this.unicoIDApp = Integer.parseInt(vetor[1]);
                this.tipoIdApp = Integer.parseInt(vetor[2]);
                break;
            case 8:

                this.tipoIdApp = Integer.parseInt(vetor[1]);
                this.unicoIDApp = Integer.parseInt(vetor[2]);
                this.estadoApp = vetor[3];
                break;

        }

    }

}
