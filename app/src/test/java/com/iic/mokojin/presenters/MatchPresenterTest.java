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
    public void testRatioZero(){
        Match match = createMatchWithChanceToWin(0);
        assertEquals("1 : ∞", MatchPresenter.getRatioString(match));
    }

    @Test
    public void testRatioOne(){
        Match match = createMatchWithChanceToWin(1);
        assertEquals("∞ : 1", MatchPresenter.getRatioString(match));
    }


    @Test
    public void testProgressSimple(){
        Match match = createMatchWithChanceToWin(0.75);
        assertEquals(75, MatchPresenter.getProgress(match));
    }

    @Test
    public void testProgressReverse(){
        Match match = createMatchWithChanceToWin(0.25);
        assertEquals(25, MatchPresenter.getProgress(match));
    }

    @Test
    public void testProgressOneDecimal(){
        Match match = createMatchWithChanceToWin(0.6);
        assertEquals(60, MatchPresenter.getProgress(match));
    }

    @Test
    public void testProgressTwoDecimal(){
        Match match = createMatchWithChanceToWin(0.5556);
        assertEquals(56, MatchPresenter.getProgress(match));
    }

    @Test
    public void testProgressZero(){
        Match match = createMatchWithChanceToWin(0);
        assertEquals(0, MatchPresenter.getProgress(match));
    }

    @Test
    public void testProgressOne(){
        Match match = createMatchWithChanceToWin(1);
        assertEquals(100, MatchPresenter.getProgress(match));
    }

    private Match createMatchWithChanceToWin(double value) {
        Match mockMatch = MockFactory.createMatch(null);
        when(mockMatch.getChanceToWin()).thenReturn(value);
        return mockMatch;
    }

}
