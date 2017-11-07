package projetoraspberry.Middleware;

import java.net.Socket;
import java.util.ArrayList;

public class MiddlewareTabelaHash {

    private ArrayList<ArrayList<MiddlewareDadosApp>> tabela = new ArrayList<>();

    public MiddlewareTabelaHash() {
        inicia();
    }

    //Este metodo inicia a tabela hash com seus devidos arrayList
    private void inicia() {
        for (int i = 0; i < 100; i++) {
            ArrayList<MiddlewareDadosApp> lista = new ArrayList<>();
            tabela.add(lista);
        }
    }

    //Este metodo representa a função hash responsvel pelo espalhamento dos registros 
    private int fHash(MiddlewareDadosApp dados) {

        return dados.getTipoId() % 100;
    }

    //metodo responsavel por adicionar um novo registro a tabela
    public void add(MiddlewareDadosApp dados) {

        int posicao = fHash(dados);

        tabela.get(posicao).add(dados);
        
    }

    public void remover(int tipo, int unicoId) {
        for (MiddlewareDadosApp dados : tabela.get(tipo)) {

            if (dados.getUnicoId() == unicoId) {

                tabela.remove(dados);

            }

        }
    }

    // procura por um determinado registro
    public MiddlewareDadosApp buscaRegistro(MiddlewareDadosApp registro) {

        int tipo = fHash(registro);
        for (MiddlewareDadosApp dados : tabela.get(tipo)) {
            if (dados.isCondicao() == false) {
                
                return dados;

            }

        }
        return null;
    }
    
    public boolean verificaRegistro(MiddlewareDadosApp registro) {

        int tipo = fHash(registro);

        for (MiddlewareDadosApp dados : tabela.get(tipo)) {

            if (dados.getUnicoId() == registro.getUnicoId()) {
                
                return true;

            }

        }
        return false;
    }

    public boolean addSocketRegistro(MiddlewareDadosApp registro, Socket cliente) {

        int tipo = fHash(registro);

        for (MiddlewareDadosApp dados : tabela.get(tipo)) {

            if (dados.getUnicoId() == registro.getUnicoId()) {
                    dados.setCliente(cliente);
                    return true;
            }

        }
        return false;
    }

    //este metodo muda a condição do registro de uma aplicação, onde esta aplicação pode estar inativa esperando handoff ou ativa e operando
    public void mudaEstado(MiddlewareDadosApp registro,boolean validade) {

        int tipo = fHash(registro);

        for (MiddlewareDadosApp dados : tabela.get(tipo)) {//para cada registro 

            if (dados.getUnicoId() == registro.getUnicoId()) {

                dados.setCondicao(validade);
                
                break;
            }

        }

    }

    public void mostrarRegistros(int posicao) {

        for (MiddlewareDadosApp dados : tabela.get(posicao)) {

            System.out.println(dados.getTipoId());
            System.out.println(dados.getUnicoId());
            System.out.println(dados.isCondicao());
            System.out.println("______________________");
        }
    }

}
