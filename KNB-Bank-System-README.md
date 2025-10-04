<div align="center">

# 🏦 KNB.inc Bank Management System

![Java](https://img.shields.io/badge/Java-JDK%208%2B-red?style=for-the-badge&logo=openjdk&logoColor=white)
![Java Swing](https://img.shields.io/badge/Java%20Swing-GUI-brightgreen?style=for-the-badge)
![Oracle DB](https://img.shields.io/badge/Oracle-Database-blue?style=for-the-badge&logo=oracle)
![JDBC](https://img.shields.io/badge/JDBC-ojdbc8-yellow?style=for-the-badge)
![Stable](https://img.shields.io/badge/Status-Stable-success?style=for-the-badge)

### 🚀 Enterprise-Grade Banking System with Role-Based Dashboards

A **comprehensive, multi-user banking application** built with **Java Swing** and **Oracle DB**. Features distinct, role-based dashboards for clients, managers, and admins, all within a secure **3-tier architecture**. This system expertly simulates real-world banking operations, making it a robust educational and demonstration project.

</div>

---

## 📖 **Project Overview**

KNB.inc Bank Management System is a **feature-rich, enterprise-grade banking simulation** that demonstrates advanced Java programming concepts, database design, and GUI development. The system implements real-world banking workflows with comprehensive user management, transaction processing, audit logging, and reporting capabilities.

### 🎯 **Core Objectives**
- **Simulate real banking operations** with multiple user roles
- **Demonstrate 3-tier architecture** (Presentation → Business → Data)
- **Implement secure authentication** and role-based access control  
- **Showcase advanced Java Swing** GUI development
- **Practice enterprise database design** with Oracle integration

---

## 🌟 **Key Features**

<table>
<tr>
<td width="33%">

### 👤 **Client Dashboard**
- 💰 View account balances & summaries
- 📄 Generate mini & detailed statements  
- 🔄 Fund transfer (intra-bank)
- 💵 Deposit & withdrawal operations
- 🧾 Pay utility bills
- 📱 Update contact details
- 🔐 Reset password functionality
- 📊 Download account statements

</td>
<td width="33%">

### 🛡️ **Admin Dashboard**
- 👥 Complete user management
- 🏦 Account creation & management
- 🔧 Role assignment (admin/manager/client)
- ✅ Approve/reject account requests
- 📈 Monitor audit logs & activities
- 📋 Generate system reports
- 🔄 Backup/restore operations
- 🚨 System monitoring & alerts

</td>
<td width="33%">

### 📊 **Manager Dashboard**
- 🏢 Branch-wide customer summaries
- 💸 Approve large transactions
- 🔍 Review suspicious activities
- 📉 Manage branch limits & liquidity
- 👨‍💼 Staff approval workflows
- 📝 KYC compliance handling
- 📊 Branch performance metrics
- 🎯 Risk management tools

</td>
</tr>
</table>

---

## 🏗️ **Project Structure**

```plaintext
KNB.inc/
├── bin/                          # Compiled .class output files
├── db/                           # Database setup scripts and documentation
│   └── schema.sql               # Oracle SQL script for table and sequence creation
├── lib/                         # External libraries
│   └── ojdbc8.jar              # Oracle JDBC Driver
└── src/                         # Java source code
    └── com/
        └── knb/
            ├── MainApp.java                    # Main entry point
            ├── model/                          # Data Model classes
            │   ├── Account.java               # Account entity
            │   ├── AuditEntry.java           # Audit log entity  
            │   ├── Transaction.java          # Transaction entity
            │   └── User.java                 # User entity
            ├── service/                        # Business Logic
            │   ├── AuthenticationService.java # Authentication & authorization
            │   ├── DatabaseManager.java       # Database connection & operations
            │   └── TransactionManager.java    # Transaction processing logic
            └── view/                           # GUI classes
                ├── LoginUI.java               # Login interface
                ├── WelcomeUI.java            # Welcome screen
                ├── Theme.java                # UI theme configuration
                ├── common/                    # Shared UI components
                │   └── LogoPanel.java        # Company logo panel
                ├── admin/                     # Admin interface
                │   └── AdminDashboard.java   # Admin dashboard
                ├── manager/                   # Manager interface
                │   └── ManagerDashboard.java # Manager dashboard
                └── client/                    # Client interface
                    └── ClientDashboard.java  # Client dashboard
```

---

## 🛠️ **Technology Stack**

<div align="center">

### **Core Technologies**
![Java](https://img.shields.io/badge/Java-JDK%208%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![Oracle](https://img.shields.io/badge/Oracle-Database%20XE-F80000?style=for-the-badge&logo=oracle&logoColor=white)
![JDBC](https://img.shields.io/badge/JDBC-ojdbc8-blue?style=for-the-badge)

### **Architecture & Design**
![Swing](https://img.shields.io/badge/Java%20Swing-GUI%20Framework-green?style=for-the-badge)
![3-Tier](https://img.shields.io/badge/Architecture-3--Tier-purple?style=for-the-badge)
![Spiral](https://img.shields.io/badge/SDLC-Spiral%20Model-orange?style=for-the-badge)

</div>

| **Component**       | **Technology**           | **Purpose**                    |
|:------------------|:------------------------|:------------------------------|
| **Language**      | Java (JDK 8+)           | Core application development   |
| **GUI Framework** | Java Swing              | Desktop user interface         |
| **Database**      | Oracle Database XE      | Data persistence & management  |
| **Connectivity** | JDBC (ojdbc8.jar)       | Database connection layer      |
| **Architecture** | 3-Tier MVC              | Separation of concerns         |
| **Build System** | Manual compilation      | Simple build process           |
| **OS Support**   | Linux, Windows          | Cross-platform compatibility   |

---

## 🚀 **Installation & Setup**

### **Prerequisites**
- ☕ Java JDK 8 or higher
- 🗄️ Oracle Database XE 
- 💻 Windows or Linux operating system

### **Step-by-Step Installation**

#### **1. Database Setup**
```sql
-- Install Oracle Database XE on localhost:1521/XE
-- Execute the schema creation script
@db/schema.sql
COMMIT;
```

#### **2. JDBC Driver Configuration**
```bash
# Download ojdbc8.jar and place in lib/ directory
wget https://download.oracle.com/otn-pub/otn_software/jdbc/ojdbc8.jar
mv ojdbc8.jar lib/
```

#### **3. Database Connection Configuration**
Edit `src/com/knb/service/DatabaseManager.java`:
```java
String user = "your_oracle_username";
String pass = "your_oracle_password";
```

#### **4. Compilation (Linux/macOS)**
```bash
mkdir -p bin
javac -cp lib/ojdbc8.jar -d bin $(find src -name "*.java")
```

#### **5. Execution**
```bash
java -cp bin:lib/ojdbc8.jar com.knb.MainApp
```
*Note: On Windows, replace `:` with `;` in the classpath*

---

## 🔐 **Default Login Credentials**

| **Role**    | **Username**     | **Password** | **Access Level**        |
|:-----------|:-----------------|:-------------|:----------------------|
| **Admin**  | `Prak`           | `prak05`     | Full system access     |
| **Manager**| `Adithya Baiju`  | `adi05`      | Branch management      |

*Note: These are demonstration credentials for testing purposes*

---

## 🏗️ **Architecture & Design**

### **Development Model**
- **Spiral Model**: Iterative, risk-driven development approach
- **3-Tier Architecture**: Clear separation of presentation, business logic, and data layers

### **Layer Structure**
```mermaid
graph TD
    A[Presentation Layer] --> B[Business Logic Layer]
    B --> C[Data Access Layer]
    
    A1[Java Swing UI] --> A
    A2[Role-based Dashboards] --> A
    
    B1[Authentication Service] --> B
    B2[Transaction Manager] --> B
    B3[Business Rules] --> B
    
    C1[Database Manager] --> C
    C2[JDBC Connection] --> C
    C3[Oracle Database] --> C
```

### **Design Patterns Used**
- **MVC (Model-View-Controller)**: Separation of data, presentation, and control logic
- **DAO (Data Access Object)**: Abstract data access operations
- **Singleton**: Database connection management
- **Factory**: UI component creation

---

## 📊 **System Features**

### **Authentication & Security**
- 🔐 Secure login with role-based access control
- 🛡️ Password encryption and validation
- 📝 Comprehensive audit logging
- 🚫 Session management and timeout handling

### **Transaction Processing**
- 💰 Real-time fund transfers
- 📊 Transaction history and reporting  
- ✅ Multi-level approval workflows
- 🔄 Automatic balance updates

### **Administrative Functions**
- 👥 User lifecycle management
- 🏦 Account creation and maintenance
- 📈 System monitoring and reporting
- 🔧 Configuration management

---

## 🔮 **Future Enhancements**

### **Planned Features**
- [ ] **Interbank Fund Transfer**: External bank integration
- [ ] **SMS/Email Notifications**: Real-time transaction alerts  
- [ ] **Advanced Encryption**: Enhanced security protocols
- [ ] **Web Interface**: Spring Boot + Angular implementation
- [ ] **Mobile App**: React Native mobile application
- [ ] **API Integration**: RESTful web services
- [ ] **Advanced Analytics**: Business intelligence dashboards
- [ ] **Multi-language Support**: Internationalization

### **Technical Improvements**
- [ ] **Microservices Architecture**: Service decomposition
- [ ] **Cloud Deployment**: AWS/Azure integration
- [ ] **Automated Testing**: Unit and integration tests
- [ ] **CI/CD Pipeline**: Automated build and deployment

---

## 🧪 **Testing**

### **Test Scenarios**
- ✅ User authentication and authorization
- ✅ Fund transfer operations
- ✅ Account management workflows
- ✅ Audit logging functionality
- ✅ Error handling and validation

### **Testing Environment**
```bash
# Run test scenarios
java -cp bin:lib/ojdbc8.jar com.knb.MainApp
# Test with different user roles
# Verify transaction processing
# Check audit trail generation
```

---

## 📝 **Documentation**

### **Available Resources**
- 📚 **Technical Specification**: Complete system documentation
- 🎯 **User Manual**: End-user operation guide
- 🔧 **Installation Guide**: Setup and configuration instructions
- 📊 **Database Schema**: Entity relationship diagrams
- 🎨 **UI/UX Design**: Interface design specifications

---

## 🤝 **Contributing**

We welcome contributions to improve the KNB.inc Bank Management System!

### **How to Contribute**
1. 🍴 Fork the repository
2. 🌿 Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. 💻 Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. 📤 Push to the branch (`git push origin feature/AmazingFeature`)
5. 🔄 Open a Pull Request

### **Contribution Guidelines**
- Follow Java coding conventions
- Add comprehensive comments
- Include unit tests for new features
- Update documentation as needed

---

## 📄 **License**

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

---

## 👤 **Author**

<div align="center">

**Prakhar Sharma**  
*AI Architect & System Programmer*

[![GitHub](https://img.shields.io/badge/GitHub-prak05-black?style=for-the-badge&logo=github)](https://github.com/prak05)
[![LinkedIn](https://img.shields.io/badge/LinkedIn-prak05--btech-blue?style=for-the-badge&logo=linkedin)](https://linkedin.com/in/prak05-btech)
[![Email](https://img.shields.io/badge/Email-praksediting5%40gmail.com-red?style=for-the-badge&logo=gmail)](mailto:praksediting5@gmail.com)

</div>

---

## 🙏 **Acknowledgments**

- **Oracle Corporation** for the robust database system
- **Java Community** for comprehensive documentation
- **RIET Faculty** for technical guidance and support
- **Open Source Community** for inspiration and best practices

---

<div align="center">

### 🌟 **If you found this project helpful, please consider giving it a star!** ⭐

*Built with ❤️ for the developer community*

</div>