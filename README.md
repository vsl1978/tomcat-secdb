tomcat-secdb
============

Tomcat-secdb (secdb) is Tomcat's add-on that provides a support for an encrypted parameters of DataSource. You can easily configure Apache's Tomcat to use secdb:  
1. Copy secdb-1.0.jar to $CATALINA_HOME/lib  
2. Configure your datasource(s) in $CATALINA_HOME/conf/context.xml:  
    `<Resource  ............. factory="factory class name" type="javax.sql.DataSource"/>`  
3. Encrypt password, username or any other datasource parameter using secdb-1.0.jar command line interface:  
   `    java -jar secdb-1.0.jar <text to encrypt> [<text to encrypt> ...]`  
4. Replace corresponding parameters value in context.xml with the encrypted one.  
5. Run Tomcat
  
Available factories:
 - xyz.vsl.tomcat.secdb.Tomcat7 - for use with Tomcat 7, 8 and tomcat-jdbc-pool
 - xyz.vsl.tomcat.secdb.Tomcat6 - for use with Tomcat 6 and DBCP. Tomcat 7 and 8 supports DBCP, but tomcat-jdbc-pool (i.e. xyz.vsl.tomcat.secdb.Tomcat7) should be preferred
 - xyz.vsl.tomcat.secdb.C3P0 - for use with Tomcat 6, 7, 8 and C3P0 connection pool.
  
  
Example:  
_Source **context.xml** file_
```
<Resource
    auth="Container"
    name="TestDS"
    driverClassName="org.postgresql.Driver"
    maxActive="50" maxIdle="10" maxWait="-1"
    username="test"
    password="test"
    url="jdbc:postgresql://localhost:5432/testdb"
    factory="xyz.vsl.tomcat.secdb.Tomcat6"
    type="javax.sql.DataSource"
/>
<Resource
    auth="Container"
    name="FilesDS"
    driverClassName="org.postgresql.Driver"
    maxActive="50" maxIdle="10" maxWait="-1"
    username="test"
    password="test"
    url="jdbc:postgresql://localhost:5432/blobs"
    factory="xyz.vsl.tomcat.secdb.Tomcat6"
    type="javax.sql.DataSource"
/>
```
In this example we have two datasources that uses the same usernames and passwords. Now generates the encrypted values:
```
$ java -jar secdb-1.0.jar test test testdb test test
test: {AES:A1kK}Qcc0j2lKCwMNqHTPNfjSvA==
test: {AES:umBX}r4KcP1AK0ffeB5mLElyeOA==
testdb: {AES:KLZd}tNO3/ci1D7/GfYkMg+yOiw==
test: {AES:Pipx}zaqHOBq/QEtzbAXGxKmHWw==
test: {AES:hq2B}FgmRQT9QulL7PAeWo7Cj4g==
```
...and replace the original parameters. Result **context.xml** file:
```
<Resource
    auth="Container"
    name="TestDS"
    driverClassName="org.postgresql.Driver"
    maxActive="50" maxIdle="10" maxWait="-1"
    username="{AES:A1kK}Qcc0j2lKCwMNqHTPNfjSvA=="
    password="{AES:umBX}r4KcP1AK0ffeB5mLElyeOA=="
    url="jdbc:postgresql://localhost:5432/{AES:KLZd}tNO3/ci1D7/GfYkMg+yOiw=="
    factory="xyz.vsl.tomcat.secdb.Tomcat6"
    type="javax.sql.DataSource"
/>
<Resource
    auth="Container"
    name="FilesDS"
    driverClassName="org.postgresql.Driver"
    maxActive="50" maxIdle="10" maxWait="-1"
    username="{AES:Pipx}zaqHOBq/QEtzbAXGxKmHWw=="
    password="{AES:hq2B}FgmRQT9QulL7PAeWo7Cj4g=="
    url="jdbc:postgresql://localhost:5432/blobs"
    factory="xyz.vsl.tomcat.secdb.Tomcat6"
    type="javax.sql.DataSource"
/>
```
  
Sample configuration for C3P0:
```
<Resource 
    auth="Container" 
    description="DB Connection"
    driverClass="org.postgresql.Driver"
    maxPoolSize="50"
    minPoolSize="2"
    acquireIncrement="1"
    name="TestDS"
    user="{AES:A1kK}Qcc0j2lKCwMNqHTPNfjSvA=="
    password="{AES:umBX}r4KcP1AK0ffeB5mLElyeOA=="
    jdbcUrl="jdbc:postgresql://localhost:5432/{AES:KLZd}tNO3/ci1D7/GfYkMg+yOiw=="
    factory="xyz.vsl.tomcat.secdb.C3P0"
    type="javax.sql.DataSource"
/>
```
