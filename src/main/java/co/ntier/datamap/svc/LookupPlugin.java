package co.ntier.datamap.svc;

import co.ntier.datamap.model.DataMapperContext;

public interface LookupPlugin<T> {
	
	T lookup(DataMapperContext ctx, Object... args);

}
