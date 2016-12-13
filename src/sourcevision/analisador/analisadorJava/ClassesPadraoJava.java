package sourcevision.analisador.analisadorJava;

import java.util.ArrayList;

public class ClassesPadraoJava
{
	private ArrayList<String> identificadores;
	
	private ClassesPadraoJava()
	{
		identificadores = new ArrayList<>();
		identificadores.add("org.eclipse");
		identificadores.add(" org.osgi");
		identificadores.add("java.applet");
		identificadores.add("java.awt");
		identificadores.add("java.awt.color");
		identificadores.add("java.awt.datatransfer");
		identificadores.add("java.awt.dnd");
		identificadores.add("java.awt.event");
		identificadores.add("java.awt.font");
		identificadores.add("java.awt.geom");
		identificadores.add("java.awt.im");
		identificadores.add("java.awt.im.spi");
		identificadores.add("java.awt.image");
		identificadores.add("java.awt.image.renderable");
		identificadores.add("java.awt.print");
		identificadores.add("java.beans");
		identificadores.add("java.beans.beancontext");
		identificadores.add("java.io");
		identificadores.add("java.lang");
		identificadores.add("java.lang.annotation");
		identificadores.add("java.lang.instrument");
		identificadores.add("java.lang.invoke");
		identificadores.add("java.lang.management");
		identificadores.add("java.lang.ref");
		identificadores.add("java.lang.reflect");
		identificadores.add("java.math");
		identificadores.add("java.net");
		identificadores.add("java.nio");
		identificadores.add("java.nio.channels");
		identificadores.add("java.nio.channels.spi");
		identificadores.add("java.nio.charset");
		identificadores.add("java.nio.charset.spi");
		identificadores.add("java.nio.file");
		identificadores.add("java.nio.file.attribute");
		identificadores.add("java.nio.file.spi");
		identificadores.add("java.rmi");
		identificadores.add("java.rmi.activation");
		identificadores.add("java.rmi.dgc");
		identificadores.add("java.rmi.registry");
		identificadores.add("java.rmi.server");
		identificadores.add("java.security");
		identificadores.add("java.security.acl");
		identificadores.add("java.security.cert");
		identificadores.add("java.security.interfaces");
		identificadores.add("java.security.spec");
		identificadores.add("java.sql");
		identificadores.add("java.text");
		identificadores.add("java.text.spi");
		identificadores.add("java.time");
		identificadores.add("java.time.chrono");
		identificadores.add("java.time.format");
		identificadores.add("java.time.temporal");
		identificadores.add("java.time.zone");
		identificadores.add("java.util");
		identificadores.add("java.util.concurrent");
		identificadores.add("java.util.concurrent.atomic");
		identificadores.add("java.util.concurrent.locks");
		identificadores.add("java.util.function");
		identificadores.add("java.util.jar");
		identificadores.add("java.util.logging");
		identificadores.add("java.util.prefs");
		identificadores.add("java.util.regex");
		identificadores.add("java.util.spi");
		identificadores.add("java.util.stream");
		identificadores.add("java.util.zip");
		identificadores.add("javax");
		identificadores.add("javax.accessibility");
		identificadores.add("javax.activation");
		identificadores.add("javax.activity");
		identificadores.add("javax.annotation");
		identificadores.add("javax.annotation.processing");
		identificadores.add("javax.crypto");
		identificadores.add("javax.crypto.interfaces");
		identificadores.add("javax.crypto.spec");
		identificadores.add("javax.imageio");
		identificadores.add("javax.imageio.event");
		identificadores.add("javax.imageio.metadata");
		identificadores.add("javax.imageio.plugins.bmp");
		identificadores.add("javax.imageio.plugins.jpeg");
		identificadores.add("javax.imageio.spi");
		identificadores.add("javax.imageio.stream");
		identificadores.add("javax.jws");
		identificadores.add("javax.jws.soap");
		identificadores.add("javax.lang.model");
		identificadores.add("javax.lang.model.element");
		identificadores.add("javax.lang.model.type");
		identificadores.add("javax.lang.model.util");
		identificadores.add("javax.management");
		identificadores.add("javax.management.loading");
		identificadores.add("javax.management.modelmbean");
		identificadores.add("javax.management.monitor");
		identificadores.add("javax.management.openmbean");
		identificadores.add("javax.management.relation");
		identificadores.add("javax.management.remote");
		identificadores.add("javax.management.remote.rmi");
		identificadores.add("javax.management.timer");
		identificadores.add("javax.naming");
		identificadores.add("javax.naming.directory");
		identificadores.add("javax.naming.event");
		identificadores.add("javax.naming.ldap");
		identificadores.add("javax.naming.spi");
		identificadores.add("javax.net");
		identificadores.add("javax.net.ssl");
		identificadores.add("javax.print");
		identificadores.add("javax.print.attribute");
		identificadores.add("javax.print.attribute.standard");
		identificadores.add("javax.print.event");
		identificadores.add("javax.rmi");
		identificadores.add("javax.rmi.CORBA");
		identificadores.add("javax.rmi.ssl");
		identificadores.add("javax.script");
		identificadores.add("javax.security.auth");
		identificadores.add("javax.security.auth.callback");
		identificadores.add("javax.security.auth.kerberos");
		identificadores.add("javax.security.auth.login");
		identificadores.add("javax.security.auth.spi");
		identificadores.add("javax.security.auth.x500");
		identificadores.add("javax.security.cert");
		identificadores.add("javax.security.sasl");
		identificadores.add("javax.sound.midi");
		identificadores.add("javax.sound.midi.spi");
		identificadores.add("javax.sound.sampled");
		identificadores.add("javax.sound.sampled.spi");
		identificadores.add("javax.sql");
		identificadores.add("javax.sql.rowset");
		identificadores.add("javax.sql.rowset.serial");
		identificadores.add("javax.sql.rowset.spi");
		identificadores.add("javax.swing");
		identificadores.add("javax.swing.border");
		identificadores.add("javax.swing.colorchooser");
		identificadores.add("javax.swing.event");
		identificadores.add("javax.swing.filechooser");
		identificadores.add("javax.swing.plaf");
		identificadores.add("javax.swing.plaf.basic");
		identificadores.add("javax.swing.plaf.metal");
		identificadores.add("javax.swing.plaf.multi");
		identificadores.add("javax.swing.plaf.nimbus");
		identificadores.add("javax.swing.plaf.synth");
		identificadores.add("javax.swing.table");
		identificadores.add("javax.swing.text");
		identificadores.add("javax.swing.text.html");
		identificadores.add("javax.swing.text.html.parser");
		identificadores.add("javax.swing.text.rtf");
		identificadores.add("javax.swing.tree");
		identificadores.add("javax.swing.undo");
		identificadores.add("javax.swing.JFrame");
		identificadores.add("javax.tools");
		identificadores.add("javax.transaction");
		identificadores.add("javax.transaction.xa");
		identificadores.add("javax.xml");
		identificadores.add("javax.xml.bind");
		identificadores.add("javax.xml.bind.annotation");
		identificadores.add("javax.xml.bind.annotation.adapters");
		identificadores.add("javax.xml.bind.attachment");
		identificadores.add("javax.xml.bind.helpers");
		identificadores.add("javax.xml.bind.util");
		identificadores.add("javax.xml.crypto");
		identificadores.add("javax.xml.crypto.dom");
		identificadores.add("javax.xml.crypto.dsig");
		identificadores.add("javax.xml.crypto.dsig.dom");
		identificadores.add("javax.xml.crypto.dsig.keyinfo");
		identificadores.add("javax.xml.crypto.dsig.spec");
		identificadores.add("javax.xml.datatype");
		identificadores.add("javax.xml.namespace");
		identificadores.add("javax.xml.parsers");
		identificadores.add("javax.xml.soap");
		identificadores.add("javax.xml.stream");
		identificadores.add("javax.xml.stream.events");
		identificadores.add("javax.xml.stream.util");
		identificadores.add("javax.xml.transform");
		identificadores.add("javax.xml.transform.dom");
		identificadores.add("javax.xml.transform.sax");
		identificadores.add("javax.xml.transform.stax");
		identificadores.add("javax.xml.transform.stream");
		identificadores.add("javax.xml.validation");
		identificadores.add("javax.xml.ws");
		identificadores.add("javax.xml.ws.handler");
		identificadores.add("javax.xml.ws.handler.soap");
		identificadores.add("javax.xml.ws.http");
		identificadores.add("javax.xml.ws.soap");
		identificadores.add("javax.xml.ws.spi");
		identificadores.add("javax.xml.ws.spi.http");
		identificadores.add("javax.xml.ws.wsaddressing");
		identificadores.add("javax.xml.xpath");
	}
	
	public boolean isClassePadrao(String identificador)
	{
		if(identificador != null)
		{
			for(String s : identificadores)
			{
				if(s.contains(identificador) || identificador.startsWith(s)) return true;
			}
		}
		
		return false;
	}
	
	public static ClassesPadraoJava getInstance()
	{
		return ClassesPadraoJavaHolder.INSTANCE;
	}

	private static class ClassesPadraoJavaHolder
	{
		private static final ClassesPadraoJava INSTANCE = new ClassesPadraoJava();
	}
}
