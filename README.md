# CacheProject
## Description
This application uses a Redis instance to cache results of calls to a local or remote database. Both SQLite and MySQL databases are allowed, the credentials and database URL can be specified in the application.yml file.

## Requirements
The application requires a working instance of Redis DataBase on the host system. It can be installed following [this guide](https://redis.io/docs/getting-started/). Redis IP and port can be specified in the application.yml. The application itself also requires Java.

## HTML endpoints
Application has endpoints that allow for direct access to the DataBase, direct access to Redis and get calls using cache functionality. By default it runs at localhost:8080

### Cache endpoints
The only available endpoint for cache calls is /cache/get. It accepts GET calls and requires a key parameter to specify what value we want to access. There are no default items in the database so an insert call to database should be used before using this endpoint.

### DataBase endpoints
There are following DataBase endpoints:
1. GET /database/get (parameters: key) - gets a value from a key from the database
2. GET /database/connection/up - checks if the database connection is up
3. POST /database/insert (parameters: key, value) - inserts a specific key, value pair into the database. Both are text values
4. POST /database/connection/retry - refreshes the connection to database

### Redis endpoints
There are following Redis endpoints
1. GET /redis/get (parameters: key) - gets a value from a key from Redis
2. GET /redis/exists (parameters: key) - checks if a key exists in Redis
3. POST /redis/set (parameters: key, value) - inserts a specific key, value pair into Redis. Both are text values

## Credits
This application uses [xerial.org SQLite JDBC driver](https://github.com/xerial/sqlite-jdbc) which follows the [Apache License version 2.0] (http://www.apache.org/licenses/). 
