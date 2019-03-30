package com.medici.stack.util;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.SearchView;
import android.widget.TextView;

import com.medici.stack.util.blankj.SizeUtil;

import java.lang.reflect.Field;


/**
 * @desc Drawable工具类
 */
public final class DrawableUtil {

	private DrawableUtil(){}

	/**
	 * 创建背景图片
	 * @param contentColor 内部填充颜色
	 * @param strokeColor  描边颜色
	 * @param radius       圆角
	 */
	public static GradientDrawable createDrawable(int contentColor, int strokeColor, int radius) {
		// 生成Shape
		GradientDrawable drawable = new GradientDrawable(); 
		// 设置矩形
		drawable.setGradientType(GradientDrawable.LINEAR_GRADIENT);
		// 内容区域的颜色
		drawable.setColor(contentColor);
		// 四周描边,描边后四角真正为圆角，不会出现黑色阴影,如果父窗体是可以滑动的,就把父View设置setScrollCache(false)
		drawable.setStroke(1, strokeColor);
		// 设置四角都为圆角
		drawable.setCornerRadius(radius); 
		return drawable;
	}

	/**
	 * 通过反射机制 清除底部横线
	 * @param mSearchView
	 */
	public static void clearViewUnderline(@NonNull SearchView mSearchView){
		Class<?> c = mSearchView.getClass();
		try {
			// 通过反射，获得类对象的一个属性对象
			Field f = c.getDeclaredField("mSearchPlate");
			// 设置此私有属性是可访问的 暴力翻身
			f.setAccessible(true);
			// 获得属性的值
			View v = (View) f.get(mSearchView);
			// 设置此view的背景
			v.setBackgroundColor(Color.TRANSPARENT);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建背景图片选择器
	 * @param normalState  普通状态的图片
	 * @param pressedState 按压状态的图片
	 */
	public static StateListDrawable createSelector(Drawable normalState, Drawable pressedState) {
		StateListDrawable bg = new StateListDrawable();
		bg.addState(new int[]{android.R.attr.state_pressed, android.R.attr.state_enabled}, pressedState);
		bg.addState(new int[]{android.R.attr.state_enabled}, normalState);
		bg.addState(new int[]{}, normalState);
		return bg;
	}

	/**
	 * 获取Drawable 对象的占内存大小
	 * @param drawable
	 * @return
	 */
	public static int getDrawableSize(Drawable drawable) {
		if (drawable == null) {
			return 0;
		}
		Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
			return bitmap.getByteCount();
		} else {
			return bitmap.getRowBytes() * bitmap.getHeight();
		}
	}
	
	/**
	 * 将矩形图片变成有圆角的图片并显示
	 * @param bitmap
	 * @param px 圆角大小
	 * @return
	 */
	public static Bitmap makeRoundCorner(Bitmap bitmap, int px){
		  int width = bitmap.getWidth(); 
		  int height = bitmap.getHeight(); 
		  Bitmap output = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888); 
		  Canvas canvas = new Canvas(output); 
		  int color = 0xff424242; 
		  Paint paint = new Paint(); 
		  Rect rect = new Rect(0, 0, width, height); 
		  RectF rectF = new RectF(rect); 
		  paint.setAntiAlias(true); 
		  canvas.drawARGB(0, 0, 0, 0); 
		  paint.setColor(color); 
		  canvas.drawRoundRect(rectF, px, px, paint); 
		  paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN)); 
		  canvas.drawBitmap(bitmap, rect, rect, paint); 
		  return output; 
	}
	
	/**
	 * 动态设置继承TextView的四周Drawable的大小
	 * @param view 继承TextView的大小
	 * @param dipSize 设置的大小
	 * @param direction 方向
	 */
    public static void initTextDrawableSize(TextView view, int dipSize, Direction direction){
    	//通过mSearchEt的getCompoundDrawables()方法，拿到图片的drawables,分别是左上右下的图片
        Drawable[] drawables = view.getCompoundDrawables();
        switch (direction) {
		case left:
			drawables[0].setBounds(0,0, SizeUtil.dp2px(dipSize), SizeUtil.dp2px(dipSize));
			view.setCompoundDrawables(drawables[0],null,null,null);
			break;
		case top:
			drawables[1].setBounds(0,0, SizeUtil.dp2px(dipSize), SizeUtil.dp2px(dipSize));
			view.setCompoundDrawables(null,drawables[1],null,null);
			break;
		case right:
			drawables[2].setBounds(0,0, SizeUtil.dp2px(dipSize), SizeUtil.dp2px(dipSize));
			view.setCompoundDrawables(null,null,drawables[2],null);
			break;
		case bottom:
			drawables[3].setBounds(0,0, SizeUtil.dp2px(dipSize), SizeUtil.dp2px(dipSize));
			view.setCompoundDrawables(null,null,null,drawables[3]);
			break;
		}
    }

	public enum Direction{
		left,top,right,bottom
	}
}
