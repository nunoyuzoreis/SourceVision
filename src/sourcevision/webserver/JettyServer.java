package sourcevision.webserver;

import org.eclipse.jetty.server.Server;


public class JettyServer extends Thread
{	
	private Server server = new Server();
	private static final int PORTA = 8321;

	public void run()
	{
		org.apache.log4j.BasicConfigurator.configure();
		
		server = new Server(PORTA);
		JettyServlet servletJetty = new JettyServlet(PORTA);

	    server.setHandler(servletJetty);

		try
		{
			server.start();
			server.join();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void stopServer()
	{
		try
		{
			server.stop();
		}
		catch(Exception e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
