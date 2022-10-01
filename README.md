# Track-stats
GUI java application for displaying track data

## Warning

This application is mostly working. Some displayed data might be incorrect. I am hoping to fix this in future.

# Contents

* [About](#about)
* [How to use](#how-to-use)
    * [Setting up database](#setting-up-database)
    * [Setting up application](#setting-up-application)
        * [Running java archive](#running-java-archive)
        * [Compiling and running source code](#compiling-and-running-source-code)
* [Languages](#languages)


# About

I wanted to build my own FOSS application, so this is my first publicly available application. This application is made for loading location data from csv _(comma separated values)_ file. I used it with [OpenTracks](https://f-droid.org/en/packages/de.dennisguse.opentracks/) from f-droid.

This app is not yet tested, so there might be a lot of bugs. Feel free to report them on GitHub, I'll try my best to fix them as soon as possible.

# How to use

There are several choices to run this application:
1. Run some parts in docker and some natively
2. Run all natively

## Setting up database

This app is intended to store all data to database. For my testing I used PostgresSQL database. PostgreSQL can be installed by following instructions on their [website](https://www.postgresql.org/). Or if docker is already installed there is **docker-compose.yml** file available at `<projectRoot>/etc/docker/onlydb/docker-compose.yml`.

Command for starting database with docker is: ```docker compose up```

To install docker follow instructions on their [website](https://www.docker.com/).

If local postgres instance is used, then database named `locationData` should exist. Currently app has username set to `trackApp` and password to `appSecretPassword`. This can be edited by changing configuration in `<projectRoot>/src/main/resources/META-INF/persistence.xml`.

## Setting up application

### Running java archive

Next you will need to start application. Application requires **Java 16** or newer. Precompiled and packaged application code can be downloaded form releases pages on GitHub.

Once java archive _(jar)_ is downloaded app can be started by executing following command in terminal: 
```java -jar  track-stat-0.0.1-jar-with-dependencies.jar```

### Compiling and running source code

Additional requirement for this choice is maven. I was using version **3.8.5**.
Source code can be compiled and ran by executing:
```mvn exec:java -Dexec.mainClass=hr.penguin9915.desktop.trackstats.Main```

# Languages

Currently app is available in 2 languages, English and Croatian. On first start application will be on English. To change that in menu bar choose options, and then settings. In settings under default.lang set string to hr. After restart application should be on Croatian.

## Available languages
* en - English
* hr - Croatian