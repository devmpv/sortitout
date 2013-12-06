package com.me.sortitout;

import java.util.ArrayList;

import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;

public class ContactHandler implements ContactListener{
	

	GameObject gameObject;
	private int count = 0;
	public void Init(GameObject gObject) {
		// TODO Auto-generated method stub
		gameObject = gObject;
		
	}
	
	@Override
	public void beginContact(Contact contact) {
		// TODO Auto-generated method stub
	}

	@Override
	public void endContact(Contact contact) {
		// TODO Auto-generated method stub
		int cnt=0;
		if (gameObject.ItemPositionsChanged()) {
			float velosity=gameObject.getActiveItem().getLinearVelocity().len();
			gameObject.blockSound.play(velosity/100, Math.max(velosity/50,1f), 0);
			Item item;
			ArrayList<Item> cItemList;
			cItemList = gameObject.getItemList();
			for (int i=0;i<15;i++) {
				item = cItemList.get(i); 
				if (item.position == i) {
					cnt++;
				} 
			}
			if (cnt>count) {
				if (cnt == 15) {
					gameObject.GameOver();
				}else {
					gameObject.newPosSound.play();
				}
			}
			count=cnt;
		}
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