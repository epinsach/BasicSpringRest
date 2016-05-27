package cat.desp.common.persistence.service;

import cat.desp.common.interfaces.IByNameApi;
import cat.desp.common.persistence.model.INameableEntity;

public interface IService<T extends INameableEntity> extends IRawService<T>, IByNameApi<T> {

    //

}
