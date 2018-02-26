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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.io.File;

public class OMRSheetDisplay extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omrsheet_display);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"picture.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

        Mat matImage = new Mat(1280, 960, CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,matImage);

        matImage = detectCircle(matImage);

        Utils.matToBitmap(matImage, bitmap);
        imageView = (ImageView) findViewById(R.id.imageViewOMRSheet);
        imageView.setImageBitmap(bitmap);
    }

    public Mat detectCircle(Mat mat){

        Mat matGaussianBlur = new Mat();
//        Imgproc.GaussianBlur(mat, matGaussianBlur, new org.opencv.core.Size(5, 5), 3, 2.5);
        Imgproc.GaussianBlur(mat, matGaussianBlur, new org.opencv.core.Size(9, 9), 2, 2);

        Imgproc.rectangle(mat, new Point(0, 0), new Point(200, 200), new Scalar(0,255,0));
        Imgproc.rectangle(mat, new Point( 760, 0), new Point(960, 200), new Scalar(0,0,255));
        Imgproc.rectangle(mat, new Point( 0, 980), new Point(200, 1180), new Scalar(0,255,0));
        Imgproc.rectangle(mat, new Point( 760, 980), new Point(960, 1180), new Scalar(0,255,0));

        //convert to gray
        Mat matGray = new Mat();
        Imgproc.cvtColor(matGaussianBlur, matGray, Imgproc.COLOR_RGB2GRAY);

        Rect rect[] = new Rect[4];
        rect[0] = new Rect(0, 0, 200, 200);
        rect[1] = new Rect(760, 0, 200, 200);
        rect[2] = new Rect(0, 980, 200, 200);
        rect[3] = new Rect(760, 980, 200, 200);

        Point pt;
        for (int i = 0; i < rect.length; i++) {

            Mat subMat = matGray.submat(rect[i]);
            Mat circles = subMat.clone();

            Mat matThresholded = new Mat();
            Core.inRange(subMat, new Scalar(50,50,50), new Scalar(255,255,255), matThresholded);

            //Detect circles
            //Imgproc.HoughCircles(matThresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 200, 7, 10, 20, 35);
            Imgproc.HoughCircles(matThresholded, circles, Imgproc.CV_HOUGH_GRADIENT, 1, 200, 7, 6, 25, 35);
            double vCircle[] = circles.get(0, 0);

            if (vCircle == null)
                break;

            pt = new Point(Math.round(vCircle[0]), Math.round(vCircle[1]));
            int radius = (int) Math.round(vCircle[2]);
            Log.i("radius", radius+" "+i);

            Imgproc.circle(mat, new Point(pt.x + rect[i].x, pt.y+ rect[i].y), radius, new Scalar(255, 0, 0), 5);
            Imgproc.circle(mat, new Point(pt.x + rect[i].x, pt.y+rect[i].y), 3, new Scalar(0, 0, 255), 5);
        }
        return  mat;
    }
}