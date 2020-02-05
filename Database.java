import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Database {
	
	private static int numRecords = 0;
	private static int recordNum = 0; //Used for updateRecord
	private static int overflowNum = 0; //Used for overflow record
	private static int recordSize = 84; //83 bytes per line + 1 byte per newline char
	public static boolean isOpen = false;
	private FileWriter dout;
	private FileWriter configOut;
	private BufferedReader din;
	private BufferedReader cin;
	private RandomAccessFile oin;
	private String prefix;

	
	public Database()
	{
		numRecords = 0;
		overflowNum = 0;
		prefix = "";
	}
	
	public void create()
	{
		Scanner in = new Scanner(System.in);
		System.out.print("Please enter the name of a .csv file: ");
		prefix = in.nextLine();
		String csv = prefix + ".csv";
		String con = prefix + ".config"; //when close DB, update config file
		String data = prefix + ".data";
		String over = prefix + ".overflow";
		System.out.println("Input File: " + csv + "\n" + "Output Files: " + con + ", " + data + ", " + over);
		
		//File I/O
		try
		{
			din = new BufferedReader(new FileReader(csv)); //Input stream
			configOut = new FileWriter(new File(con)); //config filewriter & creates config file
			dout = new FileWriter(new File(data)); //.data filewriter & creates data file
			//oin = new RandomAccessFile(new File(over), "r"); //overflow filewriter & creates overflow file
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
			///oin.close();
			
			//Write to config file
			String writeNum = Integer.toString(numRecords); //converts to string
			String writeOver = Integer.toString(overflowNum);
			configOut.write(writeNum);
			configOut.write("\n");
			configOut.write(writeOver);
			configOut.close();
			
		}
		catch (IOException e) 
		{
			e.printStackTrace();
		}
		System.out.println("Your file contained " + numRecords + " records");
		isOpen = false;
	}
	
	public boolean open() throws IOException
	{
		if (!isOpen)
		{
			Scanner in = new Scanner(System.in);
			System.out.print("Enter the prefix for a pre-exisisting database to open: ");
			prefix = in.nextLine();
			try 
			{
				//Open all files
				FileReader testD = new FileReader(prefix + ".data"); //If these open, database exists
				FileReader testC = new FileReader(prefix + ".config");
				FileReader testO = new FileReader(prefix + ".overflow");
				cin = new BufferedReader(testC); //Gets numrecords & overflowNum from config file
				
				//Read numRecords and overflowNum from config
				String numR = cin.readLine();
				numRecords = Integer.parseInt(numR);
				String numO = cin.readLine();
				overflowNum = Integer.parseInt(numO);
				testD.close();
				testC.close();
				testO.close();
				//System.out.println(overflowNum);
				
				//Set flag
				System.out.println("Database successfully opened");
				System.out.println("Database contains " + numRecords + " records");
				System.out.println("Overflow file contains " + overflowNum + " records");
				isOpen = true;
				return isOpen;
			} 
			catch (FileNotFoundException e) {
				e.printStackTrace();
				System.out.println("ERROR: File does not exist");
				return isOpen;
			}
		}
		else
		{
			System.out.println("ERROR: Another database is open.");
			return isOpen;
		}
	}
	
	public boolean close()
	{
		if(isOpen)
		{
			try 
			{
				Scanner in = new Scanner(System.in);
				configOut = new FileWriter(prefix + ".config");
				String writeNum = Integer.toString(numRecords); //converts numRecords to string so can be written to config file
				String writeOver = Integer.toString(overflowNum);
				configOut.write(writeNum);
				configOut.write("\n");
				configOut.write(writeOver);
				configOut.close();
				numRecords = 0;
				prefix = "";
				System.out.println("Database successfully closed");
				isOpen = false;
				return isOpen;
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				System.out.println("No database is open");
				return isOpen;
			}
		}
		else
		{
			System.out.println("No databases are open");
			return isOpen;
		}
		
	}
	
	public void display() throws IOException
	{
		if (isOpen)
		{
			Scanner in = new Scanner(System.in);
			try 
			{
				RandomAccessFile din = new RandomAccessFile(prefix + ".data", "r");
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
		else
		{
			System.out.println("ERROR: No open database, try opening.");
		}
	}
	
	public void update() throws IOException
	{
		if (isOpen)
		{
			Scanner in = new Scanner(System.in);
			String[] fields = new String[5];
			System.out.print("Enter name of company to update: ");
			String search = in.nextLine();
			try 
			{
				search = search.replace(" ", "_");
				RandomAccessFile din = new RandomAccessFile(prefix + ".data", "rw");
				String record = binarySearch(din, search); //updates recordNum so can find position in file
				if (record == "NOT_FOUND")
				{
					return;
				}
				System.out.println("Found record: " + record);
				
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
		else
		{
			System.out.println("ERROR: No open database, try opening");
		}
		
	}
	
	public void createReport() throws IOException
	{
		if (isOpen)
		{
			RandomAccessFile din = new RandomAccessFile(prefix + ".data", "r");
			FileWriter dout = new FileWriter(new File(prefix + ".report.txt"));
			System.out.println("Check " + prefix + ".report.txt for your report aswell");
			System.out.println("Here are the top 10 companies in your Database:");
			for (int i = 1; i <= 10; i++) 
			{
				String record = getRecord(din, i);
				System.out.println(record);
				record = record + "\n";
				dout.write(record);
			}
			dout.close();
		}
		else
		{
			System.out.println("ERROR: No open database, try opening");
		}
	
	}
	
	public void addRecord() throws IOException
	{
		if (isOpen)
		{
			try 
			{
				if(overflowNum < 4)
				{
					String[] fields = new String[6];
					Scanner in = new Scanner(System.in);
					oin = new RandomAccessFile(prefix + ".overflow", "rw");
					System.out.print("Enter the fortune 500 Rank of the company: ");
					fields[0] = in.nextLine();
					if (fields[0].length() > 4)
					{
						fields[0] = fields[0].substring(0, 4);
					}
					System.out.print("Enter the name of the company: ");
					fields[1] = in.nextLine();
					if (fields[1].length() > 40)
					{
						fields[1] = fields[1].substring(0, 40);
					}
					System.out.print("Enter the city of the company: ");
					fields[2] = in.nextLine();
					if (fields[2].length() > 20)
					{
						fields[2] = fields[2].substring(0, 20);
					}
					System.out.print("Enter the state of the company: ");
					fields[3] = in.nextLine();
					if (fields[3].length() > 3)
					{
						fields[3] = fields[3].substring(0, 3);
					}
					System.out.print("Enter the zipcode of the company: ");
					fields[4] = in.nextLine();
					if (fields[4].length() > 6)
					{
						fields[4] = fields[4].substring(0, 6);
					}
					System.out.print("Enter the employee count of the company: ");
					fields[5] = in.nextLine();
					if (fields[5].length() > 10)
					{
						fields[5] = fields[5].substring(0, 10);
					}
					for (int i = 0; i < fields.length; i++)
					{
						System.out.println(fields[i]);
					}
					oin.seek(0);
					oin.skipBytes(overflowNum * recordSize);
					writeRandomRecord(oin, fields);
					System.out.println("Successfully wrote to overflow file");
					overflowNum++;
					System.out.println("There are " + overflowNum + " records in your overflow file.");
					System.out.println("Once there are 4 records in overflow, select 'Add Record' to merge with .data!");
					oin.close();
				}
				else
				{
					System.out.println("Merging overflow and database");
					RandomAccessFile din = new RandomAccessFile(prefix + ".data", "rw");
					oin = new RandomAccessFile(prefix + ".overflow", "r");
					//RandomAccessFile dout = new RandomAccessFile(new File(prefix + ".data"), "rw");
					
					//Read overflow into ArrayList
					ArrayList<String> over = new ArrayList<String>();
					for (int i = 0; i < overflowNum; i++)
					{
						oin.seek(0);
						oin.skipBytes(i * recordSize);
						String overRec = oin.readLine();
						overRec = overRec + "\n";
						over.add(overRec);
					}
			
					for (int i = 0; i < numRecords; i++)
					{
						din.seek(0);
						din.skipBytes(i * recordSize);
						String dataRec = din.readLine();
						String subdata = dataRec.substring(0,4);
						if(subdata.compareTo("-100") == 0)
						{
							System.out.println("Removed " + dataRec);
							numRecords--;
							continue;
						}
						else
						{
							dataRec = dataRec + "\n";
							over.add(dataRec);
						}
					}
					
					//Sort ArrayList in alphabetical order
					Collections.sort(over, new Comparator<String>() {
					    @Override
					    public int compare(String s1, String s2) {
					    	s1 = s1.substring(4, 44);
					    	s2 = s2.substring(4, 44);
					        return s1.compareToIgnoreCase(s2);
					    }
					});
					for (int i = 0; i <= numRecords; i++)
					{
						din.seek(0);
						din.skipBytes(i * recordSize);
						din.writeBytes(over.get(i));
					}
					din.close();
					oin.close();
					//Clear out overflow file
					PrintWriter clear = new PrintWriter(prefix + ".overflow");
					clear.print("");
					clear.close();
					numRecords = overflowNum + numRecords;
					System.out.println("There are now " + numRecords + " records in .data");
					overflowNum = 0;
				}
			}
					
			catch (FileNotFoundException e)
			{
				e.printStackTrace();
				System.out.println("File could not be located");
			}
		}
		
		else
		{
			System.out.println("No databases are open");
		}
	}
	
	public void deleteRecord() throws IOException //Leaves city field the same so binary search functions
	{
		if (isOpen)
		{
			Scanner in = new Scanner(System.in);
			System.out.print("Enter name of company to delete record for: ");
			String search = in.nextLine();
			try 
			{
				search = search.replace(" ", "_");
				RandomAccessFile din = new RandomAccessFile(prefix + ".data", "rw");
				String record = binarySearch(din, search); //updates recordNum so can find position in file
				if (record == "NOT_FOUND")
				{
					return;
				}	
				//rank
				String rank = "-100";
				rank = String.format("%-4s", rank);
				String subRec = record.substring(0, 4);
				record = record.replace(subRec, rank);
				//city
				String city = "DELETED";
				city = String.format("%-20s", city);
				String subRec1 = record.substring(44, 64);
				record = record.replace(subRec1, city);
				//state
				String state = "NA";
				state = String.format("%-3s", state);
				String subRec3 = record.substring(64, 67);
				record = record.replace(subRec3, state);
				//zip
				String zip = "-1";
				zip = String.format("%-6s", zip);
				String subRec4 = record.substring(67, 73);
				record = record.replace(subRec4, zip);
				//employee
				String empCount = "-1";
				empCount = String.format("%-10s", empCount);
				String subRec5 = record.substring(73, 83);
				record = record.replace(subRec5, empCount);
				
				//write record
				record = record + "\n";
				din.seek(0);
				din.skipBytes(recordNum * recordSize);
				din.write(record.getBytes());
			
				din.close();
			}
			
			catch (FileNotFoundException e) 
			{
				e.printStackTrace();
				System.out.println("File could not be opened");
			}
		}
		else
		{
			System.out.println("ERROR: No open database, try opening");
		}
	}

	
	public static String getRecord(RandomAccessFile Din, int recordNum) throws IOException 
	{
	   String record = "NOT_FOUND";
       if ((recordNum >= 1) && (recordNum <= numRecords))
       {
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
     
            int result = MiddleId.compareTo(id);
            if (result == 0) //ID's match
            {
              	recordNum = Middle; //Used for update
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
    
    public static void writeRandomRecord(RandomAccessFile dout, String[] fields) throws IOException
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
			dout.writeBytes(fields[i]);
		}
		dout.writeBytes("\n");
    }
    
    public int getNumRecords()
    {
    	return numRecords;
    }
    
    public int getOverflowNum()
    {
    	return overflowNum;
    }
}

