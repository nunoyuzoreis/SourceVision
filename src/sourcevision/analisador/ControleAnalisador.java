package sourcevision.analisador;


import java.util.HashMap;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IScriptProject;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.PackageFragmentRoot;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.ISelectionService;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.ide.IDE;

import sourcevision.analisador.analisadorJava.AnalisadorJava;
import sourcevision.analisador.analisadorJava.componentesJava.ControleComponentesJava;
import sourcevision.analisador.analisadorPHP.AnalisadorPHP;
import sourcevision.analisador.analisadorPHP.componentesPHP.ControleComponentesPHP;

public class ControleAnalisador
{
	private String nomeProjeto;
	
	private IProject projeto;
	private String tipoProjeto;
	
	private HashMap<Integer, IPath> caminhoArquivosRGraph;
	
	private HashMap<String, IPath> caminhoArquivosChord;
	
	private HashMap<String, IPath> caminhoArquivosTree;
	
	private int qtdClasses;
	private int qtdInterfaces;
	private int qtdLOC;
	
	public void addCaminhoArquivosRGraph(int id, IPath caminho)
	{
		caminhoArquivosRGraph.put(id, caminho);
	}
	
	public void addCaminhoArquivosChord(String nomeCaminho, IPath caminho)
	{
		caminhoArquivosChord.put(nomeCaminho, caminho);
	}
	
	public void addCaminhoArquivosTree(String nomeCaminho, IPath caminho)
	{
		caminhoArquivosTree.put(nomeCaminho, caminho);
	}

