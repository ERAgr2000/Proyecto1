//Elder Guzmna
//19628
//Algoritmos y Estructura de Datos

/*
Funciones para fibonacci y factorial, respectivamente:

	(defun fib (a) (cond ((< a 2) (1)) ((> a 1) ( + (fib (- a 1)) (fib (- a 2))) ) ))

	(defun fac (a) (cond ((< a 2) (1)) ((> a 1) ( * (fac (- a 1)) a )) ))
*/

import java.io.*;
import java.util.*;
import java.lang.*;
import java.util.ArrayList; 

public class InterpreteLisp{
	
	//Funciones de conveniencia
	public static void print(String a){
		System.out.println(a);
	}
	
	public static void print(String[] a){
		for(int i = 0; i < a.length; i = i + 1)
		{
			
			System.out.println(i + ". " + a[i]);

		}
	}
	
	//Treemap para el almacenamineto de funciones y variables.
	TreeMap<String, String[]> functions ;
	TreeMap<String, String> assigned_variables ;
	
	//Se emplea un modelo singleton
	private static InterpreteLisp interpreter = new InterpreteLisp();
	
	public static InterpreteLisp interpreterLisp()
	{
		return interpreter;
	}
	
	//Constructor donde se inicializan los treemaps.
	private InterpreteLisp()
	{
		
		assigned_variables = new TreeMap<String, String>();
		functions = new TreeMap<String, String[]>();
		
	}
	
	//Verifica si un string es un numero
	private boolean isnumber(String texto)
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
	
