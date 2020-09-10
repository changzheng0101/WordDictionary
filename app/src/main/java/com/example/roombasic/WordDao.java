package com.example.roombasic;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao //database access object 数据库访问接口
public interface WordDao {
    @Insert
    void insertWords(Word... words);

    @Update
    void updateWords(Word... words);

    @Delete
    void  deleteWords(Word... words);

    @Query("delete from WORD")
    void  deleteAllWords();

    @Query("select * from WORD order by id desc")
    //List<Word> getAllWords();
    LiveData<List<Word>>getAllWordsLiveData();

    @Query("select * from WORD where english_word like :pattern order by id desc")
    LiveData<List<Word>>findWord(String pattern);
}
