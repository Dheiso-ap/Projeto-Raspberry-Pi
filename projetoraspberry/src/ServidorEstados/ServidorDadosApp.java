
package ServidorEstados;

/**
 *
 * @author Dheiso
 */
public class ServidorDadosApp {
    
    private String estado;
    private int tipoId;
    private int unicoId;

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public int getUnicoId() {
        return unicoId;
    }

    public void setUnicoId(int unicoId) {
        this.unicoId = unicoId;
    }
    
    
    public void deserializar(String linha){
        String vetor[] = linha.split(";");
        
        this.tipoId = Integer.parseInt(vetor[0]);
        this.unicoId = Integer.parseInt(vetor[1]);
        this.estado = vetor[2];
    }
    
    
}
