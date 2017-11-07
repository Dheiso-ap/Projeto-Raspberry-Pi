package projetoraspberry.Registrador;

import java.util.ArrayList;

public class RegistradorTabelaHash {

    private ArrayList<ArrayList<RegistradorRegistro>> tabela = new ArrayList<>();

    public RegistradorTabelaHash() {
        inicia();
    }

    //Este metodo inicia a tabela hash com seus devidos arrayList
    private void inicia() {
        for (int i = 0; i < 100; i++) {
            ArrayList<RegistradorRegistro> lista = new ArrayList<>();
            tabela.add(lista);
        }
    }

    //Este metodo representa a função hash responsvel pelo espalhamento dos registros 
    private int fHash(RegistradorRegistro registro) {

        return registro.getTipoId() % 100;
    }

    //metodo responsavel por adicionar um novo registro a tabela
    public void add(RegistradorRegistro registro) {

        int posicao = fHash(registro);

        tabela.get(posicao).add(registro);

        mostrarRegistros(posicao);

    }

    public void remover(RegistradorRegistro rgt) {
        
                int tipo = fHash(rgt);

        for (RegistradorRegistro registro : tabela.get(tipo)) {//para cada registro 

            if (registro.getUnicoId() == rgt.getUnicoId()) {//verifica se é um registro do mesmo tipo mas não o mesmo

                   tabela.get(tipo).remove(registro);
                   break;
            }

        }
        
    }

    //metodo responsavel por realizar uma busca nos registros afim de encontrar um registros semelhantes para informar a um middleware um local para realizar o handoff
    public RegistradorRegistro buscaPar(RegistradorRegistro rgt) {

        int tipo = fHash(rgt);

        for (RegistradorRegistro registro : tabela.get(tipo)) {//para cada registro 

            if (registro.getUnicoId() != rgt.getUnicoId()) {//verifica se é um registro do mesmo tipo mas não o mesmo

                    if (registro.isEstado()) {
                        return registro;
                    }

            }

        }
        return null;
    }

    public void mudaEstado(int tipo, int unicoId, boolean validade) {

        for (RegistradorRegistro registro : tabela.get(tipo)) {//para cada registro 

            if (registro.getUnicoId() == unicoId) {

                    registro.setEstado(validade);
            }

        }

    }

    private void mostrarRegistros(int posicao) {

        for (RegistradorRegistro registro : tabela.get(posicao)) {

            System.out.println(registro.getIp());
            System.out.println(registro.getPorta());
            System.out.println(registro.getTipoId());
            System.out.println(registro.getUnicoId());
            System.out.println(registro.isEstado());
            System.out.println("______________________");
        }
    }

}
