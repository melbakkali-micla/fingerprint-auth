package com.micla.plugins.fingerprintauth;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import androidx.annotation.NonNull;

import com.getcapacitor.JSObject;
import com.getcapacitor.Plugin;
import com.getcapacitor.PluginCall;
import com.getcapacitor.PluginMethod;
import com.getcapacitor.annotation.CapacitorPlugin;
import com.jesusm.kfingerprintmanager.KFingerprintManager;

@CapacitorPlugin(name = "FingerprintAuth")
public class FingerprintAuthPlugin extends Plugin {

    private static String TAG = "FingerprintAuthPlugin";
    private static String KEY_NAME = "capfingerprintauth";

    KFingerprintManager KfingerprintManager;
    KeyguardManager keyguardManager;
    FingerprintAuth fingerprintAuth;

    @Override
    public void load() {
        super.load();
        keyguardManager = (KeyguardManager) getActivity().getSystemService(Context.KEYGUARD_SERVICE);
        fingerprintAuth = new FingerprintAuth();
        KfingerprintManager = getKFingerprintManager();
    }

    @PluginMethod
    public void echo(PluginCall call) {
        Log.d(TAG, "authenticating ...");
    }

    @PluginMethod
    public void available(PluginCall call) {
        int error = fingerprintAuth.isTouchAvailable(getActivity(), keyguardManager);

        switch (error) {
            case FingerprintAuth.SCREEN_LOCK_NOT_FOUND -> {
                String message = "Device doesn't have a lock (Settings -> Lock)";

                Log.e(TAG, message);
                call.reject(message);
            }
            case FingerprintAuth.FINGERPRINT_NOT_SUPPORTED -> {
                String message = "The device doesn't support fingerprint authentication";

                Log.e(TAG, message);
                call.reject(message);
            }
            case FingerprintAuth.API_VERSION_UNSUPPORTED -> {
                String message = "Fingerprint API is not supported by your device";

                Log.e(TAG, message);
                call.reject(message);
            }
            case FingerprintAuth.NO_FINGERPRINT_ENROLLED -> {
                String message = "No fingerprints found on the system";

                Log.e(TAG, message);
                call.reject(message);
            }
            case FingerprintAuth.FINGERPRINT_AVAILABLE -> {
                Log.d(TAG, "Fingerprint is available");
                call.resolve();
            }
        }
    }

