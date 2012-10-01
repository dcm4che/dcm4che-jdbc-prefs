INFO
====

The dcm4che-jdbc-prefs project provides a JBoss deployable web-app to store java preferences in a database.

The project requires the following system properties:

* java.util.prefs.PreferencesFactoryl 
    * Values: org.dcm4che.jdbc.prefs.PreferencesFactoryImpl
    * JBoss AS7 Configuration: 
        * edit container configuration (e.g. `standalone/configuration/standalone.xml`)
        * find node `<system-properties>` (if not present create `<system-properties></system-properties>` as child under root node `<server xmlns="urn:jboss:domain:1.2">`)
        * add new child node `<property name="java.util.prefs.PreferencesFactory" value="org.dcm4che.jdbc.prefs.PreferencesFactoryImpl"/>`
* jdbc.prefs.datasource
    * JBoss AS7 Configration Example: 
        * JBoss7: `<property name="jdbc.prefs.datasource" value="java:jboss/datasources/DicomPreferencesDS"/>`
* jdbc.prefs.jndi.timeout (optional, time in seconds, used in JBoss web-app, default value: 30)

Note: The web-app requires a mapping to a configured datasource (see above).

Build
=====

* `mvn install`
