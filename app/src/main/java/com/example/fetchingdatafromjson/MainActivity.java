package com.example.fetchingdatafromjson;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static android.R.color.secondary_text_dark;

public class MainActivity extends AppCompatActivity {
    public RequestQueue mQueue;
    public LinearLayout a;
    public LinearLayout myRoot;
    public JSONArray jsonArray;
   ///public JSONArray jsonArray1;
    public JSONObject ques;
    public JSONObject ans;
    public Spinner spinner;
    Map<String,String> map;
    ///public ArrayList<String> usersList;


    //Adapters for Spinners
    ArrayAdapter<String> catAdapter;
    ArrayAdapter<String> othersAdapter;

    // Receiver which contain all the textViews [all the questions]
    List<String> receiver = new ArrayList<String>();
    List<String> receiver1 = new ArrayList<String>();
    List<String> receiver2 = new ArrayList<String>();

    // which contain individual editText are sent into edReceiver[this contains array of editTexts]
    List<EditText> myArray = new ArrayList();
    List<EditText> myArray2 = new ArrayList();

    // Which contain spinner on select values for MCQ questions.
    ArrayList<Spinner> listSpinner = new ArrayList<>();
    ArrayList<Spinner> listSpinner1 = new ArrayList<>();


    //Receivers for othersQ




    HashMap<String, String> textEditMap = new HashMap<String, String>();








    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         myRoot = (LinearLayout) findViewById(R.id.linear_layout);
         a = new LinearLayout(this);
        mQueue = Volley.newRequestQueue(this);

        //usersList = new ArrayList<String>();

