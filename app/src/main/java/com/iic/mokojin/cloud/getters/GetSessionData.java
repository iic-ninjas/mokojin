package com.iic.mokojin.cloud.getters;

import android.util.Pair;

import com.iic.mokojin.models.Match;
import com.iic.mokojin.models.QueueItem;
import com.parse.ParseCloud;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import bolts.Continuation;
import bolts.Task;

/**
 * Created by giladgo on 3/10/15.
 */
public class GetSessionData {

    private static final String GET_SESSION_DATA_CLOUD_FUNCTION_NAME = "getSessionData";

    public static Task<Pair<Match, List<QueueItem>>> getSessionData() {
        return ParseCloud.<HashMap<String, Object>>callFunctionInBackground(GET_SESSION_DATA_CLOUD_FUNCTION_NAME, new HashMap<String, Object>())
                .onSuccess(new Continuation<HashMap<String, Object>, Pair<Match, List<QueueItem>>>() {
            @Override
            public Pair<Match, List<QueueItem>> then(Task<HashMap<String, Object>> task) throws Exception {
                HashMap<String, Object> resultMap = task.getResult();

                Object matchObject = resultMap.get("match");
                Match match = null;
                if (matchObject instanceof Match) {
                    match = (Match)matchObject;
                }

                List<QueueItem> queueItemList = new ArrayList<>();
                ArrayList objectList = (ArrayList)resultMap.get("queue");
                for (Object queueItem : objectList) {
                    queueItemList.add((QueueItem)queueItem);
                }

                return new Pair<>(match, queueItemList);
            }
        });
    }
}
