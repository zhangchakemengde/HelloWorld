package com.example.activitytest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import com.example.Database.MyDatabaseHelper;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class EightActivity extends Activity{
	
	private EditText edit;
	private Button saveData;
	private Button restoreData;
	private MyDatabaseHelper dbHelper;
	
	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.eight_layout);
		edit = (EditText) findViewById(R.id.edit_1);
		saveData = (Button) findViewById(R.id.button_6);
		restoreData = (Button) findViewById(R.id.button_7);
		dbHelper = new MyDatabaseHelper(this,"BookStore.db",null,3);
		Button createDatabase = (Button) findViewById(R.id.button_8);
		Button addData = (Button) findViewById(R.id.button_9);
		Button updateData = (Button) findViewById(R.id.button_10);
		Button GotoNine = (Button) findViewById(R.id.button_11);
		GotoNine.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				Intent intent = new Intent(EightActivity.this,NinthActivity.class);
				startActivity(intent);
				finish();
			}
		});
		updateData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("price", 10.99);
				db.update("Book", values, "name = ?", new String[] { "The DaVinci Code" });
			}
		});
		addData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				SQLiteDatabase db = dbHelper.getWritableDatabase();
				ContentValues values = new ContentValues();
				values.put("name", "The DaVinci Code");
				values.put("author", "Dan Brown");
				values.put("pages", 454);
				values.put("price", 16.96);
				db.insert("Book", null, values);
				values.clear();
				values.put("name", "The Lost Symbol");
				values.put("author", "Dan Brown");
				values.put("pages", 510);
				values.put("price", 19.95);
				db.insert("Book", null, values); // 插入第二条数据
				
			}
		});
		createDatabase.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				dbHelper.getWritableDatabase();
			}
		});
		saveData.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v){
				SharedPreferences.Editor editor = getSharedPreferences("data",MODE_PRIVATE).edit();
				editor.putString("name", "Tom");
				editor.putInt("age", 28);
				editor.putBoolean("married", false);
				editor.commit();
			}
			
		});
		restoreData.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v){
				SharedPreferences pref = getSharedPreferences("data",MODE_PRIVATE);
				String name = pref.getString("name", "");
				int age = pref.getInt("age", 0);
				boolean married = pref.getBoolean("married", false);
				Log.d("EightActivity", "name is"+name);
				Log.d("EightActivity", "age is"+age);
				Log.d("EightActivity", "married is" + married);
				
			}
		});
		String inputText = load();
		if(!TextUtils.isEmpty(inputText)){
			edit.setText(inputText);
			edit.setSelection(inputText.length());
			Toast.makeText(this, "Restoring succeeded", Toast.LENGTH_SHORT).show();
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		String inputText = edit.getText().toString();
		save(inputText);
	}
	
	public void save(String inputText){
		FileOutputStream out = null;
		BufferedWriter writer = null;
		try {
			out = openFileOutput("data.txt",Context.MODE_PRIVATE);
			writer = new BufferedWriter(new OutputStreamWriter(out));
			writer.write(inputText);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			try {
				if (writer != null){
					writer.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	public String load(){
		FileInputStream in = null;
		BufferedReader reader = null;
		StringBuilder content = new StringBuilder();
		try {
			in = openFileInput("data.txt");
			reader = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while((line = reader.readLine()) != null){
				content.append(line);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(reader != null){
				try {
					reader.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return content.toString();
	}

}
