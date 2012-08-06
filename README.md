INFO
====

The dcm4che-jdbc-prefs project provides an extension to store java preferences in a database.

Library
=======

The library requires the following system properties:

* java.util.prefs.PreferencesFactoryl 
    * Values: org.dcm4che.jdbc.prefs.PreferencesFactoryImpl (e.g. for JBoss JNDI lookup) or org.dcm4che.jdbc.prefs.PreferencesFactoryJDBCImpl (e.g. for command line tool JDBC initalization)
    * Example: 
        * command line: `-Djava.util.prefs.PreferencesFactory=org.dcm4che.jdbc.prefs.PreferencesFactoryJDBCImpl`
        * JBoss7:
            * edit container configuration (e.g. `standalone/configuration/standalone.xml`)
            * find node `<system-properties>` (if not present create `<system-properties></system-properties>` as child under root node `<server xmlns="urn:jboss:domain:1.2">`)
            * add new child node `<property name="java.util.prefs.PreferencesFactory" value="org.dcm4che.jdbc.prefs.PreferencesFactoryImpl"/>`
* jdbc.prefs.datasource
    * Example: 
        * command line: `-Djdbc.prefs.datasource=jdbc:oracle:thin:@localhost:1521:xe`
        * JBoss7: `<property name="jdbc.prefs.datasource" value="java:jboss/datasources/DicomPreferencesDS"/>`
        * note: value can be a JNDI name (starting with "java:") or a connection URL (starting with "jdbc:")
* jdbc.prefs.connection.username (only required for Library and Tool)
* jdbc.prefs.connection.password (SAA)
* jdbc.prefs.jndi.timeout (time in seconds, used in JBoss web-app, default value: 30)

Tool
====

The tool `xmlPrefs2jdbc` provides a vehicle to import XML preferences into a database.

JBoss 7 Web-App
===============

The web-app can be deployed in an application server. It requires a mapping to a configured datasource (SAA).

BUILD
=====

* Library: `mvn install [-P lib]`
* Tool: `mvn install -P tool`
* Web-App: `mvn install -P jboss`
