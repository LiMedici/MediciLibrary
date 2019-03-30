package com.medici.stack.ui;

import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;

import com.medici.stack.R;
import com.medici.stack.util.ScreenManager;
import com.medici.stack.util.UIUtil;
import com.orhanobut.logger.Logger;

/**
 * @desc 1像素的Activity
 */
public class SinglePixelActivity extends ToolbarActivity {

    @Override
    protected int getContentLayoutId() {
        return R.layout.activity_single_pixel;
    }

    @Override
    protected void initData() {

        // 获得activity的Window对象，设置其属性
        Window mWindow = getWindow();
        mWindow.setGravity(Gravity.LEFT | Gravity.TOP);
        WindowManager.LayoutParams attrParams = mWindow.getAttributes();
        attrParams.x = 0;
        attrParams.y = 0;
        attrParams.height = 1;
        attrParams.width = 1;
        mWindow.setAttributes(attrParams);
        // 绑定SinglePixelActivity到ScreenManager
        ScreenManager.getScreenManagerInstance(UIUtil.getContext()).setSingleActivity(this);
    }

    @Override
    protected void onDestroy() {
        Logger.w("%s ======= 1像素保活被终止",this.getClass().getCanonicalName());
        /** APP被干掉了，我要重启它
        if(!ActivityUtil.isActivityExistsInStack(this)){
            Intent intentAlive = new Intent(this, SplashActivity.class);
            startActivity(intentAlive);
            Log.i(TAG,"SinglePixelActivity---->APP被干掉了，我要重启它");
        }
        */
        super.onDestroy();
    }
}
