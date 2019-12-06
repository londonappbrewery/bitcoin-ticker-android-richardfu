package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;
import cz.msebera.android.httpclient.Header;



public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/";
    private final String LOGTAG = "Bitcoin";
    // Member Variables:
    TextView mPriceTextView;
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener(){

            @Override
            public void onItemSelected(AdapterView parent, View view, int position, long id) {

//                Toast.makeText(MainActivity.this, "你選的是"+spinner01.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
                Log.d(LOGTAG, "" + parent.getItemAtPosition(position));
                currency = (String)parent.getItemAtPosition(position);
                letsDoSomeNetworking(BASE_URL+"BTC"+currency);
            }

            @Override
            public void onNothingSelected(AdapterView arg0) {

//                Toast.makeText(MainActivity.this, "您沒有選擇任何項目", Toast.LENGTH_LONG).show();
                Log.d(LOGTAG, "Nothing selected");
            }

        });

    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d(LOGTAG, "JSON: " + response.toString());
                try {
                    String last = response.getString("last");
                    mPriceTextView.setText(last);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d(LOGTAG, "Request fail! Status code: " + statusCode);
                Log.d(LOGTAG, "Fail response: " + response);
                Log.e("ERROR", e.toString());
                Toast.makeText(MainActivity.this, "Request Failed", Toast.LENGTH_SHORT).show();
            }
        });


    }


}
