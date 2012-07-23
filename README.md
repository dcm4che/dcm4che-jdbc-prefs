INFO
====

The dcm4che-jdbc-prefs project provides an extension to store java preferences in a database.

Library
=======

The library requires the following system properties:

* java.util.prefs.PreferencesFactory = org.dcm4che.jdbc.prefs.PreferencesFactoryImpl 
  * Example: `-Djava.util.prefs.PreferencesFactory=org.dcm4che.jdbc.prefs.PreferencesFactoryImpl`
* jdbc.backend.url
  * Example: `-Djdbc.backend.url=jdbc:oracle:thin:@localhost:1521:xe`
* jdbc.user.name
* jdbc.user.pwd

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
