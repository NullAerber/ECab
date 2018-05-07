package cn.edu.lzu.oss.ecab.fragment;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.view.MaterialListView;

import java.util.ArrayList;
import java.util.List;

import cn.edu.lzu.oss.ecab.R;
import cn.edu.lzu.oss.ecab.activities.PayActivity;


public class ItemFragment extends Fragment {
    List<Card> cards;
    private MaterialListView mListView;

    public static ItemFragment newInstance(){
        return new ItemFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get,container,false);

        cards = new ArrayList<>();
        mListView = view.findViewById(R.id.order_list);

        cards.add(CreateNewCard("pic1"));
        cards.add(CreateNewCard("pic2"));

        mListView.getAdapter().addAll(cards);

        return view;
    }


    private Card CreateNewCard(String name) {
        final CardProvider provider = new Card.Builder(getContext())
                .setTag("BIG_IMAGE_BUTTONS_CARD")
                .withProvider(new CardProvider())
                .setLayout(R.layout.material_image_with_buttons_card)
                .setTitle("兰州大学 网络中心")
                .setTitleColor(getResources().getColor(R.color.white))
                .setDescription("存储大小：2X2")
                .setDrawable(R.drawable.pic1)
                .addAction(R.id.left_text_button, new TextViewAction(getContext())
                        .setText("获取二维码")
                        .setTextResourceColor(R.color.black_button)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {
                                getActivity().startActivity(new Intent(getActivity(), PayActivity.class));
                            }
                        }))
                .addAction(R.id.right_text_button, new TextViewAction(getContext())
                        .setText("订单详情…")
                        .setTextResourceColor(R.color.colorTheme)
                        .setListener(new OnActionClickListener() {
                            @Override
                            public void onActionClicked(View view, Card card) {

                            }
                        }));

//        final CardProvider provider = new Card.Builder(this)
//                .setTag("BIG_IMAGE_BUTTONS_CARD")
//                .setDismissible()
//                .withProvider(new CardProvider())
//                .setLayout(R.layout.material_image_with_buttons_card)
//                .setTitle()
//                .setTitleColor(getColor(R.color.white))
//                .setDescription();
//                .addAction(R.id.left_text_button, new TextViewAction(this)
//                        .setTextResourceColor(R.color.colorTheme)
//                        .setListener(new OnActionClickListener() {
//                            @Override
//                            public void onActionClicked(View view, Card card) {
//                               startActivity(new Intent(ListActivity.this,MainActivity.class));
//                            }
//                        }))
//                .addAction(R.id.right_text_button, new TextViewAction(this)
//                        .setTextResourceColor(R.color.black_button)
//                        .setListener(new OnActionClickListener() {
//                            @Override
//                            public void onActionClicked(View view, Card card) {
//                                Toast.makeText(ListActivity.this, "You have pressed the right button", Toast.LENGTH_SHORT).show();
//                            }
//                        }));

        provider.setDrawable(getResources().getIdentifier(name, "drawable", getActivity().getPackageName()));
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
////                byte[] data = new byte[0];
////                try {
////                    data = ImageService.getImage(getString(R.string.LINKUSRL) + "cloth/" + id + ".jpg");
////                } catch (IOException e) {
////                    e.printStackTrace();
////                }
////                final byte[] finalData = data;
//                runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Resources res=getResources();
//                        res.getIdentifier(name,"drawable",getPackageName());
//                        provider.setDrawable(R.drawable.pic1);
//                    }
//                });
//            }
//        }).start();

        return provider.endConfig().build();
    }
}
