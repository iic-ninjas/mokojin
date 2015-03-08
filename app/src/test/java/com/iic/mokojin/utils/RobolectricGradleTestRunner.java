package com.iic.mokojin.utils;

/* Taken from https://gist.github.com/tomykaira/0c11df447cc1728b6e88 */

import org.junit.runners.model.InitializationError;
import org.robolectric.AndroidManifest;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;
import org.robolectric.res.Fs;

/**
 * Custom robolectric test runner.
 *
 * This runner replaces {@code ResourceLoader} for target package's name space,
 * in order to resolve resources from other libraries (such as appcompat).
 * They are neither shipped with Robolectric, nor linked in SUT compiled resources.
 *
 * One way is to copy the resource into our source, but it is bad from maintenance and
 * license points of view. Because the resource contents are not related to test results
 * in case of drawable, we prefer to replace them with some indifferent image.
 *
 * {@see https://github.com/robolectric/robolectric/issues/1375}
 */
public class RobolectricGradleTestRunner extends RobolectricTestRunner {

    private static final int MAX_SDK_SUPPORTED_BY_ROBOLECTRIC = 18;

    public RobolectricGradleTestRunner(Class<?> testClass) throws InitializationError {
        super(testClass);
    }

    @Override
    protected AndroidManifest getAppManifest(Config config) {
        final String manifestProperty = "./app/src/main/AndroidManifest.xml";
        final String resProperty = "./app/src/main/res";
        return new AndroidManifest(Fs.fileFromPath(manifestProperty), Fs.fileFromPath(resProperty)) {
            @Override
            public int getTargetSdkVersion() {
                return MAX_SDK_SUPPORTED_BY_ROBOLECTRIC;
            }
        };
    }
    
}