package sourcevision.analisador.analisadorJava;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IImportDeclaration;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragment;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;

import sourcevision.analisador.analisadorJava.componentesJava.Atributo;
import sourcevision.analisador.analisadorJava.componentesJava.ComponenteJava;
import sourcevision.analisador.analisadorJava.componentesJava.ControleComponentesJava;
import sourcevision.analisador.analisadorJava.componentesJava.Metodo;
import sourcevision.analisador.analisadorJava.componentesJava.Pacote;

public class AnalisadorJava
{
	private Pacote				pacoteAtual;
	private ArrayList<String>	importacoesAtual;

	//metodo que gera a AST
	private static CompilationUnit parse(ICompilationUnit unit)
	{
		ASTParser parser = ASTParser.newParser(AST.JLS8);
		parser.setKind(ASTParser.K_COMPILATION_UNIT);
		parser.setSource(unit);
		parser.setResolveBindings(true);
		return (CompilationUnit) parser.createAST(null); // parse
	}

	private void inicializar() //limpa os atributos desta classe
	{
		pacoteAtual = null;
		importacoesAtual = new ArrayList<>();
	}

	public void analisarProjeto(IJavaProject projetoJava) throws JavaModelException
	{
		inicializar();
		ControleComponentesJava.getInstance().inicializarAtributos();
		
		IPackageFragment[] pacotes = projetoJava.getPackageFragments(); //todos os pacotes do projeto

		for(IPackageFragment pacote : pacotes)
		{
			if(pacote.getKind() == IPackageFragmentRoot.K_SOURCE) // somente pacote de códigos-fonte(nao JARS)
			{
				analisarPacote(pacote);
			}
		}
		ControleComponentesJava.getInstance().calcular();
		AnalisadorDependenciasJava.getInstance().analisarDependencias();
	}

	private void analisarPacote(IPackageFragment pacote) throws JavaModelException
	{
		String nomeQualificadoPacote = pacote.getElementName();
		tratarPacote(nomeQualificadoPacote);

		for(ICompilationUnit unidade : pacote.getCompilationUnits()) //para cada codigo-fonte
		{
			IImportDeclaration[] todosImports = unidade.getImports();
			tratarImports(todosImports);

			CompilationUnit parse = parse(unidade);

			ClassInterfaceVisitor visitor = new ClassInterfaceVisitor(pacoteAtual, importacoesAtual, unidade);
			parse.accept(visitor);

		}
	}

	private void tratarImports(IImportDeclaration[] todosImports)
	{
		importacoesAtual = new ArrayList<>();
		for(IImportDeclaration i : todosImports)
		{
			importacoesAtual.add(i.getElementName());
		}
	}

	private void tratarPacote(String nomeQualificadoPacote) //cria a representacao do pacote
	{
		if(nomeQualificadoPacote != null) // possui pacote
		{
			if(nomeQualificadoPacote.contains("."))
			{
				String[] nomesPacotes = nomeQualificadoPacote.split("\\.");
				pacoteAtual = ControleComponentesJava.getInstance().getPacote(nomesPacotes[0]);
				pacoteAtual.setPacotePai(null);
				pacoteAtual.setTipoPacote("declarado");
				for(int i = 1; i < nomesPacotes.length; i++) // pega pacote dentro
																// de pacote
				{
					Pacote aux = pacoteAtual;
					pacoteAtual = pacoteAtual.getPacote(nomesPacotes[i]);
					
					pacoteAtual.setPacotePai(aux);
				}
				pacoteAtual.setNomeQualificado(nomeQualificadoPacote);
				pacoteAtual.setTipoPacote("declarado");
			}
			else
			{
				pacoteAtual = ControleComponentesJava.getInstance().getPacote(nomeQualificadoPacote);
				pacoteAtual.setNomeQualificado(nomeQualificadoPacote);
				pacoteAtual.setPacotePai(null);
				pacoteAtual.setTipoPacote("declarado");
			}
		}
		else // pacote padrao
		{
			pacoteAtual = ControleComponentesJava.getInstance().getPacotePadrao();
			pacoteAtual.setPacotePai(null);
			pacoteAtual.setTipoPacote("declarado");
		}
	}

	/***************** VISITOR PARA CLASSES E INTERFACES *****************/
	static class ClassInterfaceVisitor extends ASTVisitor
	{
		private Pacote				pacoteAtual;		//pacote atual onde as classes/interfaces serao adicionadas
		private ArrayList<String>	importacoesAtual;
		private ICompilationUnit unidade;

