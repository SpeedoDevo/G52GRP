
package test;

import java.util.ArrayList;

import org.junit.Assert;
import org.junit.Test;

import backprop.BackPropStrategy;
import commons.Connection;
import commons.Node;

public class BackPropStrategyTest {
	BackPropStrategy bp=new BackPropStrategy(0.1f);
	@Test
	/**Tests the sumFunction in backpropstrategy
	 * Creates an ArrayList<connection> and send through sumFunction
	 * Compares actual with expected when asserting
	 */
	public void testSumFunction() {
		ArrayList<Connection> inputConns=new ArrayList<Connection>();
		Node n=new Node(bp);//create two nodes to create connection
		Node m=new Node(bp);
		Connection conn=new Connection(n,m,10);//create connection between nodes to be added to inputConns
		n.setOutput(1);//set output for node n
		inputConns.add(conn);
		float actual=bp.sumFunction(inputConns, 0.25f);
		float expected=10.25f;//0.25 += 1*10
		Assert.assertEquals(expected, actual,0.1);
	}

	@Test
	/**Test activation function does the caluaction correctly
	 * Assert compare actual and expected
	 */
	public void testActivationFunction() {
		float actual=bp.activationFunction(1);
		float expected=0.7310586f;
		Assert.assertEquals(expected, actual,0.1);
	}

	@Test
	/**Test findNewBias
	 * Assert actual and expected
	 */
	public void testFindNewBias() {
		float actual=bp.findNewBias(2,3);
		float expected=3.2f;//3+(0.1*2)
		Assert.assertEquals(expected,actual,0.1);
		
	}

	@Test
	/**Test calculateError
	 * Assert actual and expected
	 */
	public void testCalculateError() {
		float actual=bp.calculateError(4.2f, 1.4f);
		float expected=-18.815998f;
		Assert.assertEquals(expected,actual,0.1);
		
	}
	
	@Test
	/**Test updateBias
	 * Assert actual with expected
	 */
	public void testUpdateBias(){
		Node n=new Node(bp);
		n.setOutput(1);
		n.setError(0.2f);
		n.setBias(2);
		bp.updateBias(n);
		float actual=n.getBias();
		float expected=2.02f;
		Assert.assertEquals(expected, actual,0.1);
		
	}
	
	@Test
	/**Test updateWeights on inputConns
	 * Assert expected and actual
	 */
	public void testUpdateWeights(){
		ArrayList<Connection> inputConns=new ArrayList<Connection>();
		Node n=new Node(bp);//create two nodes to create connection
		Node m=new Node(bp);
		Connection conn=new Connection(n,m,10);//create connection between nodes to be added to inputConns
		inputConns.add(conn);
		n.setOutput(1);//set output for node n
		m.setError(0.1f);
		bp.updateWeights(inputConns);
		float actual=inputConns.get(0).getWeight();
		float expected=10.01f;
		Assert.assertEquals(expected, actual,0.1);
	}

}
