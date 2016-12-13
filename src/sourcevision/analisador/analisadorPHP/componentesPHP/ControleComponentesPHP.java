package sourcevision.analisador.analisadorPHP.componentesPHP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import sourcevision.analisador.ControleAnalisador;

public class ControleComponentesPHP
{
	private Pasta pastaDefault;
	private HashMap<String, Pasta> pastas;
	
	private HashMap<String, ComponentePHP> componentesGenericos;
	
	private ArrayList<ComponentePHP> todosComponentes;
	private ArrayList<Arquivo> todosArquivos;
	private ArrayList<String> caminhoComponentes;
	
	private int qtdClasses;
	private int qtdInterfaces;
	private int qtdLOC;
	
	
	private ArrayList<Arquivo> arquivos;
	private ArrayList<Integer> coresArquivos;
	
	
	/*VARIAVEIS VISUALIZACAO*/
	
	private int contID;
	
	private int dimensao;
	private int[][] matrizAdjacencia;
	private ArrayList<String> nomesComponentes;
	
	private String matriz;
	private String nomeProjeto;
	private int color;
	

	public void inicializarAtributos()
	{
		pastaDefault = new Pasta("");
		pastas = new HashMap<>();
		
		componentesGenericos = new HashMap<>();
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
	
	public ComponentePHP getComponenteGenerico(String identificador)
	{
		ComponentePHP componente = componentesGenericos.get(identificador);
		if(componente == null)
		{
			componente = new ComponentePHP(identificador);
			componentesGenericos.put(identificador, componente);
		}
		return componente;
	}
	
	public ArrayList<ComponentePHP> getTodosComponentes()
	{
		this.todosComponentes = new ArrayList<>();
		
		this.todosComponentes.addAll(this.componentesGenericos.values());
		percorrePastas(pastaDefault);
		
		for(Pasta p : pastas.values())
		{
			percorrePastas(p);
		}
		
		return this.todosComponentes;
	}
	
	public void percorrePastas(Pasta pasta)
	{
		for(Pasta p : pasta.getPastas().values())
		{
			percorrePastas(p);
		}
		
		for(Arquivo a : pasta.getArquivos().values())
		{
			percorreArquivo(a);
		}
	}
	
	public void percorreArquivo(Arquivo arquivo)
	{
		for(ComponentePHP c : arquivo.getComponentes().values())
		{
			this.todosComponentes.add(c);
		}
	}
	
	public HashMap<String, Pasta> getPastas()
	{
		return pastas;
	}

	public void setPastas(HashMap<String, Pasta> pastas)
	{
		this.pastas = pastas;
	}

	private ControleComponentesPHP()
	{
	}

	public static ControleComponentesPHP getInstance()
	{
		return ControleComponentesPHPHolder.INSTANCE;
	}

	/**
	 * @return the pastaPrincipal
	 */
	public Pasta getPastaDefault()
	{
		return pastaDefault;
	}

	/**
	 * @param pastaPrincipal
	 *            the pastaPrincipal to set
	 */
	public void setPastaDefault(Pasta pastaDefault)
	{
		this.pastaDefault = pastaDefault;
	}

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
	
	private static class ControleComponentesPHPHolder
	{

		private static final ControleComponentesPHP INSTANCE = new ControleComponentesPHP();
	}
	
	public void calcular() //calcula qtd de classes, interfaces e linhas de codigo
	{
		this.qtdClasses = 0;
		this.qtdInterfaces = 0;
		this.qtdLOC = 0;
		for(ComponentePHP c : getTodosComponentes())
		{
			if(c != null)
			{
				if(c.getNome() != null)
				{
					if(!c.getNome().isEmpty())
					{
						if(c.getTIPO().equals("interface")) qtdInterfaces++;
						else qtdClasses++;
					}
				}
			}
		}
		
		for(Arquivo a : getTodosArquivos())
		{
			if(a != null)
			{
				qtdLOC += a.getLinhasDeCodigo();
			}
		}
	}
	
	public ArrayList<Arquivo> getTodosArquivos()
	{
		this.todosArquivos = new ArrayList<Arquivo>();
		
		percorrePastasGetArquivos(pastaDefault);
		
		for(Pasta p : pastas.values())
		{
			percorrePastasGetArquivos(p);
		}	
		
		return todosArquivos;
	}
	
	public void percorrePastasGetArquivos(Pasta pasta)
	{
		for(Pasta p : pasta.getPastas().values())
		{
			percorrePastasGetArquivos(p);
		}
		
		for(Arquivo a : pasta.getArquivos().values())
		{
			this.todosArquivos.add(a);
		}
	}
	
	
	/*********VISUALIZACAO*********/
	
	public String jsonRadialGraph()
	{
		contID = 0;
		return jsonPastasPhp();
	}
	
	
	private String jsonPastasPhp()
	{
		Pasta pastaDefault = getPastaDefault();
		Collection<Pasta> pastas = getPastas().values();
		
		JSONObject src = new JSONObject();
		
        src.put("id", contID);
        contID++;
		src.put("name", "src");
		
		JSONArray data = new JSONArray();
		src.put("data", data);
		
		JSONArray filho = new JSONArray();
		
		
		jsonPastasPhp(filho, pastaDefault);
		for(Pasta p : pastas)
		{
			jsonPastasPhp(filho, p);
		}
		
		src.put("children", filho);
		

		return src.toJSONString();
	}
	
	private void jsonPastasPhp(JSONArray pai, Pasta pasta)
	{
		if(pasta.getNome() != null)
		{
			boolean pastaVazia = true;
			
			Collection<Pasta> pastas = pasta.getPastas().values();
			Collection<Arquivo> arquivos = pasta.getArquivos().values();
			
			
			
			JSONObject noPasta = new JSONObject();
			
			noPasta.put("id", contID);
			contID++;
			
			if(pasta.getNome().isEmpty()) noPasta.put("name", "pasta: Default");
			else noPasta.put("name", "pasta: "+pasta.getNome());
			
			JSONArray data = new JSONArray();
			noPasta.put("data", data);
			
			JSONArray filho = new JSONArray();
			
			if(!pastas.isEmpty()) //pasta atual possui pastas dentro dele
			{
				pastaVazia = false;
				for(Pasta p : pastas)
				{
					jsonPastasPhp(filho, p);
				}
			}
			if(!arquivos.isEmpty()) //pasta atual possui arquivos dentro dele
			{
				pastaVazia = false;
				for(Arquivo a : arquivos)
				{
					jsonArquivoPhp(filho, a);
				}
			}
			
			noPasta.put("children", filho);
			if(!pastaVazia) //previne adicionar a pasta padrão quando ele não é utilizado
			{
				pai.add(noPasta);
			}
		}
	}
	
	private void jsonArquivoPhp(JSONArray pai, Arquivo arquivo)
	{
		Collection<ComponentePHP> componentes = arquivo.getComponentes().values();
		
		JSONObject noArquivo = new JSONObject();
		
		noArquivo.put("id", contID);
		ControleAnalisador.getInstance().addCaminhoArquivosRGraph(contID, arquivo.getCaminho());
		contID++;
		
		noArquivo.put("name", "arquivo: "+arquivo.getNome());
		
		JSONArray data = new JSONArray();
		noArquivo.put("data", data);
		
		JSONArray filho = new JSONArray();
		
		if(!componentes.isEmpty())
		{
			for(ComponentePHP c : componentes)
			{
				jsonComponentePhp(filho, c);
			}
			noArquivo.put("children", filho);
			
			pai.add(noArquivo);
		}
	}
	
	private void jsonComponentePhp(JSONArray pai, ComponentePHP componente)
	{
		if(!componente.getTIPO().equals("generico"))
		{	
			JSONObject noComponente = new JSONObject();
			
			noComponente.put("id", contID);
			ControleAnalisador.getInstance().addCaminhoArquivosRGraph(contID, componente.getArquivoPai().getCaminho());
			contID++;
			
			if(componente.getTIPO().equals("interface"))
			{
				noComponente.put("name", "interface: "+componente.getNome());
			}
			else noComponente.put("name", "class: "+componente.getNome());
			
			
			JSONArray data = new JSONArray();
			noComponente.put("data", data);
			
			
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
		ArrayList<ComponentePHP> todosComponentes = getTodosComponentes();
		
		arquivos = new ArrayList<>();
		coresArquivos = new ArrayList<>();
		
		ArrayList<ComponentePHP> componentesLimpos = new ArrayList<>();
		nomesComponentes = new ArrayList<>();
		caminhoComponentes = new ArrayList<>();
		
		for(ComponentePHP c : todosComponentes) //remove referencias null, "limpando" o array de componentes
		{			
			if(c != null)
			{
				if(!c.getTIPO().equals("generico")) //garante só arquivos declarados, desconsidera bibliotecas
				{
					Arquivo a = c.getArquivoPai();
					if(a != null)
					{
						int index = arquivos.indexOf(a);
						if(index == -1) //arquivo ainda nao inserido na lista
						{
							arquivos.add(a); 
							index = arquivos.indexOf(a);
						}
						coresArquivos.add(index);
						
						String nome;
						if(c.getTIPO().equals("interface")) nome = "i: " + c.getNome();
						else nome = "c: " + c.getNome();
						
						componentesLimpos.add(c); //arrayList com todos os componentes do projeto, sem referencias null
						nomesComponentes.add(nome); //arrayList com todos os nomes dos componentes, na ordem do arrayList componentesLimpos
						caminhoComponentes.add(c.getArquivoPai().getCaminho().toString());
						ControleAnalisador.getInstance().addCaminhoArquivosChord(c.getArquivoPai().getCaminho().toOSString(), c.getArquivoPai().getCaminho());
					}
				}
			}
		}
		
		this.dimensao = componentesLimpos.size();
		inicializarMatriz();
		
		for(ComponentePHP c : componentesLimpos)
		{	
			int linhasDeCodigo = c.getArquivoPai().getLinhasDeCodigo();
			
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
			
			ArrayList<ComponentePHP> implementacoes = c.getImplementa();
			for(ComponentePHP d : implementacoes) //adiciona ligacao entre c e suas superinterfaces
			{
				int i = componentesLimpos.indexOf(c);//linha referente ao componente c (na matriz de adjacencia)
				int j = componentesLimpos.indexOf(d);
				if(i != -1 && j != -1)
				{
					this.matrizAdjacencia[i][j] = 100;
					this.matrizAdjacencia[j][i] = 100;
				}
			}
			
			ArrayList<ComponentePHP> dependencias = c.getDependenciasComponente();
			for(ComponentePHP d : dependencias) //adiciona ligacao entre c e suas dependencias
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
	
	public String obterArrayCoresArquivos()
	{
		if(coresArquivos != null)
		{
			String cores = "[";
			for(Integer i : coresArquivos)
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
		this.color = -100;
		
		matriz = "[";
		matriz += "['componente', 'parent', 'size', 'color', 'data'],";
		matriz += "['"+nomeProjeto+"',    null,                 0,                               0, ''],";
		
		
		percorrePastaGoogleChart(pastaDefault); 
		color+= +7;
		for(Pasta p : pastas.values())
		{
			if(p != null)
			{
				percorrePastaGoogleChart(p);
			}
			color+= +7;
		}
		
		matriz += "]";
		return matriz;
	}
	
	private void percorrePastaGoogleChart(Pasta pasta)
	{
		if(pasta.getPastaPai() == null)
		{
			matriz += "[{v:'"+pasta.getNomeA()+"', f:'"+"p - "+pasta.getNome()+"'},    '"+nomeProjeto+"',                 0,                               0, ''],\n";
		}
		else
		{
			matriz += "[{v:'"+pasta.getNomeA()+"', f:'"+"p - "+pasta.getNome()+"'},    '"+pasta.getPastaPai().getNomeA()+"',                 0,                               0, ''],\n";	
		}
		for(Arquivo a : pasta.getArquivos().values()) //percorre todos os arquivos recursivamente
		{
			if(a != null)
			{
				percorreArquivoGoogleChart(a);
			}
		}
		for(Pasta p : pasta.getPastas().values()) //percorre todos as pastas recursivamente
		{
			if(p != null)
			{
				percorrePastaGoogleChart(p);
			}
		}
	}
	
	private void percorreArquivoGoogleChart(Arquivo arquivo)
	{
		matriz += "[{v:'"+arquivo.getCaminho()+"', f:'"+"p - "+arquivo.getNome()+"'},    '"+arquivo.getPastaPai().getNomeA()+"',                 0,                               0, ''],\n";
		
		for(ComponentePHP c : arquivo.getComponentes().values())
		{
			if(c != null)
			{
				percorreComponenteGoogleChart(c);
			}
		}
	}
	
	private void percorreComponenteGoogleChart(ComponentePHP componente)
	{
		if(componente.getNome() != null)
		{
			if(!componente.getNome().isEmpty())
			{
				String caminho = componente.getArquivoPai().getCaminho().toString() + "/"+componente.getNome();
				String aux = "[{v:'"+caminho+"', f:'"+componente.getNome()+"'}, "
						+ "'"+componente.getArquivoPai().getCaminho()+"', "
						+ ""+componente.getArquivoPai().getLinhasDeCodigo()+", "
						+ ""+componente.getQtdReferencias()+ ", '"
						+componente.atributosMetodosToString()+"'],";
				
				matriz += aux;
				
				ControleAnalisador.getInstance().addCaminhoArquivosTree(caminho, componente.getArquivoPai().getCaminho());
			}
		}
	}
}