	//Verifica si todos los parentesis estan cerrados y abiertos.
	private boolean parentesis(String texto)
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
	private String[] split_in_statements(String texto)
	{
		try{
			String txt = "";
			ArrayList<String> comandos = new ArrayList();
			int prubea = 0;
			boolean all = false;
			boolean quote = false;
			texto = texto.trim();

			//Algoritmos de division en diferentes declaraiones
			for(int i = 0; i < texto.length(); i += 1 )
			{
				
				Character caracter = texto.charAt(i);
				
				
				if(caracter == '\'' && (prubea == 0))
				{

					quote = true;
					all = true;
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
						//print("1");
						comandos.add(txt);
						all = false;
						quote = false;
						txt = "";
					}
					prubea = prubea + 1;
					if(prubea > 1 )
					{
						txt = txt + "(";
						all = true;
						quote = false;
					}
					if(quote && prubea == 1)
					{
						txt = txt + "(";
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
						//print("2");
						comandos.add("("+txt+")");
						quote = false;
						all = false;
						txt = "";
					}
					else if(txt.trim() != "" && (caracter != '\'') && !txt.equals("\'") && !quote)
					{
						//print("3");
						comandos.add(txt);
						txt = "";
						all = false;
						quote = false;
					}
					else if(txt.trim() != "" && (caracter != '\'') && !txt.equals("\'") && quote)
					{
						//print("3.2");
						txt = txt + ")";
						comandos.add(txt);
						txt = "";
						all = false;
						quote = false;
					}
					else if(quote && prubea == 1)
					{
						txt = txt + ")";
						//print("4");
						comandos.add(txt);
						txt = "";
						all = false;
						quote = false;
					}
					prubea = prubea -1;
				}
				else if(caracter == ' ' && prubea < 2 )
				{
					if(txt.trim() != "" && (caracter != '\'') && !txt.equals("\'") && !quote)
					{
						//print("5");
						comandos.add(txt);
						txt = "";
						all = false;
						quote = false;
					}
					if(caracter == ' ' && quote)
					{
						txt = txt + " ";
					}
				}
				else
				{
					txt = txt + caracter.toString();
				}
				
				if(txt.equalsIgnoreCase("quote"))
				{
					txt = "\'";
				}
				if(prubea == 0 && all && (caracter != '\'') && !txt.equals("\'"))
				{
					//print("6");
					comandos.add(txt);
					all = false;
					quote = false;
				}
			}
			
			if(!txt.equals(""))
			{
				comandos.add(txt);
			}
			
			String[] arrStr = new String[comandos.size()]; 
			arrStr = comandos.toArray(arrStr); 
			//System.out.println(arrStr.length);
			//print(arrStr);
			return arrStr;
		
		}catch(Exception e)
		{
			print("Error en split_in_statements: " + e.toString());
		}
		return new String[1];
	}
	
	public void read()
	{
		try
		{			
			Scanner sc= new Scanner(System.in);
			String texto = sc.nextLine();
			print( principal(texto,false, assigned_variables) ); //new TreeMap<String, String>()) );
			
		}
		catch(Exception e)
		{
			print("Error en read: " + e.toString());
		}
		
	}
	
	private boolean nofunc(String name, boolean isvar)
	{
		if(name.equalsIgnoreCase("setq") )
		{
			return true;
		}
		if(name.equalsIgnoreCase("defun") )
		{
			return true;
		}
		if(name.equalsIgnoreCase("atom") )
		{
			return true;
		}
		if(name.equalsIgnoreCase("list") )
		{
			return true;
		}
		if(name.equalsIgnoreCase("cond") )
		{
			return true;
		}
		if(name.equalsIgnoreCase("equal") )
		{
			return true;
		}
		if(name.equals("<") )
		{
			return true;
		}
		if(name.equals(">") )
		{
			return true;
		}
		if(name.equals("+") )
		{
			return true;
		}
		if(name.equals("-") )
		{
			return true;
		}
		if(name.equals("/") )
		{
			return true;
		}
		if(name.equals("*") )
		{
			return true;
		}
		if(!isvar)
		{
			if(noVar(name, true))
			{
				return true;
			}
		}
		return false;
		
	}
	
	private boolean noVar(String var, boolean isfun)
	{
		if( isnumber(var) )
		{
			return true;
		}
		else if(!isfun )
		{
			if( nofunc(var , true))
			{
				return true;
			}
		}
		
		return false;
		
	}
	
	public String principal(String texto, boolean defunction, TreeMap<String, String> variables)
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
				//System.out.println(strList[i]);/
				
			}
			
			
			if(defunction)
			{
				for(int i = 0; i < strList.length; i= i+1)
				{
					
					if(variables.containsKey(strList[i]))
					{
						strList[i] = variables.get(strList[i]);
					}
				}
			}
			/*
			for(int i = 0; i < strList.length; i = i+1)
			{
				//System.out.println( variables.containsKey(strList[i]) );
				//print("                a" + strList[i]);
				//System.out.println( !(strList[0].equals("setq") && (i%2  == 1)) );
				if( assigned_variables.containsKey(strList[i]) && !(strList[0].equals("setq") && (i%2  == 1)) )
				{
					strList[i] = assigned_variables.get(strList[i]);
					//print("entro " + strList[i]);
				}
				//print("                b" + strList[i]);
			}
			*/
			for(int i = 0; i < strList.length; i = i+1)
			{
				//System.out.println( variables.containsKey(strList[i]) );
				//print("                a" + strList[i]);
				//System.out.println( !(strList[0].equals("setq") && (i%2  == 1)) );
				if( variables.containsKey(strList[i]) && !(strList[0].equals("setq") && (i%2  == 1)) )
				{
					strList[i] = variables.get(strList[i]);
					//print("entro " + strList[i]);
				}
				//print("                b" + strList[i]);
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
						String parcial = principal(strList[i], defunction, variables);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						//System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			else if(strList[0].charAt(0) == '\'')
			{
				//print(strList);
				respuesta = strList[strList.length-1];
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
						String parcial = principal(strList[i], defunction, variables);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						//System.out.println(strList[i]);
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
						String parcial = principal(strList[i], defunction, variables);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						//System.out.println(strList[i]);
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
						String parcial = principal(strList[i], defunction, variables);
						strList[i] = parcial;
						i = i-1;
					}
					else{
						//System.out.println(strList[i]);
					}
					
				}
				
				respuesta = resultado.toString();
			}
			else if(strList[0].equals("<"))
			{
				
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(!isnumber(strList[i]))
					{
						throw new Exception("type operator error");
					}
				}
				
				Float a = Float.parseFloat(strList[1]);
				Float b = Float.parseFloat(strList[2]);
				
				if(a < b)
				{
					respuesta = "T";
				}
				else
				{
					respuesta = "NIL";
				}
			
			}
			else if(strList[0].equals(">"))
			{
				for(int i = 1; i < strList.length; i= i+1)
				{
					if(!isnumber(strList[i]))
					{
						throw new Exception("type operator error");
					}
				}
				
				Float a = Float.parseFloat(strList[1]);
				Float b = Float.parseFloat(strList[2]);
				
				if(a > b)
				{
					respuesta = "T";
				}
				else
				{
					respuesta = "NIL";
				}
				
			}
			else if(strList[0].equalsIgnoreCase("setq"))
			{
				if( (strList.length%2) != 1)
				{
					throw new Exception("Es imposible asignar valor al menos a una variable.");
				}
				else
				{
					respuesta = "";
					for(int i = 1; i < strList.length; i = i + 2)
					{
						String to_assign = strList[i+1];
						Character evaluate = to_assign.charAt(0);
						
						if(noVar( strList[i], false))
						{
							throw new Exception(strList[i] + " que se intenta asignar esta reservada.");
						}
						if(evaluate == '(')
						{
							to_assign = principal(to_assign, defunction, variables);
							
						}
						
						respuesta = respuesta + "\n Variable: " + strList[i] + " valor: " + to_assign + ". el valor anterior era: " + assigned_variables.put(strList[i], to_assign);
					}
					
				}
			}
			else if(strList[0].equalsIgnoreCase("defun"))
			{
				String name_function = strList[1];
				
				String[] info_function = new String[2];
				
				if(nofunc(name_function, false))
				{
					throw new Exception("Nombre de funcion reservada por el sistema");
				}
				else if((strList.length != 4))
				{
					
					throw new Exception("Error en la definicion, parametros incorrectos");
					
				}
				else if(functions.containsKey(strList[1]) )
				{
					respuesta = "Alerta: La funcion se sobreescribira.";
				}	
				
				info_function[0] = strList[2];
				info_function[1] = strList[3];
				
				functions.put(name_function, info_function);
				
			}
			else if(strList[0].equalsIgnoreCase("equal"))
			{
				if(strList.length != 3)
				{
					throw new Exception("Error en funcion equal: la operacion es binaria");
				}
				
				String par1 = principal(strList[1] , false, variables);
				String par2 = principal(strList[2] , false, variables);
				
				if(par1.equals(par2))
				{
					respuesta = "T";
				}
				else
				{
					respuesta = "NIL";
				}
				
			}
			else if(strList[0].equalsIgnoreCase("cond"))
			{
				int i = 1;
				
				while( i < strList.length)
				{
					String[] conditional_sentence = split_in_statements(strList[i]);
					
					String conditional = principal(conditional_sentence[0], defunction, variables);
					
					//print("Cond sentece" + conditional_sentence[0]);
					
					if(conditional.equals("T"))
					{
						//print(conditional_sentence[1]);
						respuesta = principal(conditional_sentence[1], defunction, variables);
						break;
					}
					else if(conditional.equals("NIL"))
					{
						i = i + 1;
						
					}
					else
					{
						throw new Exception(conditional_sentence[0] + " no es una condicional.");
					}
					//print(conditional);
				}
			}
			else if(strList[0].equalsIgnoreCase("atom"))
			{
				
				if(strList.length != 2)
				{
					throw new Exception("Parametros incorrectos");
				}
				else if(strList[1].charAt(0) == '\'')
				{
					String texto_verify  = "";
					for(int i = 1; i < strList[1].length(); i = i + 1)
					{
						texto_verify += strList[1].charAt(i);
					}
					String[] texto_splited = split_in_statements(texto_verify);
					if(texto_splited.length > 1)
					{
						respuesta = "NIL";
					}
					else if(texto_splited.length == 1)
					{
						respuesta = "T";
					}
					else
					{
						throw new Exception("Error al evaluar la funcion atom. Code: 202001");
					}
				}
				else if(strList[1].equals("( )"))
				{
					respuesta = "T";
				}
				else//(strList[1].charAt(0) == '(')
				{
					String texto_verify = principal(strList[1], defunction, variables);
					String[] texto_splited = split_in_statements(texto_verify);
					if(texto_splited.length > 1)
					{
						respuesta = "NIL";
					}
					else if(texto_splited.length == 1)
					{
						respuesta = "T";
					}
					else
					{
						throw new Exception("Error al evaluar la funcion atom. Code: 202001");
					}
				}
				
			}
			else if(strList[0].equalsIgnoreCase("list"))
			{
				
				if(strList.length == 1)
				{
					respuesta = "NIL";
				}
				else
				{
					String respuesta_preliminar = "(";
					
					for(int i = 1; i < strList.length; i = i + 1)
					{
						if(strList[i].charAt(0) == '\'')
						{
							if(i == 1)
							{
								
								respuesta_preliminar = respuesta_preliminar + strList[i].substring(1);
							
							}
							else
							{
								
								respuesta_preliminar = respuesta_preliminar + " " + strList[i].substring(1);
								
							}
						}
						else
						{
							if(i == 1)
							{
								
								respuesta_preliminar = respuesta_preliminar + principal(strList[i], false, variables);
								
							}
							else
							{
								
								respuesta_preliminar = respuesta_preliminar + " " + principal(strList[i], false, variables);
							
							}
						}					
					}
					
					respuesta_preliminar = respuesta_preliminar + ")";
					
					respuesta = respuesta_preliminar;
				}
			}
			else
			{
				String info = "";
				if(!functions.containsKey(strList[0]))
				{
					boolean valid_non_parentesis = true;
					
					for(int i = 0; i < strList.length; i = i + 1)
					{
						String review = strList[i];
						if((review.charAt(0) != '\'') && !isnumber(review) && (review.charAt(0) != '\"' || review.charAt(review.length() -1) != '\"') && !(variables.containsKey(review)) )
						{
							valid_non_parentesis = false;
							break;
						}
					}
					
					if(!valid_non_parentesis)
					{
						print(strList[0]);
						throw new Exception("Funcion inexistente");

					}
					else
					{
						respuesta = strList[strList.length-1];
					}
					
				}
				else
				{
					info = functions.get(strList[0])[0];
					String[] func_info = split_in_statements(info);
					int new_length = strList.length - 1;
					
					String[] func_parameters = new String[new_length];
					
					for(int i = 0; i < new_length; i = i + 1)
					{
						func_parameters[i] = strList[i+1];
					}
					
					
					TreeMap<String, String> parametros_usar = new TreeMap<String, String>();
					
					
					if( func_info.length != func_parameters.length)
					{
						throw new Exception("Incongruente numero de parametros");
					}
					else
					{
						for(int i = 0; i < func_info.length; i = i+1)
						{
							if(func_parameters[i].charAt(0) == '(')
							{
								func_parameters[i] = principal(func_parameters[i], false, variables);
							}
							parametros_usar.put(func_info[i], func_parameters[i]);
						}
					}
					
					//print(parametros_usar.toString());
					//print(functions.get(strList[0])[1]);
					respuesta = principal(functions.get(strList[0])[1], true, parametros_usar);
				}
			}
			return respuesta;
		}
		catch(Exception e)
		{
			return("Error en el modulo principal: " + e.toString());
		}
	}
}

