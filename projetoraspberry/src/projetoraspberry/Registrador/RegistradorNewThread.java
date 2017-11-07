package projetoraspberry.Registrador;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class RegistradorNewThread extends Thread {

    private int tipoSolicitacao;
    private Socket cliente;
    static RegistradorTabelaHash tabela;//atributo estatico para que todos os objetos instanciados dessa classe compartilhem desse atributo
    private int unicoId;
    private int tipoId;
    private boolean validade;
    private int porta;
    RegistradorRegistro registro = new RegistradorRegistro();
    private String dados;
    private String dadosMD;

    public RegistradorNewThread(Socket cliente, int unicoId) {
        this.cliente = cliente;
        this.unicoId = unicoId;
        randomPorta();

    }

    @Override
    public void run() {

        try {
            //recebe da aplicação suas informações de cadastro e adicina à tabela

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
            dados = (String) entrada.readObject();
            deserializar(dados);
            RegistradorResponde responde = new RegistradorResponde(cliente);

            switch (tipoSolicitacao) {

                case 1://este caso acontece quando um middleware solicita se registrar
                    registro.deserializar(dadosMD);
                    registro.setUnicoId(unicoId);
                    tabela.add(registro);
                    responde.informaUnicoId(registro);
                    break;

                case 2://este caso corre quando um middleware solicita informações de outro middleware

                    registro.setTipoId(tipoId);
                    registro.setUnicoId(unicoId);
                    RegistradorRegistro rtg = new RegistradorRegistro();

                    rtg = tabela.buscaPar(registro);//procura um par para um middleware

                    responde.informaPar(rtg);

                    break;

                case 3://este caso ocorre quando um middleware solicitar mudar seu estado no registro, ou quando o servidor de estados solicita mudar o estado de um middleware
                    tabela.mudaEstado(this.tipoId, this.unicoId, this.validade);
                    break;
                case 4:
                    registro.setTipoId(tipoId);
                    registro.setUnicoId(unicoId);
                    tabela.remover(registro);
                    break;

                default:
                    System.out.println("solcitação desconhecida");
                    break;
            }

        } catch (IOException ex) {
            System.out.println("Erro na conexão " + ex.getMessage());
        } catch (ClassNotFoundException ex) {
            System.out.println("Erro ao receber informações");
        }

    }

    //este metodo serve para verificar se a aplicação realizou o handoff com sucesso, para assim poder marcar os valores pares dos registros das aplicações como "true"
    private int pergunta() {
        int resposta = 0;
        try {

            ServerSocket servidor = new ServerSocket(this.porta);

            Socket cliente = servidor.accept();

            ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());

            resposta = entrada.readInt();

            entrada.close();
            cliente.close();
            servidor.close();

        } catch (IOException ex) {
            System.out.println("Erro de Conexão " + ex.getMessage());
        }

        return resposta;

    }

    private void randomPorta() {
        Random gerador = new Random();

        this.porta = gerador.nextInt(48127) + 1024;

    }

    //este metodo recebe uma string e separa os campos contidos nela, depois atribui a cada respectivo atributo
    public void deserializar(String linha) {
        String vetor[] = linha.split("#");
        this.tipoSolicitacao = Integer.parseInt(vetor[0]);
        switch (this.tipoSolicitacao) {
            case 1:
                this.dadosMD = vetor[1];
                break;
            case 2:

                this.unicoId = Integer.parseInt(vetor[1]);
                this.tipoId = Integer.parseInt(vetor[2]);
                break;
            case 3:
                this.unicoId = Integer.parseInt(vetor[1]);
                this.tipoId = Integer.parseInt(vetor[2]);
                this.validade = Boolean.parseBoolean(vetor[3]);
                break;
            case 4:
                this.unicoId = Integer.parseInt(vetor[1]);
                this.tipoId = Integer.parseInt(vetor[2]);
                break;
        }
    }

}
