package com.example.shareplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class ClassListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_list);

        // 현재 로그인된 유저의 UID는 로그인시 Intent를 통해서 넘어옴.
        // 유저별 강의 데이터베이스에서, 로그인한 유저의 UID를 통해 유저가 가지고 있는 강의 UID들을 가져옴.
        // 강의 데이터베이스에서, 유저가 가지고있는 강의 UID를 통해 각 강의의 정보를 가져옴.
        // 각 강의의 정보들을 다듬어서, ListView의 adapter에 추가해줌.
        // 이 목록은, 유저의 강의 목록이 업데이트 될 때마다 계속 갱신되어야 하므로,
        // get()이나 addListenerForSingleValueEvent가 아니라, addValueEventListener를 사용하는 것이 좋음.
        // 데이터 읽기 및 쓰기의 자세한 방법은 아래 링크 참고
        // https://firebase.google.com/docs/database/android/read-and-write?hl=ko#java_4

        // "+" 버튼 클릭 시, 강의 추가 액티비티로 넘어가도록 onClickListener를 구현해야함.
        // 강의 추가/생성 자체는 강의 추가/생성 액티비티에서 이루어지므로, 콜백 함수는 구현하지 않아도 됨.
        // 다만, 유저의 UID를 통해 권한을 받아와야 함.
        // 받아온 권한에 따라, SearchLec으로 보내야 할지, CreateLec으로 보내야 할지를 결정.
        // 이 때에는 버튼이 클릭될 시에만 실행되어야 하므로,
        // get()이나, addListenerForSingleValueEvent를 사용하는 것이 좋음.
    }
}