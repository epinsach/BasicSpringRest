package cat.desp.service.impl;

import cat.desp.common.persistence.ServicePreconditions;
import cat.desp.common.persistence.service.AbstractService;
import cat.desp.persistence.dao.IPrivilegeJpaDao;
import cat.desp.persistence.model.Privilege;
import cat.desp.service.IPrivilegeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.base.Preconditions;

@Service
@Transactional
public class PrivilegeServiceImpl extends AbstractService<Privilege> implements IPrivilegeService {

    @Autowired
    private IPrivilegeJpaDao dao;

    public PrivilegeServiceImpl() {
        super();
    }

    // API

    // find

    @Override
    public Privilege findOneByName(final String name) {
        return getDao().findOneByName(name);
    }

    // template

    @Override
    protected final void validateOnCreate(final Privilege entity) {
        Preconditions.checkArgument(entity.getName() != null, "The name of the Privilege is required");
        try {
            ServicePreconditions.checkEntityState(getDao().findOneByName(entity.getName()) == null, "Privilege with the same name already exists: " + entity.getName());
        } catch (final IllegalArgumentException iae) {
            throw iae;
        }
    }

    // Spring

    @Override
    protected final IPrivilegeJpaDao getDao() {
        return dao;
    }

    @Override
    protected JpaSpecificationExecutor<Privilege> getSpecificationExecutor() {
        return dao;
    }

}
