package com.example.roombasic;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends AppCompatActivity {

//    Button buttoninsert,buttonclear;
//    TextView textView;
//    WordViewModel wordViewModel;
//    RecyclerView recyclerView;
//    WordAdapter wordAdapter1,wordAdapter2;
//    Switch aSwitch;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        navController=Navigation.findNavController(findViewById(R.id.fragment));
        //设置有返回键
        NavigationUI.setupActionBarWithNavController(this,navController);
//        wordViewModel= ViewModelProviders.of(this).get(WordViewModel.class);
//        recyclerView=findViewById(R.id.recyclerview);
//        wordAdapter1=new WordAdapter(false);
//        wordAdapter2=new WordAdapter(true);
//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(wordAdapter1);
//        aSwitch=findViewById(R.id.switchCard);
//        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (isChecked){
//                    recyclerView.setAdapter(wordAdapter2);
//                } else {
//                    recyclerView.setAdapter(wordAdapter1);
//                }
//            }
//        });
//
//
//        wordViewModel.getAllWordsLive().observe(this, new Observer<List<Word>>() {
//            @Override
//            public void onChanged(List<Word> words) {
//                wordAdapter1.setWordsList(words);
//                wordAdapter1.notifyDataSetChanged();
//                wordAdapter2.setWordsList(words);
//                wordAdapter2.notifyDataSetChanged();
//            }
//        });
//
//
//        textView=findViewById(R.id.textViewnumber);
//        buttoninsert=findViewById(R.id.button);
//        buttonclear=findViewById(R.id.button3);
//
//        buttoninsert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Word word1=new Word("hello ","你好");
//                Word word2=new Word("world","世界");
//                wordViewModel.insertWords(word1,word2);
//
//            }
//        });
//        buttonclear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                wordViewModel.deleteWords();
//            }
//        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        navController.navigateUp();
        InputMethodManager inputMethodManager= (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(findViewById(R.id.fragment).getWindowToken(),0);
        return super.onSupportNavigateUp();
    }

    /*
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
    static class DeleteTask extends AsyncTask<Void,Void,Void>{
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
*/

}