        jsonParse();
    }




    private void jsonParse() {
        String url = "https://api.jsonbin.io/b/60a39f7989a1813069cd3a1b/9";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                             jsonArray = response.getJSONArray("questions");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                 ques = jsonArray.getJSONObject(i);
                                String type =ques.getString("type");
                                if(Objects.equals(type, "straightQ")){
                                    String question = ques.getString("question");
                                    NewTextEditViews(question);
                                }
                                else if(Objects.equals(type, "others")) {
                                    String mcq = ques.getString("question");
                                    String answer = ques.getString("answer");
                                    String[] othersOptions = answer.split("/");
                                    NewTextViewSpinnerWithOthers(mcq,othersOptions);

                                }


                                else {
                                    String mcq = ques.getString("question");
                                    String answer = ques.getString("answer");
                                    String[]options = answer.split("/");
                                    ///jsonArray1 = ques.getJSONArray("answer");
                                    //for( int j = 0; j<jsonArray1.length();j++){
                                    //ans = jsonArray1.getJSONObject(j);
                                    //usersList.add( ans.getString("option1"));
                                    //usersList.add( ans.getString("option2"));

                                    NewTextViewSpinner(mcq,options);
                                    catAdapter.notifyDataSetChanged();
                                }

                            }
                            CreateFinalButton();

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });

        mQueue.add(request);
    }


    private void NewTextEditViews(String question) { // Creating text view and edit text for straightQ.
        TextView view1= new TextView(this);
        EditText view2 = new EditText(this);
        a.setOrientation(LinearLayout.VERTICAL);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(10,10,10,10);
        view1.setText(question);
        view1.setTextSize(20);
        view2.setHint("Please Specify");
        view2.setHintTextColor(getResources().getColor(R.color.design_default_color_primary_variant));
        ///ColorStateList colorStateList = ColorStateList.valueOf(color);
        ///view2.setBackgroundTintList(colorStateList);
        view1.setLayoutParams(p);
        view2.setLayoutParams(p);
        a.addView(view1);
        a.addView(view2);
        receiver.add(view1.getText().toString());   // "All the textViews user entered data for StraighQ"
        myArray.add(view2);  // "All the editTexts user entered data for StraighQ"


    }

    private void NewTextViewSpinner(String mcq, String[] options) {  // Creating spinner and textView for MCQ.
        TextView view3 = new TextView(this);
        Spinner spinner = new Spinner(this);

        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(10,10,10,10);
        a.setOrientation(LinearLayout.VERTICAL);
        view3.setText(mcq);
        view3.setTextSize(20);
        view3.setLayoutParams(p);
        a.addView(view3);
        catAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, options);
        spinner.setAdapter(catAdapter);
        a.addView(spinner);
        listSpinner.add(spinner);
        receiver1.add(view3.getText().toString());

    }

    private void NewTextViewSpinnerWithOthers(String mcq, String[] othersOptions) { // creating spinner and textView with others popup
        TextView tv = new TextView(this);
        Spinner othersSpinner = new Spinner(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        p.setMargins(10,10,10,10);
        a.setOrientation(LinearLayout.VERTICAL);
        tv.setText(mcq);
        tv.setTextSize(20);
        tv.setLayoutParams(p);
        a.addView(tv);
        othersAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, othersOptions);
        othersSpinner.setAdapter(othersAdapter);
        a.addView(othersSpinner);
        receiver2.add(tv.getText().toString());
        listSpinner1.add(othersSpinner);


        if(a.getParent() != null) {
            ((ViewGroup)a.getParent()).removeView(a); // <- fix
        }
        myRoot.addView(a);


        othersSpinner
                .setOnItemSelectedListener(new OnItemSelectedListener() {

                    @Override
                    public void onItemSelected(AdapterView<?> arg0, View arg1,
                                               int pos, long id) {


                        if (othersSpinner.getItemAtPosition(pos).equals("others")){
                            Toast.makeText(MainActivity.this, "you selected others!!", Toast.LENGTH_LONG).show();
                             AlertEdit();
                        }

                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> arg0) {
                        // TODO Auto-generated method stub
                        Toast.makeText(MainActivity.this, "nothing selected", Toast.LENGTH_LONG).show();

                    }
                });

    }

    private void AlertEdit() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("IF others?");
        alertDialog.setMessage("Please Specify...");

        final EditText input = new EditText(MainActivity.this);
        input.setHint("your Response");
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);

        alertDialog.setView(input);
        myArray2.add(input);


        alertDialog.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.cancel();
                    }
                });

        alertDialog.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

        alertDialog.show();

    }




    private void CreateFinalButton() {
        Button submit = new Button(this);
        LinearLayout.LayoutParams p = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        submit.setLayoutParams(p);
        submit.setText(" Submit ");
        a.addView(submit);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               printer(receiver,receiver1,receiver2);

            }
        });

    }




    private void printer(List<String> receiver, List<String> receiver1, List<String> receiver2) {
        List<String> edReceiver = new ArrayList<String>();
        List<String> edReceiverDialog = new ArrayList<String>();
        List<String> mcqSpinnerReceiver = new ArrayList<String>();
        List<String> othersSpinnerReceiver = new ArrayList<String>();
        int i = 0;
        while (i < myArray.size()) {
            ///Toast.makeText(this, "d"+   String.valueOf(myArray.get(i).getText()), Toast.LENGTH_SHORT).show();
           edReceiver.add(String.valueOf(myArray.get(i).getText()));
            i++;
        }
        int l=0;
        while (l< myArray2.size()) {
            ///Toast.makeText(this, "d"+   String.valueOf(myArray.get(i).getText()), Toast.LENGTH_SHORT).show();
            edReceiverDialog.add(String.valueOf(myArray2.get(l).getText()));
            l++;
        }

        for(int j=0;j<listSpinner.size();j++){
            String p1 = (String) listSpinner.get(j).getSelectedItem();
            mcqSpinnerReceiver.add(String.valueOf(p1));

        }
        for(int k=0;k<listSpinner1.size();k++){
            String p2 = (String) listSpinner1.get(k).getSelectedItem();
            othersSpinnerReceiver.add(String.valueOf(p2));

        }


        //Toast.makeText(this, "ed"+edReceiver, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ed"+edReceiverDialog, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ff"+receiver, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ff"+receiver1, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "ff"+receiver2, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "spinner"+mcqSpinnerReceiver, Toast.LENGTH_SHORT).show();
        //Toast.makeText(this, "spinner"+othersSpinnerReceiver, Toast.LENGTH_SHORT).show();





        map = new LinkedHashMap<String,String>();  // ordered

        for (int rec=0; rec<receiver.size(); rec++) {
            map.put(receiver.get(rec), edReceiver.get(rec));    // is there a clearer way?
        }
        //Toast.makeText(this, "nea"+map, Toast.LENGTH_SHORT).show();

        Map<String,String> map1 = new LinkedHashMap<String,String>();  // ordered

        for (int rec1=0; rec1<receiver1.size(); rec1++) {
            map1.put(receiver1.get(rec1), mcqSpinnerReceiver.get(rec1));    // is there a clearer way?
        }
        //Toast.makeText(this, "ne"+map1, Toast.LENGTH_SHORT).show();

        Map<String,String> map2 = new LinkedHashMap<String,String>();

        for (int rec2=0; rec2<receiver2.size(); rec2++) {
            if(!othersSpinnerReceiver.get(rec2).equals("others")){
                map2.put(receiver2.get(rec2), othersSpinnerReceiver.get(rec2));    // is there a clearer way?
            }
            else {
                map2.put(receiver2.get(rec2),edReceiverDialog.get(rec2)+" [Others]");

            }
            }
        Toast.makeText(this, "na"+map2, Toast.LENGTH_SHORT).show();

        map.putAll(map1);
        map.putAll(map2);
        Toast.makeText(this, "Final"+map, Toast.LENGTH_SHORT).show();



        ///  Map<String, List<String>> hm = new HashMap<String, List<String>>();
        //List<String> values = new ArrayList<String>();
        //values.add("Value 1");
        //values.add("Value 2");
        //hm.put("Key1", values);
        //
        //// to get the arraylist
        //System.out.println(hm.get("key1"));


    }

































    public void callVolly(final String username, final String password){
        RequestQueue MyRequestQueue = Volley.newRequestQueue(this);
        String url = "https://api.jsonbin.io/b/60a39f7989a1813069cd3a1b/7"; // <----enter your post url here
        StringRequest MyStringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //This code is executed if the server responds, whether or not the response contains data.
                //The String 'response' contains the server's response.
                Toast.makeText(MainActivity.this, "your response is being sent", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() { //Create an error listener to handle errors appropriately.
            @Override
            public void onErrorResponse(VolleyError error) {
                //This code is executed if there is an error.
            }
        }) {
            protected Map<String, String> getParams() {
              return map;
            }
        };


        MyRequestQueue.add(MyStringRequest);
    }
}