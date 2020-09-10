package com.example.roombasic;

import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

//换成继承于ListAdapter  当两次列表不同的时候 会自动更新不同的部分 不会显得突兀
public class WordAdapter extends ListAdapter<Word,WordAdapter.MyViewHolder> {

    //private List<Word> wordsList=new ArrayList<>(); //不再需要 会有新的List传入
    private boolean useCard;



    public WordAdapter(boolean useCard) {
        super(new DiffUtil.ItemCallback<Word>() {
            //用于记录判断数据相等的方式
            @Override
            public boolean areItemsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getId()==newItem.getId();
            }

            @Override
            public boolean areContentsTheSame(@NonNull Word oldItem, @NonNull Word newItem) {
                return oldItem.getChineseMeaning().equals(newItem.getChineseMeaning())
                        && oldItem.getWord().equals(newItem.getWord());
            }
        });
        this.useCard = useCard;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if (useCard){
            view= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_meaning_card,parent,false);
        }else {
            view= LayoutInflater.from(parent.getContext()).
                    inflate(R.layout.item_meaning,parent,false);
        }
        final MyViewHolder holder=new MyViewHolder(view);
        //在这里设置每个视图的响应事件 相比于在onbindvieholder中设置 会更加的高效 因为会被多次调用onbindviewholder
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //这一部分用于启动对应的网址  注意参数的使用
                Uri uri=Uri.parse("https://translate.google.cn/#view=home&op=translate&sl=en&tl=zh-CN&text="
                        +holder.textViewEnglish.getText());
                Intent intent=new Intent(Intent.ACTION_VIEW);
                intent.setData(uri);
                holder.itemView.getContext().startActivity(intent);
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder holder, int position) {
        Word word=getItem(position);
        holder.textViewNumber.setText(String.valueOf(position+1));
        holder.textViewChinese.setText(word.getChineseMeaning());
        holder.textViewEnglish.setText(word.getWord());
    }


    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewNumber,textViewEnglish,textViewChinese;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewChinese=itemView.findViewById(R.id.textViewChinese);
            textViewEnglish=itemView.findViewById(R.id.textViewEnglish);
            textViewNumber=itemView.findViewById(R.id.textViewnumber);
        }
    }


}
