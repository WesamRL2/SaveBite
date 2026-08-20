# SaveBite

SaveBite is a Java desktop application designed to reduce food waste by allowing restaurants and food businesses to list surplus products at discounted prices before the end of the day.

Customers can browse available deals, reserve discounted food, manage their orders, and view the environmental and financial impact generated through the platform.

## Project Objective

Food businesses often have unsold products remaining at the end of the day. These products may still be safe and suitable for consumption but can become waste if they are not sold.

SaveBite provides a simple marketplace where businesses can:

- List surplus food products.
- Apply discounted prices.
- Define available quantities.
- Set pickup deadlines.

Customers can:

- Browse available surplus food deals.
- Reserve products.
- View their orders.
- Cancel reservations.
- Mark orders as collected.

The system also calculates sustainability statistics such as food items saved from waste, customer savings, recovered revenue, and surplus rescue rate.

## Technologies Used

- Java
- Java Swing
- Object-Oriented Programming
- Java Collections
- File I/O
- CSV data storage
- Git
- GitHub
- Mermaid UML

## Main Features

### Available Deals

Customers can view currently available surplus food products.

Each deal displays:

- Product ID
- Product name
- Original price
- Discounted SaveBite price
- Available quantity
- Category
- Pickup deadline

Expired or unavailable products are automatically excluded from the active deals list.

### Add Surplus Product

Businesses can add surplus products through the graphical interface.

The system validates:

- Product name
- Original price
- Quantity
- Category
- Discount percentage
- Pickup period

The discounted price is automatically calculated.

### Reservations

Customers can select an available product and reserve a quantity.

The system:

- Checks product availability.
- Prevents reservations greater than available stock.
- Reduces stock automatically.
- Generates an order ID.
- Calculates the total reservation price.
- Saves the updated information.

### My Orders

Customers can view their reservation history.

Order information includes:

- Order ID
- Product
- Quantity
- Unit price
- Total price
- Order time
- Order status

Order statuses include:

- Reserved
- Collected
- Cancelled

### Cancellation and Stock Restoration

When a reserved order is cancelled:

1. The order status changes to `Cancelled`.
2. The reserved quantity is returned to product stock.
3. Updated product and order data are saved.

Collected orders cannot be cancelled.

### Sustainability Statistics

SaveBite provides a statistics dashboard containing:

- Product listings
- Available listings
- Available items
- Total orders
- Items saved from waste
- Revenue recovered
- Customer savings
- Surplus rescue rate

The project supports the concept of:

**SDG 12 — Responsible Consumption and Production**

## Object-Oriented Programming

SaveBite was designed using Object-Oriented Programming principles.

### Encapsulation

Class attributes are private and accessed using methods.

Examples:

```java
private String id;
private String name;
private double originalPrice;
private int quantity;
```

### Abstraction

The project contains abstract classes:

```text
User
Product
```

These define common structures and behaviours for their child classes.

### Inheritance

The project uses inheritance relationships:

```text
User
├── Customer
└── Seller
```

and:

```text
Product
└── FoodProduct
```

### Polymorphism

Child classes override methods defined by their parent classes.

Examples include:

```java
getRole()
calculateFinalPrice()
getProductType()
isAvailable()
```

The application can work with parent references while executing behaviour from the actual child object at runtime.

## DRY Principle

The project follows the **Don't Repeat Yourself (DRY)** principle.

Examples include:

- Shared user properties are stored in `User`.
- Shared product properties are stored in `Product`.
- Business logic is centralized in `MarketplaceService`.
- File operations are centralized in `FileManager`.
- Input validation is centralized in `ValidationUtil`.
- Swing styling is centralized in `UITheme`.

This reduces duplicated code and makes the system easier to maintain.

## Project Structure

```text
SaveBite/
│
├── data/
│   ├── products.csv
│   └── orders.csv
│
├── docs/
│   ├── UML.md
│   └── TESTING.md
│
├── src/
│   └── com/
│       └── savebite/
│           │
│           ├── app/
│           │   └── Main.java
│           │
│           ├── model/
│           │   ├── User.java
│           │   ├── Customer.java
│           │   ├── Seller.java
│           │   ├── Product.java
│           │   ├── FoodProduct.java
│           │   └── Order.java
│           │
│           ├── service/
│           │   └── MarketplaceService.java
│           │
│           ├── storage/
│           │   └── FileManager.java
│           │
│           ├── ui/
│           │   ├── MainFrame.java
│           │   ├── AvailableDealsPanel.java
│           │   ├── AddSurplusProductPanel.java
│           │   ├── MyOrdersPanel.java
│           │   ├── StatisticsPanel.java
│           │   └── UITheme.java
│           │
│           └── util/
│               └── ValidationUtil.java
│
├── .gitignore
└── README.md
```

## Data Persistence

SaveBite uses CSV files for persistent storage.

### Product Data

Stored in:

```text
data/products.csv
```

### Order Data

Stored in:

```text
data/orders.csv
```

When SaveBite starts, saved products and orders are loaded from these files.

When products or orders are updated, the information is written back to the CSV files.

This allows data to remain available after the application is closed and reopened.

## Collections

The system uses Java `ArrayList` collections to manage application data during runtime.

```java
ArrayList<Product>
ArrayList<Order>
```

These collections are managed by:

```text
MarketplaceService
```

## UML Diagram

The complete UML class diagram is available at:

```text
docs/UML.md
```

It shows:

- Classes
- Attributes
- Methods
- Inheritance
- Associations
- Dependencies
- Abstract classes

## Testing

Testing documentation is available at:

```text
docs/TESTING.md
```

The system was tested for scenarios including:

- Valid product creation
- Invalid discount
- Invalid quantity
- Blank product name
- Successful reservation
- Insufficient stock
- Order cancellation
- Stock restoration
- Collected orders
- Product persistence
- Order persistence
- Expired products
- Statistics calculations

## How to Run

### Requirements

Install:

- Java JDK
- Visual Studio Code
- Extension Pack for Java

### Run from Visual Studio Code

Open the `SaveBite` folder in VS Code.

Then open:

```text
src/com/savebite/app/Main.java
```

Click:

```text
Run
```

The SaveBite Swing interface will open.

## Git Workflow

Development changes are tracked using Git and GitHub.

Typical workflow:

```bash
git add .
git commit -m "Describe the completed feature"
git push
```

This provides version history and allows the project development process to be reviewed.

## GitHub Repository

Repository:

```text
https://github.com/WesamRL2/SaveBite
```

The repository may remain private during development and can be made accessible to the lecturer when required for submission.

## Future Improvements

SaveBite could later be expanded into a real-world application with:

- User registration and authentication
- Separate seller and customer accounts
- Database integration
- Restaurant profiles
- Product images
- Online payment
- Maps and pickup locations
- Notifications
- Mobile application
- Web frontend
- Cloud backend
- Advanced sustainability analytics

## Conclusion

SaveBite demonstrates how Java, Object-Oriented Programming, graphical user interfaces, collections, file handling, validation, and business logic can be combined to solve a real-world food waste problem.

The project allows surplus food to be sold at discounted prices instead of being wasted while also providing measurable financial and sustainability benefits.