/* ***** BEGIN LICENSE BLOCK *****
 * Version: MPL 1.1/GPL 2.0/LGPL 2.1
 *
 * The contents of this file are subject to the Mozilla Public License Version
 * 1.1 (the "License"); you may not use this file except in compliance with
 * the License. You may obtain a copy of the License at
 * http://www.mozilla.org/MPL/
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * The Original Code is part of dcm4che, an implementation of DICOM(TM) in
 * Java(TM), hosted at https://github.com/gunterze/dcm4che.
 *
 * The Initial Developer of the Original Code is
 * Agfa Healthcare.
 * Portions created by the Initial Developer are Copyright (C) 2011
 * the Initial Developer. All Rights Reserved.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * Alternatively, the contents of this file may be used under the terms of
 * either the GNU General Public License Version 2 or later (the "GPL"), or
 * the GNU Lesser General Public License Version 2.1 or later (the "LGPL"),
 * in which case the provisions of the GPL or the LGPL are applicable instead
 * of those above. If you wish to allow use of your version of this file only
 * under the terms of either the GPL or the LGPL, and not to allow others to
 * use your version of this file under the terms of the MPL, indicate your
 * decision by deleting the provisions above and replace them with the notice
 * and other provisions required by the GPL or the LGPL. If you do not delete
 * the provisions above, a recipient may use your version of this file under
 * the terms of any one of the MPL, the GPL or the LGPL.
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che.jdbc.prefs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import javax.annotation.PreDestroy;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import org.apache.log4j.Logger;

/**
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
public final class ConnectionWrapper {

    static Connection conn;

    private static final Logger LOG = Logger.getLogger(ConnectionWrapper.class);

    public static Connection getConnection(String url) throws SQLException {
        if (url.startsWith("java:"))
            return getJNDIConnection(url);
        if (url.startsWith("jdbc:"))
            return getSimpleConnection(url);
        else
            throw new SQLException("Unsupported db connection url: " + url);
    }

    private static Connection getJNDIConnection(String url) throws SQLException {
        LOG.info("getJNDIConnection(String) - datasource=" + url);
        DataSource ds = null;
        try {
            InitialContext ctx = new InitialContext();
            ds = (DataSource) ctx.lookup(url);
        } catch (Exception e) {
            LOG.error("Error getting datasource", e);
        }
        return ds.getConnection();
    }

    private static Connection getSimpleConnection(String url) throws SQLException {
        LOG.debug("getSimpleConnection(String) - url=" + url);
        String userName = System.getProperty("jdbc.user.name");
        if (userName == null)
            throw new SQLException("Missing user name system property 'jdbc.user.name'.");
        
        String userPwd = System.getProperty("jdbc.user.pwd");
        if (userName == null || userPwd == null)
            throw new SQLException("Missing password system property 'jdbc.user.pwd'.");

        try {
            conn = DriverManager.getConnection(url, userName, userPwd);
        } catch (SQLException e) {
            LOG.error("Error connecting to db", e);
        }
        return conn;
    }
    
    @PreDestroy 
    public void preDestroy() throws SQLException{
        conn.close();
    }
}
