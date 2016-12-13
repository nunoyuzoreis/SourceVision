package sourcevision.analisador.analisadorJava.componentesJava;

import java.util.HashMap;

public class Pacote
{
	private String tipoPacote;
	
	private String						nome;
	private String						nomeQualificado;

	private HashMap<String, ComponenteJava>	componenteJavas;
	private HashMap<String, Pacote>		pacotes;
	
	private Pacote pacotePai;

	public Pacote(String nome)
	{
		this.setTipoPacote("generico");
		this.nome = nome;

		componenteJavas = new HashMap<>();
		pacotes = new HashMap<>();
	}

	public ComponenteJava getComponente(String nome)
	{
		ComponenteJava c = componenteJavas.get(nome);
		if(c == null)
		{
			c = new ComponenteJava(nome);
			this.componenteJavas.put(nome, c);
		}
		return c;
	}
	
	public Pacote getPacote(String nome)
	{
		Pacote p = pacotes.get(nome);
		if(p == null)
		{
			p = new Pacote(nome);
			this.pacotes.put(nome, p);
		}
		return p;
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

	public HashMap<String, ComponenteJava> getComponentes()
	{
		return componenteJavas;
	}

	public void setComponentes(HashMap<String, ComponenteJava> componenteJavas)
	{
		this.componenteJavas = componenteJavas;
	}

	public HashMap<String, Pacote> getPacotes()
	{
		return pacotes;
	}

	public void setPacotes(HashMap<String, Pacote> pacotes)
	{
		this.pacotes = pacotes;
	}

	public Pacote getPacotePai()
	{
		return pacotePai;
	}

	public void setPacotePai(Pacote pacotePai)
	{
		this.pacotePai = pacotePai;
	}
	
	public String getNomeA() //metodo criado para retornar o nomeQualificado, mas quando nao existir, retornar o nome
	{
		if(nomeQualificado != null) return nomeQualificado;
		
		return nome;
	}

	public String getTipoPacote()
	{
		return tipoPacote;
	}

	public void setTipoPacote(String tipoPacote)
	{
		this.tipoPacote = tipoPacote;
	}

}
