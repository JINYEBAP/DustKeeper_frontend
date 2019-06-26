package com.example.dustkeeper;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.dustkeeper.MainActivity;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FragmentHome extends Fragment {
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    String gps_location;
    RequestQueue mQueue;


    @Override
    public void onCreate(Bundle savedInstancState) {

        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        TextView string_location = (TextView) view.findViewById(R.id.string_location);

        ImageView image_cloud = (ImageView) view.findViewById(R.id.image_cloud);
        TextView string_state = (TextView) view.findViewById(R.id.string_state);
        TextView dustDegree = (TextView) view.findViewById(R.id.dustDegree);
        TextView ult_dustDegree = (TextView) view.findViewById(R.id.ult_dustDegree);

    /*
        mQueue= Volley.newRequestQueue(getActivity());
        String url="http://pms9116.cafe24.com/dust.php";
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(),"성공" + response ,Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        Toast.makeText(getActivity(),error.toString(),Toast.LENGTH_SHORT).show();
                    }
                })
        {

            @Override
            protected Map<String,String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("location","마포구");
                return params;
            }

        };
        mQueue.add(request);*/

        /*gps 위치와 비교해서 현재 위치 string 출력*/
        gps_location = new String("서울시 마포구");
        //String gps_location = new String(""); //GPS 받아오기

        switch(gps_location){
            case "강남구":
                string_location.setText("서울시 강남구");
                break;
            case "강동구":
                string_location.setText("서울시 강동구");
                break;
            case "강서구":
                string_location.setText("서울시 강서구");
                break;
            case "강북구":
                string_location.setText("서울시 강북구");
                break;
            case "관악구":
                string_location.setText("서울시 관악구");
                break;
            case "광진구":
                string_location.setText("서울시 광진구");
                break;
            case "구로구":
                string_location.setText("서울시 구로구");
                break;
            case "금천구":
                string_location.setText("서울시 금천구");
                break;
            case "노원구":
                string_location.setText("서울시 노원구");
                break;
            case "동대문구":
                string_location.setText("서울시 동대문구");
                break;
            case "도봉구":
                string_location.setText("서울시 도봉구");
                break;
            case "동작구":
                string_location.setText("서울시 동작구");
                break;
            case "마포구":
                string_location.setText("서울시 마포구");
                break;
            case "서대문구":
                string_location.setText("서울시 서대문구");
                break;
            case "성동구":
                string_location.setText("서울시 성동구");
                break;
            case "성북구":
                string_location.setText("서울시 성북구");
                break;
            case "서초구":
                string_location.setText("서울시 서초구");
                break;
            case "송파구":
                string_location.setText("서울시 송파구");
                break;
            case "영등포구":
                string_location.setText("서울시 영등포구");
                break;
            case "용산구":
                string_location.setText("서울시 용산구");
                break;
            case "양천구":
                string_location.setText("서울시 양천구");
                break;
            case "은평구":
                string_location.setText("서울시 은평구");
                break;
            case "종로구":
                string_location.setText("서울시 종로구");
                break;
            case "중구":
                string_location.setText("서울시 중구");
                break;
            case "중랑구":
                string_location.setText("서울시 중랑구");
                break;

        }

        /*농도 받아와서 상태, 농도 출력*/
        int degree = 36; //DB에서 받아오기
        int ult_degree = 30;//DB에서 받아오기

        if(degree>=0 && degree<=30){
            image_cloud.setImageDrawable(getResources().getDrawable(R.drawable.home_cloud_good));
            string_state.setText(R.string.state_good);
        }//좋음

        else if(degree<= 80){
            image_cloud.setImageDrawable(getResources().getDrawable(R.drawable.home_cloud_normal));
            string_state.setText(R.string.state_normal);
        }//보통

        else if(degree<= 150){
            image_cloud.setImageDrawable(getResources().getDrawable(R.drawable.home_cloud_bad));
            string_state.setText(R.string.state_bad);
        }//나쁨

        else{
            image_cloud.setImageDrawable(getResources().getDrawable(R.drawable.home_cloud_very_bad));
            string_state.setText(R.string.state_very_bad);
        }//매우 나쁨

        dustDegree.setText(degree + "㎍/m³");
        ult_dustDegree.setText(ult_degree + "㎍/m³");
        return view;
    }

    public void setGpsLocation(String str){
        gps_location = new String(str);
    }
}
