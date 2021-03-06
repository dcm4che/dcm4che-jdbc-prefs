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

import java.util.List;

import javax.ejb.*;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;

import org.dcm4che.jdbc.prefs.entity.Attribute;
import org.dcm4che.jdbc.prefs.entity.Node;

/**
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
@Stateless
public class QueryPreferencesBean implements QueryPreferences {

    @PersistenceContext(unitName="dcm4che-jdbc-prefs")
    private EntityManager em;

    @Override
    public void insertNode(Node node) {
        em.persist(node);
    }

    @Override
    public void removeNode(Node node) {
        node = em.find(Node.class, node.getPk());
        // if the node was 'deleted' already - do nothing
        if (node != null) em.remove(node);
    }

    @Override
    public void insertAttribute(Attribute attribute) {
        em.persist(attribute);
    }

    @Override
    public void removeAttributeByKey(String key, Node node) {
        em.createNamedQuery(Attribute.DELETE_BY_KEY_AND_NODE_PK).setParameter("key", key)
                .setParameter("nodePK", node.getPk()).executeUpdate();
    }

    /**
     * Transaction disabled to avoid problems with multiple non-xa datasources (<a href="http://blogs.adobe.com/livecycle/2011/06/javax-ejb-transactionrolledbacklocalexception-could-not-enlist-in-transaction-on-entering-meta-aware-object-error-seen-in-jboss-server-log.html">multiple non xa ds issue</a>) and to speed up reading
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Node> getRootNode() {
        return em.createNamedQuery(Node.GET_ROOT_NODE, Node.class).getResultList();
    }

    /**
     * See getRootNode
     * @param parent
     * @return
     */
    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    @Override
    public List<Node> getChildren(Node parent) {
        return em.createNamedQuery(Node.GET_CHILDREN, Node.class).setParameter(1, parent).getResultList();
    }

    @Override
    public void flush() {
        em.flush();
    }

    @Override
    public Node refresh(Node node) {
        node = em.find(Node.class, node.getPk());
        return node;
    }

}
