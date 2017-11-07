package ServidorEstados;

/**
 *
 * @author dheiso
 */
import java.util.ArrayList;

public class ServidorRegistro {

    private ArrayList<ServidorDadosMD> istanciasMD = new ArrayList<>();

    public void addMD(ServidorDadosMD MD) {

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
    
    public void removerMD(ServidorDadosMD MD) {
        
        
        
        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
                this.istanciasMD.remove(registro);
                break;
            }
        }

    }

    public void addApp(ServidorDadosMD MD, ServidorDadosApp app) {
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

    public void removerApp(ServidorDadosMD MD, ServidorDadosApp app) {
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
    
    public void mudarValidade(ServidorDadosMD MD, boolean validade) {
    

        for (ServidorDadosMD registro : istanciasMD) {
            if (registro.getUnicoId() == MD.getUnicoId()) {
           
                
                registro.setValidade(validade);
                break;
            }
        }

    }

    public ArrayList<ServidorDadosMD> getIstanciasMD() {
        return istanciasMD;
    }

    
    public void mostrarRegistros(){
        for(ServidorDadosMD registro : istanciasMD){
            System.out.println(registro.getUnicoId());
        }
    }

}
