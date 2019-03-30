package com.medici.stack.util;

import android.graphics.PointF;
import android.support.annotation.ColorInt;

/**
 * @desc 几何图形工具
 */
public class GeometryUtil {

	private GeometryUtil(){

	}
	
	/**
	 * As meaning of method name.
	 * 获得两点之间的距离
	 * @return distance
	 */
	public static float getDistanceBetween2Points(PointF p0, PointF p1) {
		float distance = (float) Math.sqrt(Math.pow(p0.y - p1.y, 2) + Math.pow(p0.x - p1.x, 2));
		return distance;
	}
	
	/**
	 * Get middle point between p1 and p2.
	 * 获得两点连线的中点
	 * @return PointF
	 */
	public static PointF getMiddlePoint(PointF p1, PointF p2) {
		return new PointF((p1.x + p2.x) / 2.0f, (p1.y + p2.y) / 2.0f);
	}
	
	/**
	 * Get point between p1 and p2 by percent.
	 * 根据百分比获取两点之间的某个点坐标
	 * @return PointF
	 */
	public static PointF getPointByPercent(PointF p1, PointF p2, float percent) {
		return new PointF(evaluateValue(percent, p1.x , p2.x), evaluateValue(percent, p1.y , p2.y));
	}
	
	/**
	 * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
	 * @return float
	 */
	public static float evaluateValue(float fraction, Number start, Number end){
		return start.floatValue() + (end.floatValue() - start.floatValue()) * fraction;
	}
	
	/**
	 * 根据分度值，计算从start到end中，fraction位置的值。fraction范围为0 -> 1
	 * @return color值
	 */
	@ColorInt
	public int evaluateColor(float fraction, @ColorInt int startValue, @ColorInt int endValue) {
        int startA = (startValue >> 24) & 0xff;
        int startR = (startValue >> 16) & 0xff;
        int startG = (startValue >> 8) & 0xff;
        int startB = startValue & 0xff;

        int endA = (endValue >> 24) & 0xff;
        int endR = (endValue >> 16) & 0xff;
        int endG = (endValue >> 8) & 0xff;
        int endB = endValue & 0xff;

        return (int)((startA + (int)(fraction * (endA - startA))) << 24) |
                (int)((startR + (int)(fraction * (endR - startR))) << 16) |
                (int)((startG + (int)(fraction * (endG - startG))) << 8) |
                (int)((startB + (int)(fraction * (endB - startB))));
	}
	
	/**
	 * Get the point of intersection between circle and line.
	 * 获取 通过指定圆心，斜率为lineK的直线与圆的交点。
	 * @return PointF[]
	 */
	public static PointF[] getIntersectionPoints(PointF pMiddle, float radius, Double lineK) {
		PointF[] points = new PointF[2];
		
		float radian, xOffset = 0, yOffset = 0; 
		if(lineK != null){
			radian= (float) Math.atan(lineK);
			xOffset = (float) (Math.sin(radian) * radius);
			yOffset = (float) (Math.cos(radian) * radius);
		}else {
			xOffset = radius;
			yOffset = 0;
		}
		points[0] = new PointF(pMiddle.x + xOffset, pMiddle.y - yOffset);
		points[1] = new PointF(pMiddle.x - xOffset, pMiddle.y + yOffset);
		
		return points;
	}
}