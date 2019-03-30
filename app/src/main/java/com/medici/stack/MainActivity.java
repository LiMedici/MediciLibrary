package com.medici.stack;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.View;

import com.medici.stack.ui.SinglePixelActivity;
import com.medici.stack.ui.ToolbarActivity;
import com.medici.stack.util.blankj.ActivityUtil;

public class MainActivity extends ToolbarActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_main;
    }

    public void skip(@NonNull View view){
        Intent intent = new Intent(this,SinglePixelActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
