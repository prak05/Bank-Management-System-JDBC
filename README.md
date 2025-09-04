

# 🏦 KNB.inc Bank Management System

![Java](https://img.shields.io/badge/Java-JDK%208%2B-red.svg)
![Swing](https://img.shields.io/badge/Java%20Swing-GUI-brightgreen.svg)
![Oracle](https://img.shields.io/badge/Oracle-Database-blue.svg)
![JDBC](https://img.shields.io/badge/JDBC-ojdbc8-yellow.svg)
![Status](https://img.shields.io/badge/Status-Stable-success.svg)

A **feature-rich, role-based banking system** built using **Java Swing (GUI)**, **Oracle DB**, and **JDBC**.
The project simulates a real-world banking environment with **Admin, Manager, and Client dashboards**, handling everything from fund transfers and bill payments to user management and audit logging.

**Repository Name Suggestion:**
👉 `KNBinc-BankManagementSystem-Java-Swing`

---

## 🌟 Key Features

### 👤 Client Dashboard

* View account balances and summaries
* Mini & detailed transaction statements
* Fund transfer (intra-bank)
* Deposit & withdraw functionality
* Pay utility bills
* Update contact details & reset password
* Download account statements

### 🛡️ Admin Dashboard

* User management (create, edit, delete, reset passwords)
* Manage accounts (view all accounts)
* Change user roles (admin/manager/client)
* Approve/reject account & loan requests
* Monitor audit logs & system activities
* Generate system reports (users, transactions, accounts)
* Backup/restore (simulated)

### 📊 Manager Dashboard

* Branch-wide customer summaries
* Approve/decline large transactions & loan requests
* Review suspicious activities
* Manage branch-level limits & liquidity positions
* System monitoring & staff approvals
* KYC compliance handling (simulated)

---

## 🛠️ Tech Stack

* **Programming Language**: Java (JDK 8+)
* **GUI**: Java Swing
* **Database**: Oracle DB (XE)
* **Connectivity**: JDBC (ojdbc8.jar)
* **OS Tested On**: Linux (Kali), Windows
* **Build**: Manual compilation (`javac` / `java`)

---

## 📂 Project Structure

```
KNB.inc/
├── bin/                  # Compiled .class files
├── db/                   # Database scripts
│   └── schema.sql        # Oracle SQL schema & default inserts
├── lib/                  # External libraries
│   └── ojdbc8.jar        # Oracle JDBC Driver
└── src/                  # Java source code
    └── com/knb/
        ├── MainApp.java
        ├── model/        # POJOs (Account, User, Transaction, AuditEntry)
        ├── service/      # Business logic (Auth, DB, Transaction services)
        └── view/         # GUI (LoginUI, Dashboards, Components)
```

---

## 🗄️ Database Setup (Oracle)

1. Install & run **Oracle DB (XE)** on `localhost:1521/XE`.
2. Execute the provided **`schema.sql`**:

```sql
-- Creates users, accounts, transactions & audit_log tables
-- Adds default admin & manager
-- Run this in SQL Developer / SQL*Plus

@db/schema.sql
COMMIT;
```

Default logins:

* **Admin** → Username: `Prak` | Password: `prak05`
* **Manager** → Username: `Adithya Baiju` | Password: `adi05`

---

## 🚀 How to Run

### 1. Place JDBC Driver

Download `ojdbc8.jar` and place it in `KNB.inc/lib/`.

### 2. Update DB Credentials

Edit:

```
KNB.inc/src/com/knb/service/DatabaseManager.java
```

Replace:

```java
String user = "yourusername";
String pass = "yourpassword";
```

with your Oracle credentials.

### 3. Compile (Linux/macOS)

```bash
mkdir -p bin
javac -cp lib/ojdbc8.jar -d bin $(find src -name "*.java")
```

### 4. Run

```bash
java -cp bin:lib/ojdbc8.jar com.knb.MainApp
```

*(For Windows: replace `:` with `;` in classpath)*

---

## 🏗️ Development Model & Architecture

* **Model Used**: Spiral Model (iterative, risk-driven)
* **Architecture**: 3-tier (Presentation ↔ Business Logic ↔ Data Layer)
* **GUI Layer**: Java Swing role-based dashboards
* **Business Layer**: Authentication, Transaction, and Service Managers
* **Data Layer**: Oracle DB with JDBC for connectivity

---

## 🔮 Future Scope

* ✅ Add **interbank fund transfer** support
* ✅ Integrate **SMS/Email alerts** for transactions
* ✅ Expand **encryption & security features**
* ✅ Build an **admin web portal (Spring Boot + Angular)** version

---

This README provides a **professional, GitHub-ready kit** for your **KNB.inc Bank Management System** 🚀.

---

✨ Suggestion: Since this is a **banking simulation project with strong OOP & DB integration**, a cleaner repo name could also be:

* `KNBinc-BankingSuite`
* `KNBinc-BankApp-Java`
* `KNBinc-SmartBankingSystem`

---
