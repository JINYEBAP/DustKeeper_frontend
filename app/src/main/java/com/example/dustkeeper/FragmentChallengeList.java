package com.example.dustkeeper;

import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

public class FragmentChallengeList extends Fragment {
    private LinearLayout ListContainer;
    private ArrayList<String> ChallengeNames;

    @Override
    public void onCreate(Bundle savedInstancState) {
        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_list, container, false);

        ListContainer = (LinearLayout) view.findViewById(R.id.challengeListContainer);

        ChallengeNames = new ArrayList<String>();
        ChallengeNames.add("물 마시기");
        ChallengeNames.add("대중교통 이용");
        ChallengeNames.add("자전거 이용");
        ChallengeNames.add("과일 먹기");
        ChallengeNames.add("손 씻기");

        //Toast.makeText(getActivity(),String.valueOf(ChallengeNames.size()),Toast.LENGTH_SHORT).show();
        showChallengeList();

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

    ImageView getCameraImage() {
        // 이미지 생성
        ImageView CameraImage = new ImageView(getActivity());
        CameraImage.setLayoutParams(new LinearLayout.LayoutParams(200, 200));

        // 패딩 설정
        CameraImage.setPadding(30,30,30,30);

        // 아이콘 설정
        CameraImage.setImageDrawable(getResources().getDrawable(R.drawable.ic_action_camera));

        return CameraImage;
    }

    void showChallengeList() {
       for (int i=0; i<ChallengeNames.size(); i++) {

            // 수평 레이아웃 생성
            LinearLayout ChallengeContainer = new LinearLayout(getActivity());
            ChallengeContainer.setOrientation(LinearLayout.HORIZONTAL);
            ChallengeContainer = setCustomLinearLayout(ChallengeContainer);

            // 텍스트 뷰 생성
            TextView nameText = new TextView(getActivity());
            nameText.setText(ChallengeNames.get(i));
            nameText.setTextColor(Color.rgb(90, 90, 90));
            nameText.setTextSize(15);

            // 카메라 아이콘 컨테이너 생성
            LinearLayout ImageContainer = new LinearLayout(getActivity());
            ImageContainer.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayout.LayoutParams Params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
            ImageContainer.setLayoutParams(Params);
            ImageContainer.setGravity(Gravity.RIGHT);

            // 카메라 아이콘 생성
            ImageView CameraImage = getCameraImage();
            ImageContainer.addView(CameraImage);

            // 수평 레이아웃에 텍스트 뷰 추가
            ChallengeContainer.addView(nameText);
            ChallengeContainer.addView(ImageContainer);

            ListContainer.addView(ChallengeContainer);
        }
    }
}
