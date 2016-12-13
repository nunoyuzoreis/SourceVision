package sourcevision.webserver;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import sourcevision.analisador.ControleAnalisador;


public class JettyServlet extends AbstractHandler {

	private String ip = null;
	private int porta;

	public JettyServlet(int porta)
	{
		this.porta = porta;
	}

	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		if(ip == null)
		{
			InetAddress addr = InetAddress.getLocalHost();

			// Get IP Address
			ip = addr.getHostAddress();
			ip = "http://"+ip+":"+porta+"/";
		}

		String url = request.getRequestURL().toString();


		System.out.println(url);

		if(url.equals("http://localhost:8321/") || url.equals(ip))
		{
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			openIndex(response);
			baseRequest.setHandled(true);
		}
		else if(url.equals("http://localhost:8321/rgraph") || url.equals(ip+"rgraph"))
		{
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			openRGraphDiagram(response);
			baseRequest.setHandled(true);
		}
		else if(url.equals("http://localhost:8321/chord") || url.equals(ip+"chord"))
		{
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			chordDiagram(response);
			baseRequest.setHandled(true);
		}
		else if(url.equals("http://localhost:8321/treemap") || url.equals(ip+"treemap"))
		{
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			treeMap(response);
			baseRequest.setHandled(true);
		}
		else if(url.equals("http://localhost:8321/abrirrgraph") || url.equals(ip+"abrirrgraph"))
		{ 
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			String data = request.getParameter("data");
			int id = Integer.valueOf(data);
			ControleAnalisador.getInstance().abrirArquivoRGraph(id);		
		}
		else if(url.equals("http://localhost:8321/abrirchord")|| url.equals(ip+"abrirchord"))
		{ 
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			String data = request.getParameter("data");
			System.out.println(">>>>>>>"+data);
			ControleAnalisador.getInstance().abrirArquivoChord(data);
		}
		else if(url.equals("http://localhost:8321/abrirtree")|| url.equals(ip+"abrirtree"))
		{ 
			response.setContentType("text/html; charset=utf-8");
			response.setStatus(HttpServletResponse.SC_OK);
			String data = request.getParameter("data");
			ControleAnalisador.getInstance().abrirArquivoTree(data);

			treeMap(response);
			baseRequest.setHandled(true);
		}
		else if(url.startsWith("http://localhost:8321/files") || url.startsWith(ip+"files"))
		{
			handleFiles(response, url);
			baseRequest.setHandled(true);
		}
	}

	public void handleFiles(HttpServletResponse response, String url) throws IOException
	{
		response.setContentType("text/html; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);


		String caminho;
		String aux;

		if(url.startsWith("http://localhost:8321/files")) aux = "http://localhost:8321/files";
		else aux = ip+"files";

		caminho = url.substring(aux.length());

		URL urlFile = getClass().getResource("/resources"+caminho);

		
		if(urlFile != null)
		{
			if(caminho.endsWith(".jpg"))
			{
				response.setContentType("image/jpeg");
				response.setStatus(HttpServletResponse.SC_OK);
				OutputStream out = response.getOutputStream();
				BufferedImage bi = ImageIO.read(urlFile);
				ImageIO.write(bi, "jpg", out);
				out.close();
			}
			else if(caminho.endsWith(".js"))
			{
				response.setContentType("text/js; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					PrintWriter out = response.getWriter();
					BufferedReader in = new BufferedReader(new InputStreamReader(urlFile.openStream()));
					String nameList;
					while ((nameList = in.readLine()) != null) {
						out.println(nameList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			else if(caminho.endsWith(".css"))
			{
				response.setContentType("text/css; charset=utf-8");
				response.setStatus(HttpServletResponse.SC_OK);
				try {
					PrintWriter out = response.getWriter();
					BufferedReader in = new BufferedReader(new InputStreamReader(urlFile.openStream()));
					String nameList;
					while ((nameList = in.readLine()) != null) {
						out.println(nameList);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void openIndex(HttpServletResponse response) throws IOException
	{	
		PrintWriter out = response.getWriter();
		
		URL urlBODY = getClass().getResource("/resources/index/index.html");
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlBODY.openStream()));
			String nameList;
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("<h2>Projeto Analisado: "+ControleAnalisador.getInstance().getNomeProjeto()+"</h2>"
					+ "<h4>Classes: "+ControleAnalisador.getInstance().getQtdClasses()+"  <br>"
					+ "Interfaces: "+ControleAnalisador.getInstance().getQtdInterfaces()+"  <br>"
					+ "LOC do projeto: "+ControleAnalisador.getInstance().getQtdLOC()+"</h4>"
					+ "<br>"
					+ "<section id=\"main\"> "
						+ "<section class=\"thumbnails\"> "
							+ "<div> "
								+ "<a href=\""+ip+"rgraph\">"
									+ "<img src=\"files/index/thumbs/rgraph.jpg\" alt=\"\" />"
									+ "<h3>Grafo Radial</h3>"
								+ "</a>"
							+ "</div>"
							+ "<div> "
								+ "<a href=\""+ip+"chord\"> "
									+ "<img src=\"files/index/thumbs/chord.jpg\" alt=\"\" /> "
									+ "<h3>Diagrama de Corda</h3> "
								+ "</a>"
							+ " </div> "
							+ "<div> "
								+ "<a href=\""+ip+"treemap\"> "
									+ "<img src=\"files/index/thumbs/treemap.jpg\" alt=\"\" /> "
									+ "<h3>Treemap</h3>"
								+ " </a> "
							+ "</div>"
						+ " </section> "
					+ "</section>"
					+ "<footer id=\"footer\">"
					+ "</footer>"
				+"</div> "
				+ "<script src=\"files/index/assets/js/jquery.min.js\"></script>"
				+ "<script src=\"files/index/assets/js/jquery.poptrox.min.js\"></script>"
				+ "<script src=\"files/index/assets/js/skel.min.js\"></script>"
				+ "<script src=\"files/index/assets/js/main.js\"></script> "
			+ "</body> "
		+ "</html>");
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		
	
	}

	public void openRGraphDiagram(HttpServletResponse response) throws IOException
	{
		String dadosJson = ControleAnalisador.getInstance().gerarJsonRadialGraph();
		
		response.setContentType("text/html; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);

		PrintWriter out = response.getWriter();

		URL urlJIT = getClass().getResource("/resources/infovis/jit.js");
		URL urlJSRGraph = getClass().getResource("/resources/infovis/rgraph.js");
		URL urlBODY = getClass().getResource("/resources/infovis/bodyRgraph.html");


		URL cssBase = getClass().getResource("/resources/infovis/css/base.css");
		URL cssRGraph = getClass().getResource("/resources/infovis/css/rgraph.css");


		//System.out.print(dadosProjeto);

		out.println("<html lang=\"pt\">"
				+ "<head>");
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cssBase.openStream()));
			String nameList;
			out.println("<style type=\"text/css\" rel=\"stylesheet\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("</style>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(cssRGraph.openStream()));
			String nameList;
			out.println("<style type=\"text/css\" rel=\"stylesheet\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("</style>");
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlJIT.openStream()));
			String nameList;
			out.println("<script type=\"text/javascript\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("</script>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlJSRGraph.openStream()));
			String nameList;
			out.println("<script type=\"text/javascript\">");
			out.print("function init(){ "
					+ "var json = " + dadosJson+";");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("var url = \""+ip+"abrirrgraph\";");
			out.println("; </script>");
		} catch (Exception e) {
			e.printStackTrace();
		}


		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlBODY.openStream()));
			String nameList;
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void chordDiagram(HttpServletResponse response) throws IOException
	{
		String matrizAdjacencia = ControleAnalisador.getInstance().obterMatrizAdjacencia();
		String nomesComponentes = ControleAnalisador.getInstance().obterArrayNomes();
		
		String caminhosComponentes = ControleAnalisador.getInstance().obterArrayCaminho();
		System.out.println(caminhosComponentes);
		
		String cores = ControleAnalisador.getInstance().obterArrayCores();

		//System.out.print(matrizAdjacencia);
		//System.out.print(nomesComponentes);


		response.setContentType("text/html; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();


		URL urlCssCircos = getClass().getResource("/resources/d3/css/circos.css");

		URL urlJsD3V2 = getClass().getResource("/resources/d3/js/lib/d3/d3.v2.js");
		URL urlJsCircos = getClass().getResource("/resources/d3/js/circos.js");

		URL urlHtmlIndex = getClass().getResource("/resources/d3/bodyChord.html");

		out.println("<!DOCTYPE html>");
		
		out.println("<html lang=\"pt\">"
				+ "<head>");
		
		out.println("<script   "
				+ "src=\"http://code.jquery.com/jquery-2.2.3.min.js\"   "
				+ "integrity=\"sha256-a23g1Nt4dtEYOj7bR+vTu7+T8VP13humZFBJNIYoEJo=\"   "
				+ "crossorigin=\"anonymous\"></script>");
		
		out.println("<script   "
				+ "src=\"http://code.jquery.com/ui/1.11.4/jquery-ui.min.js\"   "
				+ "integrity=\"sha256-xNjb53/rY+WmG+4L6tTl9m6PpqknWZvRt0rO1SRnJzw=\"   "
				+ "crossorigin=\"anonymous\"></script>");


		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlCssCircos.openStream()));
			String nameList;
			out.println("<style type=\"text/css\" rel=\"stylesheet\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("</style>");
		} catch (Exception e) {
			e.printStackTrace();
		}




		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlJsD3V2.openStream()));
			String nameList;
			out.println("<script type=\"text/javascript\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("; </script>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlJsCircos.openStream()));
			String nameList;
			out.println("<script type=\"text/javascript\">");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
			out.println("; </script>");
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.println("</head>");
		
		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlHtmlIndex.openStream()));
			String nameList;
			out.print("<body> "
					+ "<div class=\"divbotao\">"
					+ "<form action=\"/\"  method=\"get\">"
					+ "<input type=\"submit\" value=\"Voltar\" class=\"button\">"
					+ "</form>"
					+ "</div>"
					+ "<div id=\"chart\"></div> "
					+ "<script type=\"text/javascript\"> "
					+ "$(document).ready(function () {"
					+ "var matrix = " + matrizAdjacencia + ";" 
					+ "var cores = " + cores + ";" 
					+ "var names = " + nomesComponentes + ";"
					+ "var caminhos = " + caminhosComponentes + ";"
					+ "var url = \""+ip+"abrirchord\";");
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void treeMap(HttpServletResponse response) throws IOException
	{
		String matrizGoogleChart = ControleAnalisador.getInstance().obterMatrizGoogleChart();


		response.setContentType("text/html; charset=utf-8");
		response.setStatus(HttpServletResponse.SC_OK);
		PrintWriter out = response.getWriter();


		URL urlHtmlIndex = getClass().getResource("/resources/googlechart/treemap.html");


		out.println("<html>"
				+ "<head>"
				+ "<script type=\"text/javascript\" src=\"https://www.gstatic.com/charts/loader.js\"></script>"
				+ "<script type=\"text/javascript\"> "
				+ "google.charts.load('current', {'packages':['treemap']}); "
				+ "google.charts.setOnLoadCallback(drawChart); "
				+ "function drawChart() { "
				+ "var data = google.visualization.arrayToDataTable("+matrizGoogleChart+"); \n");

		try {
			BufferedReader in = new BufferedReader(new InputStreamReader(urlHtmlIndex.openStream()));
			String nameList;
			while ((nameList = in.readLine()) != null) {
				out.println(nameList);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		out.println("function showStaticTooltip(row, size, value) {"
				+ "if(data.getValue(row,4) != ''){"
				+ "return '<div style=\"background:#fd9; max-width: 400px; max-height: 300px; padding:10px;overflow: auto; border-style:solid\">' +'"
				+ "<span style=\"font-family:Courier\"><b>' + '"
				+ "<a href=\""+ip+"abrirtree?data='+data.getValue(row, 0)+'\">'"
				+ "+data.getValue(row, 0)+'</a>' +'</b>'+'</span><br><br>'"
				+ "+'Referências: ' + data.getValue(row, 3) + '<br><br>'"
				+ " + data.getValue(row, 4) + '<br>' +' </div>';"
				+ "}"
				+ "else {"
				+ "return '<div style=\"background:#fd9; padding:10px; border-style:solid\">' +'"
				+ "<span style=\"font-family:Courier\"><b> p - '"
				+ " + data.getValue(row, 0) + ' </div>';"
				+ "}"
				+ "}"
				+ "}"
				+ "</script>"
				+ "</head>"
				+ "<body><form action='/'  method=\"get\" style=\"width: 100%; height: 1%;\">"
				+ " <input type=\"submit\" value=\"Voltar\" align=\"bottom\">"
				+ "</form> "
				+ "<div id=\"chart_div\" style=\"width: 100%; height: 99%;\"></div>"
				+ "</body>"
				+ "</html>");
	}
}