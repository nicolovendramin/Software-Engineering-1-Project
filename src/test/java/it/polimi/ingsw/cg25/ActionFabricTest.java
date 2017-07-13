package it.polimi.ingsw.cg25;
import static org.junit.Assert.*;
import org.junit.Test;

import it.polimi.ingsw.cg25.actions.Action;
import it.polimi.ingsw.cg25.actions.ActionFabric;
import it.polimi.ingsw.cg25.actions.BuildWithKingAction;
import it.polimi.ingsw.cg25.actions.BuyingAction;

public class ActionFabricTest extends ActionTest{

	private ActionFabric fabric = new ActionFabric(model);

	@Test (expected = IllegalArgumentException.class)
	public void testActionFabricProduceNotExistingAction() {
		fabric.produce("test");
	}
	
	@Test (expected = IllegalArgumentException.class)
	public void testActionFabricProduceNullParameter() {
		fabric.produce(null);
	}

	@Test
	public void testActionFabricRight() {
		Action action = new BuildWithKingAction(model);
		Action action1 = new BuyingAction(model.getMarket());
		ActionFabric fabric1 = new ActionFabric(model.getMarket());
		Action reply = fabric.produce(action.toString());
		Action reply1 = fabric1.produce(action1.toString());
		assertEquals(action.toString(),reply.toString());
		assertEquals(action1.toString(),reply1.toString());
		
	}

	@Test (expected = IllegalArgumentException.class)
	public void testActionFabriConstructor() {
		new ActionFabric(null);
	}
}
