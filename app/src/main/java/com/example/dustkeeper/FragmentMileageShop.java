package com.example.dustkeeper;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class FragmentMileageShop extends Fragment {
    private LinearLayout ListContainer;
    private ArrayList<String> OrganizationNames;
    private ArrayList<String> ItemNames;
    private ArrayList<String> ItemPrice;

    @Override
    public void onCreate(Bundle savedInstancState) {
        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mileage_shop, container, false);

        ListContainer = (LinearLayout) view.findViewById(R.id.MileageShopContainer);

        OrganizationNames = new ArrayList<String>();
        OrganizationNames.add("광진구청");
        OrganizationNames.add("광진구보건소");
        OrganizationNames.add("광진구보건소");

        ItemNames = new ArrayList<String>();
        ItemNames.add("물");
        ItemNames.add("마스크");
        ItemNames.add("비타민");

        ItemPrice = new ArrayList<String>();
        ItemPrice.add("150");
        ItemPrice.add("100");
        ItemPrice.add("1000");

        showItemList();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    LinearLayout setCustomLinearLayout(LinearLayout l) {
        // 둥근 모서리 흰 배경
        l.setBackgroundResource(R.drawable.rounded_corner);

        // 가운데 정렬
        l.setVerticalGravity(Gravity.CENTER_VERTICAL);

        // 패딩 설정
        l.setPadding(50,50,50,50);

        // 마진 설정
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        Params.setMargins(50,20,50,100);
        l.setLayoutParams(Params);

        return l;
    }

    void showItemList() {
        for (int i=0; i<ItemNames.size(); i++) {

            // 수직 레이아웃 생성
            LinearLayout ItemContainer = new LinearLayout(getActivity());
            ItemContainer.setOrientation(LinearLayout.VERTICAL);
            ItemContainer = setCustomLinearLayout(ItemContainer);

            // 물품 이름 수평 레이아웃
            LinearLayout ItemNameWrapper = new LinearLayout(getActivity());
            ItemNameWrapper.setOrientation(LinearLayout.HORIZONTAL);

            // 기관 이름 수평 레이아웃
            LinearLayout OrgaNameWrapper = new LinearLayout(getActivity());
            OrgaNameWrapper.setOrientation(LinearLayout.HORIZONTAL);
            OrgaNameWrapper.setVerticalGravity(Gravity.CENTER_VERTICAL);

            // 마일리지 수평 레이아웃
            LinearLayout MileageWrapper = new LinearLayout(getActivity());
            MileageWrapper.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            MileageWrapper.setLayoutParams(Params);
            MileageWrapper.setGravity(Gravity.RIGHT);

            // 텍스트 뷰 생성
            TextView itemNameText = new TextView(getActivity());
            itemNameText.setText(ItemNames.get(i));
            itemNameText.setTextColor(Color.rgb(90, 90, 90));
            itemNameText.setTextSize(18);

            TextView orgaNameText = new TextView(getActivity());
            orgaNameText.setText(OrganizationNames.get(i));
            orgaNameText.setTextColor(Color.rgb(90, 90, 90));
            orgaNameText.setTextSize(15);

            TextView mileageText = new TextView(getActivity());
            mileageText.setText(ItemPrice.get(i));
            mileageText.setTextColor(Color.rgb(230, 173, 95));
            mileageText.setTextSize(18);

            // 수평 레이아웃에 텍스트 뷰 추가
            ItemNameWrapper.addView(itemNameText);
            OrgaNameWrapper.addView(orgaNameText);
            MileageWrapper.addView(mileageText);
            OrgaNameWrapper.addView(MileageWrapper);

            // 수직 레이아웃에 텍스트 뷰 추가
            ItemContainer.addView(ItemNameWrapper);
            ItemContainer.addView(OrgaNameWrapper);

            ItemContainer.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    useMileage();
                }
            });

            ListContainer.addView(ItemContainer);
        }
    }

    void useMileage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("마일리지 사용");
        builder.setMessage("물품을 수령하였습니까?");
        builder.setPositiveButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.show();
    }
}
