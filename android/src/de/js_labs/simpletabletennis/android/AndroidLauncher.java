package de.js_labs.simpletabletennis.android;

import android.content.Intent;
import android.os.Bundle;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;

import de.js_labs.simpletabletennis.SimpleTableTennis;

public class AndroidLauncher extends AndroidApplication {

	private PlayGamesManager gamesManager;
	private BillingManager billingManager;

	public SimpleTableTennis game;

	@Override
	protected void onCreate (Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
		config.useImmersiveMode = true;
		game = new SimpleTableTennis();
		initialize(game, config);

		billingManager = new BillingManager(this, this, game);
		gamesManager = new PlayGamesManager(this, game);

		game.setHandler(billingManager, gamesManager);
	}

	@Override
	public void onStart(){
		super.onStart();
		gamesManager.onStart();
	}

	@Override
	public void onStop(){
		super.onStop();
		gamesManager.onStop();
	}

	@Override
	public void onActivityResult(int request, int response, Intent data) {
		super.onActivityResult(request, response, data);

		gamesManager.onActivityResult(request, response, data);
		billingManager.onActivityResult(request, response, data);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		billingManager.onDestroy();
	}
}
