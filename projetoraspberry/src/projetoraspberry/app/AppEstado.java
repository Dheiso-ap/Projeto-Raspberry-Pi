/*Esta clase deve conter todos os atribusot relevantes ao estado do objeto, proveniente de qualquer classe da plicação
*/
package projetoraspberry.app;

/**
 *
 * @author Dheiso
 */
public class AppEstado {
    private int contadorValor;

    public int getContadorValor() {
        return contadorValor;
    }

    public void setContadorValor(int contadorValor) {
        this.contadorValor = contadorValor;
    }
    
    
    
    public void ObterValoresContador(AppContador contador){
        this.contadorValor = contador.getValor();
    }
    
    public void DevolverValoresContador(AppContador contador){
        contador.setValor(contadorValor);
    }
    
}
