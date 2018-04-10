## COMP3004B - Team 75 Quest Project

This program is an implementation by Team 75 in COMP3004B of the Quests of the Round Table card game.

### How to Run

Clone the repo.  
Open the project in Eclipse.  
Right click QuestSpringApplication.java -> Run As -> Java Application (OR run iteration2.jar in GameMain folder). The host can access the game by opening Chrome and navigating to localhost:8080. Clients on the same machine can navigate to localhost:8080 after the game has been created. Clients on different machines must connect to the IP address of the host on port 8080 (Ex: 192.168.0.13:8080)

### Strategy Pattern

See the following classes for the strategy pattern:  
`AIPlayer` - abstract class  
`AIPlayerStrategy` - interface  
`AIStrategyOne`, `AIStrategyTwo` - the two AI strategies  

### Built With

* [Eclipse](https://eclipse.org/) - IDE  
* [Maven](https://maven.apache.org/) - Dependency Management  


### Authors

Cooper Jones (cdj2112)  
Steffen Smith (w01f359)  
Ruicheng Song (comp1994)  
Haiyue Sun (DishySun)
