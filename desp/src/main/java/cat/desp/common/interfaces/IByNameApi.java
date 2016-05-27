package cat.desp.common.interfaces;

import cat.desp.common.interfaces.IWithName;

public interface IByNameApi <T extends IWithName>{
	 T findOneByName(final String name);
}
