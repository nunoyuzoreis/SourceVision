package sourcevision.analisador.analisadorJava.componentesJava;

import java.util.ArrayList;

public class Metodo {
	
	private String nome;
    
	private String retorno;
    
	private ArrayList<String> tipoParametros;
	
	private ArrayList<String> nomeParametros;
	
	private ArrayList<String> tipoDeclarados;
	
	private String assinatura;
	
    private boolean publi = false;
    private boolean protecte = false;
    private boolean privat = false;
    private boolean abstac = false;
    private boolean fina = false;
    private boolean stati = false;
    
    public Metodo()
    {
    	this.tipoParametros = new ArrayList<>();
    	this.nomeParametros = new ArrayList<>();
    	this.tipoDeclarados = new ArrayList<>();
    }
    
    public void addTipoParametro(String s)
    {
    	this.tipoParametros.add(s);
    }
    
    public void addNomeParametro(String s)
    {
    	this.nomeParametros.add(s);
    }
    
    public void addTipoDeclarado(String s)
    {
    	this.tipoDeclarados.add(s);
    }
    
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getRetorno()
	{
		return retorno;
	}
	public void setRetorno(String retorno)
	{
		this.retorno = retorno;
	}
	public ArrayList<String>  getTipoParametros()
	{
		return tipoParametros;
	}
	public void setTipoParametros(ArrayList<String>  tipoParametros)
	{
		this.tipoParametros = tipoParametros;
	}
	public ArrayList<String>  getNomeParametros()
	{
		return nomeParametros;
	}
	public void setNomeParametros(ArrayList<String>  nomeParametros)
	{
		this.nomeParametros = nomeParametros;
	}
	public String getAssinatura()
	{
		return assinatura;
	}
	public void setAssinatura(String assinatura)
	{
		this.assinatura = assinatura;
	}
	public boolean isPubli()
	{
		return publi;
	}
	public void setPubli(boolean publi)
	{
		this.publi = publi;
	}
	public boolean isProtecte()
	{
		return protecte;
	}
	public void setProtecte(boolean protecte)
	{
		this.protecte = protecte;
	}
	public boolean isPrivat()
	{
		return privat;
	}
	public void setPrivat(boolean privat)
	{
		this.privat = privat;
	}
	public boolean isAbstac()
	{
		return abstac;
	}
	public void setAbstac(boolean abstac)
	{
		this.abstac = abstac;
	}
	public boolean isFina()
	{
		return fina;
	}
	public void setFina(boolean fina)
	{
		this.fina = fina;
	}
	public boolean isStati()
	{
		return stati;
	}
	public void setStati(boolean stati)
	{
		this.stati = stati;
	}

	public ArrayList<String> getTipoDeclarados()
	{
		return tipoDeclarados;
	}

	public void setTipoDeclarados(ArrayList<String> tipoDeclarados)
	{
		this.tipoDeclarados = tipoDeclarados;
	}
	
}
