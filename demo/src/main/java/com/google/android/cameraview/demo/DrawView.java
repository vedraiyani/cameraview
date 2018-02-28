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

package com.google.android.cameraview.demo;

/**
 * Created by shreyas on 20/2/18.
 */

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class DrawView extends View {
    Paint paint = new Paint();

    private void init() {
        paint.setColor(Color.CYAN);
        paint.setAlpha(70);
    }

    public DrawView(Context context) {
        super(context);
        init();
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DrawView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    @Override
    public void onDraw(Canvas canvas) {

        final int viewHeight = getHeight();
        final int viewWidth = getWidth();

        final int displacementFromBorder = 10;
        final int squareLength = Integer.valueOf(viewHeight/7);

        Log.i("DrawView Height x Width", viewHeight + " x " + viewWidth);

        Rect rect = new Rect();

        /**
         * left side square are drawn first and then right side are drawn using offset to their respective left side square
         */
        //top left
        rect.set(displacementFromBorder, displacementFromBorder, squareLength, squareLength);
        canvas.drawRect(rect, paint);

        //top right
        rect.offset(viewWidth-displacementFromBorder-squareLength, 0);
        canvas.drawRect(rect, paint);

        //bottom left
        rect.set(displacementFromBorder, viewHeight-squareLength, displacementFromBorder + squareLength, viewHeight);
        canvas.drawRect(rect, paint);

        //bottom right
        rect.offset(viewWidth-displacementFromBorder-squareLength, 0);
        canvas.drawRect(rect, paint);
    }
}