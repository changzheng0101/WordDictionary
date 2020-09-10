package com.example.roombasic;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class WordRepository {
    private LiveData<List<Word>> allWordsLive;
    private WordDao wordDao;

    public WordRepository(Context context) {
        WordDatabase wordDatabase=WordDatabase.getINSTANCE(context.getApplicationContext());
        wordDao=wordDatabase.getWordDao();
        allWordsLive=wordDao.getAllWordsLiveData();
    }

    public LiveData<List<Word>> getAllWordsLive() {
        return allWordsLive;
    }
    public LiveData<List<Word>> getWord(String pattern){
        return wordDao.findWord("%"+pattern+"%");
    }

    void insertWords(Word... words){
        new InsertTask(wordDao).execute(words);
    }

    void deleteAllWords(){
        new DeleteTask(wordDao).execute();
    }

    void deleteOneWord(Word... words){
        new DeleteOneWordTask(wordDao).execute(words);
    }

    //写内部类的时候 要声明成静态的才可以
    static class InsertTask extends AsyncTask<Word,Void,Void>{
        private WordDao wordDao;

        public InsertTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.insertWords(words);
            return null;
        }
    }
    static class DeleteTask extends AsyncTask<Void,Void,Void> {
        private WordDao wordDao;

        public DeleteTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            wordDao.deleteAllWords();
            return null;
        }
    }
    static class DeleteOneWordTask extends AsyncTask<Word,Void,Void> {
        private WordDao wordDao;

        public DeleteOneWordTask(WordDao wordDao) {
            this.wordDao = wordDao;
        }

        @Override
        protected Void doInBackground(Word... words) {
            wordDao.deleteWords(words);
            return null;
        }
    }
}
