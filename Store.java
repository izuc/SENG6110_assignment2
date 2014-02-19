/**
* The Store class contains the store name, and an array of Product objects. It contains methods 
* to add a product to the store, to edit an existing product, to find a product, to delete a product, and
* methods to display product data. It also contains a method calculating the replacement strategy (based on a 
* number of weeks), and will return the data as a String.
* @author Lance Baker (c3128034).
*/
public class Store {
	private static final String STRATEGY_MESSAGE = "The replacement strategy of %s store for %s is";
	private static final String COLUMN_WEEK = "Week";
	private static final String COLUMN_QUANTITY_ORDERED = "Quantity Ordered";
	private static final String COLUMN_DEMAND = "Demand";
	private static final String COLUMN_INVENTORY = "Inventory";
	private static final String DISPLAY_TOTAL_COST = "The total cost is: $%,.2f";
	private static final String DISPLAY_TOTAL_PROFIT = "The total profit is: $%,.2f";
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String STRATEGY_ERROR = "Error: not possible to have a replacement strategy with the inputs given.";
	private static final String PRODUCT_ERROR = "Error: must add a product first.";
	private static final String EMPTY_STRING = "";
	private static final String DISPLAY_STORE = "Store: %s";
	private static final String DISPLAY_PRODUCT = "Product %d: %s";
	private static final String TAB = "\t";
	private static final String DISPLAY_PROFIT = TAB + "Profit: $%,.2f";
	private static final String DISPLAY_NUMBER_OF_PRODUCTS = TAB + "Number of products: %d";
	private static final String BEST_STRATEGY = "The best replacement strategy is for %s with a profit of $%,.2f";
	private static final int INITIAL_LENGTH = 1;
	
	private String name; // The store name attribute.
	private Product[] products; // The Product object reference.
	
	/**
	* The Store constructor accepts the store name.
	* @param name String - The name of the store.
	*/
	public Store(String name) {
		this.products = new Product[INITIAL_LENGTH];
		this.setName(name);
	}
	
	/**
	* The getter for the store name.
	* @return String - The store name.
	*/
	public String getName() {
		return this.name;
	}
	
	/**
	* The setter for the store name.
	* @param String - The store name.
	*/
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	* The resizeArray method receives the size that you wish for the array of products to be changed to. 
	* It will create a new array, and iterate throughout the positions copying any not null references from 
	* the other array. It then assigns the new array to the products reference, thus overwriting the data, then
	* leaving the old array for garbage collection.
	* @param size int - The desired size of the new array.
	*/
	private void resizeArray(int size) {
		Product[] products = new Product[size]; // Creates a new Array with the received size.
		for (int i = 0; i < this.products.length; i++) { // Iterates for each element.
			if (this.products[i] != null) { // This check will remove the 'deleted' elements.
				products[i] = this.products[i]; // Copies the object reference to the new Array.
			}
		}
		this.products = products; // Overwrites the pointer to the new Array.
	}
	
	/**
	* The findProduct method receives the product name, and it will attempt to find the product within the products array that
	* matches the product name. 
	* @param product String - The product name desired to be found.
	* @return int - If it is found it will return the index position. Otherwise, it will return -1 to indicate it wasn't found.
	*/
	public int findProduct(String product) {
		for (int i = 0; i < this.products.length; i++) { // Iterates throughout the products.
			if (this.products[i] != null) { // If the product is not null.
				// Then it will attempt to compare th product name.
				if (this.products[i].getProductName().equalsIgnoreCase(product)) {
					// If they equal, then it will return the index position.
					return i;
				}
			}
		}
		return -1; // Otherwise it wasn't found, so therefore returns -1.
	}
	
	/**
	* The deleteProduct method recieves the productName, and it will attempt to find the Product based on that name.
	* If it was found, then it will set that index position to null, and then will resize the array (substracting one position).
	* @param product String - The product name desired to be deleted.
	* @return boolean - A boolean indicating whether it was removed.
	*/
	public boolean deleteProduct(String productName) {
		int index = this.findProduct(productName); // Attempts to find the product, and returns the index position if found.
		if (index >= 0) { // If it was found (and not equal to -1).
			this.products[index] = null; // Then it will change the index position to null.
			this.resizeArray(this.products.length - 1); // Resizes the array (to get rid of the null position) and subtracting one from its length.
			return true; // Returns true if deleted.
		}
		return false; // Returns false if it wasn't found.
	}
	
	/**
	* The displayProduct method receives the index position, and it will grab the product .toString() data
	* if there is a product at that position. Otherwise it will return a empty string.
	* @param index int - The index position of the product.
	* @return String - The product data.
	*/
	public String displayProduct(int index) {
		Product product = this.products[index]; // Fetches the product based on that index position.
		return (product != null) ? product.toString() : EMPTY_STRING; // Grabs the data from the .toString()
	}
	
