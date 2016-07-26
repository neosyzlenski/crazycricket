# Crazy Cricket

Proposed solution for Crazy Cricket challenge asked by [Black Rock](https://github.com/blackrock/crazy-cricket). The solution contains two modules - a Kafka Processor & a REST service. Kafka Processor consumes game events from provided broker and store them in an in-memory HSQL database. REST service exposes end points for handling expected requests for game statistics with parameter validations. 

## Setup & Run
```
mvn clean package

chmod +x bin/run.sh

bin/run.sh <your kafka broker like localhost:9092>
...
Crazy Cricket REST Service started at: http://localhost:8292 <you can modify port in src/main/resources/application.properties>
...
INFO 13896 --- [       Thread-4] o.a.k.c.c.internals.ConsumerCoordinator  : Setting newly assigned partitions [TWENTY_TWENTY-0, TEST-0, LIMITED_OVERS-0] for group test
...
```

One REST API & Kafka Processor is up and runnning, publish few game events in a new terminal using Black Rock's crazy-cricket-assembly:
```
java -cp crazy-cricket-assembly-SNAPSHOT.jar com.bfm.acs.crazycricket.SampleDataProducer --kafka-broker localhost:9092
...
INFO 13896 --- [       Thread-4] o.a.k.c.c.internals.ConsumerCoordinator  : Setting newly assigned partitions [TWENTY_TWENTY-0, TEST-0, LIMITED_OVERS-0] for group test
INFO 3172 --- [       Thread-4] c.b.a.c.data.HsqlDataStoreImpl           : Saving game: Winner-oscar(England), Loser-sachin(India), Date-2016-01-01, Type-TEST
INFO 3172 --- [       Thread-4] c.b.a.c.data.HsqlDataStoreImpl           : Game saved
...
```
Kafka processor consumes the **newly published**(does not read from beginning currently) and persists them in an in-memory HSQL database. REST API exposes the required stats as:
* Current leaderboad:  
    `GET http://localhost:8292/api/leaderboard`  
    `Output:`  
    `[{"shubham":5},{"oscar":4},{"sachin":4},{"andrew":1},{"imran":1}]`  
* Date range leaderboard:  
    `GET http://localhost:8292/api/leaderboard?start=20160101&end=20160101`  
    `Output:`  
    `[{"oscar":2},{"andrew":1},{"imran":1},{"sachin":1},{"shubham":1}]`
* Current country leaderboard:  
    `GET http://localhost:8292/api/national_leaderboard`  
    `Output:`  
    `[{"India":9},{"England":4},{"Pakistan":1},{"USA":1}]`  
* Date range country leaderboard:  
    `GET http://localhost:8292/api/national_leaderboard?start=20160101&end=20160101`  
    `Output:`  
    `[{"England":2},{"India":2},{"Pakistan":1},{"USA":1}]`
* Invalid date range country leaderboard:  
    `GET http://localhost:8292/api/national_leaderboard?start=20160101&end=2016010`  
    `Output:`  
    `{"timestamp":1469570445922,"status":400,"error":"Bad Request","exception":"com.bfm.acs.crazycricket.data.InvalidDateRangeException","message":"Invalid end date, expected format: yyyyMMdd","path":"/api/national_leaderboard"}`
    