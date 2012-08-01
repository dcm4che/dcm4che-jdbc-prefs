INFO
====

The dcm4che-jdbc-prefs project provides an extension to store java preferences in a database.

Library
=======

The library requires the following system properties:

* java.util.prefs.PreferencesFactory = org.dcm4che.jdbc.prefs.PreferencesFactoryImpl 
    * Example: 
        * command line: `-Djava.util.prefs.PreferencesFactory=org.dcm4che.jdbc.prefs.PreferencesFactoryImpl`
        * JBoss7:
            * edit container configuration (e.g. `standalone/configuration/standalone.xml`)
            * find node `<system-properties>`
            * add new child node `<property name="java.util.prefs.PreferencesFactory" value="org.dcm4che.jdbc.prefs.PreferencesFactoryImpl"/>`
* jdbc.prefs.datasource
    * Example: 
        * command line: `-Djdbc.prefs.datasource=jdbc:oracle:thin:@localhost:1521:xe`
        * JBoss7: `<property name="jdbc.prefs.datasource" value="java:jboss/datasources/DicomPreferencesDS"/>`
        * note: value can be a JNDI name (starting with "java:") or a connection URL (starting with "jdbc:")
* jdbc.prefs.connection.username (only required for Library and Tool)
* jdbc.prefs.connection.password (SAA)

Tool
====

The tool `xmlPrefs2jdbc` provides a vehicle to import XML preferences into a database.

JBoss 7 Module
==============

The JBoss 7 Module can be unpacked into the JBoss 7 modules folder.

BUILD
=====

* Library: `mvn install [-P lib]`
* Tool: `mvn install -P tool`
* Module: `mvn install -P module`
