package it.polimi.ingsw.cg25;

import static org.junit.Assert.*;

import java.util.ArrayList;

import org.junit.Before;
import org.junit.Test;

import it.polimi.ingsw.cg25.observer.Observable;
import it.polimi.ingsw.cg25.observer.Observer;

public class ObservableTest {

	private boolean flagUpdated = false;
	private int numOfUpdateds = 0;
	private boolean flagUpdatedWithContent = false;
	private ArrayList<String> contentUpdated ;
	private smallObservable smallObservable;
	
	private class smallObserver implements Observer<String>{

		@Override
		public void update() {
			flagUpdated = true;
			numOfUpdateds++;
		}

		@Override
		public void update(String change) {
			flagUpdatedWithContent = true;
			contentUpdated.add(change.concat(""));
		}
		
	}
	
	private class smallObservable extends Observable<String>{
		
	}
	
	@Before
	public void setUp(){
		this.flagUpdated = false;
		this.flagUpdatedWithContent = false;
		this.numOfUpdateds = 0;
		this.contentUpdated = new ArrayList<>();
		this.smallObservable = new smallObservable();
	}
	
	@Test 
	public void attachAndRemoveObserverTest(){
		smallObservable.notifyObservers();
		assertTrue(this.numOfUpdateds==0);
		smallObservable.attachObserver(new smallObserver());
		smallObservable.notifyObservers();
		assertTrue(this.numOfUpdateds==1);
		smallObservable.removeAllObservers();
		smallObservable.notifyObservers();
		assertTrue(this.numOfUpdateds==1);
		smallObserver observer = new smallObserver();
		smallObservable.attachObserver(new smallObserver());
		smallObservable.attachObserver(observer);
		smallObservable.notifyObservers();
		assertTrue(this.numOfUpdateds==3);
		smallObservable.removeObserver(observer);
		smallObservable.notifyObservers();
		assertTrue(this.numOfUpdateds==4);
	}

	@Test
	public void notifyChangeTest() {
		smallObservable = new smallObservable();
		smallObservable.attachObserver(new smallObserver());
		smallObserver observer = new smallObserver();
		smallObservable.attachObserver(observer);
		smallObservable.attachObserver(new smallObserver());
		smallObservable.notifyObservers("TestString");
		for(String s:contentUpdated){
			s.equals("TestString");
		}
		assertTrue(flagUpdatedWithContent);
		assertFalse(flagUpdated);
		assertTrue(contentUpdated.size()==3);
	}

}
