## Food Ordering System (Java)

This is a simple in-memory food ordering system for one or more restaurants.
It is designed to be easy to read, while covering all required scenarios.

### 1. Project structure

- `src/`
  - `Main.java` - small wrapper entry point
  - `FoodOrderingApplication.java` - main application program
  - `model/`
    - `User.java`
    - `Restaurant.java`
    - `MenuItem.java`
    - `Order.java`
    - `OrderStatus.java`
  - `service/`
    - `UserService.java` (interface)
    - `RestaurantService.java` (interface)
    - `OrderService.java` (interface)
  - `service/impl/`
    - `InMemoryUserService.java`
    - `InMemoryRestaurantService.java`
    - `InMemoryOrderService.java`
- `test/`
  - `FoodOrderingSystemTest.java` – manual test driver for the 7 required scenarios

All data is stored in memory using Java collections (`Map`, `List`). No database is used.

---

### 2. Entity classes (`model`)

#### `User`
- **Fields**: `id`, `name`, `email`, `phone`.
- Created when a new customer registers.
- Used by `UserService` and `Order` to link orders to a user.

#### `Restaurant`
- **Fields**: `id`, `name`, `gstNumber`, `email`, `phone`.
- Holds a catalog of menu items internally: `Map<String, MenuItem> catalog`.
- **Key method**: `addOrUpdateItem(String itemName, BigDecimal price, int quantity)`
  - Adds a new item if it does not exist.
  - Increases quantity if it already exists.

#### `MenuItem`
- **Fields**: `name`, `price`, `availableQuantity`.
- **Methods**:
  - `reduceQuantity(int quantity)` – throws if not enough stock.
  - `increaseQuantity(int quantity)` – used when cancelling orders.

#### `OrderStatus`
- Enum with two values: `CONFIRMED`, `CANCELLED`.

#### `Order`
- **Fields**: `id`, `userId`, `restaurantName`, `itemName`, `quantity`, `status`.
- Starts with `status = CONFIRMED`.
- **Method**: `cancel()` – sets status to `CANCELLED`.

---

### 3. Service interfaces (`service`)

These define what the system can do, without tying to a particular implementation.

#### `UserService`
- `long userRegistration(String userName, String email, String phoneNumber)`
  - Creates a new `User` and returns its generated id.
- `User getUser(long userId)`
  - Looks up a user; throws `IllegalArgumentException` if not found.

#### `RestaurantService`
- `long restaurantRegistration(String restaurantName, String gstNumber, String emailId, String phoneNumber)`
  - Registers a new restaurant; fails if the name already exists.
- `void addItemsInCatalog(String restaurantName, String itemName, BigDecimal price, int quantity)`
  - Adds an item with price and quantity for a given restaurant.
- `MenuItem searchItem(String restaurantName, String itemName)`
  - Returns a single item, or `null` if not found.
- `Restaurant getByName(String restaurantName)`
  - Finds the `Restaurant` object by its name.
- `List<String> getItemNames(String restaurantName)`
  - Returns all item names for a restaurant, **sorted by price** (implemented in `InMemoryRestaurantService`).

#### `OrderService`
- `long placeOrder(long userId, String restaurantName, String itemName, int quantity)`
  - Validates user and item, reduces stock, creates an `Order` and returns its id.
- `List<Order> getOrders(long userId)`
  - Returns all orders for a given user.
- `void cancelOrder(long orderId)`
  - Cancels an order, restores stock, and keeps status consistent.

---

### 4. In-memory implementations (`service.impl`)

These classes actually store data using maps and lists.

#### `InMemoryUserService`
- Fields: `Map<Long, User> users`, `long nextUserId`.
- `userRegistration(...)`:
  - Creates a new `User` with an incremented id and stores it in `users`.
- `getUser(...)`:
  - Returns an existing `User` or throws `IllegalArgumentException` if not found.

#### `InMemoryRestaurantService`
- Fields: `Map<Long, Restaurant> restaurantsById`, `Map<String, Restaurant> restaurantsByName`, `long nextRestaurantId`.
- `restaurantRegistration(...)`:
  - Rejects duplicate restaurant names.
  - Creates and stores a `Restaurant`.
