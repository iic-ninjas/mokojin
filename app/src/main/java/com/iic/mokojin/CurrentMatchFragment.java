package com.iic.mokojin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.iic.mokojin.cloud.operations.EndMatchOperation;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.Player;
import com.iic.mokojin.presenters.MatchPresenter;
import com.iic.mokojin.views.CharacterViewer;
import com.iic.mokojin.views.ProgressHudDialog;
import com.squareup.otto.Subscribe;

import bolts.Continuation;
import bolts.Task;
import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class CurrentMatchFragment extends Fragment {

    private static final String LOG_TAG = CurrentMatchFragment.class.getName();

    private Match mCurrentMatch;

    @InjectView(R.id.empty_text_view)  View mEmptyText;
    @InjectView(R.id.players_container) View mPlayersContainer;

    @InjectView(R.id.player_a_name)  TextView mPlayerANameTextView;
    @InjectView(R.id.player_b_name)  TextView mPlayerBNameTextView;

    @InjectView(R.id.player_a_character) CharacterViewer mPlayerACharacter;
    @InjectView(R.id.player_b_character) CharacterViewer mPlayerBCharacter;

    @InjectView(R.id.chance_bar) ProgressBar mChanceBar;
    @InjectView(R.id.chance_to_win) TextView mChanceText;

    private CurrentSessionStore mCurrentSessionStore;

    public CurrentMatchFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_current_match, container, false);
        ButterKnife.inject(this, rootView);
        mCurrentSessionStore  = CurrentSessionStore.get(getActivity());

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
            mPlayersContainer.setVisibility(View.VISIBLE);

            mPlayerANameTextView.setText(mCurrentMatch.getPlayerA().getPerson().getName());
            mPlayerBNameTextView.setText(mCurrentMatch.getPlayerB().getPerson().getName());

            mPlayerACharacter.setPlayer(mCurrentMatch.getPlayerA());
            mPlayerBCharacter.setPlayer(mCurrentMatch.getPlayerB());

            mChanceBar.setVisibility(View.VISIBLE);
            mChanceText.setVisibility(View.VISIBLE);
            mChanceBar.setProgress(MatchPresenter.getProgress(mCurrentMatch));
            mChanceText.setText(MatchPresenter.getRatioString(mCurrentMatch));
        } else {
            mEmptyText.setVisibility(View.VISIBLE);
            mPlayersContainer.setVisibility(View.INVISIBLE);
            mChanceBar.setVisibility(View.INVISIBLE);
            mChanceText.setVisibility(View.INVISIBLE);
        }
    }

    private void endMatch(Player.PlayerType playerType) {
        final ProgressHudDialog dialog = new ProgressHudDialog(getActivity(), getResources().getString(R.string.ending_match_progress));
        dialog.show();
        new EndMatchOperation(mCurrentMatch, playerType).run().continueWith(new Continuation<Match, Void>() {
            @Override
            public Void then(Task<Match> task) throws Exception {
                Log.i(LOG_TAG, "Match ended");
                dialog.hide();
                return null;
            }
        }, Task.UI_THREAD_EXECUTOR);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.player_a_character)
    void onPlayerAClick() {
        endMatch(Player.PlayerType.PLAYER_A);
    }

    @SuppressWarnings("unused")
    @OnClick(R.id.player_b_character)
    void onPlayerBClick() {
        endMatch(Player.PlayerType.PLAYER_B);
    }


    @SuppressWarnings("unused")
    @OnLongClick(R.id.player_a_character)
    boolean onPlayerALongClick() {
        ChooseCharactersActivity.chooseCharacter(getActivity(), mCurrentMatch.getPlayerA());
        return true;
    }

    @SuppressWarnings("unused")
    @OnLongClick(R.id.player_b_character)
    boolean onPlayerBLongClick() {
        ChooseCharactersActivity.chooseCharacter(getActivity(), mCurrentMatch.getPlayerB());
        return true;
    }
}
