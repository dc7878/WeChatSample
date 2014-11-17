package com.example.wechatsample.ui.demo;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;

import com.example.wechatsample.R;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshBase.OnRefreshListener;
import com.example.wechatsample.library.pulltorefresh.PullToRefreshListView;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class PullRefreshListViewActivity extends Activity {

    private ListView mListView;
    private PullToRefreshListView mPullListView;
    private ArrayAdapter<String> mAdapter;
    private LinkedList<String> mListItems;
    private SimpleDateFormat mDateFormat = new SimpleDateFormat("MM-dd HH:mm");
    private boolean mIsStart = true;
    private int mCurIndex = 0;
    private static final int mLoadDataCount = 10;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.demo_listview);
//        mPullListView = new PullToRefreshListView(this);
//        setContentView(mPullListView);
        mPullListView = (PullToRefreshListView) findViewById(R.id.demo_listview);
        mPullListView.setPullLoadEnabled(false);
        mPullListView.setScrollLoadEnabled(true);
        
        mCurIndex = mLoadDataCount;
        mListItems = new LinkedList<String>();
        mListItems.addAll(Arrays.asList(mStrings).subList(0, mCurIndex));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mListItems);
        mListView = mPullListView.getRefreshableView();
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View view, int position, long id) {
                String text = mListItems.get(position) + ", index = " + (position + 1);
                Toast.makeText(PullRefreshListViewActivity.this, text, Toast.LENGTH_SHORT).show();
            }
        });
        
        mPullListView.setOnRefreshListener(new OnRefreshListener<ListView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = true;
                new GetDataTask().execute();
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ListView> refreshView) {
                mIsStart = false;
                new GetDataTask().execute();
            }
        });
        setLastUpdateTime();
        
        mPullListView.doPullRefreshing(true, 300);
    }
    
    private class GetDataTask extends AsyncTask<Void, Void, String[]> {

        @Override
        protected String[] doInBackground(Void... params) {
            // Simulates a background job.
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
            return mStrings;
        }

        @Override
        protected void onPostExecute(String[] result) {
            boolean hasMoreData = true;
            if (mIsStart) {
//                mListItems.addFirst("Added after refresh...");
            	mListItems.clear();
            	mCurIndex = 0;
            } 
            
            int start = mCurIndex;
            int end = mCurIndex + mLoadDataCount;
            if (end >= mStrings.length) {
                end = mStrings.length;
                hasMoreData = false;
            }
            
            for (int i = start; i < end; ++i) {
                mListItems.add(mStrings[i]);
            }
            
            mCurIndex = end;
            
            mAdapter.notifyDataSetChanged();
            mPullListView.onPullDownRefreshComplete();
            mPullListView.onPullUpRefreshComplete();
            mPullListView.setHasMoreData(hasMoreData);
            setLastUpdateTime();

            super.onPostExecute(result);
        }
    }
    
    private void setLastUpdateTime() {
        String text = formatDateTime(System.currentTimeMillis());
        mPullListView.setLastUpdatedLabel(text);
    }

    private String formatDateTime(long time) {
        if (0 == time) {
            return "";
        }
        
        return mDateFormat.format(new Date(time));
    }
    
    public static final String[] mStrings = {
        "Abbaye de Belloc", "Abbaye du Mont des Cats", "Abertam", "Abondance", "Ackawi",
        "Acorn", "Adelost", "Affidelice au Chablis", "Afuega'l Pitu", "Airag", "Airedale",
        "Aisy Cendre", "Allgauer Emmentaler", "Baby Swiss","Bouyssou", "Bra", "Braudostur",
        "Breakfast Cheese", "Brebis du Lavort", "Brebis du Lochois", "Brebis du Puyfaucon",
        "Bresse Bleu", "Brick", "Brie", "Brie de Meaux", "Brie de Melun", "Brillat-Savarin",
        "Brin", "Brin d' Amour", "Brin d'Amour","Butterkase", "Button (Innes)",
        "Buxton Blue", "Cabecou", "Caboc", "Cabrales", "Cachaille", "Caciocavallo", "Caciotta",
   
    };
}
