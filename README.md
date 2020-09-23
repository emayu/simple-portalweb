# simple-portalweb
This is a proyect to show JavaEE capabilities with RESTful and JSF client.

Steps to run this project:

1. Clone this Git repository
2. Build the application with `mvn clean package`
3. Start you Docker deamon
4. Build the Docker image with `docker build -t portalweb:1.0 .`
5. Star the Docker container with `docker run -it -p 8080:8080 portalweb:1.0`
6. Wait until the Payara server launched successfully and visit `http://localhost:8080/portalweb`


## Access to RESTful api
You can interact with the result api with relative endpoint `http://localhost:8080/portalweb/rest` this api use
basich auth (use the credentials username:test password:secret).

1. Try to get the list of user `GET http://localhost:8080/portalweb/rest/user`
2. Try to see the Role asociate to user `GET http://localhost:8080/portalweb/rest/userrole/user/1`