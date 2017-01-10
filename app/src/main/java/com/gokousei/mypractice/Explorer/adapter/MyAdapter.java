package com.gokousei.mypractice.Explorer.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.gokousei.mypractice.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by z on 2016/10/13.
 */

public class MyAdapter extends BaseAdapter {
    private LayoutInflater inflater;
    private ArrayList<String> mFileName;
    private ArrayList<String> mFilePath;
    private Bitmap directory, file;

    public MyAdapter(Context context, ArrayList<String> fm, ArrayList<String> fn) {
        mFileName = fm;
        mFilePath = fn;
        directory = BitmapFactory.decodeResource(context.getResources(), android.R.mipmap.sym_def_app_icon);
        file = BitmapFactory.decodeResource(context.getResources(), android.R.mipmap.sym_def_app_icon);
        directory = small(directory, 0.16f);
        file = small(file, 0.1f);
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mFileName.size();
    }

    @Override
    public Object getItem(int position) {
        return mFileName.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.item, null);
            holder.textView = (TextView) convertView.findViewById(R.id.textView);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        File f = new File(mFilePath.get(position).toString());
        if (mFileName.get(position).equals("@1")) {
            holder.textView.setText("/");
            holder.imageView.setImageBitmap(directory);
        } else if (mFileName.get(position).equals("@2")) {
            holder.textView.setText("..");
            holder.imageView.setImageBitmap(directory);
        } else {
            holder.textView.setText(f.getName());
            if (f.isDirectory()) {
                holder.imageView.setImageBitmap(directory);
            } else if (f.isFile()) {
                holder.imageView.setImageBitmap(file);
            } else {
                System.out.println(f.getName());
            }
        }
        return convertView;
    }

    private Bitmap small(Bitmap map, float num) {
        Matrix matrix = new Matrix();
        matrix.postScale(num, num);
        return Bitmap.createBitmap(map, 0, 0, map.getWidth(), map.getHeight(), matrix, true);
    }

    private class ViewHolder {
        private TextView textView;
        private ImageView imageView;
    }
}