		public ClassInterfaceVisitor(Pacote pacoteAtual, ArrayList<String> importacoesAtual, ICompilationUnit unidade)
		{
			this.pacoteAtual = pacoteAtual;
			this.importacoesAtual = importacoesAtual;
			this.unidade = unidade;
		}

		public boolean visit(TypeDeclaration componente) //visita a declaracao de classes/interfaces
		{
			String nome = componente.getName().getIdentifier();
			String nomeQualificado = pacoteAtual.getNomeQualificado() + "." + nome;
			int flags = componente.getModifiers();

			int linhasDeCodigo = 0;
			
			try
			{
				String codigoFonte = unidade.getSource().toString();
				String[] lines = codigoFonte.split("\r\n|\r|\n");
				linhasDeCodigo = lines.length;
			}
			catch(JavaModelException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			IPath path = unidade.getPath();
			
			Type extende = componente.getSuperclassType();
			List<Type> implementa = componente.superInterfaceTypes();

			ComponenteJava novoComponente = pacoteAtual.getComponente(nome);
			novoComponente.setCaminho(path);
			novoComponente.setPacote(pacoteAtual);

			novoComponente.setNomeQualificado(nomeQualificado);
			novoComponente.setImportacoes(importacoesAtual);
			
			novoComponente.setLinhasDeCodigo(linhasDeCodigo);

			if(componente.isInterface())
				novoComponente.setTIPO("interface");
			else novoComponente.setTIPO("classe");
			tratarModificadoresComponente(flags, novoComponente);

			if(extende != null)
			{
				novoComponente.setNomeExtende(extende.toString());
			}

			if(implementa != null)
			{
				for(Type t : implementa)
				{
					String nomeImplementa = t.toString();
					novoComponente.addNomeImplementacao(nomeImplementa);
				}
			}
			for(TypeDeclaration t : componente.getTypes())
			{
				t.accept(new ClassInterfaceAninhadaVisitor(novoComponente, importacoesAtual));
			}

			componente.accept(new DeclaracaoVariavelVisitor(novoComponente));
			componente.accept(new MetodoVisitor(novoComponente));

			return false;
		}

		private void tratarModificadoresComponente(int flags, ComponenteJava componenteAtual)
		{
			componenteAtual.setAbstac(Modifier.isAbstract(flags));
			componenteAtual.setFina(Modifier.isFinal(flags));
			componenteAtual.setPrivat(Modifier.isPrivate(flags));
			componenteAtual.setProtecte(Modifier.isProtected(flags));
			componenteAtual.setPubli(Modifier.isPublic(flags));
			componenteAtual.setStati(Modifier.isStatic(flags));
		}

	}

	/*****************
	 * VISITOR PARA CLASSES E INTERFACES ANINHADAS
	 *****************/
	static class ClassInterfaceAninhadaVisitor extends ASTVisitor
	{
		private ComponenteJava		componenteAtual;	//componente atual onde as classes/interfaces aninhadas serao adicionadas
		private ArrayList<String>	importacoesAtual;

		public ClassInterfaceAninhadaVisitor(ComponenteJava componenteAtual, ArrayList<String> importacoesAtual)
		{
			this.componenteAtual = componenteAtual;
			this.importacoesAtual = importacoesAtual;
		}

		public boolean visit(TypeDeclaration componente) //visita a declaracao de classes/interfaces
		{
			String nome = componente.getName().getIdentifier();
			String nomeQualificado = componenteAtual.getNomeQualificado() + "$" + nome;
			System.out.println("NOME QUALIFICADO: " + nomeQualificado);
			int flags = componente.getModifiers();

			Type extende = componente.getSuperclassType();
			List<Type> implementa = componente.superInterfaceTypes();

			ComponenteJava novoComponente = componenteAtual.getComponenteAninhado(nome);
			novoComponente.setPacote(componenteAtual.getPacote());

			novoComponente.setNomeQualificado(nomeQualificado);
			novoComponente.setImportacoes(importacoesAtual);

			if(componente.isInterface())
				novoComponente.setTIPO("interface");
			else novoComponente.setTIPO("classe");
			tratarModificadoresComponente(flags, novoComponente);

			if(extende != null)
			{
				novoComponente.setNomeExtende(extende.toString());
			}

			if(implementa != null)
			{
				for(Type t : implementa)
				{
					String nomeImplementa = t.toString();
					novoComponente.addNomeImplementacao(nomeImplementa);
				}
			}

			for(TypeDeclaration t : componente.getTypes())
			{
				t.accept(new ClassInterfaceAninhadaVisitor(novoComponente, importacoesAtual));
			}
			componente.accept(new DeclaracaoVariavelVisitor(novoComponente));
			componente.accept(new MetodoVisitor(novoComponente));

			return false;
		}

