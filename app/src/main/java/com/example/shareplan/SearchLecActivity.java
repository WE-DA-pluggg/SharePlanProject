package com.example.shareplan;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SearchLecActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseRef;
    private String uid = "";
    private String stuNum = "";
    private String stuName = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lec);

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("SharePlan");

        Intent userIntent = getIntent();
        uid = userIntent.getStringExtra("UserUID");

        mDatabaseRef.child("UserInfo").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserInfo userInfo = snapshot.getValue(UserInfo.class);
                stuNum = userInfo.getStunum();
                stuName = userInfo.getName();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        EditText lecName = findViewById(R.id.lec_name);
        Button search = findViewById(R.id.search);

        ListView listView = (ListView) findViewById(R.id.search_listView);

        ListViewAdapter adapter = new ListViewAdapter(getApplicationContext());
        listView.setAdapter(adapter);

        // ?????? ?????? ?????? ??? ????????? ?????? ????????????????????? ?????? ????????? UID ??????
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // ??????????????? ??????
                AlertDialog.Builder builder = new AlertDialog.Builder(SearchLecActivity.this);
                builder.setTitle("??????");
                builder.setMessage("??? ????????? ?????????????????????????");
                builder.setIcon(android.R.drawable.ic_dialog_info);

                builder.setPositiveButton("?????????", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                // ??? ?????? ?????? ??? ?????? ??????
                builder.setNegativeButton("???", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // ????????????????????????????????? ??? ??????
                        mDatabaseRef.child("LectureInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                LectureInfo touchLec = adapter.getItem(position);
                                for(DataSnapshot lectureData : snapshot.getChildren()) {
                                    String lecUID = lectureData.getKey();
                                    LectureInfo lecture = lectureData.getValue(LectureInfo.class);
                                    if(lecture.getName().equals(touchLec.getName()) && lecture.getProfessor().equals(touchLec.getProfessor()) &&
                                            lecture.getDivision().equals(touchLec.getDivision()) && lecture.getDay().equals(touchLec.getDay()) &&
                                            lecture.getTime().equals(touchLec.getTime())) {
                                        // UserLectureInfo ????????? ?????? ????????? ????????? Lecture??? UID??? ?????????
                                        ArrayList<String> lecUIDs = new ArrayList<>();
                                        mDatabaseRef.child("ClassInfo").child(lecUID).addListenerForSingleValueEvent(new ValueEventListener() {
                                            @Override
                                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                boolean registerLec = false;
                                                for(DataSnapshot userData : snapshot.getChildren()) {
                                                    String findNum = userData.getKey();
                                                    String findName = userData.getValue(String.class);
                                                    if (findNum.equals(stuNum) && findName.equals(stuName)) {
                                                        registerLec = true;
                                                        break;
                                                    }
                                                }
                                                if (registerLec) {
                                                    mDatabaseRef.child("UserLectureInfo").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
                                                        @Override
                                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                                            // ?????? ?????? ?????? UID ???????????? ????????? ???, ??????????????? ?????? ????????? UID??? ???????????? ????????????.
                                                            boolean isExist = false;
                                                            for(DataSnapshot lecUIDSet : snapshot.getChildren()) {
                                                                String lecUidExist = lecUIDSet.getValue(String.class);
                                                                lecUIDs.add(lecUidExist);
                                                                if(lecUidExist.equals(lecUID))
                                                                    isExist = true;
                                                            }
                                                            if(isExist) {
                                                                Toast.makeText(SearchLecActivity.this, "?????? ????????? ???????????????.", Toast.LENGTH_SHORT).show();
                                                            } else {
                                                                lecUIDs.add(lecUID);
                                                                mDatabaseRef.child("UserLectureInfo").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(lecUIDs);
                                                                finish();
                                                            }
                                                        }

                                                        @Override
                                                        public void onCancelled(@NonNull DatabaseError error) {

                                                        }
                                                    });
                                                } else {
                                                    Toast.makeText(SearchLecActivity.this, "???????????? ????????? ????????????.", Toast.LENGTH_SHORT).show();
                                                }
                                            }

                                            @Override
                                            public void onCancelled(@NonNull DatabaseError error) {

                                            }
                                        });
                                        break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // ?????? ?????? ?????? ??? ???????????? ?????? ???????????? ???????????? ?????? ???????????? ??????????????? ?????????
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                adapter.clear();
                String name = lecName.getText().toString();
                mDatabaseRef.child("LectureInfo").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot lectureData : snapshot.getChildren()) {
                            LectureInfo lecture = lectureData.getValue(LectureInfo.class);
                            if(lecture.getName().contains(name)) {
                                adapter.addItem(lecture);
                            }
                        }
                        listView.setAdapter(adapter);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });

        // ?????? ??????????????? ?????? ?????? ???????????? ??????????????? ???????????? ?????????
        mDatabaseRef.child("LectureInfo").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                adapter.clear();
                for(DataSnapshot lectureData : snapshot.getChildren()) {
                    LectureInfo lecture = lectureData.getValue(LectureInfo.class);
                    adapter.addItem(lecture);
                }
                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // onCreate??? ???????????? ??? ??????
        // 1. SearchLecActivity ??? ????????? ??????????????? FireBase ?????? ????????????????????? ?????? ?????? ?????? ??????????????? ?????????
        // ListView??? adapter??? ????????????.
        // ??????????????? ???????????? ??? ????????? ?????????, addValueEventListener??? ??????
        // get()?????? addListenerForSingleValueEvent??? ???????????? ???????????? ?????? ????????????.
        // ????????? ?????? ??? ????????? ????????? ????????? ?????? ?????? ??????
        // https://firebase.google.com/docs/database/android/read-and-write?hl=ko#java_4

        // ????????????????????? ?????? ?????? ListView??? ????????? ??????????????? ???????????? ?????? ????????? ????????? ????????? ??? ??? ?????????,
        // ListView??? ??? ???????????? ???????????? ??????????????? ??????????????? ????????? ?????????
        // adapter.addItem("????????? ???????????????", "?????????/2??????/?????? 10:30-12:00");

        // 2. "??????" ?????? ?????? ??? ????????? ????????? ???????????? ?????? onClickListener ??????

    }

    class ListViewAdapter extends BaseAdapter {

        Context mContext = null;
        LayoutInflater mLayoutInflater = null;
        ArrayList<LectureInfo> items = null;

        public ListViewAdapter(Context context) {
            mContext = context;
            mLayoutInflater = LayoutInflater.from(mContext);
            items = new ArrayList<>();
        }

        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public LectureInfo getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = mLayoutInflater.inflate(R.layout.lec_item, null);

            TextView lecName = (TextView)view.findViewById(R.id.lec_item_name);
            TextView lecInfo = (TextView)view.findViewById(R.id.lec_item_info);

            LectureInfo item = items.get(position);
            lecName.setText(item.getName());
            String desc = item.getProfessor() + " / " + item.getDivision() + " / " + item.getDay() + " / " + item.getTime();
            lecInfo.setText(desc);

            return view;
        }

        public void addItem(LectureInfo item) {
            items.add(item);
        }

        public void clear() {
            items = new ArrayList<LectureInfo>();
        }
    }

    // 3. onClickListener ?????? ??? ????????? ?????? ?????? ??? ?????? ( onClickListener ?????? ???????????? ??? )
    // onCreate ?????? ??? ???????????? (1)??? ????????? ??????????????? FireBase ?????? ?????? ???????????????????????? ?????? ??????????????? ?????????
    // ??????, ?????? ????????? ???????????? ?????? ?????????, ?????? ???????????? "?????? ??????" ????????? ?????? EditText??? ???????????? ????????????
    // ????????? ???????????? ?????????
    // Ex) "???????????????" ?????? ??? --> "????????????????????????", "???????????????????????????", "???????????????????????????", etc...
    // adapter??? ????????? ??? ???, ???????????????????????? ????????? ????????? adapter??? ?????????.
    // ??????????????? ???????????? ???????????? ?????? ?????? ???????????? ????????? ???????????? ???????????? ?????????,
    // get()?????? addListenerForSingleValueEvent??? ???????????? ???????????? ?????? ????????????.
}
