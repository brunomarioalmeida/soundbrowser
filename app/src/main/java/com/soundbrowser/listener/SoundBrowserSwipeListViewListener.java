package com.soundbrowser.listener;

import android.util.Log;

import com.fortysevendeg.swipelistview.BaseSwipeListViewListener;

public class SoundBrowserSwipeListViewListener extends BaseSwipeListViewListener {

	@Override
	public void onOpened(int position, boolean toRight) {}

    @Override
    public void onClosed(int position, boolean fromRight) {}

    @Override
    public void onListChanged() {}

    @Override
    public void onMove(int position, float x) {}

    @Override
    public void onStartOpen(int position, int action, boolean right) {
        Log.d("soundbrowser", String.format("onStartOpen %d - action %d", position, action));
    }

    @Override
    public void onStartClose(int position, boolean right) {
        Log.d("soundbrowser", String.format("onStartClose %d", position));
    }

    @Override
    public void onClickFrontView(int position) 
    {
        Log.d("soundbrowser", String.format("onClickFrontView %d", position));

//        swipelistview.openAnimate(position); //when you touch front view it will open
//        playAndSchedule(position);
    }

    @Override
    public void onClickBackView(int position) {
        Log.d("soundbrowser", String.format("onClickBackView %d", position));
        
//        swipelistview.closeAnimate(position);//when you touch back view it will close
    }

    @Override
    public void onDismiss(int[] reverseSortedPositions) {}

}
