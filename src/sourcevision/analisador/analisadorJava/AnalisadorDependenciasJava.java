package sourcevision.analisador.analisadorJava;

import java.util.ArrayList;

import sourcevision.analisador.analisadorJava.componentesJava.Atributo;
import sourcevision.analisador.analisadorJava.componentesJava.ComponenteJava;
import sourcevision.analisador.analisadorJava.componentesJava.ControleComponentesJava;
import sourcevision.analisador.analisadorJava.componentesJava.Metodo;
import sourcevision.analisador.analisadorJava.componentesJava.Pacote;

public class AnalisadorDependenciasJava
{
	private Pacote				pacoteAtual;
	private ArrayList<String>	importacoesAtual;
	private ComponenteJava			componenteAtual;
	private void inicializar() //limpa os atributos desta classe
	{
		pacoteAtual = null;
		importacoesAtual = new ArrayList<>();
	}

	public void analisarDependencias() //analisa todos os tipos de todas as classes para adicionar acoplamento na representacao
	{
		inicializar();
		ArrayList<ComponenteJava> todosComponentes = ControleComponentesJava.getInstance().getTodosComponentes();

		for(ComponenteJava c : todosComponentes)
		{
			componenteAtual = c; //referencia ao componente que sera analisado
			pacoteAtual = c.getPacote(); //referencia ao pacote onde o componente se encontra
			importacoesAtual = c.getImportacoes();

			analisarExtends();
			analisarImplements();
			analisarAtributos();
			analisarMetodos();
		}
	}

	private void analisarExtends()
	{
		String nomeExtend = componenteAtual.getNomeExtende();
		if(nomeExtend != null)
		{
			if(!nomeExtend.isEmpty())
			{
				if(nomeExtend.contains("<")) //nome parametrizado (qualificado ou nao)
				{
					ArrayList<String> todosIds = tratarNomeParametrizado(nomeExtend);
					ComponenteJava extende = tratarIdentificador(todosIds.get(0), true);
					componenteAtual.setExtende(extende);
					extende.addReferencia();
					for(int i = 1; i < todosIds.size(); i++)
					{
						ComponenteJava acoplado = tratarIdentificador(todosIds.get(i), false);
						if(acoplado != null)
						{
							componenteAtual.addDependencia(acoplado);
							acoplado.addReferencia();
						}
					}
				}
				else //nome nao parametrizado (qualificado ou nao)
				{
					ComponenteJava extende = tratarIdentificador(nomeExtend, true);
					componenteAtual.setExtende(extende);
					extende.addReferencia();
				}
			}
		}
	}

	private void analisarImplements()
	{
		ArrayList<String> nomesImplements = componenteAtual.getNomesImplementa();
		if(nomesImplements != null)
		{
			for(String nome : nomesImplements)
			{
				if(nome != null)
				{
					if(!nome.isEmpty())
					{
						if(nome.contains("<")) //nome parametrizado (qualificado ou nao)
						{
							ArrayList<String> todosIds = tratarNomeParametrizado(nome);
							ComponenteJava implementa = tratarIdentificador(todosIds.get(0), true);
							componenteAtual.addImplementacao(implementa);
							implementa.addReferencia();
							for(int i = 1; i < todosIds.size(); i++)
							{
								ComponenteJava acoplado = tratarIdentificador(todosIds.get(i), false);
								if(acoplado != null)
								{
									componenteAtual.addDependencia(acoplado);
									acoplado.addReferencia();
								}
									
							}
						}
						else
						{
							ComponenteJava implementa = tratarIdentificador(nome, true);
							componenteAtual.addImplementacao(implementa);
							implementa.addReferencia();
						}
					}
				}
			}
		}
	}

	private void analisarAtributos()
	{
		ArrayList<Atributo> todosAtributos = componenteAtual.getAtributos();
		for(Atributo atributo : todosAtributos)
		{
			String tipo = atributo.getTipo();
			if(tipo != null)
			{
				if(!tipo.isEmpty())
				{
					if(!isPrimitiva(tipo))
					{
						analisarDependencia(tipo);
					}
				}
			}
		}
	}

