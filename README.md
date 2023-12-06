# simple-portalweb
This is a proyect to show JavaEE capabilities with RESTful and JSF client.

Steps to run this project:

1. Clone this Git repository
2. Build the application with `mvn clean package`
3. Start you Docker deamon
4. Build the Docker image with `docker build -t portalweb:1.1 .`
5. Star Docker compose with `docker-compose -f docker/simple/docker-compose.yml up --force-recreate`
6. Wait until the Payara server launched successfully and visit `http://localhost:8080/portalweb`

There are 4 users created Joe, Sam, Tom, Sue and developer. All use 'secret' as password

Role Maping
|      User     |      Role     |  Description        |
| ------------- | ------------- | ------------------- |
| Joe           | USER_ROLE     | can access          |
| Sam           | ADMIN_ROLE    | can access and admin|
| developer     | ADMIN_ROLE    | can access and admin|


All other user cann't access by default


## Access to RESTful api
You can interact with the result api with relative endpoint `http://localhost:8080/portalweb-service/rest` this api use
basich auth (use the credentials username:Joe password:secret, use the same users above).

1. Try to get the list of user `GET http://localhost:8080/portalweb-service/rest/user`
2. Try to see the Role asociate to user `GET http://localhost:8080/portalweb-service/rest/userrole/user/1`
