package nickita.gq.yula;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import nickita.gq.yula.utils.Storage;

import static org.junit.Assert.*;

/**
 * Instrumentation test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class StorageTest {
    @Test
    public void useAppContext() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        assertEquals("nickita.gq.yula", appContext.getPackageName());
    }

    @Test
    public void writeStringToPreferencesTest() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        Storage.writeStringToPreferences(appContext, "TestPref", "TEST_CONTENT");
    }

    @Test
    public void readStringFromPreferences() throws Exception {
        Context appContext = InstrumentationRegistry.getTargetContext();
        String result = Storage.readStringFromPreferences(appContext, "TestPref");
        assertEquals(result, "TEST_CONTENT");
    }


}
