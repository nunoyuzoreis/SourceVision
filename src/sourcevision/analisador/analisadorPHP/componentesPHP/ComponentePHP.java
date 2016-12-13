package sourcevision.analisador.analisadorPHP.componentesPHP;

import java.util.ArrayList;

public class ComponentePHP
{
    private String TIPO;
    
    private String nome;
    
    private boolean abstac = false;
    private boolean fina = false;
    
    private ArrayList<Funcao> funcoes;
    
    private String nomeExtende;
    
    private ComponentePHP extende;
    
    private ArrayList<ComponentePHP> implementa;
    
    private ArrayList<String> nomesImplementa;
    
    private ArrayList<ComponentePHP> dependenciasComponente;
    
    private Arquivo arquivoPai;
    
    private int qtdReferencias;

    
	public ComponentePHP(String nome)
    {
        this.TIPO = "generico";
        this.nome = nome;
        
        funcoes = new ArrayList<>();
        
        implementa = new ArrayList<>();
        
        nomesImplementa = new ArrayList<>();
        
        dependenciasComponente =  new ArrayList<>();
        
        this.setQtdReferencias(0);
    }
    
    public void addFuncao(Funcao funcao)
    {
        this.funcoes.add(funcao);
    }
    
    public void addImplementacao(ComponentePHP implementado)
    {
        this.implementa.add(implementado);
    }
    
    public void addNomeImplementa(String nome)
    {
    	this.nomesImplementa.add(nome);
    }
    
    public void addDependencia(ComponentePHP dependencia)
    {
        this.dependenciasComponente.add(dependencia);
    }
    
    /**
     * @return the TIPO
     */
    public String getTIPO()
    {
        return TIPO;
    }

    /**
     * @param TIPO the TIPO to set
     */
    public void setTIPO(String TIPO)
    {
        this.TIPO = TIPO;
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
     * @return the abstac
     */
    public boolean isAbstac()
    {
        return abstac;
    }

    /**
     * @param abstac the abstac to set
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
     * @param fina the fina to set
     */
    public void setFina(boolean fina)
    {
        this.fina = fina;
    }

    /**
     * @return the funcoes
     */
    public ArrayList<Funcao> getFuncoes()
    {
        return funcoes;
    }

    /**
     * @param funcoes the funcoes to set
     */
    public void setFuncoes(
            ArrayList<Funcao> funcoes)
    {
        this.funcoes = funcoes;
    }

    /**
     * @return the extende
     */
    public ComponentePHP getExtende()
    {
        return extende;
    }

    /**
     * @param extende the extende to set
     */
    public void setExtende(ComponentePHP extende)
    {
        this.extende = extende;
    }

    /**
     * @return the implementa
     */
    public ArrayList<ComponentePHP> getImplementa()
    {
        return implementa;
    }

    /**
     * @param implementa the implementa to set
     */
    public void setImplementa(
            ArrayList<ComponentePHP> implementa)
    {
        this.implementa = implementa;
    }

    /**
     * @return the dependenciasComponente
     */
    public ArrayList<ComponentePHP> getDependenciasComponente()
    {
        return dependenciasComponente;
    }

    /**
     * @param dependenciasComponente the dependenciasComponente to set
     */
    public void setDependenciasComponente(
            ArrayList<ComponentePHP> dependenciasComponente)
    {
        this.dependenciasComponente = dependenciasComponente;
    }

	public String getNomeExtende()
	{
		return nomeExtende;
	}

	public void setNomeExtende(String nomeExtende)
	{
		this.nomeExtende = nomeExtende;
	}

	public ArrayList<String> getNomesImplementa()
	{
		return nomesImplementa;
	}

	public void setNomesImplementa(ArrayList<String> nomesImplementa)
	{
		this.nomesImplementa = nomesImplementa;
	}

	public Arquivo getArquivoPai()
	{
		return arquivoPai;
	}

	public void setArquivoPai(Arquivo arquivoPai)
	{
		this.arquivoPai = arquivoPai;
	}
	
	public String atributosMetodosToString()
	{
		String retorno = "";
//		for(Atributo a : atributos)
//		{
//			if(a.isPubli()) retorno += "public ";
//			else if(a.isProtecte()) retorno += "protected ";
//			else if(a.isPrivat()) retorno += "private ";
//			
//			if(a.isFina()) retorno += "final ";
//			if(a.isStati()) retorno += "static ";
//			
//			retorno += a.getTipo() + " " + a.getNome()+"<br>";
//		}
//		retorno += "<br>";
		
		for(Funcao f : funcoes)
		{
			retorno += f.getAssinatura() + "<br>";
		}
		
		return retorno;
	}
	
	public void addReferencia()
	{
		this.setQtdReferencias(this.getQtdReferencias() + 1);
	}

	public int getQtdReferencias()
	{
		return qtdReferencias;
	}

	public void setQtdReferencias(int qtdReferencias)
	{
		this.qtdReferencias = qtdReferencias;
	}
    
    
}
