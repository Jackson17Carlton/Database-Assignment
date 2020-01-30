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
	private String testStr = "f";
	
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
				fields[0] = String.format("%-4s", fields[0]); //Rank
				//fields[1] = fields[1].replace(' ','_'); //Replaces whitespace w underscores
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
	
	public boolean open()
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Enter the prefix for a pre-exisisting database to open: "); //REMINDER: Take out
		String prefix = in.nextLine();
		File test = new File(prefix + ".data");
		if (test.exists()) //If file exists, it is "open"
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public boolean close()
	{
		try 
		{
			Scanner in = new Scanner(System.in);
			System.out.print("Enter the prefix for a pre-exisisting database to close: "); //REMINDER: Take out
			String prefix = in.nextLine();
			configOut = new FileWriter(prefix + ".config");
			//System.out.print(numRecords);
			String writeNum = Integer.toString(numRecords); //converts numRecords to string so can be written to config file
			configOut.write("Updated Number of Files: " + writeNum);
			numRecords = 0;
			configOut.close();
			return true;
		} 
		catch (IOException e) 
		{
			e.printStackTrace();
			return false;
		}
	}
	
	public void display() throws IOException
	{
		Scanner in = new Scanner(System.in);
		RandomAccessFile din = new RandomAccessFile(testStr + ".data.txt", "r");
		System.out.print("Enter name of company to search for: ");
		String search = in.nextLine();
		String record = binarySearch(din, search);
		System.out.println("Found record: " + record);
		din.close();
	}
	
	public void update() throws IOException
	{
		Scanner in = new Scanner(System.in);
		RandomAccessFile din = new RandomAccessFile(testStr + ".data.txt", "r");
		System.out.print("Enter name of company to update: ");
		String search = in.nextLine();
		
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
            System.out.println(result);
            if (result == 0)   // ids match
                Found = true;
            else if (result < 0)
                Low = Middle + 1;
            else
                High = Middle - 1;
        }
        if (Found)
           return record;
        else
           return "NOT_FOUND";
	  }
}

