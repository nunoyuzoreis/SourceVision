package sourcevision.analisador.analisadorJava.componentesJava;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sourcevision.analisador.ControleAnalisador;

public class ControleComponentesJava
{
	
	private HashMap<String, Pacote>	pacotes;
	private Pacote					pacotePadrao;
	
	private ArrayList<ComponenteJava> todosComponentes;
	
	private ArrayList<Pacote> todosPacotes;
	private ArrayList<Integer> coresPacotes;
	private ArrayList<String> caminhoComponentes;
	
	
	private int qtdClasses;
	private int qtdInterfaces;
	private int qtdLOC;
	
	/*VARIAVEIS VISUALIZACAO*/
	
	private int contID;
	
	private int dimensao;
	private int[][] matrizAdjacencia;
	private ArrayList<String> nomesComponentes;
	
	private String matriz;
	private String nomeProjeto;
	public int getQtdClasses()
	{
		return qtdClasses;
	}

	public void setQtdClasses(int qtdClasses)
	{
		this.qtdClasses = qtdClasses;
	}

	public int getQtdInterfaces()
	{
		return qtdInterfaces;
	}

	public void setQtdInterfaces(int qtdInterfaces)
	{
		this.qtdInterfaces = qtdInterfaces;
	}

	public int getQtdLOC()
	{
		return qtdLOC;
	}

	public void setQtdLOC(int qtdLOC)
	{
		this.qtdLOC = qtdLOC;
	}

	private ControleComponentesJava()
	{

	}

	public void inicializarAtributos()
	{
		pacotes = new HashMap<>();
		pacotePadrao = new Pacote("");
		pacotePadrao.setNomeQualificado("");
	}

	public Pacote getPacote(String nomePacote) //busca (ou cria) e retorna o pacote com o nomePacote
	{
		Pacote pacote = this.getPacotes().get(nomePacote);
		if(pacote == null)
		{
			pacote = new Pacote(nomePacote);
			this.pacotes.put(nomePacote, pacote);
		}
		return pacote;
	}
	
	public ArrayList<ComponenteJava> getTodosComponentes()
	{
		todosComponentes = new ArrayList<>();
		
		percorrePacote(pacotePadrao); 
		for(Pacote p : pacotes.values())
		{
			if(p != null)
			{
				percorrePacote(p);
			}
		}
		return todosComponentes;
	}
	
	private void percorrePacote(Pacote pacote) 
	{
		for(Pacote p : pacote.getPacotes().values()) //percorre todos os pacotes recursivamente
		{
			percorrePacote(p);
		}
		for(ComponenteJava c : pacote.getComponentes().values()) //percorre todos os componentes recursivamente
		{
			percorreComponente(c);
		}
	}
	
	private void percorreComponente(ComponenteJava componenteJava)
	{
		for(ComponenteJava c : componenteJava.getComponentesAninhados().values()) //percorre todos os componentes aninhados recursivamente
		{
			percorreComponente(c);
		}
		todosComponentes.add(componenteJava); //adiciona cada componente em um unico arrayList
	}

	public void calcular() //calcula qtd de classes, interfaces e linhas de codigo
	{
		this.qtdClasses = 0;
		this.qtdInterfaces = 0;
		this.qtdLOC = 0;
		for(ComponenteJava c : getTodosComponentes())
		{
			if(c != null)
			{
				if(c.getNomeQualificado() != null)
				{
					if(!c.getNomeQualificado().isEmpty())
					{
						if(c.getTIPO().equals("interface")) qtdInterfaces++;
						else qtdClasses++;
						
						qtdLOC += c.getLinhasDeCodigo();
					}
				}
			}
		}
	}
	
	public HashMap<String, Pacote> getPacotes()
	{
		return pacotes;
	}

	public void setPacotes(HashMap<String, Pacote> pacotes)
	{
		this.pacotes = pacotes;
	}

	public Pacote getPacotePadrao()
	{
		return pacotePadrao;
	}

	public void setPacotePadrao(Pacote pacotePadrao)
	{
		this.pacotePadrao = pacotePadrao;
	}

	public static ControleComponentesJava getInstance()
	{
		return ControleComponentesJavaHolder.INSTANCE;
	}

	private static class ControleComponentesJavaHolder
	{

		private static final ControleComponentesJava INSTANCE = new ControleComponentesJava();
	}
	
	/*********VISUALIZACAO*********/
	