	private void analisarMetodos()
	{
		ArrayList<Metodo> todosMetodos = componenteAtual.getMetodos();
		
		for(Metodo metodo : todosMetodos)
		{
			String retorno = metodo.getRetorno();
			if(retorno != null)
			{
				analisarDependencia(retorno); //analisa o retorno
			}
			
			for(String s : metodo.getTipoParametros())
			{
				if(s != null)
				{
					analisarDependencia(s); //analisa o tipo de cada parametro
				}
			}
			
			for(String s : metodo.getTipoDeclarados())
			{
				if(s != null)
				{
					analisarDependencia(s); //analisa o tipo de cada variavel declarada no metodo
				}
			}
		}
	}
	
	private void analisarDependencia(String nome)
	{
		if(nome.contains("<"))
		{
			ArrayList<String> todosIds = tratarNomeParametrizado(nome);
			for(int i = 0; i < todosIds.size(); i++)
			{
				if(!isPrimitiva(todosIds.get(i)))
				{
					ComponenteJava acoplado = tratarIdentificador(todosIds.get(i), false);
					if(acoplado != null)
					{
						componenteAtual.addDependencia(acoplado);
						acoplado.addReferencia();
					}
						
				}
			}
		}
		else
		{
			if(!isPrimitiva(nome))
			{
				ComponenteJava acoplado = tratarIdentificador(nome, false);
				if(acoplado != null)
				{
					componenteAtual.addDependencia(acoplado);
					acoplado.addReferencia();
				}
			}
		}
	}

	private boolean isPrimitiva(String tipo)
	{
		if(tipo.contains("boolean"))
			return true;
		if(tipo.contains("byte"))
			return true;
		if(tipo.contains("char"))
			return true;
		if(tipo.contains("double"))
			return true;
		if(tipo.contains("enum"))
			return true;
		if(tipo.contains("float"))
			return true;
		if(tipo.contains("int"))
			return true;
		if(tipo.contains("long"))
			return true;
		if(tipo.contains("short"))
			return true;
		if(tipo.contains("void"))
			return true;
		if(tipo.contains("String"))
			return true;

		return false;
	}

	private ArrayList<String> tratarNomeParametrizado(String nome) //trata identif parametrizado, separado cada identificador
	{ //retorna cada identificador de Classe/Interface 
		String identificador = ""; //buffer utilizado para pegar cada identificador
		ArrayList<String> todosIds = new ArrayList<>();

		for(int i = 0; i < nome.length(); i++)
		{
			char c = nome.charAt(i);
			if(c == '<')
			{
				todosIds.add(identificador);
				identificador = "";
			}
			else if(c == '>')
			{
				todosIds.add(identificador);
				identificador = "";
			}
			else if(c == ',')
			{
				todosIds.add(identificador);
				identificador = "";
			}
			else if(c == ' ')
			{
				//pula espaços
			}
			else
			{
				identificador += c;
			}
		}
		return todosIds;
	}

	private ComponenteJava tratarIdentificador(String nome, boolean ignorarClassesPadrao) //analisa os identificadores (inclusive qualificados)
	{ //retorna o componente referente ao identificador
		if(nome.contains(".")) //nome qualificado
		{
			return tratarIdentificadorQualificado(nome, ignorarClassesPadrao);
		}
		else //nome simples
		{
			String importacao = null;
			for(String s : importacoesAtual)
			{
				if(s.endsWith(nome))
				{
					importacao = s;
				}
			}
			if(importacao != null) //achou a importacao
			{
				return tratarIdentificadorQualificado(importacao, ignorarClassesPadrao);
			}
			else //sem importacao: componente interno, de mesmo pacote, ou sob demanda
			{
				//os componentes aninhados sao excluidos da analise por ja estarem acoplados em suas classes externas
				for(ComponenteJava c : pacoteAtual.getComponentes().values()) //componentes de mesmo pacote
				{
					if(c.getNome().equals(nome))
						return c;
				}

				//  identificador nao encontrado em lugar algum, analise será feita analisado todos os PACOTES importados
				return tratarIdentificadorSobDemanda(nome, ignorarClassesPadrao);
			}
		}
	}

