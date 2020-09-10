package com.example.roombasic;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

//viewmodel 不应该有获取数据的职责 需要创建仓库  只需要负责界面的更新
public class WordViewModel extends AndroidViewModel {

    private WordRepository wordRepository;

    public WordViewModel(@NonNull Application application) {
        super(application);
        wordRepository=new WordRepository(application);
    }

    LiveData<List<Word>> getAllWordsLive() {
        return wordRepository.getAllWordsLive();
    }
    LiveData<List<Word>> getWords(String pattern){
        return wordRepository.getWord(pattern);
    }

    void insertWords(Word... words){
        wordRepository.insertWords(words);
    }

    void deleteWord(Word wordTodelete){
        wordRepository.deleteOneWord(wordTodelete);
    }
    void deleteAllWords(){
        wordRepository.deleteAllWords();;
    }


}
