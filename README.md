### Commands Windows
>".\gradlew bootRun" to run backend project

>".\gradlew" to build backend project

>".\gradlew test" to run tests

>".\gradlew tasks" for menu of cmd

>"docker image build -t warehouse ."

>"docker container run -p 9091:9091 warehouse"

### Reference Links
For further reference, please consider the following sections:

* [Backend](http://localhost:9091)
* [Get all products](http://localhost:9091/products/all)
* [Delete product](http://localhost:9091/product/remove)
* [Backend](http://localhost:9091)
* [H2 database with JDBC URL "jdbc:h2:mem:app"](http://localhost:9091/console)
* [Web server status](http://localhost:9091/actuator/health)


### Issues
- Docker image is a bit big, it could be optimized further
- ID's could used GUID in order to work in a more distributed way.
- ID's could be @GeneratedValue but this seems not to be required by the specifications.
- More tests as many cases are not tested but I was told to only put in a days worth of effort.
- There are some potential hacks if put and post are done in the wrong order but this seems to be part of the architecture that was specified. 
- Personlly I think the system could be done better but given the architectural limits of the specifications, this would have to be discussed with product, team and managers.
