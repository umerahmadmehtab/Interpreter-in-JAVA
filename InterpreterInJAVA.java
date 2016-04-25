/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interpreter.in.java;

/**
 *
 * @author Umer Ahmad Mehtab
 */
import java.io.IOException;
import java.util.ArrayList;



public class InterpreterInJAVA {
	
	String path;

	static String[] lines ;				// store the data of file
	String[] tok;						// array to store after tokenizing single line
	String var;							// to store variable name
	int value=0;						// to store value of variable
	int flag=0;							// flag to help in checking that variable is already assigned or not
	int i=0, j=0;						// loop iterators
	String name;						
	int first=0;						// first operand
	int second=0;						// second operand
	int result=0;						// to store result after arithmetic operations 
	String temp;						
	int number=0;						
	public static final String ANSI_GREEN = "\u001B[32m";
        public static final String ANSI_RESET = "\u001B[0m";
        public static final String ANSI_YELLOW = "\u001B[33m";
        public static final String ANSI_BLUE = "\u001B[34m";
        public static final String ANSI_PURPLE = "\u001B[35m";
        public static final String ANSI_CYAN = "\u001B[36m";
        public static final String ANSI_WHITE = "\u001B[37m";
        public static final String ANSI_RED = "\u001B[31m";
        
	ArrayList<InterpreterInJAVA>  list = new ArrayList<InterpreterInJAVA>() ;	//Array list to store key value for variables and their values
	
	//for storing key value pair
	public InterpreterInJAVA(String x, int y)
	{
		var=x;												
		value=y;
	}
	
	// setting file path
	public InterpreterInJAVA(String f_path)
    {
        path = f_path;
    }

	
    public void Interpret() throws IOException
    {
    	try
    	{
    		ReadFile file = new ReadFile(path);					//calling read file class
    		lines = file.OpenFile();							// storing file data in lines through calling function of ReadFile class
    		
    	
    		for(i=0; i<lines.length;i++)
    		{
    			tok=lines[i].split(" ");						//splitting on the basis of space
    			
    			if(tok[0].equals("Let"))						//checking Let declarative to initialize
    			{	
    				//System.out.println("Initialize");
    				if(!("=".equals(tok[2])))					// check if assignment operation is not present
    				{
    					System.out.println(ANSI_RED + "Syntax Error at line: "+(i+1) + " <------------> equal sign is missing" + ANSI_RESET);
    					continue;
    				}
    				
    				String name= tok[1];						//variable name
    				int val= Integer.parseInt(tok[3]);			//value of variable
    				list.add(new InterpreterInJAVA(name,val));		//storing variable name and its value in key value form in ArrayList
    			}

    			else if(tok[0].equals("print"))					//checking print declarative to print value
    			{
    				
    				String name = tok[1];						// varialble name
    				
    				//printing immediate value 
    				if(isInteger(tok[1]))						
    				{
						System.out.println(ANSI_GREEN + tok[1] + ANSI_RESET);
    				}
    				
    				else
    				{
    					//traversing complete ArrayList to print value of required variable
    					for(j=0; j<list.size();j++)
    					{
    					
    					
    						//printing if variable is initialized by checking its presence in ArrayList
    						if(name.contains(list.get(j).var))
    						{
    							//System.out.println(name);
    							System.out.println(list.get(j).value);		
    							flag=1;										//setting flag 1 if variable is already initialized
    						}
    					}
    			
    					//if variable is not initialized so, its not present in the ArrayList
    					if(flag==0)
    					{
    						System.out.println(ANSI_RED + "Error at line: "+(i+1)+" <------------> " + name +" is not intialized." + ANSI_RED);
    					}
    				
    					flag=0;											//setting flag back to zero
    				}
    			}
    			
    			else											//arithmetic operations statements
    			{
    				if(!("=".equals(tok[1])))					// check if assignment operation is not present
    				{
    					System.out.println(ANSI_RED + "Syntax Error at line: "+(i+1) + " <------------> equal sign is missing" + ANSI_RED);
    					continue;
    				}
    				
    				// L.H.S of = must be variable , it can't be number
    				if(isInteger(tok[0]))						//checking L.H.S of = is integer or not
    				{
    					System.out.println(ANSI_RED +"Syntax Error at line: "+(i+1) + " <------------> Value can't be assigned to immediate value" + ANSI_RESET);
    					continue;
    				}
    				else										// if L.H.S of = is variable , then checking is it initialized or not
    				{
    					if(InitializationCheck(tok[0],i)!=-1)
    					{
    						name = tok[0];
    					}
    					
    				}

    				
    				
    				if(isInteger(tok[2]))								//if first operand is just number
    				{
    					first=Integer.parseInt(tok[2]);
    				}
    				else												// if first operand is variable
    				{
    					if((number=InitializationCheck(tok[2],i))!=-1)		//if variable then checking its existence in ArrayList
    					{
    						first = number;
    					}
    					
    				}
    				
    				if(isInteger(tok[4]))								//if second operand is just number	
    	    		{
    					second = Integer.parseInt(tok[4]);
    	    		}
    				else												// if second operand is variable
    				{
    					if((number=InitializationCheck(tok[4],i))!=-1)   	//if variable then checking its existence in ArrayList
    					{
    						second = number;
    					}
    				}
    				
    				
    				// checking and applying arithmetic operations
    				switch (tok[3])								
    				{
    				case "+":
    					
    					result=first + second;
    					SetValue(name , result);
    				
    					
    					break;
    			
    				case "-":
    					
    					result=first - second;
    					SetValue(name , result);
    					
    					break;
    					
    				case "*":
    					
    					result=first * second;
    					SetValue(name , result);
    					
    					break;
    					
    				case "/":
    					
    					result=first / second;
    					SetValue(name , result);
    					break;
    					
    				default:
    					System.out.println(ANSI_RED + "Syntax Error at line: "+(i+1)+" <------------> Invalid arithmetic operation" + ANSI_RESET);
    					break;
    				
    				}
    				
    			}
    		}
	
    	}
    	

        catch(IOException e){
            System.out.println( e.getMessage() );
        }

        
    }
    
    //checking if string is numeric or not
    public  boolean isInteger(String str)  
    {  
      try  
      {  
    	  Integer.parseInt(str);
    	  return true;
      }  
      catch(NumberFormatException e)  
      {  
        return false;  
      }  
     
    }

    //checking if variable is initialized or not by checking its presence in ArrayList
	
    public int InitializationCheck(String name, int k)
    {
    	int num=0;
    	for(j=0; j<list.size();j++)
		{
			if(name.contains(list.get(j).var))
			{
				num=list.get(j).value;
				flag=1;								//setting flag 1 if variable is already initialized
			}
		}
		
		if(flag==0)									//if variable is not initialized so, its not present in the ArrayList
		{
			System.out.println(ANSI_RED + "Error at line: "+(k+1)+" <------------> "  + name +" is not initialized" + ANSI_RESET);
			return -1;
		}
		flag=0;
		return num;								// returning value against variable name if its present
    }
    
    
    //Setting the value against its variable name
    public void SetValue(String name , int result)
    {
    	//traversing the ArrayList to store value against its variable
    	for(j=0; j<list.size(); j++)
		{
			if(name.contains(list.get(j).var))
			{
				list.set(j, new InterpreterInJAVA(name,result));
			}
		}
    }

	public static void main(String[] args) throws IOException 
	{
    	String fileName;
    	fileName = "TestFile.txt";
    	
    	InterpreterInJAVA Ir= new InterpreterInJAVA(fileName);
    	Ir.Interpret();
    	
    	
    }

}

