package com.example.dustkeeper;

import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentChallengeGallery extends Fragment {
    private LinearLayout ListContainer;
    private ArrayList<String> ChallengeNames;

    @Override
    public void onCreate(Bundle savedInstancState) {
        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_gallery, container, false);

        ListContainer = (LinearLayout) view.findViewById(R.id.challengeGalleryContainer);

        ChallengeNames = new ArrayList<String>();
        ChallengeNames.add("물 마시기");
        ChallengeNames.add("물 마시기");
        ChallengeNames.add("과일 먹기");
        ChallengeNames.add("자전거 이용");

        showGallery();

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
        l.setPadding(50,50,30,50);

        // 마진 설정
        LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        Params.setMargins(50,0,50,100);
        l.setLayoutParams(Params);

        return l;
    }

    ImageView getAlertImage() {
        // 이미지 생성
        ImageView AlertImage = new ImageView(getActivity());
        AlertImage.setLayoutParams(new LinearLayout.LayoutParams(150, 150));

        // 패딩 설정
        AlertImage.setPadding(20,20,20,20);

        // 아이콘 설정
        AlertImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_alert));

        return AlertImage;
    }

    void showGallery() {
        for (int i=0; i<ChallengeNames.size(); i++) {

            // 수직 레이아웃 생성
            LinearLayout ChallengeContainer = new LinearLayout(getActivity());
            ChallengeContainer.setOrientation(LinearLayout.VERTICAL);
            ChallengeContainer = setCustomLinearLayout(ChallengeContainer);

            // 수평 레이아웃 생성
            LinearLayout ImageWrapper = new LinearLayout(getActivity());
            ImageWrapper.setOrientation(LinearLayout.HORIZONTAL);
            ImageWrapper.setGravity(Gravity.CENTER_HORIZONTAL);

            // 수평 레이아웃 생성
            LinearLayout ContentWrapper = new LinearLayout(getActivity());
            ContentWrapper.setOrientation(LinearLayout.HORIZONTAL);
            ContentWrapper.setVerticalGravity(Gravity.CENTER_VERTICAL);

            // 수평 레이아웃 생성
            LinearLayout AlertWrapper = new LinearLayout(getActivity());
            AlertWrapper.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            AlertWrapper.setLayoutParams(Params);
            AlertWrapper.setGravity(Gravity.RIGHT);

            // 이미지 생성
            ImageView Image = new ImageView(getActivity());
            Image.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera));
            Image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
            Image.setPadding(20,20,20,10);

            // 텍스트 뷰 생성
            TextView nameText = new TextView(getActivity());
            nameText.setText(ChallengeNames.get(i));
            nameText.setTextColor(Color.rgb(90, 90, 90));
            nameText.setTextSize(15);

            // 신고 아이콘 생성
            ImageView AlertImage = getAlertImage();
            AlertWrapper.addView(AlertImage);

            // 수평 레이아웃에 텍스트 뷰 추가
            ImageWrapper.addView(Image);
            ContentWrapper.addView(nameText);
            ContentWrapper.addView(AlertWrapper);
            ChallengeContainer.addView(ImageWrapper);
            ChallengeContainer.addView(ContentWrapper);

            ListContainer.addView(ChallengeContainer);
        }
    }
}
