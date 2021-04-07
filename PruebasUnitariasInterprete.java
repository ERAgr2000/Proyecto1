//Prueba Junit

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.*;

import java.util.TreeMap;

import org.junit.jupiter.api.Test;

class PruebasUnitariasInterprete {

	@SuppressWarnings("deprecation")
	@Test
	public void testPrincipal() 
	{
		//Realiza la prueba de varios comandos
		String[] aProbar = new String[]{"(+ 2 3)","\'2","(atom 4)","(equal 4 (+ 2 2))","(- (* 4 2) (/ 81 9))"};
		String[] esperado = new String[]{"5.0","\'2","T", "T", "-1.0"};
		String[] resultado = new String[esperado.length];
		
		InterpreteLisp interprete = InterpreteLisp.interpreterLisp();
		
		for(int i = 0; i < aProbar.length; i = i + 1) 
		{
			String resultado_parcial = interprete.principal(aProbar[i], false, new TreeMap<String, String>());
			resultado[i] = resultado_parcial;
		}
		
		assertEquals(esperado, resultado);
	}

}