    @PluginMethod
    public void authenticate(PluginCall call) {
        JSObject res = new JSObject();
        int code = fingerprintAuth.isTouchAvailable(getActivity(), keyguardManager);

        if(code != FingerprintAuth.FINGERPRINT_AVAILABLE) {
            res.put("codeError", code);
            Log.e(TAG, res.toString());
            call.reject(res.toString());
            return;
        }

        fingerprintAuth.createKey(KEY_NAME);

        KFingerprintManager.AuthenticationCallback authenticationCallback =  new KFingerprintManager.AuthenticationCallback() {
            @Override
            public void onAuthenticationSuccess() {
                res.put("method", "fingerprint");
                res.put("authenticated", true);

                Log.d(TAG, res.toString());
                call.resolve(res);
            }

            @Override
            public void onSuccessWithManualPassword(String s) {
                String error = "Using password for this operation is not allowed";

                vibrate(300);
                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onFingerprintNotAvailable() {
                String error = "Screen doesn't have a lock or no fingerprint is enrolled";
                Log.e(TAG, error);

                createDialog("Authentication Error", "Please check the Following:\n\n" +
                    "1- Locked Screen, your device should have a set up screen lock.\n" +
                    "2- Enrolled Fingerprints, your device should have at least one enrolled fingerprint.");

                call.reject(error);
            }

            @Override
            public void onFingerprintNotRecognized() {
                String error = "Fingerprint is not recognized";
                vibrate(300);

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onCancelled() {
                String error = "Fingerprint dialog is cancelled";

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onAuthenticationFailedWithHelp(String s) {}
        };

        KfingerprintManager.authenticate(authenticationCallback, getActivity().getSupportFragmentManager());
    }

    @PluginMethod
    public void encrypt(PluginCall call) {
        int code = fingerprintAuth.isTouchAvailable(getActivity(), keyguardManager);
        JSObject res = new JSObject();

        if(code != FingerprintAuth.FINGERPRINT_AVAILABLE) {
            res.put("code", code);
            Log.e(TAG, res.toString());
            call.reject(res.toString());
            return;
        }

        fingerprintAuth.createKey(KEY_NAME);

        KFingerprintManager.EncryptionCallback encryptionAuthenticatedCallback = new KFingerprintManager.EncryptionCallback() {
            @Override
            public void onEncryptionSuccess(String encryptedMessage) {
                res.put("encryptedMessage", encryptedMessage);
                call.resolve(res);
            }

            @Override
            public void onEncryptionFailed() {
                String error = "Encryption failed";

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onFingerprintNotRecognized() {
                String error = "Fingerprint not recognized";

                Log.e(TAG, error);
                call.reject(error);

                vibrate(300);
            }

            @Override
            public void onFingerprintNotAvailable() {
                String error = "Screen doesn't have a lock or no fingerprint is enrolled";

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onAuthenticationFailedWithHelp(String h) {}

            @Override
            public void onCancelled() {
                String error = "Fingerprint dialog has been dismissed";

                Log.e(TAG, error);
                call.reject(error);
            }
        };

        String messageToEncrypt = call.getString("value", "HELLO WORLD");

        if(messageToEncrypt == null) {
            call.reject("Value to encrypt is not valid");
            return;
        }

        KfingerprintManager.encrypt(messageToEncrypt, encryptionAuthenticatedCallback, getActivity().getSupportFragmentManager());
    }

    @PluginMethod
    public void decrypt(PluginCall call) {
        int code = fingerprintAuth.isTouchAvailable(getActivity(), keyguardManager);
        JSObject res = new JSObject();

        if(code != FingerprintAuth.FINGERPRINT_AVAILABLE) {
            res.put("code", code);
            call.reject(res.toString());
            return;
        }

        KFingerprintManager.DecryptionCallback decryptionAuthenticatedCallback = new KFingerprintManager.DecryptionCallback() {
            @Override
            public void onDecryptionSuccess(@NonNull String s) {
                res.put("decryptedMessage", s);
                call.resolve(res);
            }

            @Override
            public void onDecryptionFailed() {
                String error = "Decryption has failed";

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onFingerprintNotRecognized() {
                String error = "Fingerprint is not recognized";

                Log.e(TAG, error);
                call.reject(error);

                vibrate(300);
            }

            @Override
            public void onAuthenticationFailedWithHelp(String help) {}

            @Override
            public void onFingerprintNotAvailable() {
                String error = "Screen doesn't have a lock or no fingerprint is enrolled";

                Log.e(TAG, error);
                call.reject(error);
            }

            @Override
            public void onCancelled() {
                String error = "Fingerprint dialog has been dismissed";

                Log.e(TAG, error);
                call.reject(error);
            }
        };

        String messageToDecrypt = call.getString("value");

        if(messageToDecrypt == null) {
            call.reject("Value to decrypt is not valid");
            return;
        }

        KfingerprintManager.decrypt(messageToDecrypt, decryptionAuthenticatedCallback, getActivity().getSupportFragmentManager());
    }

    public KFingerprintManager getKFingerprintManager() {
        if(KfingerprintManager == null) {
            return new KFingerprintManager(getContext(), KEY_NAME);
        }

        return KfingerprintManager;
    }

    public void createDialog(String title, String message) {
        Context context = getContext(); // or use 'this' if inside an Activity

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void vibrate(int duration) {
        Vibrator v = (Vibrator) getActivity().getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(VibrationEffect.createOneShot(500, VibrationEffect.DEFAULT_AMPLITUDE));
    }
}