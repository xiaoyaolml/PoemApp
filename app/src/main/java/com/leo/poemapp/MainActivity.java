package com.leo.poemapp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.leo.poemapp.dao.PoemDao;
import com.leo.poemapp.model.Poem;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.util.List;

import static com.leo.poemapp.Constant.DB_PATH;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private PoemDao poemDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        poemDao = ((App)getApplication()).getDaoSession().getPoemDao();
        final TextView textView = (TextView) findViewById(R.id.main_show);
        final EditText editText = (EditText) findViewById(R.id.main_edit);

        findViewById(R.id.main_insert).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Poem poem = new Poem();
                poem.setCount(1);
                poem.setFirst("天");
                poem.setSecond("日");
                poem.setType(1);
                poemDao.insert(poem);
                Log.d(TAG,"insert 1 record successfully");
                textView.setText("insert 1 record successfully");
            }
        });

        findViewById(R.id.main_query_all).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Poem> poemList = poemDao.loadAll();
                StringBuilder result = new StringBuilder();
                for (Poem poem:poemList) {
                    result.append(poem.getFirst()+":"+poem.getSecond()+":"+poem.getCount()+"\n");
                }
                Log.d(TAG, result.toString());
                textView.setText(result);
            }
        });

        findViewById(R.id.main_export).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (saveDataToExternalFile("poems.db")) {
                    Toast.makeText(MainActivity.this, "导出成功！", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "导出失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        findViewById(R.id.main_import).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                copyDataToDatabase();
                Toast.makeText(MainActivity.this, "导入成功，需要重启APP！", Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.main_query).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String result;
                String input = editText.getText().toString();
                if (TextUtils.isEmpty(input)) {
                    Toast.makeText(MainActivity.this, "内容不能为空！", Toast.LENGTH_SHORT).show();
                    return;
                }

                List<Poem> poemList = poemDao.queryBuilder()
                        .whereOr(PoemDao.Properties.First.eq(input), PoemDao.Properties.Second.eq(input))
                        .list();
                if (poemList.size() > 0) {
                    Poem poem = poemList.get(0);
                    if (poem.getFirst().equals(input)) {
                        result = poem.getSecond();
                    } else {
                        result = poem.getFirst();
                    }
                } else {
                    result = "无结果";
                }
                textView.setText(result);
            }
        });

    }

    private boolean saveDataToExternalFile(String filename) {
        File dir = new File(Constant.APP_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File externalFile = new File(dir, filename);
        File dbFile = new File(Environment.getDataDirectory(), DB_PATH);
        Log.d(TAG, "数据读取路径：" + dbFile.getAbsolutePath());
        if (dbFile.exists()) {
            try {
                FileChannel src = new FileInputStream(dbFile).getChannel();
                FileChannel dst = new FileOutputStream(externalFile).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                Log.d(TAG, "数据备份路径：" + externalFile.getAbsolutePath());
                return true;
            } catch (java.io.IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private void copyDataToDatabase() {
        File dir = new File(Constant.DB_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File dbFile = new File(Environment.getDataDirectory(), DB_PATH);
        if (dbFile.exists()) {
            dbFile.delete();
        }
        try {
            InputStream inputStream = getApplicationContext().getAssets().open(Constant.DB_NAME);
            OutputStream outputStream = new FileOutputStream(dbFile.getAbsolutePath());
            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }
            outputStream.flush();
            outputStream.close();
            inputStream.close();
            Log.d(TAG, "完成数据库导入！");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
