package test;

import org.junit.*;


import commons.Connection;
import commons.Node;

public class ConnectionTest {

	@Test
	/**Test Connection(inputNode,OutputNode)
	 * Check whether weight is null(Check helperMethod.random works)
	 */
	public void testConnectionConstructor() {
		Node n=new Node();
		Node m=new Node();
		Connection conn=new Connection(n,m);
		Assert.assertNotNull(conn.getWeight());
		Assert.assertEquals(n,conn.getInputNode());//Test inputNode is set correctly
		Assert.assertEquals(m,conn.getOutputNode());//Test outputNode is set correctly
	}

}
