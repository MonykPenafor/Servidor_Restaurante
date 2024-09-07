package ifmt.cba.persistencia;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceException;

public class DAO<VO> {

	protected EntityManager entityManager;

	public DAO(EntityManager entityManager) throws PersistenciaException {
		this.entityManager = entityManager;
	}

	// public void incluir(VO vo) throws PersistenciaException {
	// 	try {
	// 		this.entityManager.persist(vo);
	// 	} catch (Exception e) {
	// 		throw new PersistenciaException("Erro ao incluir " + vo.getClass() + " - " + e.getMessage());
	// 	}
	// }

	public Object incluir(VO vo) throws PersistenciaException {
		try {
			if (entityManager.contains(vo) || getId(vo) != null) {
				vo = this.entityManager.merge(vo); // Se a entidade já existe, utilize merge
			} else {
				this.entityManager.persist(vo); // Se for uma nova entidade, utilize persist
			}
			this.entityManager.flush();
			return getId(vo);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao incluir " + vo.getClass() + " - " + e.getMessage());
		}
	}

    private Object getId(VO vo) throws PersistenceException {
        try {
            return entityManager.getEntityManagerFactory().getPersistenceUnitUtil().getIdentifier(vo);
        } catch (Exception e) {
            throw new PersistenceException("Erro ao obter ID do objeto " + vo.getClass() + " - " + e.getMessage());
        }
    }


	public void alterar(VO vo) throws PersistenciaException {
		try {
			this.entityManager.merge(vo);
		} catch (Exception e) {
			throw new PersistenciaException("Erro ao alterar " + vo.getClass() + " - " + e.getMessage());
		}
	}

	// public void excluir(VO vo) throws PersistenciaException {
	// 	try {
	// 		this.entityManager.remove(vo);
	// 	} catch (Exception e) {
	// 		throw new PersistenciaException("Erro ao excluir " + vo.getClass() + " - " + e.getMessage());
	// 	}
	// }

	public void excluir(VO vo) throws PersistenciaException {
        try {
            if (!this.entityManager.contains(vo)) {
                vo = this.entityManager.merge(vo);  // Reanexa a entidade se ela estiver detached
            }
            this.entityManager.remove(vo);  // Remove a entidade
        } catch (Exception e) {
            throw new PersistenciaException("Erro ao excluir " + vo.getClass() + " - " + e.getMessage());
        }
    }
	
	public EntityManager getEntityManager() {
		return entityManager;
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
	
	public void beginTransaction() {
		this.entityManager.getTransaction().begin();
	}
	
	public void commitTransaction() {
		this.entityManager.getTransaction().commit();
	}
	
	public void rollbackTransaction() {
		this.entityManager.getTransaction().rollback();
	}
	
}
