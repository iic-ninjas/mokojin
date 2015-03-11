package com.iic.mokojin.cloud;

import com.parse.ParseCloud;

import org.junit.Before;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.HashMap;
import java.util.Map;

import bolts.Task;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyMapOf;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Created by giladgo on 3/8/15.
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ParseCloud.class})
public abstract class AbstractOperationTest {

    @SuppressWarnings("unchecked") // Because mock() returns Task and not Task<Object> :(
    private static final Task<Object> MOCK_TASK = mock(Task.class);

    @Before
    public void mockParseCloud(){
        PowerMockito.mockStatic(ParseCloud.class);

        when(ParseCloud.callFunctionInBackground(anyString(), anyMapOf(String.class, Object.class))).thenReturn(MOCK_TASK);
    }

    protected void assertOperationReturnValue(Object retVal) {
        assertEquals(MOCK_TASK, retVal);
    }

    protected void assertOperationParams(String expectedOperationName, HashMap<String, Object> expectedParams) {
        PowerMockito.verifyStatic();

        ArgumentCaptor<String> captor1 = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Map> captor2 = ArgumentCaptor.forClass(Map.class);
        ParseCloud.callFunctionInBackground(captor1.capture(), captor2.capture());

        assertEquals(captor1.getValue(), expectedOperationName);
        assertEquals(captor2.getValue(), expectedParams);
    }
}
