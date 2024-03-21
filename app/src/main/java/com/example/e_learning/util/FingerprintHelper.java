package com.example.e_learning.util;

import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.CancellationSignal;
import android.widget.Toast;

public class FingerprintHelper extends FingerprintManager.AuthenticationCallback {

    private Context context;
    private FingerprintManager fingerprintManager;
    private CancellationSignal cancellationSignal;
    private FirebaseAuthenticationListener authenticationListener;

    public FingerprintHelper(Context context, FirebaseAuthenticationListener listener) {
        this.context = context;
        this.authenticationListener = listener;
        fingerprintManager = context.getSystemService(FingerprintManager.class);
    }

    public boolean isFingerprintAuthAvailable() {
        return fingerprintManager.isHardwareDetected() && fingerprintManager.hasEnrolledFingerprints();
    }

    public void startAuth() {
        cancellationSignal = new CancellationSignal();
        fingerprintManager.authenticate(null, cancellationSignal, 0, this, null);
    }

    public void stopAuth() {
        if (cancellationSignal != null && !cancellationSignal.isCanceled()) {
            cancellationSignal.cancel();
        }
    }

    @Override
    public void onAuthenticationSucceeded(FingerprintManager.AuthenticationResult result) {
        Toast.makeText(context, "Fingerprint authentication Succeeded", Toast.LENGTH_SHORT).show();
        authenticationListener.onAuthenticationSuccess();
    }

    @Override
    public void onAuthenticationFailed() {
        Toast.makeText(context, "Fingerprint authentication failed", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAuthenticationError(int errorCode, CharSequence errString) {
        Toast.makeText(context, "Fingerprint authentication error: " + errString, Toast.LENGTH_SHORT).show();
    }

    public interface FirebaseAuthenticationListener {
        void onAuthenticationSuccess();
    }
}
