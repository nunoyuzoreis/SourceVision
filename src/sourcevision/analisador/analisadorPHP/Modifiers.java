package sourcevision.analisador.analisadorPHP;

public class Modifiers
{
	public static final int PUBLIC = 1;
	
	public static final int PRIVATE = 2;
	
	public static final int PROTECTED = 4;
	
	public static final int STATIC = 8;

	public static final int FINAL = 16;
	
	public static final int ABSTRACT = 1024;
	
	public static boolean isPublic(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
		}
		if(flag >= FINAL)
		{
			flag = flag - FINAL;
		}
		if(flag >= STATIC)
		{
			flag = flag - STATIC;
		}
		if(flag >= PROTECTED)
		{
			flag = flag - PROTECTED;
		}
		if(flag >= PRIVATE)
		{
			flag = flag - PRIVATE;
		}
		if(flag >= PUBLIC)
		{
			flag = flag - PUBLIC;
			return true;
		}
		return false;
	}
	
	public static boolean isPrivate(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
		}
		if(flag >= FINAL)
		{
			flag = flag - FINAL;
		}
		if(flag >= STATIC)
		{
			flag = flag - STATIC;
		}
		if(flag >= PROTECTED)
		{
			flag = flag - PROTECTED;
		}
		if(flag >= PRIVATE)
		{
			flag = flag - PRIVATE;
			return true;
		}
		return false;
	}
	
	public static boolean isProtected(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
		}
		if(flag >= FINAL)
		{
			flag = flag - FINAL;
		}
		if(flag >= STATIC)
		{
			flag = flag - STATIC;
		}
		if(flag >= PROTECTED)
		{
			flag = flag - PROTECTED;
			return true;
		}
		return false;
	}
	
	public static boolean isStatic(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
		}
		if(flag >= FINAL)
		{
			flag = flag - FINAL;
		}
		if(flag >= STATIC)
		{
			flag = flag - STATIC;
			return true;
		}
		return false;
	}
	
	public static boolean isFinal(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
		}
		if(flag >= FINAL)
		{
			flag = flag - FINAL;
			return true;
		}
		return false;
	}
	
	public static boolean isAbstract(int flag)
	{
		if(flag >= ABSTRACT)
		{
			flag = flag - ABSTRACT;
			return true;
		}
		return false;
	}
}
