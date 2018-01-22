package com.zjz.myos.view;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zjz.myos.MainActivity;
import com.zjz.myos.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CreateJobActivity extends AppCompatActivity {

    @BindView(R.id.tb_job)
    Toolbar mTbJob;
    @BindView(R.id.et_job_name)
    EditText mEtJobName;
    @BindView(R.id.et_job_need)
    EditText mEtJobNeed;
    @BindView(R.id.et_job_big)
    EditText mEtJobBig;
    @BindView(R.id.btn_add_job)
    Button mBtnAddJob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_job);
        ButterKnife.bind(this);

        mTbJob.setTitle("添加作业");
        setSupportActionBar(mTbJob);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mTbJob.setNavigationOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
    }

    @OnClick(R.id.btn_add_job)
    public void onViewClicked() {
        if(mEtJobName.getText().toString().equals("")){
            showToast("请输入作业名");
            return;
        }
        try {
            if(Integer.parseInt(mEtJobNeed.getText().toString()) <= 0){
                showToast("请输入正确时间");
                return;
            }
        } catch (NumberFormatException e) {
            showToast("请输入正确时间格式（以s为单位）");
            e.printStackTrace();
            return;
        }

        try {
            if(Integer.parseInt(mEtJobBig.getText().toString()) <= 0 ||Integer.parseInt(mEtJobBig.getText().toString()) >200 ){
                showToast("请输入正确占用内存大小(>0 | <200)");
                return;
            }
        } catch (NumberFormatException e) {
            showToast("请输入正确占用内存大小(>0 | <200)");
            e.printStackTrace();
            return;
        }


        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("JobName" ,mEtJobName.getText().toString() );
        intent.putExtra("JobNeed",Integer.parseInt(mEtJobNeed.getText().toString()));
        intent.putExtra("JobBig",Integer.parseInt(mEtJobBig.getText().toString()));
        setResult(RESULT_OK,intent);
        finish();
    }


    private void showToast(String text) {
        Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
    }
}
