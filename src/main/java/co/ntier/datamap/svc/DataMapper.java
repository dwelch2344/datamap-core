package co.ntier.datamap.svc;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import co.ntier.datamap.model.DataMapperContext;
import co.ntier.datamap.model.User;

import com.google.common.collect.Maps;

/**
 * The core of this little demo. Supports the following:
 *
 * - "data['age']"  // gets the "age" column
 * - "systemRef('ORDERS', 21)
 *
 */
@Data
public class DataMapper {
	
	private User currentUser;
	private Map<String, Object> data;
	
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private DataMapperContext context;
	
	@Getter(AccessLevel.NONE) @Setter(AccessLevel.NONE)
	private Map<String, LookupPlugin<?>> plugins = Maps.newHashMap();
	

	public DataMapper(User currentUser, Map<String, Object> data) {
		super();
		this.currentUser = currentUser;
		this.data = data;
		this.context = new DataMapperContext(currentUser);
	}
	
	// TODO use the builder pattern to prevent evaluation adding a plugin 
	public DataMapper addPlugin(String key, LookupPlugin<?> plugin){
		plugins.put(key, plugin);
		return this;
	}
	
	/**
	 * Defers to a {@link LookupPlugin} to lookup an object. 
	 */
	public <T> T systemRef(String key, Object... args){
		if( !plugins.containsKey(key) ){
			throw new IllegalArgumentException("Unknown plugin: " + key);
		}
		
		LookupPlugin<?> svc = plugins.get(key);
		Object result = svc.lookup(context, args);
		
		if( result == null ){
			return null;
		}else {
			@SuppressWarnings("unchecked")
			T t = (T) result;
			return t;
		}
	}
	
	/**
	 * Just a simple demo method
	 */
	public boolean customIsOddMethod(Number number){
		return number.doubleValue() % 2 != 0;
	}
}
