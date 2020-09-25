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

package com.android.systemui.pip.phone;

import static android.content.pm.PackageManager.FEATURE_PICTURE_IN_PICTURE;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.pm.PackageManager;
import android.os.RemoteException;
import android.test.suitebuilder.annotation.SmallTest;
import android.testing.AndroidTestingRunner;
import android.testing.TestableContext;
import android.testing.TestableLooper;

import com.android.systemui.SysuiTestCase;
import com.android.systemui.pip.PipBoundsHandler;
import com.android.systemui.pip.PipTaskOrganizer;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.wmshell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayController;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

/**
 * Unit tests for {@link PipController}
 */
@SmallTest
@RunWith(AndroidTestingRunner.class)
@TestableLooper.RunWithLooper
public class PipControllerTest extends SysuiTestCase {
    private PipController mPipController;
    private TestableContext mSpiedContext;

    @Mock private ActivityManagerWrapper mMockActivityManagerWrapper;
    @Mock private ConfigurationController mMockConfigurationController;
    @Mock private DisplayController mMockdDisplayController;
    @Mock private PackageManager mPackageManager;
    @Mock private PipMenuActivityController mMockPipMenuActivityController;
    @Mock private PipAppOpsListener mMockPipAppOpsListener;
    @Mock private PipBoundsHandler mMockPipBoundsHandler;
    @Mock private PipMediaController mMockPipMediaController;
    @Mock private PipTaskOrganizer mMockPipTaskOrganizer;
    @Mock private PipTouchHandler mMockPipTouchHandler;
    @Mock private WindowManagerShellWrapper mMockWindowManagerShellWrapper;

    @Before
    public void setUp() throws RemoteException {
        MockitoAnnotations.initMocks(this);

        mSpiedContext = spy(mContext);

        when(mPackageManager.hasSystemFeature(FEATURE_PICTURE_IN_PICTURE)).thenReturn(false);
        when(mSpiedContext.getPackageManager()).thenReturn(mPackageManager);

        mPipController = new PipController(mSpiedContext, mMockdDisplayController,
                mMockPipAppOpsListener,
                mMockPipBoundsHandler, mMockPipMediaController, mMockPipMenuActivityController,
                mMockPipTaskOrganizer, mMockPipTouchHandler, mMockWindowManagerShellWrapper);
    }

    @Test
    public void testNonPipDevice_shouldNotRegisterTaskStackListener() {
        verify(mMockActivityManagerWrapper, never()).registerTaskStackListener(any());
    }

    @Test
    public void testNonPipDevice_shouldNotRegisterPipTransitionCallback() {
        verify(mMockPipTaskOrganizer, never()).registerPipTransitionCallback(any());
    }

    @Test
    public void testNonPipDevice_shouldNotAddDisplayChangingController() {
        verify(mMockdDisplayController, never()).addDisplayChangingController(any());
    }

    @Test
    public void testNonPipDevice_shouldNotAddDisplayWindowListener() {
        verify(mMockdDisplayController, never()).addDisplayWindowListener(any());
    }

    @Test
    public void testNonPipDevice_shouldNotAddCallback() {
        verify(mMockConfigurationController, never()).addCallback(any());
    }
}