- `addItemsInCatalog(...)`:
  - Delegates to `Restaurant.addOrUpdateItem(...)`.
- `searchItem(...)`:
  - Looks up the restaurant and returns the matching `MenuItem`.
- `getItemNames(...)`:
  - Pulls all `MenuItem` objects, sorts them by price ascending, then returns just the names.
  - This is how we satisfy “search responses should be sorted on price”.

#### `InMemoryOrderService`
- Fields: `Map<Long, Order> ordersById`, `Map<Long, List<Order>> ordersByUserId`, `long nextOrderId`.
- Constructor: takes `UserService` and `RestaurantService` to validate user and items.
- `placeOrder(...)`:
  - Uses `userService.getUser(userId)` to ensure the user exists.
  - Uses `restaurantService.searchItem(...)` to find the item.
  - Calls `MenuItem.reduceQuantity(...)` to handle inventory.
  - Creates an `Order` with status `CONFIRMED` and stores it.
- `getOrders(...)`:
  - Returns a copy of that user’s order list to avoid external modification.
- `cancelOrder(...)`:
  - Looks up the order, throws if not found.
  - If already cancelled, returns without doing anything.
  - Otherwise, finds the item again and calls `increaseQuantity(...)`, then sets order status to `CANCELLED`.

---

### 5. Main demo program (`FoodOrderingApplication`)

This is a simple script-like main class that demonstrates the flow from the problem statement:

1. Creates implementations:
   - `UserService userService = new InMemoryUserService();`
   - `RestaurantService restaurantService = new InMemoryRestaurantService();`
   - `OrderService orderService = new InMemoryOrderService(userService, restaurantService);`
2. Registers a restaurant `"Donald"` and prints the restaurant id.
3. Adds three items (`Sandwich`, `Burger`, `Pizza`) with different prices and quantities.
4. Prints the catalog names.
5. Registers a user `"User1"` and prints the user id.
6. Searches for `"Sandwich"` and prints its details (name, price, quantity).
7. Places two orders (one confirmed, one later cancelled) and prints the order ids.
8. Lists all orders for the user with status.
9. Cancels one order and prints a confirmation message.

Now go through this file line by line to show how the services are used end-to-end.

---

### 6. Test driver (`test/FoodOrderingSystemTest.java`)

This class is not a JUnit test; it is a simple `main` program that prints `PASS/FAIL`
for each of the 7 required scenarios:

1. **User registration** – `testUserRegistration`
2. **Restaurant registration** – `testRestaurantRegistration`
3. **Catalog addition** – `testCatalogAddition`
4. **Search items sorted by price** – `testSearchItemsSortedByPrice`
5. **Place order** – `testPlaceOrder`
6. **Get orders for user** – `testGetOrders`
7. **Cancel order** – `testCancelOrder`

Each helper method sets up only what it needs, calls the relevant service method,
checks the result, and prints whether that scenario passed.

---

### 7. How to compile and run (Windows / PowerShell)

From inside the `FoodOrderingSystem` folder:

#### Compile everything (main + tests)

```powershell
cd "C:\Users\dream\OneDrive\Desktop\FoodOrderingSystem"

javac -d out src\model\*.java src\service\*.java src\service\impl\*.java src\FoodOrderingApplication.java test\FoodOrderingSystemTest.java
```
#### Run the main demo

```powershell
java -cp out FoodOrderingApplication
```

This shows the full flow with logs (register, add catalog, search, place orders, cancel).

#### Run the 7 scenario tests

```powershell
java -cp out FoodOrderingSystemTest
```

You should see output like:

```text
Running Food Ordering System tests...
1. User registration: PASS
2. Restaurant registration: PASS
3. Catalog addition: PASS
4. Search items sorted by price: PASS
5. Place order: PASS
6. Get orders for user: PASS
7. Cancel order: PASS
```

All ready:
- Entities and relationships
- How services encapsulate business logic
- How the in-memory maps work
- How each requirement is covered by both the main demo and the test driver.