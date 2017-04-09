package de.js_labs.simpletabletennis.tools;

public interface PlayGamesHandler {
    public boolean getSignedInGPGS();
    public void loginGPGS();
    public void submitHighScoreGPGS(int score);
    public void unlockAchievementGPGS(String achievementId);
    public void getLeaderboardGPGS();
    public void getAchievementsGPGS();
    public void loadGPGHighScore();
}
