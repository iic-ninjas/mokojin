package com.iic.mokojin;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.iic.mokojin.models.QueueItem;
import com.iic.mokojin.operations.LeaveQueueOperation;
import com.iic.mokojin.views.CharacterViewer;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.util.ArrayList;
import java.util.Arrays;
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

    public PlayerQueueFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView =  inflater.inflate(R.layout.fragment_player_queue, container, false);
        ButterKnife.inject(this, rootView);

        mQueueListView.setEmptyView(rootView.findViewById(R.id.empty_queue_text));
        mQueueAdapter = new QueueAdapter(getActivity());
        mQueueListView.setAdapter(mQueueAdapter);
        mQueueListView.setDismissCallback(new EnhancedListView.OnDismissCallback() {
            @Override
            public EnhancedListView.Undoable onDismiss(final EnhancedListView enhancedListView, int i) {
                try {
                    new LeaveQueueOperation().run((QueueItem) mQueueListView.getItemAtPosition(i));
                    mQueueAdapter.dismissItem(i);
                    mQueueAdapter.notifyDataSetChanged();
                    mQueueAdapter.loadObjects();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return null;
            }
        });
        mQueueListView.enableSwipeToDismiss();
        scheduleUpdateClock();

        return rootView;
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

    public void onResume() {
        super.onResume();
        mQueueAdapter.loadObjects();
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

    static class QueueAdapter extends ParseQueryAdapter<QueueItem>{
        private List<Integer> mNonDismissedPositions;

        public QueueAdapter(Context context) {
            super(context, new QueryFactory<QueueItem>() {
                @Override
                public ParseQuery<QueueItem> create() {
                    ParseQuery<QueueItem> query = ParseQuery.getQuery(QueueItem.class);
                    query.orderByAscending("createdAt");
                    query.include("player.person");
                    query.include("player.characterA");
                    query.include("player.characterB");
                    return query;
                }
            });
            setPaginationEnabled(false);

            addOnQueryLoadListener(new OnQueryLoadListener<QueueItem>() {
                @Override
                public void onLoading() {
                    // Must be implemented
                }

                @Override
                public void onLoaded(List<QueueItem> queueItems, Exception e) {
                    // Initialize the array of non-dismissed positions to include all queried items

                    Integer[] positionsArray;
                    positionsArray = new Integer[queueItems.size()];
                    for (int i = 0; i < queueItems.size(); i++) {
                        positionsArray[i] = i;
                    }
                    mNonDismissedPositions = new ArrayList(Arrays.asList(positionsArray));
                }
            });
            
        }


        @Override
        public int getCount() {
            return mNonDismissedPositions != null ? mNonDismissedPositions.size() : 0;
        }

        @Override
        public QueueItem getItem(int position) {
            return super.getItem(mNonDismissedPositions.get(position));
        }

        @Override public long getItemId(int position) {
            return mNonDismissedPositions.get(position);
        }

        public void dismissItem(int position) {
            mNonDismissedPositions.remove(position);
        }

        @Override
        public View getItemView(final QueueItem queueItem, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.player_queue_item, null);
                v.setTag(new PlayerQueueItemViewHolder(v));
            }
            super.getItemView(queueItem, v, parent);

            final PlayerQueueItemViewHolder viewHolder = (PlayerQueueItemViewHolder) v.getTag();
            viewHolder.textView.setText(queueItem.getPlayer().getPerson().getName());
            viewHolder.characterViewer.setPlayer(queueItem.getPlayer());
            viewHolder.setStartDate(queueItem.getCreatedAt());
            return v;
        }

        class PlayerQueueItemViewHolder {
            @InjectView(R.id.player_name) TextView textView;
            @InjectView(R.id.time_in_queue) TextView clockView;
            @InjectView(R.id.player_character_image) CharacterViewer characterViewer;

            private Date mStartDate;

            public PlayerQueueItemViewHolder(View view) {
                ButterKnife.inject(this, view);
            }

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
    }
}

