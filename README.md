<h1>Project PIVIC - Federal University of Paraíba.

Teacher: Ayla Rebouças - http://goo.gl/p2urH

Project Name: Evitando Falsos Positivos em Testes de Sistemas Multi-threaded com a abordagem 
“Thread Control for Tests”

More Information: http://goo.gl/8Ywy1


<h1>Requirements:

Web Server: Apache Tomcat 7+<br />
JDK: Version 6+<br />
IDE: Eclipse Java EE indigo+<br />
AJDT (AspectJ Development Tools)¹<br />
Maven: Version 2²<br />
Test: JUnit 4³<br />
Database: PostgreSQL Preferably, but it is possible to use other banks,<br />
since the framework is implemented JPA (Java Persistence API)<br />

<h1>Configuring the Database:

1 - <b>Install PostgreSQL<b> (http://www.postgresql.org/download/)

1.1 - If you think necessary, you can install the customer PostgreSQL (http://www.pgadmin.org/download/)

1.2 - Create a database with the name "messengerConcurrent" and set the "Login Role" default PostgreSQL, which contains login and password 'postgres'

<h1>Configuring the Eclipse:
 
<b>2 - To install AJDT:<b>

2.1 Help -> Eclipse MarketPlace -> install AJDT corresponding to your version of eclipse.

<b>3 To install Maven:<b>

3.1 Help -> Eclipse MarketPlace -> install the "Maven Integration for eclipse WTP"

<b>4 - If you do not have the JUnit 4:<b>

4.1 - Go http://www.junit.org/download the jar and add to /eclipse/plugins.

<b>Come down to!<b>

<h1>To correctly import the application just follow the following steps.

1 - Using a Terminal or a Customer Git, do the clone within your workspace.

1.1 - cd workspace

1.2 - git clone git@github.com:DiegoSousa/MessengerConcurrent.git

2 - In eclipse go to file -> import -> type in the search above "Existing maven projects" -> next -> Browser -> select the project messengerConcurrent -> Finish.

The End!

<h1>To run the tests follow exactly the following steps:

1 - Go MessengerConcurrent -> Properties -> Add Library -> JUnit -> Next -> In Junit Library Version type Version4 -> Finish

2 - Still in Properties go on the "Source" -> Add Folder -> threadControl_0.3_src -> check the option srcAspectsTC and srcTC

3 - Go Again in MessengerConcurrent -> Configure -> Convert to AspectJ Project

<b>The End!<b>

<b>Doubts?<b>

<b>Contact diego@diegosousa.com<b>


<h1>Tutorial Versão em Português:

Requisitos:
Web Server: Apache Tomcat 7+<br />
JDK: Version 6+<br />
IDE: Eclipse Java EE indigo+<br />
AJDT (AspectJ Development Tools)¹<br />
Maven: Version 2²<br />
Teste: JUnit 4+³<br />
Banco de Dados: Preferencialmente PostgreSQL, mas é possivel utilizar outros bancos,<br />
já que está implementado o framework JPA (Java Persistence API)<br />
 
<h1>Configurar o Banco de dados:

1 - <b>Instalar o PostgreSQL<b> (http://www.postgresql.org/download/)

2 - Caso ache necessario, poderá instalar o cliente PostgreSQL (http://www.pgadmin.org/download/)

3 - Criar uma base de dados com o nome messengerConcurrent e setar para a base o "Login Role" default do PostgreSQL, que contém login e senha 'postgres'

<h1>Configurar o eclipse:
  
<b>1 - Para instalar o Ajdt:<b>

1.1 - Help -> Eclipse MarketPlace -> instale o ajdt correspondente a sua versão do eclipse.

<b>2 - Para instalar o Maven:<b>

2.2 - Help -> Eclipse MarketPlace -> instale o "Maven Integration for eclipse WTP"

<b>3 - Caso não tenha o JUnit 4:

3.1 - acesse http://www.junit.org/  faça o download do jar e adicione em /eclipse/plugins.

<b>Vamos ao que interessa!<b>

<h1>Para importar corretamente a aplicação siga exatamente os seguintes passos.

1 - Usando um Terminal ou um Cliente Git, faça o clone dentro do seu workspace.

1.1 - cd workspace

1.2 - git clone git@github.com:DiegoSousa/MessengerConcurrent.git

2 - No eclipse va em file -> import -> Na busca acima procure por "Existing maven projects" -> next -> Browser -> selecione o projeto messengerConcurrent -> Finish.

Fim!

<h1>Para rodar os testes siga exatamento os seguintes passos:

1 - Vá em MessengerConcurrent -> Properties -> Add Library -> JUnit -> Next -> Em Junit Library Version digite version4 -> Finish

2 - Ainda em Properties vá na aba "Source"  -> Add Folder -> threadControl_0.3_src -> Marque a opção srcAspectsTC e srcTC

3 - Vá Novamente em MessengerConcurrent - > Configure -> Convert to AspectJ Project


<b>Fim!<b>

<b>Duvidas?<b>

<b>Entre em contato com diego@diegosousa.com<b>

[]'s
