package com.iic.mokojin;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.iic.mokojin.cloud.operations.LeaveQueueOperation;
import com.iic.mokojin.data.CurrentSessionStore;
import com.iic.mokojin.models.QueueItem;
import com.iic.mokojin.views.CharacterViewer;
import com.squareup.otto.Bus;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import butterknife.OnItemLongClick;
import de.timroes.android.listview.EnhancedListView;

public class PlayerQueueFragment extends Fragment {

    @InjectView(R.id.queue_list_view) EnhancedListView mQueueListView;
    QueueAdapter mQueueAdapter;

    private Bus mEventBus;

    private List<QueueItem> mQueueItems = new ArrayList<>();

    public PlayerQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEventBus = ((Application)getActivity().getApplication()).getCurrentSessionStore().getEventBus();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_player_queue, container, false);
        ButterKnife.inject(this, rootView);

        mQueueListView.setEmptyView(rootView.findViewById(R.id.empty_queue_text));
        mQueueAdapter = new QueueAdapter();
        mQueueListView.setAdapter(mQueueAdapter);
        mQueueListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(final EnhancedListView enhancedListView, int i) {
                new LeaveQueueOperation().run((QueueItem) mQueueListView.getItemAtPosition(i));
                mQueueItems.remove(i);
                mQueueAdapter.notifyDataSetChanged();
                return null;
            }
        });
        mQueueListView.enableSwipeToDismiss();
        scheduleUpdateClock();

        return rootView;
    }


    @Subscribe
    public void refreshQueue(CurrentSessionStore.SessionUpdateEvent event) {
        mQueueItems = ((Application)getActivity().getApplication()).getCurrentSessionStore().getQueue();
        mQueueAdapter.notifyDataSetChanged();
    }

    @Override
    public void onStart() {
        super.onStart();
        mEventBus.register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        mEventBus.unregister(this);
    }

    private void scheduleUpdateClock(){
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (null != mQueueAdapter) {
                            mQueueAdapter.notifyDataSetChanged();
                            scheduleUpdateClock();
                        }
                    }
                });
            }
        }, 1000);
    }

    @OnItemLongClick(R.id.queue_list_view)
    boolean onPlayerLongClick(int position) {
        ChooseCharactersActivity.chooseCharacter(getActivity(), mQueueAdapter.getItem(position).getPlayer());
        return true;
    }

    @OnClick(R.id.add_player_button)
    void onAddPlayerClick(){
        AddPlayerActivity.launch(getActivity());
    }

    class QueueAdapter extends BaseAdapter {

        class PlayerQueueItemViewHolder {
            private Date mStartDate;

            @InjectView(R.id.player_name) TextView textView;
            @InjectView(R.id.time_in_queue) TextView clockView;
            @InjectView(R.id.player_character_image) CharacterViewer characterViewer;

            public PlayerQueueItemViewHolder(View view) { ButterKnife.inject(this, view); }

            public void setStartDate(Date mStartDate) {
                this.mStartDate = mStartDate;
                updateClock();
            }

            private void updateClock(){
                long millisecondsAgo = new Date().getTime() - mStartDate.getTime();
                long secondsAgo = millisecondsAgo / 1000;
                clockView.setText(DateUtils.formatElapsedTime(secondsAgo));
            }
        }


        @Override
        public int getCount() {
            return mQueueItems.size();
        }

        @Override
        public QueueItem getItem(int position) {
            return mQueueItems.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = View.inflate(getActivity(), R.layout.player_queue_item, null);
                convertView.setTag(new PlayerQueueItemViewHolder(convertView));
            }

            QueueItem queueItem = this.getItem(position);

            final PlayerQueueItemViewHolder viewHolder = (PlayerQueueItemViewHolder) convertView.getTag();
            viewHolder.textView.setText(queueItem.getPlayer().getPerson().getName());
            viewHolder.characterViewer.setPlayer(queueItem.getPlayer());
            viewHolder.setStartDate(queueItem.getCreatedAt());
            return convertView;
        }
    }
}


