package com.delta.app_dev2;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.StrictMode;
import android.provider.MediaStore;

import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import static java.io.FileDescriptor.out;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Scanner;
import java.util.Set;


public class MainActivity extends AppCompatActivity {


    private static final int REQUEST_CODE = 1;

    Uri imgUri;

    int count = 0;
    RelativeLayout relative;


    ImageView mImageView;
    Bitmap bitmap;
    String imgPath;
    ListView list1;
    EditText edit1;
    Button b3;
    EditText edit2;
    int cwidth = 100, cheight = 100;
    PopupWindow popUp;
    LayoutInflater inflater;
    View popupView;
    String store = null;




    File file = new File(Environment.getExternalStorageDirectory() + "/DCIM/" + "app2.txt");
    FileOutputStream stream = new FileOutputStream(file,true);
    //InputStream iis  = this.getAssets().open(Environment.getExternalStorageDirectory() + "/DCIM/" + "app2.txt");
    FileInputStream iis = new FileInputStream(file);
    BufferedReader r = new BufferedReader(new InputStreamReader(iis));


    public MainActivity() throws IOException {
    }


    public class Arun {

        public String name;
        public String img;


        public Arun(String name, String img) {
            this.name = name;
            this.img = img;

        }

    }

    public class ViewHolder {

        public TextView txt;
        public ImageView img;

    }

    private ListView list;
    private ArrayList<Arun> pairs = new ArrayList<Arun>();

    private Activity context;
    AdapterViewCustom adapter;
    Drawable d;


    public class AdapterViewCustom extends BaseAdapter {


        private Activity context_1;
        public ArrayList<Arun> pairs;


        public AdapterViewCustom(Activity context,
                                 ArrayList<Arun> pairs) {
            context_1 = context;
            this.pairs = pairs;
        }

        @Override
        public int getCount() {
            return pairs.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }


        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                convertView = LayoutInflater.from(context_1).inflate(
                        R.layout.custom_row, null);
                viewHolder = new ViewHolder();

                viewHolder.img = (ImageView) convertView
                        .findViewById(R.id.log_img);

                viewHolder.txt = (TextView) convertView
                        .findViewById(R.id.tv_view);