	public String jsonRadialGraph()
	{
		contID = 0;
		return jsonPacoteJava();
	}
	
	
	private String jsonPacoteJava()
	{
		Collection<Pacote> pacotes = getPacotes().values();
		
		if(pacotes.size() == 1)
		{
			JSONObject pacotePrincipal = new JSONObject();
			for(Pacote p : pacotes)
			{
				pacotePrincipal.put("id", contID);
				contID++;
				
				pacotePrincipal.put("name", "package: "+p.getNome());
				
				//JSONArray data = new JSONArray();
				pacotePrincipal.put("data", "");
				
				JSONArray filho = new JSONArray();
				jsonPacoteJava(filho, p);
				
				pacotePrincipal.put("children", filho);
			}
			
			return pacotePrincipal.toJSONString();
		}
		else
		{
			JSONObject src = new JSONObject();
			
	        src.put("id", contID);
	        contID++;
			src.put("name", "src");
			
			JSONArray data = new JSONArray();
			src.put("data", data);
			
			JSONArray filho = new JSONArray();
			
			//jsonPacoteJava(filho, pacotePadrao);
			for(Pacote p : pacotes)
			{
				jsonPacoteJava(filho, p);
			}
			
			src.put("children", filho);
			

			return src.toJSONString();
		}
	}
	
	private void jsonPacoteJava(JSONArray pai, Pacote pacote)
	{
		if(pacote.getNomeQualificado() != null)
		{
			boolean pacoteVazio = true;
			
			Collection<Pacote> pacotes = pacote.getPacotes().values();
			Collection<ComponenteJava> componentes = pacote.getComponentes().values();
			
			
			JSONObject noPacote = new JSONObject();
			
			noPacote.put("id", contID);
			contID++;
			
			noPacote.put("name", "package: "+pacote.getNome());
			
			JSONArray data = new JSONArray();
			noPacote.put("data", data);
			
			JSONArray filho = new JSONArray();
			
			if(!pacotes.isEmpty()) //pacote atual possui pacotes dentro dele
			{
				pacoteVazio = false;
				for(Pacote p : pacotes)
				{
					jsonPacoteJava(filho, p);
				}
			}
			if(!componentes.isEmpty()) //pacote atual possui componentes dentro dele
			{
				pacoteVazio = false;
				for(ComponenteJava c : componentes)
				{
					jsonComponenteJava(filho, c);
				}
			}
			
			noPacote.put("children", filho);
			if(!pacoteVazio) //previne adicionar o pacote padrão quando ele não é utilizado
			{
				pai.add(noPacote);
			}
		}
	}
	
	private void jsonComponenteJava(JSONArray pai, ComponenteJava componente)
	{
		if(!componente.getTIPO().equals("generico"))
		{
			Collection<ComponenteJava> componentes = componente.getComponentesAninhados().values();
			
			JSONObject noComponente = new JSONObject();
			
			noComponente.put("id", contID);
			ControleAnalisador.getInstance().addCaminhoArquivosRGraph(contID, componente.getCaminho());
			contID++;
			
			if(componente.getTIPO().equals("interface"))
			{
				noComponente.put("name", "interface: "+componente.getNome());
			}
			else noComponente.put("name", "class: "+componente.getNome());
			
			
			JSONArray data = new JSONArray();
			noComponente.put("data", data);
			
			JSONArray filho = new JSONArray();
			
			for(ComponenteJava c : componentes)
			{
				jsonComponenteJava(filho, c);
			}
			
			noComponente.put("children", filho);
			pai.add(noComponente);
		}
	}
	
	private void inicializarMatriz() //cria a matriz com dimensao sendo a qtd de componentes
	{
		this.matrizAdjacencia = new int[dimensao][dimensao];
		for(int i = 0; i < dimensao; i++)
		{
			for(int j = 0; j < dimensao; j++)
			{
				this.matrizAdjacencia[i][j] = 0;
			}
		}
	}
	
