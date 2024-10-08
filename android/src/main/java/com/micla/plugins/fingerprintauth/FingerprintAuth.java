package com.micla.plugins.fingerprintauth;

import android.app.KeyguardManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.security.keystore.KeyGenParameterSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.jesusm.kfingerprintmanager.authentication.presenter.FingerprintAuthenticationDialogPresenter;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.CertificateException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

public class FingerprintAuth {

    public static final int FINGERPRINT_NOT_SUPPORTED = -1;
    public static final int NO_FINGERPRINT_ENROLLED = -2;
    public static final int API_VERSION_UNSUPPORTED = -3;
    public static final int SCREEN_LOCK_NOT_FOUND = -4;
    public static final int FINGERPRINT_AVAILABLE = 0;

    private static String TAG = "FingerprintAuthPlugin";

    public void createKey(String KEY_NAME) {
        try {
            KeyStore keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore.load(null);

            if (keyStore.containsAlias(KEY_NAME)) {
                Log.d(TAG, "Key already exists: " + KEY_NAME);
                return;
            }

            KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

            keyGenerator.init(
                    new KeyGenParameterSpec.Builder(KEY_NAME, KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
                            .setBlockModes(KeyProperties.BLOCK_MODE_CBC)
                            .setUserAuthenticationRequired(true)
                            .setUserAuthenticationValidityDurationSeconds(-1)
                            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_PKCS7)
                            .build());

            keyGenerator.generateKey();
        } catch (IOException | CertificateException | NoSuchAlgorithmException | NoSuchProviderException | KeyStoreException | InvalidAlgorithmParameterException e) {
            e.printStackTrace();
        }
    }

    public void storeEncryptedData(Context context, String encryptedMessage, String PREFS_NAME) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("fp_plugin__secret_value", encryptedMessage);
            editor.apply();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String loadSecret(Context context, String PREFS_NAME) {
        try {
            SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String encryptedMessageStr = sharedPreferences.getString("fp_plugin__secret_value", null);

            if (encryptedMessageStr == null) {
                Log.e(TAG, "No data found for decryption");
                return null;
            }

            return encryptedMessageStr;

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public int isTouchAvailable(AppCompatActivity activity, KeyguardManager keyguardManager) {
        if (Build.VERSION.SDK_INT >= 23) {
            FingerprintManager manager = (FingerprintManager) activity.getSystemService(Context.FINGERPRINT_SERVICE);

            if (keyguardManager == null || !keyguardManager.isKeyguardSecure()) {
                return SCREEN_LOCK_NOT_FOUND;
            } else {
                if (!manager.isHardwareDetected()) {
                    return FINGERPRINT_NOT_SUPPORTED;
                } else if (!manager.hasEnrolledFingerprints()) {
                    return NO_FINGERPRINT_ENROLLED;
                } else {
                    return FINGERPRINT_AVAILABLE;
                }
            }
        } else {
            return API_VERSION_UNSUPPORTED;
        }
    }
}