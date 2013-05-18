package co.ntier.datamap.svc;

import java.util.Arrays;

import lombok.extern.slf4j.Slf4j;

import co.ntier.datamap.model.DataMapperContext;
import co.ntier.datamap.model.Order;

/**
 * A dummy implementation of a {@link LookupPlugin}.
 */
@Slf4j
public class OrderLookupPlugin implements LookupPlugin<Order>{

	private Order dummyOrder = new Order(1L, 1L, "Some Customer");
	
	public Order lookup(DataMapperContext ctx, Object... args) {
		log.info("Looking up order based on args: {}", Arrays.toString(args));
		
		// gotta have some security!
		if( !ctx.getUser().getId().equals(dummyOrder.getId()) ){
			throw new IllegalStateException("Current user doesn't have access...");
		}
		
		
		return dummyOrder;
	}

}
