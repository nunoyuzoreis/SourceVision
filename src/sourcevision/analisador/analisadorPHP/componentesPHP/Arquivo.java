package sourcevision.analisador.analisadorPHP.componentesPHP;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;

public class Arquivo
{
    private String nome;
    
    private int linhasDeCodigo;
    
    public int getLinhasDeCodigo()
	{
		return linhasDeCodigo;
	}

	public void setLinhasDeCodigo(int linhasDeCodigo)
	{
		this.linhasDeCodigo = linhasDeCodigo;
	}

	private ArrayList<String> inclusoes;
    
    private String namespace = null;
    
    private HashMap<String, String> uses;
    
    private HashMap<String, String> aliases;
    
    private ArrayList<String> somenteUses;
    
    private HashMap<String, ComponentePHP> componentes;
    
    private boolean autoLoader = false;

    private Pasta pastaPai;
    
    private IPath caminho;
    
    public IPath getCaminho()
	{
		return caminho;
	}

	public void setCaminho(IPath caminho)
	{
		this.caminho = caminho;
	}

	public Arquivo(String nome)
    {
        this.nome = nome;
        
        this.inclusoes = new ArrayList<>();
        
        this.uses = new HashMap<>();
        
        this.aliases = new HashMap<>();
        
        this.somenteUses = new ArrayList<>();
        
        this.componentes = new HashMap<>();
    }
    
    public void addInclusao(String inclusao)
    {
        this.inclusoes.add(inclusao);
    }
    
    public void addUses(String use, String alias)
    {
        uses.put(alias, use);
    }
    
    public void addAlias(String alias, String use)
    {
    	aliases.put(use, alias);
    }
    
    public void addSomenteUse(String use)
    {
    	this.somenteUses.add(use);
    }
    
    public String getUses(String alias)
    {
        String use = uses.get(alias);
        return use;
    }
    
    public String getAlias(String use)
    {
    	String alias = aliases.get(use);
    	return alias;
    }
    
    public ComponentePHP getComponentePHP(String nome)
    {
        ComponentePHP componente = componentes.get(nome);
        if(componente == null)
        {
            componente = new ComponentePHP(nome);
            this.componentes.put(nome, componente);
        }
        return componente;
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

    /**
     * @return the inclusoes
     */
    public ArrayList<String> getInclusoes()
    {
        return inclusoes;
    }

    /**
     * @param inclusoes the inclusoes to set
     */
    public void setInclusoes(ArrayList<String> inclusoes)
    {
        this.inclusoes = inclusoes;
    }

    /**
     * @return the namespace
     */
    public String getNamespace()
    {
        return namespace;
    }

    /**
     * @param namespace the namespace to set
     */
    public void setNamespace(String namespace)
    {
        this.namespace = namespace;
    }

    /**
     * @return the uses
     */
    public HashMap<String, String> getUses()
    {
        return uses;
    }

    /**
     * @param uses the uses to set
     */
    public void setUses(HashMap<String, String> uses)
    {
        this.uses = uses;
    }

    public HashMap<String, String> getAliases()
	{
		return aliases;
	}

	public void setAliases(HashMap<String, String> aliases)
	{
		this.aliases = aliases;
	}

	public HashMap<String, ComponentePHP> getComponentes()
	{
		return componentes;
	}

	public void setComponentes(HashMap<String, ComponentePHP> componentes)
	{
		this.componentes = componentes;
	}

	/**
     * @return the autoLoader
     */
    public boolean isAutoLoader()
    {
        return autoLoader;
    }

    /**
     * @param autoLoader the autoLoader to set
     */
    public void setAutoLoader(boolean autoLoader)
    {
        this.autoLoader = autoLoader;
    }

	public Pasta getPastaPai()
	{
		return pastaPai;
	}

	public void setPastaPai(Pasta pastaPai)
	{
		this.pastaPai = pastaPai;
	}

	public ArrayList<String> getSomenteUses()
	{
		return somenteUses;
	}

	public void setSomenteUses(ArrayList<String> somenteUses)
	{
		this.somenteUses = somenteUses;
	}
    
	
	
}
