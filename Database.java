import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.io.IOException;

public class Database {
	
	private static int numRecords = 0;
	private static int recordSize = 84; //83 bytes per line + 1 byte per newline char
	private FileWriter dout;
	private FileWriter configOut;
	private FileWriter overOut;
	private BufferedReader din;
	private BufferedReader cin;
	private String testStr = "f";
	private static boolean isOpen = false;
	private static int recordNum = 0; //Used for updateRecord
	
	public Database()
	{
		numRecords = 0;
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
			din = new BufferedReader(new FileReader(csv)); //Input stream
			configOut = new FileWriter(new File(con)); //config filewriter & creates config file
			dout = new FileWriter(new File(data)); //.data filewriter & creates data file
			overOut = new FileWriter(new File(over)); //overflow filewriter & creates overflow file
			String record = din.readLine();
			while (record != null)
			{
				//System.out.println("Here is a record: " + record);
				String[] fields = record.split(",");
				writeRecord(dout, fields);
				numRecords++; //Updates for every record that is written
				record = din.readLine();
			}
			
			//Close streams
			din.close();
			dout.close();
			overOut.close();
			
			//Write to config file
			String writeNum = Integer.toString(numRecords); //converts to string
			configOut.write(writeNum);
			configOut.close();
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Your file contained " + numRecords + " records");
	}
	
