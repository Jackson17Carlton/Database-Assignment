import java.util.Scanner;
import java.io.IOException;

public class Assignment1_DB {
	public static void main(String[] args) throws IOException
	{
		Scanner input = new Scanner(System.in);
		Database DB = new Database(); //Creates a Database object for menu methods
		
		//Menu choices
		System.out.println("Choose from the following list of options: ");
		System.out.println("1.) Create a New Database");
		System.out.println("2.) Open Database");
		System.out.println("3.) Close Database");
		System.out.println("4.) Display Record");
		System.out.println("5.) Update Record");
		System.out.println("6.) Create Report");
		System.out.println("7.) Add Report");
		System.out.println("8.) Delete Record");
		System.out.println("9.) Quit");
		System.out.print("Input: ");
		int choice = input.nextInt();
		System.out.println(choice);
		
		while (choice != 9)
		{
			if (choice == 1) {
				System.out.println("Create Database");
				DB.create();
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 2) {
				System.out.println("Open Database");
				boolean open = DB.open();
				if (open) 
				{
					System.out.println("Close other database");
				}
				else 
				{
					System.out.println("Database opened");
				}
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 3) {
				System.out.println("Closing database...");
				boolean close = DB.close();
				if (close)
				{
					System.out.println("Files successfully closed");
				}
				else
				{
					System.out.println("Could not close files");
				}
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 4) {
				System.out.println("Displaying records...");
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 5) {
				System.out.println("Updating records...");
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 6) {
				System.out.println("Creating report...");
				DB.createReport();
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 7) {
				System.out.println("Adding report...");
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else if (choice == 8) {
				System.out.println("Delete record...");
				System.out.print("Choose from the above list of options: ");
				choice = input.nextInt();
			}
			else
			{
				System.out.print("Invalid input, enter choice from the list: ");
				choice = input.nextInt();
			}
		}
		System.out.println("Quitting...");
		System.exit(0);
	}
}
