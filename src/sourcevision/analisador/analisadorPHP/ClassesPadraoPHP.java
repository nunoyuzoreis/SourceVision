package sourcevision.analisador.analisadorPHP;

import java.util.ArrayList;

public class ClassesPadraoPHP
{
	private ArrayList<String> identificadores;

	private ClassesPadraoPHP()
	{
		identificadores = new ArrayList<>();
		identificadores.add("Exception"); 
		identificadores.add("ReflectionException");
		identificadores.add("Reflection");
		identificadores.add("ReflectionFunction");
		identificadores.add("ReflectionParameter");
		identificadores.add("ReflectionMethod");
		identificadores.add("ReflectionClass");
		identificadores.add("ReflectionObject");
		identificadores.add("ReflectionProperty");
		identificadores.add("ReflectionExtension");
		identificadores.add("SQLiteDatabase");
		identificadores.add("SQLiteResult");
		identificadores.add("SQLiteUnbuffered");
		identificadores.add("SQLiteException");
		identificadores.add("RecursiveIteratorIterator");
		identificadores.add("FilterIterator");
		identificadores.add("ParentIterator");
		identificadores.add("LimitIterator");
		identificadores.add("CachingIterator");
		identificadores.add("CachingRecursiveIterator");
		identificadores.add("ArrayObject");
		identificadores.add("ArrayIterator");
		identificadores.add("DirectoryIterator");
		identificadores.add("RecursiveDirectoryIterator");
		identificadores.add("SimpleXMLElement");
		identificadores.add("SimpleXMLIterator");
		identificadores.add("DOMException");
		identificadores.add("DOMStringList");
		identificadores.add("DOMNameList");
		identificadores.add("DOMImplementationList");
		identificadores.add("DOMImplementationSource");
		identificadores.add("DOMImplementation");
		identificadores.add("DOMNode");
		identificadores.add("DOMNameSpaceNode");
		identificadores.add("DOMDocumentFragment");
		identificadores.add("DOMDocument");
		identificadores.add("DOMNodeList");
		identificadores.add("DOMNamedNodeMap");
		identificadores.add("DOMCharacterData");
		identificadores.add("DOMAttr");
		identificadores.add("DOMElement");
		identificadores.add("DOMText");
		identificadores.add("DOMComment");
		identificadores.add("DOMTypeinfo");
		identificadores.add("DOMUserDataHandler");
		identificadores.add("DOMDomError");
		identificadores.add("DOMErrorHandler");
		identificadores.add("DOMLocator");
		identificadores.add("DOMConfiguration");
		identificadores.add("DOMCdataSection");
		identificadores.add("DOMDocumentType");
		identificadores.add("DOMNotation");
		identificadores.add("DOMEntity");
		identificadores.add("DOMEntityReference");
		identificadores.add("DOMProcessingInstruction");
		identificadores.add("DOMStringExtend");
		identificadores.add("DOMXPath");
		identificadores.add("XSLTProcessor");
	}

	public boolean isClassePadrao(String identificador)
	{
		if(identificador != null)
		{
			for(String s : identificadores)
			{
				if(s.equals(identificador))
					return true;
			}
		}

		return false;
	}

	public static ClassesPadraoPHP getInstance()
	{
		return ClassesPadraoPHPHolder.INSTANCE;
	}

	private static class ClassesPadraoPHPHolder
	{
		private static final ClassesPadraoPHP INSTANCE = new ClassesPadraoPHP();
	}
}
