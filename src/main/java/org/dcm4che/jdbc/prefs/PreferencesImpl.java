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

import java.sql.SQLException;
import java.util.HashMap;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.BackingStoreException;

import org.apache.log4j.Logger;


/**
 * @author Juergen Schmied <juergenschmied70@gmail.com>
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
public class PreferencesImpl extends AbstractPreferences {

    private static final Logger logger = Logger.getLogger(PreferencesImpl.class);
    private int myPK;
    private HashMap<String, String> attributes;
    private HashMap<String, Integer> childs;

    private HashMap<String, Integer> childs() {
        if (childs == null)
            try {
                childs = JDBCBackend.getInstance().getChildren(myPK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return childs;
    }

    private HashMap<String, String> attributes() {
        if (attributes == null)
            try {
                attributes = JDBCBackend.getInstance().getAttributes(myPK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        return attributes;
    }

    protected PreferencesImpl() {
        super(null, "");
        myPK = 2;
    }

    public PreferencesImpl(AbstractPreferences parent, String name, int childPK) {
        super(parent, name);
        myPK = childPK;
    }

    @Override
    protected AbstractPreferences childSpi(String name) {
        logger.debug("childSpi(String) - name = " + name);
        Integer childPK = childs().get(name);
        if (childPK == null) {
            logger.debug("create new Node name=" + name);
            try {
                childPK = JDBCBackend.getInstance().insertNode(myPK, name);
                childs().put(name, childPK);
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return new PreferencesImpl(this, name, childPK);
    }

    @Override
    protected String[] childrenNamesSpi() throws BackingStoreException {
        logger.debug("childrenNamesSpi() myPK = " + myPK);
        return childs().keySet().toArray(new String[childs.size()]);
    }

    @Override
    protected String getSpi(String key) {
        logger.debug("getSpi(String) - key=" + key);
        return attributes().get(key);
    }

    @Override
    protected String[] keysSpi() throws BackingStoreException {
        logger.debug("keysSpi()");
        return attributes().keySet().toArray(new String[attributes.size()]);
    }

    @Override
    public String name() {
        return (this.parent() == null) ? "root" : super.name();
    }

    @Override
    protected void putSpi(String key, String value) {
        logger.debug("putSpi(String, String) - key=" + key + ", value=" + value);
        try {
            JDBCBackend instance = JDBCBackend.getInstance();
            instance.removeValue(myPK, key);
            attributes().remove(key);
            instance.insertValue(myPK, key, value);
            attributes().put(key, value);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void removeNodeSpi() throws BackingStoreException {
        logger.debug("removeNodeSpi() pk = " + myPK);
        try {
            JDBCBackend.getInstance().removeNode(myPK);
            ((PreferencesImpl) parent()).childs().remove(name());
        } catch (SQLException e) {
            throw new BackingStoreException(e);
        }
    }

    @Override
    protected void removeSpi(String key) {
        logger.debug("removeSpi(String) - key = " + key);
        try {
            JDBCBackend.getInstance().removeValue(myPK, key);
            attributes().remove(key);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected void syncSpi() throws BackingStoreException {
        logger.debug("syncSpi()");
        try {
            JDBCBackend.getInstance().sync();
        } catch (SQLException e) {
            throw new BackingStoreException(e);
        }
    }

    @Override
    protected void flushSpi() throws BackingStoreException {
        logger.debug("flushSpi()");
        try {
            JDBCBackend.getInstance().flush();
        } catch (SQLException e) {
            throw new BackingStoreException(e);
        }
    }
}
