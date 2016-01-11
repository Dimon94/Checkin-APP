package com.ak47.checkin_app.camera;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Typeface;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.Layout;
import android.text.StaticLayout;
import android.text.TextPaint;
import android.util.Base64;
import android.view.Display;
import android.view.WindowManager;

import com.ak47.checkin_app.R;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by desmond on 24/10/14.
 */
public class ImageUtility {

    public static String convertBitmapToString(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 80, out);

        return Base64.encodeToString(out.toByteArray(), Base64.DEFAULT);
    }

    public static byte[] convertBitmapStringToByteArray(String bitmapByteString) {
        return Base64.decode(bitmapByteString, Base64.DEFAULT);
    }

    public static Bitmap rotatePicture(Context context, int rotation, byte[] data) {
        Bitmap bitmap = decodeSampledBitmapFromByte(context, data);

        if (rotation != 0) {
            Bitmap oldBitmap = bitmap;

            Matrix matrix = new Matrix();
            matrix.postRotate(rotation);

            bitmap = Bitmap.createBitmap(
                    oldBitmap, 0, 0, oldBitmap.getWidth(), oldBitmap.getHeight(), matrix, false
            );

            oldBitmap.recycle();
        }

        return bitmap;
    }

    public static Uri savePicture(Context context, Bitmap bitmap) {
        int cropHeight;
        if (bitmap.getHeight() > bitmap.getWidth()) cropHeight = bitmap.getWidth();
        else cropHeight = bitmap.getHeight();

        bitmap = ThumbnailUtils.extractThumbnail(bitmap, cropHeight, cropHeight, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);

        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                context.getString(R.string.app_name)
        );

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = new File(
                mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg"
        );


        // Saving the bitmap
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);

            FileOutputStream stream = new FileOutputStream(mediaFile);
            stream.write(out.toByteArray());
            stream.close();

        } catch (IOException exception) {
            exception.printStackTrace();
        }

        // Mediascanner need to scan for the image saved
        Intent mediaScannerIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri fileContentUri = Uri.fromFile(mediaFile);
        mediaScannerIntent.setData(fileContentUri);
        context.sendBroadcast(mediaScannerIntent);

        return fileContentUri;
    }

    public static Bitmap decodeSampledBitmapFromPath(String path, int reqWidth, int reqHeight) {
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeFile(path, options);

        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        options.inJustDecodeBounds = false;
        options.inPurgeable = true;
        options.inInputShareable = true;

        return BitmapFactory.decodeFile(path, options);
    }

    //获取图片缩小的图片
    public static Bitmap scaleBitmap(String src, int max) {
        //获取图片的高和宽
        BitmapFactory.Options options = new BitmapFactory.Options();
        //这一个设置使 BitmapFactory.decodeFile获得的图片是空的,但是会将图片信息写到options中
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(src, options);
        // 计算比例 为了提高精度,本来是要640 这里缩为64
        max = max / 10;
        int be = options.outWidth / max;
        if (be % 10 != 0)
            be += 10;
        be = be / 10;
        if (be <= 0)
            be = 1;
        options.inSampleSize = be;
        //设置可以获取数据
        options.inJustDecodeBounds = false;
        //获取图片
        return BitmapFactory.decodeFile(src, options);
    }

    // 加水印 也可以加文字
    public static Bitmap watermarkBitmap(Bitmap src, Bitmap watermark,
                                         String title, int densityDpi) {

        int dpi = densityDpi;
        //读取原图片信息

        if (src == null) {
            return null;
        }
        int w = src.getWidth();
        int h = src.getHeight();
        Bitmap tarBitmap = src.copy(Bitmap.Config.ARGB_8888, true);
        //Bitmap tarBitmap= Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);// 创建一个新的和SRC长度宽度一样的位图
        Canvas canvas = new Canvas(tarBitmap);
        canvas.drawBitmap(src, 0, 0, null);// 在 0，0坐标开始画入src
        Paint paint = new Paint();
        //加入图片
        if (watermark != null) {
            int ww = watermark.getWidth();
            int wh = watermark.getHeight();
            paint.setAlpha(50);
            canvas.drawBitmap(watermark, w - ww + 5, h - wh + 5, paint);// 在src的右下角画入水印
        }
        //加入文字
        if (title != null) {
            String familyName = "宋体";
            Typeface font = Typeface.create(familyName, Typeface.BOLD);
            TextPaint textPaint = new TextPaint();
            textPaint.setColor(Color.RED);
            textPaint.setTypeface(font);
            textPaint.setTextSize(50);
            if (dpi <= 120) {//qvga 240X400
                textPaint.setTextSize(5);
            } else if (dpi <= 160) {//hvga 320X480
                textPaint.setTextSize(8);
            } else if (dpi <= 240) {//wvga 480X800
                textPaint.setTextSize(10);
                if (w > 700) {

                    textPaint.setTextSize(15);
                }
            } else if (dpi <= 320) {// 1280*720

                textPaint.setTextSize(15);
                if (w > 2000) {

                    textPaint.setTextSize(72);
                }

            } else { //更大屏幕分辨率

                textPaint.setTextSize(72);
            }

            //这里是自动换行的
            StaticLayout layout = new StaticLayout(title, textPaint, w, Layout.Alignment.ALIGN_NORMAL, 1.0F, 0.0F, true);
            layout.draw(canvas);
            //文字就加左上角算了
            canvas.drawText(title, (float) (w * 0.10), (float) (h * 0.20), textPaint);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);// 保存
        canvas.restore();// 存储
        return tarBitmap;
    }

    /**
     * Decode and sample down a bitmap from a byte stream
     */
    public static Bitmap decodeSampledBitmapFromByte(Context context, byte[] bitmapBytes) {
        Display display = ((WindowManager) context.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();

        int reqWidth, reqHeight;
        Point point = new Point();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            display.getSize(point);
            reqWidth = point.x;
            reqHeight = point.y;
        } else {
            reqWidth = display.getWidth();
            reqHeight = display.getHeight();
        }


        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        options.inMutable = true;
        options.inBitmap = BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false; // If set to true, the decoder will return null (no bitmap), but the out... fields will still be set, allowing the caller to query the bitmap without having to allocate the memory for its pixels.
        options.inPurgeable = true;         // Tell to gc that whether it needs free memory, the Bitmap can be cleared
        options.inInputShareable = true;    // Which kind of reference will be used to recover the Bitmap data after being clear, when it will be used in the future

        return BitmapFactory.decodeByteArray(bitmapBytes, 0, bitmapBytes.length, options);
    }

    /**
     * Calculate an inSampleSize for use in a {@link BitmapFactory.Options} object when decoding
     * bitmaps using the decode* methods from {@link BitmapFactory}. This implementation calculates
     * the closest inSampleSize that is a power of 2 and will result in the final decoded bitmap
     * having a width and height equal to or larger than the requested width and height
     * <p/>
     * The function rounds up the sample size to a power of 2 or multiple
     * of 8 because BitmapFactory only honors sample size this way.
     * For example, BitmapFactory downsamples an image by 2 even though the
     * request is 3. So we round up the sample size to avoid OOM.
     */
    public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        int initialInSampleSize = computeInitialSampleSize(options, reqWidth, reqHeight);

        int roundedInSampleSize;
        if (initialInSampleSize <= 8) {
            roundedInSampleSize = 1;
            while (roundedInSampleSize < initialInSampleSize) {
                // Shift one bit to left
                roundedInSampleSize <<= 1;
            }
        } else {
            roundedInSampleSize = (initialInSampleSize + 7) / 8 * 8;
        }

        return roundedInSampleSize;
    }

    private static int computeInitialSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final double height = options.outHeight;
        final double width = options.outWidth;

        final long maxNumOfPixels = reqWidth * reqHeight;
        final int minSideLength = Math.min(reqHeight, reqWidth);

        int lowerBound = (maxNumOfPixels < 0) ? 1 :
                (int) Math.ceil(Math.sqrt(width * height / maxNumOfPixels));
        int upperBound = (minSideLength < 0) ? 128 :
                (int) Math.min(Math.floor(width / minSideLength),
                        Math.floor(height / minSideLength));

        if (upperBound < lowerBound) {
            // return the larger one when there is no overlapping zone.
            return lowerBound;
        }

        if (maxNumOfPixels < 0 && minSideLength < 0) {
            return 1;
        } else if (minSideLength < 0) {
            return lowerBound;
        } else {
            return upperBound;
        }
    }

}
