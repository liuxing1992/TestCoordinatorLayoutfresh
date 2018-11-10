package com.example.admin.testcoordinatorlayoutfresh;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void toMain(View view){
        startActivity(new Intent(this , MainActivity.class));
    }
    public void toRecycle(View view){
        startActivity(new Intent(this , RecycleActivity.class));
    }
    public void toDialog(View view){
        startActivity(new Intent(this , DialogActivity.class));
    }
}
