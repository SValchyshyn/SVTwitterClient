package com.valchyshyn.stepan.svtwitterclient.twitter;

import java.util.List;

import twitter4j.Status;

/**
 * Created by valchyshyn on 26.02.17.
 */
public interface TwitterSearchRequester {

  void finishSuccess(List<Status> statuses);
}
