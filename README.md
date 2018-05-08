# money-transactions
Simple java Restful API to transfer money between accounts

### Technologies
- JAX-RS API
- H2 in memory database
- Java REST Client Application (for Test and Demo app) [runs as Java application, does not need any external server to run the program]
- Tomcat8.0 server


### How to run

- Execute the TestClass java file which is a JAX Client application developed to test RESTFUL API to transfer Money between accounts
- Right click, run as java application
- All available Services gets executed and results are printed in the console.

Application runs as Java application on localhost port 8081, H2 in memory database (Account related dummy data added using demo.sql file placed under Test folder) and
below services gets executed

- http://localhost:8081/MoneyTransaction/restService
- http://localhost:8081/MoneyTransaction/restService/AccountService/all
- http://localhost:8081/MoneyTransaction/restService/AccountService/101
- http://localhost:8081/MoneyTransaction/restService/AccountService/create
- http://localhost:8081/MoneyTransaction/restService/AccountService/101/deposit/200
- http://localhost:8081/MoneyTransaction/restService/AccountService/222/withdraw/100
- http://localhost:8081/MoneyTransaction/restService/transaction

### Available Services

| HTTP METHOD | PATH | USAGE |
| -----------| ------ | ------ |
| GET | /AccountService/{accountId} | get account by accountId | 
| GET | /AccountServiceaccount/all | get all accounts | 
| PUT | /AccountService/create | create a new account
| PUT | /AccountService/{accountId}/withdraw/{amount} | withdraw money from account | 
| PUT | /AccountService/{accountId}/deposit/{amount} | deposit money to account | 
| POST | /transaction | perform transaction between 2 user accounts | 

### Http Status
- 200 OK: The request has succeeded
- 400 Bad Request: The request could not be understood by the server 
- 404 Not Found: The requested resource cannot be found
- 500 Internal Server Error: The server encountered an unexpected condition 

### Sample JSON for Account
##### Account: : 

```sh
{  
   "accountId":"101",
   "amount":100.0000,
} 
```

#### Transaction:
```sh
{  
   "amount":100.0000,
   "fromAccountId":101,
   "toAccountId":222
}
```


