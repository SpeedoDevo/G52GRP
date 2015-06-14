package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import commons.Connection;
import commons.Node;

import rbf.RbfStrategy;

public class RbfStrategyTest {
	RbfStrategy rbf=new RbfStrategy();
	
	@Test
	/**Test calculateError
	 * Assert expected with actual at 0.01 accuracy
	 */
	public void testCalculateError() {
		float actual=rbf.calculateError(2, 1);
		float expected=-2.0f;
		Assert.assertEquals(expected, actual,0.01);
	}

	@Test
	/**Test sumFunction
	 * Create ArrayList<Connection> to parse through sumFunction
	 * Assert expected with actual at 0.01 accuracy
	 */
	public void testSumFunction() {
		ArrayList<Connection> inputConns=new ArrayList<Connection>();
		Node n=new Node(rbf);//create two nodes to create connection
		Node m=new Node(rbf);
		Connection conn=new Connection(n,m,10);//create connection between nodes to be added to inputConns
		n.setOutput(1);//set output for node n
		inputConns.add(conn);
		float actual=rbf.sumFunction(inputConns, 1.0f);
		float expected=11.0f;
		Assert.assertEquals(expected, actual,0.01);
	}

}
