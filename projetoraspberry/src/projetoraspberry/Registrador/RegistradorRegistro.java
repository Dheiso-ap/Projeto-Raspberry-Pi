package projetoraspberry.Registrador;

public class RegistradorRegistro {

    private int tipoId;
    private int unicoId = 0;
    private int porta;//porta principal do middlware onde ele atende solicitações 
    private String ip;
    private boolean condicao;//define se o middleware esta ativo ou não

    public int getPorta() {
        return porta;
    }

    public void setPorta(int porta) {
        this.porta = porta;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public int getTipoId() {
        return tipoId;
    }

    public void setTipoId(int tipoId) {
        this.tipoId = tipoId;
    }

    public boolean isEstado() {
        return condicao;
    }

    public void setEstado(boolean estado) {
        this.condicao = estado;

    }

    public int getUnicoId() {
        return unicoId;
    }

    public void setUnicoId(int unicoId) {
        this.unicoId = unicoId;
    }

    //este metodo recebe uma string e separa os campos contidos nela, depois atribui a cada respectivo atributo
    public void deserializar(String linha) {
        String vetor[] = linha.split(";");
       
                this.unicoId = Integer.parseInt(vetor[0]);
                this.tipoId = Integer.parseInt(vetor[1]);
                this.porta = Integer.parseInt(vetor[2]);
                this.ip = vetor[3];
                this.condicao = Boolean.parseBoolean(vetor[4]);
            

        
    }

}
