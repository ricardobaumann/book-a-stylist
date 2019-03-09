# Book a Stylist
This backend service helps you book a stylist for you.

## Architecture
This is a plain [Spring Boot](https://spring.io/projects/spring-boot) backend service, with a in-memory database.

## Running
You can import it into your preferred IDE, and run the `BookAStylistApplication` main class, or you can build and run the jar with:

````
./mvnw clean package && java -jar target/{generatedJarFile}
````

## Usage
1. Create stylists using 
````
POST /stylists/{id}
{
	"name": "Best stylist",
	"email": "mail@mail"
}
Content-Type: application/json
````
2. Check available slots with
````
GET /slots/available/{yyyy-MM-dd}
````
3. Based on the `slotNumber`s you got from the availability endpoint, you can create an appointment with:
````
POST /appointments
{
	"customerId": 3,
	"date": "2019-03-08",
	"slotNumber": 20
}
Content-Type: application/json
````

IMPORTANT: In case of error codes, check the returned object for more details