		private void tratarModificadoresComponente(int flags, ComponenteJava componenteAtual)
		{
			componenteAtual.setAbstac(Modifier.isAbstract(flags));
			componenteAtual.setFina(Modifier.isFinal(flags));
			componenteAtual.setPrivat(Modifier.isPrivate(flags));
			componenteAtual.setProtecte(Modifier.isProtected(flags));
			componenteAtual.setPubli(Modifier.isPublic(flags));
			componenteAtual.setStati(Modifier.isStatic(flags));
		}

	}

	/*****************
	 * VISITOR PARA DECLARACAO DE VARIAVEIS DA CLASSE/INTERFACE
	 *****************/
	static class DeclaracaoVariavelVisitor extends ASTVisitor
	{
		private ComponenteJava componenteAtual;

		public DeclaracaoVariavelVisitor(ComponenteJava componenteAtual)
		{
			this.componenteAtual = componenteAtual;
		}

		public boolean visit(FieldDeclaration variavel)
		{
			String tipoVariavel = variavel.getType().toString();
			int flags = variavel.getModifiers();

			Atributo novoAtributo = new Atributo();
			novoAtributo.setTipo(tipoVariavel);
			tratarModificadoresAtributo(flags, novoAtributo);

			variavel.accept(new NomeAtributoVisitor(novoAtributo)); //pega o nome da variavel

			componenteAtual.addAtributo(novoAtributo);

			return false;
		}

		private void tratarModificadoresAtributo(int flags, Atributo atributoAtual)
		{
			atributoAtual.setFina(Modifier.isFinal(flags));
			atributoAtual.setPrivat(Modifier.isPrivate(flags));
			atributoAtual.setProtecte(Modifier.isProtected(flags));
			atributoAtual.setPubli(Modifier.isPublic(flags));
			atributoAtual.setStati(Modifier.isStatic(flags));
		}
	}

	/***************** VISITOR PARA NOME DE ATRIBUTOS *****************/
	static class NomeAtributoVisitor extends ASTVisitor
	{
		private Atributo atributoAtual;

		public NomeAtributoVisitor(Atributo atributoAtual)
		{
			this.atributoAtual = atributoAtual;
		}

		public boolean visit(VariableDeclarationFragment identificadorAtributo)
		{
			atributoAtual.setNome(identificadorAtributo.getName().toString());
			//System.out.println("atributo: " + var.getName());

			return false;
		}
	}

	/***************** VISITOR PARA METODOS *****************/
	static class MetodoVisitor extends ASTVisitor
	{
		private ComponenteJava componenteAtual;

		public MetodoVisitor(ComponenteJava componenteAtual)
		{
			this.componenteAtual = componenteAtual;
		}

		public boolean visit(MethodDeclaration metodo)
		{
			String nomeMetodo = metodo.getName().getIdentifier();
			Type tipoRetorno = metodo.getReturnType2();
			List<SingleVariableDeclaration> parametros = metodo.parameters();
			int flags = metodo.getModifiers();

			Metodo novoMetodo = new Metodo();
			novoMetodo.setNome(nomeMetodo);
			if(tipoRetorno != null)
				novoMetodo.setRetorno(tipoRetorno.toString());
			else
				novoMetodo.setRetorno("void");

			tratarModificadoresMetodo(flags, novoMetodo);

			String bufferMetodo = novoMetodo.getRetorno() + " " + nomeMetodo + " (";
			if(!parametros.isEmpty())
			{
				for(SingleVariableDeclaration v : parametros)
				{
					v.accept(new ParametrosMetodoVisitor(novoMetodo));
				}

				for(int i = 0; i < novoMetodo.getTipoParametros().size() - 1; i++)
				{
					bufferMetodo += novoMetodo.getTipoParametros().get(i) + " " + novoMetodo.getNomeParametros().get(i)
							+ ", ";
				}
				bufferMetodo += novoMetodo.getTipoParametros().get(novoMetodo.getTipoParametros().size() - 1) + " "
						+ novoMetodo.getNomeParametros().get(novoMetodo.getTipoParametros().size() - 1) + ")";
			}
			else
				bufferMetodo += ")";

			novoMetodo.setAssinatura(bufferMetodo);
			metodo.accept(new VariaveisMetodoVisitor(novoMetodo));
			metodo.accept(new InstanciacaoVisitor(novoMetodo));
			metodo.accept(new ChamadasMetodoVisitor(novoMetodo));

			componenteAtual.addMetodo(novoMetodo);
			return false;
		}

