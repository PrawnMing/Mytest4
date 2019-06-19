package com.example.mytest4;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class AddActivity extends AppCompatActivity {
    static EditText et1, et2;
    Button btn;
    DBConnection helper;
    private String TITLE = "task_name";                //任务名
    private String CONTENT = "task_content";           //任务内容
    private String DATE = "task_date";                 //日期
    public int flag=0;
    public String editdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        et1 = findViewById(R.id.title);
        et2 = findViewById(R.id.content);
        btn = findViewById(R.id.submit_btn);
        btn.setOnClickListener(new myClick());

        Bundle bundle = this.getIntent().getExtras();
        String mytitle = bundle.getString("title");
        String mycontent = bundle.getString("content");
        flag = bundle.getInt("flag");
        editdate = bundle.getString("date");
        et1.setText(mytitle);
        et2.setText(mycontent);
    }

    class myClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            helper = new DBConnection(AddActivity.this);
            SQLiteDatabase db = helper.getWritableDatabase();
            //获取当前时间
            Date date = new Date();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String dateString = sdf.format(date);

            if (flag==0) {
                ContentValues values = new ContentValues();
                values.put(TITLE, AddActivity.et1.getText().toString());
                values.put(CONTENT, AddActivity.et2.getText().toString());
                values.put(DATE, dateString);
                db.insert("Mytask", null, values);
                Toast.makeText(AddActivity.this,"内容添加成功", Toast.LENGTH_LONG).show();
                db.close();
                finish();
            } else if (flag==1) {
                ContentValues values = new ContentValues();
                values.put(TITLE, AddActivity.et1.getText().toString());
                values.put(CONTENT, AddActivity.et2.getText().toString());
                values.put(DATE, dateString);
                db.update("Mytask", values, "task_date=?", new String[]{editdate});
                Toast.makeText(AddActivity.this,"内容修改成功", Toast.LENGTH_LONG).show();
                db.close();
                finish();
            } else {
                throw new IllegalArgumentException("flag参数不正确");
            }
        }
    }
}