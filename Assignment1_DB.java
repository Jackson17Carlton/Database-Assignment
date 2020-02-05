import java.util.Scanner;
import java.io.IOException;

public class Assignment1_DB {
	public static void printMenu() 
	{
		//Menu choices
		System.out.println("Choose from the following list of options (enter integer): ");
		System.out.println("1.) Create a New Database");
		System.out.println("2.) Open Database");
		System.out.println("3.) Close Database");
		System.out.println("4.) Display Record");
		System.out.println("5.) Update Record");
		System.out.println("6.) Create Report");
		System.out.println("7.) Add Record");
		System.out.println("8.) Delete Record");
		System.out.println("9.) Quit");
		System.out.print("Input: ");
	}
	public static void main(String[] args) throws IOException
	{
		Scanner input = new Scanner(System.in);
		Database DB = new Database(); //Creates a Database object for menu methods
		printMenu();
		int choice = input.nextInt();
		//System.out.println(choice);
		
		while (choice != 9)
		{
			if (choice == 1) {
				System.out.println("---Create a Database---");
				DB.create();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 2) {
				System.out.println("---Open a Database---");
				DB.open();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 3) {
				System.out.println("---Close a Database---");
				DB.close();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 4) {
				System.out.println("---Display a Record---");
				DB.display();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 5) {
				System.out.println("---Update a Record---");
				DB.update();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 6) {
				System.out.println("---Creating report---");
				DB.createReport();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 7) {
				System.out.println("---Add a new Record---");
				DB.addRecord();
				printMenu();
				choice = input.nextInt();
			}
			else if (choice == 8) {
				System.out.println("---Delete a Record---");
				DB.deleteRecord();
				printMenu();
				choice = input.nextInt();
			}
			else
			{
				System.out.print("Invalid input, enter choice from the list: ");
				choice = input.nextInt();
			}
		}
		while (DB.isOpen)
		{
			System.out.println("You left the database open!");
			System.out.println("Closing database automatically...");
			DB.close();
		}
		System.out.println("Quitting...");
		System.exit(0);
	}
}