	/**
	* The sortedProducts method will create a new Product array, and sort the objects in that array
	* based on the comparison on the title. It performs a bubblesort to sort the elements.
	* @return Product[] - The newly created & sorted product array.
	*/
	public Product[] sortedProducts() {
		// Creates a new products array, substracting one (since the last element was free to add a new product).
		Product[] products = new Product[this.products.length-1];
		// Adds the references to the new products array.
		for (int i = 0; i < products.length; i++) {
			products[i] = this.products[i];
		}
		// It will then perform a bubblesort. It will iterate throughout the entire array, and it will then
		// have a inner loop performing a sweep to swap the elements based on the title. Each sweep will be smaller
		// and smaller since the the number of iterations is substracted by the parent loop.
		for (int i = 1; i < products.length; i++) { // Iterates throughout the entire array.
			// Then for each iteration performs a sweep to swap the elements.
			for (int index = 0; index < products.length - i; index++) {
				// Compares the product name in ascending order with the next product.
				if (((products[index].getProductName()).compareTo((products[index+1].getProductName()))) > 0) {
					// It will then swap references.
					Product temp = products[index];
					products[index] = products[index+1];
					products[index+1] = temp;
				}
			}
		}
		
		return products; // Returns the sorted array.
	}
	
	/**
	* The displayProducts method will fetch the products (using the sortedProducts method) 
	* and it will iterate throughout the array. It will iterate throughout the products, and it will 
	* then fetch the product data using the toString().
	* @return String - The products within the store sorted by name.
	*/
	public String displayProducts() {
		StringBuilder builder = new StringBuilder();
		Product[] products = this.sortedProducts(); // Fetches the products as sorted.
		for (int i = 0; i < products.length; i++) { // Iterates throughout the products.
			if (products[i] != null) { // If the product is not null.
				builder.append(products[i].toString()); // It will then append the product data to the builder.
				if (i < (products.length-1)) { // If the product is not the last one in the array,.
					builder.append(NEW_LINE + NEW_LINE); // It will then append a double new line.
				}
			}
		}
		return builder.toString(); // Returns the String.
	}
	
	/**
	* The displayProductsWithProfit will display the product's name and the projected profit for the 
	* received duration in weeks. It will then work out which product had the highest profit, and display
	* it as the best strategy.
	* @param week int - The duration of the rojection.
	* @return String - The products with their projected profit. Also the product with the best strategy.
	*/
	public String displayProductsWithProfit(int weeks) {
		StringBuilder builder = new StringBuilder(); // StringBuilder to output the data.
		builder.append(NEW_LINE);
		// Creates an array to hold the profits.
		double[] profits = new double[this.products.length-1];
		// Iterates for each product in the array.
		for (int i = 0; i < this.products.length; i++) {
			// If the product is not null.
			if (products[i] != null) {
				try {
					// It will then display the product name.
					builder.append(String.format(DISPLAY_PRODUCT, (i + 1), this.products[i].getProductName()) + NEW_LINE);
					// It will then calculate the strategy, and it will then also calculate the total cost and profit.
					double[] result = this.products[i].calcCostAndProfit(this.products[i].calcStrategy(weeks));
					// It then displays the profit.
					builder.append(String.format(DISPLAY_PROFIT, result[1]) + NEW_LINE);
					// Adds the profit value to the array.
					profits[i] = result[1];
				// Catches the Exception.
				} catch (IllegalArgumentException ex) {
					builder.append(ex.getMessage() + NEW_LINE);
				}
			}		
		}
		
		int index = 0; // The index of the product with the highest product.
		// Iterates through all the profits stored.
		for (int i = 0; i < profits.length; i++) {
			// If the profit is greater than the profit at that stored index position.
			if (profits[i] > profits[index]) {
				index = i; // It will then change the index, since the iterated profit is greater than the stored one.
			}
		}
		// It will then show the product that has the highest profit.
		builder.append(String.format(BEST_STRATEGY, this.products[index].getProductName(), profits[index]) + NEW_LINE);
		return builder.toString(); // Returns it all as a String.
	}
	
	/**
	* The addProduct method receives parameters which are used for creating a new Product. It will instantiate a new Product
	* object and add it to the last position of the products array (since this is reserved for a new element). After the addition,
	* it will then resize the array by adding a new position at the end (enabling for a new product to be added).
	* @param productName String - The product name.
	* @param demandRate int - The demand rate.
	* @param setupCost double - The setup cost.
	* @param unitCost double - The unit cost.
	* @param inventoryCost double - The inventory cost.
	* @param sellingCost double - The selling cost.
	*/
	public void addProduct(String productName, int demandRate, double setupCost, 
										double unitCost, double inventoryCost, double sellingCost) {
		this.products[this.products.length - 1] = new Product(productName, demandRate, setupCost, unitCost, inventoryCost, sellingCost);
		this.resizeArray(this.products.length + 1);
	}
	
