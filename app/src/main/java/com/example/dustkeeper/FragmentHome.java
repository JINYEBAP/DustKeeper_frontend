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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class FragmentHome extends Fragment {
    public final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 1;
    private boolean isGPSEnabled;
    private boolean isNetworkEnabled;
    ImageView image_cloud;
    TextView string_state;
    TextView dustDegree;
    TextView ult_dustDegree;
    TextView string_location;
    String gps_location;
    int degree;
    int ult_degree;
    RequestQueue mQueue;


    @Override
    public void onCreate(Bundle savedInstancState) {

        super.onCreate(savedInstancState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        string_location = (TextView) view.findViewById(R.id.string_location);

        image_cloud = (ImageView) view.findViewById(R.id.image_cloud);
        string_state = (TextView) view.findViewById(R.id.string_state);
        dustDegree = (TextView) view.findViewById(R.id.dustDegree);
        ult_dustDegree = (TextView) view.findViewById(R.id.ult_dustDegree);


        /*gps 위치와 비교해서 현재 위치 string 출력*/
        gps_location = new String(" ");
        //String gps_location = new String(""); //GPS 받아오기
        string_location.setText("위치 불러오는 중");

        /*농도 받아와서 상태, 농도 출력*/
        degree = 0; //DB에서 받아오기
        ult_degree = 0;//DB에서 받아오기

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
        string_location.setText("서울시 " + gps_location);
        final String location = gps_location;
        mQueue= Volley.newRequestQueue(getActivity());
        String url="http://pms9116.cafe24.com/dust.php";
        StringRequest request=new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(getActivity(),"성공" + response ,Toast.LENGTH_SHORT).show();
                        try {
                            JSONArray arr = new JSONArray(response);
                            JSONObject obj = arr.getJSONObject(0);
                            degree =  Integer.parseInt(obj.getString("dust"));
                            ult_degree =  Integer.parseInt(obj.getString("ultra_dust"));

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
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
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
                params.put("location",location);
                return params;
            }

        };
        mQueue.add(request);
    }
}
