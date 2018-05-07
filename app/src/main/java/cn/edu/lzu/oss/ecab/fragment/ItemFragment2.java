package cn.edu.lzu.oss.ecab.fragment;

import android.graphics.Canvas;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.javabean.ItemDetail;
import cn.edu.lzu.oss.ecab.javabean.Times;
import cn.edu.lzu.oss.ecab.util.Const;

public class ItemFragment2 extends Fragment {
    private RecyclerView recyclerView;
    private ItemAdapter adapter;
    private List<ItemDetail> items;
    private ImageView background;
    public ItemFragment2(){

    }

    public static ItemFragment2 newInstance(){
        return new ItemFragment2();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_item,container,false);
        initView(view);
        initData();
        return view;
    }

    private void initData() {
        items = new ArrayList<>();
        items.add(new ItemDetail("甘肃省兰州市榆中县兰州大学榆中校区通信网络中心二楼",
                new Times(2018,4,13,9,54,10),
                4 ,4, Const.TimeBeanConst.STATE_WAITING, "00000000"));
        items.add(new ItemDetail("甘肃省兰州市城关区兰州大学图书馆",
                new Times(2018,4,10,8,22,44),
                2,2,Const.TimeBeanConst.STATE_CONTAIN, "00000001"));
    }

    private void initView(View view) {
        recyclerView = view.findViewById(R.id.fragment_item_recycler);
        adapter = new ItemAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void onDraw(Canvas c, RecyclerView parent, RecyclerView.State state) {
                super.onDraw(c, parent, state);
            }
        });
        background = view.findViewById(R.id.fragment_item_background);
        Glide.with(getContext()).load(R.mipmap.splash).into(background);
    }

    public class ItemAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View common = LayoutInflater.from(getContext()).inflate(R.layout.fragment_item_item , parent , false);
            return new NormalViewHolder(common);
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            NormalViewHolder common = (NormalViewHolder) holder;
            ItemDetail such = items.get(position);
            common.location.setText(such.getLocation());
            common.time.setText(such.getTime().toString());
//            common.size.setText(String.format(Locale.CHINA,"%dX%d",such.getWidth(),such.getHeight()));

        }

        @Override
        public int getItemCount() {
            return items == null ? 0 : items.size();
        }

        class NormalViewHolder extends RecyclerView.ViewHolder{
            TextView location;
            TextView time;
            TextView size;
            View view;
            ImageView image;
            NormalViewHolder(View itemView) {
                super(itemView);
                location = itemView.findViewById(R.id.fragment_item_detail_location);
                time = itemView.findViewById(R.id.fragment_item_detail_time);
                size = itemView.findViewById(R.id.fragment_item_detail_size);
                view = itemView.findViewById(R.id.fragment_item_view);
                image = itemView.findViewById(R.id.fragment_item_image);

            }
        }
    }
}
