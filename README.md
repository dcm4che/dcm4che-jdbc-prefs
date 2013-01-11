dcm4che-jdbc-prefs-1.x
======================
Source: https://github.com/dcm4che/dcm4che-jdbc-prefs

Tracker: http://www.dcm4che.org/jira/browse/JDBCPREFS

This project provides a wrapper for storage of Java Preferences data in a SQL backend.
It contains a JBoss AS7 deployable jar file and a jar file containing classes for use 
with command line tools to allow wrapping of java.util.prefs.PreferencesFactory to the 
according SQL backend.

Dependencies
------------
[schema-export](https://github.com/dcm4che/schema-export)

[Maven 3](http://maven.apache.org)

Build
-----
Change to the root of the project directory and execute `$ mvn install [-D db=<database>] [-D ds=<datasource>]`

Supported databases are: oracle, mysql, psql, mssql, hsql, firebird, and db2.

Example JDBC datasource: `jdbc:oracle:thin:@localhost:1521:xe`

Example JBoss AS7 datasource: `java:jboss/datasources/DicomPreferencesDS`

Note: The datasource value can also be configured after compiling the project
through editing of `META-INF/persistence.xml` within the compiled jar file 
or by setting the parameter `jdbc.prefs.datasource` with the according value
on the command line (e.g. `-Djdbc.prefs.datasource=jdbc:oracle:thin:@localhost:1521:xe`)
or as JBoss system property (e.g. `<property name="jdbc.prefs.datasource" value="java:jboss/datasources/DicomPreferencesDS"/>`).

Database Config
---------------
After compiling the project, import the file `dcm4che-jdbc-prefs/target/create-<database>.ddl` into your database.
This will create the required tables and indices.

JBoss AS7 Configuration and Deployment
--------------------------------------
# Container Configuration
* Edit the xml file of the container configuration (e.g. `standalone/configuration/standalone.xml`) and add
the following system properties underneath the `</extension>` node:

```xml
<system-properties>
    <property name="java.util.prefs.PreferencesFactory" value="org.dcm4che.jdbc.prefs.PreferencesFactoryImpl"/>
    <property name="jdbc.prefs.datasource" value="java:jboss/datasources/DicomPreferencesDS"/>
</system-properties>
```
* Configure the datasource: [JBoss AS7 Data Source Configuration](https://community.jboss.org/wiki/DataSourceConfigurationInAS7)

Note: Import an SQL driver to be used with the datasource beforehand and set the user name and password
according to your database configuration.

Example:
```xml
<subsystem xmlns="urn:jboss:domain:datasources:1.0">
    <datasources>
         <datasource jndi-name="java:jboss/datasources/DicomPreferencesDS" pool-name="DicomPreferencesDS">
             <connection-url>jdbc:oracle:thin:@localhost:1521:xe</connection-url>
             <driver>ojdbc6.jar</driver>
             <security>
                 <user-name>prefs</user-name>
                 <password>prefs</password>
             </security>
         </datasource>
    </datasources>
</subsystem>
```

# Deployment
Connect to the JBoss Command Line Interface (e.g. by executing `./<jboss-path>/bin/jboss-cli.sh -c`) and deploy the jar file: 
`[standalone@localhost:9999 /] deploy <build-path>/dcm4che-jdbc-prefs/dcm4che-jdbc-prefs/target/dcm4che-jdbc-prefs-<version>.jar`.

# Restart JBoss AS7
Once the container configuration is done and the jar file is deployed, it is required to restart JBoss in order to load the
mapping from PreferencesFactory to PreferencesFactoryImpl and to use the classes found in the deployed jar file.

The output in the server log should look similar to this:
```
...
08:28:55,205 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-2) JBAS015876: Starting deployment of "dcm4che-jdbc-prefs-1.0.0-SNAPSHOT.jar"
08:28:55,210 INFO  [org.jboss.as.server.deployment] (MSC service thread 1-3) JBAS015876: Starting deployment of "ojdbc6.jar"
08:28:55,233 INFO  [org.jboss.as.server.deployment.scanner] (MSC service thread 1-4) JBAS015012: Started FileSystemDeploymentService for directory /opt/jboss-as-7.1.1.Final-proxy-web-conf/standalone/deployments
08:28:55,498 INFO  [org.jboss.as.jpa] (MSC service thread 1-2) JBAS011401: Read persistence.xml for dcm4che-jdbc-prefs
08:28:55,713 INFO  [org.jboss.as.ejb3.deployment.processors.EjbJndiBindingsDeploymentUnitProcessor] (MSC service thread 1-2) JNDI bindings for session bean named QueryPreferencesBean in deployment unit deployment "dcm4che-jdbc-prefs-1.0.0-SNAPSHOT.jar" are as follows:

	java:global/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean!org.dcm4che.jdbc.prefs.QueryPreferences
	java:app/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean!org.dcm4che.jdbc.prefs.QueryPreferences
	java:module/QueryPreferencesBean!org.dcm4che.jdbc.prefs.QueryPreferences
	java:global/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean
	java:app/dcm4che-jdbc-prefs-1.0.0-SNAPSHOT/QueryPreferencesBean
	java:module/QueryPreferencesBean

08:28:55,834 INFO  [org.jboss.as.connector.deployers.jdbc] (MSC service thread 1-4) JBAS010403: Deploying JDBC-compliant driver class oracle.jdbc.OracleDriver (version 11.2)
08:28:55,898 INFO  [org.jboss.as.connector.subsystems.datasources] (MSC service thread 1-2) JBAS010400: Bound data source [java:jboss/datasources/DicomPreferencesDS]
08:28:56,077 INFO  [org.jboss.as.jpa] (MSC service thread 1-3) JBAS011402: Starting Persistence Unit Service 'dcm4che-jdbc-prefs-1.0.0-SNAPSHOT.jar#dcm4che-jdbc-prefs'
08:28:56,235 INFO  [org.hibernate.annotations.common.Version] (MSC service thread 1-3) HCANN000001: Hibernate Commons Annotations {4.0.1.Final}
08:28:56,243 INFO  [org.hibernate.Version] (MSC service thread 1-3) HHH000412: Hibernate Core {4.0.1.Final}
08:28:56,257 INFO  [org.hibernate.cfg.Environment] (MSC service thread 1-3) HHH000206: hibernate.properties not found
08:28:56,275 INFO  [org.hibernate.cfg.Environment] (MSC service thread 1-3) HHH000021: Bytecode provider name : javassist
08:28:56,359 INFO  [org.hibernate.ejb.Ejb3Configuration] (MSC service thread 1-3) HHH000204: Processing PersistenceUnitInfo [
	name: dcm4che-jdbc-prefs
	...]
08:28:57,134 INFO  [org.hibernate.service.jdbc.connections.internal.ConnectionProviderInitiator] (MSC service thread 1-3) HHH000130: Instantiating explicit connection provider: org.hibernate.ejb.connection.InjectedDataSourceConnectionProvider
08:28:57,738 INFO  [org.hibernate.dialect.Dialect] (MSC service thread 1-3) HHH000400: Using dialect: org.hibernate.dialect.Oracle10gDialect
08:28:57,797 INFO  [org.hibernate.engine.transaction.internal.TransactionFactoryInitiator] (MSC service thread 1-3) HHH000268: Transaction strategy: org.hibernate.engine.transaction.internal.jta.CMTTransactionFactory
08:28:57,824 INFO  [org.hibernate.hql.internal.ast.ASTQueryTranslatorFactory] (MSC service thread 1-3) HHH000397: Using ASTQueryTranslatorFactory
08:28:57,937 INFO  [org.hibernate.validator.util.Version] (MSC service thread 1-3) Hibernate Validator 4.2.0.Final
08:28:58,439 INFO  [org.hibernate.tool.hbm2ddl.SchemaValidator] (MSC service thread 1-3) HHH000229: Running schema validator
08:28:58,440 INFO  [org.hibernate.tool.hbm2ddl.SchemaValidator] (MSC service thread 1-3) HHH000102: Fetching database metadata
08:28:58,610 INFO  [org.hibernate.tool.hbm2ddl.TableMetadata] (MSC service thread 1-3) HHH000261: Table found: PREFS.ATTRIBUTE
08:28:58,610 INFO  [org.hibernate.tool.hbm2ddl.TableMetadata] (MSC service thread 1-3) HHH000037: Columns: [node, attr_key, attr_value, pk]
08:28:58,616 INFO  [org.hibernate.tool.hbm2ddl.TableMetadata] (MSC service thread 1-3) HHH000261: Table found: PREFS.NODE
08:28:58,617 INFO  [org.hibernate.tool.hbm2ddl.TableMetadata] (MSC service thread 1-3) HHH000037: Columns: [name, parent, pk]
08:28:58,834 INFO  [org.jboss.as.server] (Controller Boot Thread) JBAS018559: Deployed "dcm4che-jdbc-prefs-1.0.0-SNAPSHOT.jar"
08:28:58,835 INFO  [org.jboss.as.server] (Controller Boot Thread) JBAS018559: Deployed "ojdbc6.jar"
...
```

Configuration Data Import
-------------------------
The sub-project `dcm4che-jdbc-prefs-tool` provides a script (xmlPrefs2jdbc or xmlPrefs2jdbc.bat) for importing dcm4che compatible DICOM configuration data
into the SQL backend. To use the import script, follow these steps:
* Unzip the file `dcm4che-jdbc-prefs-tool-<version>-bin.zip`
* Copy a jdbc driver to the `/dcm4che-jdbc-prefs-tool-<version>-bin/lib/` directory
* Open the import script `/dcm4che-jdbc-prefs-tool-<version>-bin/bin/xmlPrefs2jdbc` (or xmlPrefs2jdbc.bat) in an editor of your choice
* Change the line  
```  
    # JDCB Driver  
    CP="$CP:$DCM4CHE_HOME/lib/ojdbc6.jar"  
```  
to match the jdbc driver imported above
* Change line  
```  
    # JDBC connection properties  
    JDBC="-Djdbc.prefs.datasource=jdbc:oracle:thin:@localhost:1521:xe"  
    JDBC="$JDBC -Djdbc.prefs.connection.username=prefs"  
    JDBC="$JDBC -Djdbc.prefs.connection.password=prefs"  
```  
to match the username and password for your database connection
* Run the script: `xml2prefs <xml-file>`

Example:

```
bin $ ./xmlPrefs2jdbc <path>/sample-config.xml 
08:46:35,245 INFO  - HCANN000001: Hibernate Commons Annotations {4.0.1.Final}
08:46:35,252 INFO  - HHH000412: Hibernate Core {4.1.3.Final}
08:46:35,254 INFO  - HHH000206: hibernate.properties not found
08:46:35,255 INFO  - HHH000021: Bytecode provider name : javassist
08:46:35,917 INFO  - HHH000402: Using Hibernate built-in connection pool (not for production use!)
08:46:36,013 INFO  - HHH000115: Hibernate connection pool size: 20
08:46:36,013 INFO  - HHH000006: Autocommit mode: true
08:46:36,014 INFO  - HHH000401: using driver [oracle.jdbc.OracleDriver] at URL [jdbc:oracle:thin:@localhost:1521:xe]
08:46:36,014 INFO  - HHH000046: Connection properties: {user=prefs, password=****, autocommit=true, release_mode=auto}
08:46:36,308 INFO  - HHH000400: Using dialect: org.hibernate.dialect.Oracle10gDialect
08:46:36,330 INFO  - HHH000268: Transaction strategy: org.hibernate.engine.transaction.internal.jdbc.JdbcTransactionFactory
08:46:36,334 INFO  - HHH000397: Using ASTQueryTranslatorFactory
08:46:36,548 INFO  - HHH000229: Running schema validator
08:46:36,549 INFO  - HHH000102: Fetching database metadata
08:46:36,662 INFO  - HHH000261: Table found: PREFS.ATTRIBUTE
08:46:36,662 INFO  - HHH000037: Columns: [node, attr_key, attr_value, pk]
08:46:36,668 INFO  - HHH000261: Table found: PREFS.NODE
08:46:36,668 INFO  - HHH000037: Columns: [name, parent, pk]
Hibernate: select * from ( select node0_.pk as pk0_, node0_.name as name0_, node0_.parent as parent0_ from node node0_ where node0_.name='rootNode' ) where rownum <= ?
Hibernate: select node_pk_seq.nextval from dual
Hibernate: insert into node (name, parent, pk) values (?, ?, ?)
Hibernate: select node0_.pk as pk0_0_, attributes1_.pk as pk1_1_, node0_.name as name0_0_, node0_.parent as parent0_0_, attributes1_.attr_key as attr2_1_1_, attributes1_.node as node1_1_, attributes1_.attr_value as attr3_1_1_, attributes1_.node as node0_0__, attributes1_.pk as pk0__ from node node0_ left outer join attribute attributes1_ on node0_.pk=attributes1_.node where node0_.parent=?
...
```
