package com.iic.mokojin.presenters;

import com.iic.mokojin.models.Match;
import com.iic.mokojin.utils.MockFactory;
import com.iic.mokojin.utils.RobolectricGradleTestRunner;

import org.junit.Test;
import org.junit.runner.RunWith;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * Created by yon on 3/10/15.
 */
@RunWith(RobolectricGradleTestRunner.class)
public class MatchPresenterTest {

    @Test
    public void testRatioSimple(){
        Match match = createMatchWithChanceToWin(0.75);
        assertEquals("3 : 1", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testRatioReverse(){
        Match match = createMatchWithChanceToWin(0.25);
        assertEquals("1 : 3", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testRatioOneDecimal(){
        Match match = createMatchWithChanceToWin(0.6);
        assertEquals("1.5 : 1", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testRatioTwoDecimal(){
        Match match = createMatchWithChanceToWin(0.5556);
        assertEquals("1.25 : 1", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testZero(){
        Match match = createMatchWithChanceToWin(0);
        assertEquals("1 : ∞", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testOne(){
        Match match = createMatchWithChanceToWin(1);
        assertEquals("∞ : 1", MatchPresenter.getRatioString(match));
    }

    private Match createMatchWithChanceToWin(double value) {
        Match mockMatch = MockFactory.createMatch(null);
        when(mockMatch.getChanceToWin()).thenReturn(value);
        return mockMatch;
    }

}
