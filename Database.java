import java.util.Scanner;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedReader;
import java.io.IOException;

public class Database {
	
	private static int numRecords = 0;
	private int recordSize = 83; //82 bytes per line + 1 byte per newline char
	
	public Database()
	{
		numRecords = 0;
		recordSize = 0;
	}
	
	public void create() throws IOException
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
		BufferedReader din = new BufferedReader(new FileReader(csv));
		File outData = new File(data);
		FileWriter dout = new FileWriter(outData);
		String record = din.readLine();
		while (record != null)
		{
			//System.out.println("Here is a record: " + record);
			String[] fields = record.split(",");
			fields[0] = String.format("%3s", fields[0]); //Rank
			fields[1] = String.format("%40s", fields[1]); //Company Name
			fields[2] = String.format("%20s", fields[2]); //City
			fields[3] = String.format("%3s", fields[3]); //State
			fields[4] = String.format("%6s", fields[4]); //Zip
			fields[5] = String.format("%10s", fields[5]); //Employee num
			for(int i = 0; i < fields.length; i++)
			{
				dout.write(fields[i]);
			}
			numRecords++; //Updates for every record that is written
			dout.write("\n");
			record = din.readLine();
		}
		//Close streams
		System.out.println("Your file contained " + numRecords + " records");
		din.close();
		dout.close();
	}
	
	public boolean open() 
	{
		boolean successOpen = false;
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database: ");
		String prefix = in.nextLine();
		return successOpen;
	}
	
	public void close()
	{
		
	}
	
	public void display()
	{
		
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
	
	/*
	public String getRecord(RandomAccessFile dataIn, int recordNumber) throws IOException
	{
		String record = "Record Not Found";
		System.out.println("Record Number: " + recordNumber);
		if((recordNumber >= 1) && (recordNumber <= NUM_RECORDS))
		{
			dataIn.seek(0);
			dataIn.skipBytes(recordNumber * RECORD_SIZE);
			record = dataIn.readLine();
		}
		return record;
	}
	*/
}
