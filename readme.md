# Computer-shop

Java Web Project for EPAM Systems Java Web Training as a final examination.

## Install and run the project

A step by step instructions to get a project running:

1. Open IntelliJ IDEA and press `File -> New -> Project from version control -> Git`.
2. Pull a project to IntelliJ IDEA from `https://github.com/psoap/Computer-shop.git`.
3. Customize the following property file `resources/database.properties` to access your data source.
4. Execute `db_dump.sql` in data source console.
5. Go to `File -> Project Structure -> Project` and set up the Project SDK to `1.8`.
6. Go to `View -> Tool windows -> Maven Projects` and create a Maven Goal `Clean`.
7. Go to `Run/Debug Configurations` and configure your web server, on tab `Deployment` add artifact of the project.
8. Run the project in IntelliJ IDEA.
9. Go to a browser and input `localhost:8080` in the address line.

Root user login `admin` and password `admin123`.