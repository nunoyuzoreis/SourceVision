package sourcevision.analisador.analisadorPHP.componentesPHP;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author nunoreis
 */
public class Funcao
{
	private String	nome;

	private String assinatura;
	
	private boolean	publi		= false;
	private boolean	protecte	= false;
	private boolean	privat		= false;
	private boolean	abstac		= false;
	private boolean	fina		= false;
	private boolean	stati		= false;

	ArrayList<String> nomesParametros;
	
	HashMap<String, String> tiposParametros;
	
	ArrayList<String> tiposInstanciados;
	
	private String	retorno;

	public Funcao()
	{
		this.nomesParametros = new ArrayList<>();
		this.tiposParametros = new HashMap<>();
		this.tiposInstanciados = new ArrayList<>();
	}
	
	public void addParametro(String nomeParametro)
	{
		this.nomesParametros.add(nomeParametro);
	}

	public void addTipoParametro(String nomeParametro, String tipoParametro)
	{
		this.tiposParametros.put(nomeParametro, tipoParametro);
	}
	
	public void addTipoInstanciado(String tipo)
	{
		this.tiposInstanciados.add(tipo);
	}
	
	/**
	 * @return the nome
	 */
	public String getNome()
	{
		return nome;
	}

	/**
	 * @param nome
	 *            the nome to set
	 */
	public void setNome(String nome)
	{
		this.nome = nome;
	}

	/**
	 * @return the publi
	 */
	public boolean isPubli()
	{
		return publi;
	}

	/**
	 * @param publi
	 *            the publi to set
	 */
	public void setPubli(boolean publi)
	{
		this.publi = publi;
	}

	/**
	 * @return the protecte
	 */
	public boolean isProtecte()
	{
		return protecte;
	}

	/**
	 * @param protecte
	 *            the protecte to set
	 */
	public void setProtecte(boolean protecte)
	{
		this.protecte = protecte;
	}

	/**
	 * @return the privat
	 */
	public boolean isPrivat()
	{
		return privat;
	}

	/**
	 * @param privat
	 *            the privat to set
	 */
	public void setPrivat(boolean privat)
	{
		this.privat = privat;
	}

	/**
	 * @return the abstac
	 */
	public boolean isAbstac()
	{
		return abstac;
	}

	/**
	 * @param abstac
	 *            the abstac to set
	 */
	public void setAbstac(boolean abstac)
	{
		this.abstac = abstac;
	}

	/**
	 * @return the fina
	 */
	public boolean isFina()
	{
		return fina;
	}

	/**
	 * @param fina
	 *            the fina to set
	 */
	public void setFina(boolean fina)
	{
		this.fina = fina;
	}

	/**
	 * @return the stati
	 */
	public boolean isStati()
	{
		return stati;
	}

	/**
	 * @param stati
	 *            the stati to set
	 */
	public void setStati(boolean stati)
	{
		this.stati = stati;
	}

	public String getRetorno()
	{
		return retorno;
	}

	public void setRetorno(String retorno)
	{
		this.retorno = retorno;
	}

	public String getAssinatura()
	{
		return assinatura;
	}

	public void setAssinatura(String assinatura)
	{
		this.assinatura = assinatura;
	}

	public ArrayList<String> getNomesParametros()
	{
		return nomesParametros;
	}

	public void setNomesParametros(ArrayList<String> nomesParametros)
	{
		this.nomesParametros = nomesParametros;
	}

	public HashMap<String, String> getTiposParametros()
	{
		return tiposParametros;
	}

	public void setTiposParametros(HashMap<String, String> tiposParametros)
	{
		this.tiposParametros = tiposParametros;
	}

	public ArrayList<String> getTiposInstanciados()
	{
		return tiposInstanciados;
	}

	public void setTiposInstanciados(ArrayList<String> tiposInstanciados)
	{
		this.tiposInstanciados = tiposInstanciados;
	}
	
	

}
