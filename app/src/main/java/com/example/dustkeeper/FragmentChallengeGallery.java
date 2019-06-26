package com.example.dustkeeper;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentChallengeGallery extends Fragment {
    private LinearLayout ListContainer;
    private ArrayList<String> ChallengeNames;
    RequestQueue mQueue;
    @Override
    public void onCreate(Bundle savedInstancState) {
        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_gallery, container, false);
        mQueue= Volley.newRequestQueue(getActivity());
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
        String url = "http://pms9116.cafe24.com/dictionary.php";
        StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(), "성공" + response, Toast.LENGTH_SHORT).show();

                        try {
                            JSONArray arr = new JSONArray(response);
                            //Toast.makeText(getActivity(), String.valueOf(arr.length()), Toast.LENGTH_SHORT).show();

                            for (int i=0; i<arr.length(); i++) {
                                JSONObject result = arr.getJSONObject(i);
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
                                final String imageData = result.getString("pic");
                                byte[] imageBytes = Base64.decode(imageData, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                ImageView Image = new ImageView(getActivity());
                                Image.setImageBitmap(decodedImage);
                                Image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
                                Image.setPadding(20,20,20,10);

                                // 텍스트 뷰 생성
                                final String nameData = result.getString("name");
                                TextView nameText = new TextView(getActivity());
                                nameText.setText(nameData);
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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
        {

        };
        /*
        final JsonArrayRequest request=new JsonArrayRequest(Request.Method.POST, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Toast.makeText(getActivity(),response.toString(),Toast.LENGTH_SHORT).show();

                        JSONArray arr=null;
                        try {
                            arr=response;
                            for (int i=0; i<arr.length(); i++) {
                                JSONObject result = arr.getJSONObject(i);
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
                                final String imageData = result.getString("pic");
                                byte[] imageBytes = Base64.decode(imageData, Base64.DEFAULT);
                                Bitmap decodedImage = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                                ImageView Image = new ImageView(getActivity());
                                Image.setImageBitmap(decodedImage);
                                Image.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 1000));
                                Image.setPadding(20,20,20,10);

                                // 텍스트 뷰 생성
                                final String nameData = result.getString("name");
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

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getActivity(),e.toString(),Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });*/
        mQueue.add(request);

    }
}
