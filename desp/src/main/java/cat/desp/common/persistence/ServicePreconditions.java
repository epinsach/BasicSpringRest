package cat.desp.common.persistence;

import cat.desp.common.persistence.exception.MyEntityNotFoundException;
import cat.desp.common.web.exception.MyBadRequestException;
import cat.desp.common.web.exception.MyConflictException;

public final class ServicePreconditions {

    private ServicePreconditions() {
        throw new AssertionError();
    }

    // API

    /**
     * Ensures that the entity reference passed as a parameter to the calling method is not null.
     *
     * @param entity
     *            an object reference
     * @return the non-null reference that was validated
     * @throws MyEntityNotFoundException
     *             if {@code entity} is null
     */
    public static <T> T checkEntityExists(final T entity, final String message) {
        if (entity == null) {
            throw new MyEntityNotFoundException(message);
        }
        return entity;
    }

    /**
     * @throws MyEntityNotFoundException
     */
    public static void checkEntityExists(final boolean entityExists, final String message) {
        if (!entityExists) {
            throw new MyEntityNotFoundException(message);
        }
    }

    /**
     * @throws MyBadRequestException
     */
    public static void checkOKArgument(final boolean okArgument, final String message) {
        if (!okArgument) {
            throw new MyBadRequestException(message);
        }
    }

    /**
     * @throws MyConflictException
     */
    public static void checkEntityState(final boolean okArgument, final String message) {
        if (!okArgument) {
            throw new MyConflictException(message);
        }
    }

}
