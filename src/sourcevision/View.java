package sourcevision;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.browser.Browser;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;

import sourcevision.analisador.ControleAnalisador;
import sourcevision.webserver.JettyServer;

public class View extends ViewPart
{
	public static final String	ID		= "SourceVision.SourceVision";

	
	private JettyServer jettyServer = new JettyServer();
	
	class ViewLabelProvider extends LabelProvider
	{
		public String getColumnText(Object obj, int index)
		{
			return getText(obj);
		}

		public Image getColumnImage(Object obj, int index)
		{
			return getImage(obj);
		}

		public Image getImage(Object obj)
		{
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	
	public void createPartControl(Composite parent)
	{
		String tipoProjeto = ControleAnalisador.getInstance().analisarProjeto();
		
		if(tipoProjeto != null) //analisou um projeto java ou php
		{
			//String rr = JsonCreator.getInstance().gerarJson(tipoProjeto);
			jettyServer = new JettyServer();
			jettyServer.start();
			
			
//			if(tipoProjeto.equals("java"))
//			{
//				gerarDesenhoJava(parent);
//			}
//			else if(tipoProjeto.equals("php"))
//			{
//				gerarDesenhoPHP(parent);
//			}
			
			final Browser browser = new Browser(parent, SWT.WEBKIT);//é necessário utilizar webkit. pode ser um problema no windows
	        browser.setUrl("http://localhost:8321/");
		}
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus()
	{

	}
	
	public void dispose() {
		jettyServer.stopServer();
		jettyServer.stop();
		super.dispose();
	    
	    
	}
}