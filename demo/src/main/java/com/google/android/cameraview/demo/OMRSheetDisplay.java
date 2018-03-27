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

import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.opencv.android.Utils;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class OMRSheetDisplay extends AppCompatActivity {

    private ImageView imageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_omrsheet_display);

        File file = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"picture.jpg");
        Bitmap bitmap = BitmapFactory.decodeFile(file.getPath());

        Mat matImage = new Mat(4160, 3120, CvType.CV_8UC4);
        Utils.bitmapToMat(bitmap,matImage);

        matImage = findROIofOMR(matImage,960, 1280,3120,4160);

        Utils.matToBitmap(matImage, bitmap);
        imageView = (ImageView) findViewById(R.id.imageViewOMRSheet);
        GlideApp.with(OMRSheetDisplay.this)
                .load(bitmap)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(imageView);
    }

    public Mat findROIofOMR(Mat mat, int previewWidth, int previewHeight, int pictureWidth, int pictureHeight){

        Point ptPreviewPoints[], ptPicturePoints[];

        Mat outputMat = new Mat(pictureWidth, pictureHeight, CvType.CV_8UC4);

        List<Point> src = new ArrayList<>();

        ptPreviewPoints = getPreviewPoints();
        src.add(ptPreviewPoints[0]);
        src.add(ptPreviewPoints[1]);
        src.add(ptPreviewPoints[2]);
        src.add(ptPreviewPoints[3]);


        Imgproc.circle(mat, ptPreviewPoints[0], 100, new Scalar(0,255,0),20);
        Imgproc.circle(mat, ptPreviewPoints[1], 100, new Scalar(0,255,0),20);
        Imgproc.circle(mat, ptPreviewPoints[2], 100, new Scalar(0,255,0),20);
        Imgproc.circle(mat, ptPreviewPoints[3], 100, new Scalar(0,255,0),20);

        Mat startM = Converters.vector_Point2f_to_Mat(src);

        List<Point> dest = new ArrayList<>();

        ptPicturePoints = getPicturePoints(pictureWidth, pictureHeight);
        dest.add(ptPicturePoints[0]);
        dest.add(ptPicturePoints[1]);
        dest.add(ptPicturePoints[2]);
        dest.add(ptPicturePoints[3]);

        Mat endM = Converters.vector_Point2f_to_Mat(dest);

        Mat perspectiveTransform = Imgproc.getPerspectiveTransform(startM, endM);

        Imgproc.warpPerspective(mat, outputMat, perspectiveTransform, new Size(pictureWidth, pictureHeight));

        //return outputMat;
        return mat;
    }

    public Point[] getPreviewPoints(){
        double[] first = getIntent().getDoubleArrayExtra("first");
        double[] second = getIntent().getDoubleArrayExtra("second");
        double[] third = getIntent().getDoubleArrayExtra("third");
        double[] fourth = getIntent().getDoubleArrayExtra("fourth");

        Point pt[] = new Point[4];
        double constant = 3.25;
        pt[0] = new Point(first[0]*constant, first[1]*constant);
        pt[1] = new Point(second[0]*constant, second[1]*constant);
        pt[2] = new Point(third[0]*constant, third[1]*constant);
        pt[3] = new Point(fourth[0]*constant, fourth[1]*constant);
        return pt;
    }

    public Point[] getPicturePoints(int pictureWidth, int pictureHeight){
        Point pt[] = new Point[4];
        pt[0] = new Point(0, 0);
        pt[1] = new Point(pictureWidth, 0);
        pt[2] = new Point(pictureWidth, pictureHeight);
        pt[3] = new Point(0, pictureHeight);
        return pt;
    }
}