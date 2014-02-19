import java.io.*;
import java.util.*;
/**
* The Bestbean class is the main class of the application. It will prompt for input 
* and display the appropriate output to the user. It also only has knowledge of the Store class.
* @author Lance Baker.
*/
public class Bestbean {
	private static final String WELCOME_MESSAGE = "Welcome to Bestbean coffee replenishment system";
	private static final String SPACE = " ";
	private static final String COLON = ":";
	private static final String EMPTY_STRING = "";
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String ERROR_PREFIX = "Error: ";
	private static final String ERROR_POSITIVE_AMOUNT_REQUIRED = ERROR_PREFIX + "The amount must be greater than zero.";
	private static final String ERROR_NOT_NUMERIC = ERROR_PREFIX + "Must only enter a numeric value.";
	private static final String ERROR_NOT_INTEGER = ERROR_PREFIX + "Must only enter a integer value.";
	private static final String SEPARATOR = "---------------------";
	private static final String OPTION_SEPARATOR = ") ";
	private static final String INPUT_MENU_OPTION = "Option #: ";
	private static final String INPUT_MENU_OPTION_ERR = ERROR_PREFIX + " Please enter a number corresponding to the options shown on the menu.";
	private static final String[] PRODUCT_EXISTS_MENU = {"Input another product name", "Change the data of this product"};
	private static final String[] MAIN_MENU = {"Choose Store", "Display Stores", "Open", "Save", "Exit"};
	private static final String[] STORE_MENU = {"Add/Edit product", "Delete product", "Display product", 
																	"Display all products", "Display all products with the profit", "Exit"};
	private static final String TITLE_PRODUCT_EXISTS_MENU = "Product Exists";
	private static final String TITLE_MAIN_MENU = "Main Menu";
	private static final String TITLE_STORE_MENU = "Store Menu";
	private static final String INPUT_PRODUCT = "Please input the name of the product: (type 'cancel' to exit)";
	private static final String KEYWORD_CANCEL = "cancel";
	private static final String ERROR_PRODUCT_EXISTS = "Product already exists";
	private static final String ERROR_STORE_NOT_FOUND = "Error: Store doesn't exist.";
	private static final String INPUT_STORE_NAME = "Please, input the name of the store:";
	private static final String INPUT_PRODUCT_NAME = "Please, input the name of the product:";
	private static final String INPUT_DEMAND_RATE = "Please, input the demand rate of %s:";
	private static final String INPUT_SETUP_COST = "Please, input the setup cost of %s:";
	private static final String INPUT_UNIT_COST = "Please, input the unit cost of %s:";
	private static final String INPUT_INVENTORY_COST = "Please, input the inventory cost of %s:";
	private static final String INPUT_SELLING_COST = "Please, input the selling price of %s:";
	private static final String INPUT_WEEKS = "Please, input the number of weeks:";
	
	private static final String MSG_FILE_LOADED = "The data was successfully loaded." + NEW_LINE;
	private static final String MSG_PRODUCT_DELETED = "The product was deleted." + NEW_LINE;
	private static final String MSG_STORE_IS_EMPTY = "No products";
	private static final String ERROR_PRODUCT_NOT_FOUND = "The product does not exist." + NEW_LINE;
	
	private static final String INPUT_BOOLEAN_CRITERIA = "[Yes or No]: ";
	private static final String INPUT_BOOLEAN_YES = "Yes";
	private static final String INPUT_BOOLEAN_NO = "No";
	private static final String INPUT_BOOLEAN_ERR = ERROR_PREFIX + "Must input either Yes or No.";
	private static final String SHOW_REPLENISHMENT_STRATEGY = "Show replenishment strategy? " + INPUT_BOOLEAN_CRITERIA;
	
	private static final String INPUT_FILE_NAME = "Input file name:";
	private static final String ERROR_FILE_NOT_EXISTS = "the file does not exist";
	
	// The stores are instantiated when the program is loaded, and the instances are placed within
	// the static stores array.
	private static Store[] stores = {new Store("Lambton"), new Store("Callaghan")};
	
