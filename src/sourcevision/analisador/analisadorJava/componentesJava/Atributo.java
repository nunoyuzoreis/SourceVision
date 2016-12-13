package sourcevision.analisador.analisadorJava.componentesJava;

public class Atributo
{
	private String nome;
	private String tipo;
	
	private boolean					publi		= false;
	private boolean					protecte	= false;
	private boolean					privat		= false;
	private boolean					fina		= false;
	private boolean					stati		= false;
	
	public String getNome()
	{
		return nome;
	}
	public void setNome(String nome)
	{
		this.nome = nome;
	}
	public String getTipo()
	{
		return tipo;
	}
	public void setTipo(String tipo)
	{
		this.tipo = tipo;
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
	
	
}
