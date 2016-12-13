package sourcevision.analisador.analisadorPHP.componentesPHP;

import java.util.HashMap;

public class Pasta
{
    private String nome;
    private String nomeCompleto;
    private HashMap<String, Pasta> pastas;
    private HashMap<String, Arquivo> arquivos;
    
    private Pasta pastaPai = null;

    public Pasta(String nome)
    {
        this.nome = nome;
        pastas = new HashMap<>();
        arquivos = new HashMap<>();
    }
    
    public Pasta getPasta(String nome)
    {
        Pasta pasta = pastas.get(nome);
        if(pasta == null)
        {
            pasta = new Pasta(nome);
            pastas.put(nome, pasta);
        }
        return pasta;
    }
    
    public Arquivo getArquivo(String nome)
    {
        Arquivo arquivo = arquivos.get(nome);
        if(arquivo == null)
        {
            arquivo = new Arquivo(nome);
            this.arquivos.put(nome, arquivo);
        }
        return arquivo;
    }
    
    /**
     * @return the pastas
     */
    public HashMap<String, Pasta> getPastas()
    {
        return pastas;
    }

    /**
     * @param pastas the pastas to set
     */
    public void setPastas(
            HashMap<String, Pasta> pastas)
    {
        this.pastas = pastas;
    }

    /**
     * @return the arquivos
     */
    public HashMap<String, Arquivo> getArquivos()
    {
        return arquivos;
    }

    /**
     * @param arquivos the arquivos to set
     */
    public void setArquivos(
            HashMap<String, Arquivo> arquivos)
    {
        this.arquivos = arquivos;
    }

    /**
     * @return the nome
     */
    public String getNome()
    {
        return nome;
    }

    /**
     * @param nome the nome to set
     */
    public void setNome(String nome)
    {
        this.nome = nome;
    }

	public Pasta getPastaPai()
	{
		return pastaPai;
	}

	public void setPastaPai(Pasta pastaPai)
	{
		this.pastaPai = pastaPai;
	}

	public String getNomeCompleto()
	{
		return nomeCompleto;
	}

	public void setNomeCompleto(String nomeCompleto)
	{
		this.nomeCompleto = nomeCompleto;
	}
    public String getNomeA()
    {
    	if(nomeCompleto == null) return nome;
    	if(nomeCompleto.isEmpty()) return nome;
    	return nomeCompleto;
    }
    
}