	/**
	* The inputNumber method is used for retrieving a numeric value from the user. It is used by both the inputDouble, and also the 
	* inputInteger methods. It is designed generically to avoid the repeated logic that would have occurred if a separate method
	* was indeed created for retrieving an integer. The method receives both a message String, and also a boolean value indicating whether a
	* Double or an Integer is required. It will disallow anything else other than the desired numeric type to be entered, and reprompt the user to enter a 
	* valid type with a friendly error message.
	* @param message String
	* @param isDouble boolean
	* @return double - The received value. In the circumstance that it is an Integer, than the value will have a zero decimal, 
	*				   which can be cutoff by simply casting it to an int.
	*/
	private static double inputNumber(String message, boolean isDouble) {
		double value = 0;
		do {
			try {
				String input = inputString(message);
				// converts the String to either a Double or an Integer (depending on the isDouble boolean paramater received).
				value = ((isDouble)? Double.valueOf(input).doubleValue() : Integer.valueOf(input).intValue());
				if (value <= 0) {
					System.out.println(ERROR_POSITIVE_AMOUNT_REQUIRED + NEW_LINE);
				}
			} catch (NumberFormatException ex) {
				System.out.println(((isDouble)? ERROR_NOT_NUMERIC : ERROR_NOT_INTEGER) + NEW_LINE);
			}
		} while (value <= 0);
		return value;
	}
	
	/**
	* The inputDouble method is used throughout the cli in order to receive a double value from the user. 
	* It uses the inputNumber method in order to receive the double value. The method exists purely as a wrapper, 
	* and to eliminate the overhead of passing a boolean each time.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return double
	*/
	private static double inputDouble(String message) {
		return inputNumber(message, true);
	}
	
	/**
	* The inputInteger method is used throughout the cli in order to receive a integer value from the user.
	* It uses the inputNumber method in order to receive the integer value. Like the inputDouble method, it behaves as an
	* wrapper. It eliminates the need of passing the boolean, and also casting the retrieved double to an int.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return int
	*/
	private static int inputInteger(String message) {
		return (int)inputNumber(message, false);
	}
	
	/**
	* The inputString method is the singular point in the application which prompts the user for input. The other 
	* methods use this method for the retrieval of a String that is then validated & converted to other primative data
	* types such as int and double. In the event that the user presses Control + C (or an equivalent in another OS) it will
	* recognise that no input has been received and throw an NoSuchElementException; which is then caught to end the application.
	* @param message String - The input message that you wish to be prompted to the user.
	*/
	private static String inputString(String message) {
		String input = EMPTY_STRING;
		try {
			System.out.println(message);
			input = new Scanner(System.in).nextLine().trim();
		// Catches the 'no such element' exception thrown by pressing CTRL + C.
		} catch (NoSuchElementException ex) {
			System.exit(0); // Terminates the program once caught.
		}
		return input;
	}
	
	/**
	* The inputBoolean method is method for retrieving an boolean value from the user. It prompts the received input message paramater, 
	* and only enables the user to input a Yes or No response. If a value other than Yes or No is received, it will display a friendly 
	* error message stating the required input, and re-prompt for input until a valid value is given. Once a valid value is received, it will
	* return a boolean indicating the response.
	* @param message String - The input message that you wish to be prompted to the user.
	* @return boolean - Either being true (Yes) or false (No).
	*/
	private static boolean inputBoolean(String message) {
		String input;
		boolean value = false;
		do {
			input = inputString(message);
			// Checks whether the received String is either equal to Yes or No (ignoring case).
			if (input.equalsIgnoreCase(INPUT_BOOLEAN_YES) || input.equalsIgnoreCase(INPUT_BOOLEAN_NO)) {
				// If the input received is valid, it will assign the boolean response from the equals test to the value variable.
				value = input.equalsIgnoreCase(INPUT_BOOLEAN_YES);
			} else {
				// Otherwise, it will show an friendly error message.
				System.out.println(INPUT_BOOLEAN_ERR + NEW_LINE);
			}
		// It will iterate until a valid input is given.
		} while (!(input.equalsIgnoreCase(INPUT_BOOLEAN_YES) || input.equalsIgnoreCase(INPUT_BOOLEAN_NO)));
		// Returns the answer as a boolean.
		return value;
	}
	
	/**
	* The displayTitle method is used to display a nice & consistent title based on the received String. 
	* It is used to ensure consistency amongst all menu sections to keep the user well informed about
	* the operation that they are performing.
	*/
	private static void displayTitle(String title) {
		System.out.println(NEW_LINE + SEPARATOR);
		System.out.println(title);
		System.out.println(SEPARATOR);
	}
	