                convertView.setTag(viewHolder);
            } else {

                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.txt.setText(pairs.get(position).name);





            BitmapFactory.Options bmOptions = new BitmapFactory.Options();



            Bitmap bmp = BitmapFactory.decodeFile(pairs.get(position).img);
            viewHolder.img.setImageBitmap(Bitmap.createScaledBitmap(bmp, 480, 480, false));



            return convertView;
        }


    }

    public void add(Arun e) {
        pairs.add(e);
        count++;
    }


    public void popupInit() {
        inflater
                = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        popupView = inflater.inflate(R.layout.popup, null);

        relative.setAlpha(0);
        popUp = new PopupWindow(popupView, RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        popUp.setFocusable(true);
        popUp.setOutsideTouchable(isRestricted());

        Button ok = (Button) popupView.findViewById(R.id.button);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popUp.setContentView(inflater.inflate(R.layout.popup, null, false));
                final EditText cwidth1 = (EditText) popupView.findViewById(R.id.cwidth1);
                final EditText cheight1 = (EditText) popupView.findViewById(R.id.cheight1);

                if (cwidth1.getText().toString().isEmpty() | cheight1.getText().toString().isEmpty()) {


                    Toast.makeText(MainActivity.this, "Empty Pixel Entered", Toast.LENGTH_LONG).show();


                } else {

                    cwidth = Integer.valueOf(cwidth1.getText().toString());
                    cheight = Integer.valueOf(cheight1.getText().toString());
                }


                popUp.dismiss();
                relative.setAlpha(1);
            }
        });


        popUp.showAtLocation(popupView, Gravity.CENTER, 0, 0);
    }


    public void removeLine(final File file, final int lineIndex) throws IOException{
        final List<String> lines = new LinkedList<>();
        final Scanner reader = new Scanner(new FileInputStream(file), "UTF-8");
        while(reader.hasNextLine())
            lines.add(reader.nextLine());
        reader.close();
        assert lineIndex >= 0 && lineIndex <= lines.size() - 1;
        lines.remove(lineIndex);
        final BufferedWriter writer = new BufferedWriter(new FileWriter(file, false));
        for(final String line : lines) {
            writer.write(line);
            writer.write("\n");
        }


        writer.flush();
        writer.close();
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }


    private File createNewFile(String prefix) {
        if (prefix == null || "".equalsIgnoreCase(prefix)) {
            prefix = "IMG_";
        }
        File newDirectory = new File(Environment.getExternalStorageDirectory() + "/DCIM/");

        File file = new File(newDirectory, (prefix + System.currentTimeMillis() + ".jpg"));
        if (file.exists()) {

            file.delete();
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return file;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mImageView = (ImageView) findViewById(R.id.image1);
        edit1 = (EditText) findViewById(R.id.edit1);
        edit2 = (EditText) findViewById(R.id.edit2);


        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());


        list = (ListView) findViewById(R.id.list);
        adapter = new AdapterViewCustom(this, pairs);


        Button b1 = (Button) findViewById(R.id.button1);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                startActivityForResult(intent, REQUEST_CODE);


            }


        });

        Button b2 = (Button) findViewById(R.id.button2);
        b2.setOnClickListener(new View.OnClickListener() {
            public static final int CAPTURE_IMAGE = 0;

            @Override
            public void onClick(View v) {


                final Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent.putExtra(MediaStore.EXTRA_OUTPUT, setImageUri());


                startActivityForResult(intent, CAPTURE_IMAGE);

            }
        });


        b3 = (Button) findViewById(R.id.button3);
        b3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (edit1.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty Option", Toast.LENGTH_LONG).show();
                } else {

                    int l;


                    l = Integer.valueOf(edit1.getText().toString()).shortValue();


                    if (l > 0 && l <= count) {


                        pairs.remove(l - 1);
                        try {
                            removeLine(file,l);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    } else
                        Toast.makeText(MainActivity.this, "Invalid Delete Option", Toast.LENGTH_LONG).show();


                }
            }

        });


        final Button b4 = (Button) findViewById(R.id.button4);
        b4.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {


                if (edit2.getText().toString().isEmpty()) {
                    Toast.makeText(MainActivity.this, "Empty Option", Toast.LENGTH_LONG).show();
                } else {


                    int l;

                    l = Integer.valueOf(edit2.getText().toString());

                    if (l > 0 && l <= count) {


                        relative = (RelativeLayout) findViewById(R.id.relative);

                        popupInit();


                        Bitmap crop;
                        Bitmap cropped;
                        crop = BitmapFactory.decodeFile(pairs.get(l - 1).img);
                        cropped = Bitmap.createBitmap(crop, 0, 0, cwidth, cheight);
                        mImageView.setImageBitmap(cropped);
                        File f = new File(pairs.get(l - 1).img);

                        FileOutputStream out = null;
                        try {
                            out = new FileOutputStream(f);
                            cropped.compress(Bitmap.CompressFormat.PNG, 100, out);

                        } catch (Exception e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                if (out != null) {
                                    out.close();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();

                    } else
                        Toast.makeText(MainActivity.this, "Invalid Crop Position", Toast.LENGTH_LONG).show();


                }

            }
        });



        Arun pq ;
        String line;
        try {
            while((line = r.readLine() )!= null){




                pq = new Arun(line, line);

                add(pq);
                list.setAdapter(adapter);


            }
        } catch (IOException e) {
            e.printStackTrace();
        }













//
    }






    public Uri setImageUri() {

        file = new File(Environment.getExternalStorageDirectory() + "/DCIM/", "image" + new Date().getTime() + ".png");
        Uri imgUri = Uri.fromFile(file);
        imgPath = file.getAbsolutePath();


        return imgUri;
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onRestoreInstanceState(savedInstanceState, persistentState);

        persistentState.get("imgPath");
    }

    public String getImagePath() {
        return imgPath;
    }


    public void refreshlist() {


        Arun p = new Arun(imgPath, imgPath);
        add(p);


        try {

            stream.write(imgPath.getBytes());
            stream.write("\n".getBytes());

        } catch (IOException e) {
            e.printStackTrace();
        }

        list.setAdapter(adapter);



    }


    private void setPic() {

        int targetW = mImageView.getWidth();
        int targetH = mImageView.getHeight();


        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(imgPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;


        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);


        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        bitmap = BitmapFactory.decodeFile(imgPath);


        //mImageView.setImageBitmap(bitmap);

        mImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 360, 520, false));
        d = new BitmapDrawable(this.getResources(), bitmap);
        refreshlist();
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {

            InputStream stream = null;


            if (bitmap != null) {
                bitmap.recycle();
            }
            try {
                stream = getContentResolver().openInputStream(data.getData());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            bitmap = BitmapFactory.decodeStream(stream);

            mImageView.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 360, 520, false));
            //d = new BitmapDrawable(this.getResources(),bitmap);


            Uri tempUri = getImageUri(getApplicationContext(), bitmap);
            File finalFile = new File(getRealPathFromURI(tempUri));


            imgPath = finalFile.getAbsolutePath();


            refreshlist();


        }

        if (requestCode == 0)
            setPic();





    }


}









































































