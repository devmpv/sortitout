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
					Config.getInst().playSnd(Config.SND_EDGE, velosity*0.5f, 1.5f);
					//aBody.setLinearVelocity(aBody.getLinearVelocity().scl(4f));
				} else {
					Config.getInst().playSnd(Config.SND_BLOCK, velosity, Math.max(1, velosity*1.2f));
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
						Config.getInst().playSnd(Config.SND_NEWPOS);
					}
				}
				count=cnt;
			}
		}	
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
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