	/**
	* The displayMenu is a generic method which displays a menu based on the received String[] menu parameter.
	* It is used by the mainMenu, storeMenu, and the addEditProduct methods. It uses the displayTitle
	* method to print a title based on the received String title parameter. When displaying the menu, it outputs
	* a number (starting at 1) based on the iteration along with the String element. After, it prompts the user for
	* a menu option using the inputInteger method. If the menu option is outside of the menu size, it will show an 
	* error message, and prompt for reinput. It then returns the selection made.
	* @param title String - The title to be displayed for the menu.
	* @param menu String[] - The menu String array, which contains the options.
	* @return int - The menu selection.
	*/
	private static int displayMenu(String title, String[] menu) {
		displayTitle(title); // Displays the title.
		// Iterates for each menu option
		for (int i = 0; i < menu.length; i++) {
			System.out.println((i + 1) + OPTION_SEPARATOR + menu[i]);
		}
		System.out.println(SPACE);
		// Prompts for menu option
		int selection = 0;
		do {
			selection = inputInteger(INPUT_MENU_OPTION);
			if (selection > menu.length) { // If greater than the length, it will show an error.
				System.out.println(INPUT_MENU_OPTION_ERR + NEW_LINE);
			}
		} while (!(selection > 0 && selection <= menu.length));
		return selection;
	}
	
	/**
	* The findStore method receives the String name of the store that wants to be found. It will
	* iterate throughout the stores within the array, until a store's name matches the received value.
	* @param name String - The store's name that is desired to be found.
	* @return Store - The store that matches the received name.
	*/
	private static Store findStore(String name) {
		for (Store store : stores) { // Iterates through each store.
			if (store.getName().equalsIgnoreCase(name)) { // If a store's name matches.
				return store; // Returns the object.
			}
		}
		return null; // Otherwise if not found, returns null.
	}
	
	/**
	* The addEditProduct method is used to either add a new product, or edit an existing product. The store
	* object is received, and it will prompt the user to enter a product name. If it can find the product within
	* the store, then it will present another menu allowing the user to either enter a new name, or edit the product
	* contents. Once a unique name is given or a product wishes to be edited, it will then prompt the user for the inputs
	* (demand rate, setup cost, unit cost, inventory cost, selling cost) and it will either add the product or edit the existing one.
	* @param store Store - The store object that has the products.
	*/
	private static void addEditProduct(Store store) {
		// Prompts the user for a product name.
		String product = inputString(INPUT_PRODUCT).toLowerCase();
		// If the product is not equal to the cancel keyword.
		if (!product.equalsIgnoreCase(KEYWORD_CANCEL)) {
			// It will attempt to find the product in the store (returning the index position).
			int index = store.findProduct(product);
			// If the product exists.
			if (index >= 0) {
				// It will display the menu prompting for a option, and if the option given is 
				// the first one (to enter a new name).
				if (displayMenu(TITLE_PRODUCT_EXISTS_MENU, PRODUCT_EXISTS_MENU) == 1) {
					// It will then perform a loop prompting for a unique name.
					do {
						// Prompts the user for a product name. 
						product = inputString(INPUT_PRODUCT);
						// If the product is not equal to the cancel keyword.
						if (!product.equalsIgnoreCase(KEYWORD_CANCEL)) {
							// It will attempt to find the product in the store (returning the index position).
							index = store.findProduct(product);
							// If the product exists it will display an error message.
							if (index >= 0) {
								System.out.println(ERROR_PRODUCT_EXISTS);
							}
						}
					// Iterates while the product exists (and is not unqiue) and the product name is not equal to the cancel keyword.
					} while (index >= 0 && (!product.equalsIgnoreCase(KEYWORD_CANCEL)));
				}
			}
			// If the product is not equal to the cancel keyword.
			if (!product.equalsIgnoreCase(KEYWORD_CANCEL)) {
				// It will then prompt for the demand rate, setup cost, unit cost, inventory cost, and selling cost.
				int demandRate = inputInteger(String.format(INPUT_DEMAND_RATE, product));
				double setupCost = inputDouble(String.format(INPUT_SETUP_COST, product));
				double unitCost = inputDouble(String.format(INPUT_UNIT_COST, product));
				double inventoryCost = inputDouble(String.format(INPUT_INVENTORY_COST, product));
				double sellingCost = inputDouble(String.format(INPUT_SELLING_COST, product));
				// If the product exists (meaning it was chosen to edit the product).
				if (index >= 0) {
					// Then it will edit the product at the index positon with the values inputted from the user.
					store.editProduct(index, demandRate, setupCost, unitCost, inventoryCost, sellingCost);
				// Otherwise it doesn't exist, and is therefore a new product.
				} else {
					// It will then add the product to the store.
					store.addProduct(product, demandRate, setupCost, unitCost, inventoryCost, sellingCost);
				}
			}
		}
	}
	
