package com.example.pockerguide.map.GPS;

import static com.yandex.runtime.Runtime.getApplicationContext;


import com.example.pockerguide.MainActivity;
import com.example.pockerguide.R;
import com.example.pockerguide.recycler.MainFrag;
import com.yandex.mapkit.map.Cluster;
import com.yandex.mapkit.map.ClusterListener;
import com.yandex.mapkit.map.ClusterTapListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import com.yandex.mapkit.geometry.Point;
import com.yandex.runtime.image.ImageProvider;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Clustering implements ClusterListener, ClusterTapListener {

    private static final float FONT_SIZE = 15;
    private static final float MARGIN_SIZE = 3;
    private static final float STROKE_SIZE = 3;
    public static final int PLACEMARKS_NUMBER = 2000;
    private final List<Point> CLUSTER_CENTERS = Arrays.asList(
            new Point(55.756, 37.618),
            new Point(55.829, 37.744),
            new Point(47.222, 39.720)
    );
    ArrayList<Point> points = new ArrayList<Point>();



    public class TextImageProvider extends ImageProvider {
        @Override
        public String getId() {
            return "text_" + text;
        }

        private final String text;
        @Override
        public Bitmap getImage() {
            DisplayMetrics metrics = new DisplayMetrics();

            MainActivity.manager.getDefaultDisplay().getMetrics(metrics);

            Paint textPaint = new Paint();
            textPaint.setTextSize(FONT_SIZE * metrics.density);
            textPaint.setTextAlign(Align.CENTER);
            textPaint.setStyle(Style.FILL);
            textPaint.setAntiAlias(true);

            float widthF = textPaint.measureText(text);
            FontMetrics textMetrics = textPaint.getFontMetrics();
            float heightF = Math.abs(textMetrics.bottom) + Math.abs(textMetrics.top);
            float textRadius = (float)Math.sqrt(widthF * widthF + heightF * heightF) / 2;
            float internalRadius = textRadius + MARGIN_SIZE * metrics.density;
            float externalRadius = internalRadius + STROKE_SIZE * metrics.density;

            int width = (int) (2 * externalRadius + 0.5);

            Bitmap bitmap = Bitmap.createBitmap(width, width, Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);

            Paint backgroundPaint = new Paint();
            backgroundPaint.setAntiAlias(true);
            backgroundPaint.setColor(Color.RED);
            canvas.drawCircle(width / 2, width / 2, externalRadius, backgroundPaint);

            backgroundPaint.setColor(Color.WHITE);
            canvas.drawCircle(width / 2, width / 2, internalRadius, backgroundPaint);

            canvas.drawText(
                    text,
                    width / 2,
                    width / 2 - (textMetrics.ascent + textMetrics.descent) / 2,
                    textPaint);

            return bitmap;
        }

        public TextImageProvider(String text) {
            this.text = text;
        }
    }
    @Override
    public void onClusterAdded(Cluster cluster) {
        // We setup cluster appearance and tap handler in this method
        cluster.getAppearance().setIcon(
                new TextImageProvider(Integer.toString(cluster.getSize())));
        cluster.addClusterTapListener(this);
    }

    @Override
    public boolean onClusterTap(Cluster cluster) {
        Toast.makeText(
                getApplicationContext(),
                String.valueOf(cluster.getSize()),
                Toast.LENGTH_SHORT).show();
        return true;
    }



    public List<Point> createPoints(int count) {
        if(points.size()>0){

        }else {
            for (int i = 0; i < MainFrag.states_country.size(); ++i) {

                String a = MainFrag.states_country.get(i).getCoords();
                double latitude = Double.parseDouble(a.substring(0, 9));
                double longitude = Double.parseDouble(a.substring(11, 20));

                points.add(new Point(latitude, longitude));
            }
        }
        return points;
    }
}
