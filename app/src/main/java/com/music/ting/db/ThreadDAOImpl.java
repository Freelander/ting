package com.music.ting.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.music.ting.model.ThreadInfo;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Jun on 2015/6/4.
 * 数据访问接口实现
 */
public class ThreadDAOImpl implements ThreadDAO {

    private DBHelper mHelper = null;

    public ThreadDAOImpl(Context context){
        mHelper = new DBHelper(context);//创建数据访问对象
    }

    @Override
    public void insertThread(ThreadInfo threadInfo) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//获得可写的数据库
        db.execSQL("insert into thread_info(thread_id,url,start,end,finished) values(?,?,?,?,?)",
                new Object[]{threadInfo.getId(),threadInfo.getUrl(),threadInfo.getStart(),
                        threadInfo.getEnd(),threadInfo.getFinished()});
        db.close();
    }

    @Override
    public void deleteThread(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//获得可写的数据库
        db.execSQL("delete from thread_info where url = ? and thread_id = ?",
                new Object[]{url, thread_id});
        db.close();
    }

    @Override
    public void updateThread(String url, int thread_id, int finished) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//获得可写的数据库
        db.execSQL("update thread_info set finished = ? where url = ? and thread_id = ?",
                new Object[]{finished, url, thread_id});
        db.close();
    }

    @Override
    public List<ThreadInfo> getTreads(String url) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//获得可写的数据库
        List<ThreadInfo> list = new ArrayList<ThreadInfo>();
        Cursor cursor =
                db.rawQuery("select * from thread_info where url = ?", new String[]{url});
        while (cursor.moveToNext()){
            ThreadInfo thread = new ThreadInfo();
            thread.setId(cursor.getInt(cursor.getColumnIndex("thread_id")));
            thread.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            thread.setStart(cursor.getInt(cursor.getColumnIndex("start")));
            thread.setEnd(cursor.getInt(cursor.getColumnIndex("end")));
            thread.setFinished(cursor.getInt(cursor.getColumnIndex("finished")));
            list.add(thread);
        }
        cursor.close();
        db.close();
        return list;

    }

    @Override
    public boolean isExists(String url, int thread_id) {
        SQLiteDatabase db = mHelper.getWritableDatabase();//获得可写的数据库
        Cursor cursor =
                db.rawQuery("select * from thread_info where url = ? and thread_id = ?",
                        new String[]{url, String.valueOf(thread_id)});
        boolean exists = cursor.moveToNext();
        cursor.close();
        db.close();
        return exists;
    }
}
