Bank-Management-System-JDBCA comprehensive, multi-user banking application built with Java Swing and Oracle DB. It features distinct, role-based dashboards for clients, managers, and admins, all within a secure 3-tier architecture. This system expertly simulates real-world banking operations, making it a robust educational project.🌟 Key FeaturesThe system offers a wide range of functionalities categorized by three distinct user roles, each with its own dedicated dashboard.👤 Client DashboardAccount Management: View account summary, balance, and personal details.Transaction History: Access mini and detailed transaction statements.Financial Transactions: Securely transfer funds, deposit, and withdraw cash.Bill Payments: Pay utility bills directly from your account.Self-Service: Update contact information, change passwords, and download statements.💼 Bank Manager DashboardBranch Oversight: View branch-wide customer details and transaction summaries.Transaction Monitoring: Oversee, approve, or decline large and suspicious transactions.Risk & Compliance: Manage customer KYC compliance and resolve escalated complaints.Reporting: Generate comprehensive reports on branch performance and key metrics.System Health: Monitor staff activities and view system alerts and exceptions.⚙️ Admin DashboardFull User Control: Create, view, edit, and delete any user in the system.System Administration: Manage user roles, reset passwords, and link accounts to users.Account Oversight: Access and review all bank accounts in the system.Auditing: View detailed audit logs of all critical administrative actions.Request Management: Approve or reject new account applications and loan requests.🛠️ Tech StackCore Language: Java (JDK 8+)Graphical User Interface (GUI): Java SwingDatabase: Oracle Database (XE)Connectivity: Java Database Connectivity (JDBC) using the ojdbc8.jar driverDevelopment Model: Spiral ModelOS: Developed on Kali Linux (compatible with Windows/macOS)🏗️ Project Architecture & StructureThe application follows a classic Three-Tier Architecture to ensure separation of concerns, maintainability, and scalability.Presentation Layer (GUI): Built with Java Swing, this tier handles all user interaction and renders the role-specific dashboards.Business Logic Layer (Application): The core of the application, written in Java. It enforces business rules, processes data, and handles application logic.Data Layer (Database): An Oracle Database instance is responsible for the persistent storage and retrieval of all user, account, and transaction data.Project Directory StructureKNB.inc-BankManagementSystem/
├── lib/
│   └── ojdbc8.jar              # Oracle JDBC Driver
├── db/
│   └── schema.sql              # Oracle SQL script for DB setup
└── src/
    └── com/
        └── knb/
            ├── MainApp.java          # Main application entry point
            ├── model/                # Data Model classes (POJOs)
            │   ├── User.java
            │   ├── Account.java
            │   └── Transaction.java
            ├── service/              # Business Logic & DB Services
            │   ├── DatabaseManager.java
            │   └── AuthenticationService.java
            └── view/                 # GUI/Swing classes
                ├── WelcomeUI.java
                ├── LoginUI.java
                ├── admin/
                │   └── AdminDashboard.java
                ├── manager/
                │   └── ManagerDashboard.java
                └── client/
                    └── ClientDashboard.java
🚀 How to Run LocallyPrerequisitesJava Development Kit (JDK 8 or higher) installed.Oracle Database (e.g., Oracle XE) installed and running.Your Oracle DB username and password.1. Clone the Repositorygit clone [https://github.com/your-username/KNB.inc-BankManagementSystem.git](https://github.com/your-username/KNB.inc-BankManagementSystem.git)
cd KNB.inc-BankManagementSystem
2. Set Up the Oracle DatabaseOpen your Oracle SQL client (e.g., SQL Developer, SQL*Plus).Execute the entire script located in db/schema.sql. This will create all necessary tables, sequences, and default admin/manager users.Important: Run COMMIT; in your SQL client after the script finishes to save the changes.3. Configure Database CredentialsNavigate to and open the file: src/com/knb/service/DatabaseManager.java.Locate the following lines and replace the placeholders with your actual Oracle database credentials:// Inside DatabaseManager.java
private static final String USER = "yourusername";
private static final String PASS = "yourpassword";
4. Place the JDBC DriverDownload the Oracle JDBC Driver (ojdbc8.jar).Place the downloaded ojdbc8.jar file inside the lib/ directory.5. Compile and Run the ApplicationOpen a terminal in the root directory of the project (KNB.inc-BankManagementSystem/).Compile (Linux/macOS)# This command finds all .java files and compiles them into a new 'bin' directory
mkdir -p bin
javac -cp "lib/ojdbc8.jar:." -d bin $(find src -name "*.java")
Run (Linux/macOS)# This command runs the main application from the compiled classes
java -cp "bin:lib/ojdbc8.jar" com.knb.MainApp
(For Windows Users: Replace the colon : with a semicolon ; in the classpath arguments.)🔮 Future ScopeAPI Integration: Develop a REST API to allow mobile or web clients to connect to the banking system.Advanced Security: Implement password hashing (e.g., bcrypt) and two-factor authentication (2FA).Automated Notifications: Integrate email or SMS alerts for large transactions, low balances, or security events.Loan Processing Module: Build out the simulated loan application and approval workflow into a fully functional feature.UI/UX Enhancements: Modernize the UI using a more advanced framework like JavaFX or migrate to a web-based frontend.
