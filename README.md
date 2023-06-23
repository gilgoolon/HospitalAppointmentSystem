# Hospital Appointment System - Gil Alpert & Maor Shweky
## Setup / Installation
In order to run the project one must install the technologies used in the project. We have used maven as the build system of the project, since it is easy to use and answers our needs.

Follow these steps to install the project environment properly:

1. install the following technologies:

        Maven Build System (4.0.0)
        Java Development Kit (18.0)
        MySQL Server / Workbench (8.0)
        MySQL Connector J (JDBC Driver)
        Java EE (8.0)
        GlassFish (5.2022.5 Payara)

    It is important to install exactly the versions listed above, since compatibility is a big issue when dealling with many modules.

2. Create a Java Maven project using the 'Web Application' template, using the Java EE 8.0 technology(and not 9.0 or 10.0). Select EclipseLink as an implementation library for Java EE.

3. Include the following dependencies in your pom.xml file:

        javax.faces / javax.faces-api v2.3
        javax.servlet / javax.servlet-api v4.0.1
        org.eclipselink.persistence / eclipselink v2.7.9
        org.mysql / mysql-connector-j v8.0.32
        org.primefaces / primefaces 12.0.0
        javax.transaction / javax.transaction-api v1.3
        com.itextpdf / kernel, io, layout, forms, pdfa, sign v7.2.3

4. Include the following plugin in your pom.xml file:
    
        org.apache.maven.plugins / maven-war-plugin v3.3.2

5. Launch the GlassFish web server and navigate your browser to the url: 'localhost:4848'.

6. In the admin console which launched in the previous step, set up the JDBC connection pool according to the persistence.xml file.

7. Replace the src folder with our given src folder.

8. (optional) Enable JPA logging (We disabled on default): Delete line 18 in persistence.xml file:

        <property name="eclipselink.logging.level" value="OFF"/>

9. Use our given MySQL database schema folder to create a schema on the MySQL platform.

10. Change the 'user' and 'password' properties in the persistence.xml file the you username and password set on MySQL platform.

11. Build & Run the project using maven built-in web application procedure.

12. Navigate to URL: 'localhost:8080/Hospital-Appointment-System-1.0-SNAPSHOT'.