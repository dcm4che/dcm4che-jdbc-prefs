package org.dcm4che.jdbc.prefs;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.apache.log4j.Logger;
import org.dcm4che.jdbc.prefs.persistence.Attribute;
import org.dcm4che.jdbc.prefs.persistence.Node;

/**
 * @author Michael Backhaus <michael.backhaus@agfa.com>
 */
@Stateless
public class PreferencesImplBean {

    @PersistenceContext
    private EntityManager em;

    private static final Logger LOG = Logger.getLogger(PreferencesImplBean.class);

    public PreferencesImplBean(EntityManager em) {
        this.em = em;
    }

    public PreferencesImplBean() {
        super();
    }

    public void insertNode(Node node) {
        try {
            em.persist(node);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void removeNode(Node node) {
        try {
            em.remove(node);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void insertAttribute(Attribute attribute) {
        try {
            em.persist(attribute);
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void removeAttributeByKey(String key, Node node) {
        try {
            em.createNamedQuery(Attribute.DELETE_BY_KEY_AND_NODE_PK).setParameter("key", key)
                    .setParameter("nodePK", node.getPk()).executeUpdate();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public Node getRootNode() {
        try {
            return (Node) em.createNamedQuery(Node.GET_ROOT_NODE).getSingleResult();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public List<Node> getChildren(Node parent) {
        try {
            return em.createNamedQuery(Node.GET_CHILDREN, Node.class).setParameter(1, parent).getResultList();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public void flush() {
        try {
            em.flush();
        } catch (Exception e) {
            LOG.error(e);
        }
    }

    public void refresh(Node node) {
        em.refresh(node);
    }

}
