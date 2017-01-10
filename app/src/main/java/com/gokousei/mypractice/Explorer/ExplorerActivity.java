package com.gokousei.mypractice.Explorer;

import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.gokousei.mypractice.R;
import com.gokousei.mypractice.Explorer.adapter.MyAdapter;
import com.gokousei.mypractice.customview.MyMenu;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;

public class ExplorerActivity extends ListActivity {

    private static final String ROOT_PATH = "/";
    private ArrayList<String> mFileName;
    private ArrayList<String> mFilePath;
    private View view;
    private EditText editText;
    private MyMenu myMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_explorer);
//        myMenu = new MyMenu(this, null, R.drawable.menu_background, "设置",
//                15, R.color.colorAccent, R.color.colorPrimary, R.style.PopupAnimation);
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            showFileDir(Environment.getExternalStorageDirectory().getPath());
//            showFileDir("/sdcard/");
        } else {
            showFileDir(ROOT_PATH);
        }
    }

    private void showFileDir(String path) {
        mFileName = new ArrayList<String>();
        mFilePath = new ArrayList<String>();
        File file = new File(path);
        File[] files = file.listFiles();
        if (!ROOT_PATH.equals(path)) {
            mFileName.add("@1");
            mFilePath.add(ROOT_PATH);
            mFileName.add("@2");
            mFilePath.add(file.getParent());
        }
        for (File f : files) {
            mFileName.add(f.getName());
            mFilePath.add(f.getPath());
        }
        setListAdapter(new MyAdapter(this, mFileName, mFilePath));
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        String patch = mFilePath.get(position);
        File file = new File(patch);
        if (file.exists() && file.canRead()) {
            if (file.isDirectory()) {
                showFileDir(patch);
            } else {
                fileHander(file);
            }
        } else {
            Resources res = getResources();
            new AlertDialog.Builder(this).setTitle("Message").setMessage(res.getString(R.string.no_permission)).setPositiveButton("ok", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //Toast.makeText(getApplicationContext(),"没有权限删除不了、、、",Toast.LENGTH_SHORT).show();
                }
            }).show();
        }
        super.onListItemClick(l, v, position, id);
    }

    private void fileHander(final File file) {
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                LayoutInflater inflater = LayoutInflater.from(ExplorerActivity.this);
                view = inflater.inflate(R.layout.rename_dialg, null);
                editText = (EditText) view.findViewById(R.id.editText);
                switch (which) {
                    case 0:
                        AlertDialog dialog1 = new AlertDialog.Builder(ExplorerActivity.this).create();
                        dialog1.setTitle("创建文件");
                        dialog1.setView(view);
                        dialog1.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String parent = file.getParent().toString();
                                String fileName = editText.getText().toString();
                                if (!fileName.equals("")) {
                                    File createFile = new File(parent + "/" + fileName);
                                    if (!createFile.exists()) {
                                        try {
                                            createFile.createNewFile();
                                            showFileDir(createFile.getParent());
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            displayToast("创建文件出错");
                                        }
                                    } else {
                                        displayToast("文件已存在不创建");
                                    }
                                } else {
                                    displayToast("请输入文件名");
                                }
                            }
                        });
                        dialog1.show();
                        break;
                    case 1:
                        openFile(file);
                        break;
                    case 2:
                        editText.setText(file.getName());
                        DialogInterface.OnClickListener listener1 = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String modifyName = editText.getText().toString();
                                final String patch = file.getParentFile().getPath();
                                final File newFile = new File(patch + "/" + modifyName);
                                if (newFile.exists()) {
                                    if (!modifyName.equals(file.getName())) {
                                        new AlertDialog.Builder(ExplorerActivity.this).setTitle("注意!")
                                                .setMessage("文件名已存在，是否覆盖？").setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                if (file.renameTo(newFile)) {
                                                    showFileDir(patch);
                                                    displayToast("重命名成功");
                                                } else {
                                                    displayToast("重命名失败");
                                                }
                                            }
                                        }).setNegativeButton("取消", null).show();
                                    } else {
                                        if (file.renameTo(newFile)) {
                                            showFileDir(patch);
                                            displayToast("重命名成功");
                                        } else {
                                            displayToast("重命名失败");
                                        }
                                    }
                                } else {
                                    if (file.renameTo(newFile)) {
                                        showFileDir(patch);
                                        displayToast("重命名成功");
                                    } else {
                                        displayToast("重命名失败");
                                    }
                                }
                            }
                        };
                        AlertDialog renameDialog = new AlertDialog.Builder(ExplorerActivity.this).create();
                        renameDialog.setView(view);
                        renameDialog.setButton(DialogInterface.BUTTON_POSITIVE, "确定", listener1);
                        renameDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                            }
                        });
                        renameDialog.show();
                        break;
                    case 3:
                        new AlertDialog.Builder(ExplorerActivity.this)
                                .setTitle("注意!")
                                .setMessage("确定要删除此文件吗？")
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        if (file.delete()) {
                                            //更新文件列表
                                            showFileDir(file.getParent());
                                            displayToast("删除成功！");
                                        } else {
                                            displayToast("删除失败！");
                                        }
                                    }
                                })
                                .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                    }
                                }).show();
                        break;
                }
            }
        };
        String[] menu = {"新建文件", "打开文件", "重命名", "删除文件"};
        new AlertDialog.Builder(ExplorerActivity.this).setTitle("请选择要进行的操作!")
                .setItems(menu, listener)
                .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }

    private void openFile(File file) {
        Intent intent = new Intent();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(Intent.ACTION_VIEW);
        String type = getMIMEType(file);
        intent.setDataAndType(Uri.fromFile(file), type);
        startActivity(intent);
    }

    private String getMIMEType(File file) {
        String type = "";
        String name = file.getName();
        String end = name.substring(name.lastIndexOf(".") + 1, name.length()).toLowerCase();
        if (end.equals("m4a") || end.equals("mp3") || end.equals("wav")) {
            type = "audio";
        } else if (end.equals("mp4") || end.equals("3gp")) {
            type = "video";
        } else if (end.equals("jpg") || end.equals("png") || end.equals("jpeg") || end.equals("bmp") || end.equals("gif")) {
            type = "image";
        } else {
            //如果无法直接打开，跳出列表由用户选择
            type = "*";
        }
        type += "/*";
        return type;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        setIconEnable(menu, true);
        getMenuInflater().inflate(R.menu.menu, menu);
//        MenuInflater inflater = new MenuInflater(getApplicationContext());
//        inflater.inflate(R.menu.menu, menu);
        menu.findItem(R.id.ss);
        menu.add(Menu.NONE, Menu.FIRST + 1, 7, "新建").setIcon(android.R.drawable.ic_menu_send);
        menu.add(Menu.NONE, Menu.FIRST + 2, 3, "取消").setIcon(android.R.drawable.ic_menu_edit);
//        setMenuBackground();
        return true;
//        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case Menu.FIRST + 1: {
                final EditText editText = new EditText(ExplorerActivity.this);
                new AlertDialog.Builder(ExplorerActivity.this).setTitle("请输入文件名称").setView(editText).setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // TODO Auto-generated method stub
                                String mFileName = editText.getText().toString();
                                String IMAGES_PATH = Environment.getExternalStorageDirectory() + "/" + mFileName + "/";       //获取根目录
                                //String IMAGES_PATH = getApplicationContext().getFilesDir().getAbsolutePath() + "/" + mFileName + "/";
                                createMkdir(IMAGES_PATH);
                            }
                        }).setNegativeButton("取消", null).show();
            }
            break;
            case Menu.FIRST + 2:
                Toast.makeText(this, "取消", Toast.LENGTH_LONG).show();
                break;
        }
        return false;