	public void criarMatrizAdjacencia() //cria matriz de adjacencia utilizada na representacao em javascript chordDiagram
	{
		ArrayList<ComponenteJava> todosComponentes = getTodosComponentes();
		
		ArrayList<ComponenteJava> componentesLimpos = new ArrayList<>();
		
		todosPacotes = new ArrayList<>();
		coresPacotes = new ArrayList<>();
		
		nomesComponentes = new ArrayList<>();
		caminhoComponentes = new ArrayList<>();
		for(ComponenteJava c : todosComponentes) //remove referencias null, "limpando" o array de componentes
		{			
			if(c != null)
			{
				Pacote p = c.getPacote();
				if(p != null)
				{
					if(!c.getTIPO().equals("generico")) //garante só classes declaradas, desconsidera bibliotecas
					{
						int index = todosPacotes.indexOf(p);
						if(index == -1) //pacote ainda nao inserido na lista
						{
							todosPacotes.add(p); 
							index = todosPacotes.indexOf(p);
						}
						index++;
						coresPacotes.add(index);
						
						String nome;
						if(c.getTIPO().equals("interface")) nome = "i: " + c.getNome();
						else nome = "c: " + c.getNome();
						
						componentesLimpos.add(c); //arrayList com todos os componentes do projeto, sem referencias null
						nomesComponentes.add(nome); //arrayList com todos os nomes dos componentes, na ordem do arrayList componentesLimpos
						caminhoComponentes.add(c.getNomeQualificado());
						ControleAnalisador.getInstance().addCaminhoArquivosChord(c.getNomeQualificado(), c.getCaminho());
					}
				}				
			}
		}
		
		this.dimensao = componentesLimpos.size();
		inicializarMatriz();
		
		for(ComponenteJava c : componentesLimpos)
		{	
			int linhasDeCodigo = c.getLinhasDeCodigo();
			
			if(c.getExtende() != null) //adiciona ligacao entre c e sua superclasse
			{
				int i = componentesLimpos.indexOf(c);//linha referente ao componente c (na matriz de adjacencia)
				int j = componentesLimpos.indexOf(c.getExtende());
				if(i != -1 && j != -1)
				{
					this.matrizAdjacencia[i][j] = 100;
					this.matrizAdjacencia[j][i] = 100;
				}
			}
			
			ArrayList<ComponenteJava> implementacoes = c.getImplementa();
			for(ComponenteJava d : implementacoes) //adiciona ligacao entre c e suas superinterfaces
			{
				int i = componentesLimpos.indexOf(c);//linha referente ao componente c (na matriz de adjacencia)
				int j = componentesLimpos.indexOf(d);
				if(i != -1 && j != -1)
				{
					this.matrizAdjacencia[i][j] = 100;
					this.matrizAdjacencia[j][i] = 100;
				}
			}
			
			for(ComponenteJava d : c.getComponentesAninhados().values()) //adiciona ligacao entre c e seus comp. aninhados
			{
				if(d != null)
				{
					int i = componentesLimpos.indexOf(c);//linha referente ao componente c (na matriz de adjacencia)
					int j = componentesLimpos.indexOf(d);
					if(i != -1 && j != -1)
					{
						this.matrizAdjacencia[i][j] = 100;
						this.matrizAdjacencia[j][i] = 100;
					}
				}
			}
			
			ArrayList<ComponenteJava> dependencias = c.getDependencias();
			for(ComponenteJava d : dependencias) //adiciona ligacao entre c e suas dependencias
			{
				int i = componentesLimpos.indexOf(c);
				int j = componentesLimpos.indexOf(d);
				if(i != -1 && j != -1)
				{
					this.matrizAdjacencia[i][j] = 100;
					this.matrizAdjacencia[j][i] = 100;
				}
			}
			
			if(linhasDeCodigo != 0)
			{
				int i = componentesLimpos.indexOf(c); //linha referente ao componente c (na matriz de adjacencia)
				int qtdLigacoes = 0;
				for(int j = 0; j < dimensao; j++)
				{
					if(this.matrizAdjacencia[i][j] == 1)
					{
						qtdLigacoes++;
					}
				}
				if(qtdLigacoes != 0)
				{
					int proporcao = linhasDeCodigo/qtdLigacoes;
					for(int j = 0; j < dimensao; j++)
					{
						this.matrizAdjacencia[i][j] *= proporcao;
					}
				}
			}
		}
	}
	
	public String obterArrayNomes()
	{
		if(nomesComponentes != null)
		{
			String nomes = "[";
			for(String s : nomesComponentes)
			{
				if(s !=null)
				{
					nomes += "\"" + s + "\"" + ",";
				}
			}
			nomes = nomes.substring(0, nomes.length()-1);
			nomes += "]";
			return nomes;
		}
		return "[]";
	}
	
	public String obterArrayCoresPacotes()
	{
		if(coresPacotes != null)
		{
			String cores = "[";
			for(Integer i : coresPacotes)
			{
				if(i !=null)
				{
					cores += "\"" + i + "\"" + ",";
				}
			}
			cores = cores.substring(0, cores.length()-1);
			cores += "]";
			return cores;
		}
		return "[]";
	}
	
	public String obterArrayCaminho()
	{
		if(caminhoComponentes != null)
		{
			String nomes = "[";
			for(String s : caminhoComponentes)
			{
				if(s !=null)
				{
					nomes += "\"" + s + "\"" + ",";
				}
			}
			nomes = nomes.substring(0, nomes.length()-1);
			nomes += "]";
			return nomes;
		}
		return "[]";
	}
	
