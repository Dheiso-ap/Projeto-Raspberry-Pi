package projetoraspberry.app;

import java.io.Serializable;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author dheiso
 */
//aplicação basica para servir de exemplo na realização de handoff 
public class AppContador extends Thread implements Serializable {

    private int valor;

    public AppContador(int valor) {
        this.valor = valor;
    }

    public int getValor() {

        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    @Override
    public void run() {
        int fleg = valor;
       // JFrame tela = new JFrame("valor");
        //JLabel lebel = new JLabel();
        
        //tela.add(lebel);
        //tela.setSize(300, 300);
        //tela.setVisible(true);
        System.out.println("Contando...");
        try {

            while (true) {
              //  lebel.setText("valor: " + valor);
                Thread.sleep(200);
                valor = valor + 1;
                //System.out.println(valor);
               // tela.repaint();
                //tela.validate();

            }

        } catch (Exception e) {
            System.out.println("Não deu.." + e.getMessage());
        }
    }
}
