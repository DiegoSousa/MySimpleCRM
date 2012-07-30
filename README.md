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
Database: PostgreSQL Preferably, but it is possible to use other banks,<br />
since the framework is implemented JPA (Java Persistence API)<br />

<h1>Configuring the Eclipse:
 
<b>1 - To install AJDT:<b>

	In Eclipse go in -> Help -> Eclipse MarketPlace -> install AJDT corresponding to your version of eclipse.

<b>2 - To install Maven:<b>

	In Eclipse go in -> Help -> Eclipse MarketPlace -> install the "Maven Integration for eclipse WTP".

<h1>To correctly import the application just follow the following steps.

<b>1 - Using a Terminal or a Customer Git, do the clone within your workspace.<b>

	cd ~/workspace/

<b>1.1 - Ctrl+C and Ctrl+Shift+V in terminal:<b>

	git clone git@github.com:DiegoSousa/MessengerConcurrent.git

<b>2 - In eclipse go in:<b> 

	file -> import -> type in the search above "Existing maven projects" -> next -> Browser -> 
	select the project messengerConcurrent -> Finish.

<b>3 - Wait until the maven download all libraries.<b>

The End!

<h1>Configuring the project:

<b>1 - Right-click in:<b>

	MessengerConcurrent -> properties -> "Source" -> Add Folder -> threadControl_<version>_src -> 
	check the option srcAspectsTC and srcTC. 

<b>2 - Right-click in:<b>

	MessengerConcurrent -> Configure -> Convert to AspectJ Project.

<h1>Configuring the Database:

<b>1 - Install PostgreSQL<b> 

	(http://www.postgresql.org/download/).

<b>1.1 - If you think necessary, you can install the customer PostgreSQL<b>

	http://www.pgadmin.org/download/

<b>1.2 -There are two ways of creating the database. Using the script creation (1.2.1) or 
creating manually using the client PostgreSQL PgAdmin (1.2.2). <b>Choose the one that interests you.<b>
 
<b>1.2.1 - "Using the script creation"<b> 

Open your terminal type:

	cd ~/workspace/MessengerConcurrent/src/main/resources/Scripts_Database

Then type:

	chmod +x createDatabase.sh

Then type:
	 
	./createDatabaseAndTables.sh

<b>1.2.2 - Creating manually using the client PostgreSQL PgAdmin:<b>

	Create a database with the name "messengerConcurrent" and set the "Login Role" default PostgreSQL, 
	which contains login and password 'postgres'.


<b>The End!<b>

<b>Doubts?<b>

<b>Contact</b> diego[at]diegosousa[dot]com <b>or</b> diego.sousa[at]dce.ufpb.br


<h1>Tutorial Versão em Português:

<h1>Configurando o Eclipse:
 
<b>1 - Para instalar o AJDT:<b>

	No Eclipse vá em -> Ajuda -> Eclipse MarketPlace -> instale o AJDT correspondente a sua versão do eclipse.

<b>2 - Para instalar o Maven:<b>

	No Eclipse vá em -> Ajuda -> Eclipse MarketPlace -> instale o "Maven Integration for eclipse WTP".

<h1>Para importar corretamente o aplicativo basta seguir os seguintes passos:

<b>1 - Usando um Terminal ou um cliente Git, faça o clone em seu workspace.<b>

	cd ~/workspace/

<b>1.1 - Ctrl+C and Ctrl+Shift+V no terminal:<b>

	git clone git@github.com:DiegoSousa/MessengerConcurrent.git

<b>2 - No Eclipse vá em:<b> 

	file -> import -> digite na local da busca "Existing maven projects" -> next -> Browser -> 
	selecione o project messengerConcurrent -> Fim.

<b>3 - Aguarde até que o maven faça o download de todas as bibliotecas.<b>


<h1>Configurando o projeto:

<b>1 - Botão direito em:<b>

	MessengerConcurrent -> propriedades -> "Source" -> Adicionar pasta -> busque por threadControl_<version>_src -> 
	Marque as opções srcAspectsTC e srcTC. 

<b>2 - Botão direito em:<b>

	MessengerConcurrent -> Configure -> Converter para Projeto AspectJ.

<h1>Configurando a Base de Dados:

<b>1 - Instale PostgreSQL<b> 

	(http://www.postgresql.org/download/).

<b>1.1 - Se achar necessário, instale o cliente PostgreSQL<b>

	http://www.pgadmin.org/download/

<b>1.2 - Existe duas formas de criar a base de dados. Usando o script Shell de criação (1.2.1) ou
criando manualmente usando o cliente PostgreSQL PgAdmin (1.2.2). <b>Escolhe a forma que mais lhe agradar.<b>
 
<b>1.2.1 - "Usando o script de criação"<b> 

Abra o terminal e digite:

	cd ~/workspace/MessengerConcurrent/src/main/resources/Scripts_Database/
		
Dê enter e digite:

	chmod +x createDatabase.sh

Então digite:

	./createDatabase.sh

<b>1.2.2 - Criando manualmente, usando o cliente PostgreSQL PgAdmin:<b>

	Crie a Base de Dados com o nome "messengerConcurrent" e set o "Login Role" default do PostgreSQL, 
	que contém o login e o password igual a 'postgres'.


<b>Fim!<b>

<b>Duvidas?<b>

<b>Contato:</b> diego[at]diegosousa[dot]com <b>or</b> diego.sousa[at]dce.ufpb.br