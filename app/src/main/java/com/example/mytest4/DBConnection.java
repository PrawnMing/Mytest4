package com.example.mytest4;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBConnection extends SQLiteOpenHelper
{
    static final String Database_name = "List.db";
    static final int Database_Version = 1;

    //定义数据库名称及结构
    private String TABLE_NAME = "Mytask";               //数据表名
    private String ID = "task_id";                      //ID
    private String TITLE = "task_name";                //任务名
    private String CONTENT = "task_content";           //任务内容
    private String DATE = "task_date";                 //日期
    DBConnection(Context ctx)
    {
        super(ctx, Database_name, null, Database_Version);
    }
    public void onCreate(SQLiteDatabase database)
    {
        String sql = "CREATE TABLE " + TABLE_NAME + " ("
                + ID  + " INTEGER primary key autoincrement, "
                + TITLE + " text , "
                + CONTENT + " text ,"
                + DATE +" text  "+");";
        database.execSQL(sql);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}

