

A runescape private server implementation


Features:
- Java NIO for networking
- Asynchronous player loading/saving
- Event bus architecture
	- Build local players list inbetween successive executions of the main world loop.
- Virtual world system
- Store and retrieve players by regions for quicker processing.
- Update block caching.
- No looping to find free slots in the world (pretty common) - store free index's in hashset.
- MongoDB for storage - Makes it easier to create websites as all data is within db for access.
- Pretty well documented, could be better but writing it is so boring >.<


[[https://github.com/outofthisworld/NioServer/images/Untitled.png|alt=octocat]]
[[https://github.com/outofthisworld/NioServer/images/Untitled1.png|alt=octocat]]


Next steps:

- JAGGRAB.
- Player combat (specials, ect)
- Player dueling
- Player trading
- Npc walking/movement
- Minigames

