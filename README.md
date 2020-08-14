# Java-Family-Finance
<p>App to control family finances whit the focus on familiar habitat  The main objective of the Family Finance application is to provide control over expenses, mainly aiming at a shared environment among people who live together daily, such as a family. <p>To do so, the application focuses on sharing records, history, and categorizations of expenses incurred in a familiar, everyday living environment.

<p><b>to run this project with docker, in the root folder type it:</b>
<p><b> docker build -t  family-finance:0.0.1 . </b>
<p>then
<p><b> docker run -d --name=family-finance -p 8080:8080 --link mysql:mysql  family-finance:0.0.1 </b>
<b>at last run the database in a container
<p><b>docker run --name mysql -e MYSQL_USER=family_finance -e MYSQL_DATABASE=family_finance -e   MYSQL_ROOT_PASSWORD=family_finance -d -p 3306:3306 mysql:5.6</b>



<p><b>to run this project without docker, in the root folder type it:</b>
<p><b>  java -jar target/familyfinance-0.0.1-SNAPSHOT.jar</b>
<p> or 
<p><b> mvn spring-boot:run </b>


<p><b>theres a file with mysql create schema and tables:</b>
<b> scripts sql/script_create_database_and_tables.sql</b>
<p><b>Documentation Swagger:
<p>http://localhost:8080/swagger-ui.html</b>

<br><br>
<p>java --version:
<p>openjdk 11.0.7 2020-04-14
<p>OpenJDK Runtime Environment (build 11.0.7+10-post-Ubuntu-2ubuntu218.04)
<p>OpenJDK 64-Bit Server VM (build 11.0.7+10-post-Ubuntu-2ubuntu218.04, mixed mode)
<p>docker --version
<p>Docker version 19.03.11, build dd360c7
<p>linux verson:
<p>Linux maicon-X450LD 4.15.0-54-generic #58-Ubuntu SMP Mon Jun 24 10:55:24 UTC 2019 x86_64 x86_64 x86_64 GNU/Linux
