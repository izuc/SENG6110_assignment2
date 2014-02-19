/**
* The Product class holds all the required instance data that is applicable for each product. It encapsulates
* the attributes by providing getters & setters. It also contains methods to calculate the Economic Order Quantity, and the 
* Replacement Strategy.
* @author Lance Baker (c3128034).
*/
public class Product {
	private static final String NEW_LINE = System.getProperty("line.separator");
	private static final String STRATEGY_ERROR = "Error: not possible to have a replacement strategy with the inputs given.";
	public static final String FIELD_PRODUCT_NAME = "Name: ";
	public static final String FIELD_DEMAND_RATE = "demand rate: ";
	public static final String FIELD_SETUP_COST = "setup cost: ";
	public static final String FIELD_UNIT_COST = "unit cost: ";
	public static final String FIELD_INVENTORY_COST = "inventory cost: ";
	public static final String FIELD_SELLING_PRICE = "selling price: ";
	
	private String productName; // The product name.
	private int demandRate; // The product demand rate.
	private double setupCost; // The setup cost for each order.
	private double unitCost; // The individual cost of each unit.
	private double inventoryCost; // The cost to keep it in the inventory.
	private double sellingCost; // The selling cost.
	
	/**
	* The default empty constructor.
	*/
	public Product() {
	
	}
	
	/**
	* The Product constructor accepts the product name, the demand rate, the setup cost, the unit cost,
	* the inventory cost, and the selling cost.
	* @param productName String - The product name.
	* @param demandRate int - The demand rate.
	* @param setupCost double - The setup cost.
	* @param unitCost double - The unit cost.
	* @param inventoryCost double - The inventory cost.
	* @param sellingCost double - The selling cost.
	*/
	public Product(String productName, int demandRate, double setupCost, 
										double unitCost, double inventoryCost, double sellingCost) {
		this.setProductName(productName);
		this.setDemandRate(demandRate);
		this.setSetupCost(setupCost);
		this.setUnitCost(unitCost);
		this.setInventoryCost(inventoryCost);
		this.setSellingCost(sellingCost);
	}
	
	/**
	* The getter for the product name.
	* @return String - The product name.
	*/
	public String getProductName() {
		return this.productName;
	}
	
	/**
	* The setter for the product name.
	* @param String - The product name.
	*/
	public void setProductName(String productName) {
		this.productName = productName;
	}
	
	/**
	* The getter for the demand rate.
	* @return int - The demand rate.
	*/
	public int getDemandRate() {
		return this.demandRate;
	}

	/**
	* The setter for the demand rate.
	* @param int - The demand rate.
	*/
	public void setDemandRate(int demandRate) {
		this.demandRate = demandRate;
	}
	
	/**
	* The getter for the setup cost.
	* @return double - The setup cost.
	*/
	public double getSetupCost() {
		return this.setupCost;
	}
	
	/**
	* The setter for the setup cost.
	* @param double - The setup cost.
	*/
	public void setSetupCost(double setupCost) {
		this.setupCost = setupCost;
	}
	
	/**
	* The getter for the unit cost.
	* @return double - The unit cost.
	*/
	public double getUnitCost() {
		return this.unitCost;
	}
	
