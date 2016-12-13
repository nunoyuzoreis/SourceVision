package sourcevision.analisador.analisadorPHP;

import java.util.ArrayList;

import sourcevision.analisador.analisadorPHP.componentesPHP.Arquivo;
import sourcevision.analisador.analisadorPHP.componentesPHP.ComponentePHP;
import sourcevision.analisador.analisadorPHP.componentesPHP.ControleComponentesPHP;
import sourcevision.analisador.analisadorPHP.componentesPHP.Funcao;
import sourcevision.analisador.analisadorPHP.componentesPHP.Pasta;

public class AnalisadorDependenciasPHP
{
	private Arquivo				arquivoAtual;
	private ComponentePHP		componenteAtual;

	ArrayList<ComponentePHP>	todosComponentes;

	private void inicializar() //limpa os atributos desta classe
	{
		arquivoAtual = null;
		componenteAtual = null;
	}

	public void analisarDependencias() //analisa todos os tipos de todas as classes para adicionar acoplamento na representacao
	{
		inicializar();
		todosComponentes = ControleComponentesPHP.getInstance().getTodosComponentes();

		for(ComponentePHP c : todosComponentes)
		{
			componenteAtual = c; //referencia ao componente que sera analisado
			arquivoAtual = c.getArquivoPai(); //referencia ao arquivo onde o componente se encontra
			arquivoAtual.getPastaPai();

			analisarExtends();
			analisarImplements();
			analisarFuncoes();
		}
	}

	private void analisarExtends()
	{
		String nomeExtend = componenteAtual.getNomeExtende();
		if(nomeExtend != null)
		{
			if(!nomeExtend.isEmpty())
			{
				ComponentePHP extende = analisarIdentificador(nomeExtend); //procura componente no projeto todo
				
				if(extende == null) //classe/interface nao foi encontrada no projeto
				{
					//cria uma representacao do componente
					extende = ControleComponentesPHP.getInstance().getComponenteGenerico(nomeExtend);
				}
				
				componenteAtual.setExtende(extende);
				extende.addReferencia();
			}
		}
	}
	
	private void analisarImplements()
	{
		ArrayList<String> nomesImplementa = componenteAtual.getNomesImplementa();
		for(String s : nomesImplementa)
		{
			if(s != null)
			{
				if(!s.isEmpty())
				{
					ComponentePHP implementa = analisarIdentificador(s); //procura componente no projeto todo
					
					if(implementa == null) //classe/interface nao foi encontrada no projeto
					{
						//cria uma representacao do componente
						implementa = ControleComponentesPHP.getInstance().getComponenteGenerico(s);
					}
					
					componenteAtual.addImplementacao(implementa);
					implementa.addReferencia();
				}
			}
		}
	}
	
	private void analisarFuncoes()
	{
		ComponentePHP dependencia;
		
		ArrayList<Funcao> funcoes = componenteAtual.getFuncoes();
		for(Funcao f : funcoes)
		{
			String retorno = f.getRetorno(); //retorno se houver
			ArrayList<String> tiposParametros = new ArrayList<>(f.getTiposParametros().values()); //tipo dos parametro se houver
			ArrayList<String> tiposInstanciado = f.getTiposInstanciados();
			
			if(retorno != null)
			{
				if(!retorno.isEmpty())
				{
					dependencia = analisarIdentificador(retorno);
					if(dependencia != null)
					{
						componenteAtual.addDependencia(dependencia);
						dependencia.addReferencia();
					}
				}
			}
			
			for(String s : tiposParametros)
			{
				if(s != null)
				{
					if(!s.isEmpty())
					{
						dependencia = analisarIdentificador(s);
						if(dependencia != null)
						{
							componenteAtual.addDependencia(dependencia);
							dependencia.addReferencia();
						}
					}
				}
			}
			
			for(String s : tiposInstanciado)
			{
				if(s != null)
				{
					if(!s.isEmpty())
					{
						dependencia = analisarIdentificador(s);
						if(dependencia != null)
						{
							componenteAtual.addDependencia(dependencia);
							dependencia.addReferencia();
						}
					}
				}
			}
		}
	}
	
