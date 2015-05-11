package com.soundbrowser.persistence.ormlite;

import java.sql.SQLException;
import java.util.List;
import java.util.Random;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
//import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.support.ConnectionSource;

/**
 * Our Android UI activity which displays a text window when it is run.
 *
 * <p>
 * <b>NOTE:</b> This uses the unsupported JDBC interface and the included version of the H2 database instead of the
 * built in SQLite database. This is only here as a proof of concept.
 * </p>
 */
public class HelloAndroidOracleJdbc extends Activity
{
    private final String LOG_TAG = getClass().getSimpleName();
    private TextView tv;

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        Log.i(LOG_TAG, "creating " + getClass());

        tv = new TextView(this);
        tv.setText("Doing ...");
        setContentView(tv);

        //new OracleTask().execute("simpleDao");
        new OracleTask().execute();
    }

    private class OracleTask extends AsyncTask
    //private class OracleTask extends AsyncTask<String, Integer, String>
    {
        private final String LOG_TAG = getClass().getSimpleName();

        private ConnectionSource connectionSource;
        private Dao<SimpleData, Integer> simpleDao;

        private String dbURL = "jdbc:oracle:thin:@192.168.1.82:1521:xe";
        private String dbUsername = "bruno";
        private String dbPassword = "bruno2";

        @Override
        protected String doInBackground(Object... arg0)
    //    protected String doInBackground(String... args)
        {
            if (connectionSource == null) {
                try {
//                    connectionSource = new JdbcConnectionSource(
//                        //"jdbc:h2:/data/data/com.example.helloandroidh2/databases/helloAndroidH2"
//                        dbURL, dbUsername, dbPassword
//                    );
                    /*
                    try {
                        TableUtils.dropTable(connectionSource, SimpleData.class, true);
                        TableUtils.createTable(connectionSource, SimpleData.class);
                    } catch (SQLException e) {
                        // ignored
                        e.printStackTrace();
                    }
                    */
                    simpleDao = DaoManager.createDao(connectionSource, SimpleData.class);

                    return doSampleDatabaseStuff(simpleDao);
                } catch (SQLException e) {
                    throw new RuntimeException("Problems initializing database objects", e);
                }
            }
            return "simpleDao";
        }

        @Override
        //protected void onProgressUpdate(Integer... progress)
        protected void onProgressUpdate(Object... progress)
        {
            Toast.makeText(
                getApplicationContext(), "" + ((Integer)progress[0]), Toast.LENGTH_SHORT
            ).show();
        }

//        protected void onPostExecute(String result) {
        protected void onPostExecute(Object result) {
            tv.setText((String)result);
        }

        /**
         * Do our sample database stuff.
         */
        private String doSampleDatabaseStuff(Dao<SimpleData, Integer> simpleDao)
        {
            try {
                // query for all of the data objects in the database
                List<SimpleData> list = simpleDao.queryForAll();
                // our string builder for building the content-view
                StringBuilder sb = new StringBuilder();
                sb.append("got ").append(list.size()).append("\n");
                publishProgress(30);

                // if we already have items in the database
                int simpleC = 0;
                for (SimpleData simple : list) {
                    sb.append("------------------------------------------\n");
                    sb.append("[" + simpleC + "] = ").append(simple).append("\n");
                    simpleC++;
                }

                sb.append("------------------------------------------\n");
                for (SimpleData simple : list) {
                    simpleDao.delete(simple);
                    sb.append("deleted id ").append(simple.id).append("\n");
                    Log.i(LOG_TAG, "deleting simple(" + simple.id + ")");
                    simpleC++;
                }
                publishProgress(60);

                int createNum;
                do {
                    createNum = new Random().nextInt(3) + 1;
                } while (createNum == list.size());
                for (int i = 0; i < createNum; i++) {
                    // create a new simple object
                    long millis = System.currentTimeMillis();
                    SimpleData simple = new SimpleData(millis);
                    // store it in the database
                    simpleDao.create(simple);
                    Log.i(LOG_TAG, "created simple(" + millis + ")");
                    // output it
                    sb.append("------------------------------------------\n");
                    sb.append("created new entry #").append(i + 1).append(":\n");
                    sb.append(simple).append("\n");
                    try {
                        Thread.sleep(5);
                    } catch (InterruptedException e) {
                        // ignore
                    }
                }
                publishProgress(100);

                Log.i("com.soundbrowser", sb.toString());
                return sb.toString();
            } catch (SQLException e) {
                Log.e(LOG_TAG, "Database exception", e);
                tv.setText("Database exeption: " + e.getMessage());
                return "";
            }
        }
    }
}

