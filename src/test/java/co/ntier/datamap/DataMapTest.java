package co.ntier.datamap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;

import co.ntier.datamap.model.Order;
import co.ntier.datamap.model.User;
import co.ntier.datamap.svc.DataMapper;
import co.ntier.datamap.svc.OrderLookupPlugin;

public class DataMapTest {

	// immutable, shared across all tests
	private static Map<String, Object> data;
	private static User user;
	
	private static ExpressionParser parser;
	
	// created for each test
	private DataMapper datamapper;
	
	@BeforeClass
	@SuppressWarnings("serial")
	public static void beforeClass(){
		data = new HashMap<String, Object>(){{
			put("name", "David Welch");
			put("age", 26);
		}};
		user = new User(1L, "Jimbo Jones");
		parser = new SpelExpressionParser();
	}
	
	@Before
	public void beforeEachTest(){
		datamapper = new DataMapper(user, data).addPlugin("ORDERS", new OrderLookupPlugin());
	}
	
	@Test
	public void dataLookupTest(){
		Expression exp = parser.parseExpression("data['age']");
		EvaluationContext ctx = new StandardEvaluationContext(datamapper); 
		Object value = exp.getValue(ctx);
		assertEquals(value, 26);
	}
	
	@Test
	public void dummyMethodTest() {
		Expression exp = parser.parseExpression("customIsOddMethod(data['age'] + 1)");
		EvaluationContext ctx = new StandardEvaluationContext(datamapper); 
		Object value = exp.getValue(ctx);
		assertEquals(value, Boolean.TRUE);
	}
	
	@Test
	public void systemRefTest(){
		Object result = datamapper.systemRef("ORDERS", "123", "Hello world");
		assertNotNull(result);
	}
	
	@Test
	public void systemRefEvalTest(){
		Expression exp = parser.parseExpression("systemRef('ORDERS', '123', 'ABC')");
		EvaluationContext ctx = new StandardEvaluationContext(datamapper);
		Object result = exp.getValue(ctx);
		assertNotNull(result);
		assertEquals(Boolean.TRUE, result instanceof Order);
	}
	
	@Test
	public void systemRefTraversalEval(){
		Expression exp = parser.parseExpression("systemRef('ORDERS', 1, 'Some random parameter').customer");
		EvaluationContext ctx = new StandardEvaluationContext(datamapper);
		assertEquals("Some Customer", exp.getValue(ctx));
	}

}
