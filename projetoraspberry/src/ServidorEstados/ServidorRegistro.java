package ServidorEstados;

/**
 *
 * @author dheiso
 */
import java.util.ArrayList;
/*esta classe é usada para instanciar um objeto responsável por guardar os registros de cada instancia de middleware,
e também registros das aplicações que são intermediadas 
*/
public class ServidorRegistro {

    private ArrayList<ServidorDadosMD> istanciasMD = new ArrayList<>();//lista de instancias do middleware

    public void addMD(ServidorDadosMD MD) {//adiciona uma instancia do middleware, verifica se a instancia j está registrada

        boolean test = true;

        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
                test = false;
            }
        }

        if (test) {
            MD.atualizaTempoDeAcesso();
            this.istanciasMD.add(MD);
        }
    }
    
    public void removerMD(ServidorDadosMD MD) {//remove uma instancia do middlware 
        
        
        
        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
                this.istanciasMD.remove(registro);
                break;
            }
        }

    }

    public void addApp(ServidorDadosMD MD, ServidorDadosApp app) {//adiciona para um middleware um regitro referente a uma aplicação
        boolean test = false;

        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
                test = registro.atualizaApp(app);
                
                if (!test) {
                    registro.addApp(app);
                }

                registro.atualizaTempoDeAcesso();
                test = true;
                break;
            }
        }

        if (!test) {
            System.out.println("Middleware não cadastrado");
        }
    }

    public void removerApp(ServidorDadosMD MD, ServidorDadosApp app) {//remove o registro referente a uma aplicação
        boolean test = false;

        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
                test = registro.removeApp(app);

                if (!test) {
                    System.out.println("App não registrado");
                }

                registro.atualizaTempoDeAcesso();
                test = true;
                break;
            }
        }

        if (!test) {
            System.out.println("Middleware não cadastrado");
        }
    }
    
    public void mudarValidade(ServidorDadosMD MD, boolean validade) {//atualiza o tempo em que a instancia do middleware se comunicou com o servidor de estados, para saber se esta ativa ou não
    

        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
           
                
                registro.setValidade(validade);
                break;
            }
        }

    }

    public ArrayList<ServidorDadosMD> getIstanciasMD() {//retorna uma lista com as instancias do middleware que estão registradas
        return istanciasMD;
    }

    
    public void mostrarRegistros(){
        for(ServidorDadosMD registro : istanciasMD){
            System.out.println(registro.getUnicoId());
        }
    }

}