//        return super.onOptionsItemSelected(item);
    }

    public static void createMkdir(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists()) {
            folder.mkdir();
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            finish();
            return true;
        } else {
            return super.onKeyDown(keyCode, event);
        }
    }

//    protected void setMenuBackground() {
//        this.getLayoutInflater().setFactory(
//                new android.view.LayoutInflater.Factory() {
//                    public View onCreateView(String name, Context context, AttributeSet attrs) {
//                        // 指定自定义inflate的对象
//                        if (name.equalsIgnoreCase("com.android.internal.view.menu.IconMenuItemView")) {
//                            try {
//                                LayoutInflater f = getLayoutInflater();
//                                final View view = f.createView(name, null, attrs);
//                                new Handler().post(new Runnable() {
//                                    public void run() {
//                                        // 设置背景图片
//                                        view.setBackgroundResource(R.drawable.menu_background);
//                                    }
//                                });
//                                return view;
//                            } catch (InflateException e) {
//                                e.printStackTrace();
//                            } catch (ClassNotFoundException e) {
//                                e.printStackTrace();
//                            }
//                        }
//                        return null;
//                    }
//                }
//        );
//    }

//    @Override
//    public boolean onMenuOpened(int featureId, Menu menu) {
//        if (myMenu != null) {
//            if (myMenu.isShowing())
//                myMenu.dismiss();
//            else {
//                myMenu.showAtLocation(findViewById(R.id.activity_explorer), Gravity.BOTTOM, 0, 0);
//            }
//        }
//        return false;
////        return super.onMenuOpened(featureId, menu);
//    }

    private void setIconEnable(Menu menu, boolean enable) {
        try {
            Class<?> clazz = Class.forName("com.android.internal.view.menu.MenuBuilder");
            Method m = clazz.getDeclaredMethod("setOptionalIconsVisible", boolean.class);
            m.setAccessible(true);

            //MenuBuilder实现Menu接口，创建菜单时，传进来的menu其实就是MenuBuilder对象(java的多态特征)
            m.invoke(menu, enable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayToast(String message) {
        Toast.makeText(ExplorerActivity.this, message, Toast.LENGTH_SHORT).show();
    }


}