	public boolean open() throws IOException//Needs work
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database to open: "); //REMINDER: Take out
		String prefix = in.nextLine();
		try 
		{
			//Open all files
			FileReader dr = new FileReader(prefix + ".data");
			FileReader cr = new FileReader(prefix + ".config");
			din = new BufferedReader(dr); //Input files are .data and .config
			cin = new BufferedReader(cr);
			//dout = new FileWriter(prefix + ".data"); //From here and below are output files
			//configOut = new FileWriter(prefix + ".config");
			//overOut = new FileWriter(prefix + ".overflow");
			
			//Read numRecords from config
			String rec = cin.readLine();
			numRecords = Integer.parseInt(rec);
	
			//Set flag
			isOpen = true;
			return isOpen;
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
			isOpen = false;
			return isOpen;
		}
	}
	
	public boolean close() //Needs work
	{
		try 
		{
			Scanner in = new Scanner(System.in);
			System.out.print("Enter the prefix for a pre-exisisting database to close: "); //REMINDER: Take out
			String prefix = in.nextLine();
			configOut = new FileWriter(prefix + ".config");
			//System.out.print(numRecords);
			String writeNum = Integer.toString(numRecords); //converts numRecords to string so can be written to config file
			configOut.write(writeNum);
			numRecords = 0;
			configOut.close();
			isOpen = false;
			return isOpen;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			isOpen = true;
			return isOpen;
		}
	}
	
	public void display() throws IOException //STILL RUNS ONLY ON TEST FILE
	{
		Scanner in = new Scanner(System.in);
		RandomAccessFile din;
		try 
		{
			din = new RandomAccessFile(testStr + ".data.txt", "r");
			System.out.print("Enter name of company to search for: ");
			String search = in.nextLine();
			search = search.replace(" ", "_");
			String record = binarySearch(din, search);
			System.out.println("Found record: " + record);
			din.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("File could not be opened");
		}
		
	}
	
	public void update() throws IOException //STILL RUNS ONLY ON TEST FILE
	{
		Scanner in = new Scanner(System.in);
		String[] fields = new String[5];
		System.out.print("Enter name of company to update: ");
		String search = in.nextLine();
		try 
		{
			search = search.replace(" ", "_");
			RandomAccessFile din = new RandomAccessFile(testStr + ".data.txt", "rw");
			String record = binarySearch(din, search); //updates recordNum so can find position in file
			if (record == "NOT_FOUND")
			{
				return;
			}
			System.out.println("Found record: " + record);
			System.out.println(recordNum);
			
			//Field choice menu
			System.out.println("Choose from the following list of fields to change (enter integer): ");
			System.out.println("1.) Rank");
			System.out.println("2.) City");
			System.out.println("3.) State");
			System.out.println("4.) Zipcode");
			System.out.println("5.) Employee Count");
			System.out.println("6.) Exit");
			System.out.print("Input: ");
			int choice = in.nextInt();
			String trash = in.nextLine();
			while (choice != 6)
			{
				if (choice == 1)
				{
					System.out.print("Enter new Rank (integer): ");
					String rank = in.nextLine();
					if (rank.length() > 4) //rank can only be 4 bytes
					{
						rank = rank.substring(0, 4);
					}
					rank = String.format("%-4s", rank);
					String subRec = record.substring(0, 4);
					record = record.replace(subRec, rank);
					record = record + "\n";
					din.seek(0);
					din.skipBytes(recordNum * recordSize);
					din.write(record.getBytes());
					//System.out.println(record);
					choice = 6;
				}
				else if (choice == 2)
				{
					System.out.print("Enter new City: ");
					String city = in.nextLine();
					city = String.format("%-20s", city);
					if (city.length() > 20) //city can only be 20 bytes
					{
						city = city.substring(0, 20);
					}
					String subRec = record.substring(44, 64);
					record = record.replace(subRec, city);
					record = record + "\n";
					System.out.println(record);
					System.out.println(record.length());
					din.seek(0);
					din.skipBytes(recordNum * recordSize);
					din.write(record.getBytes());
					choice = 6;
				}
				else if (choice == 3)
				{
					System.out.print("Enter new State: ");
					String state = in.nextLine();
					state = String.format("%-3s", state);
					if (state.length() > 3) //state can only be 3 bytes
					{
						state = state.substring(0, 3);
					}
					String subRec = record.substring(64, 67);
					record = record.replace(subRec, state);
					record = record + "\n";
					System.out.println(record);
					System.out.println(record.length());
					din.seek(0);
					din.skipBytes(recordNum * recordSize);
					din.write(record.getBytes());
					choice = 6;
				}
				else if (choice == 4)
				{
					System.out.print("Enter new Zipcode (integer): ");
					String zip = in.nextLine();
					zip = String.format("%-6s", zip);
					if (zip.length() > 6) //rank can only be 4 bytes
					{
						zip = zip.substring(0, 6);
					}
					String subRec = record.substring(67, 73);
					record = record.replace(subRec, zip);
					record = record + "\n";
					System.out.println(record);
					System.out.println(record.length());
					din.seek(0);
					din.skipBytes(recordNum * recordSize);
					din.write(record.getBytes());
					choice = 6;
				}
				else if (choice == 5)
				{
					System.out.print("Enter new Employee Count (integer): ");
					String empCount = in.nextLine();
					empCount = String.format("%-10s", empCount);
					if (empCount.length() > 10) //empCount can only be 10 bytes
					{
						empCount = empCount.substring(0, 10);
					}
					String subRec = record.substring(73, 83);
					record = record.replace(subRec, empCount);
					record = record + "\n";
					System.out.println(record);
					System.out.println(record.length());
					din.seek(0);
					din.skipBytes(recordNum * recordSize);
					din.write(record.getBytes());
					choice = 6;
				}
				else
				{
					System.out.print("Enter valid menu choice: ");
					choice = in.nextInt();
				}
			}
			din.close();
		} 
		catch (FileNotFoundException e) 
		{
			e.printStackTrace();
			System.out.println("File could not be opened");
		}
	}
	
	public void createReport() throws IOException //NEEDS TO WRITE TO FILE
	{
		RandomAccessFile din = new RandomAccessFile(testStr + ".data.txt", "r");
		System.out.println("Here are the top 10 companies in your Database:");
		for (int i = 1; i <= 10; i++) 
		{
			String record = getRecord(din, i);
			
			System.out.println(record);
		}
	
	}
	
	public void addRecord() throws IOException //NEEDS TO BE COMPLETED
	{
		String record = "";
		System.out.print("Enter a record to add: ");
	}
	
	public void deleteRecord() //NEEDS TO BE COMPLETED
	{
		
	}
	
	public static String getRecord(RandomAccessFile Din, int recordNum) throws IOException 
	{
	   String record = "NOT_FOUND";
       if ((recordNum >= 1) && (recordNum <= numRecords))
       {
    	   //System.out.println(recordNum + "," + recordSize); REMINDER: TESTING
           Din.seek(0); // return to the top of the file
           Din.skipBytes((recordNum - 1) * recordSize);
           record = Din.readLine();
       }
       return record;
	}

	/*Binary Search record id */
    public static String binarySearch(RandomAccessFile Din, String id) throws IOException 
    {
	    int Low = 0;
	    int High = numRecords-1;
	    int Middle;
	    String MiddleId;
	    String record = "NOT_FOUND";
	    boolean Found = false;

        while (!Found && (High >= Low)) 
        {
            Middle = (Low + High) / 2;
            record = getRecord(Din, Middle+1);
            MiddleId = record.substring(4, id.length() + 4);
            //System.out.println(MiddleId); //REMINDER: TESTING
     
            int result = MiddleId.compareTo(id);
            //System.out.println(result);
            if (result == 0) //ID's match
            {
              	recordNum = Middle;
                Found = true;
            }
            else if (result < 0) { Low = Middle + 1; }
            else { High = Middle - 1; }
        }
        if (Found)
        {
        	return record;
        }
        else
        {
        	return "NOT_FOUND";
        }
     }
    
    public static void writeRecord(FileWriter dout, String[] fields) throws IOException
    {
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
		dout.write("\n");
    }
    
    public int getNumRecords()
    {
    	return numRecords;
    }
}

