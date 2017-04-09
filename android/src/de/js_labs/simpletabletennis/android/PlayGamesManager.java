package de.js_labs.simpletabletennis.android;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.leaderboard.LeaderboardScore;
import com.google.android.gms.games.leaderboard.LeaderboardVariant;
import com.google.android.gms.games.leaderboard.Leaderboards;
import com.google.example.games.basegameutils.GameHelper;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.tools.PlayGamesHandler;

/**
 * Created by Janik on 04.06.2016.
 */
public class PlayGamesManager implements PlayGamesHandler, GameHelper.GameHelperListener {
    public static final String HIGHSCORE_LB_ID = "CgkI4aCD9-sREAIQAA";
    private long highScore = -1;

    private GameHelper gameHelper;
    private Activity activity;
    private SimpleTableTennis game;

    public PlayGamesManager(Activity activity, SimpleTableTennis game){
        this.game = game;
        this.activity = activity;
        if (gameHelper == null) {
            gameHelper = new GameHelper(activity, GameHelper.CLIENT_GAMES);
            gameHelper.enableDebugLog(true);
        }
        gameHelper.setup(this);
    }

    @Override
    public boolean getSignedInGPGS() {
        return gameHelper.isSignedIn();
    }

    @Override
    public void loginGPGS() {
        try {
            activity.runOnUiThread(new Runnable(){
                public void run() {
                    gameHelper.beginUserInitiatedSignIn();
                }
            });
        } catch (final Exception ex) {
        }
    }

    @Override
    public void submitHighScoreGPGS(int score) {
        if(getSignedInGPGS())
            Games.Leaderboards.submitScore(gameHelper.getApiClient(), HIGHSCORE_LB_ID, score);
    }

    @Override
    public void unlockAchievementGPGS(String achievementId) {
        if(getSignedInGPGS())
            Games.Achievements.unlock(gameHelper.getApiClient(), achievementId);
    }

    @Override
    public void getLeaderboardGPGS() {
        if (getSignedInGPGS()) {
            activity.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(gameHelper.getApiClient(), HIGHSCORE_LB_ID), 100);
        }
        else if (!gameHelper.isConnecting()) {
            loginGPGS();
        }
    }

    @Override
    public void getAchievementsGPGS() {

    }

    public void loadGPGHighScore() {
        Games.Leaderboards.loadCurrentPlayerLeaderboardScore(gameHelper.getApiClient(), HIGHSCORE_LB_ID, LeaderboardVariant.TIME_SPAN_ALL_TIME, LeaderboardVariant.COLLECTION_PUBLIC).setResultCallback(new ResultCallback<Leaderboards.LoadPlayerScoreResult>() {

            @Override
            public void onResult(Leaderboards.LoadPlayerScoreResult arg0) {
                LeaderboardScore c = arg0.getScore();
                highScore = c.getRawScore();
            }

        });
    }

    public void onStart(){
        gameHelper.onStart(activity);
    }

    public void onStop(){
        gameHelper.onStop();
    }

    public void onActivityResult(int request, int response, Intent data) {
        gameHelper.onActivityResult(request, response, data);
        game.gameManager.activityInProgress = false;

        if(response == GamesActivityResultCodes.RESULT_RECONNECT_REQUIRED){
            gameHelper.disconnect();
        }
    }

    @Override
    public void onSignInFailed() {

    }

    @Override
    public void onSignInSucceeded() {

    }
}
