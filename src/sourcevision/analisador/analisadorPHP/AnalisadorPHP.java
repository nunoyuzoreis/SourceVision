package sourcevision.analisador.analisadorPHP;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.IScriptFolder;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.dltk.core.ISourceModule;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.php.internal.core.ast.nodes.ASTParser;
import org.eclipse.php.internal.core.ast.nodes.BodyDeclaration;
import org.eclipse.php.internal.core.ast.nodes.ClassDeclaration;
import org.eclipse.php.internal.core.ast.nodes.ClassInstanceCreation;
import org.eclipse.php.internal.core.ast.nodes.Expression;
import org.eclipse.php.internal.core.ast.nodes.FormalParameter;
import org.eclipse.php.internal.core.ast.nodes.FunctionDeclaration;
import org.eclipse.php.internal.core.ast.nodes.Identifier;
import org.eclipse.php.internal.core.ast.nodes.Include;
import org.eclipse.php.internal.core.ast.nodes.InterfaceDeclaration;
import org.eclipse.php.internal.core.ast.nodes.MethodDeclaration;
import org.eclipse.php.internal.core.ast.nodes.NamespaceDeclaration;
import org.eclipse.php.internal.core.ast.nodes.NamespaceName;
import org.eclipse.php.internal.core.ast.nodes.Program;
import org.eclipse.php.internal.core.ast.nodes.Scalar;
import org.eclipse.php.internal.core.ast.nodes.Statement;
import org.eclipse.php.internal.core.ast.nodes.UseStatementPart;
import org.eclipse.php.internal.core.ast.visitor.AbstractVisitor;

import sourcevision.analisador.analisadorPHP.componentesPHP.Arquivo;
import sourcevision.analisador.analisadorPHP.componentesPHP.ComponentePHP;
import sourcevision.analisador.analisadorPHP.componentesPHP.ControleComponentesPHP;
import sourcevision.analisador.analisadorPHP.componentesPHP.Funcao;
import sourcevision.analisador.analisadorPHP.componentesPHP.Pasta;

@SuppressWarnings({ "restriction", "deprecation" })
public class AnalisadorPHP
{
	
	private ControleComponentesPHP	controleComponentesPHP;

	private Pasta					pastaAtual;
	private Arquivo					arquivoAtual;
	private AnalisadorPHP()
	{
		controleComponentesPHP = ControleComponentesPHP.getInstance();
	}

	private void inicializar()
	{
		pastaAtual = null;
		arquivoAtual = null;
	}

	//método que faz o parse do fonte, retornando a AST 
	private Program parse(ISourceModule modulo)
	{
		ASTParser parser = ASTParser.newParser(modulo);
		try
		{
			return (Program) parser.createAST(null);
		}
		catch(Exception e)
		{
			return null;
		}
	}

	//método principal da analise
	public void analisarProjeto(IScriptProject projetoPHP) throws ModelException
	{
		inicializar();
		controleComponentesPHP.inicializarAtributos();
		IScriptFolder[] pastas = projetoPHP.getScriptFolders(); //pega todas as pastas com fontes .php
		for(IScriptFolder pasta : pastas)
		{
			analisarPasta(pasta);
		}
		
		ControleComponentesPHP.getInstance().calcular();
		AnalisadorDependenciasPHP.getInstance().analisarDependencias();
	}

	//método que analisa uma unica pasta
	private void analisarPasta(IScriptFolder pasta) throws ModelException
	{
		String nomePasta = pasta.getElementName();
		if(nomePasta == "") //pasta default
		{
			pastaAtual = controleComponentesPHP.getPastaDefault();
		}
		else
		{
			String[] nomesPastas = nomePasta.split("/");
			String nomeCompleto;
			Pasta pastaPai;
			pastaAtual = controleComponentesPHP.getPasta(nomesPastas[0]);
			nomeCompleto = nomesPastas[0];
			pastaAtual.setNomeCompleto(nomeCompleto);
			for(int i = 1; i < nomesPastas.length; i++)
			{
				pastaPai = pastaAtual;
				pastaAtual = pastaAtual.getPasta(nomesPastas[i]); //pega a pasta mais interna
				nomeCompleto += nomesPastas[i];
				pastaAtual.setNomeCompleto(nomeCompleto);
				pastaAtual.setPastaPai(pastaPai);
			}
		}

		ISourceModule[] fontes = pasta.getSourceModules(); //pega todos os fontes .php dentro da pasta
		for(ISourceModule fonte : fontes)
		{
			analisarFonte(fonte);
		}
	}

