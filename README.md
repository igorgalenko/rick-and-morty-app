# rick-and-morty-app
Using [public third-party API](https://rickandmortyapi.com/documentation/) web-app allows you get 
wiki about any movie character from the universe the animated series Rick & Morty as a JSON.

On a regular basis, the web application downloads data from a third-party
service to the internal database. Implemented API requests must work with a 
local database (i.e. fetch data from a database). It contains two endpoints:

1. GET /movie-characters/random <br>
The request randomly generates a wiki about one character in the universe the animated series Rick & Morty.
2. GET /movie-characters/by-name?name<br> 
The request takes a string as an argument, and returns a list of all characters whose name contains the search string.

### Project structure
* Controllers
* DTOs
* Models
* Repository
* Services
* Resources

### Used technologies
* Cron job to implement data synchronization with Rick & Morty api.
* Tomcat
* Spring Boot
* Spring Data JPA
* PostgresQL
* Git
* JUnit
* Docker

### Running project
* Install Docker
* Configure your credentials in [application.properties](src/main/resources/application.properties)
* Run in the Terminal:
    ```shell
    docker-compose up
    ```
* Use endpoints to sending GET requests