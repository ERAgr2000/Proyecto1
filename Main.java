import java.util.*; 

public class Main{
	
	public static void print(String a){
		System.out.println(a);
	}
	
	public static void main(String args[]) throws Exception
	{
		
		print("Lisp Interpreter");
		
		
		while(true){
			
			//sc.nextLine();
			InterpreteLisp.read();
		}
	}
}