	private ComponenteJava tratarIdentificadorQualificado(String nome, boolean ignorarClassesPadrao) //somente ids qualificados
	{
		if(!ClassesPadraoJava.getInstance().isClassePadrao(nome)) //se nao for classe a ser excluida da analise
		{
			String[] ids = nome.split("\\."); //separa cada nome de pacote e como ultimo elem. o componente
			
			String importacao = null;
			for(String s : importacoesAtual)
			{
				if(s.endsWith(ids[0]) && !s.equals(ids[0]))
				{
					importacao = s;
				}
			}
			if(importacao != null) //utilizacao de classe aninhada
			{
				return tratarIdentificadorQualificado(importacao, ignorarClassesPadrao);
			}
			else
			{
				Pacote pacoteComp = ControleComponentesJava.getInstance().getPacote(ids[0]);
				ComponenteJava acoplado;
				for(int i = 1; i < ids.length - 1; i++) //percorre todos os pacotes
				{
					pacoteComp = pacoteComp.getPacote(ids[i]);
				}
				acoplado = pacoteComp.getComponente(ids[ids.length - 1]);
				return acoplado;
			}
		}
		else if(ignorarClassesPadrao) //é classe padrao, mas é extends ou implements, entao add como acoplamento
		{
			String[] ids = nome.split("\\."); //separa cada nome de pacote e como ultimo elem. o componente
			String importacao = null;
			for(String s : importacoesAtual)
			{
				if(s.endsWith(ids[0]) && !s.equals(ids[0]))
				{
					importacao = s;
				}
			}
			if(importacao != null) //utilizacao de classe aninhada
			{
				return tratarIdentificadorQualificado(importacao, ignorarClassesPadrao);
			}
			
			
			Pacote pacotePadrao = ControleComponentesJava.getInstance().getPacotePadrao();
			ComponenteJava componenteFaxada = pacotePadrao.getComponente(ids[ids.length - 1]); //cria uma representacao de uma classe padrao (ex. ArrayList)
			componenteFaxada.setNomeQualificado(nome);
			componenteFaxada.setPacote(pacotePadrao);

			return componenteFaxada;
		}
		return null;
	}

	private ComponenteJava tratarIdentificadorSobDemanda(String nome, boolean ignorarClassesPadrao)
	{
		String importacao;
		Pacote pacote;
		for(String s : importacoesAtual)
		{
			if(s.endsWith("*")) //sob demanda
			{
				importacao = s.substring(0, s.length() - 2); //remove .*
				if(!ClassesPadraoJava.getInstance().isClassePadrao(importacao)) //se importacao de pacote nao for padrao
				{
					String[] ids = importacao.split("\\.");
					pacote = ControleComponentesJava.getInstance().getPacote(ids[0]);
					for(int i = 1; i < ids.length; i++)
					{
						pacote = pacote.getPacote(ids[i]);
					}
					for(ComponenteJava c : pacote.getComponentes().values())
					{
						if(c.getNome().equals(nome))
							return c;
					}
				}
			}
		}
		if(ignorarClassesPadrao)
		{
			pacote = ControleComponentesJava.getInstance().getPacotePadrao();
			ComponenteJava componenteFaxada = pacote.getComponente(nome);
			componenteFaxada.setNomeQualificado(nome);
			componenteFaxada.setPacote(pacote);
			return componenteFaxada;
		}
		return null;
	}

	public static AnalisadorDependenciasJava getInstance()
	{
		return AnalisadorDependenciasJavaHolder.INSTANCE;
	}

	private static class AnalisadorDependenciasJavaHolder
	{
		private static final AnalisadorDependenciasJava INSTANCE = new AnalisadorDependenciasJava();
	}
}