	/**
	* The deleteProduct method deletes a product name from the store object received. It prompts
	* the user to input a product name, and attempts to delete the product. It iterates until a valid
	* name is given or until the cancel keyword is entered.
	* @param store Store - The store object that has the products.
	*/
	private static void deleteProduct(Store store) {
		// If the store has products.
		if (store.getProductCount() > 0) {
			boolean exitFlag = false; // Iterates until exitFlag is true.
			do {
				// Prompts the user to input a product name.
				String product = inputString(INPUT_PRODUCT);
				// If the product name is not equal to the cancel keyword.
				if (!product.equalsIgnoreCase(KEYWORD_CANCEL)) {
					// If the product exists and is dleted it will return true.
					if (store.deleteProduct(product)) {
						exitFlag = true; // Sets the exit flag thus getting out of the loop.
						System.out.println(MSG_PRODUCT_DELETED); // Displays the message that the product was deleted.
					// Otherwise the product doesn't exist.
					} else {
						// Displays the error that the product wasn't found.
						System.out.println(ERROR_PRODUCT_NOT_FOUND);
					}
				} else {
					exitFlag = true; // Exits loop, since the user typed in cancel.
				}
			} while (!exitFlag); // Iterates until the exitFlag is true.
		} else {
			// Otherwise, the store is empty so it displays an error.
			System.out.println(MSG_STORE_IS_EMPTY);
		}
	}
	
	/**
	* The displayProduct method will prompt a user to enter a product name, and it will
	* display the contents of the product to the user & optionally show the replacement strategy 
	* (also then prompting the user to input the weeks for the projection). If the product was not
	* found, it will show an error message and continue the iteration until a valid product name is given.
	* The user can get out of the loop by typing in 'cancel'.
	* @param store Store - The store object that has the products.
	*/
	private static void displayProduct(Store store) {
		if (store.getProductCount() > 0) { // If the store has products.
			boolean exitFlag = false;
			// Iterates until the exitFlag is true.
			do {
				// Prompts the user for a product name.
				String product = inputString(INPUT_PRODUCT);
				// If the user doesn't type in 'cancel'.
				if (!product.equalsIgnoreCase(KEYWORD_CANCEL)) {
					// Then it will attempt to find the product.
					int index = store.findProduct(product);
					// If the product was found.
					if (index >= 0) {
						// It will then display the product.
						System.out.println(store.displayProduct(index));
						exitFlag = true; // Set the exitFlag to true to avoid recursion.
						// Asks the user whether they want to see the replacement strategy.
						boolean showStrategy = inputBoolean(SHOW_REPLENISHMENT_STRATEGY);
						if (showStrategy) { // If the user inputted yes.
							// It will then prompt the user to input the weeks for the projection.
							int weeks = inputInteger(INPUT_WEEKS);
							// It will then show the replacement strategy for that product (based on the index position)
							// and for the weeks that the user inputted.
							System.out.println(store.replacementStrategy(index, weeks));
						}
					} else {
						// If the product could not be found, it will show an error.
						System.out.println(ERROR_PRODUCT_NOT_FOUND);
					}
				} else {
					exitFlag = true; // If the user inputted cancel.
				}
			} while (!exitFlag); // It will then exit the loop.
		} else {
			// Otherwise, the store is empty so it will display an error.
			System.out.println(MSG_STORE_IS_EMPTY);
		}
	}
	
	/**
	* The displayProducts method will display the products that are within a given store. If the store
	* doesn't have any products, it will then display a error message stating that it is empty.
	* @param store Store - The store object that has the products.
	*/
	private static void displayProducts(Store store) {
		if (store.getProductCount() > 0) { // Checks whether the store has products.
			System.out.println(store.displayProducts()); // If so, it will display the products.
		} else {
			System.out.println(MSG_STORE_IS_EMPTY); // If not show a message stating it is empty.
		}
	}
	
	/**
	* The displayProductsWithProfit method will display the products with the profit projected based
	* on the number of weeks given by the user. It will check whether the received store has products,
	* and it will then prompt the user for the number of weeks, and then invoke the corresponding method
	* belonging to the store instance.
	* @param store Store - The store object that has the products.
	*/
	private static void displayProductsWithProfit(Store store) {
		if (store.getProductCount() > 0) { // Checks whether the store has products.
			System.out.println(store.displayProductsWithProfit(inputInteger(INPUT_WEEKS))); // If so, it will display the products with profit.
		} else {
			System.out.println(MSG_STORE_IS_EMPTY); // If not show a message stating it is empty.
		}
	}
	
