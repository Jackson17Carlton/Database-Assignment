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
		String data = fname + ".data.txt";
		String over = fname + ".overflow";
		System.out.println("Input File: " + csv + "\n" + "Output Files: " + con + ", " + data + ", " + over);
		
		//File I/O
		try
		{
			BufferedReader din = new BufferedReader(new FileReader(csv));
			File outData = new File(data);
			dout = new FileWriter(outData);
			String record = din.readLine();
			while (record != null)
			{
				//System.out.println("Here is a record: " + record);
				String[] fields = record.split(",");
				fields[0] = String.format("%-4s", fields[0]); //Rank
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
			//din.close(); //closes reading stream, close method used for closing writing stream
			din.close();
			dout.close();
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		//Close streams
		System.out.println("Your file contained " + numRecords + " records");
		
	}
	
	public boolean open() 
	{
		boolean successOpen = false;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database to open: ");
		String prefix = in.nextLine();
		File data = new File(prefix + ".data.txt");
		Boolean open = data.canWrite();
		System.out.print(open);
		if(open)
		{
			successOpen = true;
			return successOpen;
		}
		else 
		{
			return successOpen;
		}
	}
	
	public boolean close()
	{
		boolean successClose = false;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database to close: ");
		String prefix = in.nextLine();
		try {
			dout = new FileWriter(prefix + ".data.txt");
			numRecords = 0;
			dout.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return successClose;
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
