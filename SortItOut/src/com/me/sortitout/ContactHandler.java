package com.me.sortitout;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener{
	

	GameObject game;
	private int count = 0;
	public void init(GameObject gameObject) {
		game = gameObject;		
	}
	
	@Override
	public void beginContact(Contact contact) {
		int cnt=0;
		Body 	
			aBody = game.getActiveItem(),
			A = contact.getFixtureA().getBody(),
			B = contact.getFixtureB().getBody();	
		if ((A == aBody || B == aBody)) {			
			float velosity=Math.max(A.getLinearVelocity().len()/Config.MAX_SPEED, B.getLinearVelocity().len()/Config.MAX_SPEED);
			if (game.itemPositionsChanged()) {		
				if (A.getUserData() == null || B.getUserData() == null) {
					Config.getInst().edgeSound.play(0.5f, 1.5f, 0);
					aBody.setLinearVelocity(aBody.getLinearVelocity().div(4f));
				} else {
					Config.getInst().blockSound.play(velosity, Math.max(1, velosity*1.2f), 0);
				}
				
				Item item;
				ArrayList<Item> cItemList;
				cItemList = game.getItemList();
				for (int i=0;i<15;i++) {
					item = cItemList.get(i); 
					if (item.position == i) {
						cnt++;
					} 
				}
				if (cnt>count) {
					if (cnt == 15) {
						game.gameOver();
					}else {
						Config.getInst().newPosSound.play();
					}
				}
				count=cnt;
			}
		}	
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		/*Body 	
		A = contact.getFixtureA().getBody(),
		B = contact.getFixtureB().getBody();
		A.setLinearVelocity(A.getLinearVelocity().div(2f));
		B.setLinearVelocity(B.getLinearVelocity().div(2f));
		*/
	}

	@Override
	public void preSolve(Contact contact, Manifold oldManifold) {
		// TODO Auto-generated method stub
	}

	@Override
	public void postSolve(Contact contact, ContactImpulse impulse) {
		// TODO Auto-generated method stub
	}

}