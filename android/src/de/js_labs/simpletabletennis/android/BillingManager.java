package de.js_labs.simpletabletennis.android;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import de.js_labs.simpletabletennis.SimpleTableTennis;
import de.js_labs.simpletabletennis.android.util.IabHelper;
import de.js_labs.simpletabletennis.android.util.IabResult;
import de.js_labs.simpletabletennis.android.util.Purchase;
import de.js_labs.simpletabletennis.tools.BillingHandler;

/**
 * Created by Janik on 04.06.2016.
 */
public class BillingManager implements BillingHandler, IabHelper.OnIabPurchaseFinishedListener {
    public static final String BASE_64_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjuPdLBULN0wi9e/ayOz35elocRAKioFP5+5KD2W64dfLrnwdLzN+EQ9D7WndC5DE6d648DJwrlANZiAhePqRkSU9db25BgUyvIo4+s+vxSzLbRisqnkyeFeP8alIX2yDJJICbwtnZHmoNg21DtsslcqO1PJfb4pMYBvIdWFLPHxGBCFLzFTqkTLNRJRojDPrdxStg410QRmNQQZA7Z9A+30etShq4XhvMmmkovUw4/xQ3YtXq4lPJHG86utMRgrn5iqnUjUjiDqp9oRMhzPv1gxPE9yMb2svxci4Gldfo0jCeOt/cbhuNNdS9HYpiBzrKNNDQqdSkpgu4p9//Y1x6QIDAQAB";
    static final int RC_REQUEST = 10001;
    public static final String PAYLOAD = "jsLabsPayload";

    public static final String REMOVEADS_SKU = "de.js_labs.simpletabletennis.removeads";
    public static final String PREMIUM_SKU = "de.js_labs.simpletabletennis.premium";
    public static final String PREMIUM_PLUS_SKU = "de.js_labs.simpletabletennis.removeads_plus";
    public static final String PREMIUM_UPGRADE_SKU = "de.js_labs.simpletabletennis.premium_upgrade";

    private IabHelper billingHelper;
    private SimpleTableTennis game;
    private Context context;
    private Activity activity;

    private boolean isBillingAvailable;
    private String billingError;

    public BillingManager(Context context, Activity activity, SimpleTableTennis game){
        this.game = game;
        this.context = context;
        this.activity = activity;

        billingHelper = new IabHelper(context, BASE_64_KEY);
        billingHelper.startSetup(new IabHelper.OnIabSetupFinishedListener() {
            public void onIabSetupFinished(IabResult result) {
                if (!result.isSuccess()) {
                    isBillingAvailable = false;
                    billingError = result.getMessage();
                    Log.d("JSL:IAP", "Error: " + billingError);
                }else {
                    isBillingAvailable = true;
                    Log.d("JSL:IAP", "Setup Success");
                }
            }
        });
    }

    @Override
    public boolean launchPurchaseFlow(String SKU) {
        if(isBillingAvailable){
            billingHelper.launchPurchaseFlow(activity, SKU, RC_REQUEST,
                    this, PAYLOAD);
            return true;
        }else {
            game.gameManager.billingErrorMessage = billingError;
            return false;
        }
    }

    public void onActivityResult(int request, int response, Intent data) {
        if (billingHelper != null) {
            billingHelper.handleActivityResult(request, response, data);
        }
    }

    public void onDestroy() {
        if (billingHelper != null)
            billingHelper.dispose();
        billingHelper = null;
    }

    @Override
    public void onIabPurchaseFinished(IabResult result, Purchase purchase) {
        if (billingHelper == null) return;

        Log.d("JSL:IAP", "Purchase finished: " + result + ", purchase: " + purchase);
        game.gameManager.activityInProgress = false;

        if ( purchase == null) return;

        if (result.isFailure()) {

            return;
        }

        Log.d("JSL:IAP", "Purchase successful.");

        if (purchase.getSku().equals(REMOVEADS_SKU)) {
            Log.d("JSL:IAP", "Remove Ads Success!");
        }
    }
}
