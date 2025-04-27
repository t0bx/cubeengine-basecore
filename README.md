# basecore

A modular Minecraft Paper plugin providing a foundational API for database integration, player utilities (such as coins), and event handling.

Developed by t0bx.

## Features

### Database Integration:
- Automatic creation and management of a MySQL database connection.
- Default configuration file (`database.json`) generated on first run.
- Asynchronous table creation for player coins.

### Player Utilities:
- Built-in coins provider with persistent storage.
- Extensible API for further player-related features.

### Event Handling:
- Registers listeners for player join events and more.

## Requirements
- Java 21
- PaperMC 1.21+
- MySQL Database

## Getting Started

### 1. Build
This project uses Maven:
```xml
<repository>
      <id>cubeengine-repository</id>
      <url>https://repository.t0bx.de/repository/cubeengine-repository/</url>
</repository>
```

```xml
<dependency>
    <groupId>de.t0bx</groupId>
    <artifactId>basecore</artifactId>
    <version>1.0.0</version>
    <scope>provided</scope>
</dependency>
```


### 1. Configure Database
- Edit the generated `database.json` in the plugin’s data folder:
- Restart your server after editing.

## Development
- Main class: `de.t0bx.basecore.BaseAPI`
- API version: 1.21
- See `plugin.yml` for plugin metadata.

## License
This project is licensed under the [Creative Commons Attribution-NonCommercial-NoDerivatives 4.0 International License](https://creativecommons.org/licenses/by-nc-nd/4.0/).