	/**
	* The setter for the unit cost.
	* @param double - The unit cost.
	*/
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}
	
	/**
	* The getter for the inventory cost.
	* @return double - The inventory cost.
	*/
	public double getInventoryCost() {
		return this.inventoryCost;
	}
	
	/**
	* The setter for the inventory cost.
	* @param double - The inventory cost.
	*/
	public void setInventoryCost(double inventoryCost) {
		this.inventoryCost = inventoryCost;
	}
	
	/**
	* The getter for the selling cost.
	* @return double - The selling cost.
	*/
	public double getSellingCost() {
		return this.sellingCost;
	}
	
	/**
	* The setter for the selling cost.
	* @param double - The selling cost.
	*/
	public void setSellingCost(double sellingCost) {
		this.sellingCost = sellingCost;
	}
	
	/**
	* The calcOrderQuantity method is used to calculate the Economic Order Quantity (which is the optimal order quantity).
	* It will round the calculated double and return the value as an int.
	* @param int - The calculated economic order quantity.
	*/
	public int calcOrderQuantity() {
		return (int)Math.round(Math.sqrt((2 * (this.getSetupCost() * this.getDemandRate())) / this.getInventoryCost()));
	}
	
	/**
	* The calcStrategy method is used to calculate the stratgey on the product for the projected weeks received.
	* It will store the data collected from the calculation into a two dim array, and returns the end result.
	* @param week int - The weeks for the strategy to be projected for.
	* @return int[][] - The resulting grid of numbers from the calculation.
	*/
	public int[][] calcStrategy(int weeks) {
		int[][] strategy = new int[weeks][4]; // Creates a two dim array for the grid data.
		// Calculates the Economic Order Quantity, and sets it to the variable (to avoid recaculation).
		int eOrderQuantity = this.calcOrderQuantity();
		// If the EOQ is greater than the demand rate.
		if (eOrderQuantity > this.demandRate) {
			// Calculates the orders required.
			int ordersRequired = (int) Math.ceil((this.demandRate * weeks) / (double) eOrderQuantity);
			// Declares some vairables & Iterates throughout each week.
			for (int week = 0, orderQuantity = eOrderQuantity, 
								inventory = 0, orderCount = 0; week < weeks; week++) {
				// If the inventory is less than the demand rate.
				if (inventory < this.demandRate) {
					// Then the order quantity is set to the EOQ.
					orderQuantity = eOrderQuantity;
					// Increments the order count, and if it equals the require number of orders.
					if (++orderCount == ordersRequired) {
						// Then it will decrement the remainder.
						orderQuantity -= ((ordersRequired * eOrderQuantity) - (this.demandRate * weeks));
					}
					// Adds the ordered quantity to the inventory.
					inventory += orderQuantity;
				}
				inventory -= this.demandRate; // Decrements the demand rate from the inventory.
				// Sets the vairables values to the row.
				strategy[week][0] = (week + 1);
				strategy[week][1] = orderQuantity;
				strategy[week][2] = this.demandRate;
				strategy[week][3] = inventory;
				orderQuantity = 0; // Sets the order quantity to zero.
			}
		// Otherwise, the strategy is not feasible. So it will throw an Exception.
		} else {
			throw new IllegalArgumentException(STRATEGY_ERROR);
		}
		return strategy; // Returns the projected strategy.
	}
	
	/**
	* The calcCostAndProfit method accepts the strategy two dim array, and iterates throughout
	* the values adding up the totals & then calculating the total cost of the strategy with the 
	* profit projected.
	* @param int[][] - The strategy grid of numbers from the calcStrategy.
	* @return double[] - A array of two elements, with the first position being the total cost & the 
	* second being the profit.
	*/
	public double[] calcCostAndProfit(int[][] strategy) {
		double[] result = new double[2]; // Creates a two element array.
		// Declares some initial variables for the calculations.
		int orderCount = 0;
		int orderTotal = 0;
		int inventoryCount = 0;
		// Iterates throughout each week.
		for (int week = 0; week < strategy.length; week++) {
			// If the order quantity is greater than 0.
			if (strategy[week][1] > 0) {
				// Then a batch was ordered, so it increments the count.
				orderCount++;
				// Also adds it to the order total.
				orderTotal += strategy[week][1];
			}
			// The inventory count sums up the inventory for each week.
			inventoryCount += strategy[week][3];
		}
		// Calculates the total cost. Being the ((setup cost * order count) + (total ordered * unit cost)) + inventory count * inventory cost.
		double totalCost = (((this.getSetupCost() * orderCount) + (orderTotal * this.getUnitCost())) + (inventoryCount * this.getInventoryCost()));
		// Calculates the profit. Being the (demand rate * weeks * selling cost) - the total cost.
		double profit = (this.getDemandRate() * strategy.length * this.getSellingCost()) - totalCost;
		result[0] = totalCost; // Assigns the total cost to the first element.
		result[1] = profit; // Assigns the profit to the second element.
		return result; // Returns the array.
	}
	
	/**
	* The toString() method will output the product details with the field name (separated with a colon) and then the value.
	* Each property being on a new line.
	* @return String - The product details.
	*/
	public String toString() {
		StringBuilder builder = new StringBuilder(); // Creates a StringBuilder used for appending the output.
		// Appends the data of the product.
		builder.append(FIELD_PRODUCT_NAME + this.getProductName() + NEW_LINE);
		builder.append(FIELD_DEMAND_RATE + this.getDemandRate() + NEW_LINE);
		builder.append(FIELD_SETUP_COST + this.getSetupCost() + NEW_LINE);
		builder.append(FIELD_UNIT_COST + this.getUnitCost() + NEW_LINE);
		builder.append(FIELD_INVENTORY_COST + this.getInventoryCost() + NEW_LINE);
		builder.append(FIELD_SELLING_PRICE + this.getSellingCost());
		return builder.toString(); // Returns everything as a String.
	}
}