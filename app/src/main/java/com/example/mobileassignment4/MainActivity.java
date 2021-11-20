package com.example.mobileassignment4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int[] articlesId = new int[50];
    ArrayList<Article> articles;
    int validArticles=0;

    ListView articlesListView;
    ArrayAdapter<String> adapter;

    String[] namesUsingDatabase = new String[20];
    String[] urlUsingDatabase = new String[20];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        articles = new ArrayList<>();
        articlesListView = (ListView)findViewById(R.id.articlesListView);

        new Thread(new Runnable() {
                @Override
                public void run() {

                    try {

                        //getArticlesId();
                        //getAndParseArticles(articlesId);

                        retrieveArticlesFromDatabase();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                articlesListView.setAdapter(adapter);
                            }
                        });
                    }


                    //catch (JSONException e) {
                      //  e.printStackTrace();
                    //}
                    catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        articlesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                goToStory(i);
            }
        });

    }


    public void getArticlesId() throws JSONException, IOException {

        JSONArray arr = new JSONArray(getJSONStringOfIds());

        for(int i=0; i<50 ; i++){
            articlesId[i]= (int) arr.get(i);
        }
    }

    public String getJSONStringOfIds() throws IOException {
        URL url = new URL("https://hacker-news.firebaseio.com/v0/topstories.json");

        BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

        String jsonResult = "";
        String inputLine = "";

        while ((inputLine = in.readLine()) != null){
            jsonResult=jsonResult+inputLine;
        }
        return jsonResult;
    }

    public void getAndParseArticles(int[] articlesId) throws IOException, JSONException {

        for(int i=0 ; i<articlesId.length ; i++) {

            if(validArticles==20)
                break;
            int articleId = articlesId[i];
            String urlString = "https://hacker-news.firebaseio.com/v0/item/" + articleId + ".json";
            URL url = new URL(urlString);

            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));

            String jsonResult = "";
            String inputLine = "";

            while ((inputLine = in.readLine()) != null){
                jsonResult=jsonResult+inputLine;
            }

            parseArticleJsonAndAddToList(jsonResult);
        }
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, convertToStringArray());
    }


    public String[] convertToStringArray(){
        String[] names = new String[articles.size()];
        for(int i=0; i<articles.size(); i++){
            names[i] = i+1 +") "+ articles.get(i).toString();
        }
        return names;
    }

    public void parseArticleJsonAndAddToList(String json) throws JSONException {

        JSONObject obj = new JSONObject(json);
        if(obj.has("url")){
            articles.add(new Article(obj.getString("title"),obj.getString("url")));
            validArticles++;
        }
    }


    public void goToStory(int i){
        Intent intent = new Intent(this,StoryDetail.class);
        intent.putExtra("name",namesUsingDatabase[i]);
        intent.putExtra("url", urlUsingDatabase[i]);
        startActivity(intent);
    }


    public void retrieveArticlesFromDatabase() throws IOException {


        SQLiteDatabase db = this.openOrCreateDatabase("articleDb",MODE_PRIVATE,null);

        /*
        db.execSQL("DROP TABLE articles");

        db.execSQL("CREATE TABLE IF NOT EXISTS articles (name VARCHAR,url VARCHAR)");



        for(int i=0; i<articles.size(); i++){

            ContentValues values = new ContentValues();
            values.put("name",articles.get(i).getName());
            values.put("url",articles.get(i).getUrl());
            db.insert("articles",null,values);

        }
        */


        //System.out.println("Finished saving to database");




        Cursor c = db.rawQuery("SELECT * FROM articles",null);

        int nameIndex = c.getColumnIndex("name");
        int urlIndex = c.getColumnIndex("url");


        int i=0;
        while (c.moveToNext()){
            namesUsingDatabase[i] = c.getString(nameIndex);
            urlUsingDatabase[i] = c.getString(urlIndex);
            i++;
        }

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, namesUsingDatabase);

    }






}