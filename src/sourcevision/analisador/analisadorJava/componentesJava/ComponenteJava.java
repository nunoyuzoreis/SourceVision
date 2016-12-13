package sourcevision.analisador.analisadorJava.componentesJava;

import java.util.ArrayList;
import java.util.HashMap;

import org.eclipse.core.runtime.IPath;

public class ComponenteJava
{
	private String						TIPO		= "generico";

	private String						nome;
	private String						nomeQualificado;
	private int linhasDeCodigo;

	private boolean						publi		= false;
	private boolean						protecte	= false;
	private boolean						privat		= false;
	private boolean						abstac		= false;
	private boolean						fina		= false;
	private boolean						stati		= false;

	private String						nomeExtende;
	private ComponenteJava					extende;

	private ArrayList<String>			nomesImplementa;
	private ArrayList<ComponenteJava>		implementa;

	private ArrayList<Atributo>			atributos;

	private ArrayList<Metodo>			metodos;

	private ArrayList<ComponenteJava>		dependencias;

	private ArrayList<String>			importacoes;

	private HashMap<String, ComponenteJava>	componentesAninhados;

	private Pacote						pacote;
	
	private IPath caminho;
	
	private int qtdReferencias;

	public ComponenteJava(String nome)
	{
		this.nome = nome;

		//this.nomeExtende = "";

		this.nomesImplementa = new ArrayList<>();

		this.implementa = new ArrayList<>();

		this.atributos = new ArrayList<>();

		this.metodos = new ArrayList<>();

		this.dependencias = new ArrayList<>();

		this.importacoes = new ArrayList<>();

		this.componentesAninhados = new HashMap<>();
		
		this.linhasDeCodigo = 0;
		
		this.setQtdReferencias(0);
	}

	public ComponenteJava getComponenteAninhado(String nome)
	{
		ComponenteJava c = componentesAninhados.get(nome);
		if(c == null)
		{
			c = new ComponenteJava(nome);
			this.componentesAninhados.put(nome, c);
		}
		return c;
	}

	public void addImplementacao(ComponenteJava c)
	{
		if(c != null) this.implementa.add(c);
	}

	public void addAtributo(Atributo a)
	{
		if(a != null) this.atributos.add(a);
	}

	public void addMetodo(Metodo m)
	{
		if(m != null) this.metodos.add(m);
	}

	public void addDependencia(ComponenteJava c)
	{
		if(c != null) this.dependencias.add(c);
	}

	public void addImportacao(String s)
	{
		this.importacoes.add(s);
	}

	public void addNomeImplementacao(String s)
	{
		this.nomesImplementa.add(s);
	}

	public String getNome()
	{
		return nome;
	}

	public void setNome(String nome)
	{
		this.nome = nome;
	}

	public String getNomeQualificado()
	{
		return nomeQualificado;
	}

	public void setNomeQualificado(String nomeQualificado)
	{
		this.nomeQualificado = nomeQualificado;
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

	public ComponenteJava getExtende()
	{
		return extende;
	}

	public void setExtende(ComponenteJava extende)
	{
		this.extende = extende;
	}

	public ArrayList<ComponenteJava> getImplementa()
	{
		return implementa;
	}

	public void setImplementa(ArrayList<ComponenteJava> implementa)
	{
		this.implementa = implementa;
	}

	public ArrayList<Atributo> getAtributos()
	{
		return atributos;
	}

	public void setAtributos(ArrayList<Atributo> atributos)
	{
		this.atributos = atributos;
	}

	public ArrayList<Metodo> getMetodos()
	{
		return metodos;
	}

	public void setMetodos(ArrayList<Metodo> metodos)
	{
		this.metodos = metodos;
	}

	public ArrayList<ComponenteJava> getDependencias()
	{
		return dependencias;
	}

	public void setDependencias(ArrayList<ComponenteJava> dependencias)
	{
		this.dependencias = dependencias;
	}

	public ArrayList<String> getImportacoes()
	{
		return importacoes;
	}

	public void setImportacoes(ArrayList<String> importacoes)
	{
		this.importacoes = importacoes;
	}

	public HashMap<String, ComponenteJava> getComponentesAninhados()
	{
		return componentesAninhados;
	}

	public void setComponentesAninhados(HashMap<String, ComponenteJava> componentesAninhados)
	{
		this.componentesAninhados = componentesAninhados;
	}

	public String getTIPO()
	{
		return TIPO;
	}

	public void setTIPO(String tIPO)
	{
		TIPO = tIPO;
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

	public Pacote getPacote()
	{
		return pacote;
	}

	public void setPacote(Pacote pacote)
	{
		this.pacote = pacote;
	}

	public int getLinhasDeCodigo()
	{
		return linhasDeCodigo;
	}

	public void setLinhasDeCodigo(int linhasDeCodigo)
	{
		this.linhasDeCodigo = linhasDeCodigo;
	}

	public IPath getCaminho()
	{
		return caminho;
	}

	public void setCaminho(IPath caminho)
	{
		this.caminho = caminho;
	}
	
	public String atributosMetodosToString()
	{
		String retorno = "";
		for(Atributo a : atributos)
		{
			if(a.isPubli()) retorno += "public ";
			else if(a.isProtecte()) retorno += "protected ";
			else if(a.isPrivat()) retorno += "private ";
			
			if(a.isFina()) retorno += "final ";
			if(a.isStati()) retorno += "static ";
			
			retorno += a.getTipo() + " " + a.getNome()+"<br>";
		}
		retorno += "<br>";
		
		for(Metodo m : metodos)
		{
			retorno += m.getAssinatura() + "<br>";
		}
		
		return retorno;
	}
	
	public int getQtdComponentesAninhados()
	{
		int qtd = 0;
		for(ComponenteJava c : componentesAninhados.values())
		{
			if(c != null) qtd++;
		}
		return qtd;
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
