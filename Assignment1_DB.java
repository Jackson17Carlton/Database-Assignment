import java.util.Scanner;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Database {
	
	private int numRecords = 0;
	private int recordSize = 0;
	
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
		
		//File Input
		Scanner din = new Scanner(new File(csv));
		File outData = new File(data);
		FileWriter dout = new FileWriter(outData);
		din.useDelimiter(",");
		String record = "EMPTY";
		String rank = "-1";
		String name = "EMPTY";
		String city = "EMPTY";
		String state = "EMPTY";
		String zip = "-1";
		String employees = "-1";
		while (din.hasNext())
		{
			rank = din.next();
			name = din.next();
			city = din.next();
			state = din.next();
			zip = din.next();
			employees = din.next();
			record = din.nextLine();
			System.out.println(rank);
			System.out.println(name);
			System.out.println(city);
			System.out.println(state);
			System.out.println(zip);
			System.out.println(employees);
			dout.write(rank);
			dout.write(name);
			dout.write(city);
			dout.write(state);
			dout.write(zip);
			dout.write(employees);
		}
		din.close();
		dout.close();
		/*
		RandomAccessFile dataIn = new RandomAccessFile(csv, "r");
		RandomAccessFile dataOut = new RandomAccessFile(data, "rw");
		String record = "";
		int recordNumber = 1;
		while (recordNumber <= NUM_RECORDS)
		{
			record = getRecord(dataIn, recordNumber);
			System.out.println(record);
			dataOut.seek(recordNumber * RECORD_SIZE);
			dataOut.writeBytes(record);
			recordNumber++;
		}
		dataOut.close(); //Take this out later
		dataIn.close();
		*/
	}
	
	public boolean open() 
	{
		return true;
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
