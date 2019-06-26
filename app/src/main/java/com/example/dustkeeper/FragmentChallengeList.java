package com.example.dustkeeper;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

public class FragmentChallengeList extends Fragment {
    private LinearLayout ListContainer;
    private ArrayList<LinearLayout> ChallengeLayout;
    private ArrayList<String> ChallengeNames;

    // 사진 촬영, 저장을 위한 변수
    private int RESULT_OK = -1;

    public static final String FILE_NAME = "image.jpg";
    public static final int CAMERA_PERMISSIONS_REQUEST = 2;
    public static final int CAMERA_IMAGE_REQUEST = 3;
    private static final int MAX_DIMENSION = 1200;

    @Override
    public void onCreate(Bundle savedInstancState) {

        super.onCreate(savedInstancState);

    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_challenge_list, container, false);

        ListContainer = (LinearLayout) view.findViewById(R.id.challengeListContainer);
        ChallengeLayout = new ArrayList<LinearLayout>();

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

            ChallengeContainer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startCamera();
                }
            });

            ChallengeLayout.add(ChallengeContainer);
        }
        for(Iterator<LinearLayout>itr=ChallengeLayout.iterator(); itr.hasNext();) {
            ListContainer.addView(itr.next());
        }
    }
    public void startCamera() {

        if (PermissionUtils.requestPermission(
                getActivity(),
                CAMERA_PERMISSIONS_REQUEST,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.CAMERA)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), getCameraFile());
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivityForResult(intent, CAMERA_IMAGE_REQUEST);
        }
    }

    public File getCameraFile() {
        File dir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return new File(dir, FILE_NAME);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_IMAGE_REQUEST && resultCode == RESULT_OK) {

            Uri photoUri = FileProvider.getUriForFile(getActivity(), getActivity().getPackageName(), getCameraFile());
            uploadImage(photoUri);
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST:
                if (PermissionUtils.permissionGranted(requestCode, CAMERA_PERMISSIONS_REQUEST, grantResults)) {
                    startCamera();
                }
                break;
        }
    }

    public void uploadImage(Uri uri) {
        if (uri != null) {
            try {
                ChallengeLayout.clear();
                ListContainer.removeAllViews();
                // scale the image to save on bandwidth
                Bitmap bitmap =
                        scaleBitmapDown(
                                MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uri),
                                MAX_DIMENSION);

                // 미리보기 이미지
                ImageView preview = new ImageView(getActivity());
                preview.setImageBitmap(bitmap);
                preview.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ListContainer.addView(preview);

                // 텍스트
                LinearLayout questionWrapper = new LinearLayout(getActivity());
                questionWrapper.setOrientation(LinearLayout.HORIZONTAL);
                questionWrapper.setVerticalGravity(Gravity.CENTER_VERTICAL);
                questionWrapper.setGravity(Gravity.CENTER_HORIZONTAL);

                TextView question = new TextView(getActivity());
                question.setText("사진을 업로드 하시겠습니까?");
                question.setTextSize(20);
                questionWrapper.addView(question);
                ListContainer.addView(questionWrapper);

                // 버튼
                LinearLayout buttonWrapper = new LinearLayout(getActivity());
                buttonWrapper.setOrientation(LinearLayout.HORIZONTAL);
                buttonWrapper.setVerticalGravity(Gravity.CENTER_VERTICAL);
                buttonWrapper.setGravity(Gravity.CENTER_HORIZONTAL);
                LinearLayout.LayoutParams bParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                bParams.setMargins(50,50,50,80);
                buttonWrapper.setLayoutParams(bParams);

                LinearLayout noTextWrapper = new LinearLayout(getActivity());
                noTextWrapper.setOrientation(LinearLayout.HORIZONTAL);
                noTextWrapper.setGravity(Gravity.CENTER_HORIZONTAL);
                LinearLayout.LayoutParams Params1 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                Params1.setMargins(50,0,50,0);
                noTextWrapper.setLayoutParams(Params1);
                TextView noText = new TextView(getActivity());
                noText.setText("취소");
                noText.setBackgroundResource(R.drawable.rounded_corner);
                noText.setPadding(100,50,100,50);
                noTextWrapper.addView(noText);

                LinearLayout yesTextWrapper = new LinearLayout(getActivity());
                yesTextWrapper.setOrientation(LinearLayout.HORIZONTAL);
                yesTextWrapper.setGravity(Gravity.CENTER_HORIZONTAL);
                LinearLayout.LayoutParams Params2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
                Params2.setMargins(50,0,50,0);
                yesTextWrapper.setLayoutParams(Params2);
                TextView yesText = new TextView(getActivity());
                yesText.setText("확인");
                yesText.setBackgroundResource(R.drawable.rounded_corner);
                yesText.setPadding(100,50,100,50);
                yesTextWrapper.addView(yesText);

                buttonWrapper.addView(noTextWrapper);
                buttonWrapper.addView(yesTextWrapper);

                ListContainer.addView(buttonWrapper);

            } catch (IOException e) {
                Log.d("Debug", "이미지 불러오기 실패");
            }
        } else {
            Log.d("Debug", "이미지가 없습니다.");
        }
    }

    private Bitmap scaleBitmapDown(Bitmap bitmap, int maxDimension) {

        int originalWidth = bitmap.getWidth();
        int originalHeight = bitmap.getHeight();
        int resizedWidth = maxDimension;
        int resizedHeight = maxDimension;

        if (originalHeight > originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = (int) (resizedHeight * (float) originalWidth / (float) originalHeight);
        } else if (originalWidth > originalHeight) {
            resizedWidth = maxDimension;
            resizedHeight = (int) (resizedWidth * (float) originalHeight / (float) originalWidth);
        } else if (originalHeight == originalWidth) {
            resizedHeight = maxDimension;
            resizedWidth = maxDimension;
        }
        return Bitmap.createScaledBitmap(bitmap, resizedWidth, resizedHeight, false);
    }
}
