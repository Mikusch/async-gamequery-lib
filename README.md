Asynchronous Game Query Library
===============================

[mavenImg]: https://img.shields.io/maven-central/v/com.ibasco.agql/async-gamequery-lib.svg
[mavenLink]: https://search.maven.org/search?q=com.ibasco.agql

[![Maven][mavenImg]][mavenLink] [![Donate](https://img.shields.io/badge/Donate-PayPal-green.svg)](https://www.paypal.com/cgi-bin/webscr?cmd=_s-xclick&hosted_button_id=29TX29ZSNXM64) [![Build Status](https://travis-ci.org/ribasco/async-gamequery-lib.svg?branch=master)](https://travis-ci.org/ribasco/async-gamequery-lib) [![Javadocs](https://www.javadoc.io/badge/com.ibasco.agql/async-gamequery-lib.svg)](https://www.javadoc.io/doc/com.ibasco.agql/async-gamequery-lib) [![Gitter](https://badges.gitter.im/gitterHQ/gitter.svg)](https://gitter.im/async-gamequery-lib/lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link) [![Codacy Badge](https://api.codacy.com/project/badge/Grade/2f5f445a366a4692ab8aa49b0cf4f477)](https://www.codacy.com/app/raffy/async-gamequery-lib?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=ribasco/async-gamequery-lib&amp;utm_campaign=Badge_Grade) [![Project Stats](https://www.openhub.net/p/async-gamequery-lib/widgets/project_thin_badge?format=gif&ref=sample)](https://www.openhub.net/p/async-gamequery-lib)
 
A high-performance java game query library designed for steam/source based games. It's built on top of [Netty](https://github.com/netty/netty) as it's core transport engine and use [AsyncHttpClient](https://github.com/AsyncHttpClient/async-http-client) for web services.

![Supported Games](site/resources/images/agql-project-banner-big.png "Games supported by Source Query Protocol")

Project Resources
-------------

* [Java API Docs](https://ribasco.github.io/async-gamequery-lib/apidocs)
* [Project Documentation](https://ribasco.github.io/async-gamequery-lib/)
* [Continuous Integration](https://travis-ci.org/ribasco/async-gamequery-lib)
* [Snapshot Builds](https://oss.sonatype.org/content/repositories/snapshots/com/ibasco/agql/)

Releases
-------------

1.0.0

* Moved MasterServerFilter to core package and renamed to ServerFilter
* Added new webapi implementation for interface IGameServersService
* All primitive types have been replaced by reference types in pojos
* Fixed consistency of id property types

Discussion Platforms
-----------------

If you have any inquiries,concerns or suggestions please use one of the official communication channels for this project

* [Project Issue Tracker](https://github.com/ribasco/async-gamequery-lib/issues/new) (For bug reports/issues please use this)
* [Gitter IM](https://gitter.im/async-gamequery-lib/lobby?utm_source=share-link&utm_medium=link&utm_campaign=share-link)

Implementations
----------------
 
Below is the list of what is currently implemented on the library

* Valve Master Server Query Protocol
* Valve Source Query Protocol
* Valve Steam Web API
* Valve Steam StoreFront Web API
* Valve Dota 2 Web API
* Valve CS:GO Web API 
* Valve Source Log Handler (a log monitor service)
* Supercell Clash of Clans Web API

Requirements
------------

* Java JDK 8
* Apache Commons Lang 3.x
* Apache Commons Math 3.x
* Netty 4.1.x
* AsyncHttpClient 2.5.x
* SLF4J 1.7.x
* Google Gson 2.8.x
* Google Guava 23.x
 
Installation
------------

Just add the following dependencies to your maven pom.xml. Only include the modules you need.

### Install from Maven Central

**Valve Master Server Query Protocol**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-master</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Valve Source Query Protocol**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-source-query</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Valve Steam Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-steam-webapi</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Valve Dota 2 Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-dota2-webapi</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Valve CS:GO Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-csgo-webapi</artifactId>
    <version>1.0.0</version>
</dependency>
```

**Supercell Clash of Clans Web API**

```xml
<dependency>
    <groupId>com.ibasco.agql</groupId>
    <artifactId>agql-coc-webapi</artifactId>
    <version>1.0.0</version>
</dependency>
```

### Install from Source

Clone from remote repository then `mvn install`. All of the modules will be installed to your local maven repository.

~~~bash
git clone https://github.com/ribasco/async-gamequery-lib.git
cd async-gamequery-lib
mvn install
~~~

Usage
------------

For usage examples, please refer to the [site docs](http://ribasco.github.io/async-gamequery-lib/).

Interactive Examples
--------------------

To run the available examples, I have included a convenience script (`run-example.sh`) that will allow you to pick a specific example you want to run. 

The script accepts a "key" that represents an example application. To get a list of keys, simply invoke the script without arguments, for example: 

~~~bash
raffy@spinmetal:~/projects/async-gamequery-lib$ ./run-example.sh
Error: Missing Example Key. Please specify the example key. (e.g. source-query)

====================================================================
List of available examples
====================================================================
- Source Server Query Example      (key: source-query)
- Master Server Query Example      (key: master-query)
- Source Rcon Example              (key: source-rcon)
- Clash of Clans Web API Example   (key: coc-webapi)
- CS:GO Web API Example            (key: csgo-webapi)
- Steam Web API Example            (key: steam-webapi)
- Steam Storefront Web API Example (key: steam-store-webapi)
- Source Log Listener Example      (key: source-logger)
- Steam Econ Web API Example       (key: steam-econ-webapi)
- Dota2 Web API Example            (key: dota2-webapi)
~~~

If you are running a web service type example, you  will be prompted with an API key. Simply copy and paste the key to the console.

~~~
raffy@spinmetal:~/projects/async-gamequery-lib$ ./run-example.sh coc-webapi
Running example for coc-webapi
[INFO] Scanning for projects...
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] Building AGQL - Examples 0.1.5
[INFO] ------------------------------------------------------------------------
[INFO]
[INFO] --- exec-maven-plugin:1.5.0:java (default-cli) @ agql-lib-examples ---
19:59:25.659 [com.ibasco.agql.examples.base.ExampleRunner.main()] INFO  com.ibasco.agql.examples.base.ExampleRunner - Running Example : coc-webapi
Please input your API Token:
~~~

**Note:**
* Don't forget to perform a `mvn clean install` before running an example
* The output can be reviewed from the `logs` directory under the project's directory.

Protocol Specifications
-----------------------

References you might find helpful regarding the implementations

* [Valve Source RCON Protocol](https://developer.valvesoftware.com/wiki/Source_RCON_Protocol)
* [Valve Master Server Query Protocol](https://developer.valvesoftware.com/wiki/Master_Server_Query_Protocol)
* [Valve Source Query Protocol](https://developer.valvesoftware.com/wiki/Server_queries)
* [Valve TF2 Web API Wiki](https://wiki.teamfortress.com/wiki/WebAPI)
* [Valve Steam Web API](https://developer.valvesoftware.com/wiki/Steam_Web_API)
* [Valve Steam Storefront API](https://wiki.teamfortress.com/wiki/User:RJackson/StorefrontAPI)
* [Clash of Clans Web API](https://developer.clashofclans.com/#/documentation)
* [xPaw Steam Web API Documentation](https://lab.xpaw.me/steam_api_documentation.html)


Source Compatible Games
-----------------------

The list of games compatible with the source query protocol (Updated as of 4/21/2019)

| App ID  | Name                                          |
|---------|-----------------------------------------------|
| 10      | Counter-Strike                                |
| 20      | Team Fortress Classic                         |
| 30      | Day of Defeat                                 |
| 40      | Deathmatch Classic                            |
| 50      | Half-Life: Opposing Force                     |
| 60      | Ricochet                                      |
| 70      | Half-Life                                     |
| 80      | Counter-Strike: Condition Zero                |
| 240     | Counter-Strike: Source                        |
| 300     | Day of Defeat: Source                         |
| 320     | Half-Life 2: Deathmatch                       |
| 360     | Half-Life Deathmatch: Source                  |
| 440     | Team Fortress 2                               |
| 500     | Left 4 Dead                                   |
| 550     | Left 4 Dead 2                                 |
| 630     | Alien Swarm                                   |
| 730     | Counter-Strike: Global Offensive              |
| 1200    | Red Orchestra: Ostfront 41-45                 |
| 1250    | Killing Floor                                 |
| 4000    | Garry's Mod                                   |
| 10500   | Total War: EMPIRE – Definitive Edition        |
| 10680   | Aliens vs. Predator™                          |
| 16900   | GROUND BRANCH                                 |
| 17500   | Zombie Panic! Source                          |
| 17570   | Pirates, Vikings, and Knights II              |
| 17580   | Dystopia                                      |
| 17710   | Nuclear Dawn                                  |
| 17740   | Empires Mod                                   |
| 22350   | BRINK                                         |
| 27920   | Booster Trooper                               |
| 33930   | Arma 2: Operation Arrowhead                   |
| 34030   | Total War: NAPOLEON – Definitive Edition      |
| 39000   | Moonbase Alpha                                |
| 41070   | Serious Sam 3: BFE                            |
| 42120   | Lead and Gold: Gangs of the Wild West         |
| 55100   | Homefront                                     |
| 63200   | Monday Night Combat                           |
| 63380   | Sniper Elite V2                               |
| 70000   | Dino D-Day                                    |
| 91700   | E.Y.E: Divine Cybermancy                      |
| 107410  | Arma 3                                        |
| 108600  | Project Zomboid                               |
| 203290  | America's Army: Proving Grounds               |
| 204340  | Serious Sam 2                                 |
| 207230  | ArcheBlade™                                   |
| 214190  | Minimum                                       |
| 215470  | Primal Carnage                                |
| 219640  | Chivalry: Medieval Warfare                    |
| 221100  | DayZ                                          |
| 222880  | Insurgency                                    |
| 224260  | No More Room in Hell                          |
| 225600  | Blade Symphony                                |
| 225840  | Sven Co-op                                    |
| 228380  | Wreckfest                                     |
| 230190  | War for the Overworld                         |
| 231330  | Deadfall Adventures                           |
| 232090  | Killing Floor 2                               |
| 234630  | Project CARS                                  |
| 238090  | Sniper Elite 3                                |
| 238430  | Contagion                                     |
| 242610  | Grappledrome                                  |
| 242760  | The Forest                                    |
| 243800  | Gas Guzzlers Extreme                          |
| 244850  | Space Engineers                               |
| 246700  | Strike Vector                                 |
| 247730  | Nether: Resurrected                           |
| 251570  | 7 Days to Die                                 |
| 252490  | Rust                                          |
| 259080  | Just Cause 2: Multiplayer Mod                 |
| 259570  | EDEN STAR                                     |
| 263060  | Blockstorm                                    |
| 265630  | Fistful of Frags                              |
| 266470  | Dark Horizons: Mechanized Corps               |
| 282440  | Quake Live™                                   |
| 290080  | Life is Feudal: Your Own                      |
| 293220  | H-Hour: World's Elite                         |
| 296300  | Ballistic Overkill                            |
| 299740  | Miscreated                                    |
| 300380  | Road Redemption                               |
| 304930  | Unturned                                      |
| 311210  | Call of Duty®: Black Ops III                  |
| 312660  | Sniper Elite 4                                |
| 317360  | Double Action: Boogaloo                       |
| 318100  | AXYOS                                         |
| 321360  | Primal Carnage: Extinction                    |
| 321400  | Supraball                                     |
| 322330  | Don't Starve Together                         |
| 324080  | Rising World                                  |
| 324810  | TOXIKK™                                       |
| 328070  | Reflex Arena                                  |
| 332500  | GRAV                                          |
| 333950  | Medieval Engineers                            |
| 334540  | Vox Machinae                                  |
| 335430  | Grimoire: Manastorm                           |
| 346110  | ARK: Survival Evolved                         |
| 346330  | BrainBread 2                                  |
| 349510  | Hanako: Honor &amp; Blade                     |
| 351290  | SURVIVAL                                      |
| 355180  | Codename CURE                                 |
| 355400  | Diesel Guns                                   |
| 360940  | The Mean Greens - Plastic Warfare             |
| 362890  | Black Mesa                                    |
| 366220  | Wurm Unlimited                                |
| 366440  | Savage Resurrection                           |
| 367270  | Angels Fall First                             |
| 374280  | Hired Ops                                     |
| 375230  | Warhammer 40,000: Eternal Crusade             |
| 376210  | The Isle                                      |
| 377140  | Hide &amp; Hold Out - H2o                     |
| 378860  | Project CARS 2                                |
| 383120  | Empyrion - Galactic Survival                  |
| 383790  | The Ship: Remasted                            |
| 393380  | Squad                                         |
| 393420  | Hurtworld                                     |
| 393430  | Ice Lakes                                     |
| 394690  | Tower Unite                                   |
| 407530  | ARK: Survival Of The Fittest                  |
| 408900  | Unfortunate Spacemen                          |
| 411480  | BATTLECREW™ Space Pirates                     |
| 412450  | The Black Death                               |
| 418460  | Rising Storm 2: Vietnam                       |
| 420290  | Blackwake                                     |
| 424030  | War of Rights                                 |
| 431450  | Alchemist's Awakening                         |
| 431600  | Automobilista                                 |
| 434510  | Formicide                                     |
| 436260  | Ultimate Arena                                |
| 437610  | SQUIDS FROM SPACE                             |
| 438740  | Friday the 13th: The Game                     |
| 439370  | Midair                                        |
| 440900  | Conan Exiles                                  |
| 447820  | Day of Infamy                                 |
| 454350  | Days of War                                   |
| 461990  | The Prison Game                               |
| 462440  | ROKH                                          |
| 467820  | Zero G Arena                                  |
| 476360  | Strike Vector EX                              |
| 487120  | Citadel: Forged with Fire                     |
| 488430  | Galaxy in Turmoil                             |
| 489940  | BATTALION 1944                                |
| 505460  | Foxhole                                       |
| 510840  | Evolvation                                    |
| 517280  | ZEscape                                       |
| 529180  | Dark and Light                                |
| 530700  | Argo                                          |
| 541300  | Survive the Nights                            |
| 544550  | Stationeers                                   |
| 555160  | Pavlov VR                                     |
| 555440  | Deathgarden                                   |
| 556780  | Neptune: Arena FPS                            |
| 559650  | Witch It                                      |
| 564310  | Serious Sam Fusion 2017 (beta)                |
| 569530  | Sky Noon                                      |
| 574080  | Fog of War                                    |
| 581320  | Insurgency: Sandstorm                         |
| 581910  | Iron Armada                                   |
| 588120  | Capsa                                         |
| 590720  | Cobalt WASD                                   |
| 593600  | PixARK                                        |
| 595120  | Cold Space                                    |
| 600180  | Light Strike Array                            |
| 612660  | Just Us                                       |
| 619910  | Just Cause™ 3: Multiplayer Mod                |
| 620590  | Ancestors Legacy                              |
| 626200  | Dead Alliance™                                |
| 626680  | Kreedz Climbing                               |
| 629760  | MORDHAU                                       |
| 636620  | Tanks VR                                      |
| 638070  | Murderous Pursuits                            |
| 640860  | Stone Rage                                    |
| 644290  | MEMORIES OF MARS                              |
| 647640  | ShockRods                                     |
| 647740  | Airmen                                        |
| 660920  | The Warhorn                                   |
| 672040  | WarFallen                                     |
| 673560  | IOSoccer                                      |
| 674020  | World War 3                                   |
| 677480  | Outpost Zero                                  |
| 679100  | Aequitas Orbis                                |
| 686810  | Hell Let Loose                                |
| 689570  | BattleCore Arena                              |
| 691020  | Fog Of War - Free Edition                     |
| 704020  | Master Arena                                  |
| 707010  | Will To Live Online                           |
| 711810  | Never Split the Party                         |
| 715400  | Frozen Flame                                  |
| 719890  | Beasts of Bermuda                             |
| 728540  | Islands of Nyne: Battle Royale                |
| 732340  | Ball Kicker                                   |
| 734580  | BattleRush                                    |
| 735600  | BANZAI ROYALE                                 |
| 736220  | Post Scriptum                                 |
| 746880  | Spoxel                                        |
| 750800  | Egress                                        |
| 751240  | Fractured Lands                               |
| 754530  | IL-2 Sturmovik: Cliffs of Dover Blitz Edition |
| 764920  | Fear the Night - 恐惧之夜                     |
| 773920  | scram                                         |
| 792990  | Identity                                      |
| 794490  | Journey Of Life                               |
| 801140  | unitystation                                  |
| 802080  | Pillage                                       |
| 811310  | Soviet Lunapark VR                            |
| 821290  | Rune                                          |
| 834910  | ATLAS                                         |
| 840800  | Outlaws of the Old West                       |
| 841070  | Dominance                                     |
| 845660  | Super Versus                                  |
| 869130  | World of Zombies                              |
| 871990  | BattleRush 2                                  |
| 889400  | StickyBots                                    |
| 903730  | AXIOM SOCCER                                  |
| 903950  | Last Oasis                                    |
| 916940  | 利刃 (Blade)                                  |
| 929060  | Road to Eden                                  |
| 941850  | Vanguard: Normandy 1944                       |
| 955410  | Armoured Alliance                             |
| 963930  | Contractors                                   |
| 982760  | Medieval Towns                                |
| 1039870 | Survival Frenzy                               |
| 1045750 | 光明决 DUEX                                   |


Contributing
------------

Fork it and submit a pull request. Any type of contributions are welcome.

Special Thanks/Sponsors
------------------------

* ej Technologies - Developer of the award-winning JProfiler, a full-featured "All-in-one" Java Profiler. Click on the icon below to find out more. 

  [![JProfiler](https://www.ej-technologies.com/images/product_banners/jprofiler_medium.png)](http://www.ej-technologies.com/products/jprofiler/overview.html)
  
* JetBrains - For providing the open-source license for their awesome Java IDE. 
 
  [![IntelliJ IDEA](site/resources/images/intellij-icon.png)](https://www.jetbrains.com/idea)
