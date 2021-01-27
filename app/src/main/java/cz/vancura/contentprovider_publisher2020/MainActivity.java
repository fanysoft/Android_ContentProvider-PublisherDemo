package cz.vancura.contentprovider_publisher2020;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.AsyncTaskLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import cz.vancura.contentprovider_publisher2020.data.MyDataContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor>  {

    private static String TAG = "myTAG-MainActivity";

    private static final int TASK_LOADER_ID = 0;
    Cursor mTaskData = null;
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "activity started");

        textView = findViewById(R.id.textView2);

        // init Loader
        getSupportLoaderManager().initLoader(TASK_LOADER_ID, null, this);

        // 1 - insert some data
        ContentValues contentValues = new ContentValues();
        contentValues.put(MyDataContract.TaskEntry.COLUMN_DESCRIPTION, "some task");
        contentValues.put(MyDataContract.TaskEntry.COLUMN_PRIORITY, 1);

        // Insert the content values via a ContentResolver
        Uri uri = getContentResolver().insert(MyDataContract.TaskEntry.CONTENT_URI, contentValues);
        if(uri != null) {
            Log.d(TAG, "creating item " + uri); // example content://cz.vancura.android.todolist/tasks/3
            Toast.makeText(getBaseContext(), uri.toString(), Toast.LENGTH_LONG).show();
        }


        // 3 - delete data
        /*
        Uri deleteUri = Uri.parse("content://cz.vancura.android.todolist/tasks/1");
        getContentResolver().delete(deleteUri, null, null);
        Log.d(TAG, "deleting item " + deleteUri);
        */


    }


    // Callbacks from LoaderManager

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        Log.d(TAG, "LoaderManager - onCreateLoader ..");

        return new AsyncTaskLoader<Cursor>(this) {

            // onStartLoading() is called when a loader first starts loading data
            @Override
            protected void onStartLoading() {
                if (mTaskData != null) {
                    // Delivers any previously loaded data immediately
                    deliverResult(mTaskData);
                } else {
                    // Force a new load
                    forceLoad();
                }
            }

            // loadInBackground() performs asynchronous loading of data
            @Override
            public Cursor loadInBackground() {
                // Query and load all task data in the background; sort by priority
                try {
                    return getContentResolver().query(MyDataContract.TaskEntry.CONTENT_URI,
                            null,
                            null,
                            null,
                            MyDataContract.TaskEntry.COLUMN_PRIORITY);

                } catch (Exception e) {
                    Log.e(TAG, "LoaderManager - Failed to asynchronously load data.");
                    e.printStackTrace();
                    return null;
                }
            }

            // deliverResult sends the result of the load, a Cursor, to the registered listener
            public void deliverResult(Cursor data) {
                Log.d(TAG, "LoaderManager - deliverResult ");
                mTaskData = data;
                super.deliverResult(data);

                int loop = 0;
                // 2 - read data
                if (data.moveToFirst()){
                    do{
                        loop++;
                        String dataId = data.getString(data.getColumnIndex("_id"));
                        String dataDesr = data.getString(data.getColumnIndex("description"));
                        String dataPrio = data.getString(data.getColumnIndex("priority"));
                        String dataUri = "content://cz.vancura.android.todolist/tasks/"+dataId;
                        String dataTogether =  loop + "] id=" + dataId + " description=" + dataDesr + " priority=" + dataPrio;

                        Log.d(TAG, dataTogether);
                        Log.d(TAG, dataUri);

                        textView.append(dataTogether + "\n" + dataUri + "\n\n");

                    }while(data.moveToNext());
                }
                data.close();

            }
        };

    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}