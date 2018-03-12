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

import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Unknown on 13-Mar-18.
 */

public class DrawCirclePreview {
    private final DrawCircle mDrawCircle;

    public DrawCirclePreview(Context context, ViewGroup parent) {
        final View view;
        if (Build.VERSION.SDK_INT < 14) {
            view = View.inflate(context, R.layout.surface_view, parent);
            mDrawCircle = (DrawCircle) view.findViewById(R.id.viewDrawCircleSurface);
        } else {
             view = View.inflate(context, R.layout.texture_view, parent);
            mDrawCircle = (DrawCircle) view.findViewById(R.id.viewDrawCircleTexture);
        }
    }

    public DrawCircle getDrawCircle() {
        return mDrawCircle;
    }
}
