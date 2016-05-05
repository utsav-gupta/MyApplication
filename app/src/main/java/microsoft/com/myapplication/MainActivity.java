package microsoft.com.myapplication;

import android.content.ClipData;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private EditText tv;
    private StringRequest stringRequest;
    private ArrayList<JSONObject> pagesList ;
    private ListView lv;
    private ItemAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        final String url ="https://en.wikipedia.org/w/api.php?action=query&prop=pageimages&format=json&piprop=thumbnail&pithumbsize=50&pilimit=50&generator=prefixsearch&gpssearch=";
        pagesList = new ArrayList<JSONObject>();

        lv = (ListView) findViewById(R.id.lv);
        adapter = new ItemAdapter(this,R.layout.listview_item_row,pagesList);
        lv.setAdapter(adapter);


        tv = (EditText)findViewById(R.id.text);
        tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(stringRequest!=null){    /// nullify the previous search request if present
                    stringRequest.cancel();
                }
                if(s.toString().equals("")){
                    pagesList.clear();
                    adapter.notifyDataSetChanged();
                }
                 stringRequest = new StringRequest(Request.Method.GET, url+s.toString(),
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                ArrayList<JSONObject> list = new ArrayList<JSONObject>();
                                try {
                                    JSONObject pages = new JSONObject(response).getJSONObject("query").getJSONObject("pages");
                                    Iterator<String> keys = pages.keys();

                                    while (keys.hasNext()){
                                        String key = keys.next();
                                        JSONObject page = pages.getJSONObject(key);
                                        list.add(page);
                                    }

                                } catch (JSONException e) {

                                }
                                finally {
                                    pagesList.clear();
                                    pagesList.addAll(list);
                                    adapter.notifyDataSetChanged();
                                }

                            }
                        }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        System.out.print("That didn't work!");
                    }
                });

                queue.add(stringRequest);
            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
