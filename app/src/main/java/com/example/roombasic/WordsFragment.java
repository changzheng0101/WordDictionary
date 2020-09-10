package com.example.roombasic;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class WordsFragment extends Fragment {
    private WordViewModel wordViewModel;
    private RecyclerView recyclerView;
    private WordAdapter adapter1;
    private WordAdapter adapter2;
    private FloatingActionButton floatingActionButton;
    private LiveData<List<Word>> filteredWords;
    private static final String VIEW_TYPE_SHP  = "view_type_shp";
    private static final String IS_USING_CARD = "is_using_card";
    private List<Word> allWords;
    private boolean undo=false;
    private DividerItemDecoration dividerItemDecoration;

    public WordsFragment() {
        // Required empty public constructor
        //保证菜单可以正常的显示
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.main_menu,menu);
        SearchView searchView= (SearchView) menu.findItem(R.id.app_bar_search).getActionView();
        searchView.setMaxWidth(700);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //点击确定 会响应这里的事件 返回true 代表事件处理完成 不再继续分发 返回false 则代表继续需要其他进行处理
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String text=newText.trim();
                //因为要引入新的数据观察  所以在这里移除之前的观察
                filteredWords.removeObservers(requireActivity());
                filteredWords=wordViewModel.getWords(text);
                filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
                    @Override
                    public void onChanged(List<Word> words) {
                        int temp=adapter1.getItemCount();
                        allWords=words;
                        if (temp!=words.size()){
                            adapter1.submitList(words);
                            adapter2.submitList(words);
                        }
                    }
                });
                return true;
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.clearData:
                AlertDialog.Builder builder=new AlertDialog.Builder(requireActivity());
                builder.setTitle("是否确定清除数据");
                builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        wordViewModel.deleteAllWords();
                    }
                });
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.switchViewType:
                SharedPreferences shp=requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
                boolean use_card=shp.getBoolean(IS_USING_CARD,false);
                SharedPreferences.Editor editor=shp.edit();
                if (use_card){
                    recyclerView.setAdapter(adapter2);
                    recyclerView.addItemDecoration(dividerItemDecoration);
                    editor.putBoolean(IS_USING_CARD,false);
                }else {
                    recyclerView.setAdapter(adapter1);
                    recyclerView.removeItemDecoration(dividerItemDecoration);
                    editor.putBoolean(IS_USING_CARD,true);
                }
                editor.apply();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_words, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        wordViewModel= ViewModelProviders.of(requireActivity()).get(WordViewModel.class);
        recyclerView=requireActivity().findViewById(R.id.recyclerView);
        floatingActionButton=requireActivity().findViewById(R.id.floatingActionButton);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireActivity()));
        recyclerView.setItemAnimator(new DefaultItemAnimator(){
            @Override
            public void onAnimationFinished(@NonNull RecyclerView.ViewHolder viewHolder) {
                super.onAnimationFinished(viewHolder);
                LinearLayoutManager linearLayoutManager= (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager!=null){
                    int firstPosition=linearLayoutManager.findFirstVisibleItemPosition();
                    int lastPosition=linearLayoutManager.findLastVisibleItemPosition();
                    for(int i=firstPosition;i<=lastPosition;i++){
                        WordAdapter.MyViewHolder holder = (WordAdapter.MyViewHolder) recyclerView.findViewHolderForAdapterPosition(i);
                        if (holder!=null){
                            holder.textViewNumber.setText(String.valueOf(i+1));
                        }
                    }
                }
            }
        });
        adapter1=new WordAdapter(true);
        adapter2=new WordAdapter(false);
        dividerItemDecoration=new DividerItemDecoration(requireActivity(),DividerItemDecoration.VERTICAL);

        //读取是否为卡片视图
        SharedPreferences shp=requireActivity().getSharedPreferences(VIEW_TYPE_SHP,Context.MODE_PRIVATE);
        boolean use_card=shp.getBoolean(IS_USING_CARD,false);
        if (use_card){
            recyclerView.setAdapter(adapter1);
        }else {
            recyclerView.setAdapter(adapter2);
            recyclerView.addItemDecoration(dividerItemDecoration);
        }
        //recyclerView.setAdapter(adapter1);
        filteredWords=wordViewModel.getAllWordsLive();
        filteredWords.observe(getViewLifecycleOwner(), new Observer<List<Word>>() {
            @Override
            public void onChanged(List<Word> words) {
                int temp=adapter1.getItemCount();
                allWords=words;
                if (temp!=words.size()){
                    if (temp<words.size()&& !undo){
                        recyclerView.smoothScrollBy(0,-200);
                    }
                    undo=false;
                    //这种写法有太大的花销 而且过渡不平滑 标记所有的数据改变
                    adapter1.submitList(words);
                    adapter2.submitList(words);
//                    adapter1.notifyItemChanged(0); //这种写法只有一项改变了
//                    adapter1.notifyDataSetChanged();
//                    adapter2.notifyDataSetChanged();
                }
            }
        });

        //为recyclerView设置为可以删除某一项
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.START| ItemTouchHelper.END) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                final Word wordToDelete=allWords.get(viewHolder.getAdapterPosition());
                wordViewModel.deleteWord(wordToDelete);
                Snackbar.make(requireActivity().findViewById(R.id.wordsFragmentView),"删除了一个单词",Snackbar.LENGTH_SHORT)
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                undo=true;
                                wordViewModel.insertWords(wordToDelete);
                            }
                        })
                        .show();

            }
        }).attachToRecyclerView(recyclerView);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavController navController= Navigation.findNavController(v);
                navController.navigate(R.id.action_wordsFragment_to_addFragment);
            }
        });
    }

    @Override
    public void onResume() {
        //重写为了保证可以正常的隐藏键盘
//        InputMethodManager inputMethodManager= (InputMethodManager) requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
//        inputMethodManager.hideSoftInputFromWindow(getView().getWindowToken(),0);
        super.onResume();
    }
}
