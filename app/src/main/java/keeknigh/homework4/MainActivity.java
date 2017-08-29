package keeknigh.homework4;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String LOG_TAG = "homework4";

    RequestQueue queue;

    private class ListElement{
        ListElement() {};

        ListElement(String t1, String subt1, String myURL){
            title = t1;
            subtitle = subt1;
            url = myURL;
        }

        public String title;
        public String subtitle;
        public String url;
    }

    private ArrayList<ListElement> aList;


    private class MyAdapter extends ArrayAdapter<ListElement>{

        int resource;
        Context context;

        public MyAdapter(Context _context, int _resource, List<ListElement> items){
            super(_context, _resource, items);
            resource = _resource;
            context = _context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){

            RelativeLayout newView;

            ListElement item = getItem(position);

            //inflate new view
            if(convertView == null){
                newView = new RelativeLayout(getContext());
                LayoutInflater vi = (LayoutInflater)
                        getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                vi.inflate(resource, newView, true);
            } else{
                newView = (RelativeLayout) convertView;
            }

            //Fill in view
            TextView titleTextView = (TextView) newView.findViewById(R.id.title);
            TextView subtitleTextView = (TextView) newView.findViewById(R.id.subtitle);

            titleTextView.setText(item.title);
            subtitleTextView.setText(item.subtitle);

            //set listener
            newView.setTag(item.url);
            newView.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    //when cell is clicked
                    String s = v.getTag().toString();
                    int duration = Toast.LENGTH_SHORT;
                    Toast toast = Toast.makeText(context, s, duration);
                    toast.show();

                    //pass url to next activity
                    Intent intent = new Intent(context, Activity2.class);
                    intent.putExtra("url", v.getTag().toString());
                    startActivity(intent);
                }
            });

            return newView;
        }
    }

    private MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        queue = Volley.newRequestQueue(this);

        aList = new ArrayList<ListElement>();
        adapter = new MyAdapter(this, R.layout.list_element, aList);
        ListView myListView = (ListView) findViewById(R.id.listView);
        myListView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        loadCell();


    }

    public void clickRefresh(View v){
        loadCell();
    }

    public void loadCell(){
        Log.i(LOG_TAG, "Requested refresh");
        aList.clear();

        getList();

        Toast toast = Toast.makeText(getBaseContext(), "New element added", Toast.LENGTH_SHORT);
        toast.show();
        adapter.notifyDataSetChanged();
    }

    private void getList(){

        //instantiate the RequestQueue
        String url = "https://luca-ucsc-teaching-backend.appspot.com/hw4/get_news_sites";

        JsonObjectRequest jsObjRequest = new JsonObjectRequest
                (Request.Method.GET, url, null, new Response.Listener<JSONObject>(){

            @Override
            public void onResponse(JSONObject response){
                Log.d(LOG_TAG, "Received: " + response.toString());
                try{
                    JSONArray receivedList = response.getJSONArray("news_sites");
                    for(int i = 0; i < receivedList.length(); i++) {
                        JSONObject myObject = new JSONObject(receivedList.getString(i));
                        Log.d(LOG_TAG, "myObject " + i + myObject);
                        if (myObject.getString("title").equals("null")) {
                            continue;
                        } else if (myObject.getString("subtitle").equals("null")) {
                            adapter.add(new ListElement(myObject.getString("title"),
                                    "",
                                    myObject.getString("url")));

                            continue;
                        } else if (myObject.getString("url").equals("null")) {
                            continue;
                        } else {
                            adapter.add(new ListElement(myObject.getString("title"),
                                    myObject.getString("subtitle"),
                                    myObject.getString("url")));
                        }
                    }
                } catch(Exception e){
                    //nothing
                }
            }
        }, new Response.ErrorListener(){

                    @Override
                    public void onErrorResponse(VolleyError error){
                        Log.d(LOG_TAG, error.toString());
                    }
                });

        queue.add(jsObjRequest);
    }
}



