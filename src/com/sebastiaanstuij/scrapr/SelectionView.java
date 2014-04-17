package com.sebastiaanstuij.scrapr;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;


public class SelectionView extends View {

    Paint paint = new Paint();
	float X1, Y1, X2, Y2;
	boolean mDrag;
	float mDragX, mDragY;
	float mDraggedX, mDraggedY;
	
	public SelectionView(Context context, AttributeSet attrs) {
		super(context, attrs);
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(3);
        paint.setStyle(Style.STROKE);
	}

	
	//This method is called each time Android wants to draw a new screen
	//Method is overridden with a custom draw Rectangle operation which is provided with coordinates from the OnTouchEvent
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
	    //Draw a rectangle with acquired coordinates                            
        canvas.drawRect(Math.min(X1, X2) - mDragX + mDraggedX, Math.min(Y1, Y2) - mDragY + mDraggedY, Math.max(X1, X2) - mDragX + mDraggedX, Math.max(Y1, Y2) - mDragY + mDraggedY, paint);
	}
		
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO check which finger is being used when there are multiple fingers touching the screen
		//When the user touches the screen after the selection button has been pressed this method is fired
		//Check whether the finger was an 'action down' action
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			//get X and Y coordinates from 1st finger (0)
			float X = event.getX(0);
			float Y = event.getY(0);
			
			//If new X coordinate lies between X1 and X2 (same for Y) then the user is busy dragging the window
			if (X > Math.min(X1, X2) && X < Math.max(X1, X2) && Y > Math.min(Y1, Y2) && Y < Math.max(Y1, Y2)) {
				mDrag = true;
				mDragX = X;
				mDragY = Y;
				mDraggedX = X;
				mDraggedY = Y;
			} else {
				X1 = X;
				X2 = X;
				Y1 = Y;
				Y2 = Y;
			}			
		} else if (event.getAction() == MotionEvent.ACTION_MOVE) {
			float X = event.getX(0);
			float Y = event.getY(0);
			
			if (mDrag) {
				mDraggedX = X;
				mDraggedY = Y;
			} else {
				X2 = X;
				Y2 = Y;			
			}
		} else if (event.getAction() == MotionEvent.ACTION_UP) {
			mDrag = false;
	        X1 = X1 - mDragX + mDraggedX;
	        X2 = X2 - mDragX + mDraggedX;
	        
	        Y1 = Y1 - mDragY + mDraggedY;
	        Y2 = Y2 - mDragY + mDraggedY;

			mDragX = 0;
			mDraggedX = 0;
			mDragY = 0;
			mDraggedY = 0;
		}
		
		//Call invalidate so that android redraws screen by calling onDraw()
		invalidate();
		
		return true;
	}
	
	public Rect getSelection(){
		Rect rect = new Rect();
		rect.set((int)(Math.min(X1, X2)), (int)(Math.min(Y1, Y2)), (int)(Math.max(X1, X2)), (int)(Math.max(Y1, Y2)));
		return rect;
	}
		
}