	/**
	* The editProduct method receives parameters which are used to edit an existing Product. The first param is the index
	* position of the product in the array, and so it will fetch the Product in the position; then it will use the setters
	* to mutate the contents of the product with the received values.
	* @param index int - The index position of the product to be edited.
	* @param demandRate int - The demand rate.
	* @param setupCost double - The setup cost.
	* @param unitCost double - The unit cost.
	* @param inventoryCost double - The inventory cost.
	* @param sellingCost double - The selling cost.
	*/
	public void editProduct(int index, int demandRate, double setupCost, 
										double unitCost, double inventoryCost, double sellingCost) {
		Product product = this.products[index]; // Grabs the product at the index position.
		if (product != null) { // If the product is not null.
			product.setDemandRate(demandRate); // Sets the demand rate.
			product.setSetupCost(setupCost); // Sets the setup cost.
			product.setUnitCost(unitCost); // Sets the unit cost.
			product.setInventoryCost(inventoryCost); // Sets the inventory cost.
			product.setSellingCost(sellingCost); // Sets the sellling cost.
		}
	}
	
	/**
	* The getProductCount method will return the amount of products within the store (which is the array length subtracting
	* one due to the last being a empty position).
	* @return int - The amount of products in the store.
	*/
	public int getProductCount() {
		return this.products.length - 1;
	}
	
	/**
	* The replacementStrategy method accepts a value indicating the weeks the strategy will
	* be projected for. The output will solely depend on the Product. It will construct a grid 
	* for the number of weeks, with each week consisting of the order quantity, demand rate, and the inventory level.
	* The total cost, and profit is also appended in the output; with the whole thing being returned as a String.
	* @param index int - The index position of the product to be displayed.
	* @param weeks int - The weeks to be projected for.
	* @return String - The replacement strategy for the particular product & duration.
	*/
	public String replacementStrategy(int index, int weeks) {
		StringBuilder output = new StringBuilder(); // The StringBuilder used to append the output.
		Product product = this.products[index]; // The Product based on the index position.
		if (product != null) { // If the product exists.
			try {
				// The strategy calculated from the product for the projected weeks.
				int[][] strategy = product.calcStrategy(weeks);
				// A message to show what product the strategy is for.
				output.append(NEW_LINE + String.format(STRATEGY_MESSAGE, this.getName(), product.getProductName()) + NEW_LINE);
				// Shows the columns.
				output.append(NEW_LINE + String.format("%10s %20s %10s %10s", COLUMN_WEEK, COLUMN_QUANTITY_ORDERED, COLUMN_DEMAND, COLUMN_INVENTORY) + NEW_LINE);
				// Iterates through the strategy two dim array.
				for (int week = 0; week < strategy.length; week++) {
					// Outputting the data from each column into a formatted String.
					output.append(String.format("%10d %20d %10d %10d", strategy[week][0], strategy[week][1], strategy[week][2], strategy[week][3]) + NEW_LINE);
				}
				// Fetches the calculated total cost and profit for that strategy.
				double[] result = product.calcCostAndProfit(strategy);
				// Outputs the total cost.
				output.append(NEW_LINE + String.format(DISPLAY_TOTAL_COST, result[0]) + NEW_LINE);
				// Outputs the profit.
				output.append(String.format(DISPLAY_TOTAL_PROFIT, result[1]) + NEW_LINE);
			// Catches any Exceptions (Particularly if the strategy cannot be projected).
			} catch (IllegalArgumentException ex) {
				output.append(ex.getMessage());
			}
		}
		return output.toString(); // Returns everything as a String.
	}
	
	/**
	* The toString() method is used to display the details of what is in the store. It will display the 
	* store name, and the number of products; with also a list of the product names in the store.
	* @return String - The store contents.
	*/
	public String toString() {
		StringBuilder builder = new StringBuilder();
		// Displays the store name.
		builder.append(String.format(DISPLAY_STORE, this.getName()) + NEW_LINE);
		// Displays the number of products.
		builder.append(String.format(DISPLAY_NUMBER_OF_PRODUCTS, this.getProductCount()));
		// If the store has products.
		if (this.getProductCount() > 0) {
			builder.append(NEW_LINE);
			// It will the iterate throughout the products.
			for (int i = 0; i < this.products.length; i++) {
				if (products[i] != null) { // If the product is not null.
					// It will then show the product's name.
					builder.append(TAB + String.format(DISPLAY_PRODUCT, (i + 1), this.products[i].getProductName()) + NEW_LINE);
				}	
			}
		}
		return builder.toString(); // Returns everything as a String.
	}
}