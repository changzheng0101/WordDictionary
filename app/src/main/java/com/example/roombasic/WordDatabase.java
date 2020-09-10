package com.example.roombasic;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;


//singleton 只允许生成一个实例 防止生成多个
//synchronized 可以保证即使有多个线程  也只创建一个数据库
//当版本改变的时候 要改变version 否则会报错
@Database(entities = {Word.class},version = 2,exportSchema = false)
public abstract class WordDatabase extends RoomDatabase {
    private static WordDatabase INSTANCE;
    static  synchronized WordDatabase getINSTANCE(Context context){
        if (INSTANCE==null){
            //getApplicationContext() 可以返回全局唯一的根节点的上下文
            INSTANCE= Room.databaseBuilder(context.getApplicationContext(),WordDatabase.class,"word_database")
                    //.allowMainThreadQueries() //强制允许在主线程中进行查询  一般是不允许的  要自己开线程
                    //.fallbackToDestructiveMigration() //这种方式不用写migration 会损坏原来的数据
                    .addMigrations(MIGRATION_1_2)
                    .build();
        }
        return INSTANCE;
    }
    public abstract WordDao getWordDao();

    //migration 要自己声明  注意修饰词 传入的参数是哪个版本到哪个版本
    static final Migration MIGRATION_1_2=new Migration(1,2) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("alter table word add column visible INTEGER not null default 1 ");
        }
    };
}
