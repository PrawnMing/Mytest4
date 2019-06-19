package com.example.mytest4;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn;
    Intent intent;
    ListView lv;
    List<Map<String, Object>> dataList;
    SQLiteDatabase db;
    DBConnection helper;
    SimpleAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn=findViewById(R.id.add_btn);
        btn.setOnClickListener(new myClick());
        lv=findViewById(R.id.ls);
        dataList=new ArrayList<Map<String,Object>>();
        lv.setOnItemClickListener(new myItemClick());
        lv.setOnItemLongClickListener(new myItemLongClick());

        helper=new DBConnection(MainActivity.this);
        db=helper.getReadableDatabase();

        intent = new Intent(this, LongRunningService.class);
        startService(intent);

    }

    @Override
    protected void onStart() {
        super.onStart();
        flush();
    }

    class myClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=new Intent(MainActivity.this,AddActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("title","");
            bundle.putString("content","");
            bundle.putInt("flag",0);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }
    class myItemLongClick implements AdapterView.OnItemLongClickListener{
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view,final int position, long id) {
            Builder builder=new Builder(MainActivity.this);
            builder.setTitle("该事项已完成？");
            builder.setPositiveButton("我已完成",new DialogInterface.OnClickListener(){

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    String map=lv.getItemAtPosition(position).toString();
                    String date=map.substring(map.indexOf("key_date=")+9,map.indexOf("}"));
                    db.delete("Mytask","task_date=?",new String[]{date});
                    flush();
                }
            });
            builder.setNegativeButton("尚未完成", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });
            builder.create();
            builder.show();
            return true;
        }
    }
    class myItemClick implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Bundle bundle=new Bundle();
            String map=lv.getItemAtPosition(position).toString();
            String title=map.substring(map.indexOf("{key_name=")+10,map.indexOf(","));
            String date=map.substring(map.indexOf("key_date=")+9,map.indexOf("}"));
            bundle.putString("title",title);
            Cursor cursor = db.query("Mytask", null, "task_date = ?", new String[]{date}, null, null, null);
            startManagingCursor(cursor);
            cursor.moveToFirst();
            String content=cursor.getString(cursor.getColumnIndex("task_content"));
            bundle.putString("content",content);
            bundle.putString("date",date);
            bundle.putInt("flag",1);
            Intent intent=new Intent(MainActivity.this,AddActivity.class);
            intent.putExtras(bundle);
            startActivity(intent);
        }
    }

    public void flush(){
        int size = dataList.size();
        if (size > 0) {
            dataList.removeAll(dataList);
            adapter.notifyDataSetChanged();
        }
        Cursor cursor = db.query("Mytask", null, null, null, null, null, null);
        startManagingCursor(cursor);
        while (cursor.moveToNext()) {
            String name = cursor.getString(cursor.getColumnIndex("task_name"));
            String date = cursor.getString(cursor.getColumnIndex("task_date"));
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("key_name", name);
            map.put("key_date", date);
            dataList.add(map);
        }
        adapter=new SimpleAdapter(MainActivity.this,dataList,R.layout.item,new String[]{"key_name","key_date"},new int[] {R.id.tv_content,R.id.tv_date});
        lv.setAdapter(adapter);
    }
}
