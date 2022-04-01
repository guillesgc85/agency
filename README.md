# How to? 

### Get Docker
Go to https://docs.docker.com/get-docker/

### docker-compose

Go to /local-environment folder and type the following command in a terminal
```
docker-compose up postgres
```

## Recommended UIs or dashboards
* postgres
  * dbeaver

##Database info :
```
- Host: localhost
- Username: postgres
- Password: postgres
- Port: 5432
```
##Create databases:

* Create **ads_360_agency** database
    ```postgresql
    CREATE DATABASE "ads_360_agency" WITH OWNER postgres;
    GRANT ALL PRIVILEGES ON DATABASE "ads_360_agency" TO postgres;
    ```
  
* Create **ads_360_agency_test_db** database for testing.   
    ```postgresql
    CREATE DATABASE "ads_360_agency_test_db" WITH OWNER postgres;
    GRANT ALL PRIVILEGES ON DATABASE "ads_360_agency_test_db" TO postgres;
    ```
## Main class to run the microservice is:
```
com.agency.ads.app.Application
```

## swagger-ui:
```
http://localhost:8082/swagger-ui.htm
```

## server.port=8082

1 Test your changes
```
$ ./gradlew test 
```

2 Build the project without Test
```
$ ./gradlew -x test build 
```

3 Build the project complete
```
$ ./gradlew build 
```

### Postman collection 
You can import postman collection from **./collections/360.agency.postman_collection.json**