	//metodo que analisa um unico codigo-fonte .php
	private void analisarFonte(ISourceModule fonte)
	{
		boolean readOnly = fonte.isReadOnly();
		IPath caminho = fonte.getPath();
		
		int linhasDeCodigo = 0;
		
		String codigoFonte;
		try
		{
			codigoFonte = fonte.getSource().toString();
			String[] lines = codigoFonte.split("\r\n|\r|\n");
			linhasDeCodigo = lines.length;
		}
		catch(ModelException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	
		if(!readOnly) //verifica se o fonte não é da biblioteca padrão(não será somente leitura)
		{
			String nomeFonte = fonte.getElementName();
			arquivoAtual = pastaAtual.getArquivo(nomeFonte); //cria/pega o arquivo 
			arquivoAtual.setPastaPai(pastaAtual);
			arquivoAtual.setCaminho(caminho);
			arquivoAtual.setLinhasDeCodigo(linhasDeCodigo);

			Program program = parse(fonte); //gera AST
			
			program.accept(new NamespaceVisitor(arquivoAtual));
			program.accept(new IncludeVisitor(arquivoAtual));
			program.accept(new UseVisitor(arquivoAtual));

			program.accept(new ClassInterfaceVisitor(arquivoAtual));
		}
	}

	/***************** VISITOR PARA INCLUDE *****************/
	private static class IncludeVisitor extends AbstractVisitor
	{
		private Arquivo arquivoAtual;

		public IncludeVisitor(Arquivo arquivoAtual)
		{
			this.arquivoAtual = arquivoAtual;
		}

		@Override
		public boolean visit(Include declaracaoInclude)
		{
			declaracaoInclude.accept(new IncludeScalarVisitor(arquivoAtual));
			return false;
		}
	}

	/****************** CLASSE VISITOR QUE LE O VALOR DO INCLUDE*****************/
	private static class IncludeScalarVisitor extends AbstractVisitor
	{
		private Arquivo arquivoAtual;

		public IncludeScalarVisitor(Arquivo arquivoAtual)
		{
			this.arquivoAtual = arquivoAtual;
		}

		@Override
		public boolean visit(Scalar declaracaoInclude)
		{
			String include = declaracaoInclude.getStringValue();
			arquivoAtual.addInclusao(include);

			System.out.println("include" + include); //debug
			return false;
		}
	}

	/***************** VISITOR PARA NAMESPACE *****************/
	private static class NamespaceVisitor extends AbstractVisitor
	{
		private Arquivo arquivoAtual;

		public NamespaceVisitor(Arquivo arquivoAtual)
		{
			this.arquivoAtual = arquivoAtual;
		}

		@Override
		public boolean visit(NamespaceDeclaration declaracaoNamespace)
		{
			String namespace = declaracaoNamespace.getName().getName();
			arquivoAtual.setNamespace(namespace);
			System.out.println("namespace: " + namespace); //debug
			return false;
		}
	}

	/***************** VISITOR PARA USE E ALIAS *****************/
	private static class UseVisitor extends AbstractVisitor
	{
		private Arquivo arquivoAtual; //referencia ao arquivo onde o use foi declarado

		public UseVisitor(Arquivo arquivoAtual)
		{
			this.arquivoAtual = arquivoAtual;
		}

		@Override
		public boolean visit(UseStatementPart declaracaoUse)
		{
			String use, alias;
			use = declaracaoUse.getName().getName();
			
			if(declaracaoUse.getAlias() != null) //se foi definido um alias
			{
				alias = declaracaoUse.getAlias().getName();
				
				arquivoAtual.addAlias(alias, use);
				arquivoAtual.addUses(use, alias);
			}
			else //declaracao somente de use
			{ 
				arquivoAtual.addSomenteUse(use);
			}
			
//			System.out.println("namespace (as): " + declaracaoUse.getName().getName()); //debug
//			System.out.println("alias (use): " + declaracaoUse.getAlias().getName()); //debug
			return false;
		}
	}

	/***************** VISITOR PARA CLASSES E INTERFACES *****************/
	private static class ClassInterfaceVisitor extends AbstractVisitor
	{
		private Arquivo arquivoAtual; //referencia ao arquivo a qual a classe/interface pertence

		public ClassInterfaceVisitor(Arquivo arquivoAtual)
		{
			this.arquivoAtual = arquivoAtual;
		}

		@Override
		public boolean visit(ClassDeclaration declaracaoClasse)
		{
			String nomeClasse = declaracaoClasse.getName().getName();
			int flags = declaracaoClasse.getModifier();
			Expression superClasse = declaracaoClasse.getSuperClass();
			List<Identifier> superInterfaces = declaracaoClasse.interfaces();
			
			ComponentePHP classeAtual = arquivoAtual.getComponentePHP(nomeClasse);
			classeAtual.setTIPO("classe");
			tratarModificadoresComponente(flags, classeAtual);
			
			classeAtual.setArquivoPai(arquivoAtual);
			
			if(superClasse!= null)
			{
				superClasse.accept(new ExtendsVisitor(classeAtual));
			}
			
			for(Identifier identificador : superInterfaces)
			{
				classeAtual.addNomeImplementa(identificador.getName());
			}
			
			declaracaoClasse.accept(new MetodoVisitor(classeAtual));
			
			return false;
		}

		@Override
		public boolean visit(InterfaceDeclaration declaracaoInterface)
		{
			String nomeInterface = declaracaoInterface.getName().getName();
			List<Identifier> superInterfaces = declaracaoInterface.interfaces();
			
			ComponentePHP interfaceAtual = arquivoAtual.getComponentePHP(nomeInterface);
			interfaceAtual.setTIPO("interface");
			
			interfaceAtual.setArquivoPai(arquivoAtual);
			
			for(Identifier identificador : superInterfaces)
			{
				interfaceAtual.addNomeImplementa(identificador.getName());
			}
			
			declaracaoInterface.accept(new MetodoVisitor(interfaceAtual));

			return false;
		}
		
		private void tratarModificadoresComponente(int flags, ComponentePHP componenteAtual)
		{
			componenteAtual.setAbstac(Modifiers.isAbstract(flags));
			componenteAtual.setFina(Modifiers.isFinal(flags));
		}
	}
	
	/***************** VISITOR PARA NAMESPACENAME DO EXTENDS *****************/
	private static class ExtendsVisitor extends AbstractVisitor
	{
		private ComponentePHP componenteAtual; //referencia à classe/interface
		
		public ExtendsVisitor(ComponentePHP componenteAtual)
		{
			this.componenteAtual = componenteAtual;
		}
		
		@Override
		public boolean visit(NamespaceName nomeExtends)
		{
			String nomeExtende = nomeExtends.getName();
			componenteAtual.setNomeExtende(nomeExtende);
			return false;
		}
	}
	
	/***************** VISITOR PARA METODOS (FUNCOES)*****************/
	static class MetodoVisitor extends AbstractVisitor
	{
		private ComponentePHP componenteAtual; //referencia à classe/interface que o metodo pertence
		
		public MetodoVisitor(ComponentePHP componenteAtual)
		{	
			this.componenteAtual = componenteAtual;
		}
		
		@Override
		public boolean visit(MethodDeclaration declaracaoMetodo)
		{
			FunctionDeclaration declaracaoFuncao = declaracaoMetodo.getFunction(); //pega a funcao declarada
			
			int flags = declaracaoMetodo.getModifier(); //modificadores do método
			
			String nomeFuncao = declaracaoFuncao.getFunctionName().getName(); //nome da função
			
			String retorno = null;
			
			if(declaracaoFuncao.getReturnType() != null)
			{
				retorno = declaracaoFuncao.getReturnType().getName(); //tipo de retorno (se houver)
			}
			
			List<FormalParameter> listaParametros = declaracaoFuncao.formalParameters(); //todos os parametros
			
			Funcao novaFuncao = new Funcao();
			
			System.out.println("Funcao: " + nomeFuncao);
			
			novaFuncao.setNome(nomeFuncao);
			novaFuncao.setRetorno(retorno);
			tratarModificadoresMetodo(flags, novaFuncao);
			
			for(FormalParameter parametro : listaParametros)
			{
				String nomeParametro = parametro.getParameterNameIdentifier().getName();
				novaFuncao.addParametro(nomeParametro);
				parametro.accept(new TipoParametroScalarVisitor(novaFuncao, nomeParametro)); 
			}
			
			List<Statement> declaracoes = declaracaoFuncao.getBody().statements();
			for(Statement s : declaracoes)
			{
				s.accept(new ClassInstanceCreationVisitor(novaFuncao)); //para cada statement procura instanciacao de classe
				//System.out.println("class instance creation: " + s.toString());
			}
			
			ArrayList<String> nomesParametros = novaFuncao.getNomesParametros();
			HashMap<String, String> tiposParametro = novaFuncao.getTiposParametros();
			
			String assinatura = nomeFuncao + "(";
			if(!nomesParametros.isEmpty())
			{
				String tipoParametro;
				for(int i = 0; i < nomesParametros.size() - 1; i++)
				{
					tipoParametro = tiposParametro.get(nomesParametros.get(i));
					if(tipoParametro != null)
					{
						assinatura += tipoParametro + " " + nomesParametros.get(i) + ", ";
					}
					else assinatura += nomesParametros.get(i) + ", ";
				}
				tipoParametro = tiposParametro.get(nomesParametros.get(nomesParametros.size()-1));
				if(tipoParametro != null)
				{
					assinatura += tipoParametro + " " + nomesParametros.get(nomesParametros.size()-1) + ")";
				}
				else assinatura += nomesParametros.get(nomesParametros.size()-1) + ")";
			}
			else assinatura += ")";
			
			if(retorno != null) assinatura += " :" + retorno;
			
			novaFuncao.setAssinatura(assinatura);
			
			
			this.componenteAtual.addFuncao(novaFuncao);

			return false;
		}
		
		private void tratarModificadoresMetodo(int flags, Funcao funcaoAtual)
		{
			funcaoAtual.setAbstac(Modifiers.isAbstract(flags));
			funcaoAtual.setFina(Modifiers.isFinal(flags));
			funcaoAtual.setPrivat(Modifiers.isPrivate(flags));
			funcaoAtual.setProtecte(Modifiers.isProtected(flags));
			funcaoAtual.setPubli(Modifiers.isPublic(flags));
			funcaoAtual.setStati(Modifiers.isStatic(flags));
		}
	}
	
	/***************** VISITOR PARA TIPO DO PARAMETRO DA FUNCAO*****************/
	private static class TipoParametroScalarVisitor extends AbstractVisitor
	{
		private Funcao funcaoAtual;
		private String nomeParametro;

		public TipoParametroScalarVisitor(Funcao funcaoAtual, String nomeParametro)
		{
			this.funcaoAtual = funcaoAtual;
			this.nomeParametro = nomeParametro;
		}

		@Override
		public boolean visit(Scalar declaracaoTipoParametro)
		{
			String tipoParametro = declaracaoTipoParametro.getStringValue();
			System.out.println("nome parametro: " + nomeParametro); //debug
			funcaoAtual.addTipoParametro(this.nomeParametro, tipoParametro);
			
			return false;
		}
	}
	
	/***************** VISITOR PARA REFERENCIAS A CLASSES*****************/
	private static class ClassInstanceCreationVisitor extends AbstractVisitor
	{
		private Funcao funcaoAtual;

		public ClassInstanceCreationVisitor(Funcao funcaoAtual)
		{
			this.funcaoAtual = funcaoAtual;
		}

		@Override
		public boolean visit(ClassInstanceCreation instanciacaoClasse)
		{
			instanciacaoClasse.accept(new NamespaceReferenceVisitor(funcaoAtual));
			//System.out.println("Tipo: " + instanciacaoClasse.getClassName().getName()); //debug
			return true;
		}
	}
	
	/***************** VISITOR PARA IDENTIFICADOR DE REFERENCIAS A CLASSES*****************/
	private static class NamespaceReferenceVisitor extends AbstractVisitor
	{
		private Funcao funcaoAtual;

		public NamespaceReferenceVisitor(Funcao funcaoAtual)
		{
			this.funcaoAtual = funcaoAtual;
		}

		@Override
		public boolean visit(NamespaceName referencia)
		{
			String nomeComponente = referencia.getName();
			funcaoAtual.addTipoInstanciado(nomeComponente);
			
			System.out.println("Tipo instanciado: " + nomeComponente); //debug
			
			return true;
		}
	}

	public static AnalisadorPHP getInstance()
	{
		return AnalisadorPHPHolder.INSTANCE;
	}

	private static class AnalisadorPHPHolder
	{
		private static final AnalisadorPHP INSTANCE = new AnalisadorPHP();
	}
}
