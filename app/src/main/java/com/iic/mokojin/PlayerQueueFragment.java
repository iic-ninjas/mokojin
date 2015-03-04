package com.iic.mokojin;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iic.mokojin.models.QueueItem;
import com.iic.mokojin.presenters.CharacterPresenter;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PlayerQueueFragment extends Fragment {

    @InjectView(R.id.queue_list_view) ListView mQueueListView;
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

        mQueueAdapter = new QueueAdapter(getActivity());
        mQueueListView.setAdapter(mQueueAdapter);

        return rootView;
    }

    static class QueueAdapter extends ParseQueryAdapter<QueueItem>{

        public QueueAdapter(Context context) {
            super(context, new QueryFactory<QueueItem>() {
                @Override
                public ParseQuery<QueueItem> create() {
                    ParseQuery<QueueItem> query = ParseQuery.getQuery(QueueItem.class);
                    query.orderByDescending("createdAt");
                    query.include("player.person");
                    return query;
                }
            });
            setPaginationEnabled(false);
        }

        @Override
        public View getItemView(QueueItem queueItem, View v, ViewGroup parent) {
            if (v == null) {
                v = View.inflate(getContext(), R.layout.player_queue_item, null);
                v.setTag(new PlayerQueueItemViewHolder(v));
            }
            super.getItemView(queueItem, v, parent);

            PlayerQueueItemViewHolder viewHolder = (PlayerQueueItemViewHolder) v.getTag();
            viewHolder.textView.setText(queueItem.getPlayer().getPerson().getName());
            viewHolder.imageView.setImageResource(CharacterPresenter.getImageResource(getContext(), queueItem.getPlayer().getCharacterA()));

            return v;
        }

        class PlayerQueueItemViewHolder {
            @InjectView(R.id.player_name) TextView textView;
            @InjectView(R.id.player_character_image) ImageView imageView;

            public PlayerQueueItemViewHolder(View view) { ButterKnife.inject(this, view); }
        }
    }
}

