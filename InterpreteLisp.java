//Elder Guzmna
//19628
//Algoritmos y Estructura de Datos

import java.util.*;
import java.util.ArrayList; 

public class InterpreteLisp{
	
	public static void print(String a){
		System.out.println(a);
	}
	
	//Verifica si un string es un numero
	private static boolean isnumber(String texto)
	{
		int j = 0;
		int points = 0;
		String txt = texto.trim();
		if(txt.charAt(0)== '-')
		{
			j = 1;
		}
		for(int i = j; i < txt.length(); i = i+1)
		{
			if(txt.charAt(i)== '.')
			{
				points += 1;
			}
			//Si hay algun caracter que no sea un digito de inmediato regresa falso
			if( !(Character.isDigit(txt.charAt(i))) && !(txt.charAt(i)== '.') && points < 2 )
			{
				
				return false;
			}
		}
		return true;
	}
	
	//Verifica si no faltan parentesis
	private static boolean parentesis(String texto)
	{
		int abrir_parentesis = 0;
		int cerrar_parentesis = 0;
				
		for(int i=0; i < texto.length(); i = i+1)
		{
			if( texto.charAt(i) == '('	)
			{
				abrir_parentesis += 1;
			}
			else if(texto.charAt(i) == ')'	)
			{
				cerrar_parentesis += 1;
			}
		}
		if(cerrar_parentesis != abrir_parentesis)
		{
			print("Error: parentesis incorrectos");
			
			return false;
		}
		return true;
	}
	
	//Para dividir el enunciado principal en enunciados
	private static String[] split_in_statements(String texto)
	{
		try{
			String txt = "";
			ArrayList<String> comandos = new ArrayList();
			int prubea = 0;
			boolean all = false;
			boolean quote = false;
		
			
			for(int i = 0; i < texto.length(); i += 1 )
			{
				Character caracter = texto.charAt(i);
				if(caracter == '\'' && i == 0)
				{
					String[] strList = new String[1];
					strList[0] = texto;
					return  strList;
				}
				
				if(txt.equals("\'") && caracter == ' ')
				{
					print("Error en split_in_statements: quote mal implementado" );
					return new String[1];
				}
				
				if(prubea == 1)
				{
					all = false;

				}
				
				if( caracter == '(')
				{
					if(txt.trim() != "" && prubea == 1 && caracter != '\'' && !txt.equals("\'"))
					{
						comandos.add(txt);
						txt = "";
					}
					prubea = prubea + 1;
					if(prubea > 1 )
					{
						txt = txt + "(";
						all = true;
						quote = false;
					}
					/*else if(prubea > 1)
					{
						txt = txt + "(";
						all = true;
					}*/
				}
				else if(caracter == ')'	)
				{
					if(all)
					{
						txt = txt + ")";
					}
					else if(txt.trim() != "" && prubea > 1 && (caracter != '\'') && !txt.equals("\'"))
					{
						comandos.add("("+txt+")");
						txt = "";
					}
					else if(txt.trim() != "" && (caracter != '\'') && !txt.equals("\'"))
					{
						comandos.add(txt);
						txt = "";
					}
					prubea = prubea - 1;
				}
				else if(caracter == ' ' && prubea < 2)
				{
					if(txt.trim() != "" && (caracter != '\'') && !txt.equals("\'"))
					{
						comandos.add(txt);
						txt = "";
					}
				}
				else if(prubea>0)
				{
					txt = txt + caracter.toString();
				}
				if(prubea == 0 && all && (caracter != '\'') && !txt.equals("\'"))
				{
					comandos.add(txt);
				}
			}
		
			String[] arrStr = new String[comandos.size()]; 
			arrStr = comandos.toArray(arrStr); 
			return arrStr;
		
		}catch(Exception e)
		{
			print("Error en split_in_statements: " + e.toString());
		}
		return new String[1];
	}
	
	public static void read()
	{
		try
		{			
			Scanner sc= new Scanner(System.in);
			String texto = sc.nextLine();
			print( principal(texto) );
			
		}
		catch(Exception e)
		{
			print("error en read: " + e.toString());
		}
		
	}
	
	
	public static String principal(String texto)
	{
		try
		{
			String respuesta = "Default";
			texto = texto.trim();
			//Evita que de error por ser string vacio
			if( texto == null || texto.length() == 0  )
			{
				
			return "";
			
			}
			
			//Asegurar que no sobren parentesis
			if(!parentesis(texto))
			{
				return "";
			}
			
			//Separa el texto en una lista de strings por enunciado
			String[] strList = split_in_statements(texto);

			
			for(int i = 0; i < strList.length; i= i+1)
			{
				System.out.println(strList[i]);
			}
			
			
			//Para las 5 operaciones basicas
			if(strList[0].equals("+"))
			{
				Float resultado = 0f;
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(isnumber(strList[i]))
					{
						Float parcial = Float.parseFloat(strList[i]);
						resultado = resultado + parcial;
						
					}
					else if(strList[i].charAt(0) == '(')
					{
						//System.out.println(strList[i]);
						//Manda a que si existe una posbile orden lisp se ejecute antes de continuar y la sustituye por la correspondiente
						String parcial = principal(strList[i]);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			else if(strList[0].equals("-"))
			{
				Float resultado = 0f;
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(isnumber(strList[i]) && i == 1)
					{
						resultado = Float.parseFloat(strList[i]);
					}
					else if(isnumber(strList[i]))
					{
						Float parcial = Float.parseFloat(strList[i]);
						resultado = resultado - parcial;
						
					}
					else if(strList[i].charAt(0) == '(')
					{
						//System.out.println(strList[i]);
						//Manda a que si existe una posbile orden lisp se ejecute antes de continuar y la sustituye por la correspondiente
						String parcial = principal(strList[i]);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			else if(strList[0].equals("*"))
			{
				Float resultado = 1f;
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(isnumber(strList[i]))
					{
						Float parcial = Float.parseFloat(strList[i]);
						resultado = resultado * parcial;
						
					}
					else if(strList[i].charAt(0) == '(')
					{
						//System.out.println(strList[i]);
						//Manda a que si existe una posbile orden lisp se ejecute antes de continuar y la sustituye por la correspondiente
						String parcial = principal(strList[i]);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			else if(strList[0].equals("/"))
			{
				Float resultado = 0f;
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(isnumber(strList[i]) && i == 1)
					{
						resultado = Float.parseFloat(strList[i]);
					}
					else if(isnumber(strList[i]))
					{
						Float parcial = Float.parseFloat(strList[i]);
						resultado = resultado/parcial;
						
					}
					else if(strList[i].charAt(0) == '(')
					{
						//System.out.println(strList[i]);
						//Manda a que si existe una posbile orden lisp se ejecute antes de continuar y la sustituye por la correspondiente
						String parcial = principal(strList[i]);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			return respuesta;
		}
		catch(Exception e)
		{
			return("Error en el modulo principal: " + e.toString());
		}
	}
}