	/**
	* The saveData method receives the filename that you wish the data to be saved as. It will then create a 
	* PrintWriter object used for outputting the data. It then iterates for each store object, and it will display
	* the store name with a colon afterwards, then it will display the products within the store.
	* @param filename String - The filename to be saved as.
	*/
	private static void saveData(String filename) {
		try {
			FileWriter outData = new FileWriter(filename);
			PrintWriter outFile = new PrintWriter(outData, true);
			for (int i = 0; i < stores.length; i++) { // Iterates through each of the stores.
				// Outputs the store name with a colon afterwards.
				outFile.println(((i > 0) ? NEW_LINE : EMPTY_STRING) + stores[i].getName() + COLON);
				// If the store has products.
				if (stores[i].getProductCount() > 0) {
					// Displays the products in the store.
					outFile.println(NEW_LINE + stores[i].displayProducts());
				}
			}
		} catch (Exception ex) {
			// Displays any errors to the user.
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	* The loadData method accepts a filename, and attempts to open that file. It will then iterate throughout the file
	* reading in the data, adding the products into the corresponding store objects.
	* @param filename String - The filename to be read.
	*/
	private static void loadData(String filename) {
		try {
			// Creates a Scanner to read the data based on the received file.
			Scanner reader = new Scanner(new File(filename));
			String data = reader.nextLine(); // Reads the firstline (which should be a store).
			while (reader.hasNext()) { // Iterates throughout the file until there is no more lines.
				// Finds the store object based on the line.
				Store store = findStore(data.replaceAll(COLON, EMPTY_STRING).trim());
				// If the store was found.
				if (store != null) {
					// It will then iterate until there are no more products.
					hasProducts: while(true) {
						// If there is another line and that line is actually just empty.
						if (reader.hasNext() && reader.nextLine().isEmpty()) {
							// Then it will prepare the variables to add a new product.
							String product = EMPTY_STRING;
							int demandRate = 0;
							double setupCost = 0, unitCost = 0, inventoryCost = 0, sellingPrice = 0;
							// Iterates for each of the product fields.
							for (int i = 0; i < 6; i++) {
								// If there is another line.
								if (reader.hasNext()) {
									// Then it will fetch that line.
									data = reader.nextLine();
									// It will then work out which field it is, adding the line data to the corresponding variable.
									if (data.toLowerCase().startsWith(Product.FIELD_PRODUCT_NAME.toLowerCase())) {
										product = data.substring(data.indexOf(COLON) + 2).trim();
									} else if (data.toLowerCase().startsWith(Product.FIELD_DEMAND_RATE.toLowerCase())) {
										demandRate = Integer.parseInt(data.substring(data.indexOf(COLON) + 2).trim());
									} else if (data.toLowerCase().startsWith(Product.FIELD_SETUP_COST.toLowerCase())) {
										setupCost = Double.parseDouble(data.substring(data.indexOf(COLON) + 2).trim());
									} else if (data.toLowerCase().startsWith(Product.FIELD_UNIT_COST.toLowerCase())) {
										unitCost = Double.parseDouble(data.substring(data.indexOf(COLON) + 2).trim());
									} else if (data.toLowerCase().startsWith(Product.FIELD_INVENTORY_COST.toLowerCase())) {
										inventoryCost = Double.parseDouble(data.substring(data.indexOf(COLON) + 2).trim());
									} else if (data.toLowerCase().startsWith(Product.FIELD_SELLING_PRICE.toLowerCase())) {
										sellingPrice = Double.parseDouble(data.substring(data.indexOf(COLON) + 2).trim());
									} else {
										// If it reaches here, it means there is a string read and its not related to a product -
										// so therefore it should be a store name. So it will break out of the products loop.
										break hasProducts;
									}
								}
							}
							// After the iteration, it is then ready to add the product. But if it already exists, it will just
							// update the existing product.
							int index = store.findProduct(product);
							if (index >= 0) { // If it already exists, it will update the product with the new values.
								store.editProduct(index, demandRate, setupCost, unitCost, inventoryCost, sellingPrice);
							} else {
								// Otherwise, it will add a new product to the store.
								store.addProduct(product, demandRate, setupCost, unitCost, inventoryCost, sellingPrice);
							}
						} else {
							// If there are no more lines, or for some reason there wasn't a gap; then
							// it will break out of the add more products loop.
							break;
						}
					}
				}
			}
			reader.close(); // Closes the Scanner object.
			System.out.println(MSG_FILE_LOADED);
			for (Store s : stores) {
				System.out.println(s);
			}
		// Catches the file not found exception.
		} catch(FileNotFoundException ex) {
			// Displays an error stating the file does not exist.
			System.out.println(ERROR_FILE_NOT_EXISTS);
		} catch(IOException ex) {
			// Displays any other exception errors relating to file input.
			System.out.println(ex.getMessage());
		}
	}
	
	/**
	* The storeMenu displays menu options relating to managing the received store object. 
	* It enables for the user to: Add/Edit product, Delete product, Display product, Display all products, and
	* Display all products with the profit. It prompts a menu using the displayMenu method in a loop
	* that will continue iterating until the exit option has been selected. 
	* @param store Store - The store object.
	*/
	private static void storeMenu(Store store) {
		// Iterates until the exitFlag.
		for (boolean exitFlag = false; (!exitFlag); ) {
			try {
				// Displays the menu to the user, and prompts for an option number. It will
				// then use a switch to determine what to invoke.
				switch(displayMenu(TITLE_STORE_MENU, STORE_MENU)) {
					case 1: 
						displayTitle(STORE_MENU[0]);
						addEditProduct(store);
						break;
					case 2:
						displayTitle(STORE_MENU[1]);
						deleteProduct(store);
						break;
					case 3: 
						displayTitle(STORE_MENU[2]);
						displayProduct(store);
						break;
					case 4:
						displayTitle(STORE_MENU[3]);
						displayProducts(store);
						break;
					case 5:
						displayTitle(STORE_MENU[4]);
						displayProductsWithProfit(store);
						break;
					case 6:
						exitFlag = true;
						break;			
				}
			// Catches and displays any Exception raised as an error.
			} catch (Exception ex) {
				System.out.println(ERROR_PREFIX + ex.getMessage());
			}
		}
	}
	
	/**
	* The mainMenu method is the starting point to the application. It prompts a menu using the displayMenu method in a loop
	* that will continue iterating until the exit option has been selected. 
	* It enables for the user to Choose Store, Display Stores, Open, Save, and Exit.
	*/
	public static void mainMenu() {
		// Iterates until the exitFlag.
		for (boolean exitFlag = false; (!exitFlag); ) {
			try {
				// Displays the menu to the user, and prompts for an option number. It will
				// then use a switch to determine what to invoke.
				switch(displayMenu(TITLE_MAIN_MENU, MAIN_MENU)) {
					// The first option is to 'Choose Store'. It will prompt the user
					// to input a store name, and will iterate until a store is actually found.
					// Once a store is found, it will then invoke the storeMenu, passing in the 
					// store object.
					case 1:
						displayTitle(MAIN_MENU[0]);
						Store store; 
						do { // Iterates until the store is found.
							// Finds the store based on a given name.
							store = findStore(inputString(INPUT_STORE_NAME));
							if (store != null) { // If the store was found.
								storeMenu(store); // It will then invoke the store menu.
							} else {
								// Otherwise, it will show an error to the user.
								System.out.println(ERROR_STORE_NOT_FOUND + NEW_LINE);
							}
						} while(store == null);
						break;
					// The second option is 'Display Stores'. It will iterate
					// throughout each store, and it will display the store using its .toString().
					case 2:
						displayTitle(MAIN_MENU[1]);
						for (Store s : stores) {
							System.out.println(s);
						}
						break;
					// The third option is 'Open'. It will prompt the user for a file to be read,
					// and it will then pass the receive filename to the loadData method.
					case 3:
						displayTitle(MAIN_MENU[2]);
						loadData(inputString(INPUT_FILE_NAME));
						break;
					// The fourth option is 'Save'. It will prompt the user for a filename for the 
					// data to be saved into, and it will invoke the saveData method passing in that filename.
					case 4: 
						displayTitle(MAIN_MENU[3]);
						saveData(inputString(INPUT_FILE_NAME));
						break;
					// Exits the application by setting the exitFlag to true.
					case 5:
						exitFlag = true;
						break;
				}
			// Catches and displays any Exception raised as an error.
			} catch (Exception ex) {
				System.out.println(ERROR_PREFIX + ex.getMessage());
			}
		}
	}
	
	/**
	* The main method launches the program.
	*/
	public static void main(String[] args) {
		System.out.println(WELCOME_MESSAGE + NEW_LINE); // Shows a welcome message.
		mainMenu(); // Invokes the mainMenu(), thus starting the application.
	}
}