		private void tratarModificadoresMetodo(int flags, Metodo metodoAtual)
		{
			metodoAtual.setAbstac(Modifier.isAbstract(flags));
			metodoAtual.setFina(Modifier.isFinal(flags));
			metodoAtual.setPrivat(Modifier.isPrivate(flags));
			metodoAtual.setProtecte(Modifier.isProtected(flags));
			metodoAtual.setPubli(Modifier.isPublic(flags));
			metodoAtual.setStati(Modifier.isStatic(flags));
		}
	}

	/*****************
	 * VISITOR PARA CHAMADAS A METODO
	 ****************/
	static class ChamadasMetodoVisitor extends ASTVisitor
	{
		private Metodo metodoAtual;

		public ChamadasMetodoVisitor(Metodo metodoAtual)
		{
			this.metodoAtual = metodoAtual;
		}

		public boolean visit(MethodInvocation invocacaoMetodo)
		{
			List<Expression> parametros = invocacaoMetodo.arguments();
			Expression nome = invocacaoMetodo.getExpression();

			if(nome != null)
			{
				String chamadaMetodo = nome.toString();

				boolean possivelStatic = false;

				String bufferIdentificador = "";
				String aux = "";
				for(int i = 0; i < chamadaMetodo.length(); i++)
				{
					char c = chamadaMetodo.charAt(i);
					if(c == '.')
					{
						bufferIdentificador += aux + ".";
						aux = "";
					}
					if(c == '(' && !bufferIdentificador.isEmpty()) //se ha algo no buffer e foi encontrado um (, é uma possivel chamada direto da classe
					{
						possivelStatic = true;
						break;
					}
					else
					{
						aux += c;
					}
				}
				if(possivelStatic)
				{
					bufferIdentificador = bufferIdentificador.substring(0, bufferIdentificador.length() - 1);
					metodoAtual.addTipoDeclarado(bufferIdentificador);
				}
			}

			for(Expression e : parametros)
			{
				e.accept(new InstanciacaoVisitor(metodoAtual));
			}

			return true;
		}
	}

	/*****************
	 * VISITOR PARA CADA VARIAVEL DECLARADA NO METODO
	 ****************/
	static class VariaveisMetodoVisitor extends ASTVisitor
	{
		private Metodo metodoAtual;

		public VariaveisMetodoVisitor(Metodo metodoAtual)
		{
			this.metodoAtual = metodoAtual;
		}

		public boolean visit(VariableDeclarationStatement variavel)
		{
			String tipo = variavel.getType().toString();
			metodoAtual.addTipoDeclarado(tipo);

			return true;
		}
	}

	/***************** VISITOR PARA CADA PARAMETRO DO METODO *****************/
	static class ParametrosMetodoVisitor extends ASTVisitor
	{
		private Metodo metodoAtual;

		public ParametrosMetodoVisitor(Metodo metodoAtual)
		{
			this.metodoAtual = metodoAtual;
		}

		public boolean visit(SingleVariableDeclaration variavel)
		{
			String identificador = variavel.getName().getIdentifier();
			String tipo = variavel.getType().toString();

			metodoAtual.addTipoParametro(tipo);
			metodoAtual.addNomeParametro(identificador);

			return false;
		}
	}

	/****************** VISITOR PARA NEW STATEMENT *****************/
	static class InstanciacaoVisitor extends ASTVisitor
	{
		private Metodo metodoAtual;

		public InstanciacaoVisitor(Metodo metodoAtual)
		{
			this.metodoAtual = metodoAtual;
		}

		public boolean visit(ClassInstanceCreation variavel)
		{
			String identificador = variavel.getType().toString();
			this.metodoAtual.addTipoDeclarado(identificador);

			return false;
		}
	}

	public static AnalisadorJava getInstance()
	{
		return AnalisadorJava2Holder.INSTANCE;
	}

	private static class AnalisadorJava2Holder
	{
		private static final AnalisadorJava INSTANCE = new AnalisadorJava();
	}
}
