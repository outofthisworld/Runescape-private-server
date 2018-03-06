

A runescape private server implementation

Currently implemented:
- Java NIO for networking
- Asynchronous player loading/saving
- Event bus architecture
	- Build local players list in between successive executions of the main world loop.
- Virtual world system
- Store and retrieve players by regions for quicker processing.
- Update block caching.
- No looping to find free slots in the world (pretty common) - store free index's in hashset.
- MongoDB for storage - Makes it easier to create websites as all data is within db for access.
- Pretty well documented, could be better but writing it is so boring >.<
- Player login with RSA encryption
- Banking,inventory and equipment
- Player updating
- Common commands (::master,::runes,::search)
- Player saving and loading
- MongoDB data store
- Player walking/movement (including run/run energy formula)
- JSON definitions for virtually all immutable data, e.g spells,prayer,
  weapon interfaces.
- Networking infrastructure can currently support 6-8k players
  online simultaneously on x64 i7-6800k @ 3.40 ghz with 16gb RAM


![alt text](https://raw.githubusercontent.com/outofthisworld/NioServer/master/images/Untitled.png)
![alt text](https://github.com/outofthisworld/NioServer/blob/master/images/Untitled1.png)


Next steps:
- Private messaging
- Chat
- JAGGRAB.
- Player combat (specials, ect)
- Player dueling
- Player trading
- Player skills
    - Runecrafting
    - Construction
    - Agility
    - Herblore  
    - Thieving  
    - Crafting  
    - Fletching 
    - Slayer 
    - Hunter 
    - Mining 
    - Smithing 
    - Fishing 
    - Cooking
    - Fire making
    - Woodcutting
    - Farming
- Improved player movement (collision detection/clipping)
- Minigames (Pest control,jad)

