import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.io.IOException;

public class Database {
	
	private int numRecords = 0;
	private int recordSize = 84; //83 bytes per line + 1 byte per newline char
	private FileWriter dout;
	private FileWriter configOut;
	private FileWriter overOut;
	private BufferedReader din;
	
	public Database()
	{
		numRecords = 0;
		recordSize = 0;
	}
	
	public void create()
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter the name of a .csv file: ");
		String fname = in.nextLine();
		String csv = fname + ".csv";
		String con = fname + ".config"; //when close DB, update config file
		String data = fname + ".data.txt"; //REMINDER: change this later
		String over = fname + ".overflow";
		System.out.println("Input File: " + csv + "\n" + "Output Files: " + con + ", " + data + ", " + over);
		
		//File I/O
		try
		{
			din = new BufferedReader(new FileReader(csv)); //Input stream
			dout = new FileWriter(new File(data)); //.data filewriter & creates data file
			configOut = new FileWriter(new File(con)); //config filewriter & creates config file
			overOut = new FileWriter(new File(over)); //overflow filewriter & creates overflow file
			String record = din.readLine();
			while (record != null)
			{
				//System.out.println("Here is a record: " + record);
				String[] fields = record.split(",");
				fields[0] = String.format("%-4s", fields[0]); //Rank
				fields[1] = fields[1].replace(' ','_'); //Replaces whitespace w underscores
				fields[1] = String.format("%-40s", fields[1]); //Company Name
				fields[2] = String.format("%-20s", fields[2]); //City
				fields[3] = String.format("%-3s", fields[3]); //State
				fields[4] = String.format("%-6s", fields[4]); //Zip
				fields[5] = String.format("%-10s", fields[5]); //Employee num
				for(int i = 0; i < fields.length; i++)
				{
					dout.write(fields[i]);
				}
				numRecords++; //Updates for every record that is written
				dout.write("\n");
				record = din.readLine();
			}
			//Close streams
			din.close();
			dout.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Your file contained " + numRecords + " records");
	}
	
	public boolean open() 
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database to open: ");
		String prefix = in.nextLine();;
		try 
		{
			dout = new FileWriter(prefix + ".data.txt");
			configOut = new FileWriter(prefix + ".config");
			overOut = new FileWriter(prefix + ".overflow");
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public boolean close()
	{
		try 
		{
			numRecords = 0;
			dout.close();
			configOut.close();
			overOut.close();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void display()
	{
		//RandomAccessFile din = new RandomAccessFile();
	}
	
	public void createReport()
	{
		
	}
	
	public void addRecord()
	{
		String record = "";
		System.out.print("Enter a record to add: ");
	}
	
	public void deleteRecord()
	{
		
	}
	

	
}
