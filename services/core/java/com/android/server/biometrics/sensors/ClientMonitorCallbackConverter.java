/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.android.server.biometrics.sensors;

import android.annotation.NonNull;
import android.hardware.biometrics.BiometricAuthenticator;
import android.hardware.biometrics.IBiometricSensorReceiver;
import android.hardware.face.Face;
import android.hardware.face.FaceAuthenticationFrame;
import android.hardware.face.FaceEnrollFrame;
import android.hardware.face.IFaceServiceReceiver;
import android.hardware.fingerprint.Fingerprint;
import android.hardware.fingerprint.IFingerprintServiceReceiver;
import android.os.RemoteException;

/**
 * Class that allows ClientMonitor send results without caring about who the client is. These are
 * currently one of the below:
 *  1) {@link com.android.server.biometrics.BiometricService}
 *  2) {@link android.hardware.fingerprint.FingerprintManager}
 *  3) {@link android.hardware.face.FaceManager}
 *
 * This is slightly ugly due to:
 *   1) aidl not having native inheritance
 *   2) FaceManager/FingerprintManager supporting a venn diagram of functionality
 * It may be possible at some point in the future to combine I<Sensor>ServiceReceivers to share
 * a common interface.
 */
public class ClientMonitorCallbackConverter {
    private IBiometricSensorReceiver mSensorReceiver; // BiometricService
    private IFaceServiceReceiver mFaceServiceReceiver; // FaceManager
    private IFingerprintServiceReceiver mFingerprintServiceReceiver; // FingerprintManager

    public ClientMonitorCallbackConverter(IBiometricSensorReceiver sensorReceiver) {
        mSensorReceiver = sensorReceiver;
    }

    public ClientMonitorCallbackConverter(IFaceServiceReceiver faceServiceReceiver) {
        mFaceServiceReceiver = faceServiceReceiver;
    }

    public ClientMonitorCallbackConverter(IFingerprintServiceReceiver fingerprintServiceReceiver) {
        mFingerprintServiceReceiver = fingerprintServiceReceiver;
    }

    // The following apply to all clients

    void onAcquired(int sensorId, int acquiredInfo, int vendorCode) throws RemoteException {
        if (mSensorReceiver != null) {
            mSensorReceiver.onAcquired(sensorId, acquiredInfo, vendorCode);
        } else if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onAcquired(acquiredInfo, vendorCode);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onAcquired(acquiredInfo, vendorCode);
        }
    }

    void onAuthenticationSucceeded(int sensorId, BiometricAuthenticator.Identifier identifier,
            byte[] token, int userId, boolean isStrongBiometric) throws RemoteException {
        if (mSensorReceiver != null) {
            mSensorReceiver.onAuthenticationSucceeded(sensorId, token);
        } else if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onAuthenticationSucceeded((Face) identifier, userId,
                    isStrongBiometric);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onAuthenticationSucceeded((Fingerprint) identifier, userId,
                    isStrongBiometric);
        }
    }

    void onAuthenticationFailed(int sensorId) throws RemoteException {
        if (mSensorReceiver != null) {
            mSensorReceiver.onAuthenticationFailed(sensorId);
        } else if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onAuthenticationFailed();
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onAuthenticationFailed();
        }
    }

    public void onError(int sensorId, int cookie, int error, int vendorCode)
            throws RemoteException {
        if (mSensorReceiver != null) {
            mSensorReceiver.onError(sensorId, cookie, error, vendorCode);
        } else if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onError(error, vendorCode);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onError(error, vendorCode);
        }
    }

    // The following only apply to IFingerprintServiceReceiver and IFaceServiceReceiver

    public void onDetected(int sensorId, int userId, boolean isStrongBiometric)
            throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onFaceDetected(sensorId, userId, isStrongBiometric);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onFingerprintDetected(sensorId, userId, isStrongBiometric);
        }
    }

    void onEnrollResult(BiometricAuthenticator.Identifier identifier, int remaining)
            throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onEnrollResult((Face) identifier, remaining);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onEnrollResult((Fingerprint) identifier, remaining);
        }
    }

    void onRemoved(BiometricAuthenticator.Identifier identifier, int remaining)
            throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onRemoved((Face) identifier, remaining);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onRemoved((Fingerprint) identifier, remaining);
        }
    }

    public void onChallengeGenerated(int sensorId, long challenge) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onChallengeGenerated(sensorId, challenge);
        } else if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onChallengeGenerated(sensorId, challenge);
        }
    }

    public void onFeatureSet(boolean success, int feature) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onFeatureSet(success, feature);
        }
    }

    public void onFeatureGet(boolean success, int feature, boolean value) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onFeatureGet(success, feature, value);
        }
    }

    public void onChallengeInterrupted(int sensorId) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onChallengeInterrupted(sensorId);
        }
    }

    public void onChallengeInterruptFinished(int sensorId) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onChallengeInterruptFinished(sensorId);
        }
    }

    // Fingerprint-specific callbacks for FingerprintManager only

    public void onUdfpsPointerDown(int sensorId, int cookie) throws RemoteException {
        if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onUdfpsPointerDown(sensorId);
        }
    }

    public void onUdfpsPointerUp(int sensorId, int cookie) throws RemoteException {
        if (mFingerprintServiceReceiver != null) {
            mFingerprintServiceReceiver.onUdfpsPointerUp(sensorId);
        }
    }

    // Face-specific callbacks for FaceManager only

    /**
     * Called each time a new frame is received during face authentication.
     *
     * @param frame Information about the current frame.
     *
     * @throws RemoteException If the binder call to {@link IFaceServiceReceiver} fails.
     */
    public void onAuthenticationFrame(@NonNull FaceAuthenticationFrame frame)
            throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onAuthenticationFrame(frame);
        }
    }

    /**
     * Called each time a new frame is received during face enrollment.
     *
     * @param frame Information about the current frame.
     *
     * @throws RemoteException If the binder call to {@link IFaceServiceReceiver} fails.
     */
    public void onEnrollmentFrame(@NonNull FaceEnrollFrame frame) throws RemoteException {
        if (mFaceServiceReceiver != null) {
            mFaceServiceReceiver.onEnrollmentFrame(frame);
        }
    }
}
