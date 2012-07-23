/* ***** BEGIN LICENSE BLOCK *****
 * Version: Apache License Version 2.0
 *
 * The contents of this file are subject to the Apache License, Version 2.0;
 * you may not use this file except in compliance with the License. You may 
 * obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Software distributed under the License is distributed on an "AS IS" basis,
 * WITHOUT WARRANTY OF ANY KIND, either express or implied. See the License
 * for the specific language governing rights and limitations under the
 * License.
 *
 * Contributor(s):
 * See @authors listed below
 *
 * ***** END LICENSE BLOCK ***** */

package org.dcm4che.jdbc.prefs;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author Juergen Schmied <juergenschmied70@gmail.com>
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
public class JDBCBackend {

    private static final Logger LOG = Logger.getLogger(JDBCBackend.class);
    private static JDBCBackend singleton;
    private static String url = System.getProperty("jdbc.backend.url");
    private Connection prefsConnection;

    public JDBCBackend(String url) throws SQLException {
        prefsConnection = ConnectionWrapper.getConnection(url);
    }

    public static JDBCBackend getInstance() throws SQLException {
        if (singleton == null) {
            if (url == null)
                throw new RuntimeException("Missing system property 'jdbc.backend.url'.");
            singleton = new JDBCBackend(url);
        }
        return singleton;
    }

    public int insertNode(int myPK, String name) throws SQLException {
        LOG.debug("insertNode(int, String) - myPK=" + myPK + ", name=" + name);
        int newPK = -1;
        CallableStatement stmt = prefsConnection
                .prepareCall("begin insert into nodes (pk, parent, name) values(s_nodes.nextval, ?, ?) returning pk into ?; end;");
        try {
            stmt.setInt(1, myPK);
            stmt.setString(2, name);
            stmt.registerOutParameter(3, Types.INTEGER);
            stmt.execute();
            newPK = stmt.getInt(3);
        } finally {
            stmt.close();
        }
        return newPK;
    }

    public void removeNode(int myPK) throws SQLException {
        LOG.debug("removeNode(int) - myPK=" + myPK);
        PreparedStatement stmt = prefsConnection.prepareStatement("delete from nodes where pk = ?");
        try {
            stmt.setInt(1, myPK);
            stmt.execute();
        } finally {
            stmt.close();
        }
    }

    public void insertValue(int myPK, String key, String value) throws SQLException {
        LOG.debug("insertValue(int, String, String) - myPK=" + myPK + ", key=" + key + ", value=" + value);
        PreparedStatement stmt = prefsConnection
                .prepareStatement("insert into pref_value (pk, parent, key, value) values(s_pref_value.nextval, ?, ?, ?)");
        try {
            stmt.setInt(1, myPK);
            stmt.setString(2, key);
            stmt.setString(3, value);
            stmt.execute();
        } finally {
            stmt.close();
        }
    }

    public void removeValue(int myPK, String key) throws SQLException {
        LOG.debug("removeValue(int, String) - myPK=" + myPK + ", key=" + key);
        PreparedStatement stmt = prefsConnection
                .prepareStatement("delete from pref_value where parent = ? and key = ?");
        try {
            stmt.setInt(1, myPK);
            stmt.setString(2, key);
            stmt.execute();
        } finally {
            stmt.close();
        }
    }

    public void sync() throws SQLException {
        prefsConnection.commit();
    }

    public void flush() throws SQLException {
        prefsConnection.commit();
    }

    public HashMap<String, String> getAttributes(int myPK) throws SQLException {
        LOG.debug("getAttributes(int) - myPK=" + myPK);
        PreparedStatement stmt = prefsConnection.prepareCall("select key, value from pref_value where parent = ?");
        HashMap<String, String> result = new HashMap<String, String>();
        try {
            stmt.setInt(1, myPK);
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next())
                    result.put(rs.getString(1), rs.getString(2));
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
        return result;
    }

    public HashMap<String, Integer> getChildren(int myPK) throws SQLException {
        LOG.debug("getChildren(int) - myPK = " + myPK);
        PreparedStatement stmt = prefsConnection.prepareCall("select name, pk from nodes where parent = ?");
        HashMap<String, Integer> result = new HashMap<String, Integer>();
        try {
            stmt.setInt(1, myPK);
            ResultSet rs = stmt.executeQuery();
            try {
                while (rs.next()) {
                    result.put(rs.getString(1), rs.getInt(2));
                }
            } finally {
                rs.close();
            }
        } finally {
            stmt.close();
        }
        return result;
    }
}
