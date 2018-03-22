/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.cameraview;

/**
 * Created by shreyas on 20/2/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawCircle extends View {
    Paint paint = new Paint();
    int cx, cy;
    int radius;
    Circle[] mCircles;

    private void init() {
        paint.setColor(Color.YELLOW);
        paint.setAlpha(80);
    }

    public DrawCircle(Context context) {
        super(context);
        init();
    }

    public DrawCircle(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawCircle(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if(mCircles != null){
            for (int i=0; i < mCircles.length; i++){
                cx = (int) mCircles[i].getCx();
                cy = (int) mCircles[i].getCy();
                radius = mCircles[i].getRadius();
                canvas.drawCircle(cx, cy, radius, paint);
                Log.i("DrawCircle",cx+","+cy+","+radius);
            }
        }
    }

    public Circle[] getCircles() {
        return mCircles;
    }

    public void setCircles(Circle[] circles) {
        mCircles = circles;
    }
}