	public String analisarProjeto()
	{
		// pega a raiz do workspace
		caminhoArquivosRGraph = new HashMap<>();
		caminhoArquivosTree = new HashMap<>();
		caminhoArquivosChord = new HashMap<>();
		
		IWorkspace workspace = ResourcesPlugin.getWorkspace();
		IWorkspaceRoot raiz = workspace.getRoot();

		projeto = getCurrentSelectedProject();
		if(projeto != null)
		{
			try
			{
				return percorrerProjeto(projeto);
			}
			catch(CoreException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}
	
	public static IProject getCurrentSelectedProject() {
	    IProject project = null;
	    ISelectionService selectionService = 
	        PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService();

	    ISelection selection = selectionService.getSelection();

	    if(selection instanceof IStructuredSelection) {
	        Object element = ((IStructuredSelection)selection).getFirstElement();

	        if (element instanceof IResource) {
	            project= ((IResource)element).getProject();
	        } else if (element instanceof PackageFragmentRoot) {
	            IJavaProject jProject = 
	                ((PackageFragmentRoot)element).getJavaProject();
	            project = jProject.getProject();
	        } else if (element instanceof IJavaElement) {
	            IJavaProject jProject= ((IJavaElement)element).getJavaProject();
	            project = jProject.getProject();
	        }
	    }
	    return project;
	}

	private String percorrerProjeto(IProject projeto) throws CoreException, JavaModelException
	{
		// System.out.println("Projeto " + project.getName());
		this.nomeProjeto = projeto.getName();
		if(projeto.isNatureEnabled("org.eclipse.jdt.core.javanature"))
		{ //projeto java

			IJavaProject projetoJava = JavaCore.create(projeto);
			System.out.println("entrou na analise java!");
			AnalisadorJava.getInstance().analisarProjeto(projetoJava);
			
			
			
			this.tipoProjeto = "java";
			ControleComponentesJava.getInstance().criarMatrizAdjacencia();
			this.qtdClasses = ControleComponentesJava.getInstance().getQtdClasses();
			this.qtdInterfaces = ControleComponentesJava.getInstance().getQtdInterfaces();
			this.qtdLOC = ControleComponentesJava.getInstance().getQtdLOC();
		}
		else if(projeto.isNatureEnabled("org.eclipse.php.core.PHPNature")) 
		{ // projeto php
			IScriptProject projetoPHP = DLTKCore.create(projeto);
			System.out.println("entrou na analise php!");
			AnalisadorPHP.getInstance().analisarProjeto(projetoPHP);
			
			
			this.tipoProjeto = "php";
			ControleComponentesPHP.getInstance().criarMatrizAdjacencia();
			this.qtdClasses = ControleComponentesPHP.getInstance().getQtdClasses();
			this.qtdInterfaces = ControleComponentesPHP.getInstance().getQtdInterfaces();
			this.qtdLOC = ControleComponentesPHP.getInstance().getQtdLOC();
		}
		return this.tipoProjeto;
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
	
	public static ControleAnalisador getInstance()
	{
		return ControleAnalisadorHolder.INSTANCE;
	}

	public String getNomeProjeto()
	{
		return nomeProjeto;
	}

	public void setNomeProjeto(String nomeProjeto)
	{
		this.nomeProjeto = nomeProjeto;
	}

	private static class ControleAnalisadorHolder
	{
		private static final ControleAnalisador INSTANCE = new ControleAnalisador();
	}
	
	/*****ABRIR ARQUIVOS******/
	
	public void abrirArquivoRGraph(int id)
	{
		IPath caminho = caminhoArquivosRGraph.get(id);
		abrirArquivo(caminho);
	}
	
	public void abrirArquivoChord(String nomeCaminho)
	{
		IPath caminho = caminhoArquivosChord.get(nomeCaminho);
		abrirArquivo(caminho);
	}
	
	public void abrirArquivoTree(String nomeCaminho)
	{
		IPath caminho = caminhoArquivosTree.get(nomeCaminho);
		abrirArquivo(caminho);
	}
	
	private void abrirArquivo(IPath caminho)
	{
		if(caminho != null)
		{
			IFile file = ResourcesPlugin.getWorkspace().getRoot().getFile(caminho);
			if(file != null)
			{
				openEditor(file);
			}
		}
	}
	
	private void openEditor(IFile file)
	{
		final IFile aFile = file;
		PlatformUI.getWorkbench().getDisplay().asyncExec(new Runnable() {
			public void run() {
				IWorkbenchWindow dwindow = 
						PlatformUI.getWorkbench().getActiveWorkbenchWindow();
				IWorkbenchPage page = dwindow.getActivePage();
				if (page != null) {
					try {
						IDE.openEditor(page, aFile, true);
					}catch (PartInitException pie) {
						
					}
				}	
			}
		});
	}
	
	/*****VISUALIZACAO******/
	
	public String gerarJsonRadialGraph()
	{
		if(tipoProjeto.equals("java"))
		{
			return ControleComponentesJava.getInstance().jsonRadialGraph();
		}
		else if(tipoProjeto.equals("php"))
		{
			return ControleComponentesPHP.getInstance().jsonRadialGraph();
		}	
		
		
		return "";
	}
	
	public String obterArrayNomes()
	{
		String array = "[]";
		if(tipoProjeto.equals("java"))
		{
			array = ControleComponentesJava.getInstance().obterArrayNomes();
		}
		else if(tipoProjeto.equals("php"))
		{
			array = ControleComponentesPHP.getInstance().obterArrayNomes();
		}
		return array;
	}
	
	public String obterArrayCores()
	{
		String array = "[]";
		if(tipoProjeto.equals("java"))
		{
			array = ControleComponentesJava.getInstance().obterArrayCoresPacotes();
		}
		else if(tipoProjeto.equals("php"))
		{
			array = ControleComponentesPHP.getInstance().obterArrayCoresArquivos();
		}
		return array;
	}
	
	public String obterArrayCaminho()
	{
		String array = "[]";
		if(tipoProjeto.equals("java"))
		{
			array = ControleComponentesJava.getInstance().obterArrayCaminho();
		}
		else if(tipoProjeto.equals("php"))
		{
			array = ControleComponentesPHP.getInstance().obterArrayCaminho();
		}
		return array;
	}
	
	public String obterMatrizAdjacencia()
	{
		String matriz = "[]";
		if(tipoProjeto.equals("java"))
		{
			matriz = ControleComponentesJava.getInstance().obterMatrizAdjacencia();
		}
		else if(tipoProjeto.equals("php"))
		{
			matriz = ControleComponentesPHP.getInstance().obterMatrizAdjacencia();
		}
		return matriz;
	}
	
	public String obterMatrizGoogleChart()
	{
		String matriz = "[]";
		if(this.tipoProjeto.equals("java"))
		{
			matriz = ControleComponentesJava.getInstance().getMatrizGoogleChart(this.nomeProjeto);
		}
		else if(this.tipoProjeto.equals("php"))
		{
			matriz = ControleComponentesPHP.getInstance().getMatrizGoogleChart(this.nomeProjeto);
		}
		return matriz;
	}
}
