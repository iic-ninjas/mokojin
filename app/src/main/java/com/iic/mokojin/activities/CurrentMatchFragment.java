package com.iic.mokojin.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iic.mokojin.R;
import com.iic.mokojin.cloud.operations.EndMatchOperation;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.presenters.MatchPresenter;
import com.iic.mokojin.views.CharacterViewer;
import com.iic.mokojin.views.ProgressHudDialog;
import com.squareup.otto.Subscribe;

import javax.inject.Inject;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CurrentMatchFragment extends AbstractMokojinFragment {

    private static final String LOG_TAG = CurrentMatchFragment.class.getName();

    private Match mCurrentMatch;

    @InjectView(R.id.empty_text_view)  View mEmptyText;
    @InjectView(R.id.active_match) View mActiveMatch;

    @InjectView(R.id.player_a_name)  TextView mPlayerANameTextView;
    @InjectView(R.id.player_b_name)  TextView mPlayerBNameTextView;

    @InjectView(R.id.player_a_character) CharacterViewer mPlayerACharacter;
    @InjectView(R.id.player_b_character) CharacterViewer mPlayerBCharacter;

    @InjectView(R.id.chance_bar) ProgressBar mChanceBar;
    @InjectView(R.id.chance_to_win) TextView mChanceText;

    @Inject CurrentSessionStore mCurrentSessionStore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_match, container, false);
        ButterKnife.inject(this, rootView);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        mCurrentSessionStore.getEventBus().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mCurrentSessionStore.getEventBus().unregister(this);
    }

    @Subscribe
    public void refreshCurrentMatch(CurrentSessionStore.SessionUpdateEvent event) {
        mCurrentMatch = mCurrentSessionStore.getCurrentMatch();
        refreshUI();
    }


    private void refreshUI() {
        if (mCurrentMatch != null) {
            mEmptyText.setVisibility(View.INVISIBLE);
            mActiveMatch.setVisibility(View.VISIBLE);

            mPlayerANameTextView.setText(mCurrentMatch.getPlayerA().getPerson().getName());
            mPlayerBNameTextView.setText(mCurrentMatch.getPlayerB().getPerson().getName());

            mPlayerACharacter.setPlayer(mCurrentMatch.getPlayerA());
            mPlayerBCharacter.setPlayer(mCurrentMatch.getPlayerB());

            mChanceBar.setProgress(MatchPresenter.getProgress(mCurrentMatch));
            mChanceText.setText(MatchPresenter.getRatioString(mCurrentMatch));
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
            mActiveMatch.setVisibility(View.INVISIBLE);
        }
    }

    private void endMatch(Player.PlayerType playerType) {
        final ProgressHudDialog dialog = new ProgressHudDialog(getActivity(), getResources().getString(R.string.ending_match_progress));
        dialog.show();
        new EndMatchOperation(mCurrentMatch, playerType).run().continueWith(new Continuation<Match, Void>() {
            @Override
            public Void then(Task<Match> task) throws Exception {
                dialog.dismiss();
                mCurrentSessionStore.refreshData();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @OnClick(R.id.player_a)
    void onPlayerAClick() {
        endMatch(Player.PlayerType.PLAYER_A);
    }

    @OnClick(R.id.player_b)
    void onPlayerBClick() {
        endMatch(Player.PlayerType.PLAYER_B);
    }


    @OnLongClick(R.id.player_a)
    boolean onPlayerALongClick() {
        ChooseCharactersActivity.chooseCharacter(getActivity(), mCurrentMatch.getPlayerA());
        return true;
    }

    @OnLongClick(R.id.player_b)
    boolean onPlayerBLongClick() {
        ChooseCharactersActivity.chooseCharacter(getActivity(), mCurrentMatch.getPlayerB());
        return true;
    }
}
