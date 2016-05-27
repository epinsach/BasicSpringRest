package cat.desp.common.persistence.service;

import cat.desp.common.interfaces.IOperations;
import cat.desp.common.persistence.model.IEntity;
import org.springframework.data.domain.Page;

public interface IRawService<T extends IEntity> extends IOperations<T> {

    Page<T> findAllPaginatedAndSortedRaw(final int page, final int size, final String sortBy, final String sortOrder);

}