	public String obterMatrizAdjacencia()
	{
		if(matrizAdjacencia != null)
		{
			String matriz = "[";
			for(int i = 0; i < dimensao - 1; i++)
			{
				matriz += "[";
				for(int j = 0; j < dimensao - 1; j++)
				{
					matriz += this.matrizAdjacencia[i][j] + ",";
				}
				matriz += this.matrizAdjacencia[i][dimensao-1] + "],";
			}
			
			matriz += "[";
			for(int j = 0; j < dimensao -1; j++)
			{
				matriz += this.matrizAdjacencia[dimensao - 1][j] + ",";
			}
			matriz += this.matrizAdjacencia[dimensao - 1][dimensao - 1] + "]]";
			
			return matriz;
		}
		return "[]";
	}
	
	
	public String getMatrizGoogleChart(String nomeProjeto)
	{
		this.nomeProjeto = nomeProjeto;
		matriz = "[";
		matriz += "['componente', 'parent', 'size', 'color', 'data'],\n";
		matriz += "['"+nomeProjeto+"',    null,                 0,                               0, ''],\n";
		
		
		percorrePacoteGoogleChart(pacotePadrao); 
		for(Pacote p : pacotes.values())
		{
			if(p != null)
			{
				percorrePacoteGoogleChart(p);
			}
		}
		
		matriz += "]";
		return matriz;
	}
	
	private void percorrePacoteGoogleChart(Pacote pacote)
	{
		if(!pacote.getTipoPacote().equals("generico"))
		{
			if(pacote.getPacotePai() == null)
			{
				matriz += "[{v:'"+pacote.getNomeA()+"', f:'"+"p - "+pacote.getNome()+"'},    '"+nomeProjeto+"',                 0,                               0, ''],\n";
			}
			else
			{
				matriz += "[{v:'"+pacote.getNomeA()+"', f:'"+"p - "+pacote.getNome()+"'},    '"+pacote.getPacotePai().getNomeA()+"',                 0,                               0, ''],\n";	
			}
			
			for(ComponenteJava c : pacote.getComponentes().values()) //percorre todos os componentes recursivamente
			{
				if(c != null)
				{
					percorreComponenteGoogleChart(c);
				}
			}
			for(Pacote p : pacote.getPacotes().values()) //percorre todos os pacotes recursivamente
			{
				percorrePacoteGoogleChart(p);
				
			}
		}
	}
	
	private void percorreComponenteGoogleChart(ComponenteJava componente)
	{
		if(componente.getNomeQualificado() != null)
		{
			if(!componente.getNomeQualificado().isEmpty())
			{
				if(componente.getPacote() != null)
				{
					String aux = "[{v:'"+componente.getNomeQualificado()+"', f:'"+componente.getNome()+"'}, "
							+ "'"+componente.getPacote().getNomeA()+"', "
							+ ""+componente.getLinhasDeCodigo()+", "
							+ ""+componente.getQtdReferencias()+ ", '"
							+componente.getNome()+"<br><br>"+componente.atributosMetodosToString()+"'],";
					
					matriz += aux + "\n";
					for(ComponenteJava c : componente.getComponentesAninhados().values()) //percorre todos os componentes aninhados recursivamente
					{
						if(c != null)
						{
							percorreComponenteAninhadoGoogleChart(c, componente);
						}
					}
					
					ControleAnalisador.getInstance().addCaminhoArquivosTree(componente.getNomeQualificado(), componente.getCaminho());
				}
			}
		}
	}
	
	private void percorreComponenteAninhadoGoogleChart(ComponenteJava componente, ComponenteJava pai)
	{
		componente.setQtdReferencias(pai.getQtdReferencias());
		if(componente.getNomeQualificado() != null)
		{
			if(!componente.getNomeQualificado().isEmpty())
			{
				int locPai = pai.getLinhasDeCodigo();
				int qtdIrmaos = pai.getQtdComponentesAninhados();
				String nomeQualificado = componente.getNomeQualificado();
				int loc = locPai/qtdIrmaos;
				String auxAninhado = "[{v:'"+nomeQualificado+"', f:'"+componente.getNome()+"'}, "
						+ "'"+pai.getNomeQualificado()+"', "
						+ ""+loc+", "
						+ ""+componente.getQtdReferencias()+", '"
						+componente.getNome()+" (classe aninhada)<br><br>"+componente.atributosMetodosToString()+"'],";
				
				matriz += auxAninhado + "\n";
				for(ComponenteJava c : componente.getComponentesAninhados().values()) //percorre todos os componentes aninhados recursivamente
				{
					if(c != null)
					{
						percorreComponenteAninhadoGoogleChart(c, componente);
					}
				}
			}
		}
	}
}