	private ComponentePHP analisarIdentificador(String identificador)
	{
		if(identificador.startsWith("\\")) //remove backslash inicial
		{
			identificador = identificador.substring(1);
		}

		if(identificador.contains("\\")) //nome composto, ou alias para namespace ou nome inteiro da classe/interface
		{
			return tratarIdentificadorComposto(identificador);
		}
		else //nome simples: ou o proprio identificador daclasse, ou um alias
		{
			return tratarIdentificadorSimples(identificador);
		}
	}
	
	private ComponentePHP tratarIdentificadorComposto(String identificador)
	{
		ArrayList<ComponentePHP> possiveisComponentes;
		ComponentePHP componenteRef = null;
		
		String[] ids = identificador.split("\\\\");
		String nomeComponente = ids[ids.length - 1];

		String alias;
		int corta = 0;
		for(int i = identificador.length() - 1; i >= 0; i--)
		{
			if(identificador.charAt(i) == '\\') //acha o ultimo delimitador \
			{
				corta = i;
				break;
			}
		}
		alias = identificador.substring(0, corta);

		String use = arquivoAtual.getUses(alias);

		if(use == null) //nao foi definido alias
		{
			ArrayList<String> somenteUses = arquivoAtual.getSomenteUses();
			for(String s : somenteUses)
			{
				if(s.endsWith(alias)) use = s; //foi declarado use para namespace
			}
			if(use == null) use = alias; //referencia direta, sem use, o proprio namespace foi utilizado
		}
			
		possiveisComponentes = buscarComponentes(nomeComponente);

		if(possiveisComponentes.size() == 1) //somente um componente com esse nome
		{
			componenteRef = possiveisComponentes.get(0);
		}
		else
		{
			for(ComponentePHP c : possiveisComponentes)
			{
				if(c.getArquivoPai().getNamespace().equals(use)) //verifica os namespaces dos possiveis componentes
				{
					componenteRef = c;
				}
			}
		}
		
		return componenteRef;
	}
	
	private ComponentePHP tratarIdentificadorSimples(String identificador)
	{
		ArrayList<ComponentePHP> possiveisComponentes;
		ComponentePHP componenteRef = null;
		
		String use = arquivoAtual.getUses(identificador);
		
		if(use == null) //nao foi definido alias
		{
			ArrayList<String> somenteUses = arquivoAtual.getSomenteUses();
			for(String s : somenteUses)
			{
				if(s.endsWith(identificador)) use = s; //foi declarado use para a classe/interface
			}
		}
		
		if(use != null) //existe uma referencia a namespace para o componente que foi referenciado
		{
			String nomeComponente = "";
			String namespace = "";
			int corta = 0;
			for(int i = use.length()-1; i >= 0; i--) //acha o ultimo delimitador \
			{
				if(use.charAt(i) == '\\')
				{
					corta = i;
					break;
				}
			}
			
			namespace = use.substring(0, corta); //corta o namespace a partir do use
			nomeComponente = use.substring(corta+1); //corta o nome da classe/interface a partir do use
			
			possiveisComponentes = buscarComponentes(nomeComponente);
			
			if(possiveisComponentes.size() == 1)
			{
				componenteRef = possiveisComponentes.get(0);
			}
			else
			{
				for(ComponentePHP c : possiveisComponentes)
				{
					if(c.getArquivoPai().getNamespace().equals(namespace)) //verifica os namespaces dos possiveis componentes
					{
						componenteRef = c;
					}
				}
			}
		}
		else //não há referencia de use para o componente
		{
			possiveisComponentes = buscarComponentes(identificador);
			
			if(possiveisComponentes.size() != 0) componenteRef = possiveisComponentes.get(0); //pega a primeira classe q encontrar
		}
		return componenteRef;
	}

	private ArrayList<ComponentePHP> buscarComponentes(String identificador)
	{
		ArrayList<ComponentePHP> componentes = new ArrayList<>();
		for(ComponentePHP c : todosComponentes)
		{
			if(!c.equals(componenteAtual)) //para nao criar referencia para si mesmo
			{
				if(c.getNome().equals(identificador))
				{
					componentes.add(c);
				}
			}
		}
		return componentes;
	}

	public static AnalisadorDependenciasPHP getInstance()
	{
		return AnalisadorDependenciasPHPHolder.INSTANCE;
	}

	private static class AnalisadorDependenciasPHPHolder
	{
		private static final AnalisadorDependenciasPHP INSTANCE = new AnalisadorDependenciasPHP